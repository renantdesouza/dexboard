package br.com.dextra.dexboard.api;

import static org.junit.Assert.assertEquals;

import java.util.NoSuchElementException;

import javax.ws.rs.core.Form;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.dextra.dexboard.domain.Classificacao;

/**
 * Testes de API sao testes fim-a-fim para servicos. Nesses testes os
 * clientes Mobile (Android/iOS) e Web (Javascript) ficam de fora, mas
 * os principais fluxos da aplicacao sao testados via chamadas HTTP.
 *
 * Esses testes devem rodar em um ambiente de integracao continua, o
 * mais similar possivel ao ambiente de producao.
 *
 * Sao os testes mais lentos, e os mais passiveis de falsos-negativos.
 * Devem portanto ser o tipo de teste de menor numero.
 *
 */
public class ApiTest {

	protected static LocalHttpFacade service;

	@BeforeClass
	public static void init() {
		service = new LocalHttpFacade();
		carregaProjetos();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void destroy() {
		service.close();
	}


	protected void alteraIndicadorDeProjeto(long idProjeto, int idIndicador, Classificacao classificacao) {
		String registro = "{\"classificacao\": \"%s\", \"comentario\": \"desc desc\"}";

		Form form = new Form();
		form.param("projeto", Long.toString(idProjeto));
		form.param("indicador", Integer.toString(idIndicador));
		form.param("registro", String.format(registro, classificacao));

		service.post("/indicador", form);
	}

	public static void carregaProjetos() {
		JsonObject response = service.get("/reload/projetos").getAsJsonObject();
		String status = response.get("status").getAsString();

		assertEquals("success", status);
	}

	protected JsonObject getProjeto(long id) {
		JsonArray projetos = getProjetos();
		for (JsonElement p : projetos) {
			JsonObject projeto = p.getAsJsonObject();
			if (projeto.get("idPma").getAsLong() == id) {
				return projeto;
			}
		}

		throw new NoSuchElementException(Long.toString(id));
	}

	private JsonArray getProjetos() {
		return service.get("/query").getAsJsonArray();
	}

}
