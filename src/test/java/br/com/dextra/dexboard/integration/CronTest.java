package br.com.dextra.dexboard.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.AbstractTestCase;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.repository.ProjetoRepository;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

import com.googlecode.restitory.gae.http.Response;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class CronTest extends AbstractTestCase {

	@Test
	public void testaCarregarProjetosEAlterarIndicadores() throws IOException, SAXException {

		carregaProjetos();
		alteraIndicadorDeProjeto(495, 1);
		alteraIndicadorDeProjeto(495, 2);
		verificaSeProjetoEstaComIndicadorPreenchido(495, 1);
		verificaSeProjetoEstaComIndicadorPreenchido(495, 2);
	}

	private void verificaSeProjetoEstaComIndicadorPreenchido(Integer idProjeto, Integer idIndicadorAlterado) {

		Projeto projeto = ProjetoRepository.buscarPorId(495, ProjetoRepository.buscaProjetos());
		List<Indicador> indicadores = projeto.getIndicadores();
		
		Indicador indicadorAlterado = encontraIndicadorDeId(indicadores, 1);

		Assert.assertNotNull(indicadorAlterado);
		Assert.assertEquals(2, indicadorAlterado.getCor());
		Assert.assertNotNull(indicadorAlterado.getUltimaAlteracao());
		Assert.assertEquals("test@example.com", indicadorAlterado.getUsuarioUltimaAlteracao());
		
	}

	private Indicador encontraIndicadorDeId(List<Indicador> indicadores, int idParaEncontrar) {
		for (Indicador ind : indicadores) {
			if (ind.getId().intValue() == idParaEncontrar) {
				return ind;
			}
		}
		return null;
	}

	private void alteraIndicadorDeProjeto(Integer idProjeto, Integer idIndicador) throws IOException, SAXException {

		ServletRunner sr = new ServletRunner();
		sr.registerServlet("indicadorServlet", IndicadorServlet.class.getName());
		
		ServletUnitClient sc = sr.newClient();
	    WebRequest request = new PostMethodWebRequest("http://localhost:8380/indicadorServlet");
	    request.setParameter("projeto", idProjeto.toString());
	    request.setParameter("indicador", "{ 'id' : '" + idIndicador + "', 'nome' : 'NomeBla', 'cor' : '2', 'descricao': 'desc desc' }");

	    sc.getResponse(request);
	}

	private void carregaProjetos() {

		Response resp = adapter.success("GET", "/reload/projetos", null, null);		
		String status = getJson(resp).get("status").getAsString();
		assertEquals("success", status);

	}

}
