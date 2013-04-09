package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.json.ProjetoJson;
import br.com.dextra.dexboard.repository.ProjetoComparator;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import flexjson.JSONSerializer;

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = -1248500946944090403L;

	public static final int CACHE_EXPIRATION_SECONDS = 60 * 60;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MemcacheService memcacheService = MemcacheServiceFactory
				.getMemcacheService();

		resp.setContentType("application/json");

		String cacheJson = (String) memcacheService.get(ProjetoDao.KEY_CACHE);
		if (cacheJson != null) {
			resp.getWriter().print(cacheJson);
		} else {
			ProjetoDao dao = new ProjetoDao();
			List<ProjetoJson> projetos = new ArrayList<ProjetoJson>();
			List<Projeto> buscarTodosProjetos = dao.buscarTodosProjetos();

			for (Projeto projeto : buscarTodosProjetos) {
				projetos.add(new ProjetoJson(projeto));
			}

			Collections.sort(projetos, new ProjetoComparator());

			JSONSerializer serializer = new JSONSerializer();
			serializer.exclude("*.class", "*.projeto");
			String json = serializer.deepSerialize(projetos);
			memcacheService.put(ProjetoDao.KEY_CACHE, json);

			resp.getWriter().print(json);
		}

	}

}
