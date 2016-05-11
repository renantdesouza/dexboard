package br.com.dextra.dexboard.servlet.dev;

import java.io.Closeable;
import java.io.IOException;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class LocalGaeEnvironment implements Closeable {
	
	private final LocalServiceTestHelper gaeHelper;
	
	private LocalGaeEnvironment() {
		this.gaeHelper = new LocalServiceTestHelper(new LocalUserServiceTestConfig())
				.setEnvIsAdmin(false)
				.setEnvIsLoggedIn(true)
				.setEnvEmail("test@dextra-sw.com")
				.setEnvAuthDomain("dextra-sw.com");
	}
	
	@Override
	public void close() throws IOException {
		this.gaeHelper.tearDown();
	}
	
	public static LocalGaeEnvironment setUp() {
		LocalGaeEnvironment localEnvironment = new LocalGaeEnvironment();
		localEnvironment.gaeHelper.setUp();
		return localEnvironment;
	}

}
