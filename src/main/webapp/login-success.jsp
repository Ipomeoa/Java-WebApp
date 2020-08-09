<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<%@page import="com.ipomoea.webapp.web.Constants"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta content="text/html;charset=utf-8" http-equiv="Content-Type">
	<meta content="utf-8" http-equiv="encoding">
	<title>Successful Login!</title>
	<link rel="stylesheet" type="text/css" href="style.css">
	<link rel="icon" href="favicon.ico">
</head>
<body>
<%
	String username = (String) session.getAttribute("username");
	String sqrlId = (String) session.getAttribute("sqrlId");
	
	if((session.getAttribute("username") == null) && (session.getAttribute(Constants.SESSION_SQRL_IDENTITY) == null)){
		System.out.println("No user in session");
		request.getRequestDispatcher("login?error=0").forward(request, response);
		return;
	}
	
	if(session.getAttribute("username") == null){
		username = "You first registered using SQRL so no username could be found.";
	}
	if(session.getAttribute(Constants.SESSION_SQRL_IDENTITY) == null){
		sqrlId = "You never registered using SQRL so no SQRL ID could be found.";
	}
	String name = "";
	if(session.getAttribute("firstname")!=null){
		name = " "+session.getAttribute("firstname");
	}
	if(session.getAttribute("lastname")!=null){
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
		<p><b>Username:</b> <%=username%></p>
		<p><b>SQRL Ientity Key:</b> <%=sqrlId%></p>
		<p><b>Account Type</b>: <%=(String) session.getAttribute("accountType") %></p>
		<p><a href="logout">Logout</a></p>
	</div>
</body>
</html>