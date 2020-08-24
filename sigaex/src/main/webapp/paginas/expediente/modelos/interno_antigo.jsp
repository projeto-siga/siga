<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
	</mod:entrevista>
	<mod:documento>
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
		<table width="100%" align="left" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td width="100%"> 
					<table width="100%" border="0" cellpadding="2">
						<tr> 
							<td width="100%" align="center"><img src="contextpath/imagens/brasao2.png" width="50" height="50"/></td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-family:AvantGarde Bk BT, Arial;font-size:11pt;">${f:resource('modelos.cabecalho.titulo')}</td>
						</tr>
						<c:if test="${not empty f:resource('modelos.cabecalho.subtitulo')}">
							<tr>
								<td width="100%" align="center" style="font-family:Arial;font-size:10pt;font-weight: bold;">${f:resource('modelos.cabecalho.subtitulo')}</td>
							</tr>
						</c:if>
						<tr>
							<td width="100%" align="center" style="font-family:AvantGarde Bk BT, Arial;font-size:8pt;"><c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</td></tr>
		<tr><td></td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%" border="0" cellpadding="12" cellspacing="12">
						<tr>
							<td width="50%" align="left" style="font-family:Arial;font-size:10pt;font-weight: bold;">Expediente Interno N&ordm; ${doc.codigo}</td>
							<td width="50%" align="right" style="font-family:Arial;font-size:10pt;font-weight: bold;">${doc.dtExtenso}</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td width="100%"> 
					<table width="100%" border="0" cellpadding="2">
						<tr>
							<td width="100%" align="center" style="font-family:AvantGarde Bk BT, Arial;font-size:11pt;">${f:resource('modelos.cabecalho.titulo')}</td>
						</tr>
						<c:if test="${not empty f:resource('modelos.cabecalho.subtitulo')}">
							<tr>
								<td width="100%" align="center" style="font-family:Arial;font-size:10pt;font-weight: bold;">${f:resource('modelos.cabecalho.subtitulo')}</td>
							</tr>
						</c:if>
						<tr>
							<td width="100%" align="center" style="font-family:AvantGarde Bk BT, Arial;font-size:8pt;"><c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM CABECALHO -->
		<br />
		<br />
		<div style="font-family:Arial;font-size:10pt;">
		<table width="100%" border="0" cellpadding="12" cellspacing="12"
			bgcolor="#FFFFFF">
			<tr>
				<td width="25%">Número Original:</td>
				<td width="75%">${doc.numExtDoc}</td>
			</tr>
			<tr>
				<td>Número no Sistema Antigo:</td>
				<td>${doc.numAntigoDoc}</td>
			</tr>
			<tr>
				<td>Forma:</td>
				<td>${doc.exFormaDocumento.descrFormaDoc}</td>
			</tr>
			<tr>
				<td>Modelo:</td>
				<td>${doc.exModelo.nmMod}</td>
			</tr>
			<tr>
				<td>Subscritor:</td>
				<td>${doc.subscritorString}</td>
			</tr>
			<tr>
				<td>Destinatário:</td>
				<td>${doc.destinatarioString}</td>
			</tr>
			<tr>
				<td>Descrição:</td>
				<td>${doc.descrDocumento}</td>
			</tr>
			<tr>
				<td>Cadastrante:</td>
				<td>${doc.cadastrante.descricao}</td>
			</tr>
			<tr>
				<td>Data do cadastro:</td>
				<td>${doc.dtRegDocDDMMYYHHMMSS}</td>
			</tr>
		</table>
		</div>

		<!-- INICIO PRIMEIRO RODAPE
		
		<table align="left" width="100%" bgcolor="#FFFFFF">
		    <tr>
		        <td width="70%">
		        </td>
		        <td width="30%" >
		        <table align="right" width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" bgcolor="#000000">
		            <tr>
		                <td align="center" width="60%" style="border-collapse: collapse; border-color: black; font-family:Arial; font-size:8pt;" bgcolor="#FFFFFF"><i>Classif. documental</i></td>
		                <td align="center" width="40%" style="border-collapse: collapse; border-color: black; font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${doc.exClassificacao.sigla}</td>
		            </tr>
		        </table>
		        </td>
		    </tr>
		    
		</table>
		
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td width="100%" align="center">#pg</td>
			</tr>
		</table>
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>

