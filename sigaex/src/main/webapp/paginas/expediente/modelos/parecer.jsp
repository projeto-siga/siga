<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados referenciais">
		
			<mod:grupo>
				<mod:texto titulo="Assunto" var="assunto" largura="30" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Vocativo" var="vocativo" largura="30" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo do Parecer">
			<mod:grupo>
				<mod:editor titulo="" var="texto_parecer"/>
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
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight:bold;"></br>
							<c:choose>
								<c:when test="${doc.exModelo.nmMod == 'Parecer Médico'}">
									PARECER MÉDICO N&ordm; ${doc.codigo}</p></td>
								</c:when>
								<c:when test="${doc.exModelo.nmMod == 'Parecer Psicológico'}">
									PARECER PSICOLÓGICO N&ordm; ${doc.codigo}</p></td>
								</c:when>
								<c:when test="${doc.exModelo.nmMod == 'Parecer'}">
									PARECER N&ordm; ${doc.codigo}</p></td>
								</c:when>
								<c:otherwise>
									PARECER SOCIAL N&ordm; ${doc.codigo}</p></td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr><td><br></td></tr>
						<tr>
						    <c:if test="${not empty assunto}">
							<td align="left"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;MARGIN-RIGHT: 7cm">ASSUNTO: ${assunto}</p></mod:letra></td>
							</c:if>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<mod:letra tamanho="${tl}">
		<p><br>
		<br>
		</p>
		<span style="font-size:${tl};">
		<p style="TEXT-INDENT: 2cm">${vocativo}</p>
		<p style="TEXT-INDENT: 2cm">${texto_parecer}</p>
		</span>
		<p>
		<br/>
		</p>
		
		<p align="center" >${doc.dtExtenso}</p>
		
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		
		
		</mod:letra>
		
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
	
		</body>
		</html>
	</mod:documento>
</mod:modelo>

