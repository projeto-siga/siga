<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script>
	function sbmt(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}
</script>

<siga:pagina titulo="Lista Pessoas e Identidades">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Identidades cadastradas</h2>

			<div class="gt-content-box gt-for-table">
				<form id="listar" name="listar"
					action="/siga/app/gi/identidade/listar" method="get"
					class="form100">
					
					<input type="hidden" name="propriedade"	value="${param.propriedade}" />
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="apenasRefresh" value="0" />
					<input type="hidden" name="p.offset" value="0" />
					
					<div class="card w-75 mx-auto">
						<div class="card-header">
							Selecione a Pessoa
						</div>
						<div class="card-body">
							<siga:selecao titulo="Matrícula" propriedade="pessoa" modulo="siga" />
							<siga:monobotao inputType="submit" value="Buscar" cssClass="btn btn-primary" />
						</div>
					</div>
				</form>
			</div>
		</div>

		<br />

		<c:if test="${not empty pessoaSel.id}">
			<div class="gt-content-box gt-for-table">
				<form id="editar" name="editar"
					action="/siga/app/gi/identidade/editar_gravar" method="get"
					class="form100">
					
					<input type="hidden" name="pessoaSel.id" value="${pessoaSel.id}" />
					<input type="hidden" name="pessoaSel.descricao" value="${pessoaSel.descricao}" />
						<input type="hidden" name="pessoaSel.sigla"
							value="${pessoaSel.sigla}" />
					
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

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

						<c:if test="${pessoaSel.objeto.bloqueada}">
							<tr>
								<td colspan="4"><span style="color: red"><b>Esta
											pessoa está bloqueada. Para remover o bloqueio, clique no
											botão &quot;Desbloquear&quot;, abaixo.</b> </span></td>
							</tr>
						</c:if>

						<tr>
							<td colspan="2">
								<c:choose>
									<c:when test="${pessoaSel.objeto.bloqueada}">
										<input type="button"
											onclick="javascript: sbmt('editar','/siga/app/gi/identidade/desbloquear_pessoa');"
											value="Desbloquear" class="gt-btn-medium gt-btn-left" />
									</c:when>
									<c:otherwise>
										<input type="button"
											onclick="javascript: sbmt('editar','/siga/app/gi/identidade/bloquear_pessoa');"
											value="Bloquear" class="gt-btn-medium gt-btn-left" />
									</c:otherwise> 
								</c:choose>
								
							</td>
						</tr>
					</table>
				</form>
			</div>
			<br />
		</c:if>

		<c:forEach var="ident" items="${itens}">
			<div class="gt-content-box gt-for-table">
				<form id="editar_${ident.id}" name="editar_${ident.id}"
					action="editar_gravar" method="get"
					class="form100">
					
					<input type="hidden" name="id" value="${ident.id}" />
					<input type="hidden" name="pessoaSel.id" value="${pessoaSel.id}" />
					<input type="hidden" name="pessoaSel.descricao" value="${pessoaSel.descricao}" />
					<input type="hidden" name="pessoaSel.sigla" value="${pessoaSel.sigla}" />
					
					<table class="gt-form-table">
						<colgroup>
							<col width="15%"></col>
						</colgroup>

						<tr class="header">
							<td align="left" valign="top" colspan="4">Tipo da
								Identidade: ${ident.cpTipoIdentidade.dscCpTpIdentidade}</td>
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

						<c:if test="${ident.bloqueada}">
							<tr>
								<td colspan="4"><span style="color: red"><b>Esta
											identidade está bloqueada. <c:if
												test="${not pessoaSel.objeto.bloqueada}">Para remover o bloqueio, clique no botão
					&quot;Desbloquear&quot;, abaixo.</c:if> </b> </span></td>
							</tr>
						</c:if>

						<tr>
							<td colspan="2">
								<siga:monobotao inputType="submit" value="Gravar" cssClass="gt-btn-medium gt-btn-left" />
								<input type="button" onclick="javascript: sbmt('editar_${ident.id}','/siga/app/gi/identidade/cancelar');"
									value="Cancelar" class="gt-btn-medium gt-btn-left" /> 
									
								<c:if test="${not pessoaSel.objeto.bloqueada}">
									<c:choose>
										<c:when test="${ident.bloqueada}">
											<input type="button"
												onclick="javascript: sbmt('editar_${ident.id}','/siga/app/gi/identidade/desbloquear');"
												value="Desbloquear" class="gt-btn-medium gt-btn-left" />
										</c:when>
										<c:otherwise>
											<input type="button"
												onclick="javascript: sbmt('editar_${ident.id}','/siga/app/gi/identidade/bloquear');"
												value="Bloquear" class="gt-btn-medium gt-btn-left" />
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<br />
		</c:forEach>
	</div>
</siga:pagina>
