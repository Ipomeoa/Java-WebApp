<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<meta http-equiv="refresh" content="<%=request.getAttribute(com.ipomoea.webapp.web.Constants.JSP_PAGE_REFRESH_SECONDS)%>">
	<title>SQRL Authentication</title>
	<link rel="stylesheet"
	 href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	 integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	 crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="style.css">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/atmosphere/2.2.12/atmosphere.js"></script>
	<link rel="icon" href="favicon.ico">
</head>
<body>
<%
	/**if(session.getAttribute("username") == null){
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
	}**/
%>
	
<div class="container">
	<div class="div-title div-center"><h1>Welcome</h1></div>
   	<div class="row justify-content-center">
		<div class="col-xs-6">
			<p>This is your first time logging in with SQRL.</p>
			<p>If you wish to link your account to an existing one<br/> please insert your credentials.<br/></p>
			<br/>
			<br/>
			<br/>
			<button onclick="location.href = 'success';">I don't have a username/password</button>
		</div>
        <div class="div-space">
        </div>
		<div class="col-xs-6">
			<form action="linkaccount" method="post">
				<div class="form-group">
					<label for="uname">User Name:</label>
					<input type="text" class="form-control" id="username" placeholder="User Name"
			    name="username" required>
			    </div>
				<div class="form-group">
					<label for="uname">Password:</label> <input type="password"
					 class="form-control" id="password" placeholder="Password"
					 name="password" required>
				</div>
				<button type="submit" class="btn btn-primary">Submit</button>
			</form>
			<br/>
			<p><a href="logout">Cancel/Logout</a><p/>
			<br/>
			<br/>
		</div>
	</div>
</div>
</body>
</html>