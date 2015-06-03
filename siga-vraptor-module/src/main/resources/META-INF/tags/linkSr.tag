<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="acoes" type="java.util.List"%>

<p class="gt-table-action-list">
	<c:set var="linkSeparator" value="${false}"/>
	
	<c:forEach items="${acoes}" var="acao">
		<c:set var="linkConfirm" value="${null}"/>
		
		<c:if test="${not empty acao.msgConfirmAcao}">
			<c:set var="linkConfirm" value="if (confirm('${acao.msgConfirmacao}')) " />
		</c:if>
		
		<c:set var="img">
			<img src="/siga/css/famfamfam/icons/${acao.icone}.png" style="margin-right: 5px;">
		</c:set>
		
		<c:if test="${linkSeparator}">
			<span class="gt-separator">|</span>
		</c:if>
		
		<c:set var="linkSeparator" value="${true}" />
		
		${acao.pre}
		<c:choose>
			<c:when test="${acao.popup}">
				<a href="javascript:${linkConfirm != null ? linkConfirm.raw() : ''}popitup('${acao.url}');">${img.raw()}${acao.nome}</a>
			</c:when>
			<c:when test="${not empty acao.ajax}">
				<span id="spanAjax_${acao.idAjax}"/> 
				<a href="javascript: SetInnerHTMLFromAjaxResponse('${acao.url}', 'spanAjax_${acao.idAjax}');">${img.raw()}${acao.nome}</a>
			</c:when>
			<c:when test="${not empty acao.modal}">
				<a href="javascript: ${acao.url}();">${img.raw()}${acao.nome}</a>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${linkConfirm != null}">
						<a href="javascript:${linkConfirm.raw()}location.href='${acao.url}';">${img.raw()}${acao.nome}</a>
					</c:when>
					<c:otherwise>
						<a href="${acao.url}">${img.raw()}${acao.nome}</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		
		${acao.pos}
	</c:forEach>
</p>
