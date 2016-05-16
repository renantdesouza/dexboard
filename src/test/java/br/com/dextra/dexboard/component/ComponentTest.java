package br.com.dextra.dexboard.component;

import java.io.Closeable;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

import com.googlecode.objectify.ObjectifyService;

import br.com.dextra.dexboard.servlet.Context;

/**
 * Testes de componente testam a aplicacao de forma isolada, usando dubles
 * para representar os demais componentes como banco e servicos externos.
 * 
 */
public class ComponentTest {

	private Closeable objectfy;
	private Closeable localGaeEnvironment;

	@Before
	public void setUp() {
		Context.forceMock(true);
		System.setProperty("validade", "15");
		this.objectfy = ObjectifyService.begin();
		this.localGaeEnvironment = LocalGaeEnvironment.setUp();
	}
	
	@After
	public void tearDown() throws IOException {
		Context.forceMock(false);
		this.objectfy.close();
		this.localGaeEnvironment.close();
	}

}
