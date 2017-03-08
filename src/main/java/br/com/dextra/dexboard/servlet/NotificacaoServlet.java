package br.com.dextra.dexboard.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.domain.Projeto;

public class NotificacaoServlet extends HttpServlet {

	private static final long serialVersionUID = -9049212521731856178L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		notificarProjetosAtrasados();
		resp.getWriter().print("{status: 'success'}");
	}

	private void notificarProjetosAtrasados() {
		NotificacaoDao dao = new NotificacaoDao();
		for (Projeto projeto : dao.buscarProjetosParaNotificar()) {
			dao.notificarEquipeProjeto(projeto);
		}
	}
}
