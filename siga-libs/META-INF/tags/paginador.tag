<%@ tag body-content="scriptless"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ attribute name="maxItens"%>
<%@ attribute name="maxIndices"%>
<%@ attribute name="totalItens"%>
<%@ attribute name="itens" type="java.util.List"%>
<%@ attribute name="var"%>

<%-- 
<c:if test="${totalItens>0}">
	<c:if test="${totalItens==1}">
		Total: ${totalItens} Item 
	</c:if>
	<c:if test="${totalItens>1}">
		Total: ${totalItens} Itens 
	</c:if>
</c:if>
--%>
<pg:pager id="p" maxPageItems="${maxItens}"
	maxIndexPages="${maxIndices}" scope="request" isOffset="true"
	items="${totalItens}" export="offset,pageOffset;currentPageNumber=pageNumber">
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
			<%--<c:choose>
				<c:when test='${evenorodd == "even"}'>
					<c:set var="evenorodd" value="odd" />
					${statusTeste} - era even virou odd
				</c:when>
				<c:otherwise>
					<c:set var="evenorodd" value="even" />
					${statusTeste} - era outra coisa e virou even
				</c:otherwise>
			</c:choose>--%>
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
</div>
	
<%-- 	
	<table width="100%" border="0">
	<!-- Indice do paginador -->
	<pg:index>
		<tr>
		<td width="15%">
		<pg:first>
			<div align="left">
			<a href="javascript:sbmt(0);">
			<font color="#993399">[Primeira]</font>
			</a>
			</div>
		</pg:first>
		</td>
		<td width="17%">
		<pg:prev>
			<div align="center">
			<a href="javascript:sbmt(${(pageNumber-1)*maxItens});">
			<font color="#993399">[(<c:out value="${pageNumber}" />) &lt;Anterior]</font>
			</a>
			</div>
		</pg:prev>
		</td>
		<td width="36%" align="center">
		<pg:pages>
			<a href="javascript:sbmt(${(pageNumber-1)*maxItens});">
			<font color="#993399">
			<c:out value="${pageNumber}" />
			</font>
			</a>
		</pg:pages>
		</td>
		<td width="17%">
		<pg:next>
			<div align="center">
			<a href="javascript:sbmt(${(pageNumber-1)*maxItens});">
			<font color="#993399">[Pr&oacute;xima&gt; (<c:out
				value="${pageNumber}" />)]</font>
			</a>
			</div>
		</pg:next>
		</td>
		<td width="15%">
		<pg:last>
			<div align="right">
			<a href="javascript:sbmt(${(pageNumber-1)*maxItens});">
			<font color="#993399">[&Uacute;ltima]</font>
			</a>
			</div>
		</pg:last>
		</td>

		</tr>
	</pg:index>
--%>	
</pg:pager>

