<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Notificar">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
			$("body").addClass("fisico");
		</script>
	</c:if>


	<script type="text/javascript" language="Javascript1.1">
		function sbmt() {
			document.getElementById('notificar_gravar_sigla').value = '${mob.sigla}';
			document.getElementById('notificar_gravar_pai').value = '';
			frm.action = 'notificar?sigla=VALOR_SIGLA&popup=true'.replace(
					'VALOR_SIGLA', document
							.getElementById('notificar_gravar_sigla').value);
			frm.submit();
		}

		function submeter() {

			document.getElementById("button_ok").onclick = function() {
				console.log("Aguarde requisição")
			};
			document.getElementById('frm').submit();
		}

		$(function() {
			$("#formulario_lotaResponsavelSel_sigla").focus();
		});
	</script>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Notificar - ${mob.siglaEDescricaoCompleta}</h5>
			</div>
			<div class="card-body">
				<form name="frm" id="frm" action="transferir_gravar" method="post">
					<input type="hidden" name="id" value="${id}" /> 
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="docFilho" value="true" /> 
					<input type="hidden" name="sigla" value="${sigla}" id="notificar_gravar_sigla" /> 
					<input type="hidden" name="mobilPaiSel.sigla" value="" id="notificar_gravar_pai" /> 
					<input type="hidden" name="despachando" value="" id="notificar_gravar_despachando" />
					<input type="hidden" name="tipoTramite" value="83"/>

					<div class="row">
						<div class="col col-3">
							<div class="form-group">
								<label>Destinatário</label> <select name="tipoResponsavel"
									onchange="javascript:sbmt();" class="form-control">
									<c:forEach items="${listaTipoResp}" var="item">
										<option value="${item.key}"
											${item.key == tipoResponsavel ? 'selected' : ''}>
											${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col col-9">
							<div class="form-group">
								<label>&nbsp;&nbsp;&nbsp;</label>
								<c:choose>
									<c:when test="${tipoResponsavel == 1}">
										<siga:selecao propriedade="lotaResponsavel" tema="simple"
											modulo="siga" />
									</c:when>
									<c:when test="${tipoResponsavel == 2}">
										<siga:selecao propriedade="responsavel" tema="simple"
											modulo="siga" />
									</c:when>
								</c:choose>
								</td>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col col-12">
							<div class="form-group">
								<a accesskey="o" id="button_ok" onclick="javascript:submeter();"
									class="btn btn-primary"><u>O</u>k</a> <a
									href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}"
									class="btn btn-light ml-2">Cancela</a>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
