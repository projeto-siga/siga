<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table align="left" width="100%" bgcolor="#FFFFFF">
	<tr>
		<td width="70%"></td>
		<td width="30%">
		<table align="right" width="100%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td align="center" width="60%"
					style="font-family:Arial;font-size:8pt;text-decoration:italic;"
					bgcolor="#FFFFFF">Classif. documental</td>
				<td align="center" width="40%"
					style="font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${doc.exClassificacao.sigla}</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
