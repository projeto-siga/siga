<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="acao" type="br.gov.jfrj.siga.sr.model.SrAcao" required="false"%>

<c:choose>
	<c:when test="${acao != null}">
		<span>${acao.tituloAcao}</span>
	</c:when>
	<c:otherwise>
		<span>Ação não informada</span>
	</c:otherwise>
</c:choose>