<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>	
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="nestesTermos" value="Nestes termos, aguarda o atendimento" scope="request" />
<c:set var="apenasNome" value="Sim" scope="request" />
<c:set var="para" value="sinap" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DO FUNCIONÁRIO">
			<mod:texto titulo="Ramal" var="ramal"/>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>		
		<mod:valor var="texto_requerimento"><p style="TEXT-INDENT: 2cm" align="justify">		
		<br>${doc.subscritor.descricao}, ${doc.subscritor.funcaoString}, 
		${doc.subscritor.padraoReferenciaInvertido}, matrícula nº ${doc.subscritor.sigla}, do Quadro de Pessoal Permanente da Justiça Federal de Primeiro Grau no Rio de Janeiro,
		lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, 
		ramal ${ramal}, vem requerer a V. Sa. a elaboração de projeção de aposentadoria.
		</p>
		</mod:valor>
	</mod:documento>
	
</mod:modelo>