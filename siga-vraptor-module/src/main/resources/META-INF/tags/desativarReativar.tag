<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="id" required="true" %>
<%@ attribute name="onReativar" required="true" %>
<%@ attribute name="onDesativar" required="true" %>
<%@ attribute name="isAtivo" required="true" %>

<c:choose>
	<c:when test="${isAtivo}">
		<a class="once gt-btn-ativar" onclick="${onDesativar}(event, ${id})" title="Desativar">
			<img src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;"> 
		</a>
	</c:when>
	<c:otherwise>
		<a class="once gt-btn-ativar item-desativado" onclick="${onReativar}(event, ${id})" title="Reativar">
			<img src="/siga/css/famfamfam/icons/tick.png" style="margin-right: 5px;"> 
		</a>
	</c:otherwise>
</c:choose>