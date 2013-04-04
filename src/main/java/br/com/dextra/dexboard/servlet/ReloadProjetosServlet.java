package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;
import br.com.dextra.dexboard.service.ProjetoPlanilhaService;

public class ReloadProjetosServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;
	private ProjetoDao dao = new ProjetoDao();
	private List<Indicador> indicadores = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");
		PlanilhaIndicadores planilhaIndicadores = new PlanilhaIndicadores();
		this.indicadores = planilhaIndicadores.criarListaDeIndicadores();

		Map<Integer, Projeto> projetosPlanilha = ProjetoPlanilhaService
				.buscarDadosProjetosAtivos();
		List<Projeto> projetosDataStore = dao.buscarTodosProjetos();

		atualizaProjetosAtivos(projetosPlanilha, projetosDataStore);
		projetosDataStore = dao.buscarTodosProjetos();

		adicionaProjetosNovos(projetosDataStore, projetosPlanilha.values());

		resp.getWriter().print("{status: 'success'}");
	}

	private void adicionaProjetosNovos(List<Projeto> destino,
			Collection<Projeto> ativos) {

		for (Projeto p : ativos) {
			if (!projetoEstaNaLista(p, destino)) {
				dao.salvarProjeto(p);
				for (Indicador i : indicadores) {
					dao.salvaIndicador(p.getIdPma(), i);
				}
			}
		}
	}

	private void atualizaProjetosAtivos(Map<Integer, Projeto> projetosPlanilha,
			List<Projeto> projetosEmCache) {

		if (projetosEmCache != null) {
			for (Projeto p : projetosEmCache) {
				Projeto projetoAtivo = projetosPlanilha.get(p.getIdPma());
				if (projetoAtivo != null) {
					p.setCpi(projetoAtivo.getCpi());
					dao.salvarProjeto(p);
				} else {
					dao.delete(p.getIdPma());
				}
			}
		}

	}

	private boolean projetoEstaNaLista(Projeto projeto, List<Projeto> projetos) {
		for (Projeto p : projetos) {
			if (p.getIdPma() == projeto.getIdPma()) {
				return true;
			}
		}
		return false;
	}

}
