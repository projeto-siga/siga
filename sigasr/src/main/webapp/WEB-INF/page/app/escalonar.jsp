<c:if test="${solicitacao.solicitante}">
<script>
$(document).ready(function() {
	$('#outrasInformacoesDaFilha').hide();
	$("#escalonar_dialog").dialog('option', 'width', 580);
	onchangeCheckCriaFilha();
});
function onchangeCheckCriaFilha(){
	var checkbox = $('#checkcriaFilha');
	if(checkbox[0].checked) {
		checkbox.prop('value', 'true');
		$('#outrasInformacoesDaFilha').show();
		$('#motivoEscalonamento').hide();
		return;
	}
	checkbox.prop('value', 'false');
	$('#outrasInformacoesDaFilha').hide();
	$('#motivoEscalonamento').show();
}
function carregarAcao(){
	var inputIdSolicitacao = document.getElementById('id');
	var inputIdItem = document.getElementById('itemConfiguracao');
	var params = 'id='+ inputIdSolicitacao.value + '&itemConfiguracao='+inputIdItem.value;
	PassAjaxResponseToFunction('@{Application.exibirAcaoEscalonar}?' + params , 'carregouAcao', null, false, null);
}

function carregouAcao(response, param){
	var div = document.getElementById('divAcaoEscalonar');
	div.innerHTML = response;
	var scripts = div.getElementsByTagName("script");
	for(var i=0;i<scripts.length;i++)  
	   eval(scripts[i].text);
}
</script>
<div class="gt-content-box gt-form">
	<form id="formEscalonar" action="@{Application.escalonarGravar}" 
			onsubmit="javascript: return block();" method="POST" enctype="multipart/form-data">
		
		<div class="gt-form-row" >
			<label><siga:checkbox name="criaFilha"  onchange="onchangeCheckCriaFilha();" value="true" /> 
					<c:choose>
					<c:when test="${solicitacao.isFilha()}">
						<c:set var="codigo" value="${solicitacao.solicitacaoPai.codigo}" /> 
					</c:when>
					<c:otherwise> 
						<c:set var="codigo" value="${solicitacao.codigo}" />
					</c:otherwise>
					</c:choose>
					Criar Solicitação filha de ${codigo}</label>
			<br />
			<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label> 
			<sigasr:selecao tipo="item"
			nome="itemConfiguracao"
			value="${solicitacao.itemConfiguracao}" grande="true" onchange="carregarAcao();"
			params="sol.solicitante=${solicitacao.solicitante?.idPessoa}&sol.local=${solicitacao.local?.idComplexo}
						&sol.cadastrante.idPessoa=${cadastrante?.idPessoa}&sol.lotaCadastrante.idLotacao=${lotaTitular?.idLotacao}" /> <span style="color: red"><sigasr:error
				nome="solicitacao.itemConfiguracao" /></span>
		</div>
		<div id="divAcaoEscalonar"><jsp:include page="exibirAcaoEscalonar.jsp"/></div>
		<div id="outrasInformacoesDaFilha" class="gt-form-row">
			<a href="javascript: modalAbrir('lotacaoAtendente')" class="gt-btn-medium" style="margin:5px 0 0 -3px;" >Alterar atendente</a>
			<input type="hidden" name="idAtendenteNaoDesignado" id="atendenteNaoDesignado" value="" />
			<br />
			<label>Descrição</label>
			<textarea name="descricao" cols="70" rows="4">${solicitacao.descrSolicitacao}</textarea>
			<br /><br />	
			<c:if test="${!solicitacao.isPai() && !solicitacao.isFilha()}">
				<siga:checkbox name="fechadoAuto"
					value="${solicitacao.fechadoAutomaticamente}" /> Fechar automaticamente a solicitação <b>${codigo}</b>, quando 
					todas as solicitaç&otilde;es filhas forem fechadas pelos seus respectivos atendentes.
			</c:if>
		</div>
		<div  id="motivoEscalonamento" class="gt-form-row">
			<br />
			<label>Motivo do Escalonamento</label>
			<sigasr:select name="motivo" items="${SrTipoMotivoEscalonamento.values()}"
						value="NOVO_ATENDENTE"
						labelProperty="${descrTipoMotivoEscalonamento}" style="width:auto;" />
		</div>
		<div  class="gt-form-row">
			<br />
			<input type="hidden" name="id" id="id" value="${solicitacao.idSolicitacao}">
			<input type="hidden" name="solicitante" value="${solicitacao.solicitante}">
			<input type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
			<a href="@{Application.exibir(solicitacao.idSolicitacao)}" class="gt-btn-medium gt-btn-left">Voltar</a>
		</div>
	</form>
</div>

<sigasr:modal nome="lotacaoAtendente" titulo="Alterar Atendente padrão">
	<div id="dialogAtendente">
		<div class="gt-content">
			<div class="gt-form gt-content-box">
				<div class="gt-form-row">
					<div class="gt-form-row">
						<label>Lotação Atendente</label>
							<sigasr:selecao tipo="lotacao"
								nome="lotacaoSelecao"
								value="${lotacaoSelecao}" />			
						<span style="display:none;color: red" id="atendente">Atendente não informado.</span>
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
		$("#" + componentId + "_dialog").dialog('option', 'width', 560);
		$("#" + componentId + "_dialog").dialog('open');
	}
	
	function modalFechar(componentId) {
		$("#" + componentId + "_dialog").dialog('close');
	}
	// limpa campos do componente de busca - tag selecao
	function limparCampos() {
		$("#lotacaoSelecao").val('');
		$("#lotacaoSelecao_descricao").val('');
		$("#lotacaoSelecao_sigla").val('');
		$("#lotacaoSelecaoSpan").html('');
	}
	function alterarAtendente() {
		var inputNovoAtendente = $("#lotacaoSelecao").val();
		var spanNovoAtendente = $("#lotacaoSelecao_sigla").val() + " - " + $("#lotacaoSelecao_descricao").val();
		modalFechar('lotacaoAtendente');
		$("#atendenteNaoDesignado").val(inputNovoAtendente);
		$("#atendentePadrao").html(spanNovoAtendente);
	}
</script>
</c:if>
