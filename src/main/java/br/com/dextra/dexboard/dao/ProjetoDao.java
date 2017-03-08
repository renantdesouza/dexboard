package br.com.dextra.dexboard.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

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
		ofy.save().entity(p).now();
	}

	public Projeto buscarProjeto(Long idProjeto) {
		return ofy.load().type(Projeto.class).id(idProjeto).now();
	}

	public Projeto buscarProjetoByKey(Key<Projeto> key) {
		return ofy.load().type(Projeto.class).filterKey(key).first().now();
	}

	public Indicador buscarIndicadorByKey(Key<Indicador> key) {
		return ofy.load().type(Indicador.class).filterKey(key).first().now();
	}

	public List<RegistroAlteracao> buscarHistoricoAlteracoes(Date minDate, Integer limit) {
		List<RegistroAlteracao> list = ofy.load().type(RegistroAlteracao.class).filter("data >=", minDate).list();

		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}

		Collections.reverse(list);

		return list.size() <= limit-1 ? list : list.subList(0, limit);
	}

	public List<Projeto> buscarTodosProjetos() {
		return ofy.load().type(Projeto.class).filter("ativo", true).list();
	}

	public List<Projeto> buscarProjetosInativos() {
		return ofy.load().type(Projeto.class).filter("ativo", false).list();
	}

	public List<Projeto> buscarProjetosEquipe(String equipe) {
		List<Projeto> ativos = new ArrayList<>();

		for (Projeto projeto : ofy.load().type(Projeto.class).filter("equipe", equipe.toUpperCase()).list()) {
			if (projeto.isAtivo()) {
				ativos.add(projeto);
			}
		}

		return ativos;
	}

	public List<Projeto> buscarTodosProjetos(String equipe) {
		return equipe == null || equipe.trim().isEmpty() ? buscarTodosProjetos() : buscarProjetosEquipe(equipe);
	}

	public List<Indicador> buscarIndicadoresDoProjeto(Long idPma) {
		return ofy.load().type(Indicador.class).filter("projeto", buscarProjeto(idPma)).list();
	}

	public List<RegistroAlteracao> buscarRegistrosDeAlteracoes(Indicador indicador) {
		List<RegistroAlteracao> list = ofy.load().type(RegistroAlteracao.class).filter("indicador", indicador).list();
		if (list == null) {
			list = new ArrayList<>();
		}

		if (indicador.getProjeto().getId() == 619) {
			for (RegistroAlteracao reg : list) {
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

	public RegistroAlteracao salvaAlteracao(Long idProjetoPma, Long idIndicador, RegistroAlteracao registroAlteracao) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		Key<Indicador> keyIndicador = Key.create(Indicador.class, idProjetoPma + ";" + idIndicador);

		registroAlteracao.setIndicador(keyIndicador);
		registroAlteracao.setProjeto(keyProjeto);
		registroAlteracao.defineId();
		ofy.save().entity(registroAlteracao).now();

		return registroAlteracao;
	}

}
