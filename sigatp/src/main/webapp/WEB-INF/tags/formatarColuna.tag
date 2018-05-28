<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ attribute name="href"%>
<%@ attribute name="titulo"%>
<%@ attribute name="operacao"%>
<%@ attribute name="onclick"%>
<%@ attribute name="classe"%>

<c:choose>
	<c:when test="${operacao == 'editar'}">
		<c:set var="imagem" value="/sigatp/public/images/editarTabelas.png" />
	</c:when>
	<c:when test="${operacao == 'cancelar'}">
		<c:set var="imagem" value="/sigatp/public/images/cancelarMissao.png" />
	</c:when>
	<c:when test="${operacao == 'imprimir'}">
		<c:set var="imagem" value="/sigatp/public/images/imprimir.png" />
	</c:when>
	<c:when test="${operacao == 'excluir'}">
		<c:set var="imagem" value="/sigatp/public/images/deletar.png"/>
	</c:when>
	<c:when test="${operacao == 'autorizar'}">
		<c:set var="imagem" value="/sigatp/public/images/approvedicon.png" />
	</c:when>
	<c:when test="${operacao == 'rejeitar'}">
		<c:set var="imagem" value="/sigatp/public/images/rejectedicon.png"/>
	</c:when>
</c:choose>

<c:set var="alt">
	<fmt:message key="views.botoes.${operacao}" />
</c:set>

<c:set var="tituloCompleto">
	<fmt:message key="views.label.${operacao}" /> ${titulo}
</c:set>

<a href="${href}" <c:if test="${not empty onclick}">onclick="${onclick}"</c:if><c:if test="${not empty classe}">class="${classe}"</c:if>> 
    <img src="${imagem}" alt="${alt}" title="${tituloCompleto}" <c:if test="${operacao == 'autorizar' || operacao == 'rejeitar'}">style="margin-right: 5px;"</c:if> width="19" height="22">
</a>