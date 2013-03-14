package br.com.dextra.dexboard.old;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.gson.JsonArray;

public class DadosServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		JsonArray ret = Projeto.buscarDadosProjetos();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("projectData", "data");
		entity.setProperty("json", new Text(ret.toString()));
		entity.setProperty("lastModified", new Date().getTime());
		service.put(entity);
		resp.getWriter().print("true");
	}

}
