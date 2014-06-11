<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<c:set var="subscritor" scope="request" value="${cadastrante}"/>
<c:set var="data" scope="request" value="Cidade, dd de MMMM de yyyy."/>

<siga:pagina titulo="Teste de Modelo">

<script type="text/javascript">
function sbmtDocumento() {
	ExDocumentoForm.entrevista.value = "";
	ExDocumentoForm.submit();
}

function sbmtEntrevista() {
	ExDocumentoForm.entrevista.value = "1";
	ExDocumentoForm.submit();
}

function sbmt() {
	ExDocumentoForm.submit();
}

</script>

<form name="ExDocumentoForm" method="GET" action="<c:url value="/expediente/modelo.action"/>">
<input type="hidden" name="entrevista" value="${param.entrevista}"/>
<input type="hidden" name="modelo" value="${param.modelo}"/>
<input type="hidden" name="teste" value="sim"/>

<c:choose>
	<c:when test="${param.entrevista == 1}">
		<c:import url="/paginas/expediente/modelos/${param.modelo}" />
	<input type="button" name="ver_doc"
		value="Visualizar o modelo preenchido"
		onclick="javascript: sbmtDocumento();" />
	</c:when>
	<c:otherwise>
		<c:import url="/paginas/expediente/modelos/${param.modelo}" />
	<input type="button" name="fechar_modelo"
		value="Visualizar a entrevista"
		onclick="javascript: sbmtEntrevista();" />
	</c:otherwise>
</c:choose>

</form>

<p/>
</p>
</siga:pagina>