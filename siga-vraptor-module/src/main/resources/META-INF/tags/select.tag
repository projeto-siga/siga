<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="id" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="name" required="false"%>
<%@ attribute name="list" required="false"%>
<%@ attribute name="theme" required="false"%>
<%@ attribute name="listValue" required="false"%>
<%@ attribute name="listKey" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="multiple" required="false"%>
<%@ attribute name="headerValue" required="false"%>
<%@ attribute name="headerKey" required="false"%>
<%@ attribute name="style" required="false"%>
<%@ attribute name="isEnum" required="false"%>
<%@ attribute name="required" required="false"%>

<!-- wwselect -->
<%-- Seria ótimo se pudéssemos chamar a EL para calcular a expressão "name", mas não sei como fazer isso. --%>
<c:if test="${empty value}">
	<c:set var="value" value="${requestScope[name]}" />
</c:if>

<c:set var="ehEnum" value="${false}" />
<c:if test="${not empty isEnum}">
	<c:set var="ehEnum" value="${isEnum == true}" />
</c:if>

<c:if test="${theme != 'simple'}">
	<tr>
		<td>${label}</td>
		<td>
</c:if>
<select id="${id}" name="${name}"
	<c:if test="${not empty required and required}">required="true"</c:if>
	<c:if test="${not empty onchange}">onchange="${onchange}"</c:if>
	class="form-control" style="${style != null ? style : ''}">
	<c:choose>
		<c:when test="${ehEnum}">
			<c:if
				test="${(empty required or not required) and (not empty headerKey || not empty headerValue)}">
				<option value="${headerKey}" ${headerKey == value ? 'selected' : ''}>${headerValue}</option>
			</c:if>
			<c:forEach items="${requestScope[list]}" var="item">
				<option value="${item}" ${item == value ? 'selected' : ''}>${item[listValue]}</option>
			</c:forEach>
		</c:when>

		<c:otherwise>
			<c:if
				test="${(empty required or not required) and (not empty headerKey || not empty headerValue)}">
				<option value="${headerKey}" ${headerKey == value ? 'selected' : ''}>${headerValue}</option>
			</c:if>
			<c:forEach items="${requestScope[list]}" var="item">
				<option value="${item[listKey]}"
					${item[listKey] == value ? 'selected' : ''}>${item[listValue]}</option>
			</c:forEach>
		</c:otherwise>

	</c:choose>
</select>

<c:if test="${theme != 'simple'}">
	</td>
	</tr>
</c:if>
