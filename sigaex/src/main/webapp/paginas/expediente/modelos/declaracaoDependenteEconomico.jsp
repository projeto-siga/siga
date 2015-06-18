<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<!-- este modelo trata de
DECLARACAO DE DEPENDENTE ECONOMICO-->
<mod:modelo>
	<mod:entrevista>
				<mod:grupo>
					<mod:texto titulo="Nome do dependente" var="dependente" largura="50" />
				</mod:grupo>
	</mod:entrevista>
	
	<mod:documento>
	<c:set var="tl" value="11pt"/>
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
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF">
			<tr><td>
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
			</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >DECLARA&Ccedil;&Atilde;O</p></mod:letra></td>
						</tr>
						<tr>
							<td><br/><br/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		
		<mod:letra tamanho="${tl}">
			<p style="TEXT-INDENT: 2cm" align="justify">
			Declaro, a fim de fazer prova junto ao Plano de Saúde, 
			que ${dependente} permanece na condição de solteiro(a), estudante e vive sob minha dependência econômica.
			</p>
			<br/><br/><br/>
		
		<p align="center">${doc.dtExtenso}</p>
		<br/>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		<p align="center">Matrícula: RJ${doc.subscritor.matricula}</p>
		</mod:letra>
		
			<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
	
	<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->


		</body>
		</html>
	</mod:documento>
</mod:modelo>