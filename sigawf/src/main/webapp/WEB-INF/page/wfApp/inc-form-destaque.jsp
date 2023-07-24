<%@ include file="/WEB-INF/page/include.jsp"%>

<div>
	<c:if test="${empty td or piAnterior != pi}" >
		<c:set var="td" value="${pi.definicaoDeTarefaCorrente}" scope="request"/>
	</c:if>
	<c:set var="piAnterior" value="${pi}" scope="request"/>
	<c:set var="msgAviso" value="${pi.getMsgAviso(titular, lotaTitular)}" />
	<c:set var="desabilitarFormulario" value="${pi.isDesabilitarFormulario(titular, lotaTitular)}" />
	<c:if test="${empty td.id or (not desabilitarFormulario and empty msgAviso)}"> 
	<%@ include file="inc-form-top.jsp"%>
		<%--<c:if test="${task.podePegarTarefa}"> --%>
		<div class="row">
			<c:if test="${not empty td.id}">
				<div class="col">
					<c:forEach var="desvio" items="${td.definicaoDeDesvio}"
						varStatus="loop">
						<c:set var="resp" value="${desvio.obterProximoResponsavel(pi)}" />
						<c:if test="${not empty resp}">
							<c:set var="resp" value=" &raquo; ${resp}" />
						</c:if>
						<button style="color: black;" type="submit" name="indiceDoDesvio" value="${loop.index}"
							class="border-warning bg-warning btn btn-info mr-3">${empty desvio.nome ? 'Prosseguir' : desvio.nome}${resp}</button>
					</c:forEach>
					<c:if test="${empty td.definicaoDeDesvio}">
						<c:set var="resp" value="${pi.obterProximoResponsavel()}" />
						<c:if test="${not empty resp}">
							<c:set var="resp" value=" &raquo; ${resp}" />
						</c:if>
						<button style="color: black;" type="submit" name="indiceDoDesvio" value=""
							class="border-warning bg-warning btn btn-info mr-3">Prosseguir${resp}</button>
					</c:if>
				</div>
			</c:if>
			<c:if test="${not empty td.definicaoDeVariavel}">
				<div class="col col-auto ${empty td.id ? '' : 'ml-auto'}">
					<button style="color: black;" type="submit" name="indiceDoDesvio" value="-1"
						class="border-warning bg-warning btn btn-${empty td.id ? 'primary' : 'light'}">Salvar</button>
				</div>
			</c:if>
		</div>
		<%--</c:if> --%>
	</c:if> 
	<c:if test="${not empty td.id}">
		<span style="color: red; font-weight: bold;"> ${msgAviso}</span>
	</c:if>
</div>
