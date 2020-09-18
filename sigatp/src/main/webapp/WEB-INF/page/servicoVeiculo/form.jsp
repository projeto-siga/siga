<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>

<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/stylesheets/servicoVeiculo.css">
<jsp:include page="../tags/calendario.jsp" />
<sigatp:erros />

<script type="text/javascript">

	function verificarSituacao() {
		var situacao = $('#situacaoServico').val();
		
		if (${avarias.size() > 0}) {
			$("#trAvarias").show();

			$("[name='chk']").each(function() {
				if (situacao != 'REALIZADO') {
					if ($(this).attr("checked")) {
						$(this).prop( "checked", false );
					}
					$(this).attr("disabled","true");
				}
				else {
			    	$(this).removeAttr("disabled");
				}
			});
		}
		else {
			$("#trAvarias").hide();
		}

		if (situacao == 'INICIADO') {
			$("#trDataHoraInicio").show();
			$("#trDataHoraFim").hide();
	    	$('#trMotivoCancelamento').hide();
		}
		else if (situacao == 'REALIZADO') {
			$("#trDataHoraInicio").show();
			$("#trDataHoraFim").show();
	    	$('#trMotivoCancelamento').hide();
		}
		else if (situacao == 'CANCELADO') {
			$("#trDataHoraInicio").hide();
			$("#trDataHoraFim").hide();
	    	$('#trMotivoCancelamento').show();
	    }
		else if (situacao == 'AGENDADO') {
			$("#trDataHoraInicio").hide();
			$("#trDataHoraFim").hide();
	    	$('#trMotivoCancelamento').hide();
		}
		
		if ($("#servicoVeiculoId").val() == '0') {
			$("#lstVeiculos").removeAttr("disabled");
			$("#lstTiposDeServico").removeAttr("disabled");
		}
		else {
			$("#lstVeiculos").attr("disabled","true");
			$("#lstTiposDeServico").attr("disabled","true");
		}
	}

	$(document).ready(function() {
		verificarSituacao();
		verificarAvarias();
		retornarSituacaoInicial();
		
		$("#situacaoServico").change(function(event) {
			event.preventDefault();
			verificarSituacao();
		});

		$("#btnSalvar").click(function() {
			$("#frmServicos").attr("action",$("#action").val());
			verificarAvarias();
			$("#frmServicos").submit();
			return false;
		});
	});

	function retornarSituacaoInicial() {
		var situacao = $("#situacaoServico").val(); 

		if ($("#estadoServicoInicial").val() == "") {
			$("#estadoServicoInicial").val(situacao);
		}
	}
	
	function move(MenuOrigem, MenuDestino){
	    var arrMenuOrigem = new Array();
	    var arrMenuDestino = new Array();
	    var arrLookup = new Array();
	    var i;
	    for (i = 0; i < MenuDestino.options.length; i++){
	        arrLookup[MenuDestino.options[i].text] = MenuDestino.options[i].value;
	        arrMenuDestino[i] = MenuDestino.options[i].text;
	    }
	    var fLength = 0;
	    var tLength = arrMenuDestino.length;
	    for(i = 0; i < MenuOrigem.options.length; i++){
	        arrLookup[MenuOrigem.options[i].text] = MenuOrigem.options[i].value;
	        if (MenuOrigem.options[i].selected && MenuOrigem.options[i].value != ""){
	            arrMenuDestino[tLength] = MenuOrigem.options[i].text;
	            tLength++;
	        }
	        else{
	            arrMenuOrigem[fLength] = MenuOrigem.options[i].text;
	            fLength++;
	        }
	    }
	    arrMenuOrigem.sort();
	    arrMenuDestino.sort();
	    MenuOrigem.length = 0;
	    MenuDestino.length = 0;
	    var c;
	    for(c = 0; c < arrMenuOrigem.length; c++){
	        var no = new Option();
	        no.value = arrLookup[arrMenuOrigem[c]];
	        no.text = arrMenuOrigem[c];
	        MenuOrigem[c] = no;
	    }
	    for(c = 0; c < arrMenuDestino.length; c++){
	        var no = new Option();
	        no.value = arrLookup[arrMenuDestino[c]];
	        no.text = arrMenuDestino[c];
	        MenuDestino[c] = no;
	   }
	}

	function verificarAvarias() {
		var x = 0;
		$('.container input[type=checkbox]').each(function() {
			var checkbox = $(this);
			if(checkbox.is(':checked')) {
				var _id = 'avarias[' + x + ']';
				var _valor = checkbox.attr("id").replace('avaria_','');
				var input = $('<input type="hidden">').attr('id',_id).attr('name',_id).val(_valor).appendTo('#frmServicos') ;
				x ++;
			}
		});
	}
</script>

