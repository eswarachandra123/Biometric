package com.biometric;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.sql.Connection;
import com.biometric.DBConnection;
import com.mintutiae.image.*;

@MultipartConfig(maxFileSize = 16177215)

public class RegisterServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String comment = request.getParameter("comment");
		MyResult result = AES.AESencrypt(comment);
		Part filePart = request.getPart("fileName");
		String path = Paths.get(filePart.getSubmittedFileName()).toAbsolutePath().toString();
		System.out.println(path.replace('\\','/'));
		boolean status = MintutiaeAlgo.checkimage(path.replace('\\','/'));
		if (status == false) {
			response.sendRedirect("error.html");
		} else {
			System.out.println(status);
//		String absolute = Paths.get(filePart.getSubmittedFileName()).normalize().toString();
			System.out.println(path);
			// System.out.println(absolute);
			String message = null;
			Connection conn = null;
			try {
				conn = DBConnection.getConnection();
				String sql = "INSERT INTO registernewuser (username, email, imagepath, comment, aeskey) values (?, ?, ?, ?, ?)";
				PreparedStatement pstmtSave = conn.prepareStatement(sql);
				pstmtSave.setString(1, username);
				pstmtSave.setString(2, email);
				pstmtSave.setString(3, path);
				pstmtSave.setString(4, result.getmessage());
				pstmtSave.setBytes(5, result.getskey());
				int row = pstmtSave.executeUpdate();
				if (row > 0) {
					message = "Register successfully please login for accessing data";
				}
			} catch (SQLException ex) {
				message = "ERROR: " + ex.getMessage();
				ex.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				System.out.println(message);
				request.setAttribute("message", message);
				getServletContext().getRequestDispatcher("/confirm.jsp").include(request, response);
			}
		}
	}
}
