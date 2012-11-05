<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
page
	language="java" contentType="text/html; charset=UTF-8" buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--   -->
<%@ taglib prefix="ww" uri="/webwork"%>

<script type="text/javascript" language="Javascript1.1">
<ww:url id="url" action="relRelatorios" namespace="/expediente/rel">
</ww:url>
function sbmt() {
	frmRelatorios.action='<ww:property value="%{actionName}"/>';
	frmRelatorios.submit();	
}

var newwindow = '';
function visualizarRelatorio(rel) {
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow=window.open('','',winProp);
		newwindow.name='doc';
	}
	
	newwindow.opener = self;
	t = frmRelatorios.target; 
	a = frmRelatorios.action;
	frmRelatorios.target = newwindow.name;
	frmRelatorios.action=rel;
	frmRelatorios.submit();
	frmRelatorios.target = t; 
	frmRelatorios.action = a;
	
	if (window.focus) {
		newwindow.focus()
	}
	return false;
}		
</script>

<c:import url="/paginas/cabecalho.jsp" />
<c:if test='${param.nomeArquivoRel eq "relFormularios.jsp"}'>
	<c:set var="actionName" scope="request">emiteRelFormularios</c:set>
	<c:set var="titulo_pagina" scope="request">Relação de Formulários</c:set>
	<c:set var="nomeRelatorio" scope="request">relFormularios.jsp</c:set>
	<c:set var="tipoRelatorio" scope="request">relFormularios.jrxml</c:set>
</c:if>
<c:if test='${param.nomeArquivoRel eq "relExpedientes.jsp"}'>
	<c:set var="actionName" scope="request">emiteRelExpedientes</c:set>
	<c:set var="titulo_pagina" scope="request">Relatório de Expedientes</c:set>
	<c:set var="nomeRelatorio" scope="request">relExpedientes.jsp</c:set>
	<c:set var="tipoRelatorio" scope="request">relExpedientes.jrxml</c:set>
</c:if>
<c:if test='${param.nomeArquivoRel eq "relMovInconsistentes.jsp"}'>
	<c:set var="actionName" scope="request">emiteRelMovimentacao</c:set>
	<c:set var="titulo_pagina" scope="request">Relatório de Vias Inconsistentes</c:set>
	<c:set var="nomeRelatorio" scope="request">relMovInconsistentes.jsp</c:set>
	<c:set var="tipoRelatorio" scope="request">relMovViasInconsistentes.jrxml</c:set>
</c:if>

<table width="100%">
	<tr>
		<td><ww:form name="frmRelatorios" action="${actionName}"
			theme="simple" namespace="/expediente/rel" method="POST"
			enctype="multipart/form-data">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="secaoUsuario"
				value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />
			<ww:hidden name="tipoRelatorio" value="${tipoRelatorio}" />
			<tr>
				<td>
				<table class="form" width="100%">
					<tr class="header">
						<td colspan="2">Dados do Relatório</td>
					</tr>
					<c:import url="/paginas/expediente/relatorios/${nomeRelatorio}" />
					</td>
					</tr>
					<tr class="button">
						<td></td>
						<td><ww:url id="url" action="${actionName}"
							namespace="/expediente/rel" /> <input type="button"
							value=" Gerar "
							onclick="javascript:visualizarRelatorio('${url}');" /></td>
					</tr>
				</table>
		</ww:form></td>
	</tr>
</table>

<%--  tabela do rodapé --%>
<c:import url="/paginas/rodape.jsp" />
