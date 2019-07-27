package com.biometric;

import java.nio.file.Paths;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.biometric.DBConnection;
import com.mintutiae.image.MintutiaeAlgo;

import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;

@MultipartConfig(maxFileSize = 16177215)

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.setProperty("java.class.path", "D:\\sqljdbc4-2.0.jar");
		String email = request.getParameter("mailid");
		Part filePart = request.getPart("filepath");
        String path = Paths.get(filePart.getSubmittedFileName()).toAbsolutePath().toString();
		System.out.println("fdkjfkjdsfjdshfkjdhf");
		System.out.println(path);
		try {
			Connection conn = null;
			System.out.println("check");
			conn = DBConnection.getConnection();
			System.out.println("check");
			PreparedStatement ps = conn.prepareStatement("select email, imagepath from registernewuser");
			ps.setString(1, email);
			ps.setString(2, path);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String file1 =path;
		    	String file2 = rs.getString("path");
		    	boolean status = MintutiaeAlgo.test(file1, file2);
		    	if (status) {
					response.sendRedirect("Logout.jsp");
					return;
		    	}
		    	else {
		    		response.sendRedirect("error.html");
					return;
		    	}
			}
			response.sendRedirect("error.html");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}