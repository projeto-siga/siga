<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		<mod:grupo><mod:texto titulo="Lotação a ser excluída do banco de permutas" var="lotaDesejada" largura="60"/></mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo},
			${doc.subscritor.padraoReferenciaInvertido}, do Quadro de Pessoal Permanente 
			da Justiça Federal de Primeiro Grau no Rio de Janeiro, lotado(a) no(a)
			${doc.subscritor.lotacao.nomeLotacao}, vem requerer a Vossa Senhoria exclusão do Banco de Permutas, em relação à(s) seguinte(s) unidade(s): ${lotaDesejada}.</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>
