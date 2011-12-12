<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="ww" uri="/webwork"%> <%@ taglib uri="http://localhost/customtag" prefix="tags"%><!--  -->
<tr class="header" width="">
	<td width="10%">Tarefa:</td>
	<td width="90%">
		${ti.name}
		<c:if test="!empty ${idDoc}">:</c:if>
	</td>
</tr>