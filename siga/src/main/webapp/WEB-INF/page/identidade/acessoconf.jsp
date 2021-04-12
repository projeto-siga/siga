<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>

<script>
	<c:url id="url" value="/siga/app/gi/acesso/listar"></c:url>
	function sbmt(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}

	<c:url id="url" value="/siga/app/gi/acesso/gravar"></c:url>
	function enviar(idServico, idSituacao) {
		if (!IsRunningAjaxRequest()) {
			ReplaceInnerHTMLFromAjaxResponse(
					'<c:out value="${url}"></c:out>?idServico='
							+ idServico
							+ '&idSituacao='
							+ idSituacao
							+ '<c:if test="${idAbrangencia == 4}">&perfilSel.id=${perfilSel.id}</c:if><c:if test="${idAbrangencia == 3}">&pessoaSel.id=${pessoaSel.id}</c:if><c:if test="${idAbrangencia == 2}">&lotacaoSel.id=${lotacaoSel.id}</c:if><c:if test="${idAbrangencia == 1}">&idOrgaoUsu=${idOrgaoUsu}</c:if>',
					null, document.getElementById('SPAN-' + idServico));
		}
	}
</script>

<siga:pagina titulo="Atribuição de Permissões">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Permissões cadastradas</h2>

			<div class="gt-content-box gt-for-table">
				<form id="listar" name="listar"
					action="/siga/app/gi/acesso/listar" method="get" class="form100">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%" />
						</colgroup>

						<tr class="header">
							<td align="center" valign="top" colspan="4">Selecione a
								abrangência</td>
						</tr>

						<tr>
							<td>Abrangência:</td>
							<td><siga:escolha id='idAbrangencia' var='idAbrangencia' singleLine="${true}">
									<siga:opcao id='4' texto="Perfil">
										<siga:selecao tema='simple' titulo="Perfil:"
											propriedade="perfil" modulo="siga"/>
									</siga:opcao>
									<siga:opcao id='1' texto="Órgão usuário">
										<select name="idOrgaoUsu" list="orgaosUsu"
											listKey="idOrgaoUsu" listValue="nmOrgaoUsu" label="Órgão"
											theme="simple" />
									</siga:opcao>
									<siga:opcao id='2' texto="Lotação">
										<siga:selecao tema='simple' titulo="Lotação:"
											propriedade="lotacao" modulo="siga"/>
									</siga:opcao>
									<siga:opcao id='3' texto="Matrícula">
										<siga:selecao tema='simple' titulo="Matrícula:"
											propriedade="pessoa" modulo="siga" />
									</siga:opcao>
								</siga:escolha></td>
						</tr>

						<tr>
							<td colspan="2"><siga:monobotao inputType="submit"
									value="Buscar" cssClass="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
			<br />

			<c:url id="url" value="/siga/app/gi/acesso/servconf_gravar"></c:url>

			<c:if test="${not empty itensHTML}">
				<h2>Permissões</h2>
				<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<colgroup>
							<col width="15%" />
						</colgroup>
						<c:if test="${idAbrangencia == 1}">
							<tr class="header">
								<td>Órgão usuário:</td>
								<td>${nomeOrgaoUsu}</td>
							</tr>
						</c:if>
						<c:if test="${idAbrangencia == 2}">
							<tr class="header">
								<td>Lotação:</td>
								<td>${lotacaoSel.descricao}</td>
							</tr>
							<tr class="header">
								<td>Sigla:</td>
								<td>${lotacaoSel.sigla}</td>
							</tr>
						</c:if>
						<c:if test="${idAbrangencia == 3}">
							<tr class="header">
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
							<td colspan="2">${itensHTML}</td>
						</tr>
					</table>
				</div>
			</c:if>
		</div>
	</div>
	<script> 
		muda_escolha(document.getElementById("idAbrangencia"));
	</script>
</siga:pagina>
