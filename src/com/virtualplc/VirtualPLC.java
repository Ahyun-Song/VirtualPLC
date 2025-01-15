package com.virtualplc;

import java.io.OutputStream;
import java.io.PrintWriter;
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
		slurryVolume = 100;	// 초기 슬러리 용량 (100L)
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
	public String generateJsonData() {
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("slurrySupplyRate", slurrySupplyRate);
		jsonData.addProperty("slurryVolume", slurryVolume);
		jsonData.addProperty("slurryTemperature", slurryTemperature);
		jsonData.addProperty("coatingSpeed", coatingSpeed);
		jsonData.addProperty("coatingThickness", coatingThickness);
		jsonData.addProperty("dryingTemperature", dryingTemperature);
		return jsonData.toString(); // JSON 문자열 반환
	}

	// TCP 클라이언트를 사용해 JSON 데이터 전송
	public void sendDataToServer(String serverIp, int port) {
		String jsonData = generateJsonData(); // JSON 데이터 생성
		try (Socket socket = new Socket(serverIp, port)) {
			System.out.println("Connected to server: " + serverIp + ":" + port);

			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true, StandardCharsets.UTF_8); // UTF-8로 전송
			writer.println(jsonData); // JSON 데이터 전송

			System.out.println("Sent JSON Data: " + jsonData);
		} catch (Exception ex) {
			System.err.println("Failed to send data to server: " + ex.getMessage());
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
		String serverIp = "192.168.1.173"; // WPF 서버 IP
		int serverPort = 8080;			// WPF 서버 포트

		// 상태 업데이트와 데이터 전송 반복 실행
		while (true) {
			plc.updateProcesses();		// 공정 상태 업데이트
			plc.displayStatus();		 // 현재 상태 출력
			plc.sendDataToServer(serverIp, serverPort); // 서버로 JSON 데이터 전송

			try {
				Thread.sleep(1000); // 1초 대기
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
