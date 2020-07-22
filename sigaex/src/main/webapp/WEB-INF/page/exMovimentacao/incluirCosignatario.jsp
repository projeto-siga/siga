<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript" language="Javascript1.1">
	function sbmt() {
		ExMovimentacaoForm.page.value = '';
		ExMovimentacaoForm.acao.value = 'aJuntar';
		ExMovimentacaoForm.submit();
	}

	function displayPersonalizar(thisElement) {
		var thatElement = document.getElementById('div_personalizacao');
		if (thisElement.checked) {
			thatElement.style.display = '';
		} else {
			document.getElementById('funcaoCosignatario').value = "";
			document.getElementById('unidadeCosignatario').value = "";
			thatElement.style.display = 'none';
		}
	}
	function validate() {
		var sigaCliente = document.getElementById('sigaCliente');
		if (sigaCliente.value == 'GOVSP') {
			var personalizacao = document.getElementById('exDocumentoDTO.personalizacao');
			if (personalizacao.checked) {
				if(document.getElementById('funcaoCosignatario').value.trim() == '') {
					sigaModal.alerta("É necessário informar a Função quando selecionado a opção Personalizar.");
					return false;
				}
			}
		}
		return true;
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
		<input type="hidden" id="sigaCliente" value="${siga_cliente}" />
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Inclusão de Cossignatário- ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form action="incluir_cosignatario_gravar" namespace="/expediente/mov" cssClass="form" method="post" onsubmit="return validate();" >
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}" />
					<c:if test="${siga_cliente == 'GOVSP'}">
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<siga:selecao titulo="Cossignatário" propriedade="cosignatario" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-2">
							<div class="form-group">
								<div class="form-check form-check-inline mt-4">
									<input type="checkbox" id="exDocumentoDTO.personalizacao" name="exDocumentoDTO.personalizacao" class="form-check-input ml-3"  onclick="javascript:displayPersonalizar(this);"/>
									<label class="form-check-label" for="exDocumentoDTO.personalizacao">Personalizar</label>
									<a class="fas fa-info-circle text-secondary ml-1" data-toggle="tooltip" data-trigger="click" data-placement="bottom" title="Preencher esse campo se houver a necessidade de trocar as informações cadastrais do cossignatário"></a>
								</div>
							</div>
						</div>
					</div>
					<div id="div_personalizacao" style="display: none" class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Função</label>
								<input type="text" id="funcaoCosignatario" name="funcaoCosignatario" maxlength="125" class="form-control">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label><fmt:message key="usuario.lotacao"/></label>
								<input type="text" id="unidadeCosignatario" name="unidadeCosignatario" maxlength="125" class="form-control">
							</div>
						</div>
					</div>
					</c:if>					
					<c:if test="${siga_cliente != 'GOVSP'}">
					<div class="row">
						<div class="col-12 col-lg-6">
						<siga:selecao titulo="Cossignatário" propriedade="cosignatario" modulo="siga" />
						</div>
						<div class="col-12 col-lg-6">
							<div class="form-group">
								<label>Função; Lotação; Localidade</label>
								<input class="form-control" type="text" name="funcaoCosignatario" size="50" value="${funcaoCosignatario}" maxlength="128" />
							</div>
						</div>
					</div>
					</c:if>
					<div class="row">
						<div class="col-12 col-lg-6">
							<input type="submit" value="Ok" class="btn btn-primary" />
							<input type="button" value="<fmt:message key="botao.cancela"/>" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
						</div>
					</div>
				</form>							
			</div>
		</div>
	</div>
</siga:pagina>
<script type="text/javascript">
	$('a[data-toggle="tooltip"]').tooltip({
	    placement: 'bottom',
	    trigger: 'click'
	});
</script>