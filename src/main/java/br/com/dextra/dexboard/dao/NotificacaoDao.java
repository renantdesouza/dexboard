package br.com.dextra.dexboard.dao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Notificacao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class NotificacaoDao {

	private static final Logger LOG = LoggerFactory.getLogger(NotificacaoDao.class);

	private static final int DIAS_PARA_NOTIFICAR = 3;
	private static final int DIAS_PARA_RENOTIFICAR = 4;

	private Objectify ofy;

	public NotificacaoDao() {
		RegisterClasses.register();
		ofy = ObjectifyService.ofy();
	}

	public List<Projeto> buscarProjetosParaNotificar() {
		List<Projeto> projetosAtrasados = buscarProjetosAtrasados();

		List<Projeto> projetosParaNotificar = new ArrayList<Projeto>();

		for (Projeto projeto : projetosAtrasados) {

			Notificacao notificacao = buscarUltimaNotificacao(projeto);

			if (notificacao == null || calculaDiasAtras(notificacao.getDate()) == DIAS_PARA_RENOTIFICAR) {
				projetosParaNotificar.add(projeto);
				continue;
			}

		}

		return projetosParaNotificar;
	}

	private Notificacao buscarUltimaNotificacao(Projeto projeto) {
		return ofy.load().type(Notificacao.class).filter("idPma", projeto.getIdPma()).order("-data").first().now();
	}

	private List<Projeto> buscarProjetosAtrasados() {
		List<Projeto> projetosAtrasados = new ArrayList<Projeto>();

		List<Projeto> projetos = buscarProjetos();
		for (Projeto projeto : projetos) {
			List<Indicador> indicadores = buscarIndicadores(projeto);
			for (Indicador indicador : indicadores) {
				RegistroAlteracao registroAlteracao = buscarRegistrosAletracoes(projeto, indicador);

				if (registroAlteracao == null) {
					projetosAtrasados.add(projeto);
					break;
				}

				long diasAtras = calculaDiasAtras(registroAlteracao.getData());

				if (diasAtras >= getValidadeAlteracao() - DIAS_PARA_NOTIFICAR) {
					projetosAtrasados.add(projeto);
					break;
				}
			}

		}
		return projetosAtrasados;
	}

	private long calculaDiasAtras(Date data) {
		return (new Date().getTime() - data.getTime()) / (1000 * 60 * 60 * 24);
	}

	private RegistroAlteracao buscarRegistrosAletracoes(Projeto projeto, Indicador indicador) {
		return ofy.load().type(RegistroAlteracao.class).filter("indicador", indicador).order("-data").first().now();
	}

	private List<Indicador> buscarIndicadores(Projeto projeto) {
		return ofy.load().type(Indicador.class).filter("projeto", projeto).list();
	}

	private List<Projeto> buscarProjetos() {
		return ofy.load().type(Projeto.class).filter("ativo", true).list();
	}

	public static int getValidadeAlteracao() {
		String validadeProp = System.getProperty("validade");
		if (validadeProp != null) {
			return Integer.parseInt(validadeProp);
		}
		return 20;
	}

	public void notificarEquipeProjeto(Projeto projeto) {
		envioarEmailEquipeProjeto(projeto);
		registrarDataNotificacao(projeto);
	}

	private void envioarEmailEquipeProjeto(Projeto projeto) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "<img src=\"http://dexboard-reload.appspot.com/img/lazy.jpg\"/>";

		Message msg = new MimeMessage(session);

		LOG.info("Enviando email de notificacao: projeto=" + projeto.getNome() + ", equipe=" + projeto.getEquipe() + ", email="
				+ projeto.getEmail());

		try {
			msg.setFrom(new InternetAddress("dexboard@dextra-sw.com", "Dexboard Reload"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(projeto.getEmail(), projeto.getEquipe()));
			msg.setSubject("Atualizar projeto ;)");
			msg.setContent(msgBody, "text/html");
			Transport.send(msg);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
	}

	private void registrarDataNotificacao(Projeto projeto) {
		Notificacao notificacao = new Notificacao();
		notificacao.setDate(new Date());
		notificacao.setIdPma(projeto.getIdPma());
		ofy.save().entity(notificacao);
	}
}
