<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Cargo do subscritor" var="cargoSubscritor"
				opcoes="Magistrado;Servidor (Diretor)" reler="sim" />
			<c:choose>
				<c:when test="${cargoSubscritor == 'Servidor (Diretor)'}">
					<br />
					<br />
					<mod:pessoa titulo="Magistrado" var="magistrado" reler="sim" />
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="E-mail Institucional (alias)" var="email"
				largura="30" obrigatorio="Sim" />@jfrj.jus.br
		</mod:grupo>
		<mod:mensagem titulo="OBS"
			texto="O subscritor do formulário de exclusão deve ser o próprio magistrado. Caso o magistrado 
		já tenha sido removido, o formulário de solicitação de exclusão poderá ter como subscritor 
		o Diretor de Secretaria."
			vermelho="Sim" />

	</mod:entrevista>
	<mod:documento>
		<c:set var="magistrado"
			value="${f:pessoa(requestScope['magistrado_pessoaSel.id'])}" />
		<c:set var="tl" value="8pt" />
		<c:set var="apenasNome" value="Sim" scope="request" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
@body {
	margin-top: 6cm;
	margin-bottom: 0.5cm; 
}
@first-page-body {
	margin-top: 6cm;
	margin-bottom: 0.5cm; 
}
		</style>
		</head>
		<body>
		<!-- FOP -->
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina2.jsp" />
		</td></tr><tr><td></tr></td>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">FORMUL&Aacute;RIO DE EXCLUS&Atilde;O DE ACESSO AO SERVI&Ccedil;O INFOJUD-E-CAC
<br/>SECRETARIA DA RECEITA FEDERAL	
</td>
						</tr>
						<tr><td><p align="right"><b>Formulário N&ordm; ${doc.codigo}</b></p><br/></td></tr>
					</table>
				</td>
			</tr>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->
		<br />
		<mod:letra tamanho="${tl}">




			<table bgcolor="#000000" border="1" cellpadding="5" width="450" align="center">
				<tr>
					<td bgcolor="#FFFFFF" colspan="2" align="left"><b> 1.1 - SOLICITAÇÃO DE
					EXCLUSÃO DE MAGISTRADO </b></td>
				</tr>

				<tr>
					<td bgcolor="#FFFFFF" colspan="2" align="left"><b>1.1 - IDENTIFICAÇÃO DO
					USUÁRIO </b></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="500"><b>NOME COMPLETO</b><br />
					<c:choose>
						<c:when test="${not empty magistrado}">${magistrado.nomePessoa}</c:when>
						<c:otherwise>${doc.subscritor.descricao}</c:otherwise>
					</c:choose></td>

					<td bgcolor="#FFFFFF" width="300"><b>CPF</b><br />
					<c:choose>
						<c:when test="${not empty magistrado}">${f:formatarCPF(magistrado.cpfPessoa)}</c:when>
						<c:otherwise>${f:formatarCPF(doc.subscritor.cpfPessoa)}</c:otherwise>
					</c:choose></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF"><b>MATRICULA</b><br />
					<c:choose>
						<c:when test="${not empty magistrado}">${magistrado.sigla}</c:when>
						<c:otherwise>RJ${doc.subscritor.matricula}</c:otherwise>
					</c:choose></td>
					<td bgcolor="#FFFFFF"><b>SIGLA</b><br />
					<c:choose>
						<c:when test="${not empty magistrado}">${magistrado.siglaPessoa}</c:when>
						<c:otherwise>${doc.subscritor.siglaPessoa}</c:otherwise>
					</c:choose></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF"><b>LOTA&Ccedil;&Atilde;O</b><br />
					<c:choose>
						<c:when test="${not empty magistrado}">${magistrado.lotacao.descricao}</c:when>
						<c:otherwise>${doc.subscritor.lotacao.descricao}</c:otherwise>
					</c:choose></td>


					<td bgcolor="#FFFFFF"><b>E-MAIL INSTITUCIONAL</b><br />
					${email}@jfrj.jus.br</td>
				</tr>

			</table>
			<table bgcolor="#000000" border="1" cellpadding="5" width="450" align="center">




				<tr>
					<td bgcolor="#FFFFFF" width="400"><b>LOCAL/DATA</b><br />
					${doc.dtExtenso}</td>
					<td bgcolor="#FFFFFF" align="center" width="500"><br />
					<br />
					<c:choose>
						<c:when test="${cargoSubscritor == 'Magistrado'}">
					____________________________________________ <br />
					Assinatura do Juiz Solicitante<br />
					${doc.subscritor.descricao}
						</c:when>
						<c:otherwise>
					____________________________________________ <br />
					Assinatura do Solicitante<br />
					${doc.subscritor.descricao}
						</c:otherwise>
					</c:choose></td>
				</tr>
			</table>

		</mod:letra>

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
