package br.com.dextra.dexboard.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

class LocalHttpFacade {

	private static final String BASE_URL = "http://localhost:8080";
	private static final HashMap<String, String> EMPTY_QUERY = new HashMap<>(0);

	private final Client CLIENT = ClientBuilder.newClient().register(Authenticator.class);
	private final WebTarget target = CLIENT.target(BASE_URL);
	private final JsonParser parser = new JsonParser();

	public JsonElement get(String path, Map<String, String> query) {
		WebTarget target = this.appendQuery(query).path(path);
		Builder request = target.request(MediaType.APPLICATION_JSON);
		
		this.gaeEventualConsistencyDelay();
		String responseText = request.get(String.class);
		return this.parser.parse(responseText);
	}
	
	public JsonElement post(String path, Form form) {
		WebTarget target = this.target.path(path);
		Builder request = target.request(MediaType.APPLICATION_JSON);
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		this.gaeEventualConsistencyDelay();
		String responseText = request.post(entity, String.class);
		return this.parser.parse(responseText);
	}


	public JsonElement get(String path) {
		return this.get(path, EMPTY_QUERY);
	}

	private WebTarget appendQuery(Map<String, String> query) {
		try {
			WebTarget target = this.target;
			for (String key : query.keySet()) {
				target = target.queryParam(key, URLEncoder.encode(query.get(key), "UTF-8"));
			}
			return target;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		CLIENT.close();
	}

	private void gaeEventualConsistencyDelay() {
		// Os testes quebram por causa da consistencia eventual do GAE
		// Incluindo esse delay para evitar falsos negativos
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
