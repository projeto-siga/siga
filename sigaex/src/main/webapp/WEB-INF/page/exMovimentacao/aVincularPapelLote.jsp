<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:message key="documento.vinculacao.lote" var="titulo"/>

<siga:pagina titulo="${titulo}">

     <script type="text/javascript">
     	var cont = 0;
     	var qtdTotalExecucao = 0;
		var listaResponsavel = new Array();
        var listaExecucaoLote = new Array();
        var strHtmlTableStatus = '<br /><br />' +
								'<div class="row">' +
						        	'<div class="col-sm-7">' +
										'<h5>Status de Processamento - Acompanhamento do Documento em Lote</h5>' +
									'</div>' +
						        	'<div class="col-sm-1">' +
										'<button type="button" onclick="javascript:gerarTableStatus(false);" title="Status de Processamento" class="btn btn-secondary btn-sm mb-1 ml-1 mr-2"><i class="fas fa-sync-alt"></i></button>' +
									'</div>' +
									'<div class="col-sm-4" id="idDivAvisoStatus"> </div>' +
						        '</div>' +
								'<div>' +
									'<table class="table table-hover table-striped" id="idTableStatus">' +
										'<thead class="thead-dark align-middle text-center">' +
											'<tr>' +
												'<th class="text-center" style="width: 10%;">Matrícula</th>' +
												'<th class="text-center">Unidade</th>' +
												'<th class="text-center">Número</th>' +
												'<th class="text-center">Status</th>' +
												'<th class="text-center">Descrição erro</th>' +
											'</tr>' +
										'</thead>' +
										'<tbody class="table-bordered" id="idTbodyStatus">' +					
										'</tbody>' +
									'</table>' +
								'</div>';
		var strDivAvisoStatusProc = '<span><i class="fa fa-exclamation-triangle text-danger"></i></span>    Atualize a tabela em instantes, processamento em execução...';
		
		function sbmt(offset) {
				if (offset == null) {
					offset = 0;
				}
				let form = document.forms['frm'];
				form ["paramoffset"].value = offset;
				form.action = "vincularPapelLote";
				form.method = "GET";
				form ["p.offset"].value = offset;
		
				form.submit();
		}
		
		function alteraResponsavel() {
			var objSelecionado = document.getElementById('tipoResponsavel');
			
			switch (parseInt(objSelecionado.value)) {
				case 1:
					document.getElementById('selecaoResponsavel').style.display = '';
					document.getElementById('selecaoLotaResponsavel').style.display = 'none';
					document.getElementById('formulario_lotaResponsavelSel_sigla').value = '';
					document.getElementById('lotaResponsavelSelSpan').textContent = '';
					document.getElementById('formulario_lotaResponsavelSel_id').value = '';
					document.getElementById("formulario_lotaResponsavelSel_descricao").value ='';
					break;
				case 2:
					document.getElementById('selecaoResponsavel').style.display = 'none';
					document.getElementById('selecaoLotaResponsavel').style.display = '';
					document.getElementById('formulario_responsavelSel_sigla').value = '';
					document.getElementById('responsavelSelSpan').textContent = '';
					document.getElementById('formulario_responsavelSel_id').value = '';
					document.getElementById("formulario_responsavelSel_descricao").value ='';
					break;
			}
		}
		
		function limparCamposSelResponsavel() {
			document.getElementById("formulario_responsavelSel_sigla").value = "";
			document.getElementById("formulario_responsavelSel_id").value = "";
			document.getElementById("formulario_responsavelSel_descricao").value = "";
			$("#responsavelSelSpan").html("");
			document.getElementById('formulario_lotaResponsavelSel_sigla').value = '';
			document.getElementById("formulario_lotaResponsavelSel_id").value = "";
			document.getElementById("formulario_lotaResponsavelSel_descricao").value = "";
			$("#lotaResponsavelSelSpan").html("");
		}
		
		function inserirResponsavelTable(){
			sigaSpinner.mostrar();
			
			var responsavelSelSigla = document.getElementById("formulario_responsavelSel_sigla")
			var responsavelSelId = document.getElementById("formulario_responsavelSel_id");
			var responsavelSelDescr = document.getElementById("formulario_responsavelSel_descricao");
			var lotaResponsavelSelSigla = document.getElementById("formulario_lotaResponsavelSel_sigla");
			var lotaResponsavelSelId = document.getElementById("formulario_lotaResponsavelSel_id");
			var lotaResponsavelSelDescr = document.getElementById("formulario_lotaResponsavelSel_descricao");
			
			var idPapel = document.getElementById("idPapel"); 
			var tipoResponsavel = document.getElementById("tipoResponsavel"); 
			
			if (isNullOrVazio(responsavelSelId.value) && isNullOrVazio(lotaResponsavelSelId.value)) {									
				sigaModal.alerta("Atenção! Informe um responsável");
				sigaSpinner.ocultar();
				return;	
			}
			
			if (existeResponsavelArray(responsavelSelId.value, lotaResponsavelSelId.value, tipoResponsavel.value)){
				sigaModal.alerta("Atenção! Responsável já foi incluído na tabela");
				sigaSpinner.ocultar();
				return;	
			}
			
			if (listaResponsavel.length >= 3){
				sigaModal.alerta("Atenção! Você pode incluir 3 Responsáveis para Acompanhamento de Documentos");
				sigaSpinner.ocultar();
				return;	
			}
			
			popularResponsavelMap(responsavelSelSigla, responsavelSelId, responsavelSelDescr, lotaResponsavelSelSigla,
					lotaResponsavelSelId, lotaResponsavelSelDescr, idPapel, tipoResponsavel);
			
			gerarTableResponsavel();
			limparCamposSelResponsavel();
			
			sigaSpinner.ocultar();
		}
		
		function popularResponsavelMap(responsavelSelSigla, responsavelSelId, responsavelSelDescr, lotaResponsavelSelSigla,
				lotaResponsavelSelId, lotaResponsavelSelDescr, idPapel, tipoResponsavel){
			var myMap = new Map();
			myMap.set("responsavelSelSigla", responsavelSelSigla.value);
			myMap.set("responsavelSelId", responsavelSelId.value);
			myMap.set("responsavelSelDescr", responsavelSelDescr.value);
			myMap.set("lotaResponsavelSelSigla", lotaResponsavelSelSigla.value);
			myMap.set("lotaResponsavelSelId", lotaResponsavelSelId.value);
			myMap.set("lotaResponsavelSelDescr", lotaResponsavelSelDescr.value);
			myMap.set("idPapel", idPapel.value);
			myMap.set("tipoResponsavel", tipoResponsavel.value);
			
			var myJson = {};
			myJson = mapToObj(myMap);
			
			listaResponsavel.push(myJson);
			localStorage.setItem('listaResponsavelJson', JSON.stringify(listaResponsavel));
		}
		
		function existeResponsavelArray(responsavelSelId, lotaResponsavelSelId, tipoResponsavel){
			let respSelId = listaResponsavel.find(o => o.responsavelSelId === responsavelSelId);
			let lotaRespSelId = listaResponsavel.find(o => o.lotaResponsavelSelId === lotaResponsavelSelId);
			
			if((!isNullOrVazio(respSelId) && tipoResponsavel == 1) 
						|| (!isNullOrVazio(lotaRespSelId) && tipoResponsavel == 2))
				return true;
			return false;
		}
		
		function removerResponsavel(index){
			listaResponsavel.splice(index, 1);
			localStorage.setItem('listaResponsavelJson', JSON.stringify(listaResponsavel));
			gerarTableResponsavel();
		}
		
		function checkUncheckGestorInter(theElement) {
		    let isChecked = theElement.checked;
		    let form = document.forms['frm'];
			form.action = "vincularPapelLote";
			form.method = "GET";
			form ["chkGestorInteressado"].value = isChecked;
			form.submit();
		}
		
		function checkUncheckAll(theElement) {
		    let isChecked = theElement.checked;
		    Array.from(document.getElementsByClassName('chkDocumento')).forEach(chk => chk.checked = isChecked);
		}
		
		function displaySel() {
		    document.getElementById('checkall').checked =
		        Array.from(document.getElementsByClassName('chkDocumento')).every(chk => chk.checked);
		}
		
		function validar() {
			if (!Array.isArray(listaResponsavel) || !listaResponsavel.length) {
				sigaModal.alerta("Atenção! Informe pelo menos um responsável");
				return;
			}
			
			var checkedElements = $("input[name='documentosSelecionados']:checked");
			if (checkedElements.length === 0) {
			    sigaModal.alerta('Selecione pelo menos um documento');
			    return;
			} else {
			    sigaModal.abrir('confirmacaoModal');
			}
		}
		
		function confirmar() {
		    $("#btnOk").prop("disabled", true);
		    $("#btnIncluir").prop("disabled", true);
		    sigaModal.fechar('confirmacaoModal');
		    vincularPapeisLote();
		}
		
		function vincularPapeisLote(){
			listaExecucaoLote = new Array();
			qtdTotalExecucao = 0;
			cont = 0;
			process.reset();
			
		 	process.push(function () {
                $('#progressModal').modal({
                    backdrop: 'static',
                    keyboard: false
                });
           	});

            let arrayDocs = Array.from($(".chkDocumento:checkbox").filter(":checked"));
			qtdTotalExecucao = arrayDocs.length * listaResponsavel.length;

			listaResponsavel.forEach(
            		res => {
            			arrayDocs.forEach(
			                    chk => {
			                        process.push(function () {
			                            return ExecutarPost(chk.value, res.tipoResponsavel, res.responsavelSelId, 
			                            		res.responsavelSelDescr, res.responsavelSelSigla, res.lotaResponsavelSelId, 
			                            		res.lotaResponsavelSelDescr, res.lotaResponsavelSelSigla, res.idPapel);			
			                        });
			                        process.push(function () {
			                            chk.checked = false;
			                        });
			                    }
			             );
            		}
			);
            
            process.push(function () {
            	$("#btnOk").prop("disabled", false);
    		    $("#btnIncluir").prop("disabled", false);
    		    $('#checkall').prop('checked', false);
    		    gerarTableStatus(true);
    		    sigaModal.fechar('progressModal');
    		    location.href = "#idTableStatus";
            });

            process.run();
		}
		
		function ExecutarPost(documentoSelSigla, tipoResponsavel, responsavelSelId, responsavelSelDescr, responsavelSelSigla,
				lotaResponsavelSelId, lotaResponsavelSelDescr, lotaResponsavelSelSigla, idPapel) {
			$.ajax({
				  url: '${pageContext.request.contextPath}/app/expediente/mov/vincularPapel_gravar_ajax',
				  type: 'POST',
				  data: {
					  'postback': 1,
				      'sigla': documentoSelSigla,
				      'tipoResponsavel': tipoResponsavel,
				      'responsavelSel.id': responsavelSelId,
				      'responsavelSel.descricao': responsavelSelDescr,
				      'responsavelSel.sigla': responsavelSelSigla,
				      'lotaResponsavelSel.id': lotaResponsavelSelId,
				      'lotaResponsavelSel.descricao': lotaResponsavelSelDescr,
				      'lotaResponsavelSel.sigla': lotaResponsavelSelSigla,
				      'idPapel': idPapel
				  },
				  success: function () {
					  adicionarListaExecucaoLote(++cont, responsavelSelSigla, lotaResponsavelSelSigla, documentoSelSigla, "OK", "");
				  },
				  error: function (err) {
				      adicionarListaExecucaoLote(++cont, responsavelSelSigla, lotaResponsavelSelSigla, documentoSelSigla, "ERRO", err.responseText);
				  }
			});
		}
		
		function adicionarListaExecucaoLote(idSeq, responsavelSelSigla, lotaResponsavelSelSigla, nrDoc, status, descrErr){
			var myMap = new Map();
			myMap.set("idSeq", idSeq);
			myMap.set("responsavelSelSigla", responsavelSelSigla);
			myMap.set("lotaResponsavelSelSigla", lotaResponsavelSelSigla);
			myMap.set("nrDoc", nrDoc);
			myMap.set("status", status);
			myMap.set("descrErr", descrErr);
			
			var myJson = mapToObj(myMap);
			listaExecucaoLote.push(myJson);	
			localStorage.setItem('listaExecucaoLote', JSON.stringify(listaExecucaoLote));
		}
		
		function gerarTableResponsavel(){
			var data = JSON.parse(localStorage.getItem('listaResponsavelJson'));
			var idx = 0;
			const warehouseQuant = data =>
			  document.getElementById("idTbodyResponsavel").innerHTML = data.map(
			    item => ([
			      '<tr>',
			      ['responsavelSelSigla','responsavelSelDescr', 'lotaResponsavelSelDescr'].map(
			        key => '<td>'+item[key]+'</td>'
			      ),
			      "<td><button type='button' class='btn btn-danger' onclick='javascript:removerResponsavel(".concat(idx++,");'>Excluir</button></td>"),
			      '</tr>',
			    ])
			  ).flat(Infinity).join('');
			  
			warehouseQuant(data);
		}
		
		function gerarTableStatus(exibeMsg){
			var data = JSON.parse(JSON.stringify(listaExecucaoLote));
			var idx = 0;
			
			document.getElementById("idTableStatus").innerHTML = strHtmlTableStatus;
			
			if (listaExecucaoLote.length < qtdTotalExecucao)
				document.getElementById("idDivAvisoStatus").innerHTML = '<small>' + strDivAvisoStatusProc + ' ' + listaExecucaoLote.length + ' de ' + qtdTotalExecucao + ' registros</small>';
			
			const warehouseQuant = data =>
			  document.getElementById("idTbodyStatus").innerHTML = data.map(
			    item => ([
			      '<tr>',
			      ['responsavelSelSigla','lotaResponsavelSelSigla','nrDoc','status','descrErr'].map(
			        key => '<td>'+item[key]+'</td>'
			      ),
			      '</tr>',
			    ])
			  ).flat(Infinity).join('');
			  
			warehouseQuant(data);
			
			if (exibeMsg && listaExecucaoLote.length >= qtdTotalExecucao)
				sigaModal.alerta("Processamento de Acompanhamento em Lote realizado com sucesso!");
		}
		
		//Functions Utils
		function isTableStatusPopulado(tempoSec){
			var start = new Date().getTime();
        	var end = start;
			do {
        		end = new Date().getTime();
        		if(end > start + (tempoSec * 1000)){
        			break;
        		}
        	} while (listaExecucaoLote.length < qtdTotalExecucao);
        	
        	return true;
		}
		
		function isNullOrVazio(obj){
			if (obj == null || obj == "")
				return true;
			return false;
		}
		
		function mapToObj(map){
			  const obj = {}
			  for (let [k,v] of map)
			    obj[k] = v
			  return obj
		}
	</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>${titulo}</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="vincularPapel_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="paramoffset" value="0" /> 
					<input type="hidden" name="p.offset" value="0" />
					<div class="row">
						<c:if test="${siga_cliente != 'GOVSP'}">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label for="dtMovString">Data</label>
								<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this,0);" class="form-control"/>
							</div>
						</div>
						</c:if>
						<div class="col-sm-3">
							<div class="form-group">
								    <label>Responsável</label>
									<select class="form-control" id="tipoResponsavel"  name="tipoResponsavel" onchange="javascript:alteraResponsavel();">
										<c:forEach items="${listaTipoRespPerfil}" var="item">
											<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
												${item.value}
											</option>  
										</c:forEach>
									</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
									<span id="selecaoResponsavel">
										<label>&nbsp;</label>
										<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
									</span>
									<span id="selecaoLotaResponsavel">
										<label>&nbsp;</label>
										<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
									</span>		
									<script>alteraResponsavel();</script>			  
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label for="idPapel">Perfil</label>
								<select class="form-control" name="idPapel" id="idPapel">
									<c:forEach items="${listaExPapel}" var="item">
										<option value="${item.idPapel}" ${item.idPapel == idPapel ? 'selected' : ''}>
											${item.descPapel}
										</option>  
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-2 align-self-center text-left">
							<input type="button" value="Incluir" id="btnIncluir" onclick="javascript:inserirResponsavelTable();" class="btn btn-primary" />
						</div>
						<div class="align-self-center col-md-auto">
							<a href="javascript:validar();" class="btn btn-success form-control">
								<i class="fas fa-poll-h mr-1"></i>Executar Processamento
							</a>
						</div>
					</div>

					<div class="gt-content-box gt-for-table">
						<br />
						<h5>Usuário(s)/Unidade(s) adicionado(s)</h5>
						<div>
							<table class="table table-hover table-striped" id="idTableResponsavel">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th class="text-center" style="width: 10%;">Matrícula</th>
										<th class="text-center" style="width: 41%;">Nome</th>
										<th class="text-center" style="width: 41%;">Unidade</th>
										<th class="text-center" style="width: 8%;">Excluir</th>
									</tr>
								</thead>
								<tbody class="table-bordered" id="idTbodyResponsavel">
									
								</tbody>
							</table>
						</div>
					</div>
<!-- 					<div class="row"> -->
<!-- 						<div class="col-sm-6 mt-5 text-left"> -->
<!-- 							<input type="checkbox" name="chkGestorInteressado" value="" onclick="javascript:checkUncheckGestorInter(this);"> -->
<!-- 							<label class="form-check-label" for="substituicao">Mostrar apenas Documentos que estou como Interessado/Gestor</label>  -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="gt-content-box gt-for-table mt-2">
						<br />
						<br />
						<div>
							<table class="table table-hover table-striped" id="idTableDocumento">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th rowspan="2" class="text-center" style="width: 5%;">
											<input type="checkbox" 	id="checkall" name="checkall" value="true"
											onclick="checkUncheckAll(this)" />
										</th>
										<th class="text-center" style="width: 15%;" colspan="1">Número</th>
										<th class="text-center" style="width: 25%;" colspan="4">Cadastrante</th>
										<th class="text-center" style="width: 25%;" rowspan="2">Descrição</th>
									</tr>
									<tr>
										<th class="text-center" style="width: 16%;"></th>
										<th class="text-center">Data</th>
										<th class="text-center"><fmt:message key="usuario.lotacao" /></th>
										<th class="text-center"><fmt:message key="usuario.pessoa2" /></th>
										<th class="text-center">Tipo</th>
									</tr>
								</thead>
								<tbody class="table-bordered">
									<siga:paginador maxItens="${maxItems}" maxIndices="10"
										totalItens="${tamanho}" itens="${itens}" var="documento">
										<c:set var="x" scope="request">chk_${documento.id}</c:set>
										<c:set var="tpd_x" scope="request">tpd_${documento.id}</c:set>
										<tr>
											<td align="center" class="align-middle text-center"><input
												type="checkbox" name="documentosSelecionados"
												value="${documento.codigoCompacto}" id="${x}" class="chkDocumento"
												onclick="javascript:displaySel();" /></td>
											<td class="text-center align-middle"><c:choose>
													<c:when test='${param.popup!="true"}'>
														<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
															${documento.sigla} </a>
													</c:when>
													<c:otherwise>
														<a
															href="javascript:opener.retorna_${param.propriedade}('${documento.id}','${documento.sigla},'');">
															${documento.sigla} </a>
													</c:otherwise>
												</c:choose></td>
											<td class="text-center">${documento.doc.dtDocDDMMYY}</td>
											<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.lotaSubscritor.sigla}"
														descricao="${documento.doc.lotaSubscritor.descricao}" />
											</td>
											<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.subscritor.iniciais}"
														descricao="${documento.doc.subscritor.descricao}" />
											</td>
											<td>
												${documento.doc.exFormaDocumento.descrFormaDoc}
											</td>
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
					
					<div class="gt-content-box gt-for-table" id="idTableStatus">
					</div>
					
				</form>
			</div>
		</div>
		<siga:siga-modal id="confirmacaoModal" exibirRodape="false"
                         tituloADireita="Confirma&ccedil;&atilde;o" linkBotaoDeAcao="#">
	         <div class="modal-body">
	             O(s) documento(s) selecionado(s) será(ão) acompanhado(s) pelo(s) respectivo(s) responsável(eis). Deseja, confirmar?
	         </div>
	         <div class="modal-footer">
	             <button type="button" class="btn btn-danger" data-dismiss="modal">N&atilde;o</button>
	             <a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true"
	                onclick="confirmar();">
	                 Sim</a>
	         </div>
	     </siga:siga-modal>
	     <siga:siga-modal id="progressModal" exibirRodape="false" centralizar="true" tamanhoGrande="true"
                         tituloADireita="Acompanhamento em lote" linkBotaoDeAcao="#" botaoFecharNoCabecalho="false">
            <div class="modal-body">
                <div id="progressbar-ad"></div>
            </div>
        </siga:siga-modal>
	</div>
    
</siga:pagina>
 <script type="text/javascript">
    window.onload = function () { 
			localStorage.removeItem('listaResponsavelJson');
			localStorage.removeItem('listaExecucaoLote');
	} 
    
    let process = {
            steps: [],
            index: 0,
            title: "Executando a Acompanhamento do Documento em lote dos documentos selecionados",
            errormsg: "Não foi possível completar a operação",
            urlRedirect: null,
            reset: function () {
                this.steps = [];
                this.index = 0;
            },
            push: function (x) {
                this.steps.push(x);
            },
            run: function () {
                this.progressbar = $('#progressbar-ad').progressbar();
                this.nextStep();
            },
            finalize: function () {
                this.dialogo.dialog('destroy');
            },
            nextStep: function () {
                if (typeof this.steps[this.index] == 'string')
                    eval(this.steps[this.index++]);
                else {
                    let ret = this.steps[this.index++]();
                    if ((typeof ret == 'string') && ret != "OK") {
                        this.finalize();
                        alert(ret, 0, this.errormsg);
                        return;
                    }
                }

                this.progressbar.progressbar("value",
                    100 * (this.index / this.steps.length));

                if (this.index != this.steps.length) {
                    let me = this;
                    window.setTimeout(function () {
                        me.nextStep();
                    }, 100);
                } else {
                    this.finalize();
                }
            }
        };
</script>


