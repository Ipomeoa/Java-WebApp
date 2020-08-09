<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page session="false"%>
<%@page import="com.ipomoea.webapp.web.Constants"%>
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

<script>
	// Avoid `console` errors in browsers that lack a console.   http://stackoverflow.com/a/11663507/2863942 
	(function() {
	    var method;
	    var noop = function () {};
	    var methods = [
	        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
	        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
	        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
	        'timeStamp', 'trace', 'warn'
	    ];
	    var length = methods.length;
	    var console = (window.console = window.console || {});
	
	    while (length--) {
	        method = methods[length];
	
	        // Only stub undefined methods.
	        if (!console[method]) {
	            console[method] = noop;
	        }
	    }
	}());
	
	var localhostRoot = 'http://localhost:25519/';	// the SQRL client CPS listener
	var cpsGifProbe = new Image(); 					// create an instance of a memory-based probe image
	
	// Taken from https://www.grc.com/sqrl/demo/pagesync.js and renamed
	cpsGifProbe.onload = function() {  // if the SQRL localhost CPS listener is present
		document.location.href = localhostRoot + "<%=(String) request.getAttribute("sqrlurlwithcan64")%>";
	};

	// Taken from https://www.grc.com/sqrl/demo/pagesync.js and renamed
	cpsGifProbe.onerror = function() {
		setTimeout( function(){ cpsGifProbe.src = localhostRoot + Date.now() + '.gif';	}, 200 );
	}
	
	function sqrlInProgress() {
		var sqrlButtonSrc = $("#sqrlButton").attr("src");
		var showingSqrlQr = sqrlButtonSrc != "spinner.gif";
    	if(!showingSqrlQr) {
    		return;
    	}
    	$("#sqrlButton").attr("src", "spinner.gif");
    	$("#cancel").hide();
    	$("#footer").hide();
    	$("#subtitle").hide();
    	$("#uplogin").hide();
    	$("#sqrlQrRow").hide();
    	$("#or1").hide();
    	$("#or2").hide();
    	$("#sqrlImg").hide();
        instruction.innerText = "Waiting for SQRL client";
		$("#cancel").show();
    	if(subtitle.innerText.indexOf("rror") >=0 ) {
    		subtitle.innerText = "";
    	}
    	if(<%=(String) request.getAttribute("cpsEnabled")%>) {
    		cpsGifProbe.onerror();	// try to connect to the SQRL client on localhost if possible (CPS)
    	}
	}
	
	function stopPolling(socket, subsocket, request) {
		subsocket.push("done");
		socket.close();
	}
	
    $(document).ready(function() {
    $("#cancel").hide();
	// Atmosphere logic for auto refresh
	var socket = atmosphere;
	var subsocket;
	var atmosphereurl = window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/")) +"/sqrlauthpolling";
    var request = { url: atmosphereurl,
            contentType: "application/json",
            logLevel: "debug",
            transport: "sse",
            reconnectInterval: 5000,
            headers:{'X-sqrl-corelator':'<%=(String) request.getAttribute("correlator")%>'},
            fallbackTransport: "long-polling"};

        request.onOpen = function (response) {
        	console.debug("Atmosphere connected using " + response.transport );
        };
        
        request.onReconnect = function (request, response) {
            console.info("Atmosphere connection lost, trying to reconnect " + request.reconnectInterval);
        };

        request.onReopen = function (response) {
        	console.info("Atmosphere re-connected using " + response.transport );
        };

        request.onMessage = function (response) {
   	        var message = response.responseBody;
			console.info("received from server: " + message);
   	        try {
   	        	var json = atmosphere.util.parseJSON(message);
   	        } catch (e) {
   	            console.log('JSON parse error: ', message);
   	            return;
   	        }
        	var statusText = json.status;
			if (statusText && statusText.indexOf("ERROR_") > -1) {
				subsocket.close();
				window.location.replace("login?error="+statusText);
			} else if (statusText && statusText.indexOf("AUTHENTICATED_CPS") > -1) {
				// Stop polling and wait for the SQRL client to provide the URL	
				subsocket.close();
			} else if(statusText == "AUTHENTICATED_BROWSER") {
				subsocket.push(atmosphere.util.stringifyJSON({ state: "AUTHENTICATED_BROWSER" , correlator: "<%=(String) request.getAttribute("correlator")%>"}));
				subsocket.close();
				window.location.replace("sqrllogin");
			} else if(statusText == "COMMUNICATING"){
				// The user scanned the QR code and sqrl auth is in progress
				instruction.innerText = "Communicating with SQRL client";
				subsocket.push(atmosphere.util.stringifyJSON({ state: "COMMUNICATING" , correlator: "<%=(String) request.getAttribute("correlator")%>"}));
				sqrlInProgress();
			} else {
				console.error("received unknown state from server: " + statusText);
				subsocket.close();
				window.location.replace("login?error=ERROR_SQRL_INTERNAL");
			}
        };

        request.onClose = function (response) {
        	console.info("Server closed the connection after a timeout");
        };

        request.onError = function (response) {
            console.error("Error, there\'s some problem with your " 
                + "socket or the server is down");
        };
      
        subsocket = socket.subscribe(request);
        subsocket.push(atmosphere.util.stringifyJSON({ state: "CORRELATOR_ISSUED", correlator: "<%=(String) request.getAttribute("correlator")%>" }));
	});
</script>

<body>
<div class="container">
	<div class="div-title div-center"><h1>Login using Password Authentication or SQRL</h1></div>
    <div class="row div-center justify-content-center">
        <div class="col-xs-6 div-center">
				<p><%=request.getAttribute("message")%></p>
				<br/>
				<form action="<%=request.getContextPath()%>/auth" method="post">
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
			<p></p>
			<p><a href="register.jsp">Register</a></p>
        </div>
        <div class="col-xs-6 div-center">
			<h5><i> -- or --</i></h5>
        </div>
        <div class="col-xs-6 div-center">
			<div>
				<a href="<%=(String) request.getAttribute("sqrlurl")%>"  onclick="sqrlInProgress();return true;" >
					<img id="sqrlButton" src="signInSqrl.png" alt="Click to sign in with SQRL" /></a>
				<br/>
				<a id="cancel"  href="login?error=8">Cancel SQRL authentication</a>
				<br/>
			</div>
			<div id="sqrlQrRow">
				<div>
					<img src="<%=(String) request.getAttribute("sqrlqr64")%>"
			        	alt="<%=(String) request.getAttribute("sqrlqrdesc")%>" />
				</div>
				<div>
					<br/>
					<p>Click the Button or scan the
					<br/> QR code with a SQRL mobile app to log in.</p>
				</div>
			</div>
        </div>
    </div>
</div>
</body>
</html>