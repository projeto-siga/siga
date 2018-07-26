<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<div class="container-fluid content" id="page">

		<c:if test="${not mob.doc.eletronico}">
			<script type="text/javascript">
				$("html").addClass("fisico");
				$("body").addClass("fisico");
			</script>
		</c:if>

		<script type="text/javascript" language="Javascript1.1">
			function sbmt() {
				frm.action = '${pageContext.request.contextPath}/app/expediente/mov/anotar?id=${doc.idDoc}';
				frm.submit();
			}
			function tamanho() {
				var i = tamanho2();
				if (i < 0) {
					i = 0
				}
				;
				document.getElementById("Qtd").innerText = 'Restam ' + i
						+ ' Caracteres';
			}

			function tamanho2() {
				nota = new String();
				nota = this.frm.descrMov.value;
				var i = 255 - nota.length;
				return i;
			}
			function corrige() {
				if (tamanho2() < 0) {
					alert('Descrição com mais de 255 caracteres');
					nota = new String();
					nota = document.getElementById("descrMov").value;
					document.getElementById("descrMov").value = nota.substring(
							0, 255);
				}
			}
			var newwindow = '';
			function popitup_movimentacao() {
				if (!newwindow.closed && newwindow.location) {
				} else {
					var popW = 600;
					var popH = 400;
					var winleft = (screen.width - popW) / 2;
					var winUp = (screen.height - popH) / 2;
					winProp = 'width=' + popW + ',height=' + popH + ',left='
							+ winleft + ',top=' + winUp
							+ ',scrollbars=yes,resizable'
					newwindow = window.open('', '${propriedade}', winProp);
					newwindow.name = 'mov';
				}

				newwindow.opener = self;
				t = frm.target;
				a = frm.action;
				frm.target = newwindow.name;
				frm.action = '${pageContext.request.contextPath}/app/expediente/mov/prever?id=${mov.idMov}';
				frm.submit();
				frm.target = t;
				frm.action = a;

				if (window.focus) {
					newwindow.focus()
				}
				return false;
			}
		</script>

		<h2>Anotação - ${mob.siglaEDescricaoCompleta}</h2>
		<form name="frm" action="anotar_gravar" method="post">
			<input type="hidden" name="postback" value="1" /> <input
				type="hidden" name="sigla" value="${sigla}" />
			<table>
				<tr class="header">
					<td colspan="2">Anotação</td>
				</tr>
				<tr>
					<td>Data:</td>
					<td><input type="text" name="dtMovString"
						value="${dtMovString}" onblur="javascript:verifica_data(this,0);" />
					</td>
				</tr>
				<tr>
					<td>Responsável:</td>
					<td><siga:selecao tema="simple" propriedade="subscritor"
							modulo="siga" /> &nbsp;&nbsp; <input type="checkbox"
						theme="simple" name="substituicao" value="${substituicao}"
						onclick="javascript:displayTitular(this);" /> Substituto</td>
				</tr>
				<c:choose>
					<c:when test="${!substituicao}">
						<tr id="tr_titular" style="display: none">
					</c:when>
					<c:otherwise>
						<tr id="tr_titular" style="">
					</c:otherwise>
				</c:choose>
				<td>Titular:</td>
				<input type="hidden" name="campos" value="titularSel.id" />
				<td colspan="3"><siga:selecao propriedade="titular"
						tema="simple" modulo="siga" /></td>
				</tr>
				<tr>
					<td>Função do Responsável:</td>
					<td><input type="hidden" name="campos"
						value="nmFuncaoSubscritor" /> <input type="text"
						name="nmFuncaoSubscritor" value="${nmFuncaoSubscritor}" size="50"
						maxLength="128" theme="simple" /> (opcional)</td>
				</tr>
				<tr>
					<td>Nota</td>
					<td><textarea name="descrMov" value="${descrMov}" cols="60"
							rows="5" onkeydown="corrige();tamanho();" maxlength="255"
							onblur="tamanho();" onclick="tamanho();"></textarea>
						<div id="Qtd">Restam&nbsp;255&nbsp;Caracteres</div></td>
				</tr>
				<c:if test="${tipoResponsavel == 3}">
					<tr>
						<td>Observação</td>
						<td><input type="text" value="${obsOrgao}" size="30"
							name="obsOrgao" /></td>
					</tr>
				</c:if>

				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok"
						class="btn btn-primary" /> <input type="button" value="Cancela"
						onclick="javascript:history.back();" class="btn btn-cancel" /></td>
				</tr>
			</table>
		</form>
	</div>
</siga:pagina>
