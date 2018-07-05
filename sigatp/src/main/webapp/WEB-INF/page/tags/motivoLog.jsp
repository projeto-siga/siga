<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<div id="dialog-form" title="Motivo Log" class="gt-form">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
	   		<label for="txtMotivoLog" class="obrigatorio">Motivo</label>
	   		<sigatp:controleCaracteresTextArea idTextArea="txtMotivoLog" nomeTextArea="txtMotivoLog" rows="8" cols="68" />
		</div>
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
</div>

<script>
	$(function(e) {
		$("#dialog-form").dialog( {      
			autoOpen: false,
			height: 400,      
			width: 350,      
			modal: true, 
			resizable:true,
			buttons: [
				{			
					text: "Confirmar",
					"class":"gt-table-buttons",
					click: function() {
						var $formulario = $("#formulario");
						var input = $("<input>")
			               .attr("type", "hidden")
			               .attr("name", "motivoLog").val($("#txtMotivoLog").val());
						$formulario.append($(input));
				        var $link = $(this).data("link");
				        $formulario.attr('action',$link);
				        $formulario.attr('method','post');
				        $formulario.submit();
				        return true;
					}
				},
				{
					text:"Cancelar",
					"class":"gt-table-buttons",
					click: function() {
						$(this).dialog("close");
						$("#txtMotivoLog").val("");
						return false;
					}
				}
			],
			close: function() {
				var $formulario = $("#formulario");
				$("#txtMotivoLog").val("");
				$(this).dialog("close");
			}
		});  
	});  
</script>