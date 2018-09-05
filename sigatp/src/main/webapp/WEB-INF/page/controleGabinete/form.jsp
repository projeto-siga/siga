<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal />

<form id="formGabinete" action="${linkTo[ControleGabineteController].salvar}" method="post" class="form" enctype="multipart/form-data">
	<div class="gt-content-box gt-form"> 
	<input type="hidden" name="controleGabinete" value="${controleGabinete.id}" />
	<div class="clearfix">
		<div class="coluna margemDireitaG">
	       	<label for= "controleGabinete.veiculo" class= "obrigatorio">Ve&iacute;ulo</label>
	       	<siga:select id="veiculos" name="controleGabinete.veiculo" list="veiculos" listKey="id" listValue="dadosParaExibicao" value="${controleGabinete.veiculo.id}" />
	       	<label for= "controleGabinete.condutor" class= "obrigatorio">Condutor</label>
	       	<siga:select id="condutores" name="controleGabinete.condutor" list="condutores" listKey="id" listValue="dadosParaExibicao" value="${controleGabinete.condutor.id}" />
		</div>
		<div class="coluna margemDireitaG">
	       	<label for="controleGabinete.dataHoraSaida" class= "obrigatorio">Data e Hora Sa&iacute;da</label>
	    	<input type="text" name="controleGabinete.dataHoraSaida" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${controleGabinete.dataHoraSaida.time}" />" size="16" class="dataHora" />
	       	<label for="controleGabinete.odometroEmKmSaida" class= "obrigatorio">Od&ocirc;metro Sa&iacute;da (Km)</label>
	       	<input type="text" name="controleGabinete.odometroEmKmSaida" value="${controleGabinete.odometroEmKmSaida}" class="valor_numerico decimal"/>
		</div>
		<div class="coluna">
	       	<label for="controleGabinete.dataHoraRetorno" class= "obrigatorio">Data e Hora Retorno</label>
	    	<input type="text" name="controleGabinete.dataHoraRetorno" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${controleGabinete.dataHoraRetorno.time}" />" size="16" class="dataHora" />
	       	<label for="controleGabinete.odometroEmKmRetorno" class= "obrigatorio">Od&ocirc;metro Retorno (Km)</label>
	       	<input type="text" name="controleGabinete.odometroEmKmRetorno" value="${controleGabinete.odometroEmKmRetorno}" class="valor_numerico decimal"/>
		</div>
	</div>
	<label for="controleGabinete.naturezaDoServico" class= "obrigatorio">Natureza do servi&ccedil;o</label>
	<sigatp:controleCaracteresTextArea nomeTextArea="controleGabinete.naturezaDoServico" rows="6" cols="230" valorTextArea="${controleGabinete.naturezaDoServico}" />
										   
	<label for="controleGabinete.itinerario" class= "obrigatorio">Itiner&aacute;rio</label>
	<sigatp:controleCaracteresTextArea nomeTextArea="controleGabinete.itinerario" rows="6" cols="230" valorTextArea="${controleGabinete.itinerario}" />

	<label for="controleGabinete.ocorrencias">Ocorr&ecirc;ncias</label>
	<sigatp:controleCaracteresTextArea nomeTextArea="controleGabinete.ocorrencias" rows="6" cols="230" valorTextArea="${controleGabinete.ocorrencias}" />
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" onClick="javascript:location.href='${linkTo[ControleGabineteController].listar}'" class="gt-btn-medium gt-btn-left" />
	</div>
</form>