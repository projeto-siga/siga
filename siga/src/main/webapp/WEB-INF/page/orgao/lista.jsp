<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

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
<siga:pagina titulo="Lista Orgão Externo">
<div class="container-fluid">
	<form name="frm" action="listar" class="form" method="GET">
	<input type="hidden" name="paramoffset" value="0" />
	<input type="hidden" name="p.offset" value="0" />

	<div class="card bg-light mb-3" >
		<div class="card-header">
			<h5>Orgãos Externos cadastrados</h5>
		</div>
		<div class="card-body">	
			<table border="0" class="table table-sm table-striped">
				<thead class="${thead_color}">
					<tr>
						<th align="left">Nome</th>
						<th align="center">Sigla</th>
						<th align="left">Orgão Solicitante</th>
						<th align="right">Ativo</th>		
						<th colspan="2" align="center">Opções</th>					
					</tr>
				</thead>
				<tbody class="table-bordered">
					<siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
						itens="${itens}" var="orgao">
						<tr>
							<td align="left">${orgao.descricao}</td>
							<td align="left">${orgao.sigla}</td>
							<td align="left">${orgao.orgaoUsuario.descricao}</td>
							<c:choose>
						      	<c:when test="${orgao.registroAtivo == 'S'}">
						      		<td align="left">Sim</td>
						      	</c:when>
						      	<c:otherwise>
						      		<td align="left">Não</td>
						      	</c:otherwise>
							</c:choose>
							<td align="left">
								<c:url var="url" value="/app/orgao/editar">
									<c:param name="id" value="${orgao.idOrgao}"></c:param>
								</c:url>
								<input type="button" value="Alterar" class="btn btn-primary" onclick="javascript:location.href='${url}'"/>				
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
	</div>
	<div class="gt-table-buttons">
		<c:url var="url" value="/app/orgao/editar"></c:url>
		<input type="button" value="Incluir" onclick="javascript:window.location.href='${url}'" class="btn btn-primary">
	</div>			
	</form>
</div>			
</siga:pagina>
