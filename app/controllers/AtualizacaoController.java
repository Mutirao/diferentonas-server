package controllers;


import static akka.pattern.Patterns.ask;
import static play.libs.Json.toJson;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Atualizacao;
import models.AtualizacaoDAO;
import models.Cidadao;
import models.CidadaoDAO;
import play.Configuration;
import play.Logger;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.concurrent.duration.Duration;
import actors.AtualizadorActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;

@Singleton
@Security.Authenticated(AcessoAdmin.class)
public class AtualizacaoController extends Controller {

	private ActorRef atualizador;
	private AtualizacaoDAO daoAtualizacao;
	private CidadaoDAO daoCidadao;
	private ActorSystem system;
	private Cancellable atualizacaoAgendada;
	private WSRequest atualizacaoURL;
	private Pattern padraoDaDataDePublicacao;
	private JPAApi jpaAPI;
	private boolean atualizacaoAtivada;

	@Inject
	public AtualizacaoController(AtualizacaoDAO daoAtualizacao, CidadaoDAO daoCidadao,
			@Named("atualizador-actor") ActorRef atualizador, ActorSystem system, Configuration configuration, WSClient client, JPAApi jpaAPI) {
		this.daoAtualizacao = daoAtualizacao;
		this.daoCidadao = daoCidadao;
		this.atualizador = atualizador;
		this.system = system;
		this.jpaAPI = jpaAPI;
		this.atualizacaoAtivada = configuration.getBoolean("diferentonas.atualizacao.automatica", false);
		if(atualizacaoAtivada){
			this.atualizacaoAgendada = system.scheduler().schedule(Duration.create(10, TimeUnit.SECONDS), 
					Duration.create(1, TimeUnit.DAYS), () -> {
						this.atualiza();
					}, 
					system.dispatcher());
			this.atualizacaoURL = client.url(configuration.getString("diferentonas.url", "http://portal.convenios.gov.br/download-de-dados"));
			this.padraoDaDataDePublicacao = Pattern.compile("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d");
		}
		
	}

	@Transactional
	public Result getStatus() {
		
		Cidadao cidadao = daoCidadao
				.find(UUID.fromString(request().username()));
		if (!cidadao.isFuncionario()) {
			return unauthorized("Cidadão não autorizado");
		}
		
		return ok(toJson(daoAtualizacao.find()));
	}

	public void atualiza() {
		Logger.info("Hora deste servidor tentar atualizar os dados do SICONV e SIAFI");
		if(atualizacaoAtivada){
			if(jpaAPI.withTransaction(()->daoAtualizacao.find().estaAtualizando())){
				this.atualizacaoAgendada.cancel();
				long quando = new Random().nextInt(12);
				Logger.info("Há um servidor atualizando agora. Vou escolher um novo horário para verificações periódicas. Daqui a " + quando + " horas.");
				this.atualizacaoAgendada = system.scheduler().schedule(Duration.create(quando, TimeUnit.HOURS),
						Duration.create(1, TimeUnit.DAYS), () -> {
							this.atualiza();
						}, 
						system.dispatcher());
				return;
			}
		}
		
		Logger.info("Tentando atualizar usando dados de: " + atualizacaoURL.getUrl());

		atualizacaoURL.get().thenApply(response -> {
			Logger.info("Conexão realizada.");
			String body = response.getBody();
			Matcher matcher = padraoDaDataDePublicacao.matcher(body);
			if(matcher.find()){
				Atualizacao status = jpaAPI.withTransaction((em) -> {
					Atualizacao dao = daoAtualizacao.find();
					String data = matcher.group(0);
					Logger.info("Iniciando atualização de dados às: " + new Date() + " com dados publicados em: " + data);
					dao.atualiza(Arrays.asList(data));
					em.persist(dao);
					em.flush();
					em.refresh(dao);
					return dao;
				});

				if (status.estaDesatualizado()) {
					ask(atualizador, new AtualizadorActorProtocol.AtualizaIniciativasEScores(), 1000L);
				}
			}else{
				Logger.info("Problemas ao acessar página em: " + atualizacaoURL.getUrl());
			}
			return ok();
		});

	}
}
