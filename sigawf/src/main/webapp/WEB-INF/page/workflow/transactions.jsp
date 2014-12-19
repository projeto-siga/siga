<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="ww" uri="/webwork"%> <%@ taglib uri="http://localhost/customtag" prefix="tags"%><!--  -->
<tr class="count">
	<td width="10%"></td>
	<td width="90%">
		<c:forEach var="transition" items="${transitionList}">
			<input type="submit" value="${empty transition.name ? 'OK' : transition.name}" onclick="javascript:trId.value='${transition.id}';" />
		</c:forEach>
	</td>
</tr>