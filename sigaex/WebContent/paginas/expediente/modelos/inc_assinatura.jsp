<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<p style="font-family: Arial; font-size: 11pt; font-weight: bold;"
	align="center">
	<c:choose>
		<c:when test="${not empty doc.nmSubscritor}">
			${doc.nmSubscritor}
		</c:when>
		<c:otherwise>${doc.subscritor.descricao}</c:otherwise>
	</c:choose>
<c:if test="${empty apenasNome}">	
	<br />
<c:choose>
	<c:when test="${not empty param.apenasCargo}">
		${doc.subscritor.cargo.nomeCargo}
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${not empty doc.nmFuncao}">
				${doc.nmFuncao}
			</c:when>
			<c:when test="${not empty doc.titular.funcaoConfianca.nomeFuncao}">
				${doc.titular.funcaoConfianca.nomeFuncao}
				<c:if
					test="${doc.titular.idPessoa != doc.subscritor.idPessoa}">
					EM EXERCÍCIO
				</c:if>
			</c:when>
			<c:when test="${not empty doc.subscritor.funcaoConfianca.nomeFuncao}">
				${doc.subscritor.funcaoConfianca.nomeFuncao}
			</c:when>
			<c:otherwise>${doc.subscritor.cargo.nomeCargo}</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose> 
<c:if test="${not empty param.formatarOrgao}">
	<br>
	<c:choose>
		<c:when test="${not empty doc.nmLotacao}">
	${doc.nmLotacao}
	</c:when>
		<c:otherwise>${doc.titular.lotacao.nomeLotacao}</c:otherwise>
	</c:choose>

</c:if><c:forEach var="mov" items="${doc.mobilGeral.exMovimentacaoSet}">
	<c:if test="${mov.exTipoMovimentacao.idTpMov == 24}">
		<br>
		<br>
		<br>
		<c:choose>
			<c:when test="${not empty mov.nmSubscritor}">
				${mov.nmSubscritor}
			</c:when>
			<c:otherwise>${mov.subscritor.nomePessoa}</c:otherwise>
		</c:choose>		
		<br>
		<c:choose>
			<c:when test="${not empty mov.nmFuncao}">
			${mov.nmFuncao}
			</c:when>
			<c:when test="${not empty mov.titular.funcaoConfianca.nomeFuncao}">
				${mov.titular.funcaoConfianca.nomeFuncao} 
				<c:if
					test="${substituicao==true && doc.titular.idPessoa!=doc.subscritor.idPessoa}">
					EM EXERCÍCIO
				</c:if>
			</c:when>
			<c:when test="${not empty mov.subscritor.funcaoConfianca.nomeFuncao}">
				${mov.subscritor.funcaoConfianca.nomeFuncao}
			</c:when>
			<c:otherwise>${mov.subscritor.cargo.nomeCargo}</c:otherwise>
		</c:choose>
		<c:if test="${not empty param.formatarOrgao}">
			<br>
			<c:choose>
				<c:when test="${not empty mov.nmLotacao}">
				${mov.nmLotacao}
				</c:when>
				<c:otherwise>${mov.titular.lotacao.nomeLotacao}</c:otherwise>
			</c:choose>
		</c:if>
	</c:if>
</c:forEach></c:if>
<c:if test="${not empty param.textoFinal}">
	<br/>${param.textoFinal}
</c:if></p>
