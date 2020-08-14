<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Desentranhamento">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					<fmt:message key="documento.cancelamento.juntada"/> - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form action="${request.contextPath}/app/expediente/mov/cancelar_juntada_gravar" method="post" class="js-formulario-cancelar-juntada" novalidate>
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}" />
					<div class="row">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label for="dtMovString">Data</label>
								<input class="form-control" type="text" name="dtMovString" onblur="javascript:verifica_data(this, true);"/>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>&nbsp;
							</div>
						</div>
						<div class="col-sm-2 mt-4">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" 
									theme="simple" name="substituicao" value="${substituicao}" 
									onclick="javascript:displayTitular(this);" />
								<label class="form-check-label">Substituto</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							<c:choose>
								<c:when test="${!substituicao}">
									<div id="tr_titular" style="display: none">
								</c:when>
								<c:otherwise>
									<div id="tr_titular" style="">
								</c:otherwise>
							</c:choose>
										<label>Titular</label>
										<input class="form-control" type="hidden" name="campos" value="titularSel.id" />
										<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
									</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="motivo">Motivo</label>
								<input class="form-control" type="text" id="motivo" name="descrMov" maxlength="80" size="80" required/>
								<div class="invalid-feedback">Por favor, informe um motivo.</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="submit" value="Ok" class="btn btn-primary" />							
							<a href="${linkTo[ExDocumentoController].exibe()}?sigla=${sigla}" class="btn btn-cancel ml-2"><fmt:message key="botao.cancela"/></a>														 												
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
<script>
	$('[name=dtMovString]').focus();
</script>	
<c:if test="${validarCamposObrigatoriosForm}">
	<script>		
		(function() {
			'use strict';
			window.addEventListener('load', function() {		 
			 var forms = document.getElementsByClassName('js-formulario-cancelar-juntada');		 
			 var validation = Array.prototype.filter.call(forms, function(form) {
			   form.addEventListener('submit', function(event) {
				 var motivo = $('#motivo');
			     if (motivo.val().length == 0) {
			       	motivo.addClass('is-invalid');	 		    	
			     	event.preventDefault();
			       	event.stopPropagation();		       
			     } else {
			    	motivo.removeClass('is-invalid');
			     }		     
			   }, false);
			 });
			}, false);
		})();
	</script>		
</c:if>	
</siga:pagina>
