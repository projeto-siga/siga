<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados referenciais">
			<mod:grupo>
				<mod:selecao titulo="Tipo" var="tipo_mandado" opcoes="Intimação;Citação;Penhora"/>  
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Referência" var="referencia" largura="40" />
			</mod:grupo>
			<mod:grupo >
				<mod:texto titulo="Vocativo" var="vocativo" largura="20" />Ex.: O doutor; A doutora.
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Autoridade" var="autoridade" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Função" var="funcao" largura="45"/>
			</mod:grupo>
		    <mod:grupo titulo="Texto a ser inserido no corpo do Mandado">
			    <mod:grupo>
					<mod:editor titulo="" var="texto_mandado"/>
				</mod:grupo>
			</mod:grupo>	
			<mod:grupo>
				<mod:texto titulo="Base Legal" var="baselegal" largura="40" />
			</mod:grupo>
		</mod:grupo>
		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
		
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="11pt"/></c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="9pt"/></c:if>
		<c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="13pt"/></c:if>
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
		<body>
			<!-- INICIO PRIMEIRO CABECALHO
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<br><br/><c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
						</td>	
					</tr>
					<tr bgcolor="#FFFFFF">
						<td cellpadding="5">
							&nbsp;
						</td>
					</tr>
				</table>
			FIM PRIMEIRO CABECALHO -->

			<!-- INICIO CABECALHO
				<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
				
			FIM CABECALHO -->	
		
			<table width=100% border=0 bgcolor="#FFFFFF">
				<tr><td align="center"><b>MANDADO DE ${fn:toUpperCase(tipo_mandado)} - N&ordm; ${doc.codigo} - </b></td></tr>
				<c:if test="${not empty referencia}">
		        <tr><td align="center">Ref.:&nbsp<b>${referencia} <br><br><br></b></td></tr>
		        </c:if>
		        <tr><td align="center">${fn:toUpperCase(vocativo)}&nbsp ${requestScope['autoridade_pessoaSel.descricao']}</td></tr>
		        <tr><td align="center"> ${f:pessoa(requestScope['autoridade_pessoaSel.id']).cargo.descricao} </td></tr>
		        <tr><td align="center"> ${fn:toUpperCase(funcao)}<br><br><br></td></tr>
			</table>
			<p align="center"><b><u>MANDA</u></b></p>
			<p align="justify">${texto_mandado}</p>
			</br>
			<p align="center">${doc.dtExtenso}</p> 
		
	<%--	<table align="center" border=0 bgcolor="#FFFFFF">
		<tr><td align="center"><c:import url="/paginas/expediente/modelos/inc_assinatura.jsp"/></td></tr>
		<tr><td align="center"><b>${baselegal}<b></td></tr>
		</table> --%>
		
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp"/>
		<p align="center"><b>${baselegal}<b/><p/>
		
		
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		</body>
		</html>
		</mod:documento>
</mod:modelo>	