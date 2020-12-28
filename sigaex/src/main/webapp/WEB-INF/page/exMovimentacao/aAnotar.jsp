<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<div class="container-fluid content" id="page">

		<c:if test="${not mob.doc.eletronico}">
			<script type="text/javascript">
				$("html").addClass("fisico");
				$("body").addClass("fisico");
			</script>
		</c:if>

		<script type="text/javascript" language="Javascript1.1">
			function sbmt() {
				frm.action = '${pageContext.request.contextPath}/app/expediente/mov/anotar?id=${doc.idDoc}';
				frm.submit();
			}
			function tamanho() {
				var i = tamanho2();
				if (i < 0) {
					i = 0
				}
				;
				document.getElementById("Qtd").innerText = 'Restam ' + i
						+ ' caracteres';
			}

			function tamanho2() {
				nota = new String();
				nota = this.frm.descrMov.value;
				var i = 500 - nota.length;
				return i;
			}
			function corrige() {
				if (tamanho2() < 0) {
					sigaModal.alerta('Descrição com mais de 500 caracteres');
					nota = new String();
					nota = document.getElementById("descrMov").value;
					document.getElementById("descrMov").value = nota.substring(
							0, 500);
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
					winProp = 'width=' + popW + ',height=' + popH + ',left='
							+ winleft + ',top=' + winUp
							+ ',scrollbars=yes,resizable'
					newwindow = window.open('', '${propriedade}', winProp);
					newwindow.name = 'mov';
				}

				newwindow.opener = self;
				t = frm.target;
				a = frm.action;
				frm.target = newwindow.name;
				frm.action = '${pageContext.request.contextPath}/app/expediente/mov/prever?id=${mov.idMov}';
				frm.submit();
				frm.target = t;
				frm.action = a;

				if (window.focus) {
					newwindow.focus()
				}
				return false;
			}
		</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Anotação - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="anotar_gravar" method="post">
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="sigla" value="${sigla}" />
					<div class="row ${hide_only_GOVSP}">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label for="dtMovString">Data</label>
								<input class="form-control" type="text" name="dtMovString"
								value="${dtMovString}" onblur="javascript:verifica_data(this,0);" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
							</div>
						</div>
						<div class="col-sm-2 mt-4">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" 
									theme="simple" name="substituicao"  value="${substituicao}" 
									onclick="javascript:displayTitular(this);" />
								<label class="form-check-label">Substituto</label>
							</div>
						</div>
					</div>
					<div class="row ${hide_only_GOVSP}">
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
					<div class="row ${hide_only_GOVSP}">
						<div class="col-sm">
							<div class="form-group">
								<label>Função do Responsável</label>
								<input class="form-control" type="hidden" name="campos" value="nmFuncaoSubscritor" />
								<input class="form-control" type="text"
									name="nmFuncaoSubscritor" value="${nmFuncaoSubscritor}" size="50"
									maxLength="128" theme="simple" />
								<small class="form-text text-muted">(opcional)</small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="descrMov">Nota</label>
								<textarea class="form-control" name="descrMov" cols="60" rows="5" onkeydown="corrige();tamanho();" maxlength="500"
									onblur="tamanho();" onclick="tamanho();">${descrMov}</textarea>
								<small class="form-text text-muted" id="Qtd">Restam&nbsp;500&nbsp;caracteres</small>
							</div>
						</div>
					</div>
					<c:if test="${tipoResponsavel == 3}">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>Observação</label>
									<input class="form-control" type="text" value="${obsOrgao}" size="30" name="obsOrgao" />
								</div>
							</div>
						</div>
					</c:if>

					<div class="row">
						<div class="col-sm">
							<input type="submit" value="Ok" class="btn btn-primary" />
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
<script>
	$(function() {
		$('[name=descrMov]').focus();
	});
</script>