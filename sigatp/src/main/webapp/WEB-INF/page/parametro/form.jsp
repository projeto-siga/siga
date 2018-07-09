<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<sigatp:erros />
<sigatp:calendario/>
<sigatp:decimal />

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<script>
$(function() {
    $('#formParametro').submit(function() {
		$("input[name='parametro.dpLotacao']").val($("input[name='lotacao_lotacaoSel.id']").val()); 
		$("input[name='parametro.dpPessoa']").val($("input[name='pessoa_pessoaSel.id']").val()); 
        return true;
    });
});
</script>

<form id="formParametro" action="${linkTo[ParametroController].salvar}" method="post" cssClass="form" enctype="multipart/form-data">
	<div class="gt-content-box gt-form"> 
		<label for="parametro.nomeParametro" class="obrigatorio">Nome do Par&acirc;metro</label>
		<input type="text" name="parametro.nomeParametro" value="${parametro.nomeParametro}" />

		<label for="parametro.valorParametro" class="obrigatorio">Valor do Par&acirc;metro</label>
		<input type="text" name="parametro.valorParametro" value="${parametro.valorParametro}" />
	    
	    <label for="parametro.dpLotacao">Lota&ccedil;&atilde;o</label>
	    <input type="hidden" name="parametro.dpLotacao" value="" />
		<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" urlAcao="buscar" siglaInicial="${parametro.dpLotacao}" idInicial="${parametro.dpLotacao.id}" descricaoInicial="${parametro.dpLotacao.descricao}"/>
			
		<label for="parametro.dpPessoa">Servidor </label>
		<input type="hidden" name="parametro.dpPessoa" value="" />
		<siga:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" urlAcao="buscar" siglaInicial="${parametro.dpPessoa}" idInicial="${parametro.dpPessoa.id}" descricaoInicial="${parametro.dpPessoa.descricao}"/>
	    
    	<label for="parametro.cpOrgaoUsuario">Org&atilde;o do Usu&aacute;rio</label>
   		<siga:select  id="comboorgao" name="parametro.cpOrgaoUsuario" list="cpOrgaoUsuarios" listKey="idOrgaoUsu" listValue="nmOrgaoUsu" value="${parametro.cpOrgaoUsuario.idOrgaoUsu}" headerValue="" headerKey="0" />
				
   		<label for= "parametro.cpComplexo">Complexo</label>	
		<select name="parametro.cpComplexo" size="1" >
			<option value=""></option>
			<c:forEach items="${cpComplexos}" var="cpComplexo">
				<c:choose>
					<c:when test="${parametro.cpComplexo != null && cpComplexo.idComplexo == parametro.cpComplexo.idComplexo}">
	  						<option value="${cpComplexo.idComplexo}" selected=selected>${cpComplexo.nomeComplexo}</option>
					</c:when>
					<c:otherwise>
						<option value="${cpComplexo.idComplexo}">${cpComplexo.nomeComplexo}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>

		<label for="parametro.dataInicio">In&iacute;cio de Vig&ecirc;ncia</label>
    	<input type="text" name="parametro.dataInicio" class="datePicker" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${parametro.dataInicio.time}" />" />
    
	    <label for="parametro.dataFim">Fim de Vig&ecirc;ncia</label>
	    <input type="text" name="parametro.dataFim" class="datePicker" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${parametro.dataFim.time}" />" />
	    
	    <label for="parametro.descricao" class="obrigatorio">Descri&ccedil;&atilde;o</label>
	    <sigatp:controleCaracteresTextArea nomeTextArea="parametro.descricao" rows="7" cols="80" valorTextArea="${parametro.descricao}" />
										   
	    <input type="hidden" name="parametro" value="${parametro.id}"/>
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" id="salvar" value="Salvar" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="Cancelar" class="gt-btn-medium gt-btn-left" onclick="javascript:window.location = '${linkTo[ParametroController].listar}';" />
	</div>
</form>
