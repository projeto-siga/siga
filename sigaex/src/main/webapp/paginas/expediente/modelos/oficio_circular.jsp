<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados do destinatário">
			<mod:grupo>
				<mod:texto titulo="Forma de endereçamento" var="enderecamento"
					largura="30" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Nome" var="nome_dest" largura="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Cidade/UF" var="cidade_uf" largura="30" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Vocativo" var="vocativo" largura="30" />
			</mod:grupo>
			<mod:grupo titulo="Texto a ser inserido no corpo do ofício">
				<mod:grupo>
					<mod:editor titulo="" var="texto_oficio" />
				</mod:grupo>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Fecho (de acordo com o vocativo)" var="fecho"
					largura="60" />
			</mod:grupo>
		</mod:grupo>
		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="11pt"/></c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="9pt"/></c:if>
		<c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="13pt"/></c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head></head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="50%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">OF&Iacute;CIO CIRCULAR N&ordm; ${doc.codigo}</p></td>
							<td align="right" width="50%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
		<!-- INICIO NUMERO <span style="font-weight: bold;">OF&Iacute;CIO CIRCULAR N&ordm; ${doc.codigo}</span> FIM NUMERO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
			
		<mod:letra tamanho="${tl}">
		<!-- INICIO MIOLO -->
		
		<div style="font-family:Arial;font-size:10pt;">
		<p>&nbsp;</p>
		<!-- INICIO CORPO -->
		<p style="align:left;TEXT-INDENT: 2cm">${vocativo},</p>
		<span style="font-size:${tl};">
		<p>${texto_oficio}</p>
		</span>
		<!-- FIM CORPO -->
		<c:if test="${not empty fecho}">
			<p align="center"><!-- INICIO FECHO -->${fecho},<!-- FIM FECHO --></p>
		</c:if>
		<p align="center"><br />
		<br />
		</p>
		</div>
		<!-- FIM MIOLO -->
		<!-- INICIO ASSINATURA -->
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->	
		</mod:letra>
		<!-- INICIO PRIMEIRO RODAPE
		<table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td align="left">
					<table align="left" width="100%" border="0" cellpadding="2" cellspacing="0">
						<tr><td><mod:letra tamanho="${tl}"><p>${enderecamento}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p>${nome_dest}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p>${cargo_dest}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p>${cidade_uf}</p></mod:letra></td></tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
				</td>
			</tr>
		</table>
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>

