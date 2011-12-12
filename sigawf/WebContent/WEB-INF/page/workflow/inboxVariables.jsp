<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="ww" uri="/webwork"%> <%@ taglib uri="http://localhost/customtag" prefix="tags"%><!--  -->
<c:if test="${!empty idDoc}">
<tr class="count">
	<td width="10%">Documento :</td>
	<td width="90%">
		<ww:url id="url" action="exibir" namespace="/expediente/doc">
			<ww:param name="id">${idDoc}</ww:param>
			<ww:param name="via">${viaDocVariable}</ww:param>
		</ww:url>
		<a href="${url}">${idDocVariable.value}</a>
	</td>
</tr>
</c:if>
<c:forEach var="variable" items="${variableList}">
	<tr class="count">
		<td width="10%">${variable.name}:</td>
		<td width="90%">
			<input type="text" size="50" ${ variable.writeInput } name="fieldVariable" value="${variable.value}" />
		</td>
	</tr>
</c:forEach>