<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="left">
	<tr >
		<td align="left" valign="bottom" width="15%"><img
			src="contextpath/imagens/brasao2.png" width="82" height="82" /></td>
		<td width="85%">
		<table align="left" width="100%">
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${f:resource('modelos.cabecalho.titulo')}</p>
				</td>
			</tr>
			<c:if test="${not empty f:resource('modelos.cabecalho.subtitulo')}">
				<tr>
					<td width="100%" align="left">
					<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${f:resource('modelos.cabecalho.subtitulo')}</p>
					</td>
				</tr>
			</c:if>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				<c:choose>
					<c:when test="${empty mov}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when>
					<c:otherwise>${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:otherwise>
				</c:choose></p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
