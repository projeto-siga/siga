<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ attribute name="pessoa"%>
<%@ attribute name="funcao"%>

<c:if test="${not empty pessoa}">

<c:set var="pes" value="${f:pessoa(pessoa)}"/>

<b>${pes.descricao}</b>, 
matrícula N&ordm; <b>${pes.sigla}</b>, 
<b>${pes.cargo.nomeCargo}</b>, 
classe <b>${pes.padraoReferencia}</b>

<c:if test="${not empty funcao and not empty pes.funcaoConfianca}">
ocupante do(a) cargo em comissão/função comissionada de <b>${pes.funcaoConfianca.descricao}</b>,
</c:if>

</c:if>