<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Listar Lota&ccedil;&atilde;o">
<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset == null) {
		offset = 0;
	}
	frm.elements["paramoffset"].value = offset;
	frm.elements["p.offset"].value = offset;
	frm.submit();
}
</script>
	<!-- main content -->
	<div class="container-fluid">
		<form name="frm" action="listar" class="form100" method="GET">
		<input type="hidden" name="paramoffset" value="0" />
		<input type="hidden" name="p.offset" value="0" />
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da <fmt:message key="usuario.lotacao"/></h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label>Órgão</label>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control">
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label>Nome</label>
							<input type="text" id="nome" name="nome" value="${nome}" maxlength="100" class="form-control"/>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<input value="Pesquisar" class="btn btn-primary" onclick="javascript: sbmt(0);"/>
						</div>
					</div>
				</div>			
			</div>
		</div>
		
		<h3 class="gt-table-head"><fmt:message key="usuario.lotacoes"/> cadastradas</h3>
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
				<tr>
					<th align="left">Nome</th>
					<th align="left">Sigla</th>
					<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
				</tr>
			</thead>
			
			<tbody>
				<siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
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
							<a href="${url}" role="button" aria-pressed="true" class="btn btn-primary" >Alterar</a>		
							<c:choose>
								<c:when test="${empty lotacao.dataFimLotacao}">
									<a href="${urlAtivarInativar}" role="button" aria-pressed="true" class="btn btn-primary" >Inativar</a>
								</c:when>
								<c:otherwise>
									<a href="${urlAtivarInativar}" role="button" aria-pressed="true" class="btn btn-primary" >Ativar</a>
								</c:otherwise>
							</c:choose>				
						</td>						
					</tr>
				</siga:paginador>
			</tbody>
		</table>				
		<div class="gt-table-buttons">
			<c:url var="url" value="/app/lotacao/editar"></c:url>
			<c:url var="urlAtivarInativar" value="/app/lotacao/ativarInativar"></c:url>
			<input type="button" value="Incluir" onclick="javascript:window.location.href='${url}'" class="btn btn-primary">
		</div>				
		</form>
	</div>

</siga:pagina>
