<%@ include file="/WEB-INF/page/include.jsp"%>

<c:if test="${conhecimentos != null}">
	<c:forEach var="conhecimento" items="${items:conhecimentos}">
		<c:if test="${cadastrante!=null}	 
		<a style=" float:
			right;" title="Editar conhecimento"
			href="${linkTo[AppController].editar[conhecimento[3][null][null][referer])}"
			target="<c:if test="${popup}_blank#{/if}">
			<img src="/siga/css/famfamfam/icons/pencil.png">
			</a>
		</c:if>

		<p style="font-weight: bold">${conhecimento[1]}</p>
	${conhecimento[2]}
	
</c:forEach>
</c:if>
<c:if test="${conhecimentos == null} and podeCriar">
	<c:set var="url"
		value="${linkTo[AppController].editar[null][classificacao][titulo][referer])}" />
	<a style="float: right;" title="Registrar um novo conhecimento"
		href="${url}" ${popup?'target="_blank"':''}> <img
		src="/siga/css/famfamfam/icons/add.png">
	</a>
	<c:choose>
		<c:when test="${msgvazio != null}">
			<p>${msgvazio.replace("\$1", url).raw()}</p>
		</c:when>
		<c:otherwise>
	Nenhum conhecimento relacionado registrado. Clique <a href="${lurl}"
				${popup?'target="_blank" ':''}s>aqui</a>
	para registrar um novo conhecimento.
	</c:otherwise>
	</c:choose>
</c:if>
