<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.biometric.DBConnection"%>
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
%>

<form method="link" action="logoutprocess.jsp">

	<body>
        <div align="right">
            <input type="submit" value="Logout" >
        </div>
       
<%
try{ 
statement=conn.createStatement();
String sql ="SELECT * FROM Register_table where email_id =" + session.getAttribute("email_id");

resultSet = statement.executeQuery(sql);
while(resultSet.next()){
%>

<h5>Hello <%=resultSet.getString("username") %></h5>
<h5>Your Message : <%=resultSet.getString("comment") %></h5>

<% 
}

} catch (Exception e) {
e.printStackTrace();
}
%>
</body>
</html>