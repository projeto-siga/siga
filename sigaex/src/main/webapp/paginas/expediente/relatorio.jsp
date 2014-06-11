<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@
page language="java" contentType="text/html; charset=UTF-8" buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>

<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Relatório">

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

<c:choose>
	<c:when test='${param.nomeArquivoRel eq "relFormularios.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelFormularios</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Formulários</c:set>
		<c:set var="nomeRelatorio" scope="request">relFormularios.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relConsultaDocEntreDatas.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelDocEntreDatas</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de documentos entre datas</c:set>
		<c:set var="nomeRelatorio" scope="request">relConsultaDocEntreDatas.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relModelos.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelModelos</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Modelos</c:set>
		<c:set var="nomeRelatorio" scope="request">relModelos.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relDocumentosSubordinados.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelDocumentosSubordinados</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Documentos em Setores Subordinados</c:set>
		<c:set var="nomeRelatorio" scope="request">relDocumentosSubordinados.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relMovimentacaoDocSubordinados.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelMovDocsSubordinados</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Movimentação de Documentos em Setores Subordinados</c:set>
		<c:set var="nomeRelatorio" scope="request">relMovimentacaoDocSubordinados.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relCrDocSubordinados.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelDocsSubCriados</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Criação de Documentos em Setores Subordinados</c:set>
		<c:set var="nomeRelatorio" scope="request">relCrDocSubordinados.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relMovimentacao.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelMovimentacao</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Movimentações</c:set>
		<c:set var="nomeRelatorio" scope="request">relMovimentacao.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relMovCad.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelMovCad</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Movimentações por Cadastrante</c:set>
		<c:set var="nomeRelatorio" scope="request">relMovCad.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relOrgao.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelOrgao</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Despachos e Transferências</c:set>
		<c:set var="nomeRelatorio" scope="request">relOrgao.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relTipoDoc.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelTipoDoc</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Documentos Criados</c:set>
		<c:set var="nomeRelatorio" scope="request">relTipoDoc.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relMovProcesso.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelMovProcesso</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Movimentações de Processos</c:set>
		<c:set var="nomeRelatorio" scope="request">relMovProcesso.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relClassificacao.jsp"}'>
		<c:set var="actionName" scope="request">aRelClassificacao</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Classificação Documental</c:set>
		<c:set var="nomeRelatorio" scope="request">relClassificacao.jsp</c:set>
	</c:when>
	<c:when test='${param.nomeArquivoRel eq "relDocsClassificados.jsp"}'>
		<c:set var="actionName" scope="request">emiteRelClassDocDocumentos</c:set>
		<c:set var="titulo_pagina" scope="request">Relação de Documentos Classificados</c:set>
		<c:set var="nomeRelatorio" scope="request">relDocsClassificados.jsp</c:set>
	</c:when>
	
	<c:otherwise>
		<c:set var="actionName" scope="request">emiteRelExpedientes</c:set>
		<c:set var="titulo_pagina" scope="request">Relatório de Expedientes</c:set>
		<c:set var="nomeRelatorio" scope="request">relExpedientes.jsp</c:set>
		<c:set var="tipoRelatorio" scope="request">relExpedientes.jrxml</c:set>
	</c:otherwise>
</c:choose>

<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${titulo_pagina}</h2>
			<div class="gt-content-box gt-for-table">
			
		<ww:form name="frmRelatorios" action="${actionName}"
			theme="simple" namespace="/expediente/rel" method="GET"
			enctype="multipart/form-data">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="secaoUsuario"
				value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />
			<ww:hidden name="tipoRelatorio" value="${tipoRelatorio}" />
			
			<table class="gt-form-table">
			<tr class="header">
				<td colspan="2">Dados do Relatório</td>
			</tr>
			<c:import url="/paginas/expediente/relatorios/${nomeRelatorio}"/>
			<tr class="button">
				<td colspan="2"><ww:url id="url" action="${actionName}"
					namespace="/expediente/rel" /> <input type="button"
					value="Gerar" onclick="javascript:visualizarRelatorio('${url}');" /></td>
			</tr>
			</table>
		</ww:form>
</div></div></div>
</siga:pagina>
