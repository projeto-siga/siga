<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${sigla == 'M'}">
	<jsp:include page="../missao/ler.jsp"/>
</c:if>
<c:if test="${sigla == 'R'}">
	<jsp:include page="../requisicao/ler.jsp"/>
</c:if>
<c:if test="${sigla == 'S'}">
	<jsp:include page="../servicoVeiculo/ler.jsp"/>
</c:if>