<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="collapseMode"%>
<%@ attribute name="title"%>
<%@ attribute name="id"%>

<c:choose>
	<c:when test="${(collapseMode == 'collapsible closed') or (collapseMode == 'collapsible expanded') or (collapseMode == 'collapsible')}">
		<c:choose>
			<c:when test="${collapseMode == 'collapsible closed'}">
				<div class="card-header" id="head${id}">
					<a href="#collapse${id}" class="card-toggle collapsed text-dark" data-toggle="collapse" data-target="#collapse${id}" aria-expanded="false">
						${title}
					</a>
				</div>
				<div class="card-body collapse" id="collapse${id}">
			</c:when>
			<c:otherwise>
				<div class="card-header" id="head${id}">
					<a href="#collapse${id}" class="card-toggle text-dark" data-toggle="collapse" data-target="#collapse${id}" aria-expanded="true">
						${title}
					</a>
				</div>
				<div class="card-body collapse show" id="collapse${id}">
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<div class="card-header">
			<a href="#collapse${id}" class="text-dark">
				${title}
			</a>
		</div>
		<div class="card-body">
	</c:otherwise>
</c:choose>
<jsp:doBody />
	</div>