<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Vocativo" var="vocativo" largura="30" />
			</mod:grupo>
			<mod:grupo titulo="Texto a ser inserido no corpo da carta">
				<mod:grupo>
					<mod:editor titulo="" var="texto_carta" />
				</mod:grupo>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Fecho (de acordo com o vocativo)" var="fecho"
					largura="60" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Dados do destinatário">
			<c:if
				test="${(not empty destinatarioSel.id) and (tipoDestinatario == 1)}">
				<mod:grupo>
					<mod:texto titulo="Forma de endereçamento" var="enderecamento_dest"
						largura="30" />
				</mod:grupo>
				<mod:caixaverif
					titulo="Especificar manualmente todos os dados do destinatário"
					var="especificarDest" reler="sim" />
				<c:choose>
					<c:when test="${especificarDest == 'Sim'}">
						<mod:grupo>
							<mod:texto titulo="Nome" var="nome_dest" largura="60" />
						</mod:grupo>
						<mod:grupo>
							<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
						</mod:grupo>
						<mod:grupo>
							<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="nome_dest" valor="${doc.destinatario.descricao}" />
						<mod:oculto var="cargo_dest"
							valor="${doc.destinatario.cargo.nomeCargo}" />
						<mod:oculto var="orgao_dest"
							valor="${doc.destinatario.lotacao.descricao}" />
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if
				test="${( empty destinatarioSel.id) and (tipoDestinatario == 1)}">
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />
				</mod:grupo>
			</c:if>
			<c:if
				test="${( empty lotacaoDestinatarioSel.id) and (tipoDestinatario == 2)}">

				<mod:grupo>
					<mod:texto titulo="Nome" var="nome_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
				</mod:grupo>
				<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />

				<mod:grupo>
					<mod:texto titulo="Forma de endereçamento" var="enderecamento_dest"
						largura="30" />
				</mod:grupo>
			</c:if>
			<c:if
				test="${( not empty lotacaoDestinatarioSel.id) and (tipoDestinatario == 2)}">
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Forma de endereçamento" var="enderecamento_dest"
						largura="30" />
				</mod:grupo>
				<mod:caixaverif titulo="Especificar manualmente o Orgão"
					var="especificarDest" reler="sim" />
				<c:choose>
					<c:when test="${especificarDest == 'Sim'}">

						<mod:grupo>
							<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="orgao_dest"
							valor="${lotacaoDestinatarioSel.descricao}" />
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if
				test="${( empty orgaoExternoDestinatarioSel.id) and (tipoDestinatario == 3)}">
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
				</mod:grupo>
				<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />

				<mod:grupo>
					<mod:texto titulo="Forma de endereçamento" var="enderecamento_dest"
						largura="30" />
				</mod:grupo>
			</c:if>
			<c:if
				test="${( not empty orgaoExternoDestinatarioSel.id) and (tipoDestinatario == 3)}">
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Forma de endereçamento" var="enderecamento_dest"
						largura="30" />
				</mod:grupo>
				<mod:caixaverif titulo="Especificar manualmente o Orgão"
					var="especificarDest" reler="sim" />
				<c:choose>
					<c:when test="${especificarDest == 'Sim'}">

						<mod:grupo>
							<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="orgao_dest"
							valor="${orgaoExternoDestinatarioSel.descricao}" />
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if
				test="${( empty destinatarioSel.id) and (tipoDestinatario == 4)}">
				<mod:grupo>
					<mod:texto titulo="Forma de endereçamento" var="enderecamento_dest"
						largura="30" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />
				</mod:grupo>
			</c:if>
			
		</mod:grupo>
		<mod:grupo>
			<mod:memo titulo="Endereço" var="endereco_dest" linhas="4"
				colunas="60" />
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
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
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
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">CARTA ${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		<mod:letra tamanho="${tl}">
		<table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td align="left">
					<table align="left" width="100%" border="0" cellpadding="2" cellspacing="0">
						<tr><td><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;">${enderecamento_dest}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;">${nome_dest}</p></mod:letra></td></tr>
						<c:if test="${not empty cargo_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;">${cargo_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty orgao_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;">${orgao_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty endereco_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;"><siga:fixcrlf>${endereco_dest}</siga:fixcrlf></p></mod:letra></td></tr>
						</c:if>
					</table>
				</td>
			</tr>
		</table>
	
	
		<div style="font-family:Arial;font-size:10pt;">
		<p>&nbsp;</p>
		<p style="TEXT-INDENT: 2cm">${vocativo},</p>
		
		<p>
		<span style="font-size:${tl};">
		${texto_carta}
		</span>
		 <c:if test="${not empty fecho}">
			<center>${fecho},</center>
		</c:if></p>
		</div>
		
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		</mod:letra>
		<c:import 
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp"  />
	
		</body>
		</html>
	</mod:documento>
</mod:modelo>

