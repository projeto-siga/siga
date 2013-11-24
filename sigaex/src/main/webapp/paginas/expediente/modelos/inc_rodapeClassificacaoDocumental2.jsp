<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table align="left" width="100%" bgcolor="#FFFFFF">
	<col width="70%"></col>
	<col width="30%"></col>
	<tr>
		<td width="70%"></td>
		<td width="30%">
		<table align="right" width="100%" cellspacing="0"
			style="border-width: 1px 1px 1px 1px; border-style: solid;">
			<col width="60%"></col>
			<col width="40%"></col>
			<tr>
				<td align="center" valign="bottom" width="60%"
					style="font-size: 8pt; font-style: italic; border-width: 0px 0px 0px 0px; border-style: solid; margin: 0">Classif.
				documental</td>
				<td valign="bottom" align="center" width="40%"
					style="font-size: 8pt; border-width: 0px 0px 0px 1px; border-style: solid; margin: 0;">${doc.exClassificacao.sigla}</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
