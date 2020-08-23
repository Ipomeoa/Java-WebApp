package com.ipomoea.webapp.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ipomoea.webapp.webauthn.RegistrationStorage;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.yubico.webauthn.data.UserIdentity;
import com.yubico.webauthn.exception.RegistrationFailedException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.*;

@WebServlet(urlPatterns = { "/webauthnregisterfinish" })
public class WebAuthnRegisterFinishServlet extends HttpServlet {
    private static final long serialVersionUID = 2;
    private static final SecureRandom random = new SecureRandom();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	// Get the response from the client
    	String responseJson = request.getReader().lines().collect(Collectors.joining());
    	responseJson = responseJson.substring(14, responseJson.length()-1);
    	System.out.println("responseJson: " + responseJson);
    	PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc =
    	    PublicKeyCredential.parseRegistrationResponseJson(responseJson);
    	// Validate the response
    	/**try {
    	    RegistrationResult result = Constants.DEFAULT_RP_ID
    	    		.finishRegistration(FinishRegistrationOptions.builder()
	    	        .request(request)
	    	        .response(pkc)
	    	        .build());
    	} catch (RegistrationFailedException e) {
    		System.out.println("WebAuthn registration failed");
    		e.printStackTrace();
    	}**/
    	
    	// To Do storeCredential("alice", result.getKeyId(), result.getPublicKeyCose());
    }
}