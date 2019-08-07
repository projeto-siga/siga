<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>


	<table class="table-hover" style="width: 100%">
		<thead>
			<tr>
				<th width="50%">Descri&ccedil;&atilde;o</th>
				<th width="25%" style="text-align: right">Total</th>
			</tr>
		</thead>
		<tbody>
        <c:if test="${not empty lista}">		
			<c:forEach items="${lista}" var="item">
				<tr>
				    <td ><a href="${item[0]}">${null != item[1] ? item[1] : ""}</a></td>
				    <td align="right" ><a href="${item[0]}">${item[2]}</a></td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>

<div class="mt-2">
	<a class="btn btn-primary float-right btn-sm ml-2"
		href="${linkTo[RequisicaoController].incluir}"
		title="<fmt:message key='views.botoes.novo' />"><fmt:message key='views.botoes.novo' /></a>
</div>
