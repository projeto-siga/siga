<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:grupo>
				<mod:lotacao titulo="Lotação" var="lotacao" reler="sim" />
			</mod:grupo>

			<input type="hidden" id="atualizandoCaixaVerif"
				name="atualizandoCaixaVerif" />
			<mod:caixaverif var="subLotacoes" reler="sim" marcado="Sim"
				titulo="Incluir Sublotações"
				onclique="javascript:document.getElementById('atualizandoCaixaVerif').value=1;" />
			<c:if test="${requestScope['subLotacoes'] eq 'Sim'}">
				<c:set var="testarSubLotacoes" value="true" />
			</c:if>
			<c:if test="${requestScope['subLotacoes'] != 'Sim'}">
				<c:set var="testarSubLotacoes" value="false" />
			</c:if>

		</mod:grupo>

		<%-- O bloco abaixo carrega do BD as pessoas de um lotação, apenas quando a lotação muda --%>
		<mod:ler var="siglaLotacaoAnterior" />
		<c:choose>
			<c:when
				test="${(requestScope['lotacao_lotacaoSel.sigla'] != siglaLotacaoAnterior) or (param['atualizandoCaixaVerif']) == 1}">
				<c:set var="pessoas"
					value="${f:pessoasPorLotacao(param['lotacao_lotacaoSel.id'], testarSubLotacoes)}" />
				<c:set var="i" value="${0}" />
				<c:forEach var="pes" items="${pessoas}">
					<c:set var="i" value="${i+1}" />
					<mod:oculto var="matricula${i}" valor="${pes.matricula}" />
					<mod:oculto var="nome${i}" valor="${pes.descricao}" />
					<mod:oculto var="cargo${i}" valor="${pes.cargo.descricao}" />
				</c:forEach>
				<mod:oculto var="contadorDePessoas" valor="${i}" />
				<mod:oculto var="siglaLotacaoAnterior"
					valor="${requestScope['lotacao_lotacaoSel.sigla']}" />
			</c:when>
			<c:otherwise>
				<mod:oculto var="contadorDePessoas" />
				<mod:oculto var="siglaLotacaoAnterior"
					valor="${siglaLotacaoAnterior}" />
				<c:forEach var="i" begin="1" end="${contadorDePessoas}">
					<mod:oculto var="matricula${i}" />
					<mod:oculto var="nome${i}" />
					<mod:oculto var="cargo${i}" />
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<c:if test="${(not empty requestScope['lotacao_lotacaoSel.id'])}">
			<mod:grupo>
				<mod:mensagem titulo="Atenção"
					texto="Selecione somente os servidores que necessitam de certificado digital da Certisign." vermelho="Sim" />
			</mod:grupo>
			<br/>
				
			<table border="0" cellspacing="0" width="80%">
				<tr>
					<td width="05%" align="center"></td>
					<td width="40%" align="left"><b>Nome</b></td>
					<td width="10%" align="center"><b>Matrícula</b></td>
					<td width="45%" align="left"><b>Cargo</b></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" width="80%">
				<c:forEach var="i" begin="1" end="${contadorDePessoas}">
					<c:set var="pes_descricao"
						value="${requestScope[f:concat('nome',i)]}" />
					<c:set var="pes_matricula"
						value="${requestScope[f:concat('matricula',i)]}" />
					<c:set var="pes_cargo" value="${requestScope[f:concat('cargo',i)]}" />
					<tr>
						<td width="05%" align="center"><mod:caixaverif
							var="selecionado${pes_matricula}" marcado="Sim" /></td>
						<td width="40%" align="left">${pes_descricao}</td>
						<td width="10%" align="center">${pes_matricula}</td>
						<td width="45%" align="left">${pes_cargo}</td>
					</tr>
				</c:forEach>
			</table>
			<br />
			<%--	<c:if test="${not empty requestScope['lotacao_lotacaoSel.sigla']}"> --%>
			<mod:grupo>
				<mod:mensagem titulo="Total de pessoas por Lotação :" />
				<b>${contadorDePessoas}</b>
			</mod:grupo>
		</c:if>
		<mod:grupo>
			<mod:selecao titulo="Deseja incluir mais servidores?"
				var="servIncluidos" opcoes="Não;Sim" reler="ajax" idAjax="servIncl" />
		</mod:grupo>
		<mod:grupo depende="servIncl">
			<c:if test="${servIncluidos eq 'Sim'}">
				<mod:grupo>
					<mod:selecao titulo="Nº de Servidor(es) Incluido(s) "
						var="numServIncluidos"
						opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30"
						reler="ajax" idAjax="numServIncl" />
				</mod:grupo>
				<mod:grupo depende="numServIncl">
					<c:forEach var="i" begin="1" end="${numServIncluidos}">
						<mod:grupo>
							<mod:pessoa titulo="Servidor" var="servidorIncluido${i}"
								reler="sim" />
								&nbsp;-&nbsp; ${f:pessoa(requestScope[f:concat(f:concat('servidorIncluido',i),'_pessoaSel.id')]).cargo.descricao}								
							</mod:grupo>
					</c:forEach>
				</mod:grupo>
			</c:if>
		</mod:grupo>
		<%--</c:if> --%>
	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 3cm;
	margin-top: 3cm;
	margin-bottom: 3cm;
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
		<p align="right"><b>MEMORANDO N&ordm; ${doc.codigo}</b><br />
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
		de Certificado(s) Digital(is) da CertiSign ao(s) servidor(es) da
		listagem anexa.</p>
		<br />
		<br />
		<br />
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
			<table width="100%" align="center" border="1" cellpadding="2"
				cellspacing="1" bgcolor="#000000">
				<tr>
					<td width="46%" align="left"><b>Nome</b></td>
					<td width="13%" align="center"><b>Matrícula</b></td>
					<td width="42%" align="left"><b>Cargo</b></td>
				</tr>
			</table>
			<table width="100%" align="center" border="1" cellpadding="2"
				cellspacing="1" bgcolor="#000000">
				<c:forEach var="i" begin="1" end="${contadorDePessoas}">
					<c:if
						test="${requestScope[f:concat('selecionado',requestScope[f:concat('matricula',i)])] == 'Sim'}">
						<c:set var="pes_descricao"
							value="${requestScope[f:concat('nome',i)]}" />
						<c:set var="pes_matricula"
							value="${requestScope[f:concat('matricula',i)]}" />
						<c:set var="pes_cargo"
							value="${requestScope[f:concat('cargo',i)]}" />
						<tr>
							<td width="46%" align="left">${f:maiusculasEMinusculas(pes_descricao)}</td>
							<td width="13%" align="center">${pes_matricula}</td>
							<td width="42%" align="left">${f:maiusculasEMinusculas(pes_cargo)}</td>
						</tr>
					</c:if>
				</c:forEach>
				<c:if test="${numServIncluidos > '0'}">
					<c:forEach var="i" begin="1" end="${numServIncluidos}">
						<c:set var="pes"
							value="${f:pessoa(requestScope[f:concat(f:concat('servidorIncluido',i),'_pessoaSel.id')])}" />
						<tr>
							<td width="46%" align="left">${f:maiusculasEMinusculas(pes.descricao)}</td>
							<td width="13%" align="center">${pes.matricula}</td>
							<td width="42%" align="left">${f:maiusculasEMinusculas(pes.cargo.descricao)}</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
		</c:if>

		<br />
		<br />
		</body>
		</html>
	</mod:documento>
</mod:modelo>
