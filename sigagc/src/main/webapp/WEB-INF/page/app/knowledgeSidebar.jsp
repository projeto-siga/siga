<%@ include file="/WEB-INF/page/include.jsp"%>

<a title="Registrar um novo conhecimento" style="float: right;"
	href="${linkTo[AppController].editar}?classificacao=${classificacao}"
	${popup?'target="_blank" ':''}> <img
	src="/siga/css/famfamfam/icons/add.png">
</a>
<h3>Conhecimentos Relacionados</h3>
<c:choose>
	<c:when test="${not empty conhecimentos}">
		<c:forEach var="conhecimento" items="${conhecimentos}">
			<p>
				<b><a href="${linkTo[AppController].exibir[conhecimento[3]]}"
					${popup?'target="_blank" ':''}>${conhecimento[1]}</a> </b>
			</p>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:if test="${podeCriar}">
			<p>
				Nenhum conhecimento relacionado registrado. Clique <a
					href="${linkTo[AppController].editar}?classificacao=${classificacao}"
					${popup?'target="_blank" ':''}>aqui</a> para registrar um novo
				conhecimento.
			</p>
		</c:if>
	</c:otherwise>
</c:choose>
