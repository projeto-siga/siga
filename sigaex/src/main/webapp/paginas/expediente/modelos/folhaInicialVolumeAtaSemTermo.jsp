<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table cellpadding="0" cellspacing="0" width="100%" border="1"
	style="border-color: black; border-spacing: 0px; border-collapse: collapse">
	<tr bgcolor="#C0C0C0"">
		<td colspan="4" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">
			Solicitação de Empenho</td>
		<td width="5%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		<td width="12%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">CND<br />Válida
			até</td>
		<td width="5%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		<td width="12%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">CCN<br />Válida
			até</td>
		<td width="5%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		<td width="12%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">CRF<br />Válida
			até</td>
		<td width="5%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
	</tr>
	<c:forEach var="x" begin="0" end="6">
		<tr>
			<td height="23px" align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		</tr>
	</c:forEach>
	<tr bgcolor="#C0C0C0"">
		<td width="17%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Nota
			de Empenho</td>
		<td width="9%" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Tipo</td>
		<td width="9%" align="center"
			width="9%" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Data</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Valor
			(R$)</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
	</tr>
	<c:forEach var="x" begin="0" end="5">
		<tr>
			<td height="23px" align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		</tr>
	</c:forEach>
	<tr bgcolor="#C0C0C0"">
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Vigência</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Public.
			<br />Fls.</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
	</tr>
	<tr>
		<td height="23px" align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">Ata
			N&ordm;</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		<td  align="center"
			style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
	</tr>
	<c:forEach var="x" begin="0" end="3">
		<tr>
			<td height="23px" align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">Termo
				Aditivo N&ordm;</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
			<td  align="center"
				style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		</tr>
	</c:forEach>


</table>


