<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:texto titulo="Nome do Projeto" var="nomeDoProjeto" largura="50" />
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 2 cm;
	margin-top: 0.5cm;
	margin-bottom: 2 cm;
}
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="11pt"></c:set>
		</c:if>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<table border="0" align="center" bgcolor="#ffffff" width="100%">
						<tr>
							<td width="20%" bgcolor="#787878"></td>
							<td width="60%" align="center" style=" font: bold; font-size: 14pt"><b>PROCESSO&nbsp;ADMINISTRATIVO</b></td>
							<td width="20%" bgcolor="#787878"></td>
						</tr>
					</table>
				</td>
			<tr>
			<tr>
				<td>
					<br><br/><c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td cellpadding="5">
					&nbsp;
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<%--      <!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		<br/><br/>
		FIM CABECALHO --> --%>

		<br />
		<%-- Não serão necessários os campos Apenso e volume abaixo, por isto estão comentados  --%>
		<table align="center" width="60%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td width="30%" bgcolor="#FFFFFF" align="center"><br />
				<b>Processo Nº</b><br />
				<br />
				</td>
				<%-- 		<td width="15%" bgcolor="#FFFFFF" align="center"><b>Apenso</b></td> 
				<td width="15%" bgcolor="#FFFFFF" align="center"><b>Volume</b></td> --%>
			</tr>

			<tr>
				<td bgcolor="#FFFFFF" align="center"><br />
				${doc.codigo}<br />
				<br />
				</td>
			</tr>
		</table>

		<c:if test="${doc.exTipoDocumento.idTpDoc == 2}">
			<br>
			<table align="center" width="60%" border="1" cellspacing="1"
				bgcolor="#000000">
				<tr>
					<td width="30%" bgcolor="#FFFFFF" align="center"><br />
					<b>Nº no Sistema Antigo</b><br />
					<br />
					</td>
					<%-- 		<td width="15%" bgcolor="#FFFFFF" align="center"><b>Apenso</b></td> 
				<td width="15%" bgcolor="#FFFFFF" align="center"><b>Volume</b></td> --%>
				</tr>

				<tr>
					<td bgcolor="#FFFFFF" align="center"><br />
					${doc.numAntigoDoc}<br />
					<br />
					</td>
				</tr>
		</c:if>
		</table>

		<br>
		<br>

		<table align="center" width="50%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" align="center"><br />
				<b>Data de abertura</b><br />
				<br />
				</td>
				<td bgcolor="#FFFFFF" align="center">${doc.dtDocDDMMYYYY}</td>
			</tr>
		</table>

		<br>
		<br>

		<table align="center" width="85%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" align="center"><b>Nome do Projeto</b></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" align="center"><br />
				${nomeDoProjeto}<br />
				<br />
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" align="center"><b>OBJETO</b></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" align="center"><br />
				${doc.descrDocumento}<br />
				<br />
				</td>
			</tr>
		</table>

		<br>
		<br>

		<table align="center" width="60%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" align="left">
				Findo este processo, deve-se encaminhá-lo ao Arquivo para guarda permanente, 
				não cabendo eliminação.
				<br />
				<br />
				</td>
			</tr>
		</table>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->



		<c:if
			test="${not doc.eletronico and doc.exTipoDocumento.idTpDoc == 1}">
			<c:if test="${doc.exFormaDocumento.idFormaDoc=='57'}">
				<div style="PAGE-BREAK-AFTER: always" /><c:import
					url="/paginas/expediente/modelos/folhaInicialVolumeEof.jsp" />
			</c:if>
		</c:if>

		</body>
		</html>

	</mod:documento>

	<%--
	<mod:resumo visivel="false">
		<mod:topico descricao="Dt. Abertura" valor="${doc.dtDocDDMMYYYY}" />
		<mod:topico descricao="Dt. Término" valor="02/02/2002" />
		<mod:topico descricao="Total" valor="R$1000,00" />
		<mod:topico descricao="Empresa" valor="fulano de tal" />
	</mod:resumo>
	
--%>
	<%-- 	
	<mod:finalizacao>
		{Memorando Finalizado!}
		<c:set var="f" value="${f:criarWorkflow('Exoneracao', doc)}" />
	</mod:finalizacao>
--%>
</mod:modelo>

