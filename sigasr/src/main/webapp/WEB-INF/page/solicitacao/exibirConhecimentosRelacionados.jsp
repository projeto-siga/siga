<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="gc">
</div>
<c:if
	test="${solicitacao.itemAtual != null && podeUtilizarServicoSigaGC}">
	<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse(
				"/../sigagc/app/knowledgeSidebarSr?${solicitacao.gcTags}&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",
				document.getElementById('gc'));
	</script>
</c:if>



