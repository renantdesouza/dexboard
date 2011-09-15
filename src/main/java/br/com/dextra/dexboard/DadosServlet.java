package br.com.dextra.dexboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DadosServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");

		String pathInfo = req.getPathInfo();

		if (pathInfo.equals("/projetos")) {
			resp.getWriter().print(Projeto.buscarDadosProjetos());
		} else {
			resp.getWriter().print("Caminho invalido: " + pathInfo);
		}
	}

}
