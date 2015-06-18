<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Substituto" var="substituto" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="Titular" var="titular" />
		</mod:grupo>
		<mod:grupo titulo="Período Solicitado">
			<mod:data titulo="De" var="dataInicio" />
			<mod:data titulo="a" var="dataFim" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Por motivo de" var="motivo" largura="60" />
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
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<br>
					<br>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">SOLICITAÇÃO N&ordm; ${doc.codigo}</p></td>
							<td align="right" width="60%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.dtExtenso}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>
		<br>
		<br>
		
			<c:import url="/paginas/expediente/modelos/inc_tit_juizfedDirForoSolicit.jsp" />
						
	
		<p style="TEXT-INDENT: 2cm" align="justify">Solicito as providências necessárias para que o(a) servidor(a) <mod:identificacao pessoa="${requestScope['substituto_pessoaSel.id']}" nivelHierarquicoMaximoDaLotacao="4" negrito="sim" />
		<b>substitua</b>
		o(a) servidor(a) 
		 <mod:identificacao pessoa="${requestScope['titular_pessoaSel.id']}" funcao="sim" nivelHierarquicoMaximoDaLotacao="4" negrito="sim" />
		<c:choose>
			<c:when test="${(dataInicio == dataFim) or (empty dataFim)}">
				no dia ${dataInicio},
			</c:when>
				<c:otherwise>
				no período de ${dataInicio} a ${dataFim},
				</c:otherwise>
		</c:choose>
		por motivo de ${motivo}.
		</p>
		<br>
		<br>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</body>
		</html>
	</mod:documento>
</mod:modelo>

