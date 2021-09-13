<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="left" border="0" cellpadding="0"
	cellspacing="0" bgcolor="#FFFFFF">
	<tr bgcolor="#FFFFFF">
		<td width="100%">
		<table width="100%" border="0" cellpadding="2">
			<tr>
			<c:choose>
				<c:when test="${not empty f:resource('/siga.relat.brasao')}">
					<td width="100%" align="center" valign="bottom">
						<img src="contextpath/imagens/${f:resource('/siga.relat.brasao')}" width="65" height="65" />
					</td>
				</c:when>
				<c:otherwise>
					<td width="100%" align="center" valign="bottom">
						<img src="contextpath/imagens/brasao2.png" width="65" height="65" />
					</td>
				</c:otherwise>
			</c:choose>
			</tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${f:resource('modelos.cabecalho.titulo')}</p>
				</td>
			</tr>
			<c:if test="${not empty f:resource('modelos.cabecalho.subtitulo')}">
				<tr>
					<td width="100%" align="center">
					<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${f:resource('modelos.cabecalho.subtitulo')}</p>
					</td>
				</tr>
			</c:if>
			<tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;"><c:choose>
					<c:when test="${empty mov}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when>
					<c:otherwise>${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:otherwise>
				</c:choose></p>
				</td>
			</tr>
			</tr>
		</table>
		</td>
	</tr>
</table>
