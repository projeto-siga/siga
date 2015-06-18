<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>	
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo urlBase="/paginas/expediente/modelos/solicitacao.jsp">
	<mod:entrevista>
		<c:if test="${empty texto_solicitacao}">
		<c:set var="texto_solicitacao" scope="request">
			<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2cm" align="justify"><b>O JUIZ FEDERAL
			- DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA
			JUSTIÇA FEDERAL DE 1&ordm; GRAU - <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose></b>, no uso de suas atribuições legais, </p>

			<p style="TEXT-INDENT: 2cm" align="justify"><b>RESOLVE:</b></p><!-- FIM ABERTURA -->
			
			<p style="TEXT-INDENT: 2cm" align="justify">&nbsp;</p>
		</c:set>
		</c:if>
	</mod:entrevista>
</mod:modelo>



