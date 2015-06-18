<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/ato_presidencia.jsp">
	<mod:entrevista>
 		<mod:grupo titulo="Juiz Federal Convocado:">
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="substituto" />
		    </mod:grupo>
		    <mod:grupo>
				<mod:selecao titulo="Com prejuízo" var="preju"  opcoes="sim;nao" reler="nao" />
			</mod:grupo>
		</mod:grupo>
		<br><br>
		<mod:grupo titulo="Desembargador Federal Substituído:">
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="titular" />
	    	</mod:grupo>
	    	<mod:grupo>
				<mod:selecao titulo="Motivo" var="motiv" opcoes="licença médica;férias regulamentares" reler="nao"/>
			</mod:grupo>   
		</mod:grupo>
		<br><br>
		<mod:grupo titulo="Período de Convocação:">
			<mod:data titulo="De" var="dtini" />
			<mod:data titulo="a" var="dtfin" />
		</mod:grupo>
		<br><br>
	</mod:entrevista>
	
	<mod:documento>
	<c:set var="pessoa_titular" value ="${f:pessoa(requestScope['titular_pessoaSel.id'])}" />
	<c:set var="pessoa_subst" value ="${f:pessoa(requestScope['substituto_pessoaSel.id'])}" />
		
		    <mod:valor var="texto_ato">
			<br/><br/>
			<p style="TEXT-INDENT: 2cm" align="justify">
			<c:choose>
			<c:when test="${doc.subscritor.sexo == 'M'}">
						O PRESIDENTE
					</c:when>
					<c:otherwise>
					    A PRESIDENTE
					</c:otherwise></c:choose> DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO, no uso de suas atribuições, RESOLVE:<br><br>
			CONVOCAR 
		<c:choose>
			<c:when test="${pessoa_subst.sexo == 'M'}">
						o Excelentíssimo Juiz Federal
					</c:when>
					<c:otherwise>
					    a Excelentíssima Juíza Federal
					</c:otherwise></c:choose>
		da
    	${pessoa_subst.lotacao.descricao}, 
			<c:choose>
				<c:when test="${pessoa_subst.sexo == 'M'}">Dr.</c:when>
				<c:otherwise>Drª.</c:otherwise>
			</c:choose>	
    	${ pessoa_subst.nomePessoa}, para, 
			
			<c:choose>
				<c:when test="${preju == 'sim'}">com</c:when>
				<c:otherwise>sem</c:otherwise>
			</c:choose>
			
			prejuizo de sua jurisdição, compor o quorum deste Tribunal, no período de ${dtini} a ${dtfin}, 
			em virtude de ${motiv} 
			<c:choose>
				<c:when test="${pessoa_titular.sexo == 'M'}">do Excelentíssimo Desembargador Federal </c:when>
				<c:otherwise>da Excelentíssima Desembargadora Federal </c:otherwise>
			</c:choose>	
    		${ pessoa_titular.nomePessoa} , nos termos do artigo 48, inciso I, do Regimento Interno desta Corte c/c artigo 1º, inciso I, da Resolução nº 51/2009, do 
			Conselho da Justiça Federal. 
		</mod:valor>
		</mod:documento>
</mod:modelo>