package br.com.dextra.dexboard.api.base;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.ws.rs.core.Form;

import org.junit.Before;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.dextra.dexboard.domain.Classificacao;

public abstract class IntegrationTest {

	private static final String APP_TEST = "app.test";

	protected LocalHttpFacade service = new LocalHttpFacade();

	@Before
	public void setUp() throws Exception {
		System.setProperty(APP_TEST, "true");
	}

	protected void alteraIndicadorDeProjeto(long idProjeto, int idIndicador, Classificacao classificacao) throws IOException, SAXException {
		String registro = "{\"classificacao\": \"%s\", \"comentario\": \"desc desc\"}";
		
		Form form = new Form();
		form.param("projeto", Long.toString(idProjeto));
		form.param("indicador", Integer.toString(idIndicador));
		form.param("registro", String.format(registro, classificacao));
		
		this.service.post("/indicador", form);
	}

	protected void carregaProjetos() {
		JsonObject response = this.service.get("/reload/projetos").getAsJsonObject();
		String status = response.get("status").getAsString();
		
		assertEquals("success", status);
	}
	
	protected JsonObject getProjeto(long id) {
		JsonArray projetos = this.getProjetos();
		for (JsonElement p : projetos) {
			JsonObject projeto = p.getAsJsonObject();
			if (projeto.get("idPma").getAsLong() == id) {
				return projeto;
			}
		}
		
		throw new NoSuchElementException(Long.toString(id));
	}

	private JsonArray getProjetos() {
		return this.service.get("/query").getAsJsonArray();
	}
	
}
