package br.com.dextra.dexboard.api;

import static org.junit.Assert.assertEquals;

import java.util.NoSuchElementException;

import javax.ws.rs.core.Form;

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
abstract class ApiTest {

	protected LocalHttpFacade service = new LocalHttpFacade();

	protected void alteraIndicadorDeProjeto(long idProjeto, int idIndicador, Classificacao classificacao) {
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
