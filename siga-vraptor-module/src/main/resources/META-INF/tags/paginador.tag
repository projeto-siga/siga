<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ attribute name="maxItens"%>
<%@ attribute name="maxIndices"%>
<%@ attribute name="totalItens"%>
<%@ attribute name="itens" type="java.util.List"%>
<%@ attribute name="var"%>

<pg:pager id="p" maxPageItems="${maxItens}"
	maxIndexPages="${maxIndices}" scope="request" isOffset="true"
	items="${totalItens}" export="offset,pageOffset">
	<c:set var="evenorodd" value="odd" />
	<c:forEach var="item" items="${itens}" varStatus="statusTeste">
		<pg:item>
			<%
					request.setAttribute((String) jspContext.getAttribute("var"),
					jspContext.getAttribute("item"));
					String att = (String) request.getAttribute("evenorodd");
					if (att != null && att.equals("even"))
						request.setAttribute("evenorodd", "odd");
					else
						request.setAttribute("evenorodd", "even");
			%>
			<jsp:doBody />
		</pg:item>
	</c:forEach>
	</table>
	<div class="gt-table-controls gt-table-controls-btm clearfix">
		<p class="gt-table-pager">
		 	<c:set var="firstpgpages" value="${true}"/>
			<pg:pages>
			 	<c:if test="${not firstpgpages}"> | </c:if>
			 	<c:set var="firstpgpages" value="${false}"/>
				<a href="javascript:sbmt(${(pageNumber-1)*maxItens});" <c:if test="${pageNumber == currentPageNumber}">class="current"</c:if>><c:out value="${pageNumber}" /></a>
			</pg:pages>
		</p>
	</div>
	<table style="display: none">
</pg:pager>

