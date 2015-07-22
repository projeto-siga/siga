<c:if test="${solicitacao.isPendente()}">
	<c:set var="podeTerminar" value="${solicitacao.podeTerminarPendencia(lotaTitular, cadastrante)}" />
	<div class="gt-sidebar">
		<div class="gt-sidebar-content">
			<h3>PendÃªncias</h3>
			<c:forEach items="${solicitacao.pendenciasEmAberto}" var="pendencia">
				<p>
					<b>${pendencia.motivoPendenciaString}</b><c:if test="${pendencia.dtAgenda}"> at&eacute; ${pendencia.dtAgendaDDMMYYHHMM}</c:if> - ${pendencia.descrMovimentacao}
					<c:if test="${podeTerminar}">
						<a href="javascript:terminarPendencia('${pendencia.idMovimentacao}');">
							<img src="/siga/css/famfamfam/icons/cross.png" style="margin-left: 5px;">
						</a>
					</c:if>
				</p>
			</c:forEach>
		</div>
	</div>
</c:if>