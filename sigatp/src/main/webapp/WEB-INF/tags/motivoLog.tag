<%@ tag body-content="empty"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<link rel="stylesheet" type="text/css" href="/sigatp/public/stylesheets/dialogForm.css">
<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<div id="dialog-form" title="Motivo Log" class="gt-content-box gt-form clearfix">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">	
			<sigatp:erros/>
			<label for="motivo" class="obrigatorio">Motivo</label>
			<sigatp:controleCaracteresTextArea idTextArea="motivo" nomeTextArea="motivo" rows="8" cols="68" />
		</div>
		<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	</div>
</div>

<script>
	$(function(e) {	
		var formulario = $("#formulario"),
		motivo = $("#motivo"),
		divErros = $("#divErros"),
		valid = null,
		dialogForm = $("#dialog-form");
		
		$("#dialog-form").dialog( {      
			autoOpen: false,
			height: "auto",      
			width: "auto",      
			modal: true, 
			resizable:false,
			buttons: [
				{			
					click: function() {
						valid = (motivo.val() != "" && motivo.val() != null);

						if (!valid) {
							var validacaoRepetida = $("li:contains('<fmt:message key='views.erro.campoObrigatorio.fragmentoParaBusca'/>')").text() != ""; 

							if (!validacaoRepetida) {
							    divErros.show();
								divErros.append("<li><fmt:message key='views.erro.campoObrigatorio'/></li>");
							}
						}
						else {
							var input = $('<input>').attr('type', 'hidden').attr('name', 'motivoLog').val(motivo.val());
							formulario.append(input);
							var link = dialogForm.data('link');
					        formulario.attr('action',link);
					        formulario.attr('method','post');
					        formulario.submit();
						}

				        return valid;
					}
				},
				{
					click: function() {
						motivo.val('');
						dialogForm.dialog('close');
						return false;
					}
				}
			],
			close: function() {
				motivo.val('');
				dialogForm.dialog('close');
			},
			open: function(event, ui) { 
				$('#divErros li').remove();
				divErros.hide();
				var dialog = $(event.target).parents(".ui-dialog.ui-widget"),
				buttonPane = dialog.find(".ui-dialog-buttonpane"),
				buttons = dialog.find(".ui-dialog-buttonpane").find("button");

				$(buttonPane).addClass('gt-bd clearfix');
				
				for (i=0; i < buttons.length; i++) {
					if ($(buttons[i]).hasClass('ui-button-text-only')) {
						$(buttons[i]).removeClass('ui-button-text-only');
						$(buttons[i]).addClass('ui-button-text-icon-primary');
						var text = (i == 0 ? '<fmt:message key="views.botoes.confirmar"/>' : '<fmt:message key="views.botoes.cancelar"/>');
						$(buttons[i]).prepend('<span class="gt-btn-medium gt-btn-left">' + text + '</span>');
					}
				}
			} 
		});  
	});
</script>