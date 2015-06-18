<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo urlBase="/paginas/expediente/modelos/portaria.jsp">
	<mod:entrevista>
		<c:if test="${empty texto_portaria}">
		<c:set var="texto_portaria" scope="request">
			<!-- Essa marcação da abertura não está funcionando. A marcação do início da abertura 
			está sendo apagada no processamento do modelo -->
			<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2cm" align="justify"><b>O JUIZ FEDERAL
			- DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA
			JUSTIÇA FEDERAL DE 1&ordm; GRAU - SEÇÃO JUDICIÁRIA DO RIO
			DE JANEIRO</b>, no uso de suas atribuições legais, </p>

			<p style="TEXT-INDENT: 2cm" align="justify"><b>RESOLVE:</b></p><!-- FIM ABERTURA -->
			
			<!-- INICIO CORPO --><p style="TEXT-INDENT: 2cm" align="justify">&nbsp;</p><!-- FIM CORPO -->
		</c:set>
		</c:if>
	</mod:entrevista>
</mod:modelo>

