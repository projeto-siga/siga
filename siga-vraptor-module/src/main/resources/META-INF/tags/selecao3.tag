<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="titulo" required="false"%>
<%@ attribute name="propriedade"%>
<%@ attribute name="reler" required="false"%>
<%@ attribute name="relertab" required="false"%>
<%@ attribute name="desativar" required="false"%>
<%@ attribute name="ocultardescricao" required="false"%>
<%@ attribute name="tema"%>
<%@ attribute name="tipo" required="false"%>
<%@ attribute name="modulo" required="false"%>
<%@ attribute name="idAjax" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="paramList" required="false"%>
<%@ attribute name="tamanho" required="false"%>
<%@ attribute name="checarInput" required="false"%>

<c:set var="propriedadeClean"
	value="${f:slugify(propriedade,true,true)}" />

<c:forEach var="parametro" items="${fn:split(paramList,';')}">
	<c:set var="p2" value="${fn:split(parametro,'=')}" />
	<c:if test="${not empty p2 and not empty p2[0]}">
		<c:set var="selecaoParams" value="${selecaoParams}&${p2[0]}=${p2[1]}" />
	</c:if>
</c:forEach>

<c:set var="urlBuscar" value="/app/${tipo}/buscar" />

<%-- <c:set var="tam" value="${requestScope[propriedadeSel].tamanho}" /> --%>
<c:set var="tam" value="${tamanho}" />
<c:set var="larguraPopup" value="800" />
<c:set var="alturaPopup" value="600" />

<style type="text/css">

.modal-selecao .modal-dialog ,

.modal-selecao .modal-content {
  border-radius: 0 !important;
  height: 100%;
}

.modal-selecao .modal-body  {
   max-height: 97%;
   height: 97%;
   overflow-y: auto; /*habilita o overflow no corpo da modal*/
}

.modal-selecao .embed-responsive  {
   max-height: 100%;
   height: 100%;
}


</style>

<script type="text/javascript">
$(document).ready(function($) {
	window.${propriedadeClean}_default = document.getElementsByName('${propriedade}.sigla')[0].value;
});

self.isValorInputMudou_${propriedadeClean} = function() {
	var inputValue = document.getElementsByName('${propriedade}.sigla')[0].value;
	if (inputValue != window.${propriedadeClean}_default) {
		return true;
	}
	return false;
}
	
self.retorna_${propriedadeClean} = function(id, sigla, descricao) {
    try {
		//newwindow_${propriedadeClean}.close();
    	document.getElementById('btnsenhaDialog${propriedadeClean}').click(); 
    } catch (E) {
    } finally {
    }
    
	document.getElementsByName('${propriedade}.id')[0].value = id;
	
	<c:if test="${ocultardescricao != 'sim'}">
		try {
			document.getElementsByName('${propriedade}.descricao')[0].value = descricao;
			document.getElementById('${propriedadeClean}Span').innerHTML = descricao;
		} catch (E) {
		}
	</c:if>
	
	document.getElementsByName('${propriedade}.sigla')[0].value = sigla;
	window.${propriedadeClean}_default = sigla; //atualiza valor do input para ser usado na function self.isValorInputMudou_
	
	<c:if test="${reler == 'sim'}">
		document.getElementsByName('req${propriedadeClean}')[0].value = "sim";
		sbmt();
	</c:if>
	
	<c:if test="${reler == 'ajax'}">
		sbmt('${empty idAjax ? propriedade : idAjax}');
	</c:if>

	${onchange}
}



//
//Provider: modal que simula window
//
var modalsimulawindow${propriedadeClean} = 	function(url) {
		try {
			var urlInterna = url;			
			// tabindex="-1" removido da div class="modal" para corrigir erro de Maximum call stack size exceeded no js.
			var senhaDialog${propriedadeClean}  = $(
					'<div class="modal modal-selecao" role="dialog" id="senhaDialog${propriedadeClean}">'
				+	'  <div class="modal-dialog modal-lg" role="document">'
				+	'    <div class="modal-content">'
				+	'    <div class="modal-header">'
				+   '           <img src="${uri_logo_siga_pequeno}" class="siga-modal__logo" alt="logo siga">'
				+	'	        <button type="button" id="btnsenhaDialog${propriedadeClean}" class="close  p-0  m-0  siga-modal__btn-close" aria-label="Close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>'				
				+   '    </div>'
				+	'      <div class="modal-body">'
				+	'	   	   <div class="embed-responsive embed-responsive-16by9">'
				+	'	   	      <iframe id="iframe${propriedadeClean}" class="embed-responsive-item" src="' + urlInterna + '" allowfullscreen></iframe>'
				+	'	  	   </div>'
				+	'	      </div>'
				+	'	    </div>'
				+	'	  </div>'
				+	'	</div>').modal();
			
	
			senhaDialog${propriedadeClean}.on('shown.bs.modal', function () {
			    $(this).find('iframe${propriedadeClean}').attr('src', urlInterna);
			});
			
			senhaDialog${propriedadeClean}.on('hidden.bs.modal', function () {
				$('#senhaDialog${propriedadeClean}').remove();
			});
		
			return "AGUARDE";
		} catch (Err) {
			return Err.description;
		}
	}



