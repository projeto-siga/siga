<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");</script>
	</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Cancelamento de Movimentação - ${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">
				<form action="${request.contextPath}/app/expediente/mov/cancelar_movimentacao_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="id" value="${id}" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados do cancelamento de movimentacao</td>
						</tr>
						<tr>
							<td>Data (Opcional):</td>
							<td>
								<input type="text" name="dtMovString" onblur="javascript:verifica_data(this, true);"/>
							</td>
						</tr>
						<tr>
							<td>Responsável (Opcional):</td>
							<td>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>&nbsp;
								<input type="checkbox" name="substituicao" onclick="javascript:displayTitular(this);" />&nbsp;Substituto
							</td>
						</tr>
						<tr id="tr_titular" style="display:none">
							<td>Titular:</td>
							<td>
								<input type="hidden" name="campos" value="titularSel.id" />
								<siga:selecao tema="simple" propriedade="titular" modulo="siga"/>
							</td>
						</tr>
						<tr>
							<td>
								Motivo (Opcional):
							</td>
							<td>
								<input type="text" name="descrMov" maxlength="80" size="80" />
							</td>
						</tr>
						<tr class="button">
							<td colspan="2">
								<input type="submit" value="Ok" class="gt-btn-small gt-btn-left" />
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
