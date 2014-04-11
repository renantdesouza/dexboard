package br.com.dextra.dexboard;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.DexboardTestCase;
import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

public class NotificacaoTest extends DexboardTestCase {

	private static final long ID_PROJETO_CONTPLAY = 495l;
	private static final long ID_PROJETO_CONFIDENCE = 565l;

	@Test
	public void testNotificacaoAtraso() throws Exception {
		carregaProjetos();
		registraAlteracoesEmProjetos();

		NotificacaoDao dao = new NotificacaoDao();

		List<Projeto> projetos = dao.buscarProjetosParaNotificar();
		assertEquals(2, projetos.size());

		dao.notificarEquipeProjeto(projetos.get(0));
		projetos = dao.buscarProjetosParaNotificar();
		assertEquals(1, projetos.size());

		dao.notificarEquipeProjeto(projetos.get(0));
		projetos = dao.buscarProjetosParaNotificar();
		assertEquals(0, projetos.size());
	}

	private void registraAlteracoesEmProjetos() throws IOException, SAXException {
		ProjetoDao dao = new ProjetoDao();

		List<Projeto> projetos = dao.buscarTodosProjetos();

		for (Projeto projeto : projetos) {

			if (projeto.getIdPma().equals(ID_PROJETO_CONFIDENCE)) {

				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -17);

				for (int i = 1; i <= 6; i++) {
					dao.salvaAlteracao(projeto.getIdPma(), (long) i, criaRegistroAlteracaoNaData(c));
				}
				continue;
			}

			if (projeto.getIdPma().equals(ID_PROJETO_CONTPLAY)) {
				continue;
			}

			for (int i = 1; i <= 6; i++) {
				alteraIndicadorDeProjeto(projeto.getIdPma(), i, Classificacao.ATENCAO);
			}
		}
	}

	private RegistroAlteracao criaRegistroAlteracaoNaData(Calendar c) {
		RegistroAlteracao registroAlteracao = new RegistroAlteracao();
		registroAlteracao.setClassificacao(Classificacao.OK);
		registroAlteracao.setData(c.getTime());
		registroAlteracao.setComentario("xpto");
		registroAlteracao.setUsuario("john");
		return registroAlteracao;
	}

}
