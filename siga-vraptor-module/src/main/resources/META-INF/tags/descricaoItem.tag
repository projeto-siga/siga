<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="itemConfiguracao" type="br.gov.jfrj.siga.sr.model.SrItemConfiguracao" required="false"%>

<c:choose>
	<c:when test="${itemConfiguracao != null}">
		${itemConfiguracao.tituloItemConfiguracao}	
	</c:when>
	
	<c:otherwise>
		Item não informado
	</c:otherwise>
</c:choose>