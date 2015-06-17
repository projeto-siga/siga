<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- INICIO ASSINATURA -->
<c:forEach var="movim" items="${doc.mobilGeral.exMovimentacaoSet}">
	<c:if
		test="${movim.exTipoMovimentacao.idTpMov == 24 && mov.titular.idPessoa == movim.titular.idPessoa && not empty movim.descrMov}">
		<c:set var="funcSubscrDoc" value="${movim.descrMov}" />
	</c:if>
</c:forEach>
<p align="center"
	style="font-family:Arial;font-size:11pt;font-weight:bold;">${mov.subscritor.descricao}<br />
<c:choose>
	<c:when test="${not empty mov.nmFuncao}">
		${mov.nmFuncao}
	</c:when>
	<c:when
		test="${(not empty mov.titular) && mov.titular.idPessoa == doc.titular.idPessoa && not empty doc.nmFuncao}">
		${doc.nmFuncao}
	</c:when>
	<c:when
		test="${(not empty mov.subscritor) && mov.subscritor.idPessoa == doc.subscritor.idPessoa && not empty doc.nmFuncao}">
		${doc.nmFuncao}
	</c:when>
	<c:when test="${not empty funcSubscrDoc}">
		${funcSubscrDoc}
	</c:when>
	<c:when test="${not empty mov.titular.funcaoConfianca.nomeFuncao}">
		${mov.titular.funcaoConfianca.nomeFuncao}<c:if
			test="${mov.titular.idPessoa != mov.subscritor.idPessoa}">
	EM EXERCÍCIO
</c:if>
	</c:when>
	<c:when test="${not empty mov.subscritor.funcaoConfianca.nomeFuncao}">
		${mov.subscritor.funcaoConfianca.nomeFuncao}
	</c:when>
	<c:otherwise>${mov.subscritor.cargo.nomeCargo}</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${not empty mov.nmLotacao}">
	<br>
	${mov.nmLotacao}
	</c:when>
	<c:otherwise><br>${mov.titular.lotacao.nomeLotacao}</c:otherwise>
</c:choose>
<!-- FIM ASSINATURA -->