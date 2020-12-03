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
			$("#escalonar_dialog").dialog();
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
	<div>
		<form action="#" method="post" enctype="multipart/form-data" id="frmEscalonar">
			<div class="">
				<label>
					<siga:checkbox name="criaFilha" onchange="onchangeCheckCriaFilha()" value="${criarFilhaDefault}" /> 
					Criar Solicita&ccedil;&atilde;o filha de ${codigo}
				</label>
				<br/>
				<sigasr:classificacao metodo="escalonar" exibeLotacaoNaAcao="true"/>				
				
				<input type="hidden" name="atendenteNaoDesignado.id" id="atendenteNaoDesignado" value="" />
				<br/>
				<div class="form-group">
					<label>Descri&ccedil;&atilde;o</label>
					<textarea name="descricao" class="form-control" rows="4"></textarea>
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
			<div id="motivoEscalonamento" class="form-group">
				<label>Motivo do Escalonamento</label>
				<siga:select name="motivo" value="NOVO_ATENDENTE" list="tipoMotivoEscalonamentoList" isEnum="true" 
					listValue="descrTipoMotivoEscalonamento" listKey="descrTipoMotivoEscalonamento"/>
			</div>
			<div class="gt-form-row">
				<input type="hidden" name="solicitacao.codigo" id="sigla" value="${siglaCompacta}" />
				<input type="hidden" name="solicitante" value="${solicitante}">
				<input type="button" value="Gravar" class="btn btn-primary" onclick="gravar()"/>				
				<a class="btn btn-primary" role="button" href="${linkTo[SolicitacaoController].exibir(siglaCompacta)}" style="color: #fff">Voltar</a>
			</div>
		</form>
	</div>

	
	<sigasr:modal nome="lotacaoAtendente" titulo="Alterar Atendente padrão" largura="70%">
		<div class="form-group">
			<label>Lota&ccedil;&atilde;o Atendente</label> 
			<input type="hidden" name="lotacaoSelecao" id="lotacaoSelecao" class="selecao">
			
			<sigasr:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" />
			
			<span style="display: none; color: red" id="atendente">Atendente n&atilde;o informado.</span>
		</div>
		<div class="gt-form-row">
			<a href="javascript: alterarAtendente()" class="btn btn-primary" style="color: #fff">Ok</a>
			<a href="javascript: modalFechar('lotacaoAtendente')" class="btn btn-primary" style="color: #fff">Cancelar</a>
		</div>
	</sigasr:modal>

	<script>
		
		function modalAbrir(componentId) {
			limparCampos();
			$("#" + componentId + "_dialog").dialog('open');			
		}

		function modalFechar(componentId) {
			$("#" + componentId + "_dialog").dialog('close');
		}

		var lotacaoId = get_lotacao_by('id');
		var lotacaoDescricao = get_lotacao_by('descricao');
		var lotacaoSigla = get_lotacao_by('sigla'); 
		
		// limpa campos do componente de busca - tag selecao
		function limparCampos() {
			lotacaoId.value = '';
			lotacaoDescricao.value = '';
			lotacaoSigla.value = '';
			$("#lotacao_lotacaoSelSpan").html('');
			$("#atendente").hide();
		}
		
		function alterarAtendente() {
			var inputNovoAtendente = lotacaoId.value;
			if(inputNovoAtendente === '') {
				$("#atendente").show();
				return;
			}
			
			var spanNovoAtendente = lotacaoSigla.value + " - "
					+ lotacaoDescricao.value;
			modalFechar('lotacaoAtendente');
			$("#atendenteNaoDesignado").val(inputNovoAtendente);
			$("#atendentePadrao").html(spanNovoAtendente);
			$("#idAtendente").val(inputNovoAtendente);
		}
	</script>
</c:if>