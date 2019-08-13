<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html><body>
 <h1><font color="Red">You are Sucessfully logged out...</font></h1>
        <%
            response.sendRedirect("home.html");
            session.invalidate();

        %>

        

        <a href="home.html"></a>

    </body>

</html>