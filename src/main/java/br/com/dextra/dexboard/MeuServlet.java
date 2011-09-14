package br.com.dextra.dexboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;

public class MeuServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// XXX
		int id = Integer.parseInt(req.getPathInfo().split("/")[1]);

		JsonElement dados = PmaDataFetcher.fetchData(id);

		resp.setContentType("application/json");
		resp.getWriter().println(dados);
	}
}
