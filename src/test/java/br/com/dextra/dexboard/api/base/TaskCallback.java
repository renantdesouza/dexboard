package br.com.dextra.dexboard.api.base;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.google.appengine.api.taskqueue.dev.LocalTaskQueueCallback;
import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest;
import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest.RequestMethod;

class TaskCallback implements LocalTaskQueueCallback {

	private static final long serialVersionUID = -1014385841687576970L;

	@Override
	public int execute(URLFetchRequest req) {
			String path = getUrlPath(req);
			RequestMethod method = req.getMethod();
			
			call(method, path);
			
			return 200;
	}

	private String getUrlPath(URLFetchRequest req) {
		try {
			return new URI(req.getUrl()).getPath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void call(RequestMethod method, String path) {
		HttpFacade request = new HttpFacade();
		if (method.equals(RequestMethod.GET)) {
			request.get(path);
			
		} else {
			throw new UnsupportedOperationException(method.name());
		}
	}

	@Override
	public void initialize(Map<String, String> map) {
	}
}
