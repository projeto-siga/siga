<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
	function sbmt() {
		ExMovimentacaoForm.page.value = '';
		ExMovimentacaoForm.acao.value = 'aJuntar';
		ExMovimentacaoForm.submit();
	}
</script>

<siga:pagina titulo="Movimentação">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
			$("body").addClass("fisico");
		</script>
	</c:if>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Inclusão de Cossignatário- ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form action="incluir_cosignatario_gravar"
					namespace="/expediente/mov" cssClass="form" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}" />
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<siga:selecao titulo="Cossignatário" propriedade="cosignatario" modulo="siga" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Função; Lotação; Localidade</label>
								<input class="form-control" type="text" name="funcaoCosignatario" size="50" value="${funcaoCosignatario}" maxlength="128" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<input type="submit" value="Ok" class="btn btn-primary" />
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
