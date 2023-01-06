<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:message key="documento.vinculacao" var="titulo"/>

<siga:pagina titulo="${titulo}">
	<script type="text/javascript" src="/sigaex/javascript/sequential-ajax-calls.js"></script>

     <script type="text/javascript">
		var array = new Array();
        var siglasDocumentosTransferidos = new Array();
        var siglasDocumentosNaoTransferidos = new Array();
		
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
		
		function tamanho() {
			var i = tamanho2();
			if (i<0) {i=0};
			document.getElementById("Qtd").innerText = 'Restam ' + i + ' Caracteres';
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
			
			var myMap = new Map();
			myMap.set("responsavelSelSigla", responsavelSelSigla.value);
			myMap.set("responsavelSelId", responsavelSelId.value);
			myMap.set("responsavelSelDescr", responsavelSelDescr.value);
			myMap.set("lotaResponsavelSelSigla", lotaResponsavelSelSigla.value);
			myMap.set("lotaResponsavelSelId", lotaResponsavelSelId.value);
			myMap.set("lotaResponsavelSelDescr", lotaResponsavelSelDescr.value);
			myMap.set("idPapel", idPapel.value);
			myMap.set("tipoResponsavel", tipoResponsavel.value);
			myMap.set("vazio","");
			
			var myJson = {};
			myJson = mapToObj(myMap);
			array.push(myJson);	
			
			localStorage.setItem('dataRespJson', JSON.stringify(array));
			gerarTable();
			limparCamposSelResponsavel();
			
			sigaSpinner.ocultar();
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
		
		function existeResponsavelArray(responsavelSelId, lotaResponsavelSelId, tipoResponsavel){
			let respSelId = array.find(o => o.responsavelSelId === responsavelSelId);
			let lotaRespSelId = array.find(o => o.lotaResponsavelSelId === lotaResponsavelSelId);
			
			if((!isNullOrVazio(respSelId) && tipoResponsavel == 1) 
						|| (!isNullOrVazio(lotaRespSelId) && tipoResponsavel == 2))
				return true;
			return false;
		}
		
		function isNullOrVazio(obj){
			if (obj == null || obj == "")
				return true;
			return false;
		}
		
		function gerarTable(){
			var data = JSON.parse(localStorage.getItem('dataRespJson'));
			var idx = 0;
			const warehouseQuant = data =>
			  document.getElementById("idTbodyResponsavel").innerHTML = data.map(
			    item => ([
			      '<tr>',
			      ['responsavelSelSigla','responsavelSelDescr','lotaResponsavelSelDescr','vazio'].map(
			        key => '<td>'+item[key]+'</td>'
			      ),
			      "<td><button type='button' class='btn btn-danger' onclick='javascript:removerResponsavel(".concat(idx++,");'>Excluir</button></td>"),
			      '</tr>',
			    ])
			  ).flat(Infinity).join('');
			  
			warehouseQuant(data);
		}
		
		function mapToObj(map){
			  const obj = {}
			  for (let [k,v] of map)
			    obj[k] = v
			  return obj
		}
		
		function removerResponsavel(index){
			array.splice(index, 1);
			localStorage.setItem('dataRespJson', JSON.stringify(array));
			gerarTable();
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
			if (!Array.isArray(array) || !array.length) {
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
			process.reset();

            process.push(function () {
                Log("Executando a viculação em lote dos documentos selecionados");
            });
            
            var cont = 1;
            array.forEach(
            		res => {
			            Array.from($(".chkDocumento:checkbox").filter(":checked")).forEach(
			                    chk => {
			                        process.push(function () {
			                            return ExecutarPost(chk.value, res.tipoResponsavel, res.responsavelSelId, 
			                            		res.responsavelSelDescr, res.responsavelSelSigla, res.lotaResponsavelSelId, 
			                            		res.lotaResponsavelSelDescr, res.lotaResponsavelSelSigla, res.idPapel);			
			                        });
			                        process.push(function () {
			                            chk.checked = false;
			                            cont++;
			                        });
			                    }
			             );
            		}
			);
            
            
            
            process.push(function () {
                
                console.log("process.push");
                console.log("Contador: " + cont);
                //limparCampos();

//                 let url = '${pageContext.request.contextPath}/app/expediente/mov/listar_docs_transferidos';
//                 location.href = url + '?siglasDocumentosTransferidos=' + siglasDocumentosTransferidos
//                     + '&siglasDocumentosNaoTransferidos=' + siglasDocumentosNaoTransferidos;
            });

            process.run();
			
		}
		
		function ExecutarPost(documentoSelSigla, tipoResponsavel, responsavelSelId, responsavelSelDescr, responsavelSelSigla,
				lotaResponsavelSelId, lotaResponsavelSelDescr, lotaResponsavelSelSigla, idPapel) {
			//console.log("ExecutarPost" + documentoSelSigla);
			$.ajax({
				  url: '${pageContext.request.contextPath}/app/expediente/mov/vincularPapel_gravar',
				  type: 'POST',
				  data: {
					  postback: 1,
				      sigla: documentoSelSigla,
				      tipoResponsavel: tipoResponsavel,
				      'responsavelSel.id': responsavelSelId,
				      'responsavelSel.descricao': responsavelSelDescr,
				      'responsavelSel.sigla': responsavelSelSigla,
				      'lotaResponsavelSel.id': lotaResponsavelSelId,
				      'lotaResponsavelSel.descricao': lotaResponsavelSelDescr,
				      'lotaResponsavelSel.sigla': lotaResponsavelSelSigla,
				      idPapel: idPapel
				    	  
				  },
				  success: function () {
				      siglasDocumentosTransferidos.push(documentoSelSigla);
				  },
				  error: function (textStatus, errorThrown) {
				      console.log(textStatus + errorThrown)
				      siglasDocumentosNaoTransferidos.push(documentoSelSigla);
				  }
			});
		}

		
		function tamanho2() {
			nota= new String();
			nota = this.frm.descrMov.value;
			var i = 255 - nota.length;
			return i;
		}
		function corrige() {
			if (tamanho2()<0) {
				alert('Descrição com mais de 255 caracteres');
				nota = new String();
				nota = document.getElementById("descrMov").value;
				document.getElementById("descrMov").value = nota.substring(0,255);
			}
		}
		
		var newwindow = '';
		function popitup_movimentacao() {
			if (!newwindow.closed && newwindow.location) {
			} else {
				var popW = 600;
				var popH = 400;
				var winleft = (screen.width - popW) / 2;
				var winUp = (screen.height - popH) / 2;
				winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
				newwindow=window.open('','${propriedade}',winProp);
				newwindow.name='mov';
			}
			
			newwindow.opener = self;
			t = frm.target; 
			a = frm.action;
			frm.target = newwindow.name;
			frm.action='${pageContext.request.contextPath}/app/expediente/mov/prever?id=${mov.idMov}';
			frm.submit();
			frm.target = t; 
			frm.action = a;
			
			if (window.focus) {
				newwindow.focus()
			}
			return false;
		}	
		
		function alteraResponsavel() {
			var objSelecionado = document.getElementById('tipoResponsavel');
			
			switch (parseInt(objSelecionado.value))
			{
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
						<div class="col-sm align-self-center">
							<input type="button" value="Incluir" id="btnIncluir" onclick="javascript:inserirResponsavelTable();" class="btn btn-primary" />
						</div>
					</div>
<!-- 					<div class="row"> -->
<!-- 						<div class="col-sm"> -->
<!-- 							<input type="submit" value="Ok" class="btn btn-primary"/> -->
<!-- 							<input type="button" value=<fmt:message key="botao.cancela"/> onclick="javascript:history.back();" class="btn btn-cancel ml-2"/> -->
<!-- 						</div> -->
<!-- 					</div> -->

					<div class="gt-content-box gt-for-table">
						<br />
						<h5>Usuário(s)/Unidade(s) adicionado(s)</h5>
						<div>
							<table class="table table-hover table-striped" id="idTableResponsavel">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th class="text-center" style="width: 10%;">Matrícula</th>
										<th class="text-center">Nome</th>
										<th class="text-center">Unidade</th>
										<th class="text-center" style="width: 15%;">Função</th>
										<th class="text-center" style="width: 8%;">Excluir</th>
									</tr>
								</thead>
								<tbody class="table-bordered" id="idTbodyResponsavel">
									
								</tbody>
							</table>
						</div>
						<br/>
						<div class="col-sm align-self-center">
							<input type="button" value="Ok" id="btnOk" onclick="javascript:validar();" class="btn btn-primary" />
						</div>
					</div>
					
					<div class="gt-content-box gt-for-table">
						<br />
						<br />
						<div>
							<table class="table table-hover table-striped">
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
	</div>
    
</siga:pagina>
 <script type="text/javascript">
	    window.onload = function () { 
				localStorage.removeItem('dataRespJson');
		} 
</script>


