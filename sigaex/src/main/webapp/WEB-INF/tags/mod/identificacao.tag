<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ attribute name="pessoa"%>
<%@ attribute name="funcao"%>
<%@ attribute name="nivelHierarquicoMaximoDaLotacao"%>
<%@ attribute name="obs"%>
<%@ attribute name="negrito"%>
<c:if test="${not empty pessoa}">

<c:set var="pes" value="${f:pessoa(pessoa)}"/>  
<c:choose>
	<c:when test="${negrito eq 'não'}">	
		${pes.descricao},	
		matrícula n&ordm; ${pes.sigla}, 
		${pes.cargo.nomeCargo}, 
		classe ${pes.padraoReferencia}, 
		<c:if test="${not empty obs}">
		${obs}, 
		</c:if>
		lotado(a) no(a) 
		${empty nivelHierarquicoMaximoDaLotacao ? pes.lotacao.descricao : f:lotacaoPorNivelMaximo(pes.lotacao, nivelHierarquicoMaximoDaLotacao).descricao}, 

		<c:if test="${not empty funcao and not empty pes.funcaoConfianca}">
		ocupante do(a) cargo em comissão/função comissionada de ${pes.funcaoConfianca.descricao},
		</c:if>		
	</c:when>
	<c:when test="${negrito eq 'sim'}">
		<b>${pes.descricao}</b>,		
		matrícula n&ordm; ${pes.sigla}, 
		${pes.cargo.nomeCargo}, 
		classe ${pes.padraoReferencia}, 
		<c:if test="${not empty obs}">
		${obs}, 
		</c:if>
		lotado(a) no(a) 
		${empty nivelHierarquicoMaximoDaLotacao ? pes.lotacao.descricao : f:lotacaoPorNivelMaximo(pes.lotacao, nivelHierarquicoMaximoDaLotacao).descricao}, 

		<c:if test="${not empty funcao and not empty pes.funcaoConfianca}">
		ocupante do(a) cargo em comissão/função comissionada de ${pes.funcaoConfianca.descricao},
		</c:if>
	</c:when>
	<c:otherwise>
		<b>${pes.descricao}</b>,		
		matrícula n&ordm; <b>${pes.sigla}</b>, 
		<b>${pes.cargo.nomeCargo}</b>, 
		classe <b>${pes.padraoReferencia}</b>, 
		<c:if test="${not empty obs}">
		${obs}, 
		</c:if>
		lotado(a) no(a) 
		<b>${empty nivelHierarquicoMaximoDaLotacao ? pes.lotacao.descricao : f:lotacaoPorNivelMaximo(pes.lotacao, nivelHierarquicoMaximoDaLotacao).descricao}</b>, 

		<c:if test="${not empty funcao and not empty pes.funcaoConfianca}">
		ocupante do(a) cargo em comissão/função comissionada de <b>${pes.funcaoConfianca.descricao}</b>,
		</c:if>
	</c:otherwise>
</c:choose>
</c:if>
