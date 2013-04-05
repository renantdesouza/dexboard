package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;

import flexjson.JSONDeserializer;

public class IndicadorServlet extends HttpServlet {

	private static final long serialVersionUID = -7416705488396246559L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");

		Integer idProjeto = Integer.valueOf(req.getParameter("projeto"));

		JSONDeserializer<Indicador> des = new JSONDeserializer<Indicador>();
		String json = req.getParameter("indicador");
		Indicador indicador = des.deserialize(json, Indicador.class);

		UserService userService = UserServiceFactory.getUserService();

		indicador.setUsuarioUltimaAlteracao(userService.getCurrentUser()
				.getEmail());
		indicador.setUltimaAlteracao(new Date());

		ProjetoDao dao = new ProjetoDao();
		dao.salvaIndicador(idProjeto.longValue(), indicador);

		resp.getWriter().println("true");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
