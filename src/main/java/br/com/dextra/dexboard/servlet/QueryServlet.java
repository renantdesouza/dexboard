package br.com.dextra.dexboard.servlet;

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

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	public static final int CACHE_EXPIRATION_SECONDS = 60 * 60;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		
		try {

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

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			resp.getWriter().print("null");
		}
	}

}
