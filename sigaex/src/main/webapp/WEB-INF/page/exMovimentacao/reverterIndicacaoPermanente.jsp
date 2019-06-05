<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='reverterIndicacaoPermanente';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Movimentação">
<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Reversão de Indicação para Guarda Permanente - ${mob.siglaEDescricaoCompleta}</h5>
			</div>
			<div class="card-body">
			<form action="${request.contextPath}/app/expediente/mov/reverter_indicacao_permanente_gravar" enctype="multipart/form-data" method="post">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="sigla" value="${sigla}" />
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label>Data</label> 
								<input type="text" name="dtMovString" class="form-control"/>
							</div>
						</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label>Responsável</label> 
							<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<div class="form-check form-check-inline mt-4">
								<input type="checkbox" theme="simple" name="substituicao" onclick="javascript:displayTitular(this);" />
								<label class="form-check-label" for="substituicao">Substituto</label>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" id="tr_titular" style="display: ${exDocumentoDTO.substituicao ? '' : 'none'};">
					<input type="hidden" name="campos" value="titularSel.id" />
						<div class="form-group">
							<label>Titular</label>
							<input type="hidden" name="campos" value="titularSel.id" />
							<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label>Motivo</label>
							<input type="text" name="descrMov" maxlength="80" size="80" class="form-control"/>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="submit" class="btn btn-primary" >Ok</button>
							<button type="button" onclick="javascript:history.back();" class="btn btn-primary" >Cancela</button>
						</div>
					</div>
				</div>
			</form>
			</div>
		</div>
	</div>
</siga:pagina>
