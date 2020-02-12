<%@ include file="/WEB-INF/page/include.jsp"%>
<tr class="count">
	<td width="10%"></td>
	<td width="90%">
		<c:forEach var="transition" items="${transitionList}">
			<input type="submit" value="${empty transition.name ? 'OK' : transition.name}" onclick="javascript:trId.value='${transition.id}';" />
		</c:forEach>
	</td>
</tr>