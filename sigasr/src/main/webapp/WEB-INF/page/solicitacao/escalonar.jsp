<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${not empty solicitacao.solicitante}">
	<script>
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
		
		function carregarAcao() {
			var inputIdSolicitacao = document.getElementById('id');
			var inputIdItem = document.getElementById('formulario_itemConfiguracao');
			var params = 'id=' + inputIdSolicitacao.value
					+ '&itemConfiguracao=' + inputIdItem.value;
			jQuery.blockUI(objBlock);
			PassAjaxResponseToFunction(
					'${linkTo[SolicitacaoController].exibirAcaoEscalonar}?'
							+ params, 'carregouAcao', null, false, null);
		}

		function carregouAcao(response, param) {
			var div = document.getElementById('divAcaoEscalonar');
			div.innerHTML = response;
			var scripts = div.getElementsByTagName("script");
			for (var i = 0; i < scripts.length; i++)
				eval(scripts[i].text);
			jQuery.unblockUI();
		}
	</script>
	<div class="gt-content-box gt-form">
		<form id="formEscalonar" action="${linkTo[SolicitacaoController].escalonarGravar}" onsubmit="javascript: return block();" method="POST">
			<div class="gt-form-row">
				<label><c:set var="valueCheckbox" value="${titular.orgaoUsuario.idOrgaoUsu == 1 ? false : true}" />
					<siga:checkbox name="criaFilha" onchange="onchangeCheckCriaFilha()" value="valueCheckbox" /> 
					<c:choose>
						<c:when test="${solicitacao.isFilha()}">
							<c:set var="codigo" value="${solicitacao.solicitacaoPai.codigo}" />
						</c:when>
						<c:otherwise>
							<c:set var="codigo" value="${solicitacao.codigo}" />
						</c:otherwise>
					</c:choose>
					Criar Solicita&ccedil;&atilde;o filha de ${codigo}
				</label>
				<br/>
				<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label>
				<siga:selecao2 propriedade="itemConfiguracao" tipo="itemConfiguracao" tema="simple" modulo="sigasr"
					onchange="carregarAcao()"
					paramList="sol.solicitante=${solicitacao.solicitante.idPessoa};sol.local=${solicitacao.local.idComplexo};sol.titular=${cadastrante.idPessoa};sol.lotaTitular=${lotaTitular.idLotacao}" />
				<span style="color: red" />
			</div>
			<div id="divAcaoEscalonar">
				<jsp:include page="exibirAcaoEscalonar.jsp" />
			</div>
			<a href="javascript: modalAbrir('lotacaoAtendente')" class="gt-btn-medium" style="margin: 5px 0 0 -3px;">
				Alterar atendente
			</a>
			<input type="hidden" name="idAtendenteNaoDesignado" id="atendenteNaoDesignado" value="" />
			<br/>
			<div id="outrasInformacoesDaFilha" class="gt-form-row">
				<label>Descri&ccedil;&atilde;o</label>
				<textarea name="descricao" cols="85" rows="7">${solicitacao.descrSolicitacao}</textarea>
				<br/><br/>
				<c:if test="${!solicitacao.isPai() && !solicitacao.isFilha()}">
					<siga:checkbox name="fechadoAuto" onchange="onchangeCheckCriaFilha()" value="${solicitacao.isFechadoAutomaticamente()}" />
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
				<input type="hidden" name="id" id="id" value="${solicitacao.idSolicitacao}">
				<input type="hidden" name="solicitante" value="${solicitacao.solicitante}">
				<input type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
				<a href="${linkTo[SolicitacaoController].exibir[solicitacao.idSolicitacao]}" class="gt-btn-medium gt-btn-left">Voltar</a>
			</div>
		</form>
	</div>

	<siga:modal nome="lotacaoAtendente" titulo="Alterar Atendente padrão">
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
	</siga:modal>

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
			$("#formulario_lotacao").val('');
			$("#formulario_lotacao_descricao").val('');
			$("#formulario_lotacao_sigla").val('');
			$("#lotacaoSpan").html('');
		}
		
		function alterarAtendente() {
			var inputNovoAtendente = $("#formulario_lotacao").val();
			var spanNovoAtendente = $("#formulario_lotacao_sigla").val() + " - "
					+ $("#formulario_lotacao_descricao").val();
			modalFechar('lotacaoAtendente');
			$("#atendenteNaoDesignado").val(inputNovoAtendente);
			$("#atendentePadrao").html(spanNovoAtendente);
			$("#idAtendente").val(inputNovoAtendente);
		}
	</script>
</c:if>