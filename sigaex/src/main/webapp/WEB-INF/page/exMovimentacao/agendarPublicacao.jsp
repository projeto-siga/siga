<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>
	
	<script type="text/javascript">
		function prever_data() {
			var dtPublDiv = document.getElementById('dt_publ');
			ReplaceInnerHTMLFromAjaxResponse('${request.contextPath}/app/expediente/mov/prever_data?data=' + document.getElementById('dt_dispon').value, null, dtPublDiv);
		}
		
		function contaLetras() {
			var i = 256 - tamanho();
			$("#Qtd").html('Restam ' + i + ' caracteres');
		}
	
		function tamanho() {
			nota= new String();
			nota = this.frm.descrPublicacao.value;
			return nota.length;		
		}
	
		function validar() {
			var data = document.getElementsByName('dtDispon')[0].value;
			var i = tamanho();
			if (data==null || data=="") {			
				alert("Preencha a data para disponibilização.");
				document.getElementById('dt_dispon').focus();		
			}else {
				if (i>256) {
					alert('Descrição com mais de 256 caracteres');
					document.getElementById('descrPublicacao').focus();	
				}else {
					if (i<=0) {
						alert('Descrição deve ser preenchida');
						document.getElementById('descrPublicacao').focus();	
					}else	
						frm.submit();
				}	
			}
		}
		
		function buscaNomeLota(){
			var siglaLota = $('#idLotPublicacao :selected').text();	
				$.ajax({				     				  
					  url:'/siga/lotacao/selecionar.action?sigla=' + siglaLota ,					    					   					 
					  success: function(data) {
						 var parts = data.split(';');					   
				    	$('#nomeLota').html(parts[3]);				    
				 	 }
				});			
		}
	</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Agendamento de Publicação - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="${request.contextPath}/app/expediente/mov/agendar_publicacao_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					
					<c:choose>
						<c:when test="${not empty mensagem}">
							<c:set var="disabled" value="disabled" />				
						</c:when>
							
						<c:otherwise>
							<c:set var="disabled" value="" />
						</c:otherwise>
					</c:choose>
					
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<c:choose>
									<c:when test="${cadernoDJEObrigatorio}">
										<c:set var="disabledTpMat">true</c:set> 
										<input type="hidden" name="tipoMateria" value="${tipoMateria}" />
											<label>Tipo de Matéria: 
												<c:choose>
													<c:when test="${tipoMateria eq 'A'}">
														Administrativa 
													</c:when>
													<c:otherwise>
														Judicial
													</c:otherwise>
												</c:choose>
											</label>
										</tr>
									</c:when>
									<c:otherwise>
										<label>Tipo de Matéria:</label>
										<div>
											<div class="form-check form-check-inline">
												<input class="form-check-input" id="idTipoMateriaJ" type="radio" name="tipoMateria" value="J"/>
												<label class="form-check-label" for="idTipoMateriaJ">Judicial</label>
											</div>
											<div class="form-check form-check-inline">
												<input class="form-check-input" id="idTipoMateriaA" type="radio" name="tipoMateria" value="A"/>
												<label class="form-check-label" for="idTipoMateriaA">Administrativa</label>
											</div>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label>Próxima data para disponibilização</label>
								<input class="form-control" value readonly>${proximaDataDisponivelStr}</input>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
								<label>Data para disponibilização</label>
								<input class="form-control" type="text" name="dtDispon" id="dt_dispon" onblur="javascript:verifica_data(this,true);prever_data();"/>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
								<label>Data de publicação</label>
								<input class="form-control" id="dt_publ" value readonly/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-9">
							<div class="form-group">
								<label>Lotação de Publicação</label>
							
									<c:choose>
										<c:when test="${podeAtenderPedidoPubl}">
											<siga:selecao tema="simple" propriedade="lotaSubscritor" modulo="siga" />
										</c:when>
										
										<c:otherwise>
											<select class="form-control" id="idLotPublicacao" name="idLotPublicacao" onchange="javascript:buscaNomeLota();">
												<c:forEach items="${listaLotPubl}" var="item">
													<option value="${item.idLotacao}">${item.siglaLotacao}</option>
												</c:forEach>
											</select>
											<span id="nomeLota"></span>
										</c:otherwise>
									</c:choose>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Descrição do documento</label>
								<textarea class="form-control" name="descrPublicacao" cols="80" id="descrPublicacao" rows="5" class="gt-form-textarea" onkeyup="contaLetras();">${descrPublicacao}</textarea>	
								<small class="form-text text-muted" id="Qtd">Restam&nbsp;${tamMaxDescr}&nbsp;caracteres</small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="button" value="Ok" onclick="javascript: validar();" class="btn btn-primary" ${disabled}/>
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-primary ml-2" />
							<a href="${request.contextPath}/app/arquivo/download?arquivo=${mob.referenciaRTF}" class="btn btn-primary ml-2">Visualizar Publicação</a>
						</div>
					</div>
					
				</form>	
				<span style="font-weight:bold; color: red">${mensagem}</span>			
			</div>
			
			<br/>
			
			<div class="row mb-3 ml-2">
				<div class="col-sm">
					<span class="text-danger font-weight-bold">Atenção:</span>
					<ul>
						<li><span class="font-weight-bold">Data para
						Disponibilização</span> - data em que a matéria efetivamente aparece no
						site</li>
						<li><span class="font-weight-bold">Data de Publicação</span> -
						a Data de Disponibilização + 1, conforme prevê art. 4º, parágrafo 3º
						da Lei 11419 / 2006</li>
						<li><span class="font-weight-bold">Visualizar Publicação</span> -
						Permite visualizar o documento antes de ser enviado para o DJE.</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		buscaNomeLota();
		$(":radio[value=${tipoMateria}]").attr('checked', true);
		$("[name=idLotPublicacao]").val('${idLotDefault}');
		
		<c:if test="${disabledTpMat}">
			$(":radio").attr('disabled', true);
		</c:if>
	</script>
</siga:pagina>
