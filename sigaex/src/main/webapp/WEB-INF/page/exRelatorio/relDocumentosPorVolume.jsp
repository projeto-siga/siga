<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript" language="Javascript1.1">
	function sbmt() {
		frmRelExpedientes.action = '${url}';
		frmRelExpedientes.submit();
	}
</script>
<c:set var="titulo_pagina" scope="request">
	Relatórios Gerenciais
</c:set>
<c:set var="secaoUsuario" scope="request">
	"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"
</c:set>

<div class="form-row">
	<div class="form-group col-md-2">
		<label for="dtDocString">Data Inicial</label> <input
			class="form-control" type="text" name="dataInicial" id="dataInicial"
			onblur="javascript:verifica_data(this,0);" value="${dataInicial}" />
	</div>
	<div class="form-group col-md-2">
		<label for="dtDocFinalString">Data Final</label> <input
			class="form-control" type="text" name="dataFinal" id="dataFinal"
			onblur="javascript:verifica_data(this,0);" value="${dataFinal}" />
	</div>

	<div class="form-group col-md-4">
		<label><fmt:message key="usuario.lotacao" /></label>
		<siga:selecao propriedade="lotacao" tema="simple" reler="sim"
			modulo="siga" />
	</div>
	<div class="form-group col-md-4">
		<label><fmt:message key="usuario.matricula" /></label>
		<siga:selecao propriedade="usuario" tema="simple"
			paramList="buscarFechadas=true" modulo="siga" />
	</div>
	<div class="form-group col-md-4">
		<input type="submit" value="Pesquisar" class="btn btn-primary" />
	</div>
	<input type="hidden" name="lotacaoId" value="${lotacaoSel.id}" /> <input
		type="hidden" name="siglalotacao" value="${lotacaoSel.sigla}" /> <input
		type="hidden" name="usuarioId" value="${usuarioSel.id}" /> <input
		type="hidden" name="siglaUsuario" value="${usuarioSel.sigla}" /> <input
		type="hidden" name="lotacaoTitular"
		value="${lotaTitular.siglaLotacao}" /> <input type="hidden"
		name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />
	<input type="hidden" name="idTit" value="${titular.id}" /> <input
		type="hidden" name="nomeArquivoRel" value="${nomeArquivoRel}" />
</div>
<c:if
	test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1))}">
	<c:if test="${not empty tamanho and tamanho > 0}">
		<h2 class="mt-3">
			<fmt:message key="documento.encontrados" />
		</h2>
		<c:choose>
			<c:when test="${siga_cliente == 'GOVSP'}">
				<jsp:include page="./listaSP.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include page="./lista.jsp" />
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${empty tamanho or tamanho == 0}">
		<table class="gt-table table table-sm table-hover">
			<thead class="thead-light">
				<tr>
					<th rowspan="1" align="center"><b>Documentos Produzidos:
							999.999.999</b></th>
					<th colspan="1" align="center"><b>Páginas Produzidas:
							999.999.999</b></th>
				</tr>
			</thead>
		</table>
		<table class="gt-table table table-sm table-hover">
			<thead class="thead-dark">
				<tr>
					<th rowspan="1" align="center">Unidade</th>
					<th colspan="1" align="center">Nome do documento</th>
					<th rowspan="1" align="center">Quantidade</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th rowspan="1" align="center">Unidade</th>
					<th colspan="1" align="center">Nome do documento</th>
					<th rowspan="1" align="center">Quantidade</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th rowspan="1" align="center">Unidade</th>
					<th colspan="1" align="center">Nome do documento</th>
					<th rowspan="1" align="center">Quantidade</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th rowspan="1" align="center">Unidade</th>
					<th colspan="1" align="center">Nome do documento</th>
					<th rowspan="1" align="center">Quantidade</th>
				</tr>
			</thead>
			<tr>
				<th rowspan="1" align="center"></th>
				<th colspan="1" align="center"></th>
				<th rowspan="1" align="right"><button type="button"
						onclick="javascript:visualizarRelatorio('${pageContext.request.contextPath}/app/expediente/rel/resultRelDocumentosPorVolume');"
						class="btn btn-primary float-right">Relatório completo</button></th>
			</tr>
		</table>
		<p class="gt-notice-box">
			<b>Não há dados para gerar o relatório!</b>
		</p>
	</c:if>
</c:if>