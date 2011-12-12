<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="ww" uri="/webwork"%> <%@ taglib uri="http://localhost/customtag" prefix="tags"%><!--  -->
<tr class="header" width="">
	<td width="10%">Tarefa:</td>
	<td width="90%">
		<ww:url id="url" action="inboxLoadTask" namespace="/workflow">
			<ww:param name="tiId">${ti.id}</ww:param>
			<ww:param name="piId">${ti.token.processInstance.id}</ww:param>
			<ww:param name="pdId">${ti.token.processInstance.processDefinition.id}</ww:param>
		</ww:url>
		<a href="${url}">${ti.name}</a>
		<c:if test="!empty ${idDoc}">:</c:if>
	</td>
</tr>