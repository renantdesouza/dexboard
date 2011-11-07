package br.com.dextra.dexboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	public static final int CACHE_EXPIRATION_SECONDS = 60 * 60;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		try {
			MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
			if ("true".equals(req.getParameter("clean"))) {
				cache.clearAll();
			}
			CacheEntry entry = (CacheEntry) cache.get("dados-projetos");
			if (entry != null) {
				if (entry.getLastModified() != null) {
					resp.setDateHeader("Last-Modified", entry.getLastModified());
				}
				resp.setHeader("X-MemCached", "true");
				resp.getWriter().print(entry.getJson());
				return;
			}

			DatastoreService service = DatastoreServiceFactory.getDatastoreService();
			Key key = KeyFactory.createKey("projectData", "data");
			Entity entity = service.get(key);
			Text text = (Text) entity.getProperty("json");
			Long lastModified = (Long) entity.getProperty("lastModified");
			if (lastModified != null) {
				resp.setDateHeader("Last-Modified", lastModified);
			}
			String json = text.getValue();
			resp.getWriter().print(json);

			cache.put("dados-projetos", new CacheEntry(json, lastModified), Expiration
					.byDeltaSeconds(CACHE_EXPIRATION_SECONDS), SetPolicy.ADD_ONLY_IF_NOT_PRESENT);

		} catch (EntityNotFoundException e) {
			resp.getWriter().print("null");
		}
	}

}
