package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.repository.ProjetoRepository;
import br.com.dextra.dexboard.service.DadosProjeto;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class DadosServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");
		List<Projeto> projetos = new ArrayList<Projeto>();
		List<Projeto> projetosPlanilha = DadosProjeto.buscarDadosProjetos();
		List<Projeto> projetosDataStore;

		try {
			projetosDataStore = ProjetoRepository.buscaProjetos();
		} catch (EntityNotFoundException e) {
			throw new RuntimeException(e);
		}

		adicionaProjetosAtivos(projetos, projetosPlanilha, projetosDataStore);
		adicionaProjetosNovos(projetos, projetosPlanilha);

		ProjetoRepository.persisteProjetos(projetos);

		resp.getWriter().print("true");
	}

	private void adicionaProjetosNovos(List<Projeto> destino,
			List<Projeto> origem) {
		
		for (Projeto p : origem) {
			if (!projetoEstaNaLista(p, destino)) {
				destino.add(p);
			}
		}
	}

	private void adicionaProjetosAtivos(List<Projeto> destino,
			List<Projeto> origem, List<Projeto> projetosEmCache) {

		if (projetosEmCache != null) {
			for (Projeto p : projetosEmCache) {
				if (projetoEstaNaLista(p, origem)) {
					destino.add(p);
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
