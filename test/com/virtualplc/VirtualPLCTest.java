package com.virtualplc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VirtualPLCTest {

	@Test
	public void testSlurryVolumeNeverNegative() {
		VirtualPLC plc = new VirtualPLC();

		for (int i = 0; i < 1000; i++) {
			plc.updateProcesses();
			plc.displayStatus(); // 현재 상태 출력
			double volume = plc.getSlurryVolume();
			assertTrue(volume >= 0, "슬러리 용량이 음수가 되었습니다: " + volume);
		}
	}

	@Test
	public void testSlurrySupplyRateRange() {
		VirtualPLC plc = new VirtualPLC();

		for (int i = 0; i < 1000; i++) {
			plc.updateProcesses();
			double rate = plc.getSlurrySupplyRate();
			assertTrue(rate >= 10 && rate <= 15,
				"슬러리 공급 속도가 범위를 벗어났습니다: " + rate);
		}
	}

	@Test
	public void testSlurryTemperatureRange() {
		VirtualPLC plc = new VirtualPLC();

		for (int i = 0; i < 1000; i++) {
			plc.updateProcesses();
			double temp = plc.getSlurryTemperature();
			assertTrue(temp >= 20 && temp <= 30,
				"슬러리 온도가 범위를 벗어났습니다: " + temp);
		}
	}

	@Test
	public void testCoatingSpeedRange() {
		VirtualPLC plc = new VirtualPLC();

		for (int i = 0; i < 1000; i++) {
			plc.updateProcesses();
			double speed = plc.getCoatingSpeed();
			assertTrue(speed >= 0.8 && speed <= 1.5,
				"코팅 속도가 범위를 벗어났습니다: " + speed);
		}
	}

	@Test
	public void testCoatingThicknessRange() {
		VirtualPLC plc = new VirtualPLC();

		for (int i = 0; i < 1000; i++) {
			plc.updateProcesses();
			double thickness = plc.getCoatingThickness();
			assertTrue(thickness >= 8 && thickness <= 12,
				"코팅 두께가 범위를 벗어났습니다: " + thickness);
		}
	}

	@Test
	public void testDryingTemperatureRange() {
		VirtualPLC plc = new VirtualPLC();

		for (int i = 0; i < 1000; i++) {
			plc.updateProcesses();
			double temp = plc.getDryingTemperature();
			assertTrue(temp >= 80 && temp <= 100,
				"건조 온도가 범위를 벗어났습니다: " + temp);
		}
	}
	
	@Test
	public void testSendDataToServer() {
		VirtualPLC plc = new VirtualPLC();
		plc.sendDataToServer("192.168.1.173", 8080); // WPF 서버 IP와 포트
	}
}
