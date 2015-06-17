<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>

		<mod:selecao var="quantidadeDocumentos"
			titulo="Quantidade de documentos"
			opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25"
			reler="sim" idAjax="quantDependAjax" />
		<mod:grupo depende="quantDependAjax">
			<c:forEach var="i" begin="1" end="${quantidadeDocumentos}">
				<br />
				<mod:texto titulo="Documento ${i}" largura="25" var="str_doc${i}"
					maxcaracteres="50" />
				<br />
			</c:forEach>
		</mod:grupo>
		<mod:grupo titulo="Observação">
				<mod:grupo>
					<mod:editor titulo="" var="obs" />
				</mod:grupo>
		</mod:grupo>
		
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 3cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#FFFFFF">
			<tr>
				<td><c:import
					url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" /></td>
			</tr>
		</table>
		<br>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#FFFFFF">
			<tr>
				<td bgcolor="#FFFFFF" align="center">
				<h2>Remessa de Documentos Físicos</h2>
				</td>
			</tr>
		</table>
		<br>
		<br>
		<br>
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td align="right"><b>${doc.codigo}</b></td>
			</tr>
			<tr>
				<td align="left">DE:&nbsp;${doc.subscritor.lotacao.descricao}</td>
			</tr>
			<tr>
				<td align="left">PARA:&nbsp;${doc.destinatarioString}</td>
			</tr>
		</table>
		</br>

		<table width="100%" cellpadding="3" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
			<tr>
				<td width="10%" align="center"  style="border-collapse: collapse; border-color: black;">Item</td>
				<td width="40%" align="center"  style="border-collapse: collapse; border-color: black;">Documento</td>
				<td width="35%" align="center"  style="border-collapse: collapse; border-color: black;">Assinatura/Matrícula</td>
				<td width="15%" align="center"  style="border-collapse: collapse; border-color: black;">Data</td>
			</tr>
		</table>

		<table width="100%" cellpadding="3" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
			<c:forEach var="i" begin="1" end="${quantidadeDocumentos}">
				<tr>
					<td width="10%" align="center" style="border-collapse: collapse; border-color: black;">${i}</td>
					<td width="40%" align="left" style="border-collapse: collapse; border-color: black;">${requestScope[f:concat('str_doc',i)]}</td>
					<td width="35%" align="left" style="border-collapse: collapse; border-color: black;"></td>
					<td width="15%" align="left" style="border-collapse: collapse; border-color: black;"></td>
				</tr>
			</c:forEach>
		</table>
		<br />
		<table width="100%" border="1" cellpadding="3" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
			<tr>
				<th style="border-collapse: collapse; border-color: black;">Observação</th>
			</tr>
			<tr>
				<td style="border-collapse: collapse; border-color: black;"><c:if test="${empty obs}">&nbsp;</c:if>${obs}</td>
			</tr>
		</table>

		<br />
		<br />
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</body>
		</html>
	</mod:documento>
</mod:modelo>

