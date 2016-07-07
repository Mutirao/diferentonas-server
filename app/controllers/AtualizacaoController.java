package controllers;


import static akka.pattern.Patterns.ask;
import static play.libs.Json.toJson;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Atualizacao;
import models.AtualizacaoDAO;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import actors.AtualizadorActorProtocol;
import akka.actor.ActorRef;

@Singleton
public class AtualizacaoController extends Controller {

	private ActorRef atualizador;
	private AtualizacaoDAO daoAtualizacao;

	@Inject
	public AtualizacaoController(AtualizacaoDAO daoAtualizacao,
			@Named("atualizador-actor") ActorRef atualizador) {
		this.daoAtualizacao = daoAtualizacao;
		this.atualizador = atualizador;
	}

	@Transactional
	public Result getAtualizacoes() {

		return ok(toJson(daoAtualizacao.verifica()));
	}

	@Transactional
	public Result aplica() {

		Atualizacao statusDaAtualizacao = daoAtualizacao.verifica();

		if (!statusDaAtualizacao.estaDesatualizado()) {
			return ok(toJson(statusDaAtualizacao));
		}

		Logger.debug(" ask ");
		FutureConverters.toJava(
				ask(atualizador, new AtualizadorActorProtocol.AtualizaScores(),
						1000L)).thenApply(response -> {
			Logger.debug(" * * * * * terminou");
			return true;
		});
		
		return getAtualizacoes();
	}
}
