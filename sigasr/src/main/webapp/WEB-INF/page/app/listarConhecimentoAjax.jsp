<div class="gt-sidebar">
	<div class="gt-sidebar-content">
		<p><b>Item: </b> ${item?.siglaItemConfiguracao} &nbsp; ${item?.tituloItemConfiguracao} &nbsp; ${item?.gcTags}</p>
		<p><b>A&ccedil;&atilde;o: </b> ${acao?.siglaAcao} &nbsp; ${acao?.tituloAcao} &nbsp; ${acao?.gcTags}</p>
	</div>
</div>	

<div class="gt-sidebar">
	<div class="gt-sidebar-content" id="gc">
	</div>
</div>	


<script>
	#{if item}
		SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeInplace?tags=${item.gcTagAbertura}&testarAcesso=true&popup=true&msgvazio=&titulo=${item.tituloItemConfiguracao}&ts=${currentTimeMillis}",document.getElementById('gc-ancora-item'));
	#{/if}
	#{else}
		$('#gc-ancora-item').html('');
	#{/else}

	#{if item && acao}
		SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeInplace?tags=${solicitacao.gcTagAbertura}&testarAcesso=true&popup=true&msgvazio=&titulo=${acao.tituloAcao}%20-%20${item.tituloItemConfiguracao}&ts=${currentTimeMillis}",document.getElementById('gc-ancora-item-acao'));
	#{/if}
	#{else}
		$('#gc-ancora-item-acao').html('');
	#{/else}

	SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeSidebar?tags=@servico${acao ? acao?.gcTags.raw() : ''}${item ? item?.gcTags.raw() : ''}&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",document.getElementById('gc'));
</script>

