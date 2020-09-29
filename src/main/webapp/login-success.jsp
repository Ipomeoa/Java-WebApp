<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta content="text/html;charset=utf-8" http-equiv="Content-Type">
	<meta content="utf-8" http-equiv="encoding">
	<title>Successful Login!</title>
	<meta name="author" content="Marie-Luise Lux">
	<link rel="stylesheet" type="text/css" href="style.css">
	<link rel="stylesheet"
	 href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	 integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	 crossorigin="anonymous">
	<script src="lib/base64js/base64js-1.3.0.min.js"></script>
	<script src="base64url.js"></script>
	<script src="webauthn.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
	 
	 <script>
	     $(function () {
	         $('#takeAction').hide();
	     });
	 	
        function setStatus(statusText, success) {
            $('#status').text(statusText);
            if (success) {
                $('#status').removeClass('error');
            } else {
                $('#status').addClass('error');
            }
        }
    	
        // Submit a response to the backend
        function submitResponse(url, requestId, response) {
            var token = $("meta[name='_csrf']").attr("content"); 
            const body = {
                requestId,
                credential: response,
            };
            
            return fetch(url, {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                body: JSON.stringify(body),
            }).then(response => response.json())
            ;
        }
    	
        // Register a security key
        function register() {
            $('#takeAction').show();
            const username = '<%=(String) session.getAttribute("username")%>';
            const displayName = '<%=(String) session.getAttribute("username")%>';
            const credentialNickname = $("#inputNickname").val();
            const requireResidentKey = true;
    		
            // Call the WebAuthnRegisterServlet to initiate the registration process by creationg the publicKeyCreateCredentialOptions
            return fetch('webauthnregister', {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                },
                body: new URLSearchParams({
                    username, displayName, credentialNickname, requireResidentKey,
                })
            })
            .then(response => response.json())
            .then(function(request) {
                console.log('request', request);
                // Use the code from webauth.js to handle communication with the authenticator and to receive the publicKeyCredential
                // If the credentials have been received send them to the WebAuthnRegisterFinishServlet, if not handle the error
                return webauthn.createCredential(request)
	                .then(webauthn.responseToObject)
	                .then(function (publicKeyCredential) {
	                    url = 'webauthnregisterfinish';
	                    return submitResponse(url, request.requestId, publicKeyCredential);
	                })
	                .catch(error => {
	                    throw error;
	                })
	                ;
            })
            .then(data => {
            	// Update UI and Display new key in the list of registered Keys
                $('#takeAction').hide();
                console.log(data);
                setStatus("Success!", true);
                $('#no-keys').hide();
                $('#keys').show();
                $('#inputNickname').val('');
                $('#keys').append(`
                    <tr>
                        <td>${data.registration.credentialNickname}</td>
                        <td>${data.registration.registrationTime}</td>
                    </tr>
                `);
                return data;
            })
            .catch(error => {
            	//Error handling
                $('#takeAction').hide();
                console.log('register', error);
                setStatus(error.message, false);
            })
            ;
        }
	 
	 </script>
</head>
<body>
<%
	if(session.getAttribute("username") == null){
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
	System.out.println(name);
	if(name.length()==0){
		name = "You did not insert your first and/or last name when registering.";
	}
%>
	
	<div align="center" class="div-title">
		<h1>You have logged in successfully!</h1>
		<p>Hello<%=name%>.</p>
		<%
		%>
		<p><%=name%></p>
		<p>You are logged in with the username: <%=(String) session.getAttribute("username")%></p>
		<p><a href="logout">Logout</a></p>
	</div>
	
	
	<div class="box-gray">
	    <h2 class="section-header">Security Keys</h2>
	    <div th:if="${registrations.empty}" class="error" id="no-keys">
	        <p>Warning, no authenticators have been added and your account is not secure. Configure authenticators below!</p>
	    </div>
	    <table class="table" id="keys" th:classappend="${registrations.empty}? 'hide'">
	        <thead>
	            <tr>
	                <th> Nickname </th>
	                <th> Registration Time </th>
	            </tr>
	        </thead>
	        <tbody id="keys">
	            <tr th:each="registration : ${registrations}" >
	            </tr>
	        </tbody>
	    </table>
	</div>
	<br/>
    <sec:authentication property="name" var="username" />
	
	<div class="box-gray">
	    <h2 class="section-header">Register a Security Key</h2>
	    <br/>
	    <label>
	        <span>Nickname:</span>
	        <input type="text" id="inputNickname">
	    </label><br/>
	    <button onclick="register()" class="btn btn-primary">Register</button>
	    <p id="status"></p>
	    <div id="takeAction">
	        <p>Please insert and take action on the security key.</p>
	    </div>
	</div>
</body>
</html>