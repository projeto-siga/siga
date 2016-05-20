<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<c:if test="${not empty solicitante}">
	<script>
		function postbackURL(){
			return '${linkTo[SolicitacaoController].escalonar}?sigla='+$("#sigla").val()
				+'&itemConfiguracao.id='+$("#formulario_itemConfiguracao_id").val();
		}
		
		$(document).ready(function() {
			$('#outrasInformacoesDaFilha').hide();
			$("#escalonar_dialog").dialog('option', 'width', 700);
			onchangeCheckCriaFilha();
		});
		
		function onchangeCheckCriaFilha() {
			var checkbox = $('#checkcriaFilha');
			if (checkbox[0].checked) {
				checkbox.prop('value', 'true');
				$('#outrasInformacoesDaFilha').show();
				$('#motivoEscalonamento').hide();
				return;
			}
			checkbox.prop('value', 'false');
			$('#outrasInformacoesDaFilha').hide();
			$('#motivoEscalonamento').show();
		}

		$(document).ready(function() {
			removeSelectedDuplicado();
		});
		
		function carregarLotacaoDaAcao(){
			//preenche o campo atendente com a lotacao designada a cada alteracao da acao 
			var opcaoSelecionada = $("#selectAcao option:selected");
			var idAcao = opcaoSelecionada.val();
			try{
				var siglaLotacao = opcaoSelecionada.html().split("(")[1].slice(0,-2);
		
				var spanLotacao = $(".lotacao-" + idAcao + ":contains(" + siglaLotacao + ")");
				var descLotacao = spanLotacao.html();
				var idLotacao = spanLotacao.next().html();
				var idDesignacaoDaAcao = spanLotacao.prev().html();
		
				$("#idDesignacao").val(idDesignacaoDaAcao);
				$("#atendentePadrao").html(descLotacao);
				$("#idAtendente").val(idLotacao);
				//garante que quando alterar a acao o atendenteNaoDesignado fique vazio
				$("#atendenteNaoDesignado").val('');
			} catch(err){
				$("#idDesignacao").val('');
				$("#atendentePadrao").html('');
				$("#idAtendente").val('');
				//garante que quando alterar a acao o atendenteNaoDesignado fique vazio
				$("#atendenteNaoDesignado").val('');
			}
		}
		
		function removeSelectedDuplicado() {
			//solucao de contorno temporaria para opções no select com mesmo value.
			var primeiro = $("#selectAcao option:eq(0)");
			var segundo = $("#selectAcao option:eq(1)");
			if (primeiro.val() == segundo.val()) {
				segundo.prop("selected", false);
				primeiro.prop("selected", true);
			}
		}
		
	</script>
	<div class="gt-content-box gt-form">
		<form id="formEscalonar" action="${linkTo[SolicitacaoController].escalonarGravar}" onsubmit="javascript: return block();" method="POST">
			<div class="gt-form-row">
				<label>
					<siga:checkbox name="criaFilha" onchange="onchangeCheckCriaFilha()" value="${criarFilhaDefault}" /> 
					Criar Solicita&ccedil;&atilde;o filha de ${codigo}
				</label>
				<br/>
				<div class="gt-form-row">
					<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label>
					<siga:selecao2 tamanho="grande" propriedade="itemConfiguracao" tipo="itemConfiguracao" tema="simple" modulo="sigasr"
						onchange="$('#itemNaoInformado').hide();" reler="ajax"
						paramList="sol.id=${idSolicitacao};sol.solicitante.id=${solicitante.idPessoa};sol.local.id=${local.idComplexo};sol.titular.id=${cadastrante.idPessoa};sol.lotaTitular.id=${lotaTitular.idLotacao}" />
					<br/><span id="itemNaoInformado" style="color: red; display: none;">Item não informado</span>
					<br/>
					<div id="divAcao" depende="itemConfiguracao">
						<c:set var="acoesEAtendentes" value="${acoesEAtendentes}" />
						<c:if test="${not empty itemConfiguracao && not empty acoesEAtendentes}"> 
							<div class="gt-form-row">
								<label>A&ccedil;&atilde;o</label>	
								<select name="acao.id" id="selectAcao" onchange="carregarLotacaoDaAcao()">
									<c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
										<optgroup  label="${cat.tituloAcao}">
											<c:forEach items="${acoesEAtendentes.get(cat)}" var="tarefa">
												<option value="${tarefa.acao.idAcao}" ${acao.idAcao.equals(tarefa.acao.idAcao) ? 'selected' : ''}> ${tarefa.acao.tituloAcao} (${tarefa.conf.atendente.siglaCompleta})</option>
											</c:forEach>					 
										</optgroup>
									</c:forEach>
								</select>
								<br/><span id="acaoNaoInformada" style="color: red; display: none;">Ação não informada</span>
							</div>
							<div>
								<!-- Necessario listar novamente a lista "acoesEAtendentes" para ter a lotacao designada da cada acao
										ja que acima no select nao tem como "esconder" essa informacao -->
								<c:forEach items="${acoesEAtendentes.keySet()}" var="cat" varStatus="catPosition">
									<c:forEach items="${acoesEAtendentes.get(cat)}" var="t" varStatus="tPosition">
										<span class="idDesignacao-${t.acao.idAcao}" style="display:none;">${t.conf.idConfiguracao}</span>
										<span class="lotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.siglaCompleta} 
															- ${t.conf.atendente.descricao}</span>
										<span class="idLotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.idLotacao}</span>
									</c:forEach>
								</c:forEach>
						
								<label>Atendente</label>
								<span id="atendentePadrao" style="display:block;"></span>
								<input type="hidden" id="idDesignacao" name="designacao.id" value="" />
								<input type="hidden" name="atendente.id" id="idAtendente" value="" />
								<script>carregarLotacaoDaAcao();</script>
							</div>
						</c:if>
					</div>
				</div>
				
				<a href="javascript: modalAbrir('lotacaoAtendente')" class="gt-btn-medium" style="margin: 5px 0 0 -3px;">
					Alterar atendente
				</a>
				<input type="hidden" name="atendenteNaoDesignado.id" id="atendenteNaoDesignado" value="" />
				<br/>
				<div class="gt-form-row">
					<label>Descri&ccedil;&atilde;o</label>
					<textarea name="descricao" cols="85" rows="4"></textarea>
				</div>
			<div id="outrasInformacoesDaFilha" class="gt-form-row">
				<c:if test="${!isPai && empty solicitacaoPai}">
					<siga:checkbox name="fechadoAuto" onchange="onchangeCheckCriaFilha()" value="${isFechadoAutomaticamente}" />
					Fechar automaticamente a solicita&ccedil;&atilde;o <b>${codigo}</b>, quando
					todas as solicita&ccedil;&otilde;es filhas forem fechadas pelos seus
					respectivos atendentes. 
				</c:if>
			</div>
			<div id="motivoEscalonamento" class="gt-form-row">
				<label>Motivo do Escalonamento</label>
				<siga:select name="motivo" value="NOVO_ATENDENTE" list="tipoMotivoEscalonamentoList" isEnum="true" 
					listValue="descrTipoMotivoEscalonamento" listKey="descrTipoMotivoEscalonamento" style="width: auto;"/>
			</div>
			<div class="gt-form-row">
				<input type="hidden" name="sigla" id="sigla" value="${siglaCompacta}">
				<input type="hidden" name="solicitante" value="${solicitante}">
				<input type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
				<a href="${linkTo[SolicitacaoController].exibir[siglaCompacta]}" class="gt-btn-medium gt-btn-left">Voltar</a>
			</div>
		</form>
	</div>

	<sigasr:modal nome="lotacaoAtendente" titulo="Alterar Atendente padrão">
		<div id="dialogAtendente">
			<div class="gt-content">
				<div class="gt-form gt-content-box">
					<div class="gt-form-row">
						<div class="gt-form-row">
							<label>Lota&ccedil;&atilde;o Atendente</label> 
							<input type="hidden" name="lotacaoSelecao" id="lotacaoSelecao" class="selecao">
							<siga:selecao2 tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" />
							<span style="display: none; color: red" id="atendente">Atendente n&atilde;o informado.</span>
						</div>
						<div class="gt-form-row">
							<a href="javascript: alterarAtendente()" class="gt-btn-medium gt-btn-left">Ok</a>
							<a href="javascript: modalFechar('lotacaoAtendente')" class="gt-btn-medium gt-btn-left">Cancelar</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</sigasr:modal>

	<script>
		function modalAbrir(componentId) {
			limparCampos();
			$("#" + componentId + "_dialog").dialog('option', 'width', 580);
			$("#" + componentId + "_dialog").dialog('open');
		}

		function modalFechar(componentId) {
			$("#" + componentId + "_dialog").dialog('close');
		}
		
		// limpa campos do componente de busca - tag selecao
		function limparCampos() {
			$("#formulario_lotacao_id").val('');
			$("#formulario_lotacao_descricao").val('');
			$("#formulario_lotacao_sigla").val('');
			$("#lotacaoSpan").html('');
		}
		
		function alterarAtendente() {
			var inputNovoAtendente = $("#formulario_lotacao_id").val();
			var spanNovoAtendente = $("#formulario_lotacao_sigla").val() + " - "
					+ $("#formulario_lotacao_descricao").val();
			modalFechar('lotacaoAtendente');
			$("#atendenteNaoDesignado").val(inputNovoAtendente);
			$("#atendentePadrao").html(spanNovoAtendente);
			$("#idAtendente").val(inputNovoAtendente);
		}
	</script>
</c:if>