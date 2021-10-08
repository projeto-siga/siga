<%@ include file="/WEB-INF/page/include.jsp"%>

<c:choose>
	<c:when test="${not empty conhecimentos}">
		<c:forEach var="conhecimento" items="${conhecimentos}">
			<div class="alert alert-info pb-0">${conhecimento[2]}</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:if test="${false and podeCriar}">
			<div class="card bg-light mb-3 mt-3">
				<div class="card-header">
					<c:set var="url" scope="request">${linkTo[AppController].novo}?classificacao=${classificacao}&inftitulo=${f:urlEncode(titulo)}&origem=${referer}</c:set>
					<a style="float: right;" title="Registrar um novo conhecimento"
						href="${url}" ${popup?'target="_blank"':''}> <img
						src="/siga/css/famfamfam/icons/add.png">
					</a> Conhecimento Específico
					<!-- ${conhecimento[1]} -->
				</div>
				<div class="card-body bg-light pb-0">
					<c:choose>
						<c:when test="${msgvazio != null}">
							<%-- Edson: isto foi necessário porque strings vazias são transformadas em null num dos interceptors --%>
							<p>${msgvazio.replaceAll("\\$1", url).replaceAll("empty", "")}</p>
						</c:when>
						<c:otherwise>
					Nenhum conhecimento relacionado registrado. 
					Clique <a href="${url}" ${popup?'target="_blank" ':''}>aqui</a>
					para registrar um novo conhecimento.
				</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:if>
	</c:otherwise>
</c:choose>