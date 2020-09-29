package com.ipomoea.webapp.web;

import com.ipomoea.webapp.webauthn.RegistrationController;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.RelyingPartyIdentity;

public class WebAuthnConfig {
    public static final RelyingPartyIdentity DEFAULT_RP_ID
    		= RelyingPartyIdentity.builder().id("localhost").name("WebApp WebAuthn").build();
    
	public static final RelyingParty RP = RelyingParty.builder()
		    .identity(DEFAULT_RP_ID)
		    .credentialRepository(new RegistrationController())
		    .build();
	

    public static PublicKeyCredentialCreationOptions PK_REQUEST = null;
}
