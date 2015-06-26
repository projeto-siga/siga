<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="texto"%>
<%@ attribute name="titulo"%>
<%@ attribute name="vermelho"%>
<c:choose>
	<c:when test="${empty pageScope.titulo}">
		<c:choose>
			<c:when test="${vermelho=='Sim'}">
				<FONT COLOR=#ff0000>${texto}</FONT>
			</c:when>
			<c:otherwise>
				${texto}
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${empty texto}">
		<c:choose>
			<c:when test="${vermelho=='Sim'}">
				<FONT COLOR=#ff0000>${pageScope.titulo}</FONT>
			</c:when>
			<c:otherwise>
				${pageScope.titulo}
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${vermelho=='Sim'}">
				<FONT COLOR=#ff0000><b>${titulo}</b>: ${texto}</FONT>
			</c:when>
			<c:otherwise>
				<b>${titulo}</b>: ${texto}
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
