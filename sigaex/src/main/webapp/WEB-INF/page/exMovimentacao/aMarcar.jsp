<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
		</script>
	</c:if>

	<script type="text/javascript" language="Javascript1.1">
		function sbmt() {
			frm.action = '${pageContext.request.contextPath}/app/expediente/mov/vincularPapel';
			frm.submit();
		}

		function marcar(id, ativo) {
			$.ajax({
				url : "marcar_gravar",
				type : "POST",
				data : {
					sigla : "${sigla}",
					idMarcador : id,
					ativo : ativo
				},
				success : function(result) {
					$("#div1").html(result);
				}
			});
		}
	</script>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Marcação - ${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">
				<form name="frm" action="vincularPapel_gravar" method="post">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="sigla" value="${sigla}" />
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Marcação de Documento</td>
						</tr>
						<tr>
							<td>Marcadores Gerais <c:forEach items="${listaMarcadores}"
									var="item">
									<br />
									<input type="checkbox" value="${item.idMarcador}"
										onclick="marcar(this.value,this.checked)"
										${listaMarcadoresAtivos.contains(item) ? 'checked' : ''} />
										${item.descrMarcador}
								</c:forEach></td>
						</tr>
						<tr class="button">
							<td colspan="2"><input type="button" value="Voltar"
								onclick="javascript:history.back();"
								class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
