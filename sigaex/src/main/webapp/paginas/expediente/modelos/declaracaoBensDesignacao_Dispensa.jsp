<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<mod:modelo>
	<mod:entrevista>

		<mod:selecao titulo="Selecione o motivo da declaração" var="motivo"
			opcoes="Selecione;Exoneração/Dispensa;Nomeação/Designação"
			reler="sim" />
		<br />

		<c:choose>
			<c:when test="${motivo == 'Exoneração/Dispensa'}">
				<br />
				<mod:texto titulo="Cargo em comissão/função comissionada de"
					var="cargoFuncao" largura="40" obrigatorio="Sim" maxcaracteres="50" />
				<br />
				<mod:data titulo="a partir de" var="data" obrigatorio="Sim" />
				<br />
				<mod:lotacao
					titulo="Lotação onde exerceu o cargo em comissão/função comissionada"
					var="lotacao"></mod:lotacao>
				<br />
			</c:when>
			<c:when test="${motivo == 'Nomeação/Designação'}">
				<br />
				<mod:texto titulo="Cargo em comissão/função comissionada de"
					var="cargoFuncao" largura="50" obrigatorio="Sim" maxcaracteres="50" />
				<br />
				<mod:data titulo="a partir de" var="data" obrigatorio="Sim" />
				<br />
				<mod:lotacao
					titulo="Lotação onde exercerá o cargo em comissão/função comissionada"
					var="lotacao"></mod:lotacao>
				<br />
			</c:when>
			<c:otherwise></c:otherwise>
		</c:choose>


		<c:if test="${motivo ne 'Selecione'}">
			<mod:radio titulo="Declaro não possuir bens." var="tipoFormulario"
				valor="1" reler="sim" marcado="Sim" />
			<mod:radio titulo="Declaro que possuo os seguintes bens:"
				var="tipoFormulario" reler="sim" valor="2" gerarHidden="Não" />
			<c:set var="valorTipoDeForm" value="${tipoFormulario}" />
			<c:if test="${empty valorTipoDeForm}">
				<c:set var="valorTipoDeForm" value="${param['tipoFormulario']}" />
			</c:if>

			<c:if test="${valorTipoDeForm == 2}">
				<mod:texto titulo="Declaração de bens atualizada" var="bens"
					largura="40" maxcaracteres="100" obrigatorio="Sim" />
			</c:if>

			<br />
			<mod:texto titulo="Atual/Atuais fonte(s) de renda" var="fonte"
				largura="40" maxcaracteres="100" obrigatorio="Sim" />
		</c:if>
	</mod:entrevista>

	<mod:documento>

		<c:set var="tl" value="10pt" />
		<c:set var="bens" value="${fn:trim(bens)}" />
		<c:set var="fonte" value="${fn:trim(fonte)}" />

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
							<td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >DECLARA&Ccedil;&Atilde;O DE BENS</p></mod:letra></td>
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
			${doc.subscritor.descricao}, matrícula nº ${doc.subscritor.sigla},
			${doc.subscritor.cargo.nomeCargo}, lotado(a) no(a)
			${requestScope['lotacao_lotacaoSel.descricao']}, tendo em vista a <c:choose>
				<c:when test="${motivo == 'Nomeação/Designação'}">
			nomeação/designação 
			</c:when>
				<c:when test="${motivo == 'Exoneração/Dispensa'}">
			exoneração/dispensa  
			</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>do(a) cargo em comissão/função comissionada de ${cargoFuncao}, a
			partir de ${data}, declara que <c:choose>
				<c:when test="${not empty bens}">
				possui os seguintes bens: ${bens}.
				</c:when>
				<c:otherwise>
				não possui bens.
				</c:otherwise>
			</c:choose> <c:if test="${not empty fonte}">
			Declara, ainda, que tem como atual/atuais fonte(s) de renda:
			${requestScope['fonte']}.
			</c:if></p>

			<p align="center">${doc.dtExtenso}</p>

			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />

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