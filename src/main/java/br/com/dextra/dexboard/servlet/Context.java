package br.com.dextra.dexboard.servlet;

import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.utils.SystemProperty.Environment.Value;

public class Context {
	
	private static final boolean INTEGRATION_TEST = is("integration-test");
	private static final boolean DEV_ENV = is("dev-environment");
	private static final boolean PROD_ENV = SystemProperty.environment.value() == Value.Production;
	
	private static boolean forceMock = false;
	
	public static boolean isDevelopmentEnvironment() {
		if (PROD_ENV) return false;
		return forceMock || DEV_ENV;
	}
	
	public static boolean isIntegrationTestEnvironment() {
		if (PROD_ENV) return false;
		return INTEGRATION_TEST;
	}
	
	public static void forceMock(boolean value) {
		forceMock = value;
	}
	
	private static boolean is(String environment) {
		return "true".equalsIgnoreCase(System.getProperty(environment));
	}

}
