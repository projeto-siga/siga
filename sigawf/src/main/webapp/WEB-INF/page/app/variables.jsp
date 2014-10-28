<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="ww" uri="/webwork"%> <%@ taglib uri="http://localhost/customtag" prefix="tags"%><!--  -->
<c:forEach var="variable" items="${variableList}">
	<tr class="count">
		<td width="10%">${variable.name}:</td>
		<td width="90%">
			<input type="text" size="50" ${ variable.writeInput } name="fieldVariable" value="${variable.value}" />
		</td>
	</tr>
</c:forEach>