<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="texto"%>

<c:if test="${not empty texto}">
	<div class="row">
		<div class="col-sm">
			<div class="alert alert-success" role="alert">
				<i class="fas fa-check-circle"></i> ${texto}				
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
	 				<span aria-hidden="true">&times;</span>
				</button>
			</div>
		</div>
	</div>
</c:if>
