package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;
import br.com.dextra.dexboard.service.ProjetoPlanilhaService;

public class ReloadProjetosServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReloadProjetosServlet.class);

	private static final long serialVersionUID = -1248500946944090403L;
	private ProjetoDao dao = new ProjetoDao();
	private List<Indicador> indicadores = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");
		PlanilhaIndicadores planilhaIndicadores = new PlanilhaIndicadores();
		LOG.info("Buscando lista de indicadores ...");
		this.indicadores = planilhaIndicadores.criarListaDeIndicadores();

		LOG.info("Buscando projetos ativos ...");
		Map<Long, Projeto> projetosPlanilha = ProjetoPlanilhaService
				.buscarDadosProjetosAtivos();
		LOG.info(projetosPlanilha.size() + " projetos ativos encontrados ...");

		LOG.info("Buscando projetos já registrados na data store ...");
		List<Projeto> projetosDataStore = dao.buscarTodosProjetos();
		LOG.info(projetosDataStore.size()
				+ " projetos registrados encontrados ...");

		atualizaProjetosAtivos(projetosPlanilha, projetosDataStore);

		LOG.info("Buscando projetos atualizados ...");
		projetosDataStore = dao.buscarTodosProjetos();
		Map<Long, Projeto> mapProjetosDataStore = createMapProjetos(projetosDataStore);

		adicionaProjetosNovos(mapProjetosDataStore, projetosPlanilha.values());

		LOG.info("Sucesso!");
		resp.getWriter().print("{status: 'success'}");
	}

	private Map<Long, Projeto> createMapProjetos(List<Projeto> projetos) {
		Map<Long, Projeto> map = new HashMap<Long, Projeto>();
		for (Projeto p : projetos) {
			map.put(p.getIdPma(), p);
		}
		return map;
	}

	private void adicionaProjetosNovos(Map<Long, Projeto> mapProjetosDataStore,
			Collection<Projeto> ativos) {

		for (Projeto p : ativos) {
			Projeto projeto = mapProjetosDataStore.get(p.getIdPma());
			if (projeto == null) {
				LOG.info(String.format("Adicionando projeto \"%s\"",
						p.getNome()));
				dao.salvarProjeto(p);
				for (Indicador i : indicadores) {
					dao.salvaIndicador(p.getIdPma(), i);
				}
			}
		}
	}

	private void atualizaProjetosAtivos(Map<Long, Projeto> projetosPlanilha,
			List<Projeto> projetosEmCache) {

		if (projetosEmCache != null) {
			for (Projeto p : projetosEmCache) {
				Projeto projetoAtivo = projetosPlanilha.get(p.getIdPma());
				if (projetoAtivo != null
						&& (!p.getCpi().equals(projetoAtivo.getCpi()) || !p.getNome().equals(projetoAtivo.getNome()))) {
					LOG.info(String.format(
							"Atualizando cpi do projeto \"%s\" para \"%s\"",
							projetoAtivo.getNome(), projetoAtivo.getCpi()
									.toString()));
					p.setNome(projetoAtivo.getNome());
					p.setCpi(projetoAtivo.getCpi());
					dao.salvarProjeto(p);
					LOG.info(String.format("Projeto \"%s\" salvo",
							projetoAtivo.getNome()));
				} else {
					LOG.info(String.format("Excluindo projeto \"%s\"",
							p.getNome()));
					dao.delete(p.getIdPma());
				}
			}
		}

	}

}
