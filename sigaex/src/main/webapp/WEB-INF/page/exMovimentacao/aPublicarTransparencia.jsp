	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<%@ page language="java" contentType="text/html; charset=UTF-8"
		buffer="64kb"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
	<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
	
	<siga:pagina titulo="Publicar em Portal da Transparência">
		<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection" />
	
		<script type="text/javascript" language="Javascript1.1">
			function sbmt() {
				frm.submit();
			}
		</script>
	
	<!-- main content bootstrap -->
		<div class="container-fluid">
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>
						Documento - ${doc.sigla} 
					</h5>
				</div>
				<div class="card-body">
					<form name="frm" action="publicacao_transparencia_gravar" namespace="/expediente/mov" theme="simple" method="post">
						  <input type="hidden" name="postback" value="1" /> 
						  <input type="hidden" name="sigla" value="${sigla}" />
	
					    <h5 class="card-title"><fmt:message key="documento.publicar.portaltransparencia"/></h5>
					    <p class="card-text"><fmt:message key="documento.publicar.portaltransparencia.texto"/></p>

					    <div class="alert alert-warning text-center" role="alert">
						  A publicação redefinirá o <strong>Nível de Acesso</strong> ao documento para <strong>Público</strong>. Nível de Acesso atual <strong>${doc.exNivelAcessoAtual.nmNivelAcesso}</strong>.
						</div>
						
						<div class="card" >
						  <div class="card-header text-white bg-dark">
						    <i class="fas fa-sitemap"></i> Taxonomia
						  </div>
						  <div class="card-body">
						  	<div class="ml-2">
							  	<div class="form-group col-6">
									<label for="marcacoes">Marcações</label>
									<input type="hidden" name="idMarcacoes" value="${idMarcacoes}" id="inputHiddenMarcacoesSelecionadas" />
									<select id="marcacoes" name="lstMarcadores" class="form-control  siga-multiploselect  js-siga-multiploselect--marcacoes">
										<c:forEach items="${listaMarcadores}" var="item">
											<option value="${item.idMarcador}"  data-content="<span class='badge badge-primary' style='min-width: 150px;'>${item.descrMarcador}</span>">${item.descrMarcador}</option>
										</c:forEach>
									</select>
									<small class="form-text text-muted">Classifique o documento utilizando as marcações disponíveis para estruturar o conteúdo</small>
								</div>		
						    </div>
						  </div>
						</div>
	
						<br />
						<div class="row text-center">
							<div class="col-sm">
								<button id="btnSubmit" type="button" class="btn btn-primary" onclick="sbmt();"><i class="fa fa-share-alt"></i>  Enviar para Publicação</button>
								<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
	
							</div>
						</div>
		
					</form>
				</div>
				<div class="card-footer text-muted">
			    	${doc.sigla} - 
			    	<c:forEach items="${listaMarcadoresAtivos}" var="item">
			    		&nbsp;<span class="badge badge-warning" style="min-width: 100px;"><i class="fa fa-tag" aria-hidden="true"></i> ${item.descrMarcador}</span>
			    	</c:forEach>
			 	</div>
			</div>
		</div>
		

		
		
		<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
		<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
		<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
		<script type="text/javascript" src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>
		<script type="text/javascript" src="/siga/javascript/siga.multiploselect.js"></script>
		
	</siga:pagina>
