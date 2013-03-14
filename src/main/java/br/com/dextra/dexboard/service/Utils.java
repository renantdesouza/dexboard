package br.com.dextra.dexboard.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	// private SampleConsumer teste = new SampleConsumer();

	// teste.authRequest();

	public static JsonElement baixarJson(String uri) {
		return baixarJson(uri, Charset.defaultCharset());
	}

	public static JsonElement baixarJson(String uri, Charset encoding) {

		try {
			LOG.info("Baixando JSON da URI: " + uri);
			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse response = urlFetchService.fetch(new URL(uri));
			String json = new String(response.getContent(), encoding);
			LOG.debug("JSON baixado >>>\n" + json + "\n<<< JSON baixado");
			return parse(json);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonObject mesclar(JsonObject destino, JsonObject origem) {
		for (Entry<String, JsonElement> entry : origem.entrySet()) {
			destino.add(entry.getKey(), entry.getValue());
		}
		return destino;
	}

	public static JsonElement parse(String json) {
		JsonParser parser = new JsonParser();
		JsonElement ret = parser.parse(json);
		return ret;
	}

}
