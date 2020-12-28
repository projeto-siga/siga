<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<jsp:include page="../tags/calendario.jsp" />
<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<script type="text/javascript" language="Javascript1.1">
function verificaCampos(){
	var dataHoraInicio = document.getElementById("dataHoraInicio").value;
	var dataHoraFim = document.getElementById("dataHoraFim").value;
		
	if (dataHoraInicio == "" || dataHoraFim == ""){
		alert("Uma ou mais datas estÃ£o sem preenchimento.");
		return false;
	}
	else{
		dataHoraInicio = new Date(dataHoraInicio);
		dataHoraFim = new Date(dataHoraFim);
		if (dataHoraInicio > dataHoraFim) {
			alert("A data e hora do fim do plantÃ£o sÃ£o anteriores ao inÃ­cio.");
			return false;
		}
		return true;
	}	
}
</script>

<jsp:include page="../veiculo/menu.jsp"/>
<sigatp:erros/>
<br>
<form name="formPlantoes" id="formPlantoes" action="${linkTo[PlantaoController].salvar}" method="post" cssClass="form">
	<input type="hidden" name="plantao" value="${plantao.id}" />
	<input type="hidden" name="plantao.condutor.id" value="${plantao.condutor.id}">
	<div class="gt-content-box gt-form clearfix">
		<label for="plantao.dataHoraInicio" class= "obrigatorio">In&iacute;cio</label>
		<input type="text" id="dataHoraInicio" name="plantao.dataHoraInicio" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantao.dataHoraInicio.time}" />" size="14" class="dataHora" />
   		<input type="hidden" id="dataHoraInicioNova" name="dataHoraInicioNova" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantao.dataHoraInicio.time}" />" size="14" class="dataHora" />
		<label for="plantao.dataHoraFim" class= "obrigatorio">Fim</label>
		<input type="text" id="dataHoraFim" name="plantao.dataHoraFim" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantao.dataHoraFim.time}" />" size="14" class="dataHora" />
		<input type="hidden" id="dataHoraFimNova" name="dataHoraFimNova" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantao.dataHoraFim.time}" />" size="14" class="dataHora" />
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
 		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" onClick="javascript:window.location = '${linkTo[PlantaoController].listarPorCondutor(plantao.condutor.id)}'" class="gt-btn-medium gt-btn-left" />
	</div>
</form>