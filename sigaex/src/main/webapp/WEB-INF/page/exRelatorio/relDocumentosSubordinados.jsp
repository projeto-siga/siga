<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
<tr>
	<td>
		Tipo de Relatório:
	</td>
	<td>
		<select name="tipoRel" id="tipoRel">
			<c:forEach var="item" items="${listaTipoRel}" >
				<option value="${item.key}">
					${item.value}
				</option>
			</c:forEach>
		</select>
	</td>
</tr>
<tr>
	<td width="15%">
		Lotação:
	</td>
	<td>
		<siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga"/>
	</td>
</tr>
<tr>
	<td>
		Incluir setores subordinados?
	</td>
	<td>
		<input type="checkbox" name="incluirSubordinados" id="incluirSubordinados"/>
	</td>
<tr>
	<td>
		Tipo de Documento:
	</td>
	<td>
	
		<select name="tipoFormaDoc" id="tipoFormaDoc">
			<c:forEach items="${listaExTipoFormaDoc}" var="item">
				<option value="${item.descricao}" >
					${item.descricao}
				</option>
				
			</c:forEach>
		</select>
		
	</td>
</tr>

<input type="hidden" name="lotacaoTitular" value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />
<input type="hidden" name="idTit" value="${titular.id}" />



