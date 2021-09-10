<%@ include file="/WEB-INF/page/include.jsp"%>

<c:choose>
	<c:when test="${not empty conhecimentos}">
		<c:forEach var="conhecimento" items="${conhecimentos}">
			<c:if test="${cadastrante!=null}">
				<c:choose>
					<c:when test="${pagina == 'exibir'}">
						<a style="float: right;" title="Exibir conhecimento"
							href="${linkTo[AppController].exibir(conhecimento[3])}"
							${popup?'target="_blank"':''}> <img
							src="/siga/css/famfamfam/icons/zoom.png">
						</a>
					</c:when>
					<c:otherwise>
						<a style="float: right;" title="Editar conhecimento"
							href="${linkTo[AppController].editar}?sigla=${conhecimento[3]}&origem=${referer}"
							${popup?'target="_blank"':''}> <img
							src="/siga/css/famfamfam/icons/pencil.png">
						</a>
					</c:otherwise>
				</c:choose>
			</c:if>

			<p style="font-weight: bold">${conhecimento[1]}</p>
			${conhecimento[2]}
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:if test="${podeCriar}">
			<c:set var="url" scope="request">${linkTo[AppController].editar}?classificacao=${classificacao}&inftitulo=${f:urlEncode(titulo)}&origem=${referer}</c:set>
			<a style="float: right;" title="Registrar um novo conhecimento"
				href="${url}" ${popup?'target="_blank"':''}> <img
				src="/siga/css/famfamfam/icons/add.png">
			</a>
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
		</c:if>
	</c:otherwise>
</c:choose>