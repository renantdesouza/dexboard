package br.com.dextra.dexboard.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.dextra.dexboard.api.base.IntegrationTest;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.json.IndicadorJson;

public class ITestDexboard extends IntegrationTest {

	private static final int ID_PROJETO_CONTPLAY = 495;
	private static final int ID_INDICADOR_1 = 1;
	private static final int ID_INDICADOR_2 = 2;

	@Before
	public void before() {
		carregaProjetos();
	}

	@Test
	public void testQueryProjetos() {
		JsonArray projetos = queryProjetosJson(null);

		assertEquals(23, projetos.size());
		
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
		
		return this.service.get("/query", query).getAsJsonArray();
	}

	private void assertProjeto(int idPma, String nome, String equipe, double cpi, JsonObject projetoJson) {
		assertEquals(idPma, projetoJson.get("idPma").getAsInt());
		assertEquals(nome, projetoJson.get("nome").getAsString());
		assertEquals(equipe, projetoJson.get("equipe").getAsString());
		assertEquals(cpi, projetoJson.get("cpi").getAsDouble(), 0.001);
	}

	@Test
	public void testAlterarIndicadores() throws IOException, SAXException {
		alteraIndicadorDeProjeto(ID_PROJETO_CONTPLAY, ID_INDICADOR_1, Classificacao.OK);
		alteraIndicadorDeProjeto(ID_PROJETO_CONTPLAY, ID_INDICADOR_2, Classificacao.ATENCAO);

		verificaSeProjetoEstaComIndicadorPreenchido(ID_PROJETO_CONTPLAY, ID_INDICADOR_1, Classificacao.OK);
		verificaSeProjetoEstaComIndicadorPreenchido(ID_PROJETO_CONTPLAY, ID_INDICADOR_2, Classificacao.ATENCAO);
	}

	@Test
	public void testAtrasoIndicadorJson() throws Exception {
		carregaProjetos();

		for (int i = 1; i <= 6; i++) {
			alteraIndicadorDeProjeto(ID_PROJETO_CONTPLAY, i, Classificacao.ATENCAO);
		}

		ProjetoDao dao = new ProjetoDao();
		List<Indicador> indicadores = dao.buscarIndicadoresDoProjeto(495l);
		Indicador indicador = indicadores.get(0);
		IndicadorJson indicadorJson = new IndicadorJson(indicador);
		Assert.assertFalse(indicadorJson.getAtrasado());
		System.setProperty("validade", "-3");
		Assert.assertTrue(indicadorJson.getAtrasado());
	}


	private void verificaSeProjetoEstaComIndicadorPreenchido(int idProjeto, int idIndicadorAlterado, Classificacao classificacao) {
		ProjetoDao dao = new ProjetoDao();
		List<Indicador> indicadores = dao.buscarIndicadoresDoProjeto(new Long(idProjeto));

		IndicadorJson indicadorAlterado = encontraIndicadorDeId(indicadores, idIndicadorAlterado);

		Assert.assertNotNull(indicadorAlterado);
		Assert.assertEquals(classificacao, indicadorAlterado.getClassificacao());
		RegistroAlteracao registroAlteracao = indicadorAlterado.getRegistros().get(0);
		Assert.assertNotNull(registroAlteracao.getData());
		Assert.assertEquals("test@dextra-sw.com", registroAlteracao.getUsuario());

	}

	private IndicadorJson encontraIndicadorDeId(List<Indicador> indicadores, int idParaEncontrar) {
		for (Indicador ind : indicadores) {
			if (ind.getId().intValue() == idParaEncontrar) {
				return new IndicadorJson(ind);
			}
		}
		return null;
	}
}
