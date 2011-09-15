package br.com.dextra.dexboard;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.googlecode.restitory.gae.filter.util.JsonUtil;

public class MeuServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();

		if (pathInfo.equals("/projetos")) {

			JsonArray dadosPlanilha = (JsonArray) PlanilhaDataFetcher.fetchData();

			JsonArray jsonArray = new JsonArray();
			for (JsonElement elemento : dadosPlanilha) {
				int idProjeto = Integer.parseInt(elemento.getAsJsonObject().get("id").getAsString());
				JsonElement dadosPma = PmaDataFetcher.fetchData(idProjeto);
				jsonArray.add(dadosPma);
			}

			resp.setContentType("application/json");
			resp.getWriter().print(jsonArray);
		}
	}
}
