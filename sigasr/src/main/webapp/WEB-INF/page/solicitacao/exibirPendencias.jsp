<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<c:if test="${not empty pendencias}">
		<div class="card mb-1">
			<h6 class="card-header">Pend&ecirc;ncias</h6>
			<div class="card-body">
			
				<c:forEach items="${pendencias}" var="pendencia">
					<c:set var="podeTerminar" value="${pendencia.sol.podeTerminarPendencia(titular, lotaTitular)}" />
					<p>
						${pendencia.descricao}
						<c:if test="${podeTerminar}">
							<a href="javascript:terminarPendencia('${pendencia.id}');" title="Terminar Pend&ecirc;ncia">
								<img src="/siga/css/famfamfam/icons/tick.png" style="margin-left: 5px;" >
							</a>
						</c:if>
					</p>
				</c:forEach>
			</div>
		</div>
</c:if>	 