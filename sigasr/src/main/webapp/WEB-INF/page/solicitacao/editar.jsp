<%@page import="br.gov.jfrj.siga.sr.model.SrPrioridade"%>
<%@page import="br.gov.jfrj.siga.sr.model.SrTendencia"%>
<%@page import="br.gov.jfrj.siga.sr.model.SrUrgencia"%>
<%@page import="br.gov.jfrj.siga.sr.model.SrGravidade"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:set var="gravidade_normal">
    <%=SrGravidade.NORMAL.name()%>
</c:set>

<c:set var="urgencia_normal">
    <%=SrUrgencia.NORMAL.name()%>
</c:set>

<c:set var="tendencia_piora">
    <%=SrTendencia.PIORA_MEDIO_PRAZO.name()%>
</c:set>

<c:set var="prioridade_planejado">
    <%=SrPrioridade.PLANEJADO.getDescPrioridade()%>
</c:set>

<style>
.barra-subtitulo {
	color: #365b6d !important;
	border-bottom: 1px solid #ccc;
	border-radius: 0 !important;
	margin: 0 -15px 10px -15px;
}

.barra-subtitulo-top {
	border-radius: 5px 5px 0 0 !important;
	margin-top: -15px !important;
}

.tempo h3 {
	color: #365b6d;
	font-weight: normal;
	margin-bottom: 0px;
	font-size: 115.0%;
	border: 0;
}
</style>

