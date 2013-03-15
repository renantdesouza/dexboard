package br.com.dextra.dexboard.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.dextra.dexboard.domain.Projeto;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ProjetoRepository {

	public static Projeto buscarPorId(int id, List<Projeto> projetos) {
		
		
		
		return null;
	}
	
	public static List<Projeto> buscaProjetos() throws EntityNotFoundException {

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
	
	public static void persisteProjetos(List<Projeto> projetos) {

		JSONSerializer serializer = new JSONSerializer();
		String data = serializer.deepSerialize(projetos);

		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("projectData", "data");
		entity.setProperty("json", new Text(data));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		entity.setProperty("lastModified", sdf.format(new Date()));

		service.put(entity);
	}
}
