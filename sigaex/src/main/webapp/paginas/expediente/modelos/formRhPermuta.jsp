<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Servidor 1" var="servidor1" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="Servidor 2" var="servidor2" />
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="A partir de" var="dataInicio" />
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
		<c:import 
			url="/paginas/expediente/modelos/inc_tit_juizfedDirForoSolicit.jsp" />
	
		<p style="TEXT-INDENT: 2cm" align="justify">Solicito as providências necessárias para que seja realizada
		a <b>remoção por permuta </b> entre os(as) servidores(as) <mod:identificacao pessoa="${requestScope['servidor1_pessoaSel.id']}" nivelHierarquicoMaximoDaLotacao="4" negrito="sim" />
		e <mod:identificacao pessoa="${requestScope['servidor2_pessoaSel.id']}" nivelHierarquicoMaximoDaLotacao="4" negrito="sim" />
		a partir de ${dataInicio}.
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

