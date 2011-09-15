package br.com.dextra.dexboard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonElement;
import com.googlecode.restitory.gae.filter.util.JsonUtil;

public class DataFetcher {

	protected static JsonElement fetchData(String uri) {
		try {
			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse response = urlFetchService.fetch(new URL(uri));
			String json = new String(response.getContent());
			return JsonUtil.parse(json);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
