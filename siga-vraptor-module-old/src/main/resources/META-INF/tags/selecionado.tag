<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="sigla" %>
<%@ attribute name="descricao" %>
<%@ attribute name="pessoaParam" %>
<%@ attribute name="lotacaoParam" %>
<%@ attribute name="isVraptor" %>

<c:if test="${empty sigla}">
	<c:set var="sigla" value="&nbsp;" />
</c:if>

<c:if test="${not empty pessoaParam}">
       <a href="/siga/app/pessoa/exibir?sigla=${pessoaParam}">
	       <span ${estilo} title="${descricao}">
	       		${sigla}
	       </span>
       </a>
</c:if>

<c:if test="${not empty lotacaoParam}">
	<a href="/siga/app/lotacao/exibir?sigla=${lotacaoParam}">
		<span ${estilo} title="${descricao}">
       		${sigla}
		</span>
	</a>
</c:if>

<c:if test="${empty pessoaParam && empty lotacaoParam}">
	<span ${estilo} title="${descricao}">
		${sigla}
	</span>
</c:if>