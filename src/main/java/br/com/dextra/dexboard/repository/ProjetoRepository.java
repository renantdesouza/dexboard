package br.com.dextra.dexboard.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.dextra.dexboard.domain.ListaProjeto;
import br.com.dextra.dexboard.domain.Projeto;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;

public class ProjetoRepository {

	public static Projeto buscarPorId(int id, List<Projeto> projetos) {
		
		for (Projeto projeto : projetos) {
			if (projeto.getIdPma() == id)
				return projeto;
		}

		return null;
	}

	public static List<Projeto> buscaProjetos() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("projectData", "data");
		Entity entity;
		ListaProjeto projetos = null;
		try {
			entity = service.get(key);
			Text text = (Text) entity.getProperty("json");

			if (text != null) {
				Gson gson = new Gson();
				projetos = gson.fromJson(text.getValue(), ListaProjeto.class);
			}
		} catch (EntityNotFoundException e) {
			return new ArrayList<Projeto>();
		}
		return projetos.getValue();
	}

	public static void persisteProjetos(List<Projeto> projetos) {

		ListaProjeto lista = new ListaProjeto(projetos);
		Gson gson = new Gson();
		String data = gson.toJson(lista);

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		Entity entity = new Entity("projectData", "data");
		entity.setProperty("json", new Text(data));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		entity.setProperty("lastModified", sdf.format(new Date()));

		service.put(entity);
	}
}