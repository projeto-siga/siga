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
				<mod:selecao titulo="Dr" var="gen"  opcoes="masc;fem" reler="não" />
				<mod:selecao titulo="Juiz Federal" var="titulo" opcoes="Titular;Substituto" reler="não"/>
			</mod:grupo>
				
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="pessoa" />
			</mod:grupo>

			<mod:grupo>
				<mod:texto titulo="período de fruição" var="para"  largura="50"/>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="referentes ao período" var="per"  largura="50"/>
			</mod:grupo>

	</mod:entrevista>
	<mod:descricao>
		<mod:valor var="texto_ato">
			<html>
				<body>
					<c:set var="pessoa_resp" value ="${f:pessoa(requestScope['pessoa_pessoaSel.id'])}" />
					<p style="TEXT-INDENT: 2cm" align="justify">
						O Doutor ${doc.subscritor.descricao}, Corregedor-Regional da Justiça 
						Federal da <br>2ª Região, no uso de suas atribuições legais RESOLVE:
						<br/><br/><br/>
						Retificar a Portaria nº ${port}, desta Corregedoria-Regional, no que tange
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
						${pessoa_resp.nomePessoa}, para explicitar que as férias designadas para ${para }
						são  referentes ao ${per}.


						
				</body>
			</html>
		</mod:valor>
	</mod:descricao>

</mod:modelo>