package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import flexjson.JSONDeserializer;

public class IndicadorServlet extends HttpServlet {

	private static final long serialVersionUID = -7416705488396246559L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");

		Long idProjeto = Long.valueOf(req.getParameter("projeto"));
		Long idIndicador = Long.valueOf(req.getParameter("indicador"));

		JSONDeserializer<RegistroAlteracao> des = new JSONDeserializer<RegistroAlteracao>();
		String json = req.getParameter("registro");
		RegistroAlteracao regAlteracao = des.deserialize(json, RegistroAlteracao.class);

		UserService userService = UserServiceFactory.getUserService();

		regAlteracao.setUsuario(userService.getCurrentUser().getEmail());
		regAlteracao.setData(new Date());

		ProjetoDao dao = new ProjetoDao();
		dao.salvaAlteracao(idProjeto, idIndicador, regAlteracao);

		resp.getWriter().println("true");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
