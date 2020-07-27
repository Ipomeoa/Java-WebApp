<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Successful Login!</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<%
	if(session.getAttribute("username") == null){
		System.out.println("Redirecting from success to login page");
		request.getRequestDispatcher("login").forward(request, response);
		return;
	}
	String name = "";
	if(session.getAttribute("firstname")!=null){
		name = " "+session.getAttribute("firstname");
	}
	if(session.getAttribute("firstname")!=null){
		name += " "+session.getAttribute("lastname");
	}
%>
	
	<div align="center" class="div-title">
		<h1>You have logged in successfully!</h1>
		<p>Hello<%=name%>.</p>
		<%
			String text = "";
			if(name.length()==0){
				text = "You did not insert your first and/or last name when registering.";
			}
		%>
		<p><%=text%></p>
		<p>You are logged in with the username: <%=(String) session.getAttribute("username")%></p>
		<p><a href="logout">Logout</a></p>
	</div>
</body>
</html>