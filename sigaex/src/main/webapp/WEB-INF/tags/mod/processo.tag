<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="largura"%>

<script type="text/javascript">
function txtBoxFormat(objForm, strField, sMask, evtKeyPress) {
     var i, nCount, sValue, fldLen, mskLen,bolMask, sCod, nTecla;

     if(document.all) {
       nTecla = evtKeyPress.keyCode; }
     else if(document.layers) {
       nTecla = evtKeyPress.which;
     }

     sValue = strField.value;

     sValue = sValue.toString().replace( "-", "" );
     sValue = sValue.toString().replace( "-", "" );
     sValue = sValue.toString().replace( ".", "" );
     sValue = sValue.toString().replace( ".", "" );
     sValue = sValue.toString().replace( "/", "" );
     sValue = sValue.toString().replace( "/", "" );
     sValue = sValue.toString().replace( "(", "" );
     sValue = sValue.toString().replace( "(", "" );
     sValue = sValue.toString().replace( ")", "" );
     sValue = sValue.toString().replace( ")", "" );
     sValue = sValue.toString().replace( " ", "" );
     sValue = sValue.toString().replace( " ", "" );
     fldLen = sValue.length;
     mskLen = sMask.length;

     i = 0;
     nCount = 0;
     sCod = "";
     mskLen = fldLen;

     while (i <= mskLen) {
       bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/"))
       bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " "))

       if (bolMask) {
         sCod += sMask.charAt(i);
         mskLen++; }
       else {
         sCod += sValue.charAt(nCount);
         nCount++;
       }

       i++;
     }

     strField.value = sCod;

     if (nTecla != 8) {
       if (sMask.charAt(i-1) == "9") {
         return ((nTecla > 47) && (nTecla < 58)); }
       else {
         return true;
       } }
     else {
       return true;
     }
   }



var isNN = (navigator.appName.indexOf("Netscape")!=-1);
function autoTab(input,len, e) {
	var keyCode = (isNN) ? e.which : e.keyCode;
	var filter = (isNN) ? [0,8,9] : [0,8,9,16,17,18,37,38,39,40,46];
	if (input.value.length > len +1){
		input.value = '';
	}
	if(input.value.length >= len && !containsElement(filter,keyCode)) {
		input.value = input.value.slice(0, len);
	
	var nextTxtField = nextTextField(input); 	
	if (nextTxtField!=null)
		nextTxtField.focus();
	else input.blur(); 
	}

	function containsElement(arr, ele) {
		var found = false, index = 0;
		while(!found && index < arr.length)
			if(arr[index] == ele)
				found = true;
			else
				index++;
		return found;
	}

	function nextTextField(aPartirTextField){

		var i;
		for (i = getIndex(aPartirTextField) + 1; i<aPartirTextField.form.length; i++){
			//alert(aPartirTextField.form[i].type);
			if (aPartirTextField.form[i].type=='text')
				return aPartirTextField.form[i];
		}
		return null;
	} 

	function getIndex(input) {
		var index = -1, i = 0, found = false;
		while (i < input.form.length && index == -1)
			if (input.form[i] == input)
				index = i;
			else i++;
		return index;
	}
	
	return true;
	
}
</script>


<c:if test="${not empty largura}">
	<c:set var="jlargura"> size="${largura}"</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>
<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<c:if test="${not empty titulo}">
${titulo}:
</c:if>
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="text" id="${id}" name="${id}"
			onkeypress="var padrao = /^[a-zA-Z]/; var leng; if (this.value.indexOf('20')==0) {leng = 15} else if (padrao.test(this.value)) {leng = 18} else {leng = 10}; autoTab(this, leng, event);"
			value="${v}"
			onblur="var padrao = /^[a-zA-Z]/; var mask; if (this.value.indexOf('20')==0) {mask='9999.99.99.999999-9'} else if (padrao.test(this.value)) {mask='999.9999.999999-9/9999'} else {mask='99.9999999-9'}; txtBoxFormat(document.Form, this, mask, event); this.value = this.value.toUpperCase();" 
			${jlargura}>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
