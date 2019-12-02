<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="titulo" required="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="propriedade"%>
<%@ attribute name="reler" required="false"%>
<%@ attribute name="relertab" required="false"%>
<%@ attribute name="desativar" required="false"%>
<%@ attribute name="buscar" required="false"%>
<%@ attribute name="ocultardescricao" required="false"%>
<%@ attribute name="tema"%>
<%@ attribute name="modulo" required="false"%>
<%@ attribute name="tipo" required="false"%>
<%@ attribute name="idAjax" required="false"%>
<%@ attribute name="paramList" required="false"%>
<%@ attribute name="idInicial" required="false"%>
<%@ attribute name="siglaInicial" required="false"%>
<%@ attribute name="descricaoInicial" required="false"%>
<%@ attribute name="inputName" required="false"%>
<%@ attribute name="urlAcao" required="false"%>
<%@ attribute name="urlSelecionar" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="onblur" required="false"%>
<%@ attribute name="prefix" required="false"%>
<%@ attribute name="matricula" required="false"%>

<%@ attribute name="requiredValue" required="false"%>

<c:if test="${propriedade != null}">
	<c:set var="propriedade" value="${fn:replace(propriedade,'.','')}" />
</c:if>

<c:if test="${requiredValue == null}">
	<c:set var="requiredValue" value="" />
</c:if>

<!-- A lista de par -->
<c:forEach var="parametro" items="${fn:split(paramList,';')}">
	<c:set var="p2" value="${fn:split(parametro,'=')}" />
	<c:if test="${not empty p2 and not empty p2[0]}">
		<c:set var="selecaoParams" value="${urlParams}&${p2[0]}=${p2[1]}" />
	</c:if>
</c:forEach>

<c:set var="propriedadeSel" value="${propriedade}Sel" />

<c:choose>
	<c:when test="${empty tipo}">
		<c:set var="acaoBusca"
			value="${requestScope[propriedadeSel].acaoBusca}" />
		<c:set var="tipoSel" value="" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="acaoBusca" value="/${tipo}" />
		<c:set var="tipoSel" value="_${tipo}" scope="request" />
	</c:otherwise>
</c:choose>

<c:set var="propriedadeTipoSel" value="${propriedade}${tipoSel}Sel" />
<c:choose>
	<c:when test="${empty inputName}">
		<c:set var="inputNameTipoSel" value="${propriedadeTipoSel}" />
	</c:when>
	<c:otherwise>
		<c:set var="inputNameTipoSel" value="${inputName}${tipoSel}Sel" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${empty prefix}">
		<c:set var="prefixSel" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="prefixSel" value="/${prefix}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${empty urlAcao}">
		<c:set var="urlBuscar" value="/app${prefixSel}${acaoBusca}/buscar" />
	</c:when>
	<c:otherwise>
		<c:set var="urlBuscar" value="/app${prefixSel}${acaoBusca}/${urlAcao}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${empty inputName}">
		<c:set var="spanName" value="${propriedade}${tipoSel}" />
	</c:when>
	<c:otherwise>
		<c:set var="spanName" value="${inputName}" />
	</c:otherwise>
</c:choose>

<c:set var="tam" value="${requestScope[propriedadeSel].tamanho}" />

<c:set var="larguraPopup" value="800" />
<c:set var="alturaPopup" value="600" />


<script type="text/javascript">

document.addEventListener("hello", function(event) { // (1)
    alert("Hello from " + event.target.tagName); // Hello from H1
});


self.retorna_${propriedade}${tipoSel} = function(id, sigla, descricao) {
    try {
		newwindow_${propriedade}.close();
    } catch (E) {
    } finally {
    }
    
	document.getElementsByName('${inputNameTipoSel}.id')[0].value = id;
	
	<c:if test="${ocultardescricao != 'sim'}">
		try {
			document.getElementsByName('${inputNameTipoSel}.descricao')[0].value = descricao;
			document.getElementById('${spanName}SelSpan').innerHTML = descricao;
		} catch (E) {
		}
	</c:if>
	
	document.getElementsByName('${inputNameTipoSel}.sigla')[0].value = sigla;
	
	<c:if test="${reler == 'sim'}">
		document.getElementsByName('req${inputNameTipoSel}')[0].value = "sim";
		document.getElementById('alterouSel').value='${propriedade}';
		sbmt();
	</c:if>
	
	<c:if test="${reler == 'ajax'}">
		sbmt('${empty idAjax ? propriedade : idAjax}');
	</c:if> 

}
 
 
<c:choose>
	<c:when test="${empty modulo}">
		<c:set var="urlPrefix" value="/" />
	</c:when>
	<c:otherwise> 
		<c:set var="urlPrefix" value="${modulo}" />
	</c:otherwise>
</c:choose>

self.newwindow_${propriedade} = '';
self.popitup_${propriedade}${tipoSel} = function(sigla) {

	var url = '/${urlPrefix}${urlBuscar}?propriedade=${propriedade}${tipoSel}&sigla='+encodeURI($.trim(sigla)) +'${selecaoParams}';
		
	if (!newwindow_${propriedade}.closed && newwindow_${propriedade}.location) {
		newwindow_${propriedade}.location.href = url;
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
		newwindow_${propriedade}=window.open(url,'${propriedade}${tipoSel}',winProp);
	}
	newwindow_${propriedade}.opener = self;
	
	if (window.focus) {
		newwindow_${propriedade}.focus()
	}
	return false;
}

self.resposta_ajax_${propriedade}${tipoSel} = function(response, d1, d2, d3) {
	var sigla = document.getElementsByName('${inputNameTipoSel}.sigla')[0].value;
    var data = response.split(';');
    if (data[0] == '1')
	    return retorna_${propriedade}${tipoSel}(data[1], data[2], data[3]);
    retorna_${propriedade}${tipoSel}('', '', '');
    
    <c:choose>
		<c:when test="${buscar != 'nao'}">
			return popitup_${propriedade}${tipoSel}(sigla);
		</c:when>
		<c:otherwise>
			return;
		</c:otherwise>
	</c:choose>
}

self.ajax_${propriedade}${tipoSel} = function() {
	var sigla = $.trim(document.getElementsByName('${inputNameTipoSel}.sigla')[0].value);
	if (sigla == '') {
		return retorna_${propriedade}${tipoSel}('', '', '');
	}
	<c:choose>
		<c:when test="${empty urlSelecionar}">
			var url = '/${urlPrefix}/app${acaoBusca}/selecionar?propriedade=${propriedade}${tipoSel}'+'${selecaoParams}';
		</c:when>
		<c:otherwise>
			var url = '/${urlPrefix}/app${acaoBusca}/${urlSelecionar}?propriedade=${propriedade}${tipoSel}'+'${selecaoParams}';
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty matricula}">
	url = url + '&matricula=${matricula}';
	</c:if>
	url = url + '&sigla=' + sigla;
	/*
	Siga.ajax(url, null, "GET", function(response){
		resposta_ajax_${propriedade}${tipoSel}(response);
	});	*/
	$.get( url, function(response){
		resposta_ajax_${propriedade}${tipoSel}(response);
	});

	//PassAjaxResponseToFunction(url, 'resposta_ajax_${propriedade}${tipoSel}', false);
	
}

</script>

<c:if test="${tema != 'simple'}">
	<div class="form-group">
		<label for="formulario_${inputNameTipoSel}_sigla"
			class="col-sm-2x col-form-labelx">${titulo}</label>
</c:if>
<c:choose>
	<c:when test="${desativar == 'sim'}">
		<c:set var="disabledTxt" value="disabled" />
		<c:set var="disabledBtn" value="disabled" />
	</c:when>
</c:choose>

<c:if test="${empty onblur}">
	<c:set var="onblur" value="${onchange}"></c:set>
</c:if>

<input type="hidden" name="req${inputNameTipoSel}" value=""
	id="formulario_req${inputNameTipoSel}" />
