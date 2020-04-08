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
	<div class="container-fluid">
		<div class="card bg-light mb-3" >

			<div class="card-header"><h5>Identidades cadastradas</h5></div>

			<div class="card-body">
				<form id="listar" name="listar"
					action="/siga/app/gi/identidade/listar" method="get">
					
					<input type="hidden" name="propriedade"	value="${param.propriedade}" />
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="apenasRefresh" value="0" />
					<input type="hidden" name="p.offset" value="0" />
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<siga:selecao titulo="Matrícula" propriedade="pessoa" modulo="siga" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<siga:monobotao inputType="submit"
									value="Buscar" cssClass="btn btn-primary" />
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>

		<br />

		<c:if test="${not empty pessoaSel.id}">
			<div class="card bg-light mb-3" >

				<form id="editar" name="editar"
					action="/siga/app/gi/identidade/editar_gravar" method="get"
					class="form100">
					
					<input type="hidden" name="pessoaSel.id" value="${pessoaSel.id}" />
					<input type="hidden" name="pessoaSel.descricao" value="${pessoaSel.descricao}" />
						<input type="hidden" name="pessoaSel.sigla"
							value="${pessoaSel.sigla}" />
					
						<div class="card-header"><h5>Pessoa</h5></div>
						<div class="card-body">
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>Pessoa:</label>
										<label>${pessoaSel.descricao}</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>Matrícula:</label>
										<label>${pessoaSel.sigla}</label>
									</div>
								</div>
							</div>
							<c:if test="${pessoaSel.objeto.bloqueada}">
								<div class="row">
									<div class="col-sm-12">
										<div class="form-group">
											<span style="color: red"><b>Esta
												pessoa está bloqueada. Para remover o bloqueio, clique no
												botão &quot;Desbloquear&quot;, abaixo.</b> </span>
										</div>
									</div>
								</div>
							</c:if>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<c:choose>
											<c:when test="${pessoaSel.objeto.bloqueada}">
												<input type="button"
													onclick="javascript: sbmt('editar','/siga/app/gi/identidade/desbloquear_pessoa');"
													value="Desbloquear" class="btn btn-primary" />
											</c:when>
											<c:otherwise>
												<input type="button"
													onclick="javascript: sbmt('editar','/siga/app/gi/identidade/bloquear_pessoa');"
													value="Bloquear" class="btn btn-primary" />
											</c:otherwise> 
										</c:choose>
									</div>
								</div>
							</div>
						</div>
					
				</form>
			</div>
			<br />
		</c:if>

		<c:forEach var="ident" items="${itens}">
			<div class="card bg-light mb-3" >
				<form id="editar_${ident.id}" name="editar_${ident.id}"
					action="editar_gravar" method="get"
					class="form100">
					
					<input type="hidden" name="id" value="${ident.id}" />
					<input type="hidden" name="pessoaSel.id" value="${pessoaSel.id}" />
					<input type="hidden" name="pessoaSel.descricao" value="${pessoaSel.descricao}" />
					<input type="hidden" name="pessoaSel.sigla" value="${pessoaSel.sigla}" />
					
					<div class="card-header"><h5>Tipo da Identidade: ${ident.cpTipoIdentidade.dscCpTpIdentidade}</h5></div>
						<div class="card-body">
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>Login:</label>
										<label>${ident.nmLoginIdentidade}</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>Data de criação:</label>
										<label>${ident.dtCriacaoDDMMYYYY}</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<div class="form-group">
										<label>Data de expiração</label>
										<input name="dtExpiracao"
											value="${ident.dtExpiracaoDDMMYYYY}" type="text" size="10"
											maxlength="10" onblur="javascript:verifica_data(this);" class="form-control"/>
									</div>
								</div>
							</div>
									

							<c:if test="${ident.bloqueada}">
								<div class="row">
									<div class="col-sm-12">
										<div class="form-group">
											<span style="color: red"><b>Esta
												identidade está bloqueada. <c:if
												test="${not pessoaSel.objeto.bloqueada}">Para remover o bloqueio, clique no botão
												&quot;Desbloquear&quot;, abaixo.</c:if> </b> </span>
										</div>
									</div>
								</div>
							
							</c:if>

							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<siga:monobotao inputType="submit" value="Gravar" cssClass="btn btn-primary" />
										<input type="button" onclick="javascript: sbmt('editar_${ident.id}','/siga/app/gi/identidade/cancelar');"
											value="Cancelar" class="btn btn-primary" /> 
											
										<c:if test="${not pessoaSel.objeto.bloqueada}">
											<c:choose>
												<c:when test="${ident.bloqueada}">
													<input type="button"
														onclick="javascript: sbmt('editar_${ident.id}','/siga/app/gi/identidade/desbloquear');"
														value="Desbloquear" class="btn btn-primary" />
												</c:when>
												<c:otherwise>
													<input type="button"
														onclick="javascript: sbmt('editar_${ident.id}','/siga/app/gi/identidade/bloquear');"
														value="Bloquear" class="btn btn-primary" />
												</c:otherwise>
											</c:choose>
										</c:if>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<br />
		</c:forEach>
	</div>
</siga:pagina>
