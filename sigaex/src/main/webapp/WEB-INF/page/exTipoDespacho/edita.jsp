<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Tipo de Despacho">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Cadastro de Tipos de despacho</h2>

			<div class="gt-content-box gt-for-table">
				<form name="frm" action="gravar" theme="simple" method="POST">

					<table class="gt-form-table">
						<c:if test="${!empty exTipoDespacho.idTpDespacho}">
							<input type="hidden" name="exTipoDespacho.idTpDespacho" value="${exTipoDespacho.idTpDespacho}"/>
							<tr>
								<td width="10%">Código:</td>
								<td>
									<fmt:formatNumber pattern="0000000" value="${exTipoDespacho.idTpDespacho}" />
								</td>
							</tr>
						</c:if>
						<tr>
							<td width="10%">Descrição:</td>
							<td>
								<textarea name="exTipoDespacho.descTpDespacho" cols="60" rows="5" class="gt-text-area">${exTipoDespacho.descTpDespacho}</textarea>
							</td>
						</tr>
						<tr>
							<td width="10%">Ativo:</td>
							<td>
								<input type="checkbox" name="exTipoDespacho.fgAtivo" <c:if test="${exTipoDespacho.fgAtivo == 'S'}">checked</c:if> />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="submit" value="OK" class="gt-btn-small" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>

