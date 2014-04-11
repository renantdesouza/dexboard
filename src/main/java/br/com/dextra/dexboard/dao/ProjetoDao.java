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
import com.googlecode.objectify.cmd.Query;

public class ProjetoDao {

	public static final String KEY_CACHE = "dexboard.cache.key";

	private Objectify ofy;

	public ProjetoDao() {
		RegisterClasses.register();
		ofy = ObjectifyService.ofy();
	}

	public void salvarProjeto(Projeto p) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		ofy.save().entity(p);
	}

	public Projeto buscarProjeto(Long idProjeto) {
		return ofy.load().type(Projeto.class).id(idProjeto).now();
	}

	public List<Projeto> buscarTodosProjetos() {
		return buscarTodosProjetos(true, null);
	}

	public List<Projeto> buscarTodosProjetos(boolean ativo, String equipe) {
		Query<Projeto> query = ofy.load().type(Projeto.class).filter("ativo", ativo);

		if (equipe != null) {
			query = query.filter("equipe", equipe.toUpperCase());
		}

		List<Projeto> list = query.list();

		if (list == null) {
			list = new ArrayList<Projeto>();
		}
		return list;
	}

	public List<Indicador> buscarIndicadoresDoProjeto(Long idPma) {
		Projeto projeto = buscarProjeto(idPma);
		List<Indicador> list = ofy.load().type(Indicador.class).filter("projeto", projeto).list();
		return list;
	}

	public List<RegistroAlteracao> buscarRegistrosDeAlteracoes(Indicador indicador) {
		List<RegistroAlteracao> list = ofy.load().type(RegistroAlteracao.class).filter("indicador", indicador).list();
		if (list == null) {
			list = new ArrayList<RegistroAlteracao>();
		}
		return list;
	}

	public void salvaIndicador(Long idProjetoPma, Indicador indicador) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		indicador.setProjeto(keyProjeto);
		indicador.defineComposeId();
		ofy.save().entity(indicador);
	}

	public void salvaAlteracao(Long idProjetoPma, Long idIndicador, RegistroAlteracao registroAlteracao) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		Key<Indicador> keyIndicador = Key.create(Indicador.class, idProjetoPma + ";" + idIndicador);

		registroAlteracao.setIndicador(keyIndicador);
		registroAlteracao.setProjeto(keyProjeto);
		registroAlteracao.defineId();
		ofy.save().entity(registroAlteracao);
	}

}
