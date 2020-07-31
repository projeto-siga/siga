<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aTornarSemEfeitoGravar';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Movimentação">

<c:if test="${not doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5><fmt:message key="documento.tornar.sem.efeito"/> - ${mob.siglaEDescricaoCompleta}</h5>
			</div>
			<div class="card-body">
				<form action="tornarDocumentoSemEfeitoGravar" enctype="multipart/form-data" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="id" value="${id}" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<h6><fmt:message key="documento.dados.cancelamento"/></h6>
							</div>
						</div>
					</div>
					<div class="row d-none">
						<div class="col-sm-6">
							<div class="form-group">
								<label id="tr_titular" style="display:none">Titular</label>
								<input type="hidden" name="campos" value="titularSel.id" />
								<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Motivo</label>
								<input class="form-control" type="text" name="descrMov" value="${descrMov}" maxlength="80"size="80" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="submit" value="Ok" class="btn btn-primary" />							
							<a href="${linkTo[ExDocumentoController].exibe()}?sigla=${sigla}" class="btn btn-cancel ml-2">Cancela</a>														 												
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
