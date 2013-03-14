package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.service.DadosProjeto;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class DadosServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		List<Projeto> projetos = new ArrayList<Projeto>();
		List<Projeto> projetosPlanilha = DadosProjeto.buscarDadosProjetos();
		List<Projeto> projetosDataStore;
		try {
			projetosDataStore = getProjetosFromDataStore();
		} catch (EntityNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (projetosDataStore != null) {
			for (Projeto p : projetosDataStore) {
				if (projetoEstaNaLista(p, projetosPlanilha)) {
					projetos.add(p);
				}
			}
		}

		for (Projeto p : projetosPlanilha) {
			if (!projetoEstaNaLista(p, projetos)) {
				projetos.add(p);
			}
		}

		JSONSerializer serializer = new JSONSerializer();
		String data = serializer.deepSerialize(projetos);

		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("projectData", "data");
		entity.setProperty("json", new Text(data));
		service.put(entity);
		resp.getWriter().print("true");
	}

	private List<Projeto> getProjetosFromDataStore()
			throws EntityNotFoundException {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("projectData", "data");
		Entity entity = service.get(key);
		Text text = (Text) entity.getProperty("json");

		List<Projeto> projetos = null;
		if (text != null) {
			JSONDeserializer<List<Projeto>> deserializer = new JSONDeserializer<List<Projeto>>();
			projetos = deserializer.deserialize(text.toString());
		}
		return projetos;
	}

	private boolean projetoEstaNaLista(Projeto projeto, List<Projeto> projetos) {
		for (Projeto p : projetos) {
			if (p.getIdPma() == projeto.getIdPma()) {
				return true;
			}
		}
		return false;
	}

}
