<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Localidade">
				<mod:lotacao titulo="Origem" var="origem" /><br />
				<mod:lotacao titulo="Destino" var="destino" />
		</mod:grupo>
		<mod:grupo titulo="Material">		
			<mod:selecao titulo="Total de material"
			var="totalDeMaterial" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20" reler="sim" />
			<c:forEach var="i" begin="1" end="${totalDeMaterial}">
				<mod:grupo>
					<mod:texto titulo="Descrição" var="descricao${i}" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nº de Série" var="serie${i}" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:selecao titulo="Quantidade"
						var="quantidade${i}" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30" />
				</mod:grupo>
				<br />
			</c:forEach>
		</mod:grupo>
		
		<mod:grupo titulo="">
				<mod:texto titulo="Observações" var="obs" largura="80" />
		</mod:grupo>
		<br />
		<mod:grupo titulo="">
				<mod:texto titulo="Responsável" var="responsavel" largura="80" /><br />
				<mod:texto titulo="Identidade" var="identidade" largura="10" /><b>(Ex: 99999999-9)</b> 
		</mod:grupo>
		
	</mod:entrevista>
	
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 2cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">SAÍDA DE MATERIAL<br /><br />N&ordm; ${doc.codigo}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<br />

		<table width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" cellpadding="2" cellspacing="1">
			<tr>
				<td style="border-collapse: collapse; border-color: black; " align="center"><b>Fica autorizada a SAÍDA do(s) material(is), abaixo relacionado(s)<br />
					nesta Seção Judiciária.</b></td>
			</tr>
		</table>
		
		<br />
		
		<table width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" cellpadding="2" cellspacing="1">
			<tr>
				<td width="50%" style="border-collapse: collapse; border-color: black; "><b>Origem</b></td>
				<td width="50%" style="border-collapse: collapse; border-color: black; "><b>Destino</b></td>
			</tr>
			<tr>
				<td width="50%" style="border-collapse: collapse; border-color: black; " align="center">${requestScope['origem_lotacaoSel.sigla']}</td>
				<td width="50%" style="border-collapse: collapse; border-color: black; " align="center">${requestScope['destino_lotacaoSel.sigla']}</td>
			</tr>
		</table>

		<br />
		
		<table width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" cellpadding="2" cellspacing="1">
			<tr>
				<td width="50%" style="border-collapse: collapse; border-color: black; " align="center"><b>Descrição do Material</b></td>
				<td width="35%" style="border-collapse: collapse; border-color: black; " align="center"><b>Nº de Série</b></td>
				<td width="15%" style="border-collapse: collapse; border-color: black; " align="center"><b>Quantidade</b></td>
			</tr>
		</table>
		<c:forEach var="i" begin="1" end="${totalDeMaterial}">
				<table width="100%" border="1" cellpadding="2" cellspacing="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
					<tr>
						<td width="50%" style="border-collapse: collapse; border-color: black; " align="center">${requestScope[f:concat('descricao',i)]}</td>
						<td width="35%" style="border-collapse: collapse; border-color: black; " align="center">${requestScope[f:concat('serie',i)]}</td>
						<td width="15%" style="border-collapse: collapse; border-color: black; " align="center">${requestScope[f:concat('quantidade',i)]}</td>
					</tr>
				</table>
			</c:forEach>

		<br />
		
		<table width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" cellpadding="2" cellspacing="1">
			<tr>
				<td style="border-collapse: collapse; border-color: black; " ><b>Observações</b></td>
			</tr>
			<tr>
				<td style="border-collapse: collapse; border-color: black; ">
				<p>&nbsp ${obs}</p>
				</td>
			</tr>
		</table>

		<br />

		<table width="100%"  border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" cellpadding="2" cellspacing="1">
			<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Autorizado por</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="70%" align="center"><c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /></td>
			</tr>
			<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Responsável</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="70%">&nbsp ${responsavel}</td>
			</tr>
			<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Identidade</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="70%">&nbsp ${identidade}</td>
			</tr>
			<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Assinatura</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="70%">&nbsp</td>
			</tr>
		</table>
		<table width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" cellpadding="2" cellspacing="1" >
			<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Controle Portaria</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="35%" align="center"><b>SAÍDA</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="35%" align="center"><b>ENTRADA</b></td>
			</tr>
				<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Conferido por</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="35%">&nbsp</td>
				<td style="border-collapse: collapse; border-color: black; " width="35%">&nbsp</td>
			</tr>
				<tr>
				<td style="border-collapse: collapse; border-color: black; " width="30%" align="center"><b>Data/Hora</b></td>
				<td style="border-collapse: collapse; border-color: black; " width="35%">&nbsp</td>
				<td style="border-collapse: collapse; border-color: black; " width="35%">&nbsp</td>
			</tr>
		</table>
		
		<br />
		
		

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
