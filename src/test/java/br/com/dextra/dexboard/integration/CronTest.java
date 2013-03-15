package br.com.dextra.dexboard.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.dextra.dexboard.base.AbstractTestCase;

import com.googlecode.restitory.gae.http.Response;

public class CronTest extends AbstractTestCase {

	@Test
	public void testReloadProjetos() {
		Response resp = adapter.success("GET", "/reload/projetos", null, null);		
		String status = getJson(resp).get("status").getAsString();
		assertEquals("success", status);
	}

}
