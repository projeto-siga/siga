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
	<c:choose>
	<c:when test="${item}">
		SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledge?tags=${item.gcTagAbertura}&estilo=inplace&testarAcesso=true&popup=true&msgvazio=&titulo=${item.tituloItemConfiguracao}&ts=${currentTimeMillis}",document.getElementById('gc-ancora-item'));
	</c:when>
	<c:otherwise>
		$('#gc-ancora-item').html('');
	</c:otherwise>
	</c:choose>

	<c:choose>
	<c:when test="${item && acao}">
		SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledge?tags=^sr:${acao.tituloSlugify}-${item.tituloSlugify}&testarAcesso=true&estilo=inplace&popup=true&msgvazio=&titulo=${acao.tituloAcao}%20-%20${item.tituloItemConfiguracao}&ts=${currentTimeMillis}",document.getElementById('gc-ancora-item-acao'));
	</c:when>
	<c:otherwise>
		$('#gc-ancora-item-acao').html('');
	</c:otherwise>
	</c:choose>

	SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledge?tags=@servico${acao ? acao?.gcTags.raw() : ''}${item ? item?.gcTags.raw() : ''}&estilo=sidebar&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",document.getElementById('gc'));
</script>

