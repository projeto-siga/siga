<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="name" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="depende" required="false"%>

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
<input type="hidden" name="${name}" id="${name}" value="${value ? 'true':'false'}" />
<input id="check${name}" ${disabled ? disabled="disabled" : "" } type="checkbox" value="true" ${value ? 'checked':''} onchange="javascript:change${name.replace('.', '')}(); show${name.replace('.', '')}(this.checked, '${depende}');${onchange}" />
 