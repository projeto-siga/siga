<%@ include file="/WEB-INF/page/include.jsp"%>

<div>
	<%@ include file="inc-form/inc-form-vars.jsp"%>
	<c:if test="${empty td.id or (not desabilitarFormulario and empty msgAviso)}"> 
	<%@ include file="inc-form/inc-form-top.jsp"%>
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
	</c:if> 
	<%@ include file="inc-form/inc-form-aviso.jsp"%>
</div>
