<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
			<mod:grupo>
				<mod:selecao titulo="Mês de referência" var="mes" opcoes="Jan;Fev;Mar;Abr;Maio;Jun;Jul;Ago;Set;Out;Nov;Dez" />
				<mod:texto titulo="Quantidade de servidores" var="bilhetes" largura="3" reler="ajax" idAjax="bilhetesAjax" />
			</mod:grupo>
			<mod:grupo depende="bilhetesAjax">
			<c:forEach var="i" begin="1" end="${bilhetes}">
				<mod:mensagem titulo="${i})" />
				<mod:grupo>
					<mod:pessoa titulo="Servidor" var="servidor${i}" />
				</mod:grupo>	
				<mod:grupo>
					<mod:mensagem titulo="Quantidade de Bilhetes Apresentados:"/>
					<mod:texto titulo="Ida:" largura="6" var="ida${i}" />
					<mod:texto titulo="Volta:" largura="6" var="volta${i}" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Obs:" var="obs${i}" largura="70" />
				</mod:grupo>			
			</c:forEach> 
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
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="right" width="100%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.dtExtenso}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
		<p align="justify">APRESENTAÇÃO DE BILHETES DE ÔNIBUS INTERMUNICIPAIS AUXÍLIO-TRANSPORTE</p><br>

		<p>DE: <c:choose>
			<c:when test="${not empty doc.nmLotacao}">
			${doc.nmLotacao}
			</c:when>
			<c:otherwise>${doc.titular.lotacao.descricao}</c:otherwise>
		</c:choose> <br>
		PARA: ${doc.destinatarioString}</p>
	
		 <p style="TEXT-INDENT: 2cm" align="justify">
			Senhor Supervisor,
		 </p>
		 <p style="TEXT-INDENT: 2cm" align="justify">
		 	Encaminho, conforme determinação do Ato Regulamentar nº 04, de 17/07/2000, do Excelentíssimo Senhor Juiz Federal - Diretor do Foro, o quadro
		 	abaixo com os respectivos quantitativos de bilhetes intermunicipais utilizados pelo(s) respectivo(s) servidor(es) no mês de <b>${mes}</b>:
		 </p>
		 <br>
		 <c:forEach var="i" begin="1" end="${bilhetes}">
		 	<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="30%">Matrícula: ${requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.sigla')]}</td>
					<td bgcolor="#FFFFFF" width="70%">Servidor: ${requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.descricao')]}</td>
														
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="30%">Quantidade de Bilhetes Apresentados:</td>
					<td bgcolor="#FFFFFF" width="70%">Obs: ${requestScope[f:concat('obs',i)]}</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="15%">Ida: ${requestScope[f:concat('ida',i)]}</td>
					<td bgcolor="#FFFFFF" width="15%">Volta: ${requestScope[f:concat('volta',i)]}</td>
				</tr>
			</table>
			<br/>
		 </c:forEach>
		 
		 <p style="TEXT-INDENT: 2cm" align="justify">
		 Atenciosamente,
		 </p>
		 
		 <br>
		 
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp" />
			
		<br>
		
		<table width="100%" border="1" cellpadding="2" cellspacing="1">
		<tr>
			<td width="60"valign="top">PARA USO DA SEÇÃO DE BENEFÍCIOS<br>Recebido por:_______ em: __/__/__<br>Cadastrado por:______ em:__/__/__<br>Obs: _____________________________________________________<br>_________________________________________________________<br><br></td>
		</tr>
		</table>
		
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />

		</body>
		</html>
	</mod:documento>
</mod:modelo>
