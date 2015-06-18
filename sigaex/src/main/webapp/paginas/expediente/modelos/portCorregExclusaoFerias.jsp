<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/portariaCorregedoria.jsp">
	<mod:entrevista>
			<mod:grupo>
				<mod:texto titulo="Nº" var="port"/>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="referente ao período" var="periodo"/>
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Dr" var="gen"  opcoes="masc;fem" reler="não" />
				<mod:selecao titulo="Juiz Federal" var="juiz" opcoes="Substituto;Titular" reler="não"/>
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="pessoa" />
			</mod:grupo>
					
			<mod:grupo titulo = "Periodo Marcado">
				<mod:data titulo="de" var="dtMarcada1"/>
				<mod:data titulo="a" var="dtMarcada2"/>
			</mod:grupo>
				
	


	</mod:entrevista>
	
	<mod:documento>
		<c:set var="pessoa_titular" value ="${f:pessoa(requestScope['pessoa_pessoaSel.id'])}" />

		<mod:valor var="texto_ato">
		<html>
			<body>
			<p style="TEXT-INDENT: 2cm" align="justify">
				O Doutor ${doc.subscritor.descricao}, Corregedor-Regional da Justição 
				Federal da <br>2ª Região, no uso de sua de suas atribuições legais RESOLVE:
				<br/><br/><br/>
				Alterar, a pedido, a portaria ${port} de ${data} desta Corregedoria-Regional, no que 
				tange
				<c:choose>
					<c:when test="${gen=='fem'}">a MM. Juiza Federal</c:when>
					<c:otherwise>o MM. Juiz Federal</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${gen=='fem' && juiz =='Substituto'}">substituta</c:when>
					<c:when test="${gen!='fem' && juiz =='Substituto'}">substituto</c:when>
					<c:otherwise>titular</c:otherwise>
				</c:choose>
				do(a) ${pessoa_titular.lotacao.descricao},
				<c:choose>
					<c:when test="${gen=='fem'}">Drª.</c:when>
					<c:otherwise>Dr.</c:otherwise>
				</c:choose>
				${ pessoa_titular.nomePessoa}, para explicitar que as férias referentes ao ${periodo}, 
				anteriormente designadas para ${dtMarcada1} a ${dtMarcada2} serão fruídas oportunamente. 
			</body>
		</html>
		</mod:valor>
		
	</mod:documento>
</mod:modelo>