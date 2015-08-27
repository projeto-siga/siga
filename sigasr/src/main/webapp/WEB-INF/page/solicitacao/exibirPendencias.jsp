<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<c:set var="pendencias" value="${solicitacao.pendenciasEmAberto}" />
<c:if test="${not empty pendencias}">
	<c:set var="podeTerminar" value="${solicitacao.podeTerminarPendencia(titular, lotaTitular)}" />
	<div class="gt-sidebar">
		<div class="gt-sidebar-content">
			<h3>Pend&ecirc;ncias</h3>
			<c:forEach items="${pendencias}" var="pendencia">
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