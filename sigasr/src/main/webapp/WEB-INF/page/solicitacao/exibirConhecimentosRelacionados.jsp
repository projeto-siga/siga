<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<div class="gt-sidebar">
	<div class="gt-sidebar-content" id="gc"></div>
</div>
<c:if
	test="${solicitacao.itemConfiguracao != null && podeUtilizarServico}">
	<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse(
				"/../sigagc/app/knowledge?${solicitacao.gcTags.raw()}&estilo=sidebar&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",
				document.getElementById('gc'));
	</script>
</c:if>



