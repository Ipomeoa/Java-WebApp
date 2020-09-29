package com.ipomoea.webapp.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yubico.webauthn.data.RelyingPartyIdentity;


import com.ipomoea.webapp.webauthn.RegistrationController;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.yubico.webauthn.data.UserIdentity;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.*;

@WebServlet(urlPatterns = { "/webauthnregister" })
public class WebAuthnRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 2;
    private static final SecureRandom random = new SecureRandom();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		// Get parameters needed for the creation of the registration request
    	String username = request.getParameter("username");
    	String displayName = request.getParameter("firstname") + " " + request.getParameter("lastname");
    	
    	// Create PublicKeyCredentialCreationOptions and store the instance for future use
		byte[] userHandle = new byte[64];
		random.nextBytes(userHandle);
		WebAuthnConfig.PK_REQUEST = WebAuthnConfig.RP.startRegistration(StartRegistrationOptions.builder()
		    .user(UserIdentity.builder()
		        .name(username)
		        .displayName(displayName)
		        .id(new ByteArray(userHandle))
		        .build())
		    .build());

		// Convert PublicKeyCredentialCreationOptions to JSON to send to the frontend
		ObjectMapper jsonMapper = new ObjectMapper()
			    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			    .setSerializationInclusion(Include.NON_ABSENT)
			    .registerModule(new Jdk8Module());
		String json = jsonMapper.writeValueAsString(WebAuthnConfig.PK_REQUEST);
		
		// Set response content type and add PublicKeyCredentialCreationOptions to the response
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
    }
}