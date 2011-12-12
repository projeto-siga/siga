<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="ww" uri="/webwork"%> <%@ taglib uri="http://localhost/customtag" prefix="tags"%><!--  -->
<table width="100%">
	<tr>
		<td align="left" valign="top" width="100%">
			<input type="hidden" name="pdId" value="${pd.id}" />
			<input type="hidden" name="piId" value="${pi.id}" />
			<input type="hidden" name="tiId" value="${ti.id}" />
			<input type="hidden" name="trId" />
			<table class="form" width="100%">
				<c:import url="inboxTitle.jsp" />
				<c:import url="inboxVariables.jsp" />
				<c:import url="transactions.jsp" />
			</table>
		</td>
	</tr>
</table>
