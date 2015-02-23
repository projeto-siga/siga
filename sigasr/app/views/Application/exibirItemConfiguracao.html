#{if solicitacao.solicitante}
<script>

$(document).ready(function() {
	notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao');
});

function carregarAcao(){
	jQuery.blockUI(objBlock);
	frm = document.getElementById('formSolicitacao');
	params = '';
	for (i = 0; i < frm.length; i++){
		if (frm[i].name && frm[i].value)
			params = params + frm[i].name + '=' + escape(frm[i].value) + '&';
	}
	PassAjaxResponseToFunction('@{Application.exibirAcao()}?' + params, 'carregouAcao', null, false, null);
}

function carregouAcao(response, param){
	var div = document.getElementById('divAcao');
	div.innerHTML = response;
	var scripts = div.getElementsByTagName("script");
	for(var i=0;i<scripts.length;i++)  
	   eval(scripts[i].text);
	jQuery.unblockUI();
}
</script>

<div class="gt-form-row gt-width-66" >
	<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label> #{selecao tipo:'item',
	nome:'solicitacao.itemConfiguracao',
	value:solicitacao.itemConfiguracao, grande:true, onchange:"carregarAcao();notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao')",
	params:'sol.solicitante='+solicitacao.solicitante?.idPessoa+'&sol.local='+solicitacao.local?.idComplexo+
				'&sol.titular='+titular?.idPessoa+'&sol.lotaTitular='+lotaTitular?.idLotacao /} <span style="color: red">#{error
		'solicitacao.itemConfiguracao' /}</span>
</div>
<div id="divAcao">#{include 'Application/exibirAcao.html' /}</div>
#{/if}
