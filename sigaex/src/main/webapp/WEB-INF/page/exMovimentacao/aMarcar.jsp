<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
	<div class="container-fluid content pt-5">
		<div class="row">
			<div class="col offset-sm-3 col-sm-6">
				<div class="jumbotron">
					<h2><fmt:message key="documento.marcacao"/> ${mob.sigla}</h2>
					<form name="frm" action="vincularPapel_gravar" method="post">
						<input type="hidden" name="postback" value="1" /> <input
							type="hidden" name="sigla" value="${sigla}" />
						<table class="">
							<tr>
								<td><c:forEach items="${listaMarcadores}" var="item">
										<input type="checkbox" value="${item.idMarcador}"
											onclick="marcar(this.value,this.checked)"
											${listaMarcadoresAtivos.contains(item) ? 'checked' : ''} />
										${item.descrMarcador}
										<br />
									</c:forEach></td>
							</tr>
							<tr class="button">
								<td colspan="2"><input type="button" value="Voltar"
									onclick="javascript:history.back();"
									class="btn btn-primary mt-3" /></td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>
