<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="A SEC está na Programação Anual?">
			<mod:radio titulo="Sim" var="progrAnual" valor="1" marcado="Sim" />
			<mod:radio titulo="Não" var="progrAnual" valor="2" />
		</mod:grupo>
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
							<td width="100%" align="center" style="font-family:AvantGarde Bk BT, Arial;font-size:11pt;">PODER JUDICIÁRIO</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-family:Arial;font-size:10pt;font-weight: bold;">JUSTIÇA FEDERAL</td>
						</tr>
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
							<td width="50%" align="left" style="font-family:Arial;font-size:10pt;font-weight: bold;">Expediente Interno <br> N&ordm; ${doc.codigo}</td>
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
							<td width="100%" align="center" style="font-family:AvantGarde Bk BT, Arial;font-size:11pt;">PODER JUDICIÁRIO</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-family:Arial;font-size:10pt;font-weight: bold;">JUSTIÇA FEDERAL</td>
						</tr>
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
		<c:if test="${progrAnual =='2'}">
			<c:set var="varSN" value="não" scope="request" />
		</c:if>
		<div style="font-family: Arial; font-size: 10pt;">
		<table width="100%" border="1" cellpadding="4" bgcolor="#000000">
			<tr>
				<td colspan=2><b>Objeto:</b>&nbsp;${doc.descrDocumento}</td>
			</tr>
			<tr>
				<td colspan=2><b>A SEC ${varSN} está na Programação Anual.</b></td>
			</tr>
		</table>
		</div>

		<!-- INICIO PRIMEIRO RODAPE
		<table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td>
					<table align="right" width="30%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
						<tr bgcolor="#FFFFFF">
							<td align="center" style="font-family:Arial;font-size:8pt;text-decoration:italic;" width="60%">Classif. documental</td>
							<td align="center" style="font-family:Arial;font-size:8pt;" width="40%">${doc.exClassificacao.sigla}</td>
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
	<mod:assinatura>
		<c:if test="${doc.lotaTitular.orgaoUsuario.idOrgaoUsu == '1'}">
			{SEC Assinada! Iniciando procedimento "Contratação: fase de análise".}
			<c:set var="f" value="${f:criarWorkflow('Contratação: fase de análise', doc, cadastrante, titular, lotaCadastrante, lotaTitular)}" />
	    </c:if>
	</mod:assinatura>
</mod:modelo>

