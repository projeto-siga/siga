<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<script src="/sigaex/public/javascript/assinatura-digital.js"></script>
<input type="hidden" id="siglaUsuarioCadastrante" value="${cadastrante.sigla}"/>
<input type="hidden" id="siglaUsuSubscritor" value="${doc.subscritor.sigla}"/>
<input type="hidden" id="nomeUsuSubscritor" value="${doc.subscritor.nomePessoa}"/>
<c:if
	test="${not empty f:resource('siga.ex.assinador.externo.popup.url')}">
	<script src="/siga/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="${f:resource('siga.ex.assinador.externo.popup.url')}/popup-api.js"></script>
</c:if>