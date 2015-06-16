<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table align="left" width="100%">
	<tr>
		<td width="60%"></td>
		<td width="40%">
		<table align="right" width="100%"
			style="border-width: 1px; border-style: solid; border-color: #666666;">
			<colgroup>
				<col width="65%" />
				<col width="35%" />
			</colgroup>
			<tr>
				<td align="center"
					style="border-width: 0px 1px 0px 0px; border-style: solid; border-color: #666666; font-family: Arial; font-size: 8pt; color: #666666;"><i>Classif.
				documental</i></td>
				<td align="center"
					style="font-family: Arial; font-size: 8pt; color: #666666">${doc.exClassificacao.sigla}</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
