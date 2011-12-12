<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<ww:url id="url" action="editar" />
<script type="text/javascript" language="Javascript1.1">
	function sbmt(offset) {
		if (offset==null) {
			offset=0;
		}
		frm.elements['p.offset'].value=offset;
		frm.submit();
	}
	function editar(p_strId) {
		var t_strUrl = '${url}';
		if (p_strId) {
			if (t_strUrl.indexOf('?') == -1) {
				window.location.href = t_strUrl + '?' + 'idCpGrupo=' + p_strId ;
			} else {
				window.location.href = t_strUrl + '&' + 'idCpGrupo=' + p_strId ;
			}
		} else {
			window.location.href = t_strUrl;
		}
	}
	
</script>

<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>
<siga:pagina titulo="Busca de ${cpTipoGrupo.dscTpGrupo}">
	<h1>Cadastro de ${cpTipoGrupo.dscTpGrupo}:</h1>
	<table class="list" width="100%">
		<tr class="header">
			<td align="left">Sigla</td>
			<td align="left">Descrição</td>
			<td align="left">Sigla Grupo Pai</td>
		</tr>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<siga:paginador maxItens="1000" maxIndices="10" totalItens="${tamanho}"
			itens="${itens}" var="grupoItem">
			<tr class="${evenorodd}">
				<td align="left"><a
					href='javascript:editar(${grupoItem.idGrupo })'>${grupoItem.siglaGrupo
				}</a></td>
				<td align="left"><a
					href='javascript:editar(${grupoItem.idGrupo })'>${grupoItem.dscGrupo
				}</a></td>
				<td align="left"><a
					href='javascript:editar(${grupoItem.idGrupo })'>${grupoItem.cpGrupoPai.siglaGrupo
				}</a></td>
			</tr>
		</siga:paginador>
	<br/>
	<input type="button" value="Incluir" onclick="javascript:editar()">
</siga:pagina>
