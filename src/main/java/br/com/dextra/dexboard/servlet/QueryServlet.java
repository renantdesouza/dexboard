package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.json.ProjetoJson;
import br.com.dextra.dexboard.repository.ProjetoComparator;
import flexjson.JSONSerializer;

public class QueryServlet extends HttpServlet {

	private static final String EQUIPE_HTTP_PARAMETER = "equipe";

	private static final long serialVersionUID = -1248500946944090403L;

	public static final int CACHE_EXPIRATION_SECONDS = 60 * 60;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.getWriter().print(getJsonProjetosWithCache(req.getParameter(EQUIPE_HTTP_PARAMETER)));
	}

	private String getJsonProjetosWithCache(String equipe) {
		MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

		if(useCache(equipe)) {
			String cacheJson = (String) memcacheService.get(ProjetoDao.KEY_CACHE);
			if (cacheJson != null) {
				return cacheJson;
			}
		}

		String json = getJsonProjetos(equipe);

		if(useCache(equipe)) {
			memcacheService.put(ProjetoDao.KEY_CACHE, json);
		}

		return json;
	}

	private String getJsonProjetos(String equipe) {
		ProjetoDao dao = new ProjetoDao();
		List<Projeto> projetos = dao.buscarTodosProjetos(true, equipe);
		Collections.sort(projetos, new ProjetoComparator());

		List<ProjetoJson> projetosJson = Projeto.toProjetoJson(projetos);
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class", "*.projeto");
		String json = serializer.deepSerialize(projetosJson);
		return json;
	}

	private boolean useCache(String equipe) {
		return equipe == null;
	}
}