<input type="hidden" name="alterouSel" value="" id="alterouSel" />
<input type="hidden" name="${inputNameTipoSel}.id"
	value="<c:out value="${requestScope[propriedadeTipoSel].id}"/>"
	id="formulario_${inputNameTipoSel}_id" />
<input type="hidden" name="${inputNameTipoSel}.descricao"
	value="<c:out value="${requestScope[propriedadeTipoSel].descricao}"/>"
	id="formulario_${inputNameTipoSel}_descricao" />
<input type="hidden" name="${inputNameTipoSel}.buscar"
	value="<c:out value="${requestScope[propriedadeTipoSel].buscar}"/>"
	id="formulario_${inputNameTipoSel}_buscar" />
<div class="input-group">
	<input type="search" name="${inputNameTipoSel}.sigla"
		value="<c:out value="${requestScope[propriedadeTipoSel].sigla}"/>"
		id="formulario_${inputNameTipoSel}_sigla"
		onkeypress="return handleEnter(this, event)" ${requiredValue}
		onblur="javascript: ajax_${propriedade}${tipoSel}();"
		<c:if test="${not empty onblur}">${onblur};</c:if>
		onchange="<c:if test="${not empty onchange}">javascript: ${onchange};</c:if>"
		class="form-control" style="width: 1%;" ${disabledTxt} />
	<c:if test="${buscar != 'nao'}">
		<div class="input-group-append">
			<input type="button" id="${propriedade}${tipoSel}SelButton"
				value="..."
				onclick="javascript: popitup_${propriedade}${tipoSel}('');"
				${disabledBtn} class="btn btn-secondary"
				style="border-bottom-right-radius: 0.25em; border-top-right-radius: 0.25em;">
		</div>
	</c:if>
	<c:if test="${ocultardescricao != 'sim'}">
		<div class="input-group-append ml-2" style="width: 60%;">
			<span class="form-control" style="overflow: hidden; white-space:nowrap; text-overflow:ellipsis;" id="${spanName}SelSpan"><c:out value="${requestScope[propriedadeTipoSel].descricao}" escapeXml="false" /></span>
		</div>
	</c:if>
</div>

<c:if
	test="${not empty tipo and (not empty idInicial or not empty siglaInicial or not empty descricaoInicial)}">
	<c:choose>
		<c:when test="${empty idInicial}">
			<c:set var="idSubstTexto" value="${propriedadeTipoSel}.id" />
			<c:set var="idSubst" value="${requestScope[idSubstTexto]}" />
		</c:when>
		<c:otherwise>
			<c:set var="idSubst" value="${idInicial}" />
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty siglaInicial}">
			<c:set var="siglaSubstTexto" value="${propriedadeTipoSel}.sigla" />
			<c:set var="siglaSubst" value="${requestScope[siglaSubstTexto]}" />
		</c:when>
		<c:otherwise>
			<c:set var="siglaSubst" value="${siglaInicial}" />
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty descricaoInicial}">
			<c:set var="descricaoSubstTexto"
				value="${propriedadeTipoSel}.descricao" />
			<c:set var="descricaoSubst"
				value="${requestScope[descricaoSubstTexto]}" />
		</c:when>
		<c:otherwise>
			<c:set var="descricaoSubst" value="${descricaoInicial}" />
		</c:otherwise>
	</c:choose>
	<script type="text/javascript">
		document.getElementsByName('${inputNameTipoSel}.id')[0].value = '${idSubst}';
		document.getElementsByName('${inputNameTipoSel}.sigla')[0].value = '${siglaSubst}';
		document.getElementsByName('${inputNameTipoSel}.descricao')[0].value = "${descricaoSubst}";
		<c:if test="${ocultardescricao != 'sim'}">
			document.getElementById('${spanName}SelSpan').innerHTML = "${descricaoSubst}";
		</c:if>
	</script>
</c:if>

<c:if test="${tema != 'simple'}">
	</div>
</c:if>