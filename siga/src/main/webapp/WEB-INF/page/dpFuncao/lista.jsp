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
<siga:pagina titulo="Listar Fun&ccedil;&otilde;es de Confian&ccedil;a">
	<input type="hidden" name="offset" value="0" />
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2 class="gt-table-head">Dados da Fun&ccedil;&atilde;o de Confian&ccedil;a</h2>
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
			<h2 class="gt-table-head">Funç&otilde;es de Confian&ccedil;a cadastrados</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="left">Nome</th>
							<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
							itens="${itens}" var="funcao">
							<tr>
								<td align="left">${funcao.descricao}</td>
								<td align="left">
									<c:url var="url" value="/app/funcao/editar">
										<c:param name="id" value="${funcao.id}"></c:param>
									</c:url>
									<siga:link title="Alterar" url="${url}" />					
								</td>
							<%--	<td align="left">									
	 			 					<a href="javascript:if (confirm('Deseja excluir o orgão?')) location.href='/siga/app/orgao/excluir?id=${orgao.idOrgao}';">
										<img style="display: inline;"
										src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir orgão"							
										onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
										onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
									</a>															
								</td>
							 --%>							
							</tr>
						</siga:paginador>
					</tbody>
				</table>				
			</div>	
			<div class="gt-table-buttons">
					<c:url var="url" value="/app/funcao/editar"></c:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>				
		</div>			
	</div>
</siga:pagina>
</form>
