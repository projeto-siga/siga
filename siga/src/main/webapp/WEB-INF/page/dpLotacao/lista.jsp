<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Listar Lota&ccedil;&atilde;o">
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
	
	function checkUncheckAll(theElement) {
		var theForm = theElement.form, z = 0;
		for (z = 0; z < theForm.length; z++) {
			if (theForm[z].type == 'checkbox' && theForm[z].id.startsWith('chk_')) {
				theForm[z].checked = !(theElement.checked);
				theForm[z].click();
			}
		}
	}
	
</script>
	<!-- main content -->
	<div class="container-fluid">
		<form name="frm" action="listar" id="listar" class="form100" method="GET">
		<input type="hidden" name="paramoffset" value="0" />
		<input type="hidden" name="p.offset" value="0" />
		<div class="card bg-light mb-3" >
			<div class="card-header mb-4">
				<h5>Cadastro de <fmt:message key="usuario.lotacao"/></h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-md-4">
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
					<div class="col-md-4">
						<div class="form-group">
							<label>Nome ou Sigla</label>
							<input type="text" id="nome" name="nome" value="${nome}" maxlength="100" class="form-control"/>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<div class="form-check">
				    	<input class="form-check-input" type="checkbox" value="1" id="apenasAptasInativacao" name="apenasAptasInativacao" <c:if test="${apenasAptasInativacao}">checked</c:if> />
				     	<label class="form-check-label" for="apenasAptasInativacao">
				        	Listar apenas <fmt:message key="usuario.lotacao"/> aptas para inativação
				      	</label>
					</div>
				</div>
				
				<div class="row">
					<div class="col col-12 col-md-6">
						<div class="form-group">
							<input value="Pesquisar" class="btn btn-primary" onclick="javascript: sbmt(0);"/>
							<c:if test="${temPermissaoParaExportarDados and not apenasAptasInativacao}">
								<button type="button" class="btn btn-outline-success" id="exportarCsv" title="Exportar para CSV" onclick="javascript:csv('listar', '/siga/app/lotacao/exportarCsv');"><i class="fa fa-file-csv"></i> Exportar</button>
							</c:if>							
						</div>
					</div>
					
					<div class="col col-12 col-md-6 text-right">
						<c:url var="urlInativarLote" value="/app/pessoa/inativarLote">
						</c:url>
							
						<c:if test="${apenasAptasInativacao and podeInativarLotacaoLote and not empty itens}">
							<button type="button" class="btn btn-primary" 
								onclick="javascript:atualizarUrlDeInativarLotacao('javascript:inativarLotacoesSelecionadas(getIdLotacoesSelecionadas());');return false;"
								role="button" id="btnInativarLote"
								aria-pressed="true" data-siga-modal-abrir="confirmacaoModalInativacao">Inativar itens selecionados</button>			
						</c:if>
					</div>
				</div>			
			</div>
		</div>
		
		<div id="headerResultado">
			<h3 class="gt-table-head"><fmt:message key="usuario.lotacoes"/> cadastradas</h3>
		</div>
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
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
				<tr>
					<c:if test="${apenasAptasInativacao and podeInativarLotacaoLote and not empty itens}">
						<th align="center" style="text-align:center;vertical-align:middle;">
							<div class="form-group">
							    <div class="form-check">
								    <input class="form-check-input" type="checkbox" id="checkall" name="checkall" value="true" onclick="checkUncheckAll(this)" />
							    </div>
							 </div>
						</th>
					</c:if>
					<th align="left">Nome</th>
					<th align="left">Sigla</th>
					<th align="left">Externa</th>
					<th align="left">Suspensa</th>
					<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
				</tr>
			</thead>
			
			<tbody>
				<siga:paginador maxItens="${quantidadeLista}" maxIndices="${maxIndices}" totalItens="${tamanho}"
					itens="${itens}" var="lotacao">
					<tr>
						<c:if test="${apenasAptasInativacao and podeInativarLotacaoLote}">
							<td align="center">
							   <div class="form-group">
							    <div class="form-check">
							      <input class="form-check-input" name="locacaoSelecionada" type="checkbox" id="chk_${lotacao.id}" value="${lotacao.id}" />
							    </div>
							  </div>
							</td>
						</c:if>
						<td align="left">${lotacao.descricao}</td>
						<td align="left">${lotacao.sigla}</td>
						<td align="left">${lotacao.isExternaLotacao == 1 ? 'SIM' : 'NÃO'}</td>
						<td align="left">${lotacao.isSuspensa == 1 ? 'SIM' : 'NÃO'}</td>
						<td align="left">
							<c:url var="url" value="/app/lotacao/editar">
								<c:param name="id" value="${lotacao.id}"></c:param>
							</c:url>

							<c:url var="urlSuspender" value="/app/lotacao/suspender">
								<c:param name="id" value="${lotacao.id}"></c:param>
							</c:url>
							
							<div class="btn-group">								  
							  <c:choose>
								<c:when test="${empty lotacao.dataFimLotacao}">
									<c:url var="urlAtivarInativar" value="/app/lotacao/inativar">
										<c:param name="id" value="${lotacao.id}"></c:param>
									</c:url>
									
									<a href="${urlAtivarInativar}" class="btn btn-primary" role="button" aria-pressed="true" style="min-width: 80px;">Inativar</a>
									<button type="button" class="btn btn-primary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									    <span class="sr-only"></span>
								    </button>
								</c:when>
								<c:otherwise>
									<c:url var="urlAtivarInativar" value="/app/lotacao/ativar">
										<c:param name="id" value="${lotacao.id}"></c:param>
									</c:url>
									
									<a href="${urlAtivarInativar}" class="btn btn-danger" role="button" aria-pressed="true" style="min-width: 80px;">Ativar</a>
									<button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									    <span class="sr-only"></span>
								    </button>
								</c:otherwise>
							  </c:choose>	  
							  <div class="dropdown-menu">
							  	
							  <c:choose>
								<c:when test="${empty lotacao.isSuspensa or lotacao.isSuspensa == 0}">
									<a href="${urlSuspender}" onclick='javascript:atualizarUrl("javascript:submitPost(\"${urlSuspender}\")", "Deseja tornar a <fmt:message key="usuario.lotacao"/> Suspensa para o recebimento de Documentos?");return false;'  
										class="dropdown-item" role="button" aria-pressed="true" data-siga-modal-abrir="confirmacaoModal" style="min-width: 80px;">Suspender Tramita&ccedil;&atilde;o</a>
								</c:when>
								<c:otherwise>
								<a href="${urlSuspender}" onclick='javascript:atualizarUrl("javascript:submitPost(\"${urlSuspender}\")", "Deseja desfazer a Suspensão da <fmt:message key="usuario.lotacao"/>");return false;'  
										class="dropdown-item" role="button" aria-pressed="true" data-siga-modal-abrir="confirmacaoModal" style="min-width: 80px;">Ativar Tramita&ccedil;&atilde;o</a>
								</c:otherwise>
							  </c:choose>
							  
							  	<a href="${url}" class="dropdown-item" role="button" aria-pressed="true">Alterar</a>								   
							  </div>
							</div>				
						</td>						
					</tr>
				</siga:paginador>
				
				<c:if test="${apenasAptasInativacao and podeInativarLotacaoLote and tamanho gt 0}">
					<c:if test="${currentPageNumber ne 0 and not paginaInicial}">
						<button type="button" class="btn btn-primary btn-sm active mr-1 mb-3"
							onclick="javascript:sigaSpinner.mostrar();sbmt(${0});"><i class="fa fa-arrow-circle-left" aria-hidden="true"></i> Voltar para o início</button>
					</c:if>		
					<c:if test="${(currentPageNumber * itemPagina) lt tamanho}">
						<button type="button" class="btn btn-primary btn-sm active mr-1 mb-3"
							onclick="javascript:sigaSpinner.mostrar();sbmt(${currentPageNumber * itemPagina});">Mais <i class="fa fa-arrow-circle-right" aria-hidden="true"></i></button>
					</c:if>

				</c:if>
					
				<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
					<div id="msg" class="modal-body">
			       		Deseja tornar a <fmt:message key="usuario.lotacao"/> "Suspensa" para o recebimento de Documentos?
			     	</div>
			     	<div class="modal-footer">
			       		<button type="button" class="btn btn-danger" data-dismiss="modal">Não</button>		        
			       		<a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true">Sim</a>
					</div>
				</siga:siga-modal>
			</tbody>
		</table>				
		<div class="gt-table-buttons">
			<c:url var="url" value="/app/lotacao/editar"></c:url>
			<c:url var="urlAtivarInativar" value="/app/lotacao/inativar"></c:url>
			<input type="button" value="Incluir" onclick="javascript:window.location.href='${url}'" class="btn btn-primary">
		</div>				
		</form>
	</div>

	<siga:siga-modal id="confirmacaoModalInativacao" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
		<div class="modal-body">
	      		<div class="form-group row">
				<div class="col-12">
					<p><strong>Deseja inativar os cadastros selecionados?</strong></p>
					<label for="motivo">Motivo</label>
					<textarea placeholder="Preencher o campo com o motivo da Inativação" class="form-control" name="motivo" id="motivo" cols="60" rows="2"></textarea>
				</div>
			</div>
	    	</div>
	    	<div class="modal-footer">
	      		<button type="button" class="btn btn-success" data-dismiss="modal">Não</button>		        
	      		<a href="#" class="btn btn-danger btn-confirmacao-inativacao-cadastro" role="button" aria-pressed="true">Sim</a>
		</div>
	</siga:siga-modal>

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
		
		function atualizarUrlDeInativarLotacao(url){	
			$('.btn-confirmacao-inativacao-cadastro').attr("href", url);		
		}
		
		function getIdLotacoesSelecionadas() {
			var els = document.getElementsByName("locacaoSelecionada");
			var selecionados = new Array();
			for (var i = 0; i < els.length; i++) {
			  if (els[i].checked) {
				  selecionados.push(els[i].value);
			  }
			}
			return selecionados;
		}
		
		function validaMotivo() {
			var motivo = document.getElementById("motivo").value;
			if (motivo.length > 500) {
				sigaModal.alerta('Motivo com mais de 500 caracteres');
				return false;
			}
			return true;
		}
		
		function inativarLotacoesSelecionadas(listaIdLotacoesSelecionadas) {
			motivo = document.getElementById('motivo').value;
			
			if (validaMotivo()) {
				$.ajax({
					method:'POST',
					url: '/siga/app/lotacao/inativarLote',
					data: {'idLotacoesSelecionadas':listaIdLotacoesSelecionadas,'motivo':motivo},
					beforeSend: function(result){	
						document.getElementById("btnInativarLote").disabled = true;
						sigaSpinner.mostrar();
						sigaModal.fechar('confirmacaoModalInativacao');
			        },
					success: function(result){	
						location.reload();
			        },
					error: 'erro',
			        complete: function(result){	
			        	document.getElementById("btnInativarLote").disabled = false;
			        	sigaSpinner.ocultar();
			        }
				});
			}
		}
		
	
	</script>
</siga:pagina>
