<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA AUSÊNCIA AO SERVIÇO EM RAZÃO DE CASAMENTO -->


<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">	
	<mod:documento>
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			<BR>${doc.subscritor.descricao},<c:if test="${not empty doc.subscritor.cargo.nomeCargo}">
			${doc.subscritor.cargo.nomeCargo},</c:if> 
			${doc.subscritor.padraoReferenciaInvertido}, vem requerer a Vossa Senhoria 
			<B>AUSÊNCIA AO SERVIÇO EM RAZÃO DE CASAMENTO</B> com base na alínea a,
			inciso III, art. 97 da Lei 8.112/90.
			</p>
			
	</mod:valor>	
</mod:documento>
</mod:modelo>
