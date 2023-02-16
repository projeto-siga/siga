<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<fmt:message key="documento.transferencia.arquivados.lote" var="titulo" />
<siga:pagina titulo="${titulo}">
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>${titulo}</h5>
			</div>
			<div class="card-body">
				<c:choose>
					<c:when test="${not empty itens}">
						<form name="frm">
					</c:when>
					<c:otherwise>	
						<form name="frm" action="pesquisa_documentos_arquivados_transferencia" method="get">
					</c:otherwise>
				</c:choose>
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="paramoffset" value="0" /> 
					<input type="hidden" name="p.offset" value="0" />
					
					
					<h4>ORIGEM:</h4>											
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label for="tipoResponsavel">usuário/unidade</label> 
								<select class="custom-select"
									id="tipoResponsavel" name="tipoResponsavel"
									value="${tipoResponsavel}"
									onchange="javascript:updateTipoResponsavel();">
									<c:forEach var="item" items="${listaTipoRespOrigem}">
										<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
											${item.value}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>&nbsp;</label> 
								<c:if test="${tipoResponsavel == 1}">
									 <div id="lotaResponsavel" style="display:">
										<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
									</div> 
									<div id="responsavel" style="display: none;"> 
										<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
									</div>
								</c:if>
								<c:if test="${tipoResponsavel == 2}">
									<div id="lotaResponsavel" style="display: none">
										<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
									</div> 
									<div id="responsavel" style="display:"> 
										<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
									</div>
								</c:if>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${not empty itens}">
							<div class="row ">
								<div class="col-sm-3">
									<h4>DESTINO:</h4>
								</div>
							</div>
							<div class="row ">
								<div class="col-sm-3">
									<div class="form-group">
										<label for="tipoDestinatario">Unidade</label> 
										 <select class="custom-select"
											id="tipoDestinatario" name="tipoDestinatario"
											value="${tipoDestinatario}"
											onchange="javascript:updateTipoResponsavel();">
											<c:forEach var="item" items="${listaTipoRespDestino}">
												<option value="${item.key}">${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label>&nbsp;</label> 
										<siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga"/>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-9">
									<h4>Motivo da Transferência:</h4>
		                            <div class="form-group">
		                                <input type="text" size="30" name="motivoTransferencia" id="motivoTransferencia" class="form-control"/>
		                            </div>
		                        </div>
		                    </div>
		                    
		                    <div class="row">
								<div class="col-sm-1">
									<button type="button" id="btnTransferirArquivadosLote" class="btn btn-primary"
									onclick="javascript:validaCampos();" role="button">Transfererir</button>
								</div>
								<div class="col-sm-1 ml-3 my-2 my-sm-0">
		                    		<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-primary" />				
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="row">
								<div class="col-sm-1">
									<button type="submit" class="btn btn-primary">Pesquisar</button>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="gt-content-box gt-for-table">
						<br />
						<div>
							<table class="table table-hover table-striped">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th rowspan="2" align="center"><input type="checkbox"
											id="checkall" name="checkall" value="true"
											onclick="checkUncheckAll(this)" /></th>
										<th rowspan="2" class="text-right">Número</th>
										<th colspan="3">Documento</th>
										<th colspan="2">Última Movimentação</th>
										<th rowspan="2">Descrição</th>
										<th rowspan="2" class="col-5 d-none">Despacho <c:if
												test="${secao==0}" />
										</th>
									</tr>
									<tr>
										<th class="text-center">Data</th>
										<th class="text-center"><fmt:message
												key="usuario.lotacao" /></th>
										<th class="text-center"><fmt:message
												key="usuario.pessoa2" /></th>
										<th class="text-center">Data</th>
										<th class="text-center"><fmt:message
												key="usuario.pessoa2" /></th>
									</tr>
								</thead>
								<tbody class="table-bordered">
									<siga:paginador maxItens="${maxItems}" maxIndices="10"
										totalItens="${tamanho}" itens="${itens}" var="documento">
										<c:set var="x" scope="request">
												chk_${documento.idMobil}
											</c:set>
										<c:set var="tpd_x" scope="request">tpd_${documento.idMobil}</c:set>
										<tr>
											<td align="center" class="align-middle text-center"><input
												type="checkbox" name="documentosSelecionados"
												value="${documento.idMobil}" id="${x}" class="chkDocumento"
												onclick="javascript:displaySel(this, '${tpd_x}');" /></td>
											<td class="text-right"><c:choose>
													<c:when test='${param.popup!="true"}'>
														<a
															href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
															${documento.sigla} </a>
													</c:when>
													<c:otherwise>
														<a
															href="javascript:opener.retorna_${param.propriedade}('${documento.idMobil}','${documento.sigla},'');">
															${documento.sigla} </a>
													</c:otherwise>
												</c:choose></td>
											<c:if test="${not documento.geral}">
												<td class="text-center">${documento.doc.dtDocDDMMYY}</td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.lotaSubscritor.sigla}"
														descricao="${documento.doc.lotaSubscritor.descricao}" /></td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.subscritor.iniciais}"
														descricao="${documento.doc.subscritor.descricao}" /></td>
												<td class="text-center">${documento.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
														descricao="${documento.ultimaMovimentacaoNaoCancelada.resp.descricao}" />
												</td>
											</c:if>
											<c:if test="${documento.geral}">
												<td class="text-center">${documento.doc.dtDocDDMMYY}</td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.subscritor.iniciais}"
														descricao="${documento.doc.subscritor.descricao}" /></td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.lotaSubscritor.sigla}"
														descricao="${documento.doc.lotaSubscritor.descricao}" /></td>
												<td class="text-center"></td>
												<td class="text-center"></td>
												<td class="text-center"></td>
												<td class="text-center"></td>
											</c:if>
											<td>
												<c:choose>
													<c:when test="${siga_cliente == 'GOVSP'}">
														${documento.doc.descrDocumento}
													</c:when>
													<c:otherwise>
														${f:descricaoConfidencial(documento.doc, lotaTitular)}
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</siga:paginador>
								</tbody>
							</table>
						</div>
					</div>
					
					
				</form>
			</div>
		</div>
	</div>
	<siga:siga-modal id="confirmacaoModalTransferencia" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
		<div class="modal-body">
	      		<div class="form-group row">
				<div class="col-12">
					<p><strong>Transferência De:</strong></p>
					<p><span id="origem">X</span></p>
					<p><strong>Para:</strong></p>
					<p><span id="destino"></span></p>
					
					<p><strong>Quant. de Documentos:</strong> <span id="qtnDocumentosSelecionados"></span></p>
				</div>
			</div>
	    	</div>
	    	<div class="modal-footer">
	      		<button type="button" class="btn btn-danger" data-dismiss="modal">Não</button>		        
	      		<a href="#" class="btn btn-success btn-confirmacao-transferencia-cadastro" role="button" aria-pressed="true" onclick="confirmaTransferencia();">Sim</a>
		</div>
	</siga:siga-modal>
	
		<script type="text/javascript" language="Javascript1.1">
		function sbmt(offset) {
			if (offset == null) {
				offset = 0;
			}
	
			let form = document.forms['frm'];
			form ["paramoffset"].value = offset;
			form.action = "pesquisa_documentos_arquivados_transferencia";
			form.method = "GET";
			form ["p.offset"].value = offset;
	
			form.submit();
		}

		
	
		function checkUncheckAll(theElement) {
			let isChecked = theElement.checked;
			Array.from(document.getElementsByClassName("chkDocumento")).forEach(chk => chk.checked = isChecked);
		}

		function displaySel(chk, el) {
			document.getElementById("checkall").checked = 
			Array.from(document.getElementsByClassName("chkDocumento")).every(chk => chk.checked);
		}
		
		function tipoOrigemSelecionado(){
			return document.getElementById("tipoResponsavel");
		}
		
		function updateTipoResponsavel() {			
			document.getElementById("lotaResponsavel").style.display = 'none';
			document.getElementById("responsavel").style.display = 'none';
			
			limparSelecao();
			
			var objSelecionado = tipoOrigemSelecionado();
			
			switch (parseInt(objSelecionado.value)) {
			case 1:
				document.getElementById("lotaResponsavel").style.display = '';
				break;
			case 2:
				document.getElementById("responsavel").style.display = '';
				break;
			}
		}
		
		function limparSelecao() {
            document.getElementById('formulario_lotaResponsavelSel_id').value = "";
            document.getElementById('formulario_lotaResponsavelSel_sigla').value = "";
            document.getElementById('formulario_lotaResponsavelSel_descricao').value = "";
            document.getElementById('lotaResponsavelSelSpan').innerHTML = "";

            document.getElementById('formulario_responsavelSel_id').value = "";
            document.getElementById('formulario_responsavelSel_sigla').value = "";
            document.getElementById('formulario_responsavelSel_descricao').value = "";
            document.getElementById('responsavelSelSpan').innerHTML = "";
		}
		
		function getDestinoSelecionado(){
			var lotaDestinoSelDescr = document.querySelector("#formulario_lotacaoDestinatarioSel_descricao");
			console.log(lotaDestinoSelDescr.value);
			document.getElementById("destino").textContent = lotaDestinoSelDescr;
		}
		
		function getIdDocumentosSelecionados() {
			var els = document.getElementsByName("documentosSelecionados");
			var selecionados = new Array();
			for (var i = 0; i < els.length; i++) {
			  if (els[i].checked) {
				  selecionados.push(els[i].value);
			  }
			}
			return selecionados;
		}
		
		function atualizarUrlDeTransferenciaArquivados(url){	
			$('.btn-confirmacao-transferencia-cadastro').attr("href", url);		
		}
		
		function getOrigemSelecionado(){
			let responsavelSel = document.getElementById("formulario_responsavelSel_descricao").value;
			document.getElementById("origem").textContent = responsavelSel;
		}
		
		function getDestinoSelecionado(){
			let lotacaoDestinatarioSel = document.getElementById("formulario_lotacaoDestinatarioSel_descricao").value;
			document.getElementById("destino").textContent = lotacaoDestinatarioSel;
		}
		
		function qntDocumentosSelecionados(){
			let checkedElements = $("input[name='documentosSelecionados']:checked");
			document.getElementById("qtnDocumentosSelecionados").textContent = checkedElements.length;
			return checkedElements.length;
		}
		
		function validaCampos() {
			getOrigemSelecionado();
			getDestinoSelecionado();
			var motivo = document.getElementById("motivoTransferencia").value;
			var lotaDestinoSelId = document.getElementById("formulario_lotacaoDestinatarioSel_id").value;
			
			if (!motivo) {
				sigaModal.alerta('O campo MOTIVO é obrigatório!');
				return false;
			}
			
			if (motivo.length > 500) {
				sigaModal.alerta('Motivo da Transferência com mais de 500 caracteres');
				return false;
			}
			
			if (lotaDestinoSelId == "") {
				sigaModal.alerta('Necessário adicionar uma unidade de DESTINO para realizar a transferência.');
				return false;
			}
			
			if (qntDocumentosSelecionados() === 0) {
				sigaModal.alerta('Necessário selecionar pelo menos 1 documento para realizar a transferência.');
				return false;
			}
			
			sigaModal.abrir('confirmacaoModalTransferencia');
			return true;
		}
		
		function confirmaTransferencia(){
			atualizarUrlDeTransferenciaArquivados(transfereDocumentosSelecionados(getIdDocumentosSelecionados()));
		}
		
		function transfereDocumentosSelecionados(listaIdDocumentosSelecionados) {
			motivo = document.getElementById('motivoTransferencia').value;

			var responsavelSelId = document.getElementById("formulario_responsavelSel_id").value;
			var responsavelSelDescr = document.getElementById("formulario_responsavelSel_descricao").value;
			var responsavelSelSigla = document.getElementById("formulario_responsavelSel_sigla").value;
			
			var lotaResponsavelSelId = document.getElementById("formulario_lotaResponsavelSel_id").value;
			var lotaResponsavelSelDescr = document.getElementById("formulario_lotaResponsavelSel_descricao").value;
			var lotaResponsavelSelSigla = document.getElementById("formulario_lotaResponsavelSel_sigla").value;
			
			var lotaDestinoSelId = document.getElementById("formulario_lotacaoDestinatarioSel_id").value;
			var lotaDestinoSelDescr = document.getElementById("formulario_lotacaoDestinatarioSel_descricao").value;
			var lotaDestinoSelSigla = document.getElementById("formulario_lotacaoDestinatarioSel_sigla").value;
			
			$.ajax({
				method:'POST',
				async: false,
				url: '/sigaex/app/expediente/mov/transferirLoteDocumentosArquivados',
				data: {
					'lotaResponsavelSel.id': lotaResponsavelSelId,
				    'lotaResponsavelSel.descricao': lotaResponsavelSelDescr,
				    'lotaResponsavelSel.sigla': lotaResponsavelSelSigla,
					'responsavelSel.id': responsavelSelId,
			   		'responsavelSel.descricao': responsavelSelDescr,
			      	'responsavelSel.sigla': responsavelSelSigla,
			      	'lotacaoDestinatarioSel.id': lotaDestinoSelId,
			   		'lotacaoDestinatarioSel.descricao': lotaDestinoSelDescr,
			      	'lotacaoDestinatarioSel.sigla': lotaDestinoSelSigla,
					'documentosSelecionados':listaIdDocumentosSelecionados,
					'motivoTransferencia':motivo},
				beforeSend: function(result){
					console.log('carregando..');
					sigaSpinner.mostrar();
					sigaModal.fechar('confirmacaoModalTransferencia');
		        },
				success: function(result){	
					location.reload();
		        },
		        error: function(err){
		            console.log(err);
		         },
		        complete: function(result){	
		        	sigaSpinner.ocultar();
		        }
			});
		}
	</script>
	
</siga:pagina>