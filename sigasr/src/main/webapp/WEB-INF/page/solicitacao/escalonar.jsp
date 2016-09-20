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
			carregarLotacaoDaAcao();
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
		
		function carregarLotacaoDaAcao(){
			//preenche o campo atendente com a lotacao designada a cada alteracao da acao 
			var opcaoSelecionada = $("#escalonar #selectAcao option:selected");
			var idAcao = opcaoSelecionada.val();
			try{
				var siglaLotacao = opcaoSelecionada.html().split(/[)|(]+/)[1]; //[ "(", "SEDGET", ")" ]
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
				<input type="hidden" name="solicitacao.codigo" id="sigla" value="${siglaCompacta}" />
				<input type="hidden" name="solicitante" value="${solicitante}">
				<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="gravar()"/>
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