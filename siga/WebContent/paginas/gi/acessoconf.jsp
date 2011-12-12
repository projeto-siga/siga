<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>

<script>
<ww:url id="url" action="listar" namespace="/gi/acesso">
</ww:url>
function sbmt(id, action) {
	var frm = document.getElementById(id);
	frm.action=action;
	frm.submit();
	return;
}

<ww:url id="url" action="gravar" namespace="/gi/acesso">
</ww:url>
function enviar(idServico, idSituacao) {
	if (!IsRunningAjaxRequest()) {
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>?idServico=' + idServico 
			+ '&idSituacao=' + idSituacao + '<c:if test="${idAbrangencia == 4}">&perfilSel.id=${perfilSel.id}</c:if><c:if test="${idAbrangencia == 3}">&pessoaSel.id=${pessoaSel.id}</c:if><c:if test="${idAbrangencia == 2}">&lotacaoSel.id=${lotacaoSel.id}</c:if><c:if test="${idAbrangencia == 1}">&idOrgaoUsu=${idOrgaoUsu}</c:if>', null, document.getElementById('SPAN-' + idServico));
	}
}
</script>

<siga:pagina titulo="Atribuição de Permissões">
	<h1>Permissões cadastradas:</h1>

	<form id="listar" name="listar" action="/siga/gi/acesso/listar.action"
		method="GET" class="form100">
	<table class="form100">

		<tr class="header">
			<td align="center" valign="top" colspan="4">Selecione a
			abrangência</td>
		</tr>

		<tr>
			<td>Abrangência:</td>
			<td><siga:escolha var='idAbrangencia'>
				<siga:opcao id='4' texto="Perfil">
					<siga:selecao tema='simple' titulo="Perfil:" propriedade="perfil" />
				</siga:opcao>
				<siga:opcao id='1' texto="Órgão usuário">
					<ww:select name="idOrgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
						listValue="nmOrgaoUsu" label="Órgão" theme="simple" />
				</siga:opcao>
				<siga:opcao id='2' texto="Lotação">
					<siga:selecao tema='simple' titulo="Lotação:" propriedade="lotacao" />
				</siga:opcao>
				<siga:opcao id='3' texto="Matrícula">
					<siga:selecao tema='simple' titulo="Matrícula:"
						propriedade="pessoa" />
				</siga:opcao>
			</siga:escolha></td>
		</tr>

		<tr>
			<td></td>
			<td><siga:monobotao inputType="submit" value="Buscar" /></td>
		</tr>
	</table>
	</form>
	<br />


	<ww:url id="url" action="servconf_gravar" namespace="/gi">
	</ww:url>

	<c:if test="${not empty itensHTML}">
		<table class="form100">
			<tr class="header">
				<td align="left" valign="top" colspan="4">Permissões</td>
			</tr>
			<c:if test="${idAbrangencia == 1}">
				<tr>
					<td>Órgão usuário:</td>
					<td>${nomeOrgaoUsu}</td>
				</tr>
			</c:if>
			<c:if test="${idAbrangencia == 2}">
				<tr>
					<td>Lotação:</td>
					<td>${lotacaoSel.descricao}</td>
				</tr>
				<tr>
					<td>Sigla:</td>
					<td>${lotacaoSel.sigla}</td>
				</tr>
			</c:if>
			<c:if test="${idAbrangencia == 3}">
				<tr>
					<td>Pessoa:</td>
					<td>${pessoaSel.descricao}</td>
				</tr>

				<tr>
					<td>Matrícula:</td>
					<td>${pessoaSel.sigla}</td>
				</tr>
			</c:if>
			<c:if test="${idAbrangencia == 4}">
				<tr>
					<td>Perfil:</td>
					<td>${perfilSel.descricao}</td>
				</tr>
				<tr>
					<td>Sigla:</td>
					<td>${perfilSel.sigla}</td>
				</tr>
			</c:if>
			<tr>
				<td>Serviços:</td>
				<td>${itensHTML}</td>
			</tr>
		</table>
	</c:if>

</siga:pagina>
