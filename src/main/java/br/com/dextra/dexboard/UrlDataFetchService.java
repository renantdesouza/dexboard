package br.com.dextra.dexboard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonElement;
import com.googlecode.restitory.gae.filter.util.JsonUtil;

public class UrlDataFetchService {

	private static final Logger LOG = LoggerFactory.getLogger(UrlDataFetchService.class);

	public static JsonElement baixarJson(String uri) {
		try {
			LOG.debug("Baixando JSON da URI: " + uri);
			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse response = urlFetchService.fetch(new URL(uri));
			String json = new String(response.getContent());
			LOG.debug("JSON baixado: {");
			LOG.debug(json);
			LOG.debug("}");
			return JsonUtil.parse(json);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
