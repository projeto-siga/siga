<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CONCESSÃO DE HORARIO ESPECIAL AO SERVIDOR PUBLICO ESTUDANTE 
- CANCELAMENTO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:documento>
	<mod:valor var="texto_requerimento">		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria <B>CANCELAMENTO DO HORÁRIO ESPECIAL AO SERVIDOR ESTUDANTE</B>, nos termos do art. 9º da Resolução n.º 5/2008 do Conselho da Justiça Federal, uma vez que cessaram os motivos que 
		ensejaram sua concessão.
		</p><br>
	</mod:valor>	
	<mod:valor var="texto_requerimento4">
	<br><br>			
		<p style="TEXT-INDENT: 2cm" align="justify">CIENTE,</p><br><br>
		<p style="TEXT-INDENT: 2cm" align="justify">________________________________________________________</p>
		<p style="TEXT-INDENT: 2cm" align="justify">Assinatura e Matrícula do Superior Hierárquico</p>
	</mod:valor>	
</mod:documento>
</mod:modelo>
