<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<fmt:formatDate var="dataLimiteDemanda" value="${dataLimite}" pattern="yyyy-MM-dd" />
	<script type="text/javascript">
		function marcacaoSelecionada(idMarcacao, demandaJudicial, checado) {
			if(demandaJudicial) {
				Array.from(document.querySelectorAll(".demanda")).forEach(divDemanda => {
					const checkDemanda = divDemanda.querySelector("input[type='checkbox']");
					const dataDemanda = divDemanda.querySelector("input[type='date']");

					if(divDemanda.dataset.id == idMarcacao) {
						dataDemanda.disabled = !checkDemanda.checked;
						if(checkDemanda.checked) {
							dataDemanda.focus();
						} else {
							dataDemanda.value = null;
						}
					} else {
						checkDemanda.checked = false;

						dataDemanda.disabled = true;
						if(!!dataDemanda.value) {
							document.getElementById("dataLimite-" + idMarcacao).value = dataDemanda.value;
						}
						dataDemanda.value = null;
					}
				});
			}
		}

		function validar() {
			let valido = true;
			const demandasMarcadas = Array.from(document.querySelectorAll(".demanda input[type='checkbox']"))
				.filter(chk => chk.checked);

			if(demandasMarcadas.length > 1) {
				sigaModal.alerta("Você pode apenas selecionar um tipo de Demanda Judicial.");
				valido = false;
			} else if(demandasMarcadas.length === 1) {
				const campoDataLimite = document.getElementById("dataLimite-" + demandasMarcadas[0].value);
				if(!campoDataLimite.value) {
					sigaModal.alerta("Favor selecionar Data Limite.");
					campoDataLimite.focus();
					valido = false;
				}
			}

			return valido;
		}

	</script>

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
		</script>
	</c:if>

	<div class="container-fluid content pt-5">
		<div class="row">
			<div class="col offset-sm-3 col-sm-6">
				<div class="jumbotron">
					<h2>
						<fmt:message key="documento.marcacao" />
						${mob.sigla}
					</h2>
					<form name="frm" action="salvar_marcas" method="post" onsubmit="return validar()">
						<input type="hidden" name="postback" value="1" /> 
						<input type="hidden" name="sigla" value="${sigla}" />
						<c:forEach items="${listaMarcadoresAtivos}" var="marcaOriginal">
							<input type="hidden" name="marcadoresOriginais"
								value="${marcaOriginal.idMarcador}" />
						</c:forEach>
						<input type="hidden" name="dataLimiteOriginal" value="${dataLimiteDemanda}"/>
						<table class="">
							<tr>
								<td>
									<c:forEach items="${listaMarcadores}" var="item">
										<c:set var="estaMarcado"
											value="${listaMarcadoresAtivos.contains(item)}" />
										<div id="marcacao-${item.idMarcador}"
											class="${item.demandaJudicial? 'demanda': ''}"
											data-id="${item.idMarcador}">
											<input id="marcador-${item.idMarcador}" type="checkbox"
												value="${item.idMarcador}" name="marcadoresSelecionados"
												${estaMarcado ? 'checked' : ''}
												onchange="marcacaoSelecionada(${item.idMarcador}, ${item.demandaJudicial}, this.checked)" />
											<label for="marcador-${item.idMarcador}">${item.descrMarcador}</label>
											<c:if test="${item.demandaJudicial}">
												<div id="data-demanda-${item.idMarcador}">
													<label for="dataLimite-${item.idMarcador}">Data Limite:</label> 
													<br /> 
													<input id="dataLimite-${item.idMarcador}"
														name="dataLimite" type="date"
														${estaMarcado? '': 'disabled'}
														value="${estaMarcado? dataLimiteDemanda: ''}" />
												</div>
											</c:if>
										</div>
									</c:forEach>
								</td>
							</tr>
							<tr class="button">
								<td colspan="2">
									<input type="submit" value="OK" class="btn btn-primary" />
									<input type="button" value=<fmt:message key="botao.cancela"/> onclick="javascript:history.back();" class="btn btn-cancel ml-2"/>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>
