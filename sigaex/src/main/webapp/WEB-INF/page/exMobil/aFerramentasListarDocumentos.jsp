<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Listagem de Documentos por Classificação">
<script type="text/javascript" src="/sigaex/javascript/sequential-ajax-calls.js"></script>

<script type="text/javascript" language="Javascript1.1">
	$(document).ready(function() {
		$("#process_checkall").change(function() {
			var checked = $(this).prop("checked");
			$("input:checkbox.chk-assinar").each(function() {
				$(this).prop('checked', checked);
				$(this).trigger("change");
			});
		});
	});

	function executaOperacoesEmLote() {

		(function($) {
			$.fn.goTo = function() {
				$('html, body').animate({
					scrollTop : $(this).offset().top + 'px'
				}, 'fast');
				return this; // for chaining...
			}
		})(jQuery);

		process.identifyOperations();

		process.reset();

		process.push(function() {
			Log("Iniciando operação em lote")
		});

		for (i = 0; i < process.operations.length; i++) {
			var opr = process.operations[i];
			if (opr.enabled) {
				process.push("gOpr = process.operations[" + i + "];");
				process.push(function() {
					Log("Executanto operação para " + gOpr.descr)
				});
				process.push(function() {
					return ExecutarPost(gOpr.url, {
						sigla : gOpr.descr,
						nivelAcesso : $('#nivelAcesso').val()
					});
				});
				process.push(function() {
					var oChk = document.getElementsByName("process_chk_"
							+ gOpr.code)[0];
					$(oChk).goTo();
					oChk.checked = false;
				});
			}
		}

		process.push(function() {
			return "OK";
		});

		if (process.urlRedirect != null) {
			process.push(function() {
				Log("Concluído, redirecionando...");
			});

			process.push(function() {
				if (this.urlRedirect != null)
					location.href = this.urlRedirect.value;
			});
		} else {
			process.push(function() {
				Log("Concluído...");
			});
		}

		process.run();
	}

	function ExecutarPost(url, datatosend) {
		var result = "OK";
		$.ajax({
			url : url,
			type : "POST",
			data : datatosend,
			async : false,
			error : function(xhr) {
				result = TrataErro(xhr.responseText);
				if (result == "")
					result = "Erro: " + xhr.status;
			}
		});
		return result;
	}

	function TrataErro(Conteudo, retorno) {
		if (Conteudo.indexOf("gt-error-page-hd") != -1) {
			Inicio = Conteudo.indexOf("<h3>") + 4;
			Fim = Conteudo.indexOf("</h3>", Inicio);
			retorno = Conteudo.substr(Inicio, Fim - Inicio);
		}
		return retorno;
	}
</script>
	<!-- main content -->
	<div class="container-fluid">
		<form name="frm" id="frm" cssClass="form" theme="simple">
		<input type="hidden" name="postback" value="1" />
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Listagem de Documentos por Classificação</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label for="nivelAcesso">Alterar nivel de acesso</label>
							<select name="nivelAcesso" id="nivelAcesso" class="form-control">
								<c:forEach items="${listaNivelAcesso}" var="item">
									<option value="${item.idNivelAcesso}">${item.nmNivelAcesso}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>			
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<a class="btn btn-primary" href="javascript: executaOperacoesEmLote();">Gravar</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Documentos -->
		<c:if test="${(not empty lista)}">
			<h2>Lista de documentos</h2>
			<div>
				<table class="table table-sm table-striped">
					<thead class="${thead_color}">
						<tr>
							<th width="3%" style="text-align: center"></th>
							<th width="13%" rowspan="2">Número</th>
							<th rowspan="2" style="text-align: center">Data</th>
							<th width="15%" colspan="2" style="text-align: center">Cadastrante</th>
							<th width="15%" rowspan="2" align="center">Tipo</th>
							<th width="49%" rowspan="2" align="left">Descrição</th>
						</tr>
						<tr>
	
							<th style="text-align: center"><input type="checkbox"
								id="process_checkall" /></th>
							<th style="text-align: center">Pessoa</th>
							<th style="text-align: center">Lotação</th>
						</tr>
					</thead>
					<tbody class="table-bordered">
						<c:forEach var="doc" items="${lista}">
							<c:set var="x" scope="request">process_chk_${doc.idDoc}</c:set>
							<c:remove var="x_checked" scope="request" />
							<c:if test="${param[x] == 'true'}">
								<c:set var="x_checked" scope="request">checked</c:set>
							</c:if>
							<tr class="even">
								<td style="text-align: center">
									<c:if test="${true}">
										<input type="checkbox" name="${x}" value="true" ${x_checked} class="chk-assinar" />
									</c:if>
								</td>
								<td>
									<a target="_blank" href="/sigaex/app/expediente/doc/exibir?sigla=${doc.sigla}">${doc.codigo}</a>
								</td>
								<td style="text-align: center">${doc.dtDocDDMMYY}</td>
								<td style="text-align: center">${doc.lotaCadastrante.siglaLotacao}</td>
								<td style="text-align: center">${doc.cadastrante.sigla}</td>
								<td>${doc.descrFormaDoc}</td>
								<td>${doc.descrDocumento}</td>
							</tr>
							<input type="hidden" name="process_descr_${doc.idDoc}" value="${doc.sigla}" />
							<input type="hidden" name="process_url_post_${doc.idDoc}" value="/sigaex/app/expediente/mov/redefinir_nivel_acesso_gravar" />
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
		</form>
	</div>
</siga:pagina>