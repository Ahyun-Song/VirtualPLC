package com.virtualplc;

import java.util.Random;

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
		
	public VirtualPLC() {
		random = new Random();
		slurrySupplyRate = 12; // 초기값 (12 mL/min)
		slurryVolume = 100;   // 초기 슬러리 용량 (100L)
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
		if (slurryVolume < 0) {
			System.out.println("슬러리 용량이 음수로 감소하려고 했습니다. 0으로 고정합니다.");
		}
		slurryVolume = Math.max(0, slurryVolume);

		// 슬러리 온도 업데이트
		slurryTemperature += (random.nextDouble() * 0.4 - 0.2);
		if (slurryTemperature < 20 || slurryTemperature > 30) {
			System.out.println("경고: 슬러리 온도가 범위를 벗어나려고 합니다!");
		}
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

	public static void main(String[] args) {
		VirtualPLC plc = new VirtualPLC();

		while (true) {
			plc.updateProcesses();
			plc.displayStatus();

			try {
				Thread.sleep(1000); // 1초 대기
			} catch (InterruptedException e) {
				e.printStackTrace();
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

}
