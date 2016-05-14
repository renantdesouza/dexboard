package br.com.dextra.dexboard.servlet;

public class Context {
	
	public static boolean isTestEnvironment() {
		String test = System.getProperty("skip-integration-test");
		return "false".equalsIgnoreCase(test);
	}

}
