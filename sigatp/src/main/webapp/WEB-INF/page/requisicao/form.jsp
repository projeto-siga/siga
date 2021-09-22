<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<jsp:include page="menu.jsp"></jsp:include>
<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal />
<sigatp:erros/>

<form id="formRequisicoes" method="post" action="${linkTo[RequisicaoController].salvar}" enctype="multipart/form-data">
	<input type="hidden" name="requisicaoTransporte" value="${requisicaoTransporte.id}" />
	<input type="hidden" name="requisicaoTransporte.idSolicitante" value="${requisicaoTransporte.idSolicitante}" />

	<h3>Informa&ccedil;&otilde;es B&aacute;sicas</h3>
	<div id="infbasicas" class="gt-content-box gt-form clearfix">
		<c:if test="${requisicaoTransporte.id != 0}">
        	<label>Solicitante / Fun&ccedil;&atilde;o / Lota&ccedil;&atilde;o</label>

        	<c:if test="${null != requisicaoTransporte.solicitante}">
        		<h5>${requisicaoTransporte.solicitante.nomePessoaAI} -
        			${null != requisicaoTransporte.solicitante.funcaoConfianca ? requisicaoTransporte.solicitante.funcaoConfianca.nmFuncaoConfiancaAI : ""} -
        			${null != requisicaoTransporte.solicitante.lotacao ? requisicaoTransporte.solicitante.lotacao.nomeLotacaoAI : ""}
        		</h5>
        	</c:if>

        	<label>Data de Inclus&atilde;o</label>
        	<h5><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHora.time}"/></h5>
		</c:if>
		<div class="coluna">
	       	<label class="obrigatorio">Tipo</label>
	       	<select name="${requisicaoTransporte.tipoRequisicao}" disabled>
	       		<c:forEach items="${tiposRequisicao}" var="tipoRequisicao">
	       			<option value="${tipoRequisicao}" ${tipoRequisicao == requisicaoTransporte.tipoRequisicao ? 'selected' : ''}>${tipoRequisicao.descricao}</option>
	       		</c:forEach>
	       	</select>
<%-- 			<input type="hidden" name="requisicaoTransporte.tipoRequisicao" value="${requisicaoTransporte.tipoRequisicao}"> --%>
        	<label class="obrigatorio">Sa&iacute;da Prevista</label>
       		<input type="text" name="requisicaoTransporte.dataHoraSaidaPrevista" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraSaidaPrevista.time}" />" size="16" class="dataHora" /> <br/>
       		<input type="checkbox" id="checkRetorno" name="checkRetorno" ${checkRetorno ? 'checked' : ''}>
        	<label>Deseja programar o retorno?</label>
	        <div id="trRetorno" class="indentado clearfix">
	        	<label>Retorno Previsto</label>
        		<input type="text" id="dataHoraRetornoPrevisto" name="requisicaoTransporte.dataHoraRetornoPrevisto" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraRetornoPrevisto.time}"/>" size="16" class="dataHora" />
	        </div>
	        <div id="espacoRetorno" class="margemInferior"></div>
        	<label class="obrigatorio">Finalidade</label>
        	<select name="requisicaoTransporte.tipoFinalidade" id="tipoFinalidade">
        		<c:forEach items="${finalidades}" var="tipoFinalidade">
        			<option value="${tipoFinalidade.id}" ${(tipoFinalidade.id == requisicaoTransporte.tipoFinalidade.id) ? 'selected' : ''}>${tipoFinalidade.descricao}</option>
        		</c:forEach>
        	</select>
        	
       		<c:if test="${mostrarDetalhes}">
       			<label for="requisicaoTransporte.finalidade" id="detalhesFinalidade" class="obrigatorio">Detalhes da finalidade</label>
				<sigatp:controleCaracteresTextArea idTextArea="requisicaoTransporte.finalidade" nomeTextArea="requisicaoTransporte.finalidade" rows="10" cols="60" valorTextArea="${requisicaoTransporte.finalidade}" />       			
       			</div>
       			<div class="coluna">
       		</c:if>
       		      	        	        	
        	
        	<label for="requisicaoTransporte.itinerarios" id="itinerario" class="obrigatorio">Itiner&aacute;rio </label>   		        	
			<sigatp:controleCaracteresTextArea idTextArea="itinerarios" nomeTextArea="requisicaoTransporte.itinerarios" rows="6" cols="60" valorTextArea="${requisicaoTransporte.itinerarios}" />       			

       		<c:if test="${!mostrarDetalhes}">
	    	</div>
	        <div class="coluna">
       		</c:if>
	        
	        <div>
				<input type="checkbox" id="checkSemPassageiros" name="checkSemPassageiros" ${checkSemPassageiros ? 'checked' : ''}>
	        	<label>Sem passageiros</label>
			</div>
	        <div class="margemInferior"></div>
	    	<div id="trPassageiros" class="clearfix">
	        	<label class="obrigatorio">Tipo de passageiros</label>
				<h6> (&eacute; poss&iacute;vel selecionar mais de um usando a tecla "Ctrl")</h6>
				<select id="tiposDePassageiros" name="tiposDePassageiros" multiple size="5" title="caso necess&aacute;rio selecione mais de um usando a tecla 'Ctrl'">
					<c:forEach items="${opcoesDeTiposDePassageiro}" var="tipo">
						<option value="${tipo}" <c:if test="${requisicaoTransporte.contemTipoDePassageiro(tipo)}"> selected="selected" </c:if>>${tipo.descricao}</option>
					</c:forEach>
				</select>
	        	<label for="requisicaoTransporte.passageiros" id="lblPassageiros" class="obrigatorio">Passageiros</label>
				<sigatp:controleCaracteresTextArea idTextArea="passageiros" nomeTextArea="requisicaoTransporte.passageiros" rows="6" cols="60" valorTextArea="${requisicaoTransporte.passageiros}" />       			
			</div>
		</div>
	</div>
	<br/>

	<c:if test="${!esconderBotoes}">
		<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio" /></span>
		<div id="btnAcoes" class="gt-table-buttons">
			<input id="btnSalvar" type="submit" value="<fmt:message key="views.botoes.salvar" />" class="gt-btn-medium gt-btn-left" />
			<c:choose>
				<c:when test="${requisicaoTransporte.id > 0}">
					<input type="button" id="btnVoltar"  value="<fmt:message key="views.botoes.voltar"/>" onClick="javascript:location.href='${linkTo[RequisicaoController].buscarPelaSequence(popUp, sequence)}'?popUp=true&sequence=${requisicaoTransporte.buscarSequence()}')" class="gt-btn-medium gt-btn-left" />
				</c:when>
				<c:otherwise>
					<input type="button" id="btnVoltar"  value="<fmt:message key="views.botoes.voltar"/>" onClick="javascript:location.href='${linkTo[RequisicaoController].listar}'" class="gt-btn-medium gt-btn-left" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</form>

