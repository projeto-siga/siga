<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Classificação Documental">
<script type="text/javascript">
	function novaClassificacao(){
		$('#frmNovaClassif').submit();
	}
</script>

<h2 class="gt-table-head">Classificação Documental</h2>
<div class="gt-bd clearfix">
	<div class="gt-content">
		<ww:form id="frmNovaClassif" action="editar" method="aEditar">
			<div class="gt-table-buttons">
				<a id="btNovaClassif" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:novaClassificacao()">Nova Classificação</a>
			</div>
		</ww:form>
		<div class="gt-content-box">
			<table border="0" class="gt-table">
				<thead>
					<tr>
						<th>Codificação</th>
						<th>Descrição</th>
					</tr>
				</thead>
	
				<tbody>
					<c:forEach items="${classificacaoVigente}" var="cla">
						<c:set var="nivel" value="${cla.nivel}"/>
						<tr>
							<td><a href="">${cla.codificacao}</a></td>
							<td>
								<c:forEach begin="1" end="${nivel*nivel}">&nbsp;</c:forEach>
								<span style="font-size: ${16-nivel}">${cla.descrClassificacao}</span>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

</siga:pagina>
