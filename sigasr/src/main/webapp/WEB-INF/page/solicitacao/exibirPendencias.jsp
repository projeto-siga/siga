<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<c:if test="${not empty pendencias}">
		<div class="gt-sidebar-content">
			<h3>Pend&ecirc;ncias</h3>
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
</c:if>	 