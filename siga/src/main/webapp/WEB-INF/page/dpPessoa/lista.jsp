<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset == null) {
		offset = 0;
	}
	frm.elements["paramoffset"].value = offset;
	frm.elements["p.offset"].value = offset;
	frm.submit();
}
function validarCPF(Objcpf){
	var strCPF = Objcpf.replace(".","").replace(".","").replace("-","").replace("/","");
    var Soma;
    var Resto;
    Soma = 0;
	
    for (i=1; i<=9; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
    Resto = (Soma * 10) % 11;
	
    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(9, 10)) ) {
    	
    	alert('CPF Inválido!');
        return false;
	}
    Soma = 0;
    for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
    Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(10, 11) ) ) {
    	
    	alert('CPF Inválido!');
    	return false;
    }
    return true;
         
}
function cpf_mask(v){
	v=v.replace(/\D/g,"");
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");

	if(v.length == 14) {
    	validarCPF(v);
    }
	return v;
}
</script>

<siga:pagina titulo="Listar Pessoas">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />	

	<!-- main content -->
	<div class="container-fluid">
	<form name="frm" action="listar" id="listar" class="form100" method="GET">
		<input type="hidden" name="paramoffset" value="0" />
		<input type="hidden" name="p.offset" value="0" />
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da Pessoa</h5>
			</div>
			<div class="card-body">
				<div class="row">
					
					
					<div class="col-md-4">
						<div class="form-group" id="idOrgaoUsuGroup">
							<label for="idOrgaoUsu">Órgão</label>
							<select class="form-control siga-select2" id="idOrgaoUsu" name="idOrgaoUsu" value="${idOrgaoUsu}" onchange="carregarRelacionados(this.value)">
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>					
					</div>		
					
									
					<div class="col-md-2">
						<div class="form-group" id="idCargoGroup">
							<label for="idCargoPesquisa">Cargo</label>
							<select class="form-control  siga-select2" id="idCargoPesquisa" name="idCargoPesquisa" value="${idCargoPesquisa}">
								<c:forEach items="${listaCargo}" var="item">
									<option value="${item.idCargo}"
										${item.idCargo == idCargoPesquisa ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</div>					
					</div>
					<div class="col-md-2">
						<div class="form-group" id="idFuncaoGroup">
							<label for="idFuncaoPesquisa">Fun&ccedil;&atilde;o de Confian&ccedil;a</label>
							<select class="form-control  siga-select2" id="idFuncaoPesquisa" name="idFuncaoPesquisa" value="${idFuncaoPesquisa}">
								<c:forEach items="${listaFuncao}" var="item">
									<option value="${item.idFuncao}"
										${item.idFuncao == idFuncaoPesquisa ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</div>					
					</div>
					<div class="col-md-4" >
						<div class="form-group" id="idLotacaoGroup">
							<label for="idLotacaoPesquisa"><fmt:message key="usuario.lotacao"/></label>
							<select class="form-control  siga-select2" id="idLotacaoPesquisa" style="width: 100%"  
									name="idLotacaoPesquisa" value="${idLotacaoPesquisa}">
								<c:forEach items="${listaLotacao}" var="item">
									<option value="${item.idLotacao}" ${item.idLotacao == idLotacaoPesquisa ? 'selected' : ''}>
										<c:if test="${item.descricao ne 'Selecione'}">${item.siglaLotacao} / </c:if>${item.descricao}
									</option>
								</c:forEach>
							</select>
						</div>					
					</div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label for="nome">Nome</label>
							<input type="text" id="nome" name="nome" value="${nome}" maxlength="100" class="form-control"/>
						</div>					
					</div>					
					<div class="col-md-2">
						<div class="form-group">
							<label for="nome">CPF</label>
							<input type="text" id="cpfPesquisa" name="cpfPesquisa" value="${cpfPesquisa}" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" class="form-control"/>
						</div>					
					</div>					
				</div>
				<div class="row">
					<div class="col-sm-2">
						<button type="submit" class="btn btn-primary">Pesquisar</button>
						<button type="button" class="btn btn-outline-success" title="Exportar para CSV"	onclick="javascript:csv('listar', '/siga/app/pessoa/exportarCsv');"><i class="fa fa-file-csv"></i> Exportar</button>
					</div>
				</div>				

			
			</div>
		</div>
	
		<h3 class="gt-table-head">Pessoas cadastradas</h3>
		<div class="table-responsive">
			<table border="0" class="table table-sm table-striped">
				<thead class="thead-dark">
					<tr>
						<th align="left">Nome</th>						
						<th align="left">Unidade</th>
						<th align="left">Cargo</th>
						<th align="left">Função</th>						
						<th align="left">CPF</th>					
						<th align="left">Email</th>					
						<th class="text-center">Data de Nascimento</th>
						<th align="left">Matricula</th>
						<th colspan="2" class="text-center">Op&ccedil;&otilde;es</th>					
					</tr>
				</thead>
				<tbody>
					<siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
						itens="${itens}" var="pessoa">
						<tr>
							<td align="left">${pessoa.descricao}</td>						
							<td align="left"><span data-toggle="tooltip" data-placement="bottom" title="${pessoa.lotacao.siglaLotacao} / ${pessoa.lotacao.nomeLotacao}">${pessoa.lotacao.siglaLotacao}</span></td>
							<td align="left">${pessoa.cargo.nomeCargo}</td>
							<td align="left">${pessoa.funcaoString}</td>												
							<td align="left">${pessoa.cpfFormatado}</td>						
							<td align="left">${pessoa.emailPessoa}</td>						
							<td align="center"><fmt:formatDate pattern = "dd/MM/yyyy" value = "${pessoa.dataNascimento}" /></td>
							<td align="left">${pessoa.sigla}</td>
							<td align="center">
								<c:url var="url" value="/app/pessoa/editar">
									<c:param name="id" value="${pessoa.id}"></c:param>
								</c:url>
								<c:url var="urlAtivarInativar" value="/app/pessoa/ativarInativar">
									<c:param name="id" value="${pessoa.id}"></c:param>
								</c:url>																																					
								<div class="btn-group">								  
								  <c:choose>
									<c:when test="${empty pessoa.dataFimPessoa}">
										<a href="${urlAtivarInativar}" onclick="javascript:atualizarUrlDeInativarPessoa('${urlAtivarInativar}')" class="btn btn-primary" role="button" aria-pressed="true" data-toggle="modal" data-target="#modalDeConfirmacao">Inativar</a>
									</c:when>
									<c:otherwise>
										<a href="${urlAtivarInativar}" class="btn btn-primary" role="button" aria-pressed="true">Ativar</a>
									</c:otherwise>
								  </c:choose>								  								  
								  <button type="button" class="btn btn-primary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								    <span class="sr-only"></span>
								  </button>
								  <div class="dropdown-menu">
								  	<a href="${url}" class="dropdown-item" role="button" aria-pressed="true">Alterar</a>								   
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
				</tbody>
			</table>
		</div>		
		
		<div class="gt-table-buttons">
			<c:url var="url" value="/app/pessoa/editar"></c:url>
			<c:url var="urlAtivarInativar" value="/app/pessoa/ativarInativar"></c:url>
			<input type="button" value="Incluir" onclick="javascript:window.location.href='${url}'" class="btn btn-primary">
		</div>				
	</form>
	</div>
	
	<div class="modal fade" id="modalDeConfirmacao" tabindex="-1" role="dialog" aria-labelledby="confirmacao" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="confirmacao">Confirma&ccedil;&atilde;o</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        Deseja inativar o cadastro selecionado?
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-success" data-dismiss="modal">Não</button>		        
	        <a href="#" class="btn btn-danger btn-confirmacao-inativacao-cadastro" role="button" aria-pressed="true">Sim</a>
	      </div>
	    </div>
	  </div>
	</div>

<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
<script>
	function carregarRelacionados(id) {
		frm.method = "POST";
		frm.action = 'carregarCombos';
		frm.submit();
		frm.method = "GET";
	}
	
	function csv(id, action) {
		var frm = document.getElementById(id);
		frm.method = "POST";
		sbmtAction(id, action);
		frm.action = 'listar';
		frm.method = "GET";
	}
	
	function sbmtAction(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}
	
	function atualizarUrlDeInativarPessoa(url){	
		$('.btn-confirmacao-inativacao-cadastro').attr("href", url);		
	}
	
	$(document).ready(function() {				    
	    $(function () {
			$('[data-toggle="tooltip"]').tooltip()
		});
	});
</script>
</siga:pagina>