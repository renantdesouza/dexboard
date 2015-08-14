package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.domain.Projeto;

public class NotificacaoServlet extends HttpServlet {

	private static final long serialVersionUID = -9049212521731856178L;

	private NotificacaoDao dao = new NotificacaoDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		notificarProjetosAtrasados();
		resp.getWriter().print("{status: 'success'}");
	}

	private void notificarProjetosAtrasados() {

		List<Projeto> projetos = dao.buscarProjetosParaNotificar();
		for (Projeto projeto : projetos) {
			dao.notificarEquipeProjeto(projeto);
		}
	}
}
