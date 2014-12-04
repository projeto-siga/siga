<%@ include file="/WEB-INF/page/include.jsp"%>
<c:forEach var="variable" items="${variableList}">
	<tr class="count">
		<td width="10%">${variable.name}:</td>
		<td width="90%">
			<input type="text" size="50" ${ variable.writeInput } name="fieldVariable" value="${variable.value}" />
		</td>
	</tr>
</c:forEach>