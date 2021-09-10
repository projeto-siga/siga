<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="card-sidebar card bg-light mb-3">
	<div class="card-header">
		Solicita&ccedil;&atilde;o
	</div>
	
	<div class="card-body">
		<p>
			<b>Solicitante:</b>
			${solicitacao.solicitante.descricaoIniciaisMaiusculas},
			${solicitacao.solicitante.siglaCompleta},
			${solicitacao.solicitante.funcaoStringIniciaisMaiusculas},
			${solicitacao.solicitante.lotacao.siglaCompleta},
                  ${solicitacao.local.nomeComplexo}
		</p>
		<c:if test="${solicitacao.interlocutor != null}">
			<p>
				<b>Interlocutor:</b>
				${solicitacao.interlocutor.descricaoIniciaisMaiusculas}
			</p>
		</c:if>
		<c:if test="${solicitacao.dtOrigem != null}">
			<p>
				<b>Contato Inicial:</b> ${solicitacao.dtOrigemString}
				<c:if test="${solicitacao.meioComunicacao != null}">
                      por ${solicitacao.meioComunicacao.descrMeioComunicacao}
                  </c:if>
			</p>
		</c:if>
		<c:if test="${solicitacao.telPrincipal != null}">
			<p>
				<b>Telefone:</b> ${solicitacao.telPrincipal}
			</p>
		</c:if>
		<c:if test="${solicitacao.endereco != null}">
			<p>
				<b>Endereço:</b> ${solicitacao.endereco}
			</p>
		</c:if>
		<p>
			<b>Cadastrante:</b>
			${solicitacao.cadastrante.descricaoIniciaisMaiusculas},
			${solicitacao.lotaTitular.siglaLotacao}
		</p>
		<p><b>Cadastrado em:</b> ${solicitacao.dtRegDDMMYYYYHHMM}</p>
		<p>
			<b>Item de Configura&ccedil;&atilde;o:</b> ${solicitacao.descrItemAtualCompleta}
		</p>
		<p>
			<b>A&ccedil;&atilde;o:</b> ${solicitacao.descrAcaoAtualCompleta}
		</p>
		<p>
			<b>Prioridade:</b> <span style="">${solicitacao.prioridade.descPrioridade}</span>
		</p>
		<c:if test="${solicitacao.prioridadeTecnica != solicitacao.prioridade}">
			<p>
				<b>Prioridade T&eacute;cnica:</b> <span style="">${solicitacao.prioridadeTecnica.descPrioridade}</span>
			</p>
		</c:if>
		<p>
			<b>Notifica&ccedil;&atilde;o:</b>
			${solicitacao.formaAcompanhamento.descrFormaAcompanhamento}
		</p>
		<c:if
			test="${solicitacao.isFechadoAutomaticamente() != null && solicitacao.isPai()}">
			<p>
				<b>Fechamento Autom&aacute;tico:</b>
				${solicitacao.isFechadoAutomaticamente() ? "Sim" : "Não"}
			</p>
		</c:if>
	</div>
</div>