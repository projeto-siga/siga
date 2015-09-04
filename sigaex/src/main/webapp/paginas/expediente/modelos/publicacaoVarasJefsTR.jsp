<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<%--<input type="hidden" name="isFormularioPublicacao" value="true" /> 
	<input type="hidden" name="vars" value="isFormularioPublicacao" />--%>
	<mod:entrevista>
		<mod:grupo titulo="Dados referenciais">
			<mod:grupo>
				<mod:lotacao titulo="Lotação de Origem" var="lotOrigem" />
				<span><b>&nbsp;&nbsp;(Campo opcional. Preencher caso o subscritor tenha lotação diversa da origem do documento.)</b></span>
			</mod:grupo>
			<mod:grupo>
				<mod:mensagem titulo="Tipo de Matéria:" />
				<c:choose>
					<c:when test="${param.entrevista == 1}">
						<select  name="tipoMateria" onchange="javascript:document.getElementById('descrTipoMateria').value=this.options[this.selectedIndex].text;document.getElementById('idTipoMateria').value=this.options[this.selectedIndex].value">
							<c:forEach items="${f:listaPublicacao(exDocumentoDTO.modelo.idMod)}" var="item">
								<option value="${item.idDocPublicacaoString}" ${item.idDocPublicacao == idTipoMateria ? 'selected' : ''}>
									${item.nmDocPublicacao}
								</option>  
							</c:forEach>
						</select>
					</c:when>
					<c:otherwise>
						<span class="valor">${descrTipoMateria}</span>
					</c:otherwise>
				</c:choose>

				<c:if test="${empty idTipoMateria}">
					<c:set var="descrTipoMateria"
						value="${listaPublicacao[0].nmDocPublicacao}" />
					<c:set var="idTipoMateria"
						value="${listaPublicacao[0].idDocPublicacaoString}" />
				</c:if>
				<input type="hidden" id="descrTipoMateria" name="descrTipoMateria"
					value="${descrTipoMateria}" />
				<input type="hidden" id="idTipoMateria" name="idTipoMateria"
					value="${idTipoMateria}" />
				<input type="hidden" name="vars" value="idTipoMateria" />
				<input type="hidden" name="vars" value="descrTipoMateria" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Título da Matéria" var="tituloMateria"
					largura="40" />
			</mod:grupo>
			<mod:caixaverif titulo="Matéria de Juiz Distribuidor"
				var="juizDistribuidor" />
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo da publicação">
			<mod:grupo>
				<mod:editor titulo="" var="texto_publicacao" />
			</mod:grupo>
		</mod:grupo>

		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
			opcoes="Normal;Pequeno;Grande" />

	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}">
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
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">PUBLICA&Ccedil;&Atilde;O NO DI&Aacute;RIO ELETR&Ocirc;NICO N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr><td>&nbsp;</td></tr>
						<tr>
							<td align="left"><mod:letra tamanho="${tl}"><p>Título: ${tituloMateria}</p></mod:letra></td>
						</tr>
						<tr><td>&nbsp;</td></tr>
						<tr>
							<td align="left"><mod:letra tamanho="${tl}"><p>Tipo de Matéria: ${descrTipoMateria}</p></mod:letra></td>
						</tr>
						<tr><td>&nbsp;</td></tr>
						<tr>
							<td align="left"><mod:letra tamanho="${tl}">
								<p>Lotação de Origem:
									<c:choose>
										<c:when test="${not empty requestScope['lotOrigem_lotacaoSel.sigla']}">   
											${requestScope['lotOrigem_lotacaoSel.sigla']}
										</c:when>
										<c:otherwise>
											<c:if test="${not empty doc.lotaSubscritorEfetiva}">
												${doc.lotaSubscritorEfetiva.siglaLotacao}
											</c:if>
										</c:otherwise>
									</c:choose>
								</p></mod:letra>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		
		<!-- INICIO NUMERO <p style="font-family: Arial; font-size: 11pt; font-weight: bold;"align="left">Publicaçã N&ordm; ${doc.codigo}</p> FIM NUMERO -->
		<!-- INICIO TITULO 
			<mod:letra tamanho="${tl}">
				<p>${tituloMateria}</p>
			</mod:letra>
		FIM TITULO -->

		<mod:letra tamanho="${tl}">
			<p><br>
			<br>
			<br>
			</p>

			<span style="font-size: ${ t1}"> <!-- INICIO MIOLO -->
			<p>${texto_publicacao}</p>
			 </span>
			<p><br />
			</p>
			<!-- FIM MIOLO -->
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		</mod:letra>

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>

