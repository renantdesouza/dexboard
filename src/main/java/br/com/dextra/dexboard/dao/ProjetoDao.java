package br.com.dextra.dexboard.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ProjetoDao {

	public static final String KEY_CACHE = "dexboard.cache.key";

	private Objectify ofy;

	public ProjetoDao() {
		RegisterClasses.register();
		ofy = ObjectifyService.begin();
	}

	public void salvarProjeto(Projeto p) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		ofy.put(p);
	}

	public Projeto buscarProjeto(Long idProjeto) {
		Key<Projeto> key = new Key<Projeto>(Projeto.class, idProjeto);
		return ofy.get(key);
	}

	public List<Projeto> buscarTodosProjetos() {
		List<Projeto> list = ofy.query(Projeto.class).list();
		if (list == null) {
			list = new ArrayList<Projeto>();
		}
		return list;
	}

	public List<Indicador> buscarIndicadoresDoProjeto(Long idPma) {
		Projeto projeto = buscarProjeto(idPma);
		List<Indicador> list = ofy.query(Indicador.class)
				.filter("projeto", projeto).list();
		return list;
	}

	public List<RegistroAlteracao> buscarRegistrosDeAlteracoes(
			Indicador indicador) {
		List<RegistroAlteracao> list = ofy.query(RegistroAlteracao.class)
				.filter("indicador", indicador).list();
		if (list == null) {
			list = new ArrayList<RegistroAlteracao>();
		}
		return list;
	}

	public void salvaIndicador(Long idProjetoPma, Indicador indicador) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		Key<Projeto> keyProjeto = new Key<Projeto>(Projeto.class, idProjetoPma);
		indicador.setProjeto(keyProjeto);
		indicador.defineComposeId();
		ofy.put(indicador);
	}

	public void salvaAlteracao(Long idProjetoPma, Long idIndicador,
			RegistroAlteracao registroAlteracao) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		Key<Projeto> keyProjeto = new Key<Projeto>(Projeto.class, idProjetoPma);
		Key<Indicador> keyIndicador = new Key<Indicador>(Indicador.class,
				idProjetoPma + ";" + idIndicador);

		registroAlteracao.setIndicador(keyIndicador);
		registroAlteracao.setProjeto(keyProjeto);
		registroAlteracao.defineId();
		ofy.put(registroAlteracao);
	}

}
