package br.com.dextra.dexboard;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.googlecode.restitory.gae.http.Response;

public class DadosServletTestManual extends AbstractTestCase {

	@Test
	public void testUser() {
		Response resp = adapter.success("GET", "/dados/projetos", null, null);
		JsonArray json = (JsonArray) resp.getContent().getJson();
		assertTrue(json.size() > 0);

	}

}
