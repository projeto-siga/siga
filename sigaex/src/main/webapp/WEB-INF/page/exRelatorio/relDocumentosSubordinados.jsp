<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	frm.action='${url}';
	frm.submit();
}
</script>

<c:set var="titulo_pagina" scope="request">
	Relação de Documentos em Setores Subordinados
</c:set>
<c:set var="secaoUsuario" scope="request">
	"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"
</c:set>
<div class="row">
	<div class="col-sm-2">
		<label>Tipo de Relatório</label>
		<select name="tipoRel" id="tipoRel" class="form-control">
			<c:forEach var="item" items="${listaTipoRel}" >
				<option value="${item.key}">
					${item.value}
				</option>
			</c:forEach>
		</select>
	</div>
	<div class="col-sm-6">
		<label><fmt:message key="usuario.lotacao"/></label>
		<siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga"/>
	</div>
</div>
<div class="row">
	<div class="col-sm-2 mt-4 ml-4">
		<input type="checkbox" name="incluirSubordinados" id="incluirSubordinados" class="form-check-input"/><label class="form-check-label" for="incluirSubordinados">Incluir setores subordinados?</label>
	</div>
	<div class="col-sm-2">
		<label>Tipo de Documento</label>
		<select name="tipoFormaDoc" id="tipoFormaDoc" class="form-control">
			<c:forEach items="${listaExTipoFormaDoc}" var="item">
				<option value="${item.descricao}" >
					${item.descricao}
				</option>
				
			</c:forEach>
		</select>
	</div>
</div>
<input type="hidden" name="lotacaoTitular" value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />
<input type="hidden" name="idTit" value="${titular.id}" />



