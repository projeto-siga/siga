<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Anexação de Arquivo Auxiliar">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
			$("body").addClass("fisico");
		</script>
	</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Anexação de Arquivo Auxiliar -
				${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">
				<form action="anexar_arquivo_auxiliar_gravar" method="POST" onsubmit="sbmt.disabled=true;"
					enctype="multipart/form-data" class="form">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="sigla" value="${sigla}" />

					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados do Arquivo</td>
						</tr>
						<tr>
							<td><label>Arquivo:</label></td>
							<td><input type="file" name="arquivo" accept="*.*" /></td>
						</tr>
						<tr>
							<td colspan="2"><input type="submit" value="Ok"
								class="gt-btn-medium gt-btn-left"
								onclick="javascript: return validaSelecaoAnexo( this.form );" name="sbmt"/>
								<input type="button" value="Voltar"
								onclick="javascript:window.location.href='/sigaex/app/expediente/doc/exibir?sigla=${sigla}'"
								class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<script>
		/**
		 * Valida se o anexo foi selecionado ao clicar em OK
		 */
		function validaSelecaoAnexo(form) {
			var result = true;
			var arquivo = form.arquivo;
			if (arquivo == null || arquivo.value == '') {
				alert("O arquivo a ser anexado não foi selecionado!");
				result = false;
			}
			return result;
		}
	</script>
</siga:pagina>
