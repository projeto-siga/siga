<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>

<script>
<ww:url id="url" action="editar" namespace="/expediente/doc">
</ww:url>
function sbmt(id, action) {
	var frm = document.getElementById(id);
	frm.action=action;
	frm.submit();
	return;
}
</script>

<siga:pagina titulo="Lista Pessoas e Identidades">
	<h1>Identidades cadastradas:</h1>

	<form id="listar" name="listar" action="/siga/gi/identidade/listar.action"
		method="GET" class="form100">
	<table class="form100">
		<input type="hidden" name="propriedade" value="${param.propriedade}" />
		<ww:hidden name="postback" value="1" />
		<ww:hidden name="apenasRefresh" value="0" />
		<ww:hidden name="p.offset" value="0" />

		<tr class="header">
			<td align="center" valign="top" colspan="4">Selecione a Pessoa</td>
		</tr>

		<siga:selecao titulo="Matrícula" propriedade="pessoa" />

		<tr>
			<td></td>
			<td><siga:monobotao inputType="submit" value="Buscar" /></td>
		</tr>
	</table>
	</form>
	<br />


	<ww:url id="url" action="editar_gravar" namespace="/gi/identidade">
	</ww:url>

	<c:if test="${not empty pessoaSel.id}">
		<form id="editar" name="editar" action="/siga/gi/identidade/editar_gravar.action"
			method="GET" class="form100">
		<table class="form100">
			<input type="hidden" name="pessoaSel.id" value="${pessoaSel.id}" />
			<input type="hidden" name="pessoaSel.descricao"
				value="${pessoaSel.descricao}" />
			<input type="hidden" name="pessoaSel.sigla"
				value="${pessoaSel.sigla}" />

			<tr class="header">
				<td align="left" valign="top" colspan="4">Pessoa</td>
			</tr>


			<tr>
				<td>Pessoa:</td>
				<td>${pessoaSel.descricao}</td>
			</tr>

			<tr>
				<td>Matrícula:</td>
				<td>${pessoaSel.sigla}</td>
			</tr>

			<ww:if test="${pessoaSel.objeto.bloqueada}">
				<tr>
					<td colspan=4><span style="color: red"><b>Esta
					pessoa está bloqueada. Para remover o bloqueio, clique no botão
					&quot;Desbloquear&quot;, abaixo.</b></span></td>
				</tr>
			</ww:if>

			<tr>
				<td></td>
				<td><ww:if test="${pessoaSel.objeto.bloqueada}">
					<input type="button"
						onclick="javascript: sbmt('editar','/siga/gi/identidade/desbloquear_pessoa.action');"
						value="Desbloquear" />
				</ww:if><ww:else>
					<input type="button"
						onclick="javascript: sbmt('editar','/siga/gi/identidade/bloquear_pessoa.action');"
						value="Bloquear" />
				</ww:else></td>
			</tr>
		</table>
		</form>

		<br />
	</c:if>

	<c:forEach var="ident" items="${itens}">
		<form id="editar_${ident.id}"
			name="editar_${ident.id}"
			action="/siga/gi/identidade/editar_gravar.action" method="GET" class="form100">
		<table class="form100">
			<ww:hidden name="id" value="${ident.id}" />
			<input type="hidden" name="pessoaSel.id" value="${pessoaSel.id}" />
			<input type="hidden" name="pessoaSel.descricao"
				value="${pessoaSel.descricao}" />
			<input type="hidden" name="pessoaSel.sigla"
				value="${pessoaSel.sigla}" />

			<tr class="header">
				<td align="left" valign="top" colspan="4">Tipo da Identidade:
				${ident.cpTipoIdentidade.dscCpTpIdentidade}</td>
			</tr>


			<tr>
				<td>Login:</td>
				<td>${ident.nmLoginIdentidade}</td>
			</tr>

			<tr>
				<td>Data de criação:</td>
				<td>${ident.dtCriacaoDDMMYYYY}</td>
			</tr>

			<tr>
				<td>Data de expiração:</td>
				<td><input name="dtExpiracao"
					value="${ident.dtExpiracaoDDMMYYYY}" type="text" size="10"
					maxlength="10" onblur="javascript:verifica_data(this);" /></td>
			</tr>

			<ww:if test="${ident.bloqueada}">
				<tr>
					<td colspan=4><span style="color: red"><b>Esta
					identidade está bloqueada. <ww:if
						test="${not pessoaSel.objeto.bloqueada}">Para remover o bloqueio, clique no botão
					&quot;Desbloquear&quot;, abaixo.</ww:if> </b></span></td>
				</tr>
			</ww:if>

			<tr>
				<td></td>
				<td><siga:monobotao inputType="submit" value="Gravar" /> <input
					type="button"
					onclick="javascript: sbmt('editar_${ident.id}','/siga/gi/identidade/cancelar.action');"
					value="Cancelar" /> <ww:if
					test="${not pessoaSel.objeto.bloqueada}">
					<ww:if test="${ident.bloqueada}">
						<input type="button"
							onclick="javascript: sbmt('editar_${ident.id}','/siga/gi/identidade/desbloquear.action');"
							value="Desbloquear" />
					</ww:if>
					<ww:else>
						<input type="button"
							onclick="javascript: sbmt('editar_${ident.id}','/siga/gi/identidade/bloquear.action');"
							value="Bloquear" />
					</ww:else>
				</ww:if></td>
			</tr>
		</table>
		</form>

		<br />
	</c:forEach>

</siga:pagina>
