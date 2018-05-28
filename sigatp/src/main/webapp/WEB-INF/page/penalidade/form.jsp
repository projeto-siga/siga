<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<sigatp:decimal />
<form name="formPenalidades" id="formPenalidades"
	action="${linkTo[PenalidadeController].salvar}" method="post"
	cssClass="form" enctype="multipart/form-data"> 
	<sigatp:erros />
	<div class="gt-content-box gt-form"> 
		<label for="penalidade.codigoInfracao" class="obrigatorio">C&oacute;digo Infra&ccedil;&atilde;o</label>
		<input type="text" name="penalidade.codigoInfracao" value="${penalidade.codigoInfracao}" />

		<label for="penalidade.descricaoInfracao" class="obrigatorio">Descri&ccedil;&atilde;o Infra&ccedil;&atilde;o</label>
		<input type="text" size=100 name="penalidade.descricaoInfracao" value="${penalidade.descricaoInfracao}" />

		<label for="penalidade.artigoCTB" class="obrigatorio">Artigo do CTB</label>
		<input type="text" name="penalidade.artigoCTB" value="${penalidade.artigoCTB}" />

		<label for="penalidade.valor" class="obrigatorio">Valor</label>
		<input type="text" id="valor" name="penalidade.valor" value="${penalidade.valor}" class="valor_numerico decimal"/>

		<label for="penalidade.infrator" class="obrigatorio">Tipo de Infrator</label>
		<select name="penalidade.infrator" >
			<c:forEach items="${penalidade.infrator.values()}" var="infrator">
				<option value="${infrator}" ${infrator == penalidade.infrator ? 'selected' : ''}>${infrator.descricao}</option>
			</c:forEach>
		</select>									
		
		<label for="penalidade.classificacao" class= "obrigatorio">Classifica&ccedil;&atilde;o</label>
 		<select name="penalidade.classificacao" > 
 			<c:forEach items="${penalidade.classificacao.values()}" var="classificacao"> 
 				<option value="${classificacao}" ${classificacao == penalidade.classificacao ? 'selected' : ''}>${classificacao.descricao}</option>
 			</c:forEach> 
		</select> 					 
				
        <input type="hidden" name="penalidade" value="${penalidade.id}"/>
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" class="gt-btn-medium gt-btn-left" onclick="javascript:window.location = '${linkTo[PenalidadeController].listar}';" />
	</div>

</form>
