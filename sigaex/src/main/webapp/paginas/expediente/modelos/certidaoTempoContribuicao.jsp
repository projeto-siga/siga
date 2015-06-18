<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
			
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="nome" buscarFechadas="true"/>
			</mod:grupo>
				<mod:caixaverif titulo="Nome não relacionado acima?" var="nomeConstatado" reler="ajax" idAjax="nomeConstatadoAjax"/>
				<mod:grupo depende="nomeConstatadoAjax">
					<c:if test="${nomeConstatado == 'Sim'}">
						<mod:texto titulo="Nome" var="nomeCedido" largura="60" />
					<mod:grupo>
						<mod:texto titulo="Cargo" var="cargo" largura="60" />
					</mod:grupo>
					</c:if>
				</mod:grupo>
				
				<mod:grupo titulo="Período">
					<mod:data titulo="Início" var="periodoInicio"/>
					<mod:data titulo="Fim" var="periodoFim"/>
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
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" width="100%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">CERTIDÃO DE TEMPO DE CONTRIBUIÇÃO</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<br><br>
		
		 <p style="TEXT-INDENT: 2cm" align="justify">
		 <c:choose>
		 	<c:when test="${nomeConstatado == 'Sim'}">
		 	NOME: <b>${nomeCedido}</b>
		 	</c:when>
		 	<c:otherwise>
		 	NOME: <b>${requestScope['nome_pessoaSel.descricao']}</b>
		 	</c:otherwise>
		 </c:choose>
		 </p>
		 <p style="TEXT-INDENT: 2cm" align="justify">
		 <c:choose>
		 	<c:when test="${nomeConstatado == 'Sim'}">
		 	CARGO: <b>${cargo}</b>
		 	</c:when>
		 	<c:otherwise>
			CARGO: <b>${f:pessoa(requestScope['nome_pessoaSel.id']).cargo.descricao}</b>
		 	</c:otherwise>
		 </c:choose>
		 </p>
		 <p style="TEXT-INDENT: 2cm" align="justify">
			PERÍODO:
			<c:if test="${not empty periodoInicio || not empty periodoFim}">
			<b>${periodoInicio} a ${periodoFim}</b>
			</c:if>
		 </p>
		 <p style="TEXT-INDENT: 2cm" align="justify">
		 </p>
		 <br>
		<p>&nbsp;</p>
		
		<p align="center">${doc.dtExtenso}</p>
		
		<p>&nbsp;</p>
		
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp" />
	
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />

		</body>
		</html>
	</mod:documento>
</mod:modelo>
