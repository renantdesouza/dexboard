package br.com.dextra.dexboard.servlet.dev;

import java.io.Closeable;
import java.io.IOException;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class LocalGaeEnvironment implements Closeable {
	
	private final LocalServiceTestHelper gaeHelper;
	
	private LocalGaeEnvironment() {
		
		LocalDatastoreServiceTestConfig ds = new LocalDatastoreServiceTestConfig()
				.setApplyAllHighRepJobPolicy()
				.setBackingStoreLocation("target/temp.db")
				.setNoStorage(false);
		
		this.gaeHelper = new LocalServiceTestHelper(ds)
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
