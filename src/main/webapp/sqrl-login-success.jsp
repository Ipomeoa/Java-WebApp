<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@page import="com.ipomoea.webapp.web.Constants"%>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta content="text/html;charset=utf-8" http-equiv="Content-Type">
	<meta content="utf-8" http-equiv="encoding">
	<title>Successful SQRL Login!</title>
	<link rel="stylesheet" type="text/css" href="style.css">
	<link rel="icon" href="favicon.ico">
</head>

<body>
<%
	if(session.getAttribute("id") == null){
		System.out.println("Redirecting from success to login page");
		response.setStatus(302);
		response.setHeader("Location", "login");
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
<div class="container">
	<div class="div-title div-center"><h1>Login using Password Authentication or SQRL</h1></div>
    <div class="row justify-content-center">
			<div class="col-sm-6">
				<p>You have been authenticated via SQRL. </p>
				<p>If you have an existing username and password for this site that you would like to link your SQRL ID to, then enter your credentials. <br/></p>
			</div>
			<div class="col-sm-6">
				<p>Username: (alphanumeric)<br/>
				<form action="linkaccount"  method="post">
				  <input type="text" name="username" pattern="[a-zA-Z0-9]+"  maxlength="10" required><br>
				  Password:<br>
				  <input type="password" name="password" pattern="[a-zA-Z]+" maxlength="10" required><br><br>
				  <input type="hidden" name="type" value="up"><br>
				  <input type="submit" value="Link Account">
				 </form>
				 <br/>
				 <button onclick="location.href = 'usersettings.jsp';">I don't have a username/password</button>
				  <br/>
				  <br/>
				 <p><a href="logout">Cancel</a><p/>
				  <br/>
				  <br/>
			</div>
		</div>
    </div>
</div>
</body>
</html>