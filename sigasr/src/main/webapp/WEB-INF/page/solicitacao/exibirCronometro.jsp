<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<style>
	.valor {
      display:none;
    }
</style>
<c:if test="${not empty etapasCronometro}">
<c:forEach var="etapa" items="${etapasCronometro}">

	<div class="card card-sidebar mb-3 cronometro ${etapa.ativo ? 'ligado' : 'desligado'}">
		<h6 class="card-header">
			<img src="/siga/css/famfamfam/icons/clock.png" width="15px;"
				style="vertical-align: bottom;">&nbsp;${etapa.descricaoCompleta}
		</h6>
		<div class="card-body">
		
			<c:if test="${not empty etapa.inicio}">
			    <p><b>In&iacute;cio: </b>${etapa.inicioString}</p>
			</c:if>
			<c:choose>
				<c:when test="${not empty etapa.fim}">
					<p><b>Fim:</b> ${etapa.fimString}</p>
				</c:when>
				<c:when test="${not empty etapa.fimPrevisto}">
					<p><b>Previs&atilde;o:</b> ${etapa.fimPrevistoString}
				</c:when>
			</c:choose>
			<c:set var="restante" value="${etapa.restanteEmSegundos}" />
			<c:choose>
				<c:when test="${not empty restante}">
					<p><span class="crono restante ${etapa.ativo ? 'ligado' : 'desligado'}"><b><span class="label-cron"></span></b><span class="descrValor"></span><span class="valor">${restante}</span></span></p>
				</c:when>
				<c:when test="${!etapa.zeradoEParado}">
					<p><span class="crono decorrido ${etapa.ativo ? 'ligado' : 'desligado'}"><b>Decorrido:</b> <span class="descrValor"></span><span class="valor">${etapa.decorridoEmSegundos}</span></span></p>
				</c:when>
			</c:choose>
		</div>
	</div>

</c:forEach>
</c:if>
