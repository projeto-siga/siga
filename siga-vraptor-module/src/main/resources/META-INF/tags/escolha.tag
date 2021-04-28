<%@ tag body-content="scriptless"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="var" required="false"%>
<%@ attribute name="id" required="false"%>
<%@ attribute name="classSelect"%>
<%@ attribute name="executarJavascript" required="false"%>
<%@ attribute name="singleLine" required="false"%>

<c:if test="${executarJavascript != 'false'}">
	<script language="javascript" type="text/javascript">
		function muda_escolha(id) {
			<c:set var="setNull" scope="request" >true</c:set>
			<jsp:doBody/>
			<c:remove var="setNull"/>
			var span = document.getElementById(id.value);
			span.style.display = "";
		}
	</script>
</c:if>

<c:set var="selectedOption" scope="request" value="${requestScope[var]}"></c:set>

<c:set var="createSelect" scope="request">true</c:set>

<c:if test="${singleLine}">
	<div class="row">
		<div class="col col-auto">
</c:if>
<select id="${id}" name="${var}"
	onchange="javascript:muda_escolha(this);" class="form-control">
	<jsp:doBody />
</select>
<c:if test="${singleLine}">
	</div>
	<div class="col">
</c:if>
<c:remove var="createSelect" />

<c:set var="createSpan" scope="request">true</c:set>
<c:set var="setFirstStyle" scope="request">true</c:set>
<jsp:doBody />
<c:if test="${singleLine}">
	</div>
	</div>
</c:if>

<c:remove var="setFirst" />
<c:remove var="createSpan" />

<c:remove var="selectedOption" scope="request" />