<siga:pagina titulo="Cadastro de Solicita��o">
	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script>
		jQuery(document).ready(function($) {
			$('#gravar').click(function() {
				if ($('#siglaCadastrante').val() != $('#formulario_solicitacaosolicitante_sigla')[0].value) {
					var stringDt= $('#calendarioComunicacao').val() + ' ' + $('#horarioComunicacao').val();
					$('#stringDtMeioContato').val(stringDt);
				} 
			}); 
	
			if($('#formulario_solicitacaointerlocutor_id').val() != "") {
				$('#checkmostrarInterlocutor').prop('checked', true);	
				$('#interlocutor').show();		
			}
	
			$('#checkRascunho').change(function() {
				if(this.checked) {
					$('#checkRascunho').prop('value', 'true');
					return;
				}
				$('#checkRascunho').prop('value', 'false');
			});
	
			carregarFiltrosAoIniciar();
	
			// DB1: Sobrescreve o método onchange para que faça o tratamento das informações 
			// do campo vinculado ao evento
			jQuery("#checkmostrarInterlocutor")[0].onchange = function(event) {
				changemostrarInterlocutor();
				showInterlocutor(this.checked, 'interlocutor');
			};
		});
	
		function showInterlocutor(checked, divName){
			var div = document.getElementById(divName);
			if (div) {
				if (checked)
					div.style.display = 'inline';
				else {
					div.style.display = 'none';
					jQuery("#formulario_solicitacaointerlocutor_id").val("");
					jQuery("#formulario_solicitacaointerlocutor_descricao").val("");
					jQuery("#formulario_solicitacaointerlocutor_sigla").val("");
					jQuery("#solicitacaointerlocutorSpan").html("");
				}
			}
		}
	
		function formatarValorTimer(num) {
			if (num.toString().length == 1) 
	      		return "0" + num;
	
	  		return num;
		};
		
		function carregarLocalRamalEMeioContato() {
			//jQuery.blockUI(objBlock);
			frm = document.getElementById('formSolicitacao');
			params = '';
			for (i = 0; i < frm.length; i++){
				if (frm[i].name && frm[i].value) {
					if (params != '')
						params = params + '&';
				
					params = params + frm[i].name + '=' + escape(frm[i].value);
				}
			}

			var url = '${linkTo[SolicitacaoController].exibirLocalRamalEMeioContato}?' + params;
			Siga.ajax(url, null, "GET", function(response){
				carregouLocalRamalEMeioContato(response);
			});
		//	PassAjaxResponseToFunction(, 'carregouLocalRamalEMeioContato', null, false, null);
		}
		
		function carregouLocalRamalEMeioContato(response){
			var div = document.getElementById('divLocalRamalEMeioContato');
			div.innerHTML = response;
					
			$("#calendarioComunicacao").datepicker({
		        showOn: "button",
		        buttonImage: "/siga/css/famfamfam/icons/calendar.png",
		        buttonImageOnly: true,
		        dateFormat: 'dd/mm/yy'
		    });
			$("#calendarioComunicacao").mask("99/99/9999");
			$("#horarioComunicacao").mask("99:99");
	
			var scripts = div.getElementsByTagName("script");
			for(var i=0;i<scripts.length;i++)  
				   eval(scripts[i].text); 
			//jQuery.unblockUI();
		}
		
		function carregarItem(){
			jQuery.blockUI(objBlock);
			frm = document.getElementById('formSolicitacao');
			params = '';
			for (i = 0; i < frm.length; i++){
				if (frm[i].name && frm[i].value)
					params = params + frm[i].name + '=' + escape(frm[i].value) + '&';
			}
			var url = '${linkTo[SolicitacaoController].exibirItemConfiguracao['+params+']}';
			Siga.ajax(url, null, "GET", function(response){		
				carregouItem(response);
			});
			
			//PassAjaxResponseToFunction( + params, 'carregouItem', null, false, null);
		}
		
		function carregouItem(response){
			var div = document.getElementById('divItem');
			div.innerHTML = response;
			var scripts = div.getElementsByTagName("script");
			for(var i=0;i<scripts.length;i++)  
			   eval(scripts[i].text);  
			jQuery.unblockUI();
		}
	
		function carregarConhecimentosRelacionados(){
			//jQuery.blockUI(objBlock);
			frm = document.getElementById('formSolicitacao');
			params = '';
			for (i = 0; i < frm.length; i++){
				if (frm[i].name && frm[i].value)
					params = params + frm[i].name + '=' + escape(frm[i].value) + '&';
			}
			var url = '${linkTo[SolicitacaoController].exibirConhecimentosRelacionados['+params+']}';
			Siga.ajax(url, null, "GET", function(response){		
				carregouConhecimentosRelacionados(response);
			});
			//PassAjaxResponseToFunction('@{Application.exibirConhecimentosRelacionados()}?' + params, 'carregouConhecimentosRelacionados', null, false, null);
		}
		
		function carregouConhecimentosRelacionados(response){
			var div = document.getElementById('divConhecimentosRelacionados');

			if (div) {
				div.innerHTML = response;
				var scripts = div.getElementsByTagName("script");
				for(var i=0;i<scripts.length;i++)  
				   eval(scripts[i].text);  
				//jQuery.unblockUI();
			}
		}
	
		function carregarPrioridade() {
			gravidade 	= document.getElementById('gravidade');
			urgencia 	= document.getElementById('urgencia');
			tendencia 	= document.getElementById('tendencia'); 
			params = '';
			params = gravidade.name + '=' + escape(gravidade.value) + '&' + 
						urgencia.name + '=' + escape(urgencia.value) + '&' +
						tendencia.name + '=' + escape(tendencia.value) + '&';
			
			var url = '${linkTo[SolicitacaoController].exibirPrioridade['+params+']}';
			Siga.ajax(url, null, "GET", function(response){		
				$("#divPrioridade").html(response);
			});					
	 	}
		
		// funções usadas para solicitações relacionadas
		function addFiltro(divFiltro, campoRef, optionHtml, optionVl) {
			/**
			 * Se o valor foi preenchido e se existe label
			 */
			if($(campoRef).val() != "" && $(campoRef).val() != undefined && optionHtml) {
				var divFiltroExistente = $("div[identificadorFiltro='" + optionVl + "']");
				/**
				 * Se jah existe uma div para esse filtro, apenas atualiza a checkbox
				 */
				if(divFiltroExistente.size() > 0) {
					var span = divFiltroExistente.find('span'),
						checkbox = divFiltroExistente.find(':checkbox')
					
					span.html(optionHtml.trim());
					checkbox.attr('checked', true);
				}
				/** 
				 * Caso nao exista a div, entao cria uma nova checkbox
				 */
				else {
					var div = $('<div style="clear:both">');
					div.attr('identificadorFiltro', optionVl);
					
					var input = $('<input class="filtro-sol-relacionadas" type="checkbox">');
					input.attr('name', optionVl);
					input.bind('change', carregarSolRelacionadas);
					input.attr('checked', true);
					input.attr('disabled', $('#bodySolRelacionadas').attr('requesting'));
	
					var label = $('<span style="margin-left:5px">');
					label.html(optionHtml.trim());
				
					div.append(input);
					div.append(label);
					
					divFiltro.append(div);
				}	
				return true;
			}
			return false;
		}
	
		function existeFiltroSelecionavel() {
			return $("#filtro .filtro-sol-relacionadas").size() > 0;
		}
		
		function notificarCampoMudou(campoRef, tipoCampo, optionVl) {
			var label = descricaoFiltroSolRelacionadas(campoRef, tipoCampo),
				divFiltro = $('#filtro');
			
			if(!addFiltro(divFiltro, campoRef, label, optionVl)) {
				div = divFiltro.find("div[identificadorFiltro='" + optionVl + "']");
				div.remove();
			}
			validarCadastranteSolicitante();
			if(!existeFiltroSelecionavel()) {
				$('#solicitacoesRelacionadas').hide(300);
			} else {
				$('#solicitacoesRelacionadas').show(300);
			}
		}
	
		// Recebe notificacao de alteracao em campo que eh atributo
		function notificarCampoAtributoMudou(campoRef, tipoCampo, optionVl, teste) {
			var label = $(campoRef).prev('label').html().trim();
	
			notificarCampoMudou(campoRef, label, optionVl);
			carregarSolRelacionadas();
		}
		
		function descricaoFiltroSolRelacionadas(campoRef, tipoCampo) {
			var campo = $(campoRef);
	
			if(campo.size() > 0) {
				// Se o filtro eh uma select, entao pega o valor selecionado na select
				if(campo[0].tagName == 'SELECT') {
					return tipoCampo + ' - ' + campo.find('option:selected').html().trim();
				}
				else {
					// Se o campo for um componente de selecao, entao pega o valor selecionado (estara em um span gerado pelo componente)
					var span = $(campoRef + 'Span');
					if(span.size() > 0) {
						return tipoCampo + ' - ' + span.html();
					} 
					// Senao, pega o valor do campo
					else {
						return tipoCampo + ' - ' + campo.val();
					}
				}
			}
			return null;
		}
		
		// Valida se o cadastrante é o solicitante, e neste caso irá ocultar
		// alguns campos na tela
		function validarCadastranteSolicitante() {
			var siglaCadastrante = $('#siglaCadastrante').val();
			var siglaSolicitante = $('#formulario_solicitacaosolicitante_sigla')[0].value;
	
			if (siglaCadastrante == siglaSolicitante) {
				$('#spanInterlocutor')[0].style.display='none';
				$('#checkmostrarInterlocutor')[0].checked=false;
				$('#checkmostrarInterlocutor')[0].onchange();
				$('#meioComunicacao')[0].style.display='none';
			}
			else {
				$('#spanInterlocutor')[0].style.display='inline';
				$('#meioComunicacao')[0].style.display='inline';
			}
		}
		
		function carregarSolRelacionadas() {
			var filtrosSelecionados = $('#filtro input:checked');
			
			if(filtrosSelecionados.size() > 0) {
				if(existeFiltroSelecionavel()) {
					var params = construirParametrosDoFiltro($('#formSolicitacao'));
					iniciarCarregarSolicitacoesRelacionadas();
					//jQuery.blockUI(objBlock);
					
					var url = '${linkTo[SolicitacaoController].listarSolicitacoesRelacionadas['+params+']}';
					Siga.ajax(url, null, "GET", function(response){
						carregouSolicitacoesRelacionadas(response);
					});				
				}
			}
			// Senao, apenas mostra informacao
			else {
				$('#bodySolRelacionadas').html("<tr><td colspan='2' style='text-align: center;''>Selecione um filtro para realizar a pesquisa</td></tr>");
			}
		}
	
		function construirParametrosDoFiltro(frm) {
			var authField = frm.find('input:nth(0)');
			var params = authField.attr('name') + '=' + encodeURI(authField.val()) + '&';
	
			// Preenche a string que representa os parametros da requisicao
			$('#filtro input:checked').each(function() {
				var input = frm.find("[name='" +  $(this).attr('name') + "']");
				params = params + input.attr('name') + '=' + input.val() + '&'
			});
			
			params = params + 'apenasFechados' + '=' + $('#apenasFechados').val();
			return params;
		}
			
		function carregouSolicitacoesRelacionadas(response, param){
			if($('#filtro input.filtro-sol-relacionadas:checked').size() > 0) {
				$('#bodySolRelacionadas').html(response);
			}
			$('#filtro input').attr('disabled', false);
			$('#bodySolRelacionadas').attr('requesting', false);
			//jQuery.unblockUI();
		}
	
		function iniciarCarregarSolicitacoesRelacionadas() {
			$('#filtro input').attr('disabled', true);
			$('#bodySolRelacionadas').html("<tr><td colspan='2' style='text-align: center;''>Por favor aguarde. Carregando... </td></tr>");
			$('#bodySolRelacionadas').attr('requesting', true);
		}
	
		function carregarFiltrosAoIniciar() {
			var divFiltro = $('#filtro');
			
			addFiltroAoIniciar(divFiltro, '#formulario_solicitacaosolicitante_id', 'Solicitante', 'solicitacao.solicitante');
			addFiltroAoIniciar(divFiltro, '#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao');
			addFiltroAoIniciar(divFiltro, '#selectAcao', 'A&ccedil;&atilde;o', 'solicitacao.acao');
	
			carregarFiltrosAtributos();
			
			if(existeFiltroSelecionavel()) {
				carregarSolRelacionadas();
			} else {
				$('#solicitacoesRelacionadas').hide();
			}
		}
	
		function carregarFiltrosAtributos() {
			var atributos = $('#atributos').find('div'),
				atrClass, atrHtml, atrName;
			
			for(j=0; j < atributos.length; j++) {
				var divAtributo = atributos[j], 
					inputAtributo = atributos[j].children[1],
					labelAtributo = divAtributo.firstElementChild;
				
				if (inputAtributo != undefined) {
					atrClass = '.' + inputAtributo.className.split(' ')[0];
					atrHtml = labelAtributo.innerHTML + ' - ' + inputAtributo.value;
					atrName = inputAtributo.name;
	
					// Se for select, busca pelo atributo selecionado
					if (inputAtributo.tagName == 'SELECT') {
						atrHtml = atrHtml + ' - ' + $(inputAtributo).find('option:selected').html().trim();
					}
					addFiltro($('#filtro'), atrClass, atrHtml, atrName);
				}
			}
		}
		
	
		function removerFiltrosSemCampo() {
			var existeSelecionado = false;
			
			$('#filtro :checkbox').each(function() {
				var me = $(this),
					name = me.attr('name'),
					input = $("form [name='" + name + "']");
				
				// Se o campo foi limpo, tira ele da lista de filtros
				if(input.size() == 0 || input.val() == '' || input.val() == undefined) {
					me.parent('div').remove();
	
					if(!existeSelecionado) {
						existeSelecionado = me.is(':checked');
					}
				}
			});
		}
	
		function addFiltroAoIniciar(divFiltro, campoRef, optionHtml, optionVl) {
			var label = descricaoFiltroSolRelacionadas(campoRef, optionHtml);
			addFiltro(divFiltro, campoRef, label, optionVl, true);
		}
	
		function exibirSolicitacao(idSolicitacao) {
			var params = 'id=' + idSolicitacao;
			window.open('${linkTo[SolicitacaoController].exibir['+ params +']}',"_blank");
		}
		
		function verMais(){
			/*frm = document.getElementById('formSolicitacao');
			comboVl = $('#filtro').val();
			params = '';
			params = params + frm[0].name+ '=' + encodeURI(frm[0].value) + '&';
			params = params + 'filtro.pesquisar=true&';
			
			if(comboVl == 'solicitacao.lotaSolicitante'){
				params = params + 'carregarLotaSolicitante=true&';
				comboVl = 'solicitacao.solicitante';			
			}
			for (i = 1; i < frm.length; i++){
				if(frm[i].name == comboVl)
					if (frm[i].name && frm[i].value){
						params = params + frm[i].name + '=' + encodeURI(frm[i].value);
						break;
					}
			} */
			var params = construirParametrosDoFiltro($('#formSolicitacao')) + '&filtro.pesquisar=true&';
			// Replace All
			while(params.indexOf('solicitacao') >= 0) {
				params = params.replace("solicitacao", "filtro");
			}
			window.open('${linkTo[SolicitacaoController].buscar}?'+ params,"_blank");
		}
		
		function formatarValorTimer(num) {
			if (num.toString().length == 1) 
	      		return "0" + num;
	
	  		return num;
		};
	</script>
	
	<div class="gt-bd gt-cols clearfix">
		<div class="gt-content clearfix">
			<h2>
				<c:choose>
					<c:when test="${solicitacao.idSolicitacao != null || solicitacao.solicitacaoPai != null}">
						${solicitacao.codigo}
					</c:when>
					<c:otherwise>
						Cadastro de Solicita&ccedil;&atilde;o
					</c:otherwise>
				</c:choose>
			</h2>
			<div class="gt-content-box gt-for-table gt-form" style="margin-top: 15px;">
	
				<form action="${linkTo[SolicitacaoController].gravar}" 
					enctype="multipart/form-data" id="formSolicitacao" onsubmit="javascript:return block();"> 
					<c:if test="${solicitacao.solicitacaoPai != null}">
						<input type="hidden" name="solicitacao.solicitacaoPai.idSolicitacao" 
						value="${solicitacao.solicitacaoPai.idSolicitacao}" /> 
					</c:if>
					
					<input type="hidden" name="solicitacao.idSolicitacao" id="idSol" value="${solicitacao.idSolicitacao}" /> 
					<input type="hidden" name="solicitacao.dtIniEdicaoDDMMYYYYHHMMSS" value="${solicitacao.dtIniEdicaoDDMMYYYYHHMMSS}" /> 
					<input type="hidden" name="solicitacao.numSolicitacao" value="${solicitacao.numSolicitacao}" />
					<input type="hidden" name="solicitacao.numSequencia" value="${solicitacao.numSequencia}" />
					
					<div class="gt-form-table">
						<div class="barra-subtitulo barra-subtitulo-top header" align="center" valign="top">
							Dados B&aacute;sicos
						</div>
					</div>
		