self.newwindow_${propriedadeClean} = '';
self.popitup_${propriedadeClean} = function(sigla) {

	var url = '/${modulo}${urlBuscar}?propriedade=${propriedadeClean}&modal=true&sigla='+encodeURI($.trim(sigla)) +'${selecaoParams}';


	newwindow_${propriedadeClean} = modalsimulawindow${propriedadeClean};
	
	modalsimulawindow${propriedadeClean}(url);

	return false;
}

self.resposta_ajax_${propriedadeClean} = function(response, d1, d2, d3) {
	var sigla = document.getElementsByName('${propriedade}.sigla')[0].value;
    var data = response.split(';');
    if (data[0] == '1')
	    return retorna_${propriedadeClean}(data[1], data[2], data[3]);
    retorna_${propriedadeClean}('', '', '');
    
    <c:choose>
		<c:when test="${buscar != 'nao'}">
			return popitup_${propriedadeClean}(sigla);
		</c:when>
		<c:otherwise>
			return;
		</c:otherwise>
	</c:choose>
}

self.ajax_${propriedadeClean} = function() {
	var sigla = $.trim(document.getElementsByName('${propriedade}.sigla')[0].value);
	var executarEventos = true;
	if (sigla == '') {
		return retorna_${propriedadeClean}('', '', '');
	}
	<c:if test="${checarInput == 'true'}">
		executarEventos = self.isValorInputMudou_${propriedadeClean}();
	</c:if>

	if (executarEventos) {
		var url = '/${modulo}/app/${tipo}/selecionar?propriedade=${propriedade}'+'${selecaoParams}';
		url = url + '&sigla=' + sigla;
		/* Siga.ajax(url, null, "GET", function(response){		
			resposta_ajax_${propriedadeClean}(response);
		}); */	
		$.get( url, function(response){
			resposta_ajax_${propriedadeClean}(response);
		});
		//PassAjaxResponseToFunction(url, 'resposta_ajax_${propriedadeClean}', false);
	}
}


// Retorna elemento contendo id do select:
self.get_${propriedadeClean}_by_id = function() {
	return document.getElementById("formulario_${propriedadeClean}_id");
}

// Limpa dados do select:
self.limpa_${propriedadeClean} = function() {
	document.getElementById('formulario_${propriedadeClean}_id').value = '';
	document.getElementById('formulario_${propriedadeClean}_sigla').value = '';
	document.getElementById('${propriedadeClean}Span').innerHTML = '';	
}

</script>

<c:if test="${tema != 'simple'}">
	<tr>
		<td>${titulo}</td>
		<td>
</c:if>
<c:choose>
	<c:when test="${desativar == 'sim'}">
		<c:set var="disabledTxt" value="disabled" />
		<c:set var="disabledBtn" value="disabled" />
	</c:when>
</c:choose>

<input type="hidden" name="alterouSel" value="" id="alterouSel" />
<input type="hidden" name="${propriedade}.id"
	value="<c:out value="${f:evaluate(f:concat(propriedade,'.id'),requestScope)}"/>"
	id="formulario_${propriedadeClean}_id" />
<input type="hidden" name="${propriedade}.descricao"
	value="<c:out value="${f:evaluate(f:concat(propriedade,'.descricao'),requestScope)}"/>"
	id="formulario_${propriedadeClean}_descricao" />
	
<div class="input-group">	
	<input type="text" name="${propriedade}.sigla"
		value="<c:out value="${f:evaluate(f:concat(propriedade,'.sigla'),requestScope)}"/>"
		id="formulario_${propriedadeClean}_sigla"
		onkeypress="return handleEnter(this, event)"
		onblur="javascript: ajax_${propriedadeClean}();"
		class="form-control" style="width: 1%;"
		${disabledTxt} />

	<c:if test="${buscar != 'nao'}">
		<div class="input-group-append">
			<input type="button" id="${propriedadeClean}SelButton" value="..."
				onclick="javascript: popitup_${propriedadeClean}('');"
				${disabledBtn} theme="simple" class="btn btn-secondary"
				style="border-bottom-right-radius: 0.25em;border-top-right-radius: 0.25em;">
		</div>
	</c:if>
	
	<c:if test="${ocultardescricao != 'sim'}">
		<div class="input-group-append ml-2" style="width: 70%;">
			<span class="form-control" style="overflow: hidden; white-space:nowrap; text-overflow:ellipsis;" 
				id="${propriedadeClean}Span">
			<c:out value="${f:evaluate(f:concat(propriedade,'.descricao'),requestScope)}"/></span>
		</div>
	</c:if>
</div>

<c:if test="${tema != 'simple'}">
	</td>
	</tr>
</c:if>