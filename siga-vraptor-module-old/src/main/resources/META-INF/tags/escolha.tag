<%@ tag body-content="scriptless"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="var" required="false"%>
<%@ attribute name="id" required="false"%>

<script language="javascript" type="text/javascript">
	function muda_escolha(id) 
	{
		<c:set var="setNull" scope="request" >true</c:set>
			<jsp:doBody/>
		<c:remove var="setNull"/>
		var span = document.getElementById(id.value);
		span.style.display="";
	}
</script>

<c:set var="selectedOption" scope="request" value="${requestScope[var]}"></c:set>

<c:set var="createSelect" scope="request" >true</c:set>
	<select id="${id}" name="${var}" onchange="javascript:muda_escolha(this);">
		<jsp:doBody/>
	</select>
<c:remove var="createSelect"/>

<c:set var="createSpan" scope="request" >true</c:set>
	<c:set var="setFirstStyle" scope="request" >true</c:set>
		<jsp:doBody/>
	<c:remove var="setFirst"/>
<c:remove var="createSpan"/>

<c:remove var="selectedOption" scope="request"/>
