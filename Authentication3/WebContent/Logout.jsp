<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@page import="com.biometric.DBConnection"%>
<%@page import="com.biometric.AES" %>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<!DOCTYPE html>
<html>

<head>

<title>Logout Page</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width">

</head>
<%
Connection conn = null;
Statement statement = null;
ResultSet resultSet = null;
conn = DBConnection.getConnection();
try{ 
statement=conn.createStatement();
String sql ="SELECT * FROM registernewuser where email =" + "\'" + session.getAttribute("email")+ "\'" ;
System.out.println(sql);
System.out.println(session.getAttribute("email"));
resultSet = statement.executeQuery(sql);
while(resultSet.next()){
%>
<body>
<form method="link" action="logoutprocess.jsp">


<div align="right">
<input type="submit" value="Logout">
</div>

<h5>
Hello
<%=resultSet.getString("username") %></h5>
<h5>
AES key:
<%=resultSet.getBytes("aeskey") %></h5>
<h5>
Encrypted Message :
<%=resultSet.getString("comment") %></h5>
<h5>
Your Message :
<%=AES.AESdecrypt(resultSet.getBytes("aeskey"), resultSet.getString("comment")) %></h5>

<% 
}

} catch (Exception e) {
e.printStackTrace();
}
%>
</form>
</body>
</html>