<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<c:if test="${not empty solicitante}">
	<script>
		function postbackURL(){
			return '${linkTo[SolicitacaoController].escalonar}?solicitacao.codigo='+$("#sigla").val()
				+'&solicitacao.itemConfiguracao.id='+$("#formulario_solicitacaoitemConfiguracao_id").val();
		}

		function submitURL() {
			return '${linkTo[SolicitacaoController].escalonarGravar}';
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

		function onchangeCheckFechaAutomatico() {
			var checkbox = $('#checkfechadoAuto');
			var acao = $('#selectAcao').val();
			var labelAcao = $("#selectAcao").find(":selected").text();
			var labelAcaoGenerica = labelAcao.trim().substr(0,23);
			if (checkbox[0].checked) {
				if(labelAcaoGenerica == 'Atendimento de 1º nível'){
					$('#erroCheckFechadoAuto').show();
					checkbox[0].checked = false;
					return;
				}
			}
		}
				
	</script>
	<div class="gt-content-box gt-form">
		<form action="#" method="post" enctype="multipart/form-data" id="frmEscalonar">
			<div class="gt-form-row">
				<label>
					<siga:checkbox name="criaFilha" onchange="onchangeCheckCriaFilha()" value="${criarFilhaDefault}" /> 
					Criar Solicita&ccedil;&atilde;o filha de ${codigo}
				</label>
				<br/>
				<sigasr:classificacao metodo="escalonar" exibeLotacaoNaAcao="true"/>
				
				<input type="hidden" name="atendenteNaoDesignado.id" id="atendenteNaoDesignado" value="" />
				<br/>
				<div class="gt-form-row">
					<label>Descri&ccedil;&atilde;o</label>
					<textarea name="descricao" cols="85" rows="4"></textarea>
				</div>
			<div id="outrasInformacoesDaFilha" class="gt-form-row">
				<c:if test="${!isPai && empty solicitacaoPai}">
					<siga:checkbox name="fechadoAuto" onchange="onchangeCheckCriaFilha();onchangeCheckFechaAutomatico()" value="${isFechadoAutomaticamente}" />
					Fechar automaticamente a solicita&ccedil;&atilde;o <b>${codigo}</b>, quando
					todas as solicita&ccedil;&otilde;es filhas forem fechadas pelos seus
					respectivos atendentes. 
					<br/>
					<span style="display: none; color: red" id="erroCheckFechadoAuto">N&atilde;o é possível fechar automaticamente solicita&ccedil;&atilde;o com a&ccedil;&atilde;o Atendimento de 1º nível. Necessário reclassificar a aç&atilde;o.</span>
				</c:if>
			</div>
			<div id="motivoEscalonamento" class="gt-form-row">
				<label>Motivo do Escalonamento</label>
				<siga:select name="motivo" value="NOVO_ATENDENTE" list="tipoMotivoEscalonamentoList" isEnum="true" 
					listValue="descrTipoMotivoEscalonamento" listKey="descrTipoMotivoEscalonamento" style="width: auto;"/>
			</div>
			<div class="gt-form-row">
				<input type="hidden" name="solicitacao.codigo" id="sigla" value="${siglaCompacta}" />
				<input type="hidden" name="solicitante" value="${solicitante}">
				<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="gravar()"/>
				<a href="${linkTo[SolicitacaoController].exibir(siglaCompacta)}" class="gt-btn-medium gt-btn-left">Voltar</a>
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