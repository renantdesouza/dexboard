package br.com.dextra.dexboard.servlet;

public class Context {
	
	public static boolean isTestEnvironment() {
		String test = System.getProperty("app.test");
		return "true".equalsIgnoreCase(test);
	}

}
