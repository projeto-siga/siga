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
		</script>
	</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Inclusão de Cossignatário- ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

				<form action="incluir_cosignatario_gravar"
					namespace="/expediente/mov" cssClass="form" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}" />
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">
								Dados do Cossignatário
							</td>
						</tr>

						<siga:selecao titulo="Cossignatário:" propriedade="cosignatario" modulo="siga" />


						<tr>
							<td>
							<label>
								Função;Lotação;Localidade
							</label>
						</td>
						<td>
							<input type="text" name="funcaoCosignatario" size="50" value="${funcaoCosignatario}" maxlength="128" />
						</td>
						</tr>
						<tr class="button">
							<td colspan="2">
							<input type="submit" value="Ok" class="gt-btn-small gt-btn-left" />
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" />
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
