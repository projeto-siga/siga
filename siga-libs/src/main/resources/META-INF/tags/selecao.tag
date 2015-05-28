<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
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
<!-- A lista de par -->

<c:forEach var="parametro" items="${fn:split(paramList,';')}">
	<c:set var="p2" value="${fn:split(parametro,'=')}" />
	<c:if test="${not empty p2 and not empty p2[0]}">
		<c:set var="selecaoParams" value="${urlParams}&${p2[0]}=${p2[1]}" />
	</c:if>
	<%--<ww:hidden name="${propriedade}${tipoSel}Sel.${p2[0]}" value="${p2[1]}" />--%>
</c:forEach>


<ww:set name="tam" value="%{[#attr.propriedade+'Sel'].tamanho}" />


<c:choose>
	<c:when test="${empty tipo}">
				<ww:set name="acaoBusca"
					value="%{[#attr.propriedade+'Sel'].acaoBusca}" />
		<ww:set name="tipoSel" value="" scope="request" />
	</c:when>
	<c:otherwise>
				<ww:set name="acaoBusca" value="%{'/'+#attr.tipo}" />
		<ww:set name="tipoSel" value="%{'_'+#attr.tipo}" scope="request" />
	</c:otherwise>
</c:choose>


<c:set var="larguraPopup" value="600" />
<c:set var="alturaPopup" value="400" />


<script type="text/javascript">

self.retorna_${propriedade}${tipoSel} = function(id, sigla, descricao) {
    try {
		newwindow_${propriedade}.close();
    } catch (E) {
    } finally {
    }
	document.getElementsByName('${propriedade}${tipoSel}Sel.id')[0].value = id;
	<c:if test="${ocultardescricao != 'sim'}">
		try {
			document.getElementsByName('${propriedade}${tipoSel}Sel.descricao')[0].value = descricao;
			document.getElementById('${propriedade}${tipoSel}SelSpan').innerHTML = descricao;
		} catch (E) {
		}
	</c:if>
	document.getElementsByName('${propriedade}${tipoSel}Sel.sigla')[0].value = sigla;
	<c:if test="${reler == 'sim'}">
	document.getElementsByName('req${propriedade}${tipoSel}Sel')[0].value = "sim";
	document.getElementById('alterouSel').value='${propriedade}';
	sbmt();
	</c:if>
	<c:if test="${reler == 'ajax'}">
	sbmt('${empty idAjax ? propriedade : idAjax}');
	</c:if> 
}
 
 
<c:choose>
<c:when test="${empty modulo}">
<ww:set name="urlPrefix" value="/" />
</c:when>
<c:otherwise> 
<%--<ww:set name="urlPrefix" value="%{request.scheme+'://'+request.serverName+':'+request.localPort+'/'+#attr.modulo}"></ww:set>--%>
<c:set var="urlPrefix" value="/${modulo}" />
</c:otherwise>
</c:choose>

self.newwindow_${propriedade} = '';
self.popitup_${propriedade}${tipoSel} = function(sigla) {

	//var url = '${request.scheme}://${request.serverName}:${request.localPort}${acaoBusca}/buscar.action?propriedade=${propriedade}${tipoSel}&sigla='+encodeURI(sigla) +'${selecaoParams}';
	var url = '${urlPrefix}${acaoBusca}/buscar.action?propriedade=${propriedade}${tipoSel}&sigla='+encodeURI($.trim(sigla)) +'${selecaoParams}';
	
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
	var sigla = document.getElementsByName('${propriedade}${tipoSel}Sel.sigla')[0].value;
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
	var sigla = $.trim(document.getElementsByName('${propriedade}${tipoSel}Sel.sigla')[0].value);
	var matricula = localStorage.getItem("matricula");
	
	if (sigla == '') {
		return retorna_${propriedade}${tipoSel}('', '', '');
	}
	var url = '${urlPrefix}${acaoBusca}/selecionar.action?propriedade=${propriedade}${tipoSel}'+'${selecaoParams}';
	url = url + '&sigla=' + sigla;
	url = url + '&matricula=' + matricula;
	
	Siga.ajax(url, null, "GET", function(response){		
		resposta_ajax_${propriedade}${tipoSel}(response);
	});	
	//PassAjaxResponseToFunction(url, 'resposta_ajax_${propriedade}${tipoSel}', false);
}

