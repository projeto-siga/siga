<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>

		<mod:grupo titulo="Lote de Documentos Digitalizados">	
			<mod:lotacao titulo="Lotação" var="lotacao" reler="sim" />		
		</mod:grupo>


		<mod:grupo>
			
			<mod:selecao titulo="Tipo de Documento"
			var="tipoPeticao" opcoes="Petição Inicial;Petição Intercorrente;Expediente;Outros" />
			<mod:selecao var="quantidadeProcessos"
				titulo="Quantidade de processos"
				opcoes="0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40"
				reler="sim" idAjax="quantDependAjax" />
			<mod:grupo depende="quantDependAjax">
				<c:forEach var="i" begin="1" end="${quantidadeProcessos}">
					<mod:grupo largura="50">
						<mod:processo id="str_proc${i}" titulo="Processo ${i}" largura="25" var="str_proc${i}"/>
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#FFFFFF">
			<tr>
				<td><c:import	url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" /></td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#FFFFFF">
			<tr>
				<td align="center" style="font-family: Arial">
				<h2>Lote de Documentos Digitalizados</h2>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" align="left" border="0" cellpadding="0" cellspacing="0"	bgcolor="#FFFFFF">
		<tr>
			<td align="left" >Tipo de Documento:&nbsp;${tipoPeticao}</td>
		</tr>
		</table>
		<br>
		<br>
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td align="right"><b>${doc.codigo}</b></td>
			</tr>
			<tr>
				<td align="right">${doc.dtDocDDMMYY}</td>
			</tr>
			<tr>
				<td align="left">DE:&nbsp;${requestScope['lotacao_lotacaoSel.nomeLotacao']}</td>
			</tr>
			<tr>
				<td align="left">PARA:&nbsp;${doc.destinatarioString}</td>	
			</tr>
		</table>
		</br>
		
		<c:if test="${quantidadeProcessos > '0'}">
       
			<table width="100%" cellpadding="3" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
				<tr>
					<td width="15%" align="center" colspan="1" style="border-collapse: collapse; border-color: black;">ITEM</td>
					<td width="35%" align="center" colspan="1" style="border-collapse: collapse; border-color: black;">Nº PROCESSO</td>
					<td width="15%" align="center" colspan="1" style="border-collapse: collapse; border-color: black;">ITEM</td>
					<td width="35%" align="center" colspan="1" style="border-collapse: collapse; border-color: black;">Nº PROCESSO</td>
				</tr> 
			</table>
			
			<table width="100%" cellpadding="3" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
				<tr>
					<c:if test="${quantidadeProcessos mod 2 == 0}">
						<c:forEach var="i" begin="1" end="${quantidadeProcessos}">
							<td width="15%" align="center" style="border-collapse: collapse; border-color: black;">${i}</td>
							<td width="35%" align="center" style="border-collapse: collapse; border-color: black;">${requestScope[f:concat('str_proc',i)]}</td>
							<c:if test="${i mod 2 == 0}"></tr><tr></c:if>					
						</c:forEach>
					</c:if>
					<c:if test="${quantidadeProcessos mod 2 == 1}">
						<c:forEach var="i" begin="1" end="${quantidadeProcessos + '1'}">
							<td width="15%" align="center" style="border-collapse: collapse; border-color: black;">${i}</td>
							<td width="35%" align="center" style="border-collapse: collapse; border-color: black;">${requestScope[f:concat('str_proc',i)]}</td>
							<c:if test="${i mod 2 == 0}"></tr><tr></c:if>		
						</c:forEach>
					</c:if>
				</tr>
			</table>
		</c:if>
		
		<br/>
		<br/>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</body>
		</html>
	</mod:documento>
</mod:modelo>

