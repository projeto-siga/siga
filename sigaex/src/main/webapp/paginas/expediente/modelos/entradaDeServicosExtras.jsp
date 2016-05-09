<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Período">
		<mod:grupo titulo="">
			<mod:data titulo="Data: início" var="data_ini" />
			<mod:data titulo="fim" var="data_fim" />
			<mod:texto titulo=" - Hora: início" var="hora_ini" largura="5"
				maxcaracteres="5" />
			<mod:texto titulo="fim" var="hora_fim" largura="5" maxcaracteres="5" />
		</mod:grupo>
			<mod:texto titulo="Observações" var="obs" largura="60" />
		</mod:grupo>
		<mod:grupo titulo="Serviço">
			<mod:texto titulo="Local de realização" var="local" largura="60" />
			<mod:memo titulo="Descrição" var="desc" colunas="80" />
			<mod:memo titulo="Relação de pessoal" var="pessoal" colunas="80"
				linhas="10" />
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
							<td align="right"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">ENTRADA DE SERVIÇOS EXTRAS N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="right"><p>${doc.dtExtenso}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<h3>1 - PERÍODO</h3>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF">Data: de ${data_ini} à ${data_fim} -
				Hora: de ${hora_ini} à ${hora_fim}<c:if
					test="${not empty obs}">
					<br />Obs: ${obs}</c:if></td>
			</tr>
		</table>

		<h3>2 - LOCAL DE REALIZAÇÃO DO SERVIÇO</h3>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF">
				<p>${local}</p>
				</td>
			</tr>
		</table>

		<h3>3 - DESCRIÇÃO DO SERVIÇO</h3>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF">
				<p>${desc}</p>
				</td>
			</tr>
		</table>

		<h3>4 - RELAÇÃO DE PESSOAL</h3>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF">
				<p><siga:fixcrlf>${pessoal}</siga:fixcrlf></p>
				</td>
			</tr>
		</table>

		<p align="center"><br />
		<br />
		</p>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />

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
