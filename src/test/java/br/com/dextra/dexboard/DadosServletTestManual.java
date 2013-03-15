package br.com.dextra.dexboard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.restitory.gae.http.Response;

public class DadosServletTestManual extends AbstractTestCase {

	@Test
	public void testUser() {
		Response resp = adapter.success("GET", "/dados/projetos", null, null);		
		String status = getJson(resp).get("status").getAsString();
		assertEquals("success", status);
	}

}
