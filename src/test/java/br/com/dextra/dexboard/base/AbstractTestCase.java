package br.com.dextra.dexboard.base;

import org.junit.After;
import org.junit.Before;

import com.google.gson.JsonObject;
import com.googlecode.restitory.gae.http.HttpClientRequestService;
import com.googlecode.restitory.gae.http.RequestAdapter;
import com.googlecode.restitory.gae.http.RequestService;
import com.googlecode.restitory.gae.http.Response;

public class AbstractTestCase {

	protected GAETestHelper helper;

	protected RequestService service;

	protected RequestAdapter adapter;

	@Before
	public void setUp() throws Exception {
		System.setProperty("app.test", "true");
		helper = new GAETestHelper();
		helper.prepareLocalServiceTestHelper();
		helper.bootMycontainer();

		service = new HttpClientRequestService("http://localhost:8380");
		adapter = new RequestAdapter(service);
	}

	@After
	public void tearDown() {
		if (helper != null) {
			helper.shutdownMycontainer();
		}
	}

	protected JsonObject getJson(Response resp) {
		return resp.getContent().getJson().getAsJsonObject();
	}

}
