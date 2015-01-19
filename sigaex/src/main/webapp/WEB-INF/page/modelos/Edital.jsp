<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<mod:modelo>
	<mod:entrevista>
				<mod:grupo>
					<mod:texto titulo="Edital de " var="titulo_edital" largura="45" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Prazo de " var="prazo" largura="45" />
				</mod:grupo>
				<mod:grupo titulo="Texto a ser inserido no Edital">
					<mod:grupo>
						<mod:editor titulo="" var="texto_edital" />
					</mod:grupo>
				</mod:grupo>
	</mod:entrevista>
	
	<mod:documento>
	<c:set var="tl" value="11pt"/>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >EDITAL N&ordm; ${doc.codigo}</p></td>
						</tr>
				<tr><td><br/><br/></td>
				</tr>
						<tr>
							<td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >EDITAL DE ${titulo_edital}</p></mod:letra></td>
						</tr>
							<tr><td><br/><br/></td>
							</tr>
						<c:if test="${not empty prazo}">
						<tr>
							<td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >(PRAZO DE ${prazo})</p></mod:letra></td>
						</tr>
						</c:if>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		
		<!-- INICIO NUMERO <p style="font-family:Arial;font-size:11pt;font-weight:bold;" >EDITAL N&ordm; ${doc.codigo}</p> FIM NUMERO -->
		<!-- INICIO TITULO 
			<mod:letra tamanho="${tl}">
				<p>${doc.codigo}</p>
			</mod:letra>
		FIM TITULO -->
		
		<mod:letra tamanho="${tl}"><!-- INICIO MIOLO --><!-- INICIO CORPO -->
			<p style="TEXT-INDENT: 2cm" align="justify">
			${texto_edital}
			</p>
		
		<p align="center">${doc.dtExtenso}</p>
		<br/>
		<!-- FIM CORPO -->
		<!-- INICIO ASSINATURA --><c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /><!-- FIM ASSINATURA -->
		<!-- FIM MIOLO -->
		</mod:letra>
		
			<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
	
	<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->


		</body>
		</html>
	</mod:documento>
</mod:modelo>

