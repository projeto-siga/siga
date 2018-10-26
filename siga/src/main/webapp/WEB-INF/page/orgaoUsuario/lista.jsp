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
<siga:pagina titulo="Lista Orgão Usuário">
<input type="hidden" name="offset" value="0" />
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2 class="gt-table-head">Dados do &Oacute;rg&atilde;o</h2>
				<div class="gt-content-box gt-for-table">
					<table border="0" class="gt-table">
						<tr>
							<td><label>Nome:</label></td>
							<td><input type="text" id="nome" name="nome" value="${nome}" maxlength="100" size="30"/></td>
						</tr>						
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
			<h2 class="gt-table-head">&Oacute;rg&atilde;os cadastrados</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th algin="center">ID</th>
							<th align="left">Nome</th>
							<th align="center">Sigla</th>
							<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
							itens="${itens}" var="orgaoUsuario">

							<tr>
								<td align="left">${orgaoUsuario.id}</td>
								<td align="left">${orgaoUsuario.descricao}</td>
								<td align="left">${orgaoUsuario.sigla}</td>
								<td align="left">
									<c:url var="url" value="/app/orgaoUsuario/editar">
										<c:param name="id" value="${orgaoUsuario.id}"></c:param>
									</c:url>
									<c:if test="${empty orgaoUsuarioSiglaLogado || orgaoUsuarioSiglaLogado eq orgaoUsuario.sigla}">
									<siga:link title="Alterar" url="${url}" />
									</c:if>					
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
			<c:if test="${empty orgaoUsuarioSiglaLogado}">
			<div class="gt-table-buttons">
					<c:url var="url" value="/app/orgaoUsuario/editar"></c:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>	
			</c:if>			
		</div>			
	</div>
</siga:pagina>
</form>