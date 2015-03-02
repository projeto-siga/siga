#{if solicitacao.solicitante}
<script>
$(document).ready(function() {
	$('#outrasInformacoesDaFilha').hide();
	$("#escalonar_dialog").dialog('option', 'width', 700);
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
	jQuery.blockUI(objBlock);
	PassAjaxResponseToFunction('@{Application.exibirAcaoEscalonar}?' + params , 'carregouAcao', null, false, null);
}

function carregouAcao(response, param){
	var div = document.getElementById('divAcaoEscalonar');
	div.innerHTML = response;
	var scripts = div.getElementsByTagName("script");
	for(var i=0;i<scripts.length;i++)  
	   eval(scripts[i].text);
	jQuery.unblockUI();
}
</script>
<div class="gt-content-box gt-form">
	<form id="formEscalonar" action="@{Application.escalonarGravar}" 
			onsubmit="javascript: return block();" method="POST" enctype="multipart/form-data">
		
		<div class="gt-form-row" >
			<label>	#{set valueCheckbox : titular?.orgaoUsuario?.idOrgaoUsu == 1 ? false : true /}
					#{checkbox name:'criaFilha', onchange:'onchangeCheckCriaFilha();', value: valueCheckbox /} 
					#{if solicitacao.isFilha()} #{set codigo : solicitacao.solicitacaoPai.codigo /} #{/if}
					#{else} #{set codigo : solicitacao.codigo /} #{/else}
					Criar Solicitação filha de ${codigo}</label>
			<br />
			<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label> #{selecao tipo:'item',
			nome:'itemConfiguracao',
			value:solicitacao.itemConfiguracao, grande:true, onchange:"carregarAcao();",
			params:'sol.solicitante='+solicitacao.solicitante?.idPessoa+'&sol.local='+solicitacao.local?.idComplexo+
						'&sol.titular='+cadastrante?.idPessoa+'&sol.lotaTitular='+lotaTitular?.idLotacao /} <span style="color: red">#{error
				'solicitacao.itemConfiguracao' /}</span>
		</div>
		<div id="divAcaoEscalonar">#{include 'Application/exibirAcaoEscalonar.html' /}</div>
		<a href="javascript: modalAbrir('lotacaoAtendente')" class="gt-btn-medium" style="margin:5px 0 0 -3px;" >Alterar atendente</a>
		<input type="hidden" name="idAtendenteNaoDesignado" id="atendenteNaoDesignado" value="" />
		<br />
		<div id="outrasInformacoesDaFilha" class="gt-form-row">
			<label>Descrição</label>
			<textarea name="descricao" cols="85" rows="7">${solicitacao.descrSolicitacao}</textarea>
			<br /><br />	
			#{if !solicitacao.isPai() && !solicitacao.isFilha()}
				#{checkbox name:'fechadoAuto',
					value:solicitacao.fechadoAutomaticamente /} Fechar automaticamente a solicitação <b>${codigo}</b>, quando 
					todas as solicitaç&otilde;es filhas forem fechadas pelos seus respectivos atendentes.
			#{/if}
		</div>
		<div  id="motivoEscalonamento" class="gt-form-row">
			<br />
			<label>Motivo do Escalonamento</label>
			#{select name:'motivo', items:models.SrTipoMotivoEscalonamento.values(),
						value:'NOVO_ATENDENTE',
						labelProperty:'descrTipoMotivoEscalonamento', style:'width:auto;' /}
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

#{modal nome:'lotacaoAtendente', titulo:'Alterar Atendente padrão'}
	<div id="dialogAtendente">
		<div class="gt-content">
			<div class="gt-form gt-content-box">
				<div class="gt-form-row">
					<div class="gt-form-row">
						<label>Lotação Atendente</label>
							#{selecao tipo:'lotacao',
								nome:'lotacaoSelecao',
								value:lotacaoSelecao /}			
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
#{/modal}

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
#{/if}
