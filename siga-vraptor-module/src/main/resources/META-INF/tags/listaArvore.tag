<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="nivel" required="false"%>
<%@ attribute name="visualizando" required="true"%>
<%@ attribute name="solicitacao" required="true"%>

<c:if test="${nivel == null}">
	<c:set var="nivel" value="0"/>
</c:if>

<span style="padding-left: ${nivel*10}pt;">
	<c:choose>
		<c:when test="${visualizando == solicitacao}">
			<b><i>
		</c:when>
		<c:otherwise>
			<a style="color: #365b6d;" href="${linkTo[SolicitacaoController].exibir["${solicitacao.idSolicitacao}"]}"
			style="text-decoration: none">
		</c:otherwise>
	</c:choose>
	${solicitacao.codigo}
	<c:choose>
		<c:when test="${visualizando == solicitacao}">
			</i></b>
		</c:when>
		<c:otherwise>
			</a>
		</c:otherwise>
	</c:choose>
	- ${solicitacao.marcadoresEmHtml}
</span>
<br/>
<c:forEach items="" var="solFilha">
	<siga:listaArvore solicitacao="${solFilha}" visualizando="${visualizando}" nivel="${nivel+1}"/>
</c:forEach>