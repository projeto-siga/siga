<%@ include file="/WEB-INF/page/include.jsp"%>

<table class="list" width="100%" border="0" style="margin:0px;padding:0px;border:0px;">
	<tr style="margin:0px;padding:0px;border:0px;">
		<td align="left" valign="top" width="10%" style="margin:0px;padding:0px;border:0px;">
			<input type="hidden" name="pdId" value="${pd.id}" />
			<input type="hidden" name="piId" value="${pi.id}" />
			<input type="hidden" name="tiId" value="${ti.id}" />
			<input type="hidden" name="trId" />
			<c:import url="docTitle.jsp" />
			<c:import url="variables.jsp" />
			<c:import url="transactions.jsp" />
		</td>
	</tr>
</table>
