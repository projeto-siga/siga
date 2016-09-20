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

<c:set var="propriedadeClean" value="${fn:replace(propriedade,'.','')}" />

<c:forEach var="parametro" items="${fn:split(paramList,';')}">
	<c:set var="p2" value="${fn:split(parametro,'=')}" />
	<c:if test="${not empty p2 and not empty p2[0]}">
		<c:set var="selecaoParams" value="${selecaoParams}&${p2[0]}=${p2[1]}" />
	</c:if>
</c:forEach>

<c:set var="urlBuscar" value="/app/${tipo}/buscar" />

<%-- <c:set var="tam" value="${requestScope[propriedadeSel].tamanho}" /> --%>
<c:set var="tam" value="${tamanho}" />
<c:set var="larguraPopup" value="600" />
<c:set var="alturaPopup" value="400" />

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
		newwindow_${propriedadeClean}.close();
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
 
self.newwindow_${propriedadeClean} = '';
self.popitup_${propriedadeClean} = function(sigla) {

	var url = '/${modulo}${urlBuscar}?propriedade=${propriedadeClean}&popup=true&sigla='+encodeURI($.trim(sigla)) +'${selecaoParams}';
	
	if (!newwindow_${propriedadeClean}.closed && newwindow_${propriedadeClean}.location) {
		newwindow_${propriedadeClean}.location.href = url;
	} else {
	
		var popW;
		var popH;
		
		<c:choose>
			<c:when test="${tam eq 'grande'}">
				 popW = screen.width*0.75;
				 popH = screen.height*0.75;
			</c:when>
			<c:otherwise>
				 popW = ${larguraPopup};
				 popH = ${alturaPopup};	
			</c:otherwise>
		</c:choose>
			var winleft = (screen.width - popW) / 2;
			var winUp = (screen.height - popH) / 2;	
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow_${propriedadeClean}=window.open(url,'${propriedadeClean}',winProp);
	}
	newwindow_${propriedadeClean}.opener = self;
	
	if (window.focus) {
		newwindow_${propriedadeClean}.focus()
	}
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
		Siga.ajax(url, null, "GET", function(response){		
			resposta_ajax_${propriedadeClean}(response);
		});	
		//PassAjaxResponseToFunction(url, 'resposta_ajax_${propriedadeClean}', false);
	}
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
<input type="text" name="${propriedade}.sigla"
	value="<c:out value="${f:evaluate(f:concat(propriedade,'.sigla'),requestScope)}"/>"
	id="formulario_${propriedadeClean}_sigla"
	onkeypress="return handleEnter(this, event)"
	onblur="javascript: ajax_${propriedadeClean}();"
	size="25" ${disabledTxt} />

<c:if test="${buscar != 'nao'}">
	<input type="button" id="${propriedadeClean}SelButton" value="..."
		onclick="javascript: popitup_${propriedadeClean}('');"
		${disabledBtn} theme="simple">
</c:if>

<c:if test="${ocultardescricao != 'sim'}">
	<span id="${propriedadeClean}Span"> <c:out
			value="${f:evaluate(f:concat(propriedade,'.descricao'),requestScope)}" />
	</span>
</c:if>

<c:if test="${tema != 'simple'}">
	</td>
	</tr>
</c:if>