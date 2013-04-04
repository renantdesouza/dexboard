package br.com.dextra.dexboard.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ProjetoDao {

	private Objectify ofy;

	public ProjetoDao() {
		ObjectifyService.register(Projeto.class);
		ObjectifyService.register(Indicador.class);

		ofy = ObjectifyService.begin();
	}

	public void salvarProjeto(Projeto p) {
		ofy.put(p);
	}

	public Projeto buscarProjeto(int idPma) {
		Key<Projeto> key = new Key<Projeto>(Projeto.class, idPma);
		return ofy.get(key);
	}

	public List<Projeto> buscarTodosProjetos() {
		List<Projeto> list = ofy.query(Projeto.class).list();
		if (list == null) {
			list = new ArrayList<Projeto>();
		}
		return list;
	}

	public List<Indicador> buscarIndicadoresDoProjeto(int idPma) {
		Projeto projeto = buscarProjeto(idPma);
		List<Indicador> list = ofy.query(Indicador.class)
				.filter("projeto", projeto).list();
		return list;
	}

	public void salvaIndicador(int idProjetoPma, Indicador indicador) {
		Key<Projeto> keyProjeto = new Key<Projeto>(Projeto.class, idProjetoPma);
		indicador.setProjeto(keyProjeto);
		ofy.put(indicador);
	}

	public void delete(int idPma) {
		Projeto projeto = buscarProjeto(idPma);
		List<Key<Indicador>> listKeys = ofy.query(Indicador.class).filter("projeto", projeto).listKeys();
		ofy.delete(listKeys);
		Key<Projeto> key = new Key<Projeto>(Projeto.class, idPma);
		ofy.delete(key);
	}
}
