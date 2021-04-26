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
					<div class="col-sm-12">
						<div class="form-group">
							<input value="Pesquisar" class="btn btn-primary" onclick="javascript: sbmt(0);"/>	
							<c:if test="${temPermissaoParaExportarDados}">
								<button type="button" class="btn btn-outline-success" id="exportarCsv" title="Exportar para CSV" onclick="javascript:csv('listar', '/siga/app/cargo/exportarCsv');"><i class="fa fa-file-csv"></i> Exportar</button>
							</c:if>
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
							<c:url var="urlAtivarInativar" value="/app/cargo/ativarInativar">
								<c:param name="id" value="${cargo.id}"></c:param>
							</c:url>
							<c:url var="urlExcluir" value="/app/cargo/excluir">
								<c:param name="id" value="${cargo.id}"></c:param>
							</c:url>
							<div class="btn-group">								  
							  <c:choose>
								<c:when test="${empty cargo.dataFimCargo}">
									<a href="${urlAtivarInativar}" onclick='javascript:atualizarUrl("javascript:submitPost(\"${urlAtivarInativar}\")","Deseja inativar o cadastro selecionado?");return false;' class="btn btn-primary" role="button" 
										aria-pressed="true" data-siga-modal-abrir="confirmacaoModal" style="min-width: 80px;">Inativar</a>
									<button type="button" class="btn btn-primary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									    <span class="sr-only"></span>
								    </button>
								</c:when>
								<c:otherwise>
									<a href="javascript:submitPost('${urlAtivarInativar}')" class="btn btn-danger" role="button" aria-pressed="true" style="min-width: 80px;">Ativar</a>
									<button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									    <span class="sr-only"></span>
								    </button>
								</c:otherwise>
							  </c:choose>	  
							  <div class="dropdown-menu">						  
							  	<a href="${url}" class="dropdown-item" role="button" aria-pressed="true">Alterar</a>	
							  	<div class="dropdown-divider"></div>	
							  	<a href="${urlExcluir}" class="dropdown-item" onclick='javascript:atualizarUrl("javascript:submitPost(\"${urlExcluir}\")","Deseja excluir o cadastro selecionado?");return false;' role="button" 
										aria-pressed="true" data-siga-modal-abrir="confirmacaoModal" style="min-width: 80px;">Excluir</a>						   
							  </div>
							</div>						
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
				<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
					<div id="msg" class="modal-body"></div>
			     	<div class="modal-footer">
			       		<button type="button" class="btn btn-danger" data-dismiss="modal">Não</button>		        
			       		<a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true">Sim</a>
					</div>
				</siga:siga-modal>
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
	
	function submitPost(url) {
		var frm = document.getElementById('listar');
		frm.method = "POST";
		sbmtAction('listar',url);
	}
	
	function atualizarUrl(url, msg){
		$('.btn-confirmacao').attr("href", url);
		document.getElementById("msg").innerHTML = msg;
	}
	
</script>
</siga:pagina>