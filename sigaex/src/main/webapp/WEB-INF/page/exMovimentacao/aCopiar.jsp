<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Inclusão de Cópia">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">
		$("html").addClass("fisico");
		$("body").addClass("fisico");
	</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='${request.contextPath}/app/expediente/mov/copiar_gravar';
	ExMovimentacaoForm.submit();
}

</script>

<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>
				Inclusão de Cópia de Documento - ${mob.siglaEDescricaoCompleta}
			</h2>
			<div class="gt-content-box gt-for-table">
				<form action="${request.contextPath}/app/expediente/mov/copiar_gravar" enctype="multipart/form-data" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">
								Dados da Inclusão de Cópia
							</td>
						</tr>
						<c:choose>
							<c:when test="${!doc.eletronico}">
								<tr>
									<td>
										Data:
									</td>
									<td>
										<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this, true);" />
									<td>
								</tr>
								<tr>
								<td>
									Responsável:
								</td>
								<td>
									<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
									&nbsp;&nbsp;
									<input type="checkbox" theme="simple" name="substituicao" onclick="javascript:displayTitular(this);" />
									Substituto
								</td>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${!substituicao}">
								<tr id="tr_titular" style="display: none">
							</c:when>
							<c:otherwise>
								<tr id="tr_titular" style="">
							</c:otherwise>
						</c:choose>
						<td>
							Titular:
						</td>
						<input type="hidden" name="campos" value="${titularSel.id}" />
						<td colspan="3">
							<siga:selecao propriedade="titular" tema="simple" modulo="siga"/></td>
						</tr>
						<siga:selecao titulo="Documento:" propriedade="documentoRef" urlAcao="expediente/buscar" urlSelecionar="expediente/selecionar" modulo="sigaex"/>
						<tr class="button">
							<td colspan="2">
							<input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/>
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
						</tr>
				</table>
			</form>
		</div>
	</div>
</div>

</siga:pagina>
