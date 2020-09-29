package com.ipomoea.webapp.web;

import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.RelyingPartyIdentity;

/**
 * @author Marie-Luise Lux
 */

public class Constants {
	private Constants() {
	}

	private static final String					PACKAGE_NAME		= Constants.class.getPackage().getName();
	public static final String	SESSION_NATIVE_APP_USER	= PACKAGE_NAME + ".nativeAppUser";
}
