<%@ include file="/WEB-INF/page/include.jsp"%>
 
<div class="card mb-3">
	<h6 class="card-header">Conhecimentos Relacionados
		<a title="Registrar um novo conhecimento" style="float: right;"
			href="${linkTo[AppController].novo}?classificacao=${classificacao}"
			${popup?'target="_blank" ':''}> <img
			src="/siga/css/famfamfam/icons/add.png">
		</a>
	</h6>
	<div class="card-body">
		<c:choose>
			<c:when test="${not empty conhecimentos}">
				<c:forEach var="conhecimento" items="${conhecimentos}">
					<p>
						<c:if test="${conhecimento[4] > 0}"> 
							<img src="/siga/css/famfamfam/icons/heart.png" title="Como Interessado" style="width: 11px; height: 11px" />
						</c:if>
						<c:if test="${conhecimento[5] > 0}">
							<img src="/siga/css/famfamfam/icons/star.png" title="Como Executor" style="width: 11px; height: 11px" />
						</c:if>
						<b><a href="${linkTo[AppController].exibir(conhecimento[3])}"
							${popup?'target="_blank" ':''}>${conhecimento[1]}</a> </b>
					</p>
				</c:forEach>
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
		
		
		

