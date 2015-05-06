<%@ tag body-content="scriptless"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="name" required="false"%>
<%@ attribute name="nameInput" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="depende" required="false"%>
<%@ attribute name="disabled" required="false"%>

 <script language="javascript">
	function show${name.replace('.', '')}(checked, divName){
		var div = document.getElementById(divName);
		if (div) {
			if (checked)
				div.style.display = 'inline';
			else 
				div.style.display = 'none';
		}
	}
	
	function change${name.replace('.', '')}(){
		if (document.getElementById('check${name}').checked)
			document.getElementById('${name}').value='true';
		else document.getElementById('${name}').value='false';
	}
 </script>
 
<c:if test="${empty nameInput}">
	<c:set var="nameInput" value="${name}"></c:set>
</c:if>

<input type="hidden" name="${nameInput}" id="${name}" value="${value ? 'true':'false'}" />
<input id="check${name}" type="checkbox" ${null != disabled ? "disabled='disabled'" : '' } ${value == true ? 'checked="checked"':''} 
	onchange="javascript:change${name.replace('.', '')}(); show${name.replace('.', '')}(this.checked, '${depende}');${onchange}" />  