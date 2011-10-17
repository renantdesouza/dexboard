package br.com.dextra.dexboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LimparServlet extends HttpServlet {

	private static final long serialVersionUID = 3502616082193987724L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		if (pathInfo.equals("/limpar")) {
			PlanilhaProjeto.limparCache();
			resp.sendRedirect("/index.html");
		} else {
			resp.getWriter().print("Caminho invalido: " + pathInfo);
		}

	}

}
