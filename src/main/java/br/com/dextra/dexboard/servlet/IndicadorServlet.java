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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import flexjson.JSONDeserializer;

public class IndicadorServlet extends HttpServlet {

	private static final long serialVersionUID = -7416705488396246559L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");

		Integer idProjeto = Integer.valueOf(req.getParameter("projeto"));

		JSONDeserializer<Indicador> des = new JSONDeserializer<Indicador>();
		Indicador indicador = des.deserialize(req.getParameter("indicador"),
				Indicador.class);

		List<Projeto> todosProjetos = ProjetoRepository.buscaProjetos();
		UserService userService = UserServiceFactory.getUserService();

		Projeto projeto = ProjetoRepository.buscarPorId(idProjeto,
				todosProjetos);
		projeto.alteraIndicador(indicador, userService.getCurrentUser()
				.getEmail());
		ProjetoRepository.persisteProjetos(todosProjetos);

		resp.getWriter().println("true");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
