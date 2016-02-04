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
				<mod:selecao titulo="em virtude"  var="mot" opcoes="licença maternidade;licença paternidade;licença gala;licença por falecimento;outro"  reler="ajax" idAjax="motDifAjax" />
			</mod:grupo>
			<mod:grupo depende="motDifAjax" >
				<c:if test="${mot =='outro'}"><mod:texto titulo="tipo de motivo" var="mot2" /></c:if>
			</mod:grupo>
				
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="pessoa" />
			</mod:grupo>
			
			<mod:grupo titulo = "Periodo Suspenso">
				<mod:data titulo="de" var="dtSuspenso1"/>
				<mod:data titulo="a" var="dtSuspenso2"/>
			</mod:grupo>
			
			<mod:grupo titulo = "Periodo Restante">
				<mod:data titulo="de" var="dtRestante1"/>
				<mod:data titulo="a" var="dtRestante2"/>
				<mod:texto titulo="nº dias restantes" var="dias" largura ="3"/>
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
						Suspender, de ${dtSuspenso1} a ${dtSuspenso2}, inclusive, as férias aprovadas pela Portaria nº 
						${port} desta Corregedoria-Regional, no que tange 
						<c:choose>
							<c:when test="${gen=='fem'}">a MM. Juíza Federal</c:when>
							<c:otherwise>ao MM.Juíz Federal</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${titulo == 'Titular'}">titular</c:when>
							<c:when test="${titulo != 'Titular' && gen == 'fem'}">substituta</c:when>
							<c:when test="${titulo != 'Titular' && gen == 'masc'}">substituto</c:when>
						</c:choose>
						da ${pessoa_resp.lotacao.descricao},
						<c:choose>
							<c:when test="${gen=='fem'}">Drª.</c:when>
							<c:otherwise>Dr.</c:otherwise>
						</c:choose>
						${pessoa_resp.nomePessoa}, referentes ao ${periodo}, em virtude de
						<c:choose>
							<c:when test="${mot!='outro'}">${mot}</c:when>
							<c:otherwise>${mot2}</c:otherwise></c:choose>, nos termos da Resolução nº 014, 
						de 19 de maio de 2008, do Conselho da Justiça Federal, artigo 4º, § 4º e § 5º, explicitando 
						que os ${dias} dias restantes serão fruídos no periodo de ${dtRestante1} a ${dtRestante2}.
				</body>
			</html>
		</mod:valor>
	</mod:descricao>

</mod:modelo>