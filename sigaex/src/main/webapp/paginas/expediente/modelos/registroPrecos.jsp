<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<mod:modelo>
	<mod:entrevista>
	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				size:landscape;
				margin-left: 1cm;
				margin-right: 1cm;
				margin-top: 1cm;
				margin-bottom: 1cm;	}		
		</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<br>
		<p style="font-size:10" align="center">REGISTRO DE PREÇOS</p>
		<br>
		<table width="100%" border="1" bordercolor="#000000"  bgcolor="#000000">
			<tr align="center">
				<td rowspan="2" bgcolor="#FFFFFF">Item</td>
				<td rowspan="2" width="15%" bgcolor="#FFFFFF">Material</td>
				<td rowspan="2" bgcolor="#FFFFFF">Contratada</td>
				<td rowspan="2" bgcolor="#FFFFFF">Contabiliza<br>aditamento?(SIM<br>OU NÃO)</td>
				<td colspan="2" bgcolor="#FFFFFF">Quantidade</td>
				<td rowspan="2" bgcolor="#FFFFFF">Total do RP</td>
				<td colspan="9" width="40%" bgcolor="#FFFFFF">PEDIDOS</td>
				<td rowspan="2" bgcolor="#FFFFFF">Saldo</td>	
			</tr>
			<tr align="center">
				<td bgcolor="#FFFFFF">Inicial</td>
				<td bgcolor="#FFFFFF">25%</td>
				<td bgcolor="#FFFFFF">1º</td>
				<td bgcolor="#FFFFFF">2º</td>
				<td bgcolor="#FFFFFF">3º</td>
				<td bgcolor="#FFFFFF">4º</td>
				<td bgcolor="#FFFFFF">5º</td>
				<td bgcolor="#FFFFFF">6º</td>
				<td bgcolor="#FFFFFF">7º</td>
				<td bgcolor="#FFFFFF">8º</td>
				<td bgcolor="#FFFFFF">9º</td>	
			</tr>
			<c:forEach var="x" begin="0" end="20">
				<tr>
					<c:forEach var="x" begin="0" end="16">
						<td bgcolor="#FFFFFF">&nbsp</td>
					</c:forEach>	
				</tr>    
			</c:forEach>	
		</table>
		<br><br><br><br>
	
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		</body>
		</html>
	</mod:documento>
</mod:modelo>
