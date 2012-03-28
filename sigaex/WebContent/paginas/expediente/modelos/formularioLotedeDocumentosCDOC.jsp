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
				<h1>Remessa de Documentos Físicos</h1>
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
				<td align="left"><b>DE:&nbsp</b>${doc.subscritor.lotacao.descricao}</td>
			</tr>
			<tr>
				<td align="left"><b>PARA:&nbsp</b>${doc.destinatarioString}</td>
			</tr>
		</table>
		</br>

		<table bgcolor="#000000" width="100%" border="2" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="10%" align="center" colspan="1">Item</td>
				<td bgcolor="#FFFFFF" width="40%" align="center" colspan="1">Documento</td>
				<td bgcolor="#FFFFFF" width="35%" align="center" colspan="1">Assinatura/Matrícula</td>
				<td bgcolor="#FFFFFF" width="15%" align="center" colspan="1">Data</td>
			</tr>
		</table>

		<table bgcolor="#000000" width="100%" border="2" cellpadding="3">
			<c:forEach var="i" begin="1" end="${quantidadeDocumentos}">
				<tr>
					<td bgcolor="#FFFFFF" width="10%" align="center">${i}</td>
					<td bgcolor="#FFFFFF" width="40%" align="left">${requestScope[f:concat('str_doc',i)]}</td>
					<td bgcolor="#FFFFFF" width="35%" align="left" colspan="1"></td>
					<td bgcolor="#FFFFFF" width="15%" align="left" colspan="1"></td>
				</tr>
			</c:forEach>
		</table>
		<br />
		<table bgcolor="#000000" width="100%" border="2" cellpadding="3">
			<tr>
				<th bgcolor="#FFFFFF">Observação</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><c:if test="${empty obs}">&nbsp;</c:if>${obs}</td>
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

