<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>

		<mod:grupo>
			<mod:selecao var="contadorDePessoas"
				titulo="Quantidade de Magistrados, servidores e estagiários"
				opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40"
				reler="sim" idAjax="contDependAjax" />
			<mod:grupo depende="contDependAjax">
				<c:forEach var="i" begin="1" end="${contadorDePessoas}">
					<mod:grupo>
						<mod:pessoa titulo="Solicitante ${i}" var="solicitante${i}" />
					</mod:grupo>
					<mod:grupo>
						<mod:texto var="email${i}" largura="30" maxcaracteres="50"
							obrigatorio="Sim" titulo="E-mail Institucional (alias)" />@jfrj.jus.br
						<mod:texto obrigatorio="Sim" var="cpf${i}" titulo="CPF"
							largura="11" maxcaracteres="14" />
					</mod:grupo>
					<mod:grupo>
						<mod:texto var="telefone${i}" largura="12" maxcaracteres="12"
							obrigatorio="Sim" titulo="Telefone" />
						<mod:data titulo="Data agendada com a CEF" var="dataAgendada${i}"
							obrigatorio="Não" />
					</mod:grupo>
					<hr style="color: #FFFFFF;" />
				</c:forEach>
			</mod:grupo>
			<mod:mensagem titulo="<b>Observações</b>:" texto="" vermelho="Sim"></mod:mensagem>
			<br />
			<mod:mensagem titulo=""
				texto="1. Preencha o destinatário com <b>SG-SEI</b> e, após finalizar, transfira para a <b>SG-SEI</b>;"
				vermelho="Sim"></mod:mensagem>
			<br />
			<mod:mensagem titulo=""
				texto="2. O campo 'Data agendada com a CEF' é opcional."
				vermelho="Sim"></mod:mensagem>
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
	margin-bottom: 1cm;
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
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		<p align="right">
		<b>MEMORANDO N&ordm; ${doc.codigo}</b><br/>
		<b>${doc.localidadeString}-RJ, ${doc.dtDocDDMMYYYY}</b></p>
		<br />
		<br />
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td align="left">DE: ${doc.subscritor.lotacao.descricao}</td>
			</tr>
			<tr>
				<td align="left">PARA: ${doc.destinatarioString}</td>
			</tr>
		</table>

		<br />
		<p style="text-align: left; text-indent: 2cm">Solicito a emissão
		de Certificados Digitais Cert-JUS Institucional aos juízes, servidores e
		estagiários da listagem anexa.</p>
		<p style="text-align: left; text-indent: 2cm">Atenciosamente,</p>
		<br />
		<br />
		<br />
		<br />
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<br />
		<br />
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#FFFFFF">
			<tr>
				<td align="center">
				<h3>LISTAGEM</h3>
				</td>
			</tr>
		</table>
		<br />
		<br />
		<c:if test="${contadorDePessoas > '0'}">
			<c:forEach var="i" begin="1" end="${contadorDePessoas}">
				<c:set var="pes"
					value="${f:pessoa(requestScope[f:concat(f:concat('solicitante',i),'_pessoaSel.id')])}" />
				<c:set var="e" value="${requestScope[(f:concat('email',i))]}" />
				<c:set var="t" value="${requestScope[(f:concat('telefone',i))]}" />
				<c:set var="d" value="${requestScope[f:concat('dataAgendada',i)]}" />
				<c:set var="c" value="${requestScope[f:concat('cpf',i)]}" />

				<table width="100%" border="1" cellpadding="4">
					<tr>
						<td width="50%" align="left" colspan="1">Nome:
						${pes.descricao}</td>
						<td width="50%" align="left" colspan="1">Matrícula:
						RJ${pes.matricula}</td>
					</tr>
					<tr>
						<td width="50%" align="left" colspan="1">Lotação:
						${pes.lotacao.descricao}</td>
						<td width="50%" align="left" colspan="1">E-mail: ${e}@jfrj.jus.br</td>
					</tr>
					<tr>
						<td width="50%" align="left" colspan="1">Cargo:
						${pes.cargo.nomeCargo}</td>
						<td width="50%" align="left" colspan="1">Sigla:
						${pes.siglaPessoa}@JFRJ</td>
					</tr>
					<tr>
						<td width="50%" align="left" colspan="1">CPF:
						${f:formatarCPF(c)}</td>
						<td width="50%" align="left" colspan="1">Telefone: ${t}</td>
					</tr>
					<tr>
						<td width="100%" align=left colspan="2">Data agendada com a
						CEF: ${d}</td>
					</tr>
				</table>
				<br />
				<c:if test="${i mod 4 == '0' && i < contadorDePessoas}">
					<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
					<br />
				</c:if>
			</c:forEach>
		</c:if>

		<br />
		<br />
		</body>
		</html>
	</mod:documento>
</mod:modelo>

