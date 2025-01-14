package com.virtualplc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	// MSSQL 연결 정보
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=VirtualPLCDB;integratedSecurity=true;trustServerCertificate=true;";

	// 데이터베이스 연결 메서드
	public static Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}

	// 연결 테스트
	public static void main(String[] args) {
		try (Connection conn = connect()) {
			System.out.println("MSSQL 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
	