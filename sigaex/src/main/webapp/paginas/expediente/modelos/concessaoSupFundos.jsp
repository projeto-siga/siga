<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
				<mod:selecao titulo="Quantidade de suprimentos a informar:" opcoes="1;2;3;4;5;6;7;8;9;10" var="quantSuprim" reler="ajax"  idAjax="quantSuprimAjax" />
				<mod:selecao titulo="Mês de Referência:" var="mesReferencia" opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
				<mod:texto titulo="Ano:" var="anoReferencia" largura="4" />
		</mod:grupo>
			<mod:grupo depende="quantSuprimAjax">
				<c:forEach var="i" begin="1" end="${quantSuprim}">
					<mod:texto titulo="Número do Processo:" var="numproc${i}" largura="6" />	
						<mod:grupo>
							<mod:pessoa titulo="Nome" var="servidor${i}" />
						</mod:grupo>
						<mod:grupo>
							<mod:data titulo="Data do empenho" var="dataEmpenho${i}" />
						</mod:grupo>
						<mod:grupo>
							<mod:selecao titulo="Tipo de Suprimento:" var="tipoSuprimento${i}" opcoes="L;D" />
							<mod:monetario titulo="Valor(R$)" var="valorEmpenho${i}" formataNum="sim" extensoNum="sim" largura="15"/>
						</mod:grupo>
						<hr>
				</c:forEach>
			</mod:grupo>
	</mod:entrevista>
	<mod:documento>
	<!-- <c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="11pt"/></c:if> -->
	<!-- <c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="9pt"/></c:if> -->
	<!-- <c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="13pt"/></c:if> -->
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<c:set var="tl" value="8pt" />
		<mod:letra tamanho="${tl}">
		<p>DE: <c:choose>
			<c:when test="${not empty doc.nmLotacao}">
			${doc.nmLotacao}
			</c:when>
			<c:otherwise>${doc.titular.lotacao.descricao}</c:otherwise>
		</c:choose> <br>
		PARA: ${doc.destinatarioString}</p>
	
		<p align="center"> SOLICITAÇÃO DE PUBLICAÇÃO BOLETIM INTERNO<br/>
		Concessão de Suprimento de Fundos
		</p>
		<!-- INICIO CORPO -->
		<p><span style="font-weight: bold">SUPRIMENTO DE FUNDOS - ${mesReferencia} de ${anoReferencia}</span></p>
		<br>
		<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="10%" align="center"><b>N&ordm;<br/>Processo</b></td>
				<td bgcolor="#FFFFFF" width="26%" align="center"><b>Nome</b></td>
				<td bgcolor="#FFFFFF" width="22%" align="center"><b>Cargo/Função</b></td>
				<td bgcolor="#FFFFFF" width="11%" align="center"><b>Lotação</b></td>
				<td bgcolor="#FFFFFF" width="14%" align="center"><b>Data do Empenho</b></td>
				<td bgcolor="#FFFFFF" width="13%" align="center"><b>Valor(R$)</b></td>
				<td bgcolor="#FFFFFF" width="7%" align="center"><b>Tipo</b></td>		
			</tr>
		
		<c:forEach var="i" begin="1" end="${quantSuprim}">
			<tr>
					<td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('numproc',i)]}</td>
					<td bgcolor="#FFFFFF" width="26%" align="center">${requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.descricao')]}</td>
					<td bgcolor="#FFFFFF" width="22%" align="center">${f:cargoPessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')])}<c:set var="funcao" value="${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')]).funcaoConfianca.descricao}"/><c:choose><c:when test="${not empty funcao}"> - ${funcao}</c:when><c:otherwise> - SEM FUNÇÃO COMISSIONADA</c:otherwise></c:choose></td>
					<td bgcolor="#FFFFFF" width="11%" align="center">${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')]).lotacao.sigla}</td>
					<td bgcolor="#FFFFFF" width="14%" align="center">${requestScope[f:concat('dataEmpenho',i)]}</td>
					<td bgcolor="#FFFFFF" width="13%" align="center">${requestScope[f:concat('valorEmpenho',i)]}</td>
					<td bgcolor="#FFFFFF" width="7%" align="center">${requestScope[f:concat('tipoSuprimento',i)]}</td>	
			</tr>
		</c:forEach>
		</table>
		<p><b>D - despesas miúdas, de pronto pagamento</b><br/>
		<b>L - gênero alimentícios</b>
		<!-- FIM CORPO -->
		</p>
		<br>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		</mod:letra>
		
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

