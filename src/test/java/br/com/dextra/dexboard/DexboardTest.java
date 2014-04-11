package br.com.dextra.dexboard;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.DexboardTestCase;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.json.IndicadorJson;

import com.googlecode.restitory.gae.http.Response;

import flexjson.JSONDeserializer;

public class DexboardTest extends DexboardTestCase {

	private static final int ID_PROJETO_CONTPLAY = 495;
	private static final int ID_INDICADOR_1 = 1;
	private static final int ID_INDICADOR_2 = 2;

	@Before
	public void before() {
		carregaProjetos();
	}

	@Test
	public void testQueryProjetos() {
		List<Map<Object, Object>> projetos = queryProjetosJson(null);

		assertEquals(23, projetos.size());

		assertProjeto(495, "A4C", "CHAOS", 1.01, projetos.get(0));
		assertProjeto(585, "ADV: Fase II", "MUSTACHE", 1.39, projetos.get(1));
	}

	@Test
	public void testQueryProjetosEquipe() {
		List<Map<Object, Object>> projetos = queryProjetosJson("Rocket");

		assertEquals(2, projetos.size());

		assertProjeto(565, "Confidence", "ROCKET", 0.99, projetos.get(0));
		assertProjeto(579, "Movile", "ROCKET", 1.70, projetos.get(1));

	}

	private List<Map<Object, Object>> queryProjetosJson(String equipe) {
		String path = "/query" + (equipe != null ? "?equipe=" + equipe : "");
		Response response = adapter.success("GET", path, null, null);
		return new JSONDeserializer<List<Map<Object, Object>>>().use(null, ArrayList.class).deserialize(response.getContent().getText());
	}

	private void assertProjeto(int idPma, String nome, String equipe, double cpi, Map<Object, Object> projetoJson) {
		assertEquals(idPma, projetoJson.get("idPma"));
		assertEquals(nome, projetoJson.get("nome"));
		assertEquals(equipe, projetoJson.get("equipe"));
		assertEquals(cpi, projetoJson.get("cpi"));
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
