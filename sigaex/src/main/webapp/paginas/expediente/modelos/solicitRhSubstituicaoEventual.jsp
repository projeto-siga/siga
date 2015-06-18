<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/solicitacao.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Servidor" var="servidor" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Função" var="funcao" />
		</mod:grupo>
		<mod:grupo titulo="Período Solicitado">
			<mod:data titulo="De" var="dataInicio" />
			<mod:data titulo="a" var="dataFim" />
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_solicitacao">
		<p style="TEXT-INDENT: 2cm" align="justify">Solicito as providências necessárias para que o(a) servidor(a)
		<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" nivelHierarquicoMaximoDaLotacao="4" /> seja designado(a) para atuar como substituto(a) eventual do
		cargo em comissão/função comissionada de <b>${funcao}</b><c:choose><c:when test="${(empty dataInicio) and (empty dataFim)}">.</c:when><c:when test="${(dataInicio == dataFim) or (empty dataFim)}">, a partir de <b>${dataInicio}</b>.</c:when><c:otherwise>, no período de <b>${dataInicio}</b> a <b>${dataFim}</b>.</c:otherwise></c:choose></p>
		</mod:valor>
		
	</mod:documento>
</mod:modelo>

