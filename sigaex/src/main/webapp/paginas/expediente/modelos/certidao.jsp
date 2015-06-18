<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CERTIDÃO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>		
		<mod:grupo titulo="CERTIDÃO"></mod:grupo>		
 		<mod:memo colunas="66" linhas="6" titulo="Detalhes a constar" var="certidao" reler="sim"/>
		
	</mod:entrevista>	
	<mod:documento>		
			
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência que seja expedida <b>CERTIDÃO</b> onde conste:
			</p>
			<p style="TEXT-INDENT: 2cm" align="justify">
			<i>${certidao}</i>
			</p>
		
		</mod:valor>

</mod:documento>
</mod:modelo>
