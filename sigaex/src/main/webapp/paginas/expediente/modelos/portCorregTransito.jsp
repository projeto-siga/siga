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
				<mod:texto titulo="Fundamento" var="dec" largura="60"/>
			</mod:grupo>

			<mod:grupo>
				<mod:selecao titulo="Dr" var="gen"  opcoes="masc;fem" reler="não" />
				<mod:selecao titulo="Juiz Federal" var="titulo" opcoes="Titular;Substituto" reler="não"/>
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="em virtude" var="mot"  opcoes="remoção;promoção"/>
				<mod:selecao titulo="Juiz Federal" var="titulo2" opcoes="Titular;Substituto" reler="não"/>
			</mod:grupo>	
			<mod:grupo>
				<mod:texto titulo="nova lotação" var="lotacao" largura="55"/>
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="pessoa" />
			</mod:grupo>

			<mod:grupo>
				<mod:data titulo="de" var="dt"/>
				<mod:texto titulo="dias deferidos" var="dias" largura ="3"/>
			</mod:grupo>

	</mod:entrevista>
	<mod:descricao>
		<mod:valor var="texto_ato">
			<html>
				<body>
					<c:set var="pessoa_resp" value ="${f:pessoa(requestScope['pessoa_pessoaSel.id'])}" />
					<p style="TEXT-INDENT: 2cm" align="justify">
						O Excelentíssimo Dr. ${doc.subscritor.descricao}, Corregedor-Regional da Justiça 
						Federal da 2ª Região, no uso de suas atribuições legais e conforme a competência
						estabelecida pelo art. 4º da Resolução nº30/TRF/2006, ${dec},
						<br><br>
						RESOLVE:
						<br/><br/><br/>
						CONCEDER 
						<c:choose>
							<c:when test="${gen=='fem'}">a MM. Juiza Federal</c:when>
							<c:otherwise>ao MM.Juiz Federal</c:otherwise>
						</c:choose>
						${pessoa_resp.nomePessoa}, ${dias} dias de trânsito, a contar de ${dt}, inclusive
						nos termos do art. 3º §§ 1º e 2º da Resolução nº 30/TRF/2006, em virtude da sua ${mot}
						de
						<c:choose>
							<c:when test="${gen=='fem'}">Juiza </c:when>
							<c:otherwise>Juiz</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${titulo == 'Titular'}">titular</c:when>
							<c:when test="${titulo != 'Titular' && gen == 'fem'}">substituta</c:when>
							<c:when test="${titulo != 'Titular' && gen == 'masc'}">substituto</c:when>
						</c:choose>
						do(a) ${pessoa_resp.lotacao.descricao}, para 
						<c:choose>
							<c:when test="${gen=='fem'}">Juiza </c:when>
							<c:otherwise>Juiz</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${titulo2 == 'Titular'}">Titular</c:when>
							<c:when test="${titulo2 != 'Titular' && gen == 'fem'}">Substituta</c:when>
							<c:when test="${titulo2 != 'Titular' && gen == 'masc'}">Substituto</c:when>
						</c:choose>
						da ${lotacao}.


						
				</body>
			</html>
		</mod:valor>
	</mod:descricao>

</mod:modelo>