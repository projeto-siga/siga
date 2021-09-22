<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<sigatp:erros/>

<form action="${linkTo[AfastamentoController].salvar}" method="post" id="formAfastamentos">
	<input type="hidden" name="afastamento" value="${afastamento.id}" />
	<input type="hidden" name="afastamento.condutor" value="${afastamento.condutor.id}">
	
	<div class="gt-content-box gt-form clearfix">  
       	<label for="afastamento.descricao" class="obrigatorio"><fmt:message key="afastamento.descricao"/></label>
   		<input type="text" id="descricao" name="afastamento.descricao" value="${afastamento.descricao}" size="60" />

       	<label for="afastamento.dataHoraInicio" class="obrigatorio"><fmt:message key="afastamento.dataHoraInicio"/></label>
   		<input type="text" id="dataHoraInicio" name="afastamento.dataHoraInicio" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${afastamento.dataHoraInicio.time}" />" size="20" class="dataHora" />

       	<label for="afastamento.dataHoraFim" class="obrigatorio"><fmt:message key="afastamento.dataHoraFim"/></label>
   		<input type="text" id="dataHoraFim" name="afastamento.dataHoraFim" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${afastamento.dataHoraFim.time}" />" size="20" class="dataHora" />
	</div>

	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>

	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" onClick="javascript:location.href='${linkTo[AfastamentoController].listarPorCondutor(afastamento.condutor.id)}'" class="gt-btn-medium gt-btn-left" />
	</div>
</form>

<jsp:include page="../tags/calendario.jsp" />