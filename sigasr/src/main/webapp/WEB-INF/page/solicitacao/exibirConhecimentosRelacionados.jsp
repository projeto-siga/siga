<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<div class="gt-sidebar-content" id="gc"></div>
<c:if
	test="${solicitacao.itemAtual != null && podeUtilizarServicoSigaGC}">
	<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse(
				"/../sigagc/app/knowledgeSidebar?${solicitacao.gcTags}&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",
				document.getElementById('gc'));
	</script>
</c:if>