</script>

<c:if test="${tema != 'simple'}">
	<tr>
	<td>${titulo}</td>
	<td>
</c:if>
<c:choose>
	<c:when test="${desativar == 'sim'}">
		<c:set var="disabled" value="true" />
		<c:set var="disabledBtn" value="disabled" />
	</c:when>
</c:choose>


<ww:hidden name="${propriedade}${tipoSel}Sel.id" />
<ww:hidden name="${propriedade}${tipoSel}Sel.descricao" />
<ww:hidden name="${propriedade}${tipoSel}Sel.buscar" />
<ww:hidden name="req${propriedade}${tipoSel}Sel" />
<ww:hidden name="alterouSel" value="" id="alterouSel" />
<ww:textfield name="${propriedade}${tipoSel}Sel.sigla"
	onkeypress="return handleEnter(this, event)"
	onblur="javascript: ajax_${propriedade}${tipoSel}();" size="25"
	theme="simple" disabled="${disabled}" />
<c:if test="${buscar != 'nao'}">
	<input type="button" id="${propriedade}${tipoSel}SelButton" value="..."
		onclick="javascript: popitup_${propriedade}${tipoSel}('');"
		${disabledBtn} theme="simple">
</c:if>
<c:if test="${ocultardescricao != 'sim'}">
	<span id="${propriedade}${tipoSel}SelSpan">
	<ww:property value="${propriedade}${tipoSel}Sel.descricao"
		escape="false" />
	</span>
	<%--${ExDocumentoForm.${propriedade}${tipoSel}Sel.descricao} --%>
	<%--	<script type="text/javascript">
	//document.getElementById('${propriedade}${tipoSel}SelSpan').innerHTML = '<ww:property value="%{#request[#attr.propriedade+#request.tipoSel+'Sel.descricao']}"/>';
	</script> --%>
</c:if>

<c:if test="${not empty tipo}">
	<c:choose>
		<c:when test="${empty idInicial}">
			<c:set var="idSubst">
				<ww:property escape='false'
					value="%{#request[#attr.propriedade+#request.tipoSel+'Sel.id']}" />
			</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="idSubst" value="${idInicial}" />
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty siglaInicial}">
			<c:set var="siglaSubst">
				<ww:property escape='false'
					value="%{#request[#attr.propriedade+#request.tipoSel+'Sel.sigla']}" />
			</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="siglaSubst" value="${siglaInicial}" />
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty descricaoInicial}">
			<c:set var="descricaoSubst">
				<ww:property escape='false'
					value="%{#request[#attr.propriedade+#request.tipoSel+'Sel.descricao']}" />
			</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="descricaoSubst" value="${descricaoInicial}" />
		</c:otherwise>
	</c:choose>
	<script type="text/javascript">
	document.getElementsByName('${propriedade}${tipoSel}Sel.id')[0].value = '${idSubst}';
	document.getElementsByName('${propriedade}${tipoSel}Sel.sigla')[0].value = '${siglaSubst}';
	document.getElementsByName('${propriedade}${tipoSel}Sel.descricao')[0].value = "${descricaoSubst}";
	<c:if test="${ocultardescricao != 'sim'}">
	document.getElementById('${propriedade}${tipoSel}SelSpan').innerHTML = "${descricaoSubst}";
	</c:if>
	</script>
</c:if>

<%--= pageContext.getAttribute("ExDocumentoForm").getSubscritorSel().getDescricao() --%>

<c:if test="${tema != 'simple'}">
	</td>
	</tr>
</c:if>

<%-- onchange="javascript: ['${propriedade}${tipoSel}Sel.buscar'].value=1; sbmt();" --%>
