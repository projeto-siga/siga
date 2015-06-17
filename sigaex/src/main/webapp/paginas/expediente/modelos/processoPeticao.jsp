<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<mod:modelo>
   <mod:entrevista></mod:entrevista>
      <mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<title></title>
				<style type="text/css">
				   @page {
	               margin-left: 1 cm;
	               margin-right: 2.1 cm;
	               margin-top: 0.5 cm;
	               margin-bottom: 2 cm;
                   }
				</style>
			</head>
		<body>	

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<table border="0" align="center" bgcolor="#ffffff" width="100%">
						<tr>
							<td width="20%" bgcolor="#787878"></td>
							<td width="60%" align="center" style=" font: bold; font-size: 14pt"><b>PETI&Ccedil;&Atilde;O</b></td>
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
				<td bgcolor="#FFFFFF" align="center"><b>ARQUIVADO</b><BR />
				<BR />
				CX______________/________________/20____<br />
				<br />
				<br />
				<br />
				</td>
			</tr>
		</table>




				<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp"/>
				
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

