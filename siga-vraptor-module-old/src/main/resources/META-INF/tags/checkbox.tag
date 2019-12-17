<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="name" required="false"%>
<%@ attribute name="nameInput" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="depende" required="false"%>
<%@ attribute name="disabled" required="false"%>

<c:set var="nameClean" value="${fn:replace(name,'.','')}" />

 <script language="javascript">
	function show${nameClean}(checked, divName){
		var div = document.getElementById(divName);
		if (div) {
			if (checked)
				div.style.display = 'inline';
			else 
				div.style.display = 'none';
		}
	}
	
	function change${nameClean}(){
		if (document.getElementById('check${nameClean}').checked)
			document.getElementById('${nameClean}').value='true';
		else document.getElementById('${nameClean}').value='false';
	}
 </script>
 
<c:if test="${empty nameInput}">
	<c:set var="nameInput" value="${name}"></c:set>
</c:if>

<input id="check${nameClean}" type="checkbox" ${null != disabled ? "disabled='disabled'" : '' } ${value == true ? 'checked="checked"':''} 
	onchange="javascript:change${nameClean}(); show${nameClean}(this.checked, '${depende}');${onchange}" />  
	
<input type="hidden" name="${nameInput}" id="${nameClean}" value="${value ? 'true':'false'}" />
