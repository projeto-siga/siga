<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<sigatp:erros/>

<form id="formFinalidade" action="${linkTo[FinalidadeController].salvar(finalidade)}" method="post" enctype="multipart/form-data">
	<div class="gt-content-box gt-form"> 
		<label for="finalidade.descricao" class="obrigatorio">Descri&ccedil;&atilde;o</label>
		<input type="text" name="finalidade.descricao" value="${finalidade.descricao}" />
     	<input type="hidden" name="finalidade" value="${finalidade.id}"/>
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio" /></span>
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar" />" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="<fmt:message key="views.botoes.cancelar" />" class="gt-btn-medium gt-btn-left" onclick='javascript:window.location = "${linkTo[FinalidadeController].listar}";' />
	</div>
</form>