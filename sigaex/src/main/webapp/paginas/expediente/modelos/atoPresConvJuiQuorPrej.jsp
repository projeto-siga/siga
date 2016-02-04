<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/ato_presidencia.jsp">
	<mod:entrevista>
        <mod:grupo titulo=" ">	
		     <mod:grupo>
		        <mod:selecao var="presid"
			     titulo="VOCATIVO"
			     opcoes="A PRESIDENTE;O PRESIDENTE"	
			     reler="sim" />
		     </mod:grupo>
		</mod:grupo>
		
		<mod:grupo titulo="Juiz Federal Convocado">
			<mod:grupo>
				<mod:selecao titulo="Sexo" var="gen1"  opcoes="M;F" reler="não" />
				<mod:selecao titulo="Juiz Federal" reler="não"/>
				<mod:selecao titulo="Com prejuízo" var="prej2"  opcoes="sim;não" reler="não" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="convocado" />
			</mod:grupo>	
		</mod:grupo>
		
		<mod:grupo titulo="Desembargador Federal Substituído">
			<mod:grupo>
				<mod:selecao titulo="Sexo" var="gen2"  opcoes="M;F" reler="não" />
				<mod:selecao titulo="Desembargador Federal" reler="não"/>
				<mod:selecao titulo="em virtude" var="motiv" opcoes="de licença médica;das férias regulamentares" reler="não"/>
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="titular" />
			</mod:grupo>
		</mod:grupo>
		
		<mod:grupo titulo="Período de Convocação">
			<mod:data titulo="De" var="dtini" />
			<mod:data titulo="a" var="dtfin" />
		</mod:grupo>
		
	</mod:entrevista>
	
	<mod:documento>
		<c:set var="pessoa_titular" value ="${f:pessoa(requestScope['titular_pessoaSel.id'])}" />
		<c:set var="pessoa_convoc" value ="${f:pessoa(requestScope['convocado_pessoaSel.id'])}" />
		
		<mod:valor var="texto_ato">
			<br/><br/>
			<p style="TEXT-INDENT: 2cm" align="justify">
			${presid} DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO, no uso de suas atribuições, RESOLVE:
			
			CONVOCAR
			<c:choose>
					<c:when test="${ gen1 == 'M'}">
						o Excelentíssimo Juiz Federal
					</c:when>
					<c:otherwise>
					    a Excelentíssima Juíza Federal
					</c:otherwise>
			</c:choose>	
			da
			${pessoa_convoc.lotacao.descricao},
			<c:choose>
				<c:when test="${genero2 == 'F'}">Drª.</c:when>
				<c:otherwise>Dr.</c:otherwise>
			</c:choose>	
			${ pessoa_convoc.nomePessoa}, para, 
			
			<c:choose>
				<c:when test="${prej2 == 'sim'}">com</c:when>
				<c:otherwise>sem</c:otherwise>
			</c:choose>
			
			prejuizo de sua jurisdição, compor o quorum deste Tribunal, no período de ${dtini} a ${dtfin}, 
			em virtude ${motiv} 
			<c:choose>
				<c:when test="${gen2 == 'F'}">da Excelentíssima Desembargadora Federal </c:when>
				<c:otherwise>da Excelentíssimo Desembargador Federal </c:otherwise>
			</c:choose>	
			${ pessoa_titular.nomePessoa}, nos termos do artigo 48, inciso I, do Regimento Interno desta Corte c/c artigo 1º, inciso I, da Resolução nº 51/2009, do 
			Conselho da Justiça Federal. 
			
			
			<p align="center">PUBLIQUE-SE. REGISTRE-SE. CUMPRA-SE</p>
			
	</mod:documento>
</mod:modelo>