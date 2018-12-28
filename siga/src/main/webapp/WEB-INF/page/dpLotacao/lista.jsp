<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['offset'].value=offset;
	frm.submit();
}
</script>
<form name="frm" action="listar" class="form" method="POST>
<siga:pagina titulo="Listar Lota&ccedil;&atilde;o">
	<input type="hidden" name="offset" value="0" />
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2 class="gt-table-head">Dados da Lota&ccedil;&atilde;o</h2>
				<div class="gt-content-box gt-for-table">
					<table border="0" class="gt-table">
						<tr>
							<td><label>Órgão:</label></td>
							<td><select name="idOrgaoUsu" value="${idOrgaoUsu}">
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td><label>Nome:</label></td>
							<td><input type="text" id="nome" name="nome" value="${nome}" maxlength="100" size="30"/></td>
						
					</table>
				</div>
				<div class="gt-table-buttons">
					<input type="submit" value="Pesquisar" class="gt-btn-medium gt-btn-left"/>
				</div>
			</div>
		</div>
	
	
	
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Lota&ccedil;&otilde;es cadastradas</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="left">Nome</th>
							<th align="left">Sigla</th>
							<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
							itens="${itens}" var="lotacao">
							<tr>
								<td align="left">${lotacao.descricao}</td>
								<td align="left">${lotacao.sigla}</td>
								<td align="left">
									<c:url var="url" value="/app/lotacao/editar">
										<c:param name="id" value="${lotacao.id}"></c:param>
									</c:url>
									<c:url var="urlAtivarInativar" value="/app/lotacao/ativarInativar">
										<c:param name="id" value="${lotacao.id}"></c:param>
									</c:url>
									<siga:link title="Alterar" url="${url}" />	
									<c:choose>
										<c:when test="${empty lotacao.dataFimLotacao}">
											<siga:link title="Inativar" url="${urlAtivarInativar}" />		
										</c:when>
										<c:otherwise>
											<siga:link title="Ativar" url="${urlAtivarInativar}" />
										</c:otherwise>
									</c:choose>				
								</td>						
							</tr>
						</siga:paginador>
					</tbody>
				</table>				
			</div>	
			<div class="gt-table-buttons">
					<c:url var="url" value="/app/lotacao/editar"></c:url>
					<c:url var="urlAtivarInativar" value="/app/lotacao/ativarInativar"></c:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>				
		</div>			
	</div>
</siga:pagina>
</form>