<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<script src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="/sigatp/public/javascripts/jquery/jquery-1.6.4.min.js"></script>

<sigatp:calendario />
<sigatp:erros />

<form id="formRelatoriosUso" action="${linkTo[RelatorioUsoVeiculosController].gerarRelatorio}" method="post" enctype="multipart/form-data">
	<div class="gt-content-box gt-form">
		<div>
			<label for="relatorio.dataInicio" class="obrigatorio">Data In&iacute;cio</label>
			<input type="text" name="relatorio.dataInicio" class="datePicker" value="<fmt:formatDate value="${relatorio.dataInicio.time}" pattern="dd/MM/yyyy" />" />
		</div>
		<div>
			<label for="relatorio.dataFim" class="obrigatorio">Data Fim</label>
			<input type="text" name="relatorio.dataFim" class="datePicker" value="<fmt:formatDate value="${relatorio.dataFim.time}" pattern="dd/MM/yyyy" />" /> 
		</div>
	</div>

	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio" /></span>
	<div class="gt-table-buttons">
		<input type="submit" value=<fmt:message key="views.botoes.pesquisar"/> class="gt-btn-medium gt-btn-left" />
	</div>
</form>