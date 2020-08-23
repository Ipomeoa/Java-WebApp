package com.ipomoea.webapp.web;

import com.yubico.webauthn.data.RelyingPartyIdentity;

public class Constants {
	private Constants() {
	}

	private static final String					PACKAGE_NAME		= Constants.class.getPackage().getName();
	public static final String	SESSION_NATIVE_APP_USER	= PACKAGE_NAME + ".nativeAppUser";
    public static final RelyingPartyIdentity DEFAULT_RP_ID
    = RelyingPartyIdentity.builder().id("localhost").name("WebApp WebAuthn").build();
	
}
