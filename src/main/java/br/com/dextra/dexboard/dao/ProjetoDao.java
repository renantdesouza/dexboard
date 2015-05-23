package br.com.dextra.dexboard.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class ProjetoDao {

	private static final Logger LOG = LoggerFactory.getLogger(ProjetoDao.class);

	public static final String KEY_CACHE = "dexboard.cache.key";
	public static final String HISTORY_CACHE = "dexlife.cache.key";

	private Objectify ofy;

	public ProjetoDao() {
		RegisterClasses.register();
		ofy = ObjectifyService.ofy();
	}

	public void salvarProjeto(Projeto p) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		ofy.save().entity(p);
	}

	public Projeto buscarProjeto(Long idProjeto) {
		return ofy.load().type(Projeto.class).id(idProjeto).now();
	}

	public List<Projeto> buscarTodosProjetos() {
		return buscarTodosProjetos(true, null);
	}
	
	public Projeto buscarProjetoByKey(Key<Projeto> key) {
		LoadResult<Projeto> first = ofy.load().type(Projeto.class).filterKey(key).first();
		return first.now();
	}
	
	public Indicador buscarIndicadorByKey(Key<Indicador> key) {
		LoadResult<Indicador> first = ofy.load().type(Indicador.class).filterKey(key).first();
		return first.now();
	}
	
	public List<RegistroAlteracao> buscarHistoricoAlteracoes(Date minDate, Integer limit) {
	
		List<RegistroAlteracao> list;

		Query<RegistroAlteracao> queryByDate = ofy.load().type(RegistroAlteracao.class).filter("date > ", minDate);
		queryByDate.order("date");
		if (limit != null)
			queryByDate.limit(limit);
		list = queryByDate.list();

		if (list == null || list.size() == 0) {
			if (limit != null)
				list = ofy.load().type(RegistroAlteracao.class).limit(limit).list();
			
			if (list == null)
				list = new ArrayList<RegistroAlteracao>();
		}
		return list;
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

		if(indicador.getProjeto().getId() == 619) {
			for(RegistroAlteracao reg : list) {
				LOG.info(indicador.getNome() + "-" + reg.getUsuario() + "-" + reg.getData());
			}
		}

		return list;
	}

	public void salvaIndicador(Long idProjetoPma, Indicador indicador) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		indicador.setProjeto(keyProjeto);
		indicador.defineComposeId();
		ofy.save().entity(indicador);
	}

	public void salvaAlteracao(Long idProjetoPma, Long idIndicador, RegistroAlteracao registroAlteracao) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		Key<Indicador> keyIndicador = Key.create(Indicador.class, idProjetoPma + ";" + idIndicador);

		registroAlteracao.setIndicador(keyIndicador);
		registroAlteracao.setProjeto(keyProjeto);
		registroAlteracao.defineId();
		ofy.save().entity(registroAlteracao);
	}

}
