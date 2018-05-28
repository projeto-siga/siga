<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina  titulo="Transportes">
	<script src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>	
	
	<script type="text/javascript">
		$(function() {
			var $mudouPenalidadeId = $('#selPenalidade').change(function() {
				var idPenalidade = $('#selPenalidade option:selected').val();
	
				if ("${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo}") {
					if (idPenalidade != '') {
						$.ajax({
							url :  "${linkTo[PenalidadeController].listarValorPenalidade}?idPenalidade=" + idPenalidade,
							dataType : 'text',
							success : function(data) {
								if (($('#autoDeInfracaoId').val()) == 0) {
									$('#valor').val(data);
								}
							}
						});
	
						$.ajax({
							url : "${linkTo[PenalidadeController].listarClassificacaoPenalidade}?idPenalidade=" + idPenalidade,
							dataType : 'text',
							success : function(data) {
								$('#penalidadeClassificacao').val(data);
								$('#valor').focus();
							}
						});
					}
				}
			});
	
			$mudouPenalidadeId.trigger('change');
		});
	</script>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${autoDeInfracao.id > 0 ? "Editar" : "Incluir" } Auto de Infra&ccedil;&atilde;o</h2>
			<jsp:include page="../tags/calendario.jsp" />
			<sigatp:decimal/>
			<sigatp:erros/>
			
			<form name="formAutosDeInfracao" id="formAutosDeInfracao" action="${linkTo[AutoDeInfracaoController].salvar}" cssClass="form" method="post" enctype="multipart/form-data">
				<div class="gt-content-box gt-form clearfix">
					<div class="coluna margemDireitaG">
				       	<label for="autoDeInfracao.dataHora" class= "obrigatorio">Data e Hora</label>
						<input type="text" id="dataHora" name="autoDeInfracao.dataHora" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${autoDeInfracao.dataHora.time}" />" size="20" class="dataHora"/> 

						<label for="autoDeInfracao.veiculo.id" class= "obrigatorio">Ve&iacute;culo</label>
						<siga:select name="autoDeInfracao.veiculo.id" list="veiculos" listKey="id" listValue="dadosParaExibicao" value="${autoDeInfracao.veiculo.id}"/>
			
						<label for="autoDeInfracao.condutor.id" class= "obrigatorio">Condutor</label>
						<siga:select name="autoDeInfracao.condutor.id" list="condutores" listKey="id" listValue="dadosParaExibicao" value="${autoDeInfracao.condutor.id}"/>

						<label for="autoDeInfracao.local" class= "obrigatorio">Local</label>
						<input type="text" id="local" name="autoDeInfracao.local" size="46" value="${autoDeInfracao.local}"/>
					</div>
					<div class="coluna margemDireitaG">
						<c:if test="${tipoNotificacao.toString().contains('AUTUACAO')}">
							<label for="autoDeInfracao.tipoDeNotificacao" class= "obrigatorio">Autua&ccedil;&atilde;o</label>
						</c:if>
						
						<c:if test="${tipoNotificacao.toString().contains('PENALIDADE')}">
							<label for="autoDeInfracao.tipoDeNotificacao" class= "obrigatorio">Penalidade</label>
						</c:if>

						<input type="hidden" id="tipoDeNotificacao" name="autoDeInfracao.tipoDeNotificacao" value="${autoDeInfracao.tipoDeNotificacao}" />
						<siga:select id="selPenalidade" name="autoDeInfracao.penalidade.id" list="penalidades" listKey="id"  listValue="dadosParaExibicao" value="${autoDeInfracao.penalidade.id}"/>
					</div>
					
					<div class="coluna">
						<label for="autoDeInfracao.foiRecebido" class= "obrigatorio">Recebido?</label>
                        <select name="autoDeInfracao.foiRecebido">
	                        <c:forEach items="${autoDeInfracao.foiRecebido.values()}" var="foiRecebido">
	                            <option value="${foiRecebido}" ${autoDeInfracao.foiRecebido == foiRecebido ? 'selected' : ''}>${foiRecebido.descricao}</option>
	                        </c:forEach>
                        </select>

						<label for="autoDeInfracao.numeroDoProcesso">N&uacute;mero do Processo</label>
						<input type="text" id="numeroDoProcesso" name="autoDeInfracao.numeroDoProcesso" value="${autoDeInfracao.numeroDoProcesso}" />			
						
						<label for="autoDeInfracao.dataDeVencimento" class= "obrigatorio">Vencimento</label>
						<input type="text" id="dataDeVencimento" name="autoDeInfracao.dataDeVencimento" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autoDeInfracao.dataDeVencimento.time}"/>" class="datePicker"/>
						
						<label for="autoDeInfracao.valor" class= "obrigatorio">Valor</label>
						<input type="text" id="valor" name="autoDeInfracao.valor" value="${autoDeInfracao.valor}" class="decimal"/>
						
						<div id="dadosPenalidade">
							<label for='penalidadeClassificacao'>Classifica&ccedil;&atilde;o</label>
							<input type='text' id="penalidadeClassificacao" name="penalidadeClassificacao" value="" readonly="readonly" />			
						</div>	

						<label for="autoDeInfracao.valorComDesconto">Valor c/ Desconto</label>
						<input type="text" name="autoDeInfracao.valorComDesconto" value="${autoDeInfracao.valorComDesconto}" class="decimal"/>
						
						<label for="autoDeInfracao.dataDePagamento">Pagamento</label>
						<input type="text" name="autoDeInfracao.dataDePagamento" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autoDeInfracao.dataDePagamento.time}"/>" class="datePicker"/>
						
						<input type="hidden" id="autoDeInfracaoId" name="autoDeInfracao" value="${autoDeInfracao.id}"/>
					</div>
				</div>
				<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
				<div class="gt-table-buttons">
					<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
					<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" class="gt-btn-medium gt-btn-left" onClick="javascript:window.location='${linkTo[AutoDeInfracaoController].listar}'" />
				</div>
			</form>
		</div>
	</div>
</siga:pagina>