package br.com.dextra.dexboard.api;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.dextra.dexboard.domain.Classificacao;

public class ITestDexboard extends ApiTest {

	private static final int ID_PROJETO_CONTPLAY = 495;
	private static final int ID_INDICADOR_1 = 1;
	private static final int ID_INDICADOR_2 = 2;

	@Test
	public void testQueryProjetos() {
		JsonArray projetos = queryProjetosJson(null);

		assertEquals(23, projetos.size());
		
		LoggerFactory.getLogger(this.getClass()).info(projetos.toString());

		JsonObject a4c = projetos.get(0).getAsJsonObject();
		JsonObject adv = projetos.get(1).getAsJsonObject();

		assertProjeto(495, "A4C", "CHAOS", 1.01, a4c);
		assertProjeto(585, "ADV: Fase II", "MUSTACHE", 1.39, adv);
	}

	@Test
	public void testQueryProjetosEquipe() {
		JsonArray projetos = queryProjetosJson("Rocket");

		assertEquals(2, projetos.size());

		JsonObject confidence = projetos.get(0).getAsJsonObject();
		JsonObject movile = projetos.get(1).getAsJsonObject();

		assertProjeto(565, "Confidence", "ROCKET", 0.99, confidence);
		assertProjeto(579, "Movile", "ROCKET", 1.70, movile);

	}

	private JsonArray queryProjetosJson(String equipe) {
		Map<String, String> query = new HashMap<>();
		if (equipe != null && !equipe.isEmpty()) {
			query.put("equipe", equipe);
		}

		return service.get("/query", query).getAsJsonArray();
	}

	private void assertProjeto(int idPma, String nome, String equipe, double cpi, JsonObject projetoJson) {
		assertEquals(idPma, projetoJson.get("idPma").getAsInt());
		assertEquals(nome, projetoJson.get("nome").getAsString());
		assertEquals(equipe, projetoJson.get("equipe").getAsString());
		assertEquals(cpi, projetoJson.get("cpi").getAsDouble(), 0.001);
	}

	@Test
	public void testAlterarIndicadores() {
		alteraIndicadorDeProjeto(ID_PROJETO_CONTPLAY, ID_INDICADOR_1, Classificacao.OK);
		verificaSeProjetoEstaComIndicadorPreenchido(ID_PROJETO_CONTPLAY, ID_INDICADOR_1, Classificacao.OK);
	}
	
	@Test
	public void testAlterarIndicadores2() {
		alteraIndicadorDeProjeto(ID_PROJETO_CONTPLAY, ID_INDICADOR_2, Classificacao.ATENCAO);
		verificaSeProjetoEstaComIndicadorPreenchido(ID_PROJETO_CONTPLAY, ID_INDICADOR_2, Classificacao.ATENCAO);
	}

	@Test
	public void testAtrasoIndicadorJson() {
		carregaProjetos();
		
		for (int i = 1; i <= 6; i++) {
			alteraIndicadorDeProjeto(ID_PROJETO_CONTPLAY, i, Classificacao.ATENCAO);
		}
		
		JsonObject projeto = getProjeto((long) ID_PROJETO_CONTPLAY);
		JsonArray indicadores = projeto.get("indicadores").getAsJsonArray();
		JsonObject indicador = indicadores.get(0).getAsJsonObject();
		
		Assert.assertFalse(indicador.get("atrasado").getAsBoolean());
	}

	private void verificaSeProjetoEstaComIndicadorPreenchido(int idProjeto, 
			int idIndicadorAlterado, Classificacao classificacao) {
		
		
		JsonObject projeto = this.getProjeto(idProjeto);
		JsonArray indicadores = projeto.get("indicadores").getAsJsonArray();
		
		JsonObject indicadorAlterado = encontraIndicadorDeId(indicadores, idIndicadorAlterado);
		
		String classificacaoAlterada = indicadorAlterado.get("classificacao").getAsString();
		JsonArray registrosAlterados = indicadorAlterado.get("registros").getAsJsonArray();
		JsonObject ultimaAlteracao = registrosAlterados.get(0).getAsJsonObject();
		JsonElement dataAlterada = ultimaAlteracao.get("data");
		String usuarioAlterado = ultimaAlteracao.get("usuario").getAsString();

		Assert.assertEquals(classificacao, Classificacao.valueOf(classificacaoAlterada));
		Assert.assertNotNull(dataAlterada);
		Assert.assertEquals("test@dextra-sw.com", usuarioAlterado);

	}

	private JsonObject encontraIndicadorDeId(JsonArray indicadores, int idParaEncontrar) {
		for (JsonElement el : indicadores) {
			JsonObject indicador = el.getAsJsonObject();
			int id = indicador.get("id").getAsInt();
			if (id == idParaEncontrar) {
				return indicador;
			}
		}
		throw new NoSuchElementException(Integer.toString(idParaEncontrar));
	}
}
