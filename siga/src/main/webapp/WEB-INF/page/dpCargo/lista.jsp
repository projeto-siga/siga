<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Listar Cargos">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
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
<form name="frm" action="listar" id="listar" class="form" method="GET">
	<input type="hidden" name="paramoffset" value="0" />
	<input type="hidden" name="p.offset" value="0" />
	<div class="container-fluid">
		<div class="card bg-light mb-3" >		
			<div class="card-header"><h5>Cadastro de Cargo</h5></div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm">
						<div class="alert alert-info  mensagem-pesquisa" role="alert" style="display: none;">
    						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
    							<span aria-hidden="true">×</span>
  							</button>
  							<i class="fas fa-info-circle"></i> ${mensagemPesquisa}
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label>Órgão</label>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2">
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label>Nome</label>
							<input type="text" id="nome" name="nome" value="${nome}" maxlength="100" size="30" class="form-control"/>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<input value="Pesquisar" class="btn btn-primary" onclick="javascript: sbmt(0);"/>
							<button type="button" class="btn btn-outline-success" id="exportarCsv" title="Exportar para CSV"	onclick="javascript:csv('listar', '/siga/app/cargo/exportarCsv');"><i class="fa fa-file-csv"></i> Exportar</button>
						</div>
					</div>
				</div>				
			</div>
		</div>
		<h5>Cargos cadastrados</h5>
		
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
				<tr>
					<th align="left">Nome</th>
					<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
				</tr>
			</thead>
			<tbody class="table-bordered">
				<siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
					itens="${itens}" var="cargo">
					<tr>
						<td align="left">${cargo.descricao}</td>
						<td align="left">
							<c:url var="url" value="/app/cargo/editar">
								<c:param name="id" value="${cargo.id}"></c:param>
							</c:url>
							<input type="button" value="Alterar"
											class="btn btn-primary" onclick="javascript:location.href='${url}'"/>				
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
		<div class="form-group row">
			<div class="col-sm">
				<c:url var="url" value="/app/cargo/editar"></c:url>
				<input type="button" value="Incluir" onclick="javascript:window.location.href='${url}'" class="btn btn-primary">
			</div>				
		</div>
	</div>			
</form>
<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
<script type="text/javascript">
	temPermissaoParaExportarDados = '${temPermissaoParaExportarDados}' == 'true';		
	if (!temPermissaoParaExportarDados) $('#exportarCsv').attr('disabled', 'disabled').attr('title', 'Exportar para CSV - usuário sem permissão');
	
	$(document).ready(function() {	
		if ('${mensagemPesquisa}'.length > 0) $('.mensagem-pesquisa').css({'display':'block'});
	});
	
	function csv(id, action) {
		var frm = document.getElementById(id);
		frm.method = "POST";
		sbmtAction(id, action);
		
		$('.mensagem-pesquisa').alert('close');
		
		frm.action = 'listar';
		frm.method = "GET";
	}
	
	function sbmtAction(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}
</script>
</siga:pagina>