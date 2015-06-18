<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CONCESSÃO DE HORARIO ESPECIAL AO SERVIDOR PUBLICO ESTUDANTE 
- PEDIDO -->


<mod:modelo>
	<mod:entrevista>
	<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
					opcoes="Normal;Pequeno;Grande" />
	<mod:grupo>
	<mod:texto  var="nomedep" titulo="Dependente "  />
	</mod:grupo>
	</mod:entrevista>
	<mod:documento>
	<c:if test="${empty tamanhoLetra or tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 2cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
		<body></body></html>
		<center><c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" /></center>
		<p style="TEXT-INDENT: 2cm" align="justify">
		Eu, ${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.sigla}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao},
		<b>DECLARO</b> , a fim de fazer prova junto ao Plano de Saúde, que <b> ${nomedep}</b> permanece na codição de solteiro(a), estudante e vive sob minha dependência econômica.
		</p>
		<br><br>		
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
	
			
		
</mod:documento>
</mod:modelo>
