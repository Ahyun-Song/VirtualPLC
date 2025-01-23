package com.virtualplc;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import com.google.gson.JsonObject; // Gson 라이브러리를 사용

public class VirtualPLC {
	private Random random;

	// 슬러리 공급 공정 변수
	private double slurrySupplyRate;
	private double slurryVolume;
	private double slurryTemperature;

	// 코팅 공정 변수
	private double coatingSpeed;
	private double coatingThickness;

	// 건조 공정 변수
	private double dryingTemperature;

	// 초기값 설정
	public VirtualPLC() {
		random = new Random();
		slurrySupplyRate = 12; // 초기값 (12 mL/min)
		slurryVolume = 100; // 초기 슬러리 용량 (100L)
		slurryTemperature = random.nextDouble() * 10 + 20; // 20~30°C 랜덤

		coatingSpeed = random.nextDouble() * 0.7 + 0.8; // 0.8~1.5 m/min 랜덤
		coatingThickness = random.nextDouble() * 4 + 8; // 8~12 µm 랜덤

		dryingTemperature = random.nextDouble() * 20 + 80; // 80~100°C 랜덤
	}

	public void updateProcesses() {
		// 슬러리 공급 공정 업데이트
		slurrySupplyRate += (random.nextDouble() * 0.4 - 0.2); // -0.2~0.2
		slurrySupplyRate = Math.max(10, Math.min(15, slurrySupplyRate)); // 10~15 제한
		slurryVolume -= slurrySupplyRate / 60.0; // 공급 속도에 따른 감소
		slurryVolume = Math.max(0, slurryVolume);

		// 슬러리 온도 업데이트
		slurryTemperature += (random.nextDouble() * 0.4 - 0.2);
		slurryTemperature = Math.max(20, Math.min(30, slurryTemperature));

		// 코팅 공정 업데이트
		coatingSpeed = random.nextDouble() * 0.7 + 0.8; // 0.8~1.5 m/min
		coatingThickness = random.nextDouble() * 4 + 8; // 8~12 µm

		// 건조 공정 업데이트
		dryingTemperature = random.nextDouble() * 20 + 80; // 80~100°C
	}

	public void displayStatus() {
		System.out.println("=== 현재 상태 ===");
		System.out.printf("슬러리 공급 속도: %.2f mL/min\n", slurrySupplyRate);
		System.out.printf("슬러리 용량: %.2f L\n", slurryVolume);
		System.out.printf("슬러리 온도: %.2f °C\n", slurryTemperature);
		System.out.printf("코팅 속도: %.2f m/min\n", coatingSpeed);
		System.out.printf("코팅 두께: %.2f µm\n", coatingThickness);
		System.out.printf("건조 온도: %.2f °C\n", dryingTemperature);
		System.out.println("=====================");
	}

	// JSON 데이터 생성 메서드
	public String generateFormattedJsonData() {
		JsonObject root = new JsonObject();

		// 현재 시간 가져오기 (현지 시간대 기준)
		String timestamp = java.time.LocalDateTime.now().toString();

		// 슬러리 탱크 데이터
		JsonObject slurryTank = new JsonObject();
		slurryTank.addProperty("Timestamp", timestamp);
		slurryTank.addProperty("SupplySpeed", slurrySupplyRate);
		slurryTank.addProperty("RemainingVolume", slurryVolume);
		slurryTank.addProperty("Temperature", slurryTemperature);
		root.add("SlurryTank", slurryTank);

		// 코팅 공정 데이터
		JsonObject coatingProcess = new JsonObject();
		coatingProcess.addProperty("Timestamp", timestamp);
		coatingProcess.addProperty("Speed", coatingSpeed);
		coatingProcess.addProperty("Thickness", coatingThickness);
		root.add("CoatingProcess", coatingProcess);

		// 건조 공정 데이터
		JsonObject dryingProcess = new JsonObject();
		dryingProcess.addProperty("Timestamp", timestamp);
		dryingProcess.addProperty("Temperature", dryingTemperature);
		root.add("DryingProcess", dryingProcess);

		// 들여쓰기와 정렬된 JSON 문자열 생성
		return new com.google.gson.GsonBuilder()
			.setPrettyPrinting() // 들여쓰기 설정
			.create()
			.toJson(root);
}

	public void sendDataToServer(String serverIp, int port) {
		while (true) {
			try (Socket socket = new Socket(serverIp, port)) {
				System.out.println("Connected to server: " + serverIp + ":" + port);

				OutputStream outputStream = socket.getOutputStream();

				while (true) { // 지속적으로 데이터 전송
					String jsonData = generateFormattedJsonData();
					byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);

					// 메시지 길이 (4바이트)와 데이터 결합
					byte[] lengthPrefix = java.nio.ByteBuffer.allocate(4).putInt(jsonDataBytes.length).array();

					// 길이 정보와 실제 데이터를 전송
					outputStream.write(lengthPrefix);
					outputStream.write(jsonDataBytes);

					System.out.println("Generated JSON Data:");
					System.out.println(jsonData); // 콘솔에 JSON 데이터 출력
					System.out.println("Sent JSON Data to server with length prefix.");

					Thread.sleep(1000); // 1초 대기
				}
			} catch (Exception ex) {
				System.err.println("Connection lost or failed: " + ex.getMessage());
				System.out.println("Retrying connection in 5 seconds...");
				try {
					Thread.sleep(5000); // 5초 대기 후 재연결 시도
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* 테스트 코드 Getter 메서드 */
	// 슬러리 용량 반환 메서드
	public double getSlurryVolume() {
		return slurryVolume;
	}

	// 슬러리 공급 속도 반환 메서드
	public double getSlurrySupplyRate() {
		return slurrySupplyRate;
	}

	// 슬러리 온도 반환 메서드
	public double getSlurryTemperature() {
		return slurryTemperature;
	}

	// 코팅 속도 반환 메서드
	public double getCoatingSpeed() {
		return coatingSpeed;
	}

	// 코팅 두께 반환 메서드
	public double getCoatingThickness() {
		return coatingThickness;
	}

	// 건조 온도 반환 메서드
	public double getDryingTemperature() {
		return dryingTemperature;
	}

	public static void main(String[] args) {
		VirtualPLC plc = new VirtualPLC();

		// 서버 IP와 포트 설정
		String serverIp = "192.168.0.130"; // 아현 IP
		String serverIp2 = "192.168.1.173"; // 창헌
		int dbPort = 8081; // DB 저장용 포트
		int wpfPort = 8080; // WPF 서버 포트

		// 데이터 전송 스레드 생성
		Thread dbThread = new Thread(() -> {
			plc.sendDataToServer(serverIp, dbPort); // DB 저장용 서버로 JSON 데이터 전송
		});

		Thread wpfThread = new Thread(() -> {
			plc.sendDataToServer(serverIp2, wpfPort); // WPF 서버로 JSON 데이터 전송
		});

		// 데이터 전송 스레드 시작
		dbThread.start();
		wpfThread.start();

		// 상태 업데이트와 데이터 출력 반복 실행
		while (true) {
			long startTime = System.currentTimeMillis(); // 시작 시간 기록

			plc.updateProcesses(); // 공정 상태 업데이트
			plc.displayStatus(); // 상태 출력

			// 경과 시간 계산
			long elapsedTime = System.currentTimeMillis() - startTime;

			// 남은 시간 계산
			long sleepTime = 1000 - elapsedTime;

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime); // 남은 시간만큼 대기
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// 처리 시간이 1초를 초과한 경우 경고 메시지 출력
				System.out.println("Warning: Processing took longer than 1 second.");
			}
		}
	}
}