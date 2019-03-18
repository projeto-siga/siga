<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Classificações">
	<jsp:include page="../main.jsp"></jsp:include>
	<div class="gt-bd gt-cols clearfix" style="padding-bottom: 0px;">
		<div class="gt-content clearfix">
        	<h3>Ações:</h3>
        		<div class="gt-content-box">
				<table border="0" width="100%" class="gt-table mov">
					<tr>
						<th>Sigla</th>
						<th>Classificação</th>
					</tr>
					<c:forEach var="acao" items="${acoes}"> 
						<tr><td>${acao.siglaAcao}</td><td>${acao.gcTags}</td></tr>
					</c:forEach>
				</table>
				</div>
			<br/><br/>
        	<h3>Itens:</h3>
        		<div class="gt-content-box">
				<table border="0" width="100%" class="gt-table mov">
					<tr>
						<th>Sigla</th>
						<th>Classificação</th>
					</tr>
					<c:forEach var="item" items="${itens}"> 
						<tr><td>${item.siglaItemConfiguracao}</td><td>${item.gcTags}</td></tr>
					</c:forEach>
				</table>
				</div>
	    </div>
	    <div id="divRelacionados"></div>
	</div>
</siga:pagina>
