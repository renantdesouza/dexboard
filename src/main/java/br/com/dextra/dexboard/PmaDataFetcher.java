package br.com.dextra.dexboard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonElement;
import com.googlecode.restitory.gae.filter.util.JsonUtil;

public class PmaDataFetcher {

	public static JsonElement fetchData(int idProjeto) {
		try {
			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();

			// XXX
			String uriQuasePronta = "http://172.16.129.159:8001/services/indicadores?token=ac4ef0ec195ed24ab08d1e4a8a3a1ed0&projeto_id=";

			// XXX Concatenacao insegura
			HTTPResponse response = urlFetchService.fetch(new URL(uriQuasePronta + idProjeto));
			String json = new String(response.getContent());
			return JsonUtil.parse(json);

		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
