package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.repository.ProjetoRepository;

import com.google.appengine.api.datastore.EntityNotFoundException;

import flexjson.JSONDeserializer;

public class IndicadorServlet extends HttpServlet{
	
	private static final long serialVersionUID = -7416705488396246559L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer idProjeto = Integer.valueOf(req.getParameter("projeto"));
		
		JSONDeserializer<Indicador> deserializer = new JSONDeserializer<Indicador>();
		Indicador indicador = deserializer.deserialize(req.getParameter("indicador"));
		
		List<Projeto> todosProjetos;
		try {
			todosProjetos = ProjetoRepository.buscaProjetos();
		} catch (EntityNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Projeto projeto = ProjetoRepository.buscarPorId(idProjeto, todosProjetos);
		projeto.alteraIndicador(indicador);

		ProjetoRepository.persisteProjetos(todosProjetos);
	}

}