<script type="text/javascript">
	$(document).ready(function() {
		function modificacaoDinamicaFinalidade(event){			
			var tipoFinalidadeSelecionado = $('#tipoFinalidade option:selected').text().trim();		
			$('#detalhesFinalidade').removeClass('obrigatorio');	
			if (tipoFinalidadeSelecionado==='OUTRA') {
				$('#detalhesFinalidade').addClass('obrigatorio');
			}
			
			if (!$('#detalhesFinalidade').hasClass('obrigatorio')) {
				document.getElementById("requisicaoTransporte.finalidade").style.border = "thin solid #BBB";
			}
		}

		function modificacaoDinamicaTipoPassageiro(){			
			var labelPassageiros = $("label:contains('Passageiros')");
			var labelDescricao = $("label:contains('Descri\u00E7\u00E3o')");
			var tipoPassageiroSelecionado = $('#tiposDePassageiros option:selected').text().trim();		
			if (tipoPassageiroSelecionado==='CARGA' && labelDescricao.length == 0){											
				$('label').remove( ":contains('Passageiros')" );				
				var novoLabel1 = $("<label>", {for : "requisicaoTransporte.passageiros", class : "obrigatorio"}).text("Descri\u00E7\u00E3o");
				$("#passageiros").before(novoLabel1);							
			}else if (labelPassageiros.length == 0) { 
				$('label').remove( ":contains('Descri\u00E7\u00E3o')" );
				var novoLabel2 = $("<label>", {for:"requisicaoTransporte.passageiros", class : "obrigatorio"}).text("Passageiros");
				$("#passageiros").before(novoLabel2);
			}				
		}

		function protegeDocumento() {
			$('#infbasicas').find('input, textarea, button, select').attr('disabled','disabled');
		}

		function exibirOuOcultarTxtRetorno() {
		    if($('#checkRetorno').is(":checked")) {
		    	$('#trRetorno').show(250);
		    } else {
		    	$('#dataHoraRetornoPrevisto').val('');
		    	$('#trRetorno').hide(250);
		    }
	    }

		function exibirOuOcultarTxtPassageiros() {
		    if($('#checkSemPassageiros').is(":checked")) {
		    	$('#passageiros').val('');
		    	$('#tiposDePassageiros').val('');
		    	$('#trPassageiros').hide(250);
		    } else {
		    	$('#trPassageiros').show(250);
		    }
	    }

		function habilitarOuDesabilitarTiposPassageiro() {
		    if($('#checkSemPassageiros').is(":checked")) {
		    	$("#tiposDePassageiros").attr("disabled", "disabled");
		    } else {
		    	$("#tiposDePassageiros").removeAttr("disabled");
		    }
	    }

	    $('#checkRetorno').change(exibirOuOcultarTxtRetorno);
	    $('#checkSemPassageiros').change(exibirOuOcultarTxtPassageiros);
	    $('#tipoFinalidade').change(modificacaoDinamicaFinalidade);
	 	$('#tiposDePassageiros').change(modificacaoDinamicaTipoPassageiro);

	    exibirOuOcultarTxtRetorno();
	    exibirOuOcultarTxtPassageiros();
	    modificacaoDinamicaFinalidade();
	    modificacaoDinamicaTipoPassageiro();

	    if($("#lersomente").length != 0) {
			protegeDocumento();
		}	             

	});
</script>