<%-- 					#{ifErrors} --%>
<!-- 					<p class="gt-error">Alguns campos obrigat&oacute;rios n&atilde;o foram -->
<%-- 						preenchidos ${error}</p> --%>
<%-- 					#{/ifErrors} --%>
					<div class="gt-form-row box-wrapper">
						<div class="gt-form-row gt-width-66">
							<label>Cadastrante</label> ${cadastrante.nomePessoa} <input
								type="hidden" id="siglaCadastrante" name="solicitacao.cadastrante.sigla"
								value="${cadastrante.sigla}" />
								<input type="hidden" id="idCadastrante" name="solicitacao.cadastrante" value="${cadastrante.idPessoa}" />
								<input type="hidden" name="solicitacao.lotaTitular" value="${lotaTitular != null ? lotaTitular.idLotacao:''}" />
								<input type="hidden" name="solicitacao.titular" value="${titular!= null ? titular.idPessoa:''}" />
						</div>
					</div>	 
		
					<div class="gt-form-row gt-width-66">
						<label>Solicitante</label> 
						<siga:selecao2 tipo="pessoa"
							propriedade="solicitacao.solicitante"
							tema="simple"
							modulo="siga"
							onchange="carregarLocalRamalEMeioContato();carregarItem();
								notificarCampoMudou('#formulario_solicitacaosolicitante_id', 'Solicitante', 'solicitante')" />
						<span style="margin-left: 10px;" id="spanInterlocutor">
							<siga:checkbox name="mostrarInterlocutor" value="false" depende="interlocutor"/>Interlocutor
						</span>
<%-- 						<span style="color: red">#{error 'solicitante' /}</span> --%>
					</div>
					<div class="gt-form-row gt-width-66" id="interlocutor"
						style="display: none;">
						<label>Interlocutor</label> 
						<siga:selecao2 tipo="pessoa"
							propriedade="solicitacao.interlocutor" 
							tema="simple"
							modulo="siga"/>
					</div>
					
					<div id="divLocalRamalEMeioContato">
						<jsp:include page="exibirLocalRamalEMeioContato.jsp" />
					</div>
		
					<div class="gt-form-row gt-width-66">
						<label>Quando deseja receber notifica&ccedil;&atilde;o dessa solicita&ccedil;&atilde;o por e-mail?</label>
						<siga:select name="solicitacao.formaAcompanhamento"
						list="formaAcompanhamentoList"
						listValue="descrFormaAcompanhamento" listKey="idFormaAcompanhamento"
						value="${solicitacao.formaAcompanhamento}" />
					</div>	
						
					<div class="gt-form-table">
						<div class="barra-subtitulo header" align="center" valign="top">
							Detalhamento
						</div>
					</div>
		
					<div id="divItem">
						<jsp:include page="exibirItemConfiguracao.jsp" />
					</div>
		
					<div class="gt-form-row gt-width-66">
						<label>Descrição</label>
						<textarea cols="85" rows="10" name="solicitacao.descrSolicitacao"
							id="descrSolicitacao" maxlength="8192">${solicitacao.descrSolicitacao}</textarea>
<%-- 						<span style="color: red">#{error 'descrSolicitacao' /}</span> --%>
					</div>
					<div class="gt-form-row gt-width-66">
						<label>Anexar arquivo</label> <input type="file"
							name="arquivo" />
					</div>
		
					<div class="gt-form-table">
						<div class="barra-subtitulo header" align="center" valign="top">
							Prioridade</div>
					</div>
					<div class="gt-form-row box-wrapper">
						<div class="gt-form-row gt-width-33">
							<label>Gravidade</label> 
							<siga:select name="solicitacao.gravidade" id="gravidade" list="gravidadeList" listValue="respostaEnunciado" listKey="nivelGravidade" isEnum="true"
							value="${solicitacao.gravidade ? solicitacao.gravidade: gravidade_normal}" onchange="carregarPrioridade()"
							style="width:235px"  />
						</div>
						<div class="gt-form-row gt-width-33">
							<label>Urg&ecirc;ncia</label> 
							<siga:select name="solicitacao.urgencia" id="urgencia" list="urgenciaList" listValue="respostaEnunciado" listKey="nivelUrgencia" isEnum="true"
							value="${solicitacao.urgencia ? solicitacao.urgencia: urgencia_normal}" 
							onchange="carregarPrioridade()" style="width:235px" />
						</div>
						<div class="gt-form-row gt-width-33">
							<label>Tend&ecirc;ncia</label>
							<siga:select name="solicitacao.tendencia" id="tendencia" list="tendenciaList" listValue="respostaEnunciado" listKey="nivelTendencia" isEnum="true"
							value="${solicitacao.tendencia ? solicitacao.tendencia: tendencia_piora}"
							onchange="carregarPrioridade()" style="width:235px;"/>
						</div>
					</div>
					<div id="divPrioridade" class="gt-form-row gt-width-66">
						<label style="float: left">Prioridade: &nbsp;</label>
						<span>${solicitacao.prioridade != null ? solicitacao.prioridade.descPrioridade : prioridade_planejado}</span>
<%-- 							<siga:select name="prioridade" id="prioridade" list="prioridadeList" listValue="descPrioridade" listKey="idPrioridade" isEnum="true"  --%>
<%-- 							value="${solicitacao.prioridade ? solicitacao.prioridade: SrPrioridade.PLANEJADO.descPrioridade}" style="width:235px;border:none;display:none;"/> --%>
							<br />
					</div>
		
		
					<c:if test="${solicitacao.podeAbrirJaFechando(titular, lotaTitular)}">
						<div class="gt-form-row gt-width-66">
							<label><siga:checkbox name="solicitacao.fecharAoAbrir" value="${solicitacao.fecharAoAbrir}"
									depende="motivoFechamento" disabled="alterando"/>Fechar o chamado logo ap&oacute;s a abertura</label>
						</div>
					</c:if>
		
					<div class="gt-form-row gt-width-66" id="motivoFechamento"
						style="display: none">
						<label>Motivo do fechamento</label> <input type="text" size="100"
							name="motivoFechamentoAbertura"
							id="motivoFechamentoAbertura" />
					</div>
					<c:choose>
						<c:when test="${!solicitacao.jaFoiDesignada()}">
							<br />
							<div class="gt-form-row">
								<label> <siga:checkbox name="solicitacao.rascunho"
									value="${solicitacao.rascunho}"/> Rascunho </label>
							</div>
						</c:when>
						<c:when test="${solicitacao.isPai() && solicitacao.idSolicitacao != null}">
							<div class="gt-form-table">
								<div class="barra-subtitulo header" align="center" valign="top"> Fechamento Automático</div>
							</div>
							<p> <siga:checkbox name="solicitacao.fechadoAutomaticamente"
								value="${solicitacao.fechadoAutomaticamente}"/>Fechar automaticamente a solicitação <b>${solicitacao.codigo}</b>.</p>
							<br />
						</c:when>
					</c:choose>
					<div class="gt-form-row">
						<input type="submit" value="Gravar"
							class="gt-btn-medium gt-btn-left" id="gravar" /> <a
							href="${linkTo[SolicitacaoController].buscar}" class="gt-btn-medium gt-btn-left">Cancelar</a>
					</div>
				</form>
			</div>
		</div>
<!-- 		paginas n�o migradas -->
<%-- 		<jsp:include page="exibirCronometro.jsp"/> --%>
<%-- 		<jsp:include page="exibirPendencias.jsp /> --%>
		
		<%--
		<div class="gt-sidebar">
			<div class="gt-sidebar-content" id="solicitacoesRelacionadas">
				<h3>Solicita&ccedil;&otilde;es relacionadas <a href="#" onclick="verMais()">[Ver Mais]</a></h3>
				<div class="gt-content-box gt-form">
					<label>Filtro</label>
	
					<div id="filtro">
						<span><siga:checkbox name="apenasFechados"
						value="apenasFechados"/> Apenas Fechados</span>
					</div>
	
					<div class="gt-content-box "
						style="margin: 10px -16px -6px -16px; border-radius: 0 0 5px 5px !important;">
						<table border="0" width="100%" class="gt-table">
							<colgroup>
								<col width="35%" />
								<col width="65%" />
							</colgroup>
							<thead>
								<tr>
									<th>C&oacute;digo</th>
									<th>Teor</th>
								</tr>
							</thead>
							<tbody id="bodySolRelacionadas">
							<jsp:include page="listarSolicitacoesRelacionadas.html"/>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		--%>
		
		<div id="divConhecimentosRelacionados">
		</div>
	</div>
</siga:pagina>