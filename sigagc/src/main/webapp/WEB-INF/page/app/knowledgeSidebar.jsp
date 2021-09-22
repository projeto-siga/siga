<%@ include file="/WEB-INF/page/include.jsp"%>

<div class="card bg-light mb-3 mt-3">
	<div class="card-header">
		<a title="Registrar um novo conhecimento" style="float: right;"
			href="${linkTo[AppController].novo}?classificacao=${classificacao}"
			${popup?'target="_blank" ':''}> <img
			src="/siga/css/famfamfam/icons/add.png">
		</a> Conhecimentos Relacionados
	</div>
	<div class="card-body">
		<c:choose>
			<c:when test="${not empty conhecimentos}">
				<ul class="ml-0">
					<c:forEach var="conhecimento" items="${conhecimentos}">
						<li><c:if test="${conhecimento[4] > 0}">
								<img src="/siga/css/famfamfam/icons/heart.png"
									title="Como Interessado" style="width: 11px; height: 11px" />
							</c:if> <c:if test="${conhecimento[5] > 0}">
								<img src="/siga/css/famfamfam/icons/star.png"
									title="Como Executor" style="width: 11px; height: 11px" />
							</c:if> <a href="${linkTo[AppController].exibir(conhecimento[3])}"
							${popup?'target="_blank" ':''}>${conhecimento[1]}</a></li>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				<c:if test="${podeCriar}">
					<p>
						Nenhum conhecimento relacionado registrado. Clique <a
							href="${linkTo[AppController].novo}?classificacao=${classificacao}"
							${popup?'target="_blank" ':''}>aqui</a> para registrar um novo
						conhecimento.
					</p>
				</c:if>
			</c:otherwise>
		</c:choose>
	</div>
</div>