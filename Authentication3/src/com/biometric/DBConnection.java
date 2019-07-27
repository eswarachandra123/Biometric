package com.biometric;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static Connection con;

	public static Connection getConnection() throws SQLException {
		String connectionUrl =
				"jdbc:sqlserver://LAPTOP-LK2TKS62:1566;"
                +"database=learning;"
                + "user=sa;"
                + "password=hello;"
               // + "encrypt=true;"
                +"trustServerCertificate=false";
		try {
			
			con = DriverManager.getConnection(connectionUrl);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	//http://localhost:8086/Authentication3/
	public static void main(String args[]) throws SQLException {
		Connection con = getConnection();
		System.out.println(con);

	}
}