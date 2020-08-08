package com.ipomoea.webapp.web;

public class Constants {
	private Constants() {
	}

	private static final String PACKAGE_NAME = Constants.class.getPackage().getName();
	public static final String	SESSION_SQRL_IDENTITY	= PACKAGE_NAME + ".sqrlIdentity";

	public static final String	SESSION_NATIVE_APP_USER	= PACKAGE_NAME + ".nativeAppUser";
	public static final String	JSP_PAGE_REFRESH_SECONDS = "pageRefreshSeconds";
	
}
