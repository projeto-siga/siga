<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Referência">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">
		$("html").addClass("fisico");
		$("body").addClass("fisico");
	</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='${request.contextPath}/app/expediente/mov/referenciar_gravar';
	ExMovimentacaoForm.submit();
}

</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<form action="${request.contextPath}/app/expediente/mov/referenciar_gravar" enctype="multipart/form-data" method="post">
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="sigla" value="${sigla}"/>
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>
						Vinculação de Documento - ${mob.siglaEDescricaoCompleta}
					</h5>
				</div>
				<div class="card-body">
				<c:choose>
					<c:when test="${!doc.eletronico}">
						<div class="row">
							<div class="col-sm-3">
								<div class="form-group">
									<label for="dtMovString">Data:</label>
									<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this, true);" class="form-control" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>Responsável:</label>
									<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
									&nbsp;&nbsp;
									<div class="form-check form-check-inline mt-4">
										<input class="form-check-input" type="checkbox" theme="simple" name="substituicao" onclick="javascript:displayTitular(this);" />
										<label class="form-check-label" for="exDocumentoDTO.substituicao">Substituto</label>
									</div>
								</div>
							</div>
						</div>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${!substituicao}">
						<div id="tr_titular" style="display: none">
					</c:when>
					<c:otherwise>
						<div id="tr_titular" style="">
					</c:otherwise>
				</c:choose>
							<input type="hidden" name="campos" value="${titularSel.id}" />
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<label>Titular</label>
										<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<siga:selecao titulo="Documento:" propriedade="documentoRef" urlAcao="expediente/buscar" urlSelecionar="expediente/selecionar" modulo="sigaex"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm">
								<input type="submit" value="Ok" class="btn btn-primary"/>
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
							</div>
						</div>
				</div>
			</div>
		</form>
	</div>

</siga:pagina>
