<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script>
	function hideShowSel(combo) {
		var sel1Span = $('#spanPess' + combo.name.substring(4));
		var sel2Span = $('#spanLota' + combo.name.substring(4));
		var sel3Span = $('#spanText' + combo.name.substring(4));

		switch (combo.selectedIndex) {
		case 0:
			if (combo.name == $('#tipoDest').attr('name')) {
				sel1Span.show();
				sel2Span.hide();
				sel3Span.hide();
			} else {
				sel1Span.hide();
				sel2Span.hide();
				sel3Span.hide();
			}
			break;
		case 1:
			if (combo.name == $('#tipoDest').attr('name')) {
				sel1Span.hide();
				sel2Span.show();
				sel3Span.hide();
			} else {
				sel1Span.show();
				sel2Span.hide();
				sel3Span.hide();
			}
			break;
		case 2:
			sel1Span.hide();
			sel2Span.show();
			sel3Span.hide();
			break;
		case 3:
			sel1Span.hide();
			sel2Span.hide();
			sel3Span.show();
			break;
		default:
			sel1Span.hide();
			sel2Span.hide();
			sel3Span.hide();
		}
	}

	function validar() {
		if ($('#pessSel_id').val() == "" && $('#lotaSel_id').val() == "")
			alert("Preencha o destinatário.");
		else
			frm.submit();
	}
</script>
<siga:pagina titulo="Cadastro de email de notificação">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Cadastrar email de notificação</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="editar_gravar">
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="tipoDest">Tipo de destinatário da
									movimentação</label>
								<div class="row">
									<div class="col-sm-2">
										<select name="tipoDest" id="tipoDest"
											onchange="javascript:hideShowSel(this);" class="form-control">
											<c:forEach items="${listaTipoDest}" var="item">
												<option value="${item.key}"
													${item.key == tipoDest ? 'selected' : ''}>
													${item.value}</option>
											</c:forEach>
										</select>
										<c:choose>
											<c:when test="${tipoDest == 1}">
												<c:set var="destMovStyle" value="" />
												<c:set var="lotaDestMovStyle" value="display:none" />
											</c:when>
											<c:when test="${tipoDest == 2}">
												<c:set var="destMovStyle" value="display:none" />
												<c:set var="lotaDestMovStyle" value="" />
											</c:when>
										</c:choose>
									</div>
									<div class="col-sm-10">
										<span id="spanPessDest" style="${destMovStyle}"> <siga:selecao
												propriedade="pess" tema="simple" modulo="siga" />
										</span> <span id="spanLotaDest" style="${lotaDestMovStyle}"> <siga:selecao
												propriedade="lota" tema="simple"
												paramList="${strBuscarFechadas}" modulo="siga" />
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="tipoDest">Tipo de interessado na
									movimentação</label>
								<div class="row">
									<div class="col-sm-2">
										<select name="tipoEmail" id="tipoEmail"
											onchange="javascript:hideShowSel(this);" class="form-control">
											<c:forEach items="${listaTipoEmail}" var="item">
												<option value="${item.key}"
													${item.key == tipoEmail ? 'selected' : ''}>
													${item.value}</option>
											</c:forEach>
										</select>
										<c:choose>
											<c:when test="${tipoEmail == 2}">
												<c:set var="emailMovStyle" value="" />
												<c:set var="lotaEmailMovStyle" value="display:none" />
												<c:set var="emailStyle" value="display:none" />
											</c:when>
											<c:when test="${tipoEmail == 3}">
												<c:set var="emailMovStyle" value="display:none" />
												<c:set var="lotaEmailMovStyle" value="" />
												<c:set var="emailStyle" value="display:none" />
											</c:when>
											<c:when test="${tipoEmail == 4}">
												<c:set var="emailMovStyle" value="display:none" />
												<c:set var="lotaEmailMovStyle" value="display:none" />
												<c:set var="emailStyle" value="" />
											</c:when>
											<c:otherwise>
												<c:set var="emailMovStyle" value="display:none" />
												<c:set var="lotaEmailMovStyle" value="display:none" />
												<c:set var="emailStyle" value="display:none" />
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col-sm-10">
										<span id="spanPessEmail" style="${emailMovStyle}"> <siga:selecao
												propriedade="pessEmail" tema="simple" modulo="siga" />
										</span> <span id="spanLotaEmail" style="${lotaEmailMovStyle}">
											<siga:selecao propriedade="lotaEmail" tema="simple"
												paramList="${strBuscarFechadas}" modulo="siga" />
										</span> <span id="spanTextEmail" style="${emailStyle}"> <input
											type="text" name="emailTela" size="40" maxLength="40"
											class="form-control" />
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<button type="button" onclick="javascript: validar();"
									class="btn btn-primary">Ok</button>
								<button type="button" onclick="javascript:history.back();"
									class="btn btn-primary">Cancela</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>