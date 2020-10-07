<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Password Authentication</title>
	<meta name="author" content="Marie-Luise Lux">
	<link rel="stylesheet"
	 href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	 integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	 crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="style.css">
</head>

<body>
<%
	String message = "";
	if(request.getAttribute("loginerror")!=null){
		message = (String) request.getAttribute("loginerror");
	}
%>
	<div class="div-title"><h1>Login using Password Authentication</h1></div>
	<p></p>
	<div class="container col-md-3 col-md-offset-3" style="overflow: auto">
		<%=message%>
		<br/>
		<form action="<%=request.getContextPath()%>/auth" method="post">
			<div class="form-group">
				<label for="uname">User Name:</label>
				<input type="text" class="form-control" id="username" value="<%session.getAttribute("username");%>" placeholder="User Name"
		    name="username" required>
		    </div>
			<div class="form-group">
				<label for="uname">Password:</label> <input type="password"
				 class="form-control" id="password" placeholder="Password"
				 name="password" required>
			</div>
			<button type="submit" class="btn btn-primary">Submit</button>
		</form>
	</div>
	<p></p>
	<p><a href="register.jsp">Register</a></p>
</body>
</html>