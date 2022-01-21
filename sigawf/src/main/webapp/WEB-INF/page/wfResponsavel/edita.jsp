<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Responsável">
	<div class="container-fluid content" ng-app="app" ng-controller="ctrl">
		<h5>Edição de Responsável</h5>

		<form name="frm" method="post" action="/sigawf/app/responsavel/gravar"
			class="form">
			<input type="hidden" name="id" value="${dr.id}" />
			<fieldset title="Dados Básicos">
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label>Nome</label> <input type="text" name="nome"
								value="${dr.nome}" size="80" class="form-control" />
						</div>
					</div>
					<div class="col-sm-8">
						<div class="form-group">
							<label>Descrição</label> <input type="text" name="descr"
								value="${dr.descr}" size="80" class="form-control" />
						</div>
					</div>
				</div>
			</fieldset>

			<c:if test="${not empty dr}">
				<fieldset title="Designações">
					<table class="table table-sm table-striped">
						<tr class="header">
							<td>Órgão Usuário</td>
							<td>Responsável</td>
						</tr>

						<c:forEach var="r" items="${itens}" varStatus="loop">
							<tr>
								<td>${r.orgaoSigla}</td>
								<td><input type="hidden"
									name="itens[${loop.index}].orgaoId" value="${r.orgaoId}" />
								<siga:pessoaLotaSelecao2 hideLabels="${false}" labelPessoaLotacao="Tipo"
										propriedadeLotacao="itens[${loop.index}].lotacao"
										propriedadePessoa="itens[${loop.index}].pessoa" /></td>
							</tr>
						</c:forEach>
					</table>
				</fieldset>
			</c:if>

			<div class="row">
				<div class="col-sm-12">
					<div class="form-group mb-0">
						<button class="btn btn-primary"
							onclick="javascript:gravarResponsabilidade()">Ok</button>
						<button value="Desativar" class="btn btn-primary"
							ng-click="desativar()">Desativar</button>
						<button onclick="javascript:history.back();"
							class="btn btn-primary">Cancelar</button>
					</div>
				</div>
			</div>
		</form>
	</div>

</siga:pagina>