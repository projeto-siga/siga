<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<sigatp:calendario/>
<sigatp:decimal/>
<sigatp:erros/>

<form id="formRelatoriosDiarios" action="${linkTo[RelatorioDiarioController].salvar}" method="post" enctype="multipart/form-data">
	<input type="hidden" name="relatorioDiario" value="${relatorioDiario.id}" />
	<div class="gt-content-box gt-form clearfix">
		<div class="clearfix">
			<div class="coluna margemDireitaG">	
		 		<input type="hidden" name="relatorioDiario.veiculo" value="${relatorioDiario.veiculo.id}">
		      	<label for="relatorioDiario.data" class= "obrigatorio">Data</label>
	     		<input type="text" name="relatorioDiario.data" value="<fmt:formatDate value="${relatorioDiario.data.time}" pattern="dd/MM/yyyy"/>" size="12" class="datePicker"></input>
		      	<label for="relatorioDiario.odometroEmKm" class= "obrigatorio">Od&ocirc;metro</label>
				<input type="text" name="relatorioDiario.odometroEmKm" value="${relatorioDiario.odometroEmKm}" style="width: 100px; text-align: right;" class="decimal"/></td>
		      	<label for="relatorioDiario.nivelDeCombustivel" class= "obrigatorio">N&iacute;vel de Combust&iacute;vel</label>
				<select name="relatorioDiario.nivelDeCombustivel">
				    <c:forEach var="nivel" items="${relatorioDiario.nivelDeCombustivel.values()}">
				        <option value="${nivel}" ${nivel == relatorioDiario.nivelDeCombustivel ? 'selected="selected"' : ''}>${nivel.descricao}</option>
				    </c:forEach>
				</select>
			</div>
			<div class="coluna">	
		      	<label for="relatorioDiario.equipamentoObrigatorio" class= "obrigatorio">Equipamento Obrigat&oacute;rio</label>
		      	<select name="relatorioDiario.equipamentoObrigatorio">
				    <c:forEach var="equip" items="${relatorioDiario.equipamentoObrigatorio.values()}">
				        <option value="${equip}" ${equip == relatorioDiario.equipamentoObrigatorio ? 'selected="selected"' : ''}>${equip.descricao}</option>
				    </c:forEach>
				</select>
		      	<label for="relatorioDiario.cartoes" class= "obrigatorio">Cart&otilde;es</label>
		      	
		      	<select name="relatorioDiario.cartoes">
				    <c:forEach var="cartao" items="${relatorioDiario.cartoes.values()}">
				        <option value="${cartao}" ${cartao == relatorioDiario.cartoes ? 'selected="selected"' : ''}>${cartao.descricao}</option>
				    </c:forEach>
				</select>
			</div>
		</div>			  
		<label for="relatorioDiario.observacao">Observa&ccedil;&atilde;o</label>
		<sigatp:controleCaracteresTextArea nomeTextArea="relatorioDiario.observacao" rows="5" cols="100" valorTextArea="${relatorioDiario.observacao}" />		
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" class="gt-btn-medium gt-btn-left" value="<fmt:message key="views.botoes.salvar"/>"/>
		<input type="button" onClick="javascript:location.href='${linkTo[RelatorioDiarioController].listarPorVeiculo(relatorioDiario.veiculo.id)}'" class="gt-btn-medium gt-btn-left" value="<fmt:message key="views.botoes.cancelar"/>"/>
	</div>
</form>