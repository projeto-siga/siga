<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${solicitacao.acordos != null && !solicitacao.acordos.isEmpty()}">
	<div class="gt-sidebar">
		<div class="gt-sidebar-content">
			<h3>Acordos</h3>
			<c:forEach items="${solicitacao.acordos}" var="acordo">
				<p>
					<b>${acordo.acordoAtual.nomeAcordo}</b>
				<ul>
					<c:forEach items="${acordo.acordoAtual.atributoAcordoSet}"
						var="att">
						<li>${att.atributo.nomeAtributo}${att.operador.nome}
							${att.valor} ${att.unidadeMedida.plural}: 
							<c:choose>
								<c:when test="${solicitacao.isAtributoAcordoSatisfeito(att)}">
									<span style="color: green">Ok</span>
								</c:when>
								<c:otherwise>
									<span style="color: red">N&atilde;o cumprido</span>
								</c:otherwise>
							</c:choose>
						</li>
					</c:forEach>
				</ul>
				</p>
			</c:forEach>
		</div>
	</div>
</c:if>
