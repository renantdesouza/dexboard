package br.com.dextra.dexboard.dao;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

import com.googlecode.objectify.ObjectifyService;

public class RegisterClasses {

	public static void register() {
		if (System.getProperty("app.objectfy.classes.registered") != null) {
			return;
		}

		ObjectifyService.register(Projeto.class);
		ObjectifyService.register(Indicador.class);
		ObjectifyService.register(RegistroAlteracao.class);

		System.setProperty("app.objectfy.classes.registered", "true");
	}
}
