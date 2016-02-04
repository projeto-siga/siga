<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="gt-sidebar">
	<div class="gt-sidebar-content">
		<c:if test="${itemConfiguracao.idItemConfiguracao != null}">
			<p><b>Item: </b> ${itemConfiguracao.siglaItemConfiguracao} &nbsp; ${itemConfiguracao.tituloItemConfiguracao} &nbsp; ${itemConfiguracao.gcTags}</p>
		</c:if>
		<c:if test="${acao.idAcao != null}">
			<p><b>A&ccedil;&atilde;o: </b> ${acao.siglaAcao} &nbsp; ${acao.tituloAcao} &nbsp; ${acao.gcTags}</p>
		</c:if>
	</div>
</div>	

<div class="gt-sidebar">
	<div class="gt-sidebar-content" id="gc">
	</div>
</div>	


<script id="gc">
	<c:choose>		
		<c:when test="${itemConfiguracao.idItemConfiguracao != null}">
			SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeInplace?testarAcesso=true&popup=true&msgvazio=&titulo=${itemConfiguracao.tituloItemConfiguracao}&ts=${currentTimeMillis}${itemConfiguracao.gcTagAbertura}",document.getElementById('gc-ancora-item'));
		</c:when>
		<c:otherwise>
			$('#gc-ancora-item').html('');
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${itemConfiguracao.idItemConfiguracao != null and acao.idAcao != null}">
			SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeInplace?testarAcesso=true&popup=true&msgvazio=&titulo=${acao.tituloAcao}%20-%20${itemConfiguracao.tituloItemConfiguracao}&ts=${currentTimeMillis}${itemConfiguracao.gcTagAbertura}",document.getElementById('gc-ancora-item-acao'));
		</c:when>
		<c:otherwise>
			$('#gc-ancora-item-acao').html('');
		</c:otherwise>
	</c:choose>

	SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeSidebar?tags=@servico${acao.idAcao != null ? acao.gcTags : ''}${itemConfiguracao.idItemConfiguracao != null ? itemConfiguracao.gcTags : ''}&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",document.getElementById('gc'));
</script>
