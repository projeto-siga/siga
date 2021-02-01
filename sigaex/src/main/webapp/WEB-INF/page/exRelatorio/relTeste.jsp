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
<
<style>
fieldset.group {
	margin: 0;
	padding: 0;
	margin-bottom: 1.25em;
	padding: .125em;
}

fieldset.group legend {
	margin: 0;
	padding: 0;
	font-weight: bold;
	margin-left: 20px;
	font-size: 100%;
	color: black;
}
ul {
    columns: 3;
    -webkit-columns: 3;
    -moz-columns: 3;
}

ul.checkbox {
	margin: 0;
	padding: 0;
	margin-left: 20px;
	list-style: none;
}

ul.checkbox li input {
	margin-right: .25em;
}

ul.checkbox li {
	border: 1px transparent solid;
	display: inline-block;
	width: 15em;
}

ul.checkbox li label {
	margin-left:;
}

ul.checkbox li:hover, ul.checkbox li.focus {
	background-color: lightyellow;
	border: 1px gray solid;
	width: 12em;
}
</style>

<c:set var="titulo_pagina" scope="request">
	Tempo permanência de documentos no setor
</c:set>
<input type="hidden" name="secaoUsuario" id="secaoUsuario"
	value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />

<div class="row">
	<fieldset class="group">
		<legend>Assuntos</legend>
		<ul class="checkbox">
			<c:forEach items="${listaAssuntos}" var="item">
				<li><input type="checkbox" name="assuntos"
					value="${item.idClassificacao}"> ${item.codificacao} -
						${item.descrClassificacao}</input></li>
			</c:forEach>
		</ul>
	</fieldset>
</div>


<br />

<div class="row">
	<fieldset class="group">
		<legend>Setores</legend>
		<ul class="checkbox">
			<c:forEach items="${listaSetoresSubordinados}" var="item">
				<li><input type="checkbox" name="setoresSelecionados"
					value="${item.idLotacao}"> ${item.nomeLotacao}</input></li>
			</c:forEach>
		</ul>
	</fieldset>
</div>



<input type="hidden" name="lotacao" id="lotacao"
	value="${lotacaoDestinatarioSel.id}" />
<input type="hidden" name="siglalotacao" id="siglalotacao"
	value="${lotacaoDestinatarioSel.sigla}" />
<input type="hidden" name="lotacaoTitular" id="lotacaoTitular"
	value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="idTit" id="idTit" value="${titular.id}" />