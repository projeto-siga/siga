<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="acoes" type="java.util.SortedSet"%>

<p class="gt-table-action-list">
	<c:set var="linkSeparator" value="${false}"/>
	
	<c:forEach items="${acoes}" var="acao">
		<c:set var="linkConfirm" value="${null}"/>
		
		<c:if test="${not empty acao.msgConfirmacao}">
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
				<a href="javascript:${linkConfirm != null ? linkConfirm : ''}popitup('/sigasr/app/solicitacao/${acao.urlComParams}');">${img}${acao.nome}</a>
			</c:when>
			<c:when test="${acao.ajax}">
				<span id="spanAjax_${acao.url}"></span> 
				<a href="javascript: SetInnerHTMLFromAjaxResponse('${acao.urlComParams}', 'spanAjax_${acao.url}');">${img}${acao.nome}</a>
			</c:when>
			<c:when test="${acao.modal}">
				<a href="javascript: ${acao.url}();">${img}${acao.nome}</a>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty linkConfirm}">
						<a href="javascript:${linkConfirm}location.href='/sigasr/app/solicitacao/${acao.urlComParams}';">${img}${acao.nome}</a>
					</c:when>
					<c:otherwise>
						<a href="/sigasr/app/solicitacao/${acao.urlComParams}">${img}${acao.nome}</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		
		${acao.pos}
	</c:forEach>
</p>
