<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
	function sbmt() {
		frmRelExpedientes.action = '${url}';
		frmRelExpedientes.submit();
	}
</script>

<link rel="stylesheet"
	href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css"
	media="screen, projection" />
<link rel="stylesheet" href="/siga/css/siga.multiploselect.css"
	type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2.css"
	type="text/css" media="screen, projection" />
<link rel="stylesheet"
	href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
	media="screen, projection" />


<c:set var="titulo_pagina" scope="request"> Relatório de Saída de Documentos por Setor</c:set>
<input type="hidden" name="secaoUsuario" id="secaoUsuario"
	value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />

<div class="container-fluid">
	<div class="card bg-light mb-3">

		<div class="card-body">
			<div class="row">
				<div class="col-md-2">
					<label for="tipoSaida">Exportar para</label> <select id="tipoSaida"
						name="idTipoSaida" value="${idTipoSaida}" class="form-control">
						<option value="1">PDF</option>
						<option value="2">EXCEL</option>
						<option value="3">CSV</option>
					</select>
				</div>

				<div class="col-md-4">
					<label for="tipoFormaDoc">Tipo Documental</label> <select
						id="tipoFormaDoc" name="idTipoFormaDoc" value="${idTipoFormaDoc}"
						class="form-control">
						<c:forEach var="tipo" items="${listaTiposFormaDoc}">
							<option value="${tipo.idTipoFormaDoc}"
								${tipo.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>${tipo.descTipoFormaDoc}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-2">
					<label>Data Inicial</label> <input type="text" name="dataInicial" 
						id="dataInicial"
						onblur="javascript:verifica_data(this, true);comparaData(dataInicial,dataFinal);"
						theme="simple" maxlength="10" class="form-control" />
				</div>
				<div class="col-sm-2">
					<label>Data Final</label> <input type="text" name="dataFinal"
						id="dataFinal"
						onblur="javascript:verifica_data(this,true);comparaData(dataInicial,dataFinal);"
						theme="simple" maxlength="10" class="form-control" />
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-8">
					<div class="form-group">
						<label for="lotacao">Setores</label> <input type="hidden"
							name="idLotacaoPesquisa" value="${idLotacaoPesquisa}"
							id="inputHiddenLotacoesSelecionadas" /> <select id="lotacao"
							name="setoresSelecionados"
							class="form-control  siga-multiploselect  js-siga-multiploselect--lotacao">
							<c:forEach items="${listaSetoresSubordinados}" var="itemL">
								<option value="${itemL.idLotacao}">${itemL.nomeLotacao}</option>
							</c:forEach>
						</select>
					</div>
				</div>

			</div>

			<div class="row">
				<div class="col-md-8">
					<div class="form-group">
						<label for="classificacao">Assuntos</label> <input type="hidden"
							name="idClassificacaoPesquisa" value="${idClassificacaoPesquisa}"
							id="inputHiddenClassificacoesSelecionadas" /> <select
							id="classificacao" name="assuntos"
							class="form-control  siga-multiploselect  js-siga-multiploselect--classificacao">
							<c:forEach items="${listaAssuntos}" var="itemC">
								<option value="${itemC.idClassificacao}">${itemC.codificacao}-${itemC.descrClassificacao}</option>
							</c:forEach>
						</select>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<input type="hidden" name="lotacao" id="lotacao"
	value="${lotacaoDestinatarioSel.id}" />
<input type="hidden" name="siglalotacao" id="siglalotacao"
	value="${lotacaoDestinatarioSel.sigla}" />
<input type="hidden" name="lotacaoTitular" id="lotacaoTitular"
	value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="idTit" id="idTit" value="${titular.id}" />



<script type="text/javascript"
	src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>
<script type="text/javascript"
	src="/siga/javascript/siga.multiploselect.js"></script>
<script type="text/javascript"
	src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript"
	src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript"
	src="../../../javascript/lotacao.combo-multiselect.js"></script>
<script type="text/javascript"
	src="../../../javascript/classificacao.combo-multiselect.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>


