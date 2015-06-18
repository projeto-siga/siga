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
				<mod:texto titulo="Nº" var="port" largura="14"/>
			</mod:grupo>		
			<mod:grupo>
				<mod:texto titulo="referente ao período" var="periodo" largura="50"/>
			</mod:grupo>

			<mod:grupo>
				<mod:selecao titulo="Dr" var="gen"  opcoes="masc;fem" reler="não" />
				<mod:selecao titulo="Juiz Federal" var="titulo" opcoes="Titular;Substituto" reler="não"/>
			</mod:grupo>
				
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="pessoa" />
			</mod:grupo>
			<mod:grupo>
				<mod:data titulo="a partir de" var="data"/>
				<mod:texto titulo="dias restantes a serem fruídos" var="dias" largura="3"/>
				
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="em virtude" var="mot" largura="48"/>
				<mod:selecao titulo="Nº de periodos" var="numPer" opcoes="1;2;3;4;5;6;7;8;9;10" reler="ajax" idAjax="numPerAjax" />
			</mod:grupo>
			<hr>
			<mod:grupo titulo="Periodos" depende="numPerAjax">
				<c:forEach var="i" begin="1" end="${numPer}">
					<mod:grupo>
						<mod:selecao titulo="periodo" var="per${i}" opcoes="1º;2º"/>
						<mod:texto titulo="ano" var="ano${i}"  largura="9"/>
					</mod:grupo>
					<hr>
				</c:forEach>
		</mod:grupo>

	</mod:entrevista>
	<mod:descricao>
		<mod:valor var="texto_ato">
			<html>
				<body>
					<c:set var="pessoa_resp" value ="${f:pessoa(requestScope['pessoa_pessoaSel.id'])}" />
					<p style="TEXT-INDENT: 2cm" align="justify">
						O Doutor ${doc.subscritor.descricao}, Corregedor-Regional da Justição 
						Federal da <br>2ª Região, no uso de suas atribuições legais RESOLVE:
						<br/><br/><br/>
						Interromper, a partir do dia ${ data},inclusive,por necessidade de serviço, as férias aprovadas
						pela Portaria nº ${port}, desta Corregedoria, referentes ao ${periodo}, relativas
						<c:choose>
							<c:when test="${gen=='fem'}">a MM. Juíza Federal</c:when>
							<c:otherwise>ao MM.Juiz Federal</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${titulo == 'Titular'}">titular</c:when>
							<c:when test="${titulo != 'Titular' && gen == 'fem'}">substituta</c:when>
							<c:when test="${titulo != 'Titular' && gen == 'masc'}">substituto</c:when>
						</c:choose>
						do(a) ${pessoa_resp.lotacao.descricao},
						<c:choose>
							<c:when test="${gen=='fem'}">Drª.</c:when>
							<c:otherwise>Dr.</c:otherwise>
						</c:choose>
						${pessoa_resp.nomePessoa}, em virtude ${ mot},explicitando que:
						</p>
						<c:forEach var="i" begin="1" end="${numPer}">
							<c:if test="${ i == 1}"> 
								- ${requestScope[f:concat('per',i)]} período aquisitivo de ${requestScope[f:concat('ano',i)]} (${dias} dias) será fruído oportunamente;<br>
							</c:if>
							<c:if test="${ i != 1}"> 
							- ${requestScope[f:concat('per',i)]} período aquisitivo de ${requestScope[f:concat('ano',i)]} será fruído oportunamente;<br>
							</c:if>
						</c:forEach>
						
				</body>
			</html>
		</mod:valor>
	</mod:descricao>

</mod:modelo>