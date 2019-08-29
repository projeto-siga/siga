<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					<fmt:message key="documento.cancelamento.movimentacao"/> ${descrMov} - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form action="${request.contextPath}/app/expediente/mov/cancelar_movimentacao_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="id" value="${id}" />
					<input type="hidden" name="sigla" value="${mob.sigla}"/>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label>Data</label>
								<input class="form-control" type="text" name="dtMovString" onblur="javascript:verifica_data(this, true);"/>
								<small class="form-text text-muted">(opcional)</small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
								<small class="form-text text-muted">(opcional)</small>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="substituicao" onclick="javascript:displayTitular(this);" />
								<label class="form-check-label">Substituto</label>
							</div>
						</div>
					</div>						
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<div id="tr_titular" style="display:none">
									<label>Titular</label>
									<input class="form-control" type="hidden" name="campos" value="titularSel.id" />
									<siga:selecao tema="simple" propriedade="titular" modulo="siga"/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Motivo</label>
								<input class="form-control" type="text" name="descrMov" maxlength="80" size="80" />
								<small class="form-text text-muted">(opcional)</small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<input type="submit" value="Ok" class="btn btn-primary"/> 
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2"/> 
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