<form id="frmServicos" method="post" enctype="multipart/form-data">
<input type="hidden" id="servicoVeiculoId" name="servico" value="${servico.id}" />
<input type="hidden" name="servico.dataHora" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHora.time}" />" class="dataHora" />
<input type="hidden" id="action" value="${linkTo[ServicoVeiculoController].salvar}"/>
	<div class="gt-content-box gt-form clearfix" >
		<div class="coluna margemDireitaG">
		    <c:if test="${servico.ultimaAlteracao != null}">
				<label for="servico.ultimaAlteracao">&Uacute;ltima Altera&ccedil;&atilde;o</label>
				<input  type="text" name="servico.ultimaAlteracao" 
						value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.ultimaAlteracao.time}" />"
						disabled="disabled" size="20" class="dataHora" />
			</c:if>
				<label for="servico.tiposDeServico" class= "obrigatorio">Tipo de Servi&ccedil;o</label>
				
				<select id="lstTiposDeServico" name="servico.tiposDeServico" >
					<c:forEach items="${tiposDeServico}" var="tipoDeServico">
						<option value="${tipoDeServico}" ${tipoDeServico == servico.tiposDeServico ? 'selected' : ''} >${tipoDeServico.descricao}</option>
					</c:forEach>
				</select>
								
				<label for="servico.veiculo" class= "obrigatorio">Ve&iacute;culo</label>	
				<select id="lstVeiculos" name="servico.veiculo">
					<c:forEach items="${veiculos}" var="veiculo">
						<option value="${veiculo.id}" ${veiculo.id == servico.veiculo.id ? 'selected' : ''}>${veiculo.dadosParaExibicao}</option>
					</c:forEach>
				</select>
				
				<label for="servico.dataHoraInicioPrevisto" class= "obrigatorio">In&iacute;cio Previsto</label>
				<input  type="text"
						name="servico.dataHoraInicioPrevisto"
						value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHoraInicioPrevisto.time}" />"
						size="16" class="dataHora" />
				<label for="servico.dataHoraFimPrevisto" class= "obrigatorio">T&eacute;rmino Previsto</label>
				<input  type="text" name="servico.dataHoraFimPrevisto"
						value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHoraFimPrevisto.time}" />"
						size="16" class="dataHora" />
				<label for="servico.descricao" class="obrigatorio">Descri&ccedil;&atilde;o</label>
				<sigatp:controleCaracteresTextArea nomeTextArea="servico.descricao" rows="6" cols="68" valorTextArea="${servico.descricao}" />
				<label for="situacaoServico" class= "obrigatorio">	Situa&ccedil;&atilde;o</label>
				<select id="situacaoServico" name="servico.situacaoServico" >
					<c:forEach items="${estadosServico}" var="estadoServico">
						<option value="${estadoServico}" ${estadoServico == servico.situacaoServico ? 'selected' : ''} >${estadoServico.descricao}</option>
					</c:forEach>
				</select>
				
				<input type="hidden" id="estadoServicoInicial" name="servico.estadoServicoInicial" readonly="readonly"
				 	     value="${servico.estadoServicoInicial}" />
		</div>

		<div class="coluna">
			<div id="trAvarias" class="clearfix">  
				<label for="lstAvarias">Selecione as Avarias finalizadas</label>
				<div class="container" id="lstAvarias">
					<c:forEach items="${avarias}" var="avaria">
						<input type="checkbox" name="chk" id="avaria_${avaria.id}" />
						<tptags:link texto="${avaria.descricao}" parteTextoLink="${avaria.descricao}" comando="${linkTo[AvariaController].editar(servico.veiculo.id,avaria.id,false)}"></tptags:link>
		    			<br/>
					</c:forEach>
				</div>
			</div>			

			<c:if test="${servico.id != 0}">
			
				<div id="trMotivoCancelamento" class="clearfix">				
					<label for="servico.motivoCancelamento" class= "obrigatorio">Motivo do Cancelamento</label>
					<sigatp:controleCaracteresTextArea idTextArea="motivoCancelamento" nomeTextArea="servico.motivoCancelamento" rows="8" cols="68" valorTextArea="${servico.motivoCancelamento}" />
				</div>
				<div id="trDataHoraInicio" class="clearfix">				
					<label for="dataHoraInicio">In&iacute;cio</label>
					<input  type="text" id="dataHoraInicio" name="servico.dataHoraInicio"
						value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHoraInicio.time}" />" 
						size="16" class="dataHora" />
				</div>
				<div id="trDataHoraFim" class="clearfix">				
					<label for="dataHoraFim">Fim</label>
					<input  type="text" id="dataHoraFim" name="servico.dataHoraFim"
					    value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHoraFim.time}" />" 
						size="16" class="dataHora" />
				</div>
			</c:if>	
		</div>
	</div>
	
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">	
		<input id="btnSalvar" type="button" value="<fmt:message key="views.botoes.salvar"/>"class="gt-btn-medium gt-btn-left"/>
		<input id="btnCancelar" type="button" value="<fmt:message key="views.botoes.cancelar"/>" onClick="javascript:location.href='${linkTo[ServicoVeiculoController].listar}'" class="gt-btn-medium gt-btn-left" />
	</div>
</form>
