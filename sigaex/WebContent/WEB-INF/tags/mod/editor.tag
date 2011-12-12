<%@ tag body-content="empty"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="entrevista"%>
<%@ attribute name="modeloPrenchido"%>
<%@ attribute name="nmArqMod"%>
<%@ attribute name="conteudo"%>
<%@ attribute name="semBotaoSalvar"%>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>

<input type="hidden" name="vars" value="${var}" />
<div><c:if test="${not empty titulo}">
	<b>${titulo}</b>
</c:if> <c:choose>
	<c:when test="${param.entrevista == 1}">
		<%
			String s = (String) jspContext.getAttribute("v");
					jspContext.setAttribute("v",
							br.gov.jfrj.siga.ex.bl.Ex.getInstance().getBL().canonicalizarHtml(s, false, true, false,
											true));
					String var1 = (String) jspContext.getAttribute("var");
					request.setAttribute(var1, jspContext.getAttribute("v"));
		%>
		<script type="text/javascript">
		FCKeditorAPI = null;
		__FCKeditorNS = null;
	</script>
		<c:choose>
			<c:when test="${semBotaoSalvar eq 'Sim'}">
				<c:set var="toolbarSet">DefaultSemSave</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="tolobarSet">Default</c:set>
			</c:otherwise>
		</c:choose>
		<div><c:choose>
			<c:when test="${f:podeUtilizarExtensaoEditor(lotaTitular, idMod)}">
				<%@ include	file="/WEB-INF/ext/extensaoEditor.jsp" %>
			</c:when>
			<c:otherwise>
			<tags:fixeditor var="${var}">
					<td colspan="3"><FCK:editor id="xxxeditorxxx"
						basePath="/fckeditor/" height="300" toolbarSet="${toolbarSet}">${v}<c:if
							test="${empty v}">
							<p style="TEXT-INDENT: 2cm" align="justify">&nbsp;</p>
						</c:if>
					</FCK:editor></td>
				</tags:fixeditor>
			</c:otherwise>
		</c:choose></div>
	</c:when>
	<c:otherwise>
		<br>
		${v}
		<br>
		<br>
	</c:otherwise>
</c:choose></div>



