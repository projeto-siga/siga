<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Relatório">
	<script type="text/javascript" language="Javascript1.1">
// 		function sbmt() {
// 			frmRelatorios.action = '${pageContext.request.contextPath}/app/expediente/rel/relIndicadoresGestao';
// 			frmRelatorios.submit();
// 		}

		var newwindow = '';
		function visualizarRelatorio(rel) {
			if (!newwindow.closed && newwindow.location) {
			} else {
				var popW = 600;
				var popH = 400;
				var winleft = (screen.width - popW) / 2;
				var winUp = (screen.height - popH) / 2;
				winProp = 'width=' + popW + ',height=' + popH + ',left='
						+ winleft + ',top=' + winUp
						+ ',scrollbars=yes,resizable'
				newwindow = window.open('', '', winProp);
				newwindow.name = 'doc';
			}

			newwindow.opener = self;
			t = frmRelatorios.target;
			a = frmRelatorios.action;
			frmRelatorios.target = newwindow.name;
			frmRelatorios.action = rel;
			frmRelatorios.submit();
			frmRelatorios.target = t;
			frmRelatorios.action = a;

			if (window.focus) {
				newwindow.focus()
			}
			return false;
		}
	</script>

	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Indicadores de Gestão - Órgão ${lotaTitular.orgaoUsuario.descricaoMaiusculas}</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios" action="/sigaex/app/expediente/rel/relIndicadoresGestao" theme="simple" method="get">
					<input type="hidden" name="postback" value="1" /> 

					<div class="row">
						<div class="col-sm-1 mt-4">
							<strong>Período </strong>
						</div>
						<div class="form-group col-sm-2">
							<label>De</label>
<!-- 							<input type="text" name="dataInicial" id="dataInicial" onblur="javascript:verifica_data(this, true);comparaData(dataInicial,dataFinal);" -->
<!-- 								theme="simple" maxlength="10" class="form-control" /> -->
							<input class="form-control" type="text" name="dataInicial"
								id="dataFinal" value="${dataInicial}" onblur="javascript:verifica_data(this,0);" />
						</div>
						<div class="form-group col-sm-2">
							<label>até</label>
							<input class="form-control" type="text" name="dataFinal"
								id="dataFinal" value="${dataFinal}" onblur="javascript:verifica_data(this,0);" />

						</div>
						<div class="form-group col-sm-3">
							<label><fmt:message key="usuario.lotacao"/></label>
							<siga:selecao propriedade="lotacao" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
						</div>
						<div class="form-group col-sm-4">
							<label><fmt:message key="usuario.matricula"/></label>
							<siga:selecao propriedade="usuario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
						</div>
					</div>
					<div class="row">
						<div class="form-group col-sm-4">
							<input type="submit" value="Pesquisar" class="btn btn-primary mt-auto" />
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
					</div>
					<div class="row">
					<c:if test="${totalDocumentos != null}">
						<div class="col-sm-6">
							<div class="card">
								<div class="card-header">Indicadores de Produção</div>
								<div class="card-body">
									<table class="table table-sm table-hover table-striped">
										<tr class="card-text col-sm-6">
											<td class='w-80'>Total de Documentos Produzidos</td>
											<td class='align-right'>${totalDocumentos}</td>
										</tr>
										<tr>
											<td class='w-80'>Total de Páginas Geradas</td>
											<td class='align-right'>${totalPaginas}</td>
										</tr>
										<tr>
											<td class='w-80'>Total de Documentos Tramitados</td>
											<td class='align-right'>${totalTramitados}</td>
										</tr>
									</table>
								</div>
							</div>		
						</div>
					</c:if>
					<c:if test="${volumeTramitacao != null}">
						<div class="col-sm-6">
							<div class="card">
								<div class="card-header">Documentos Por Volume de Tramitação (Top 5)</div>
								<div class="card-body">
									<table class="table table-sm table-hover table-striped">
										<tr>
											<c:forEach items="${volumeTramitacao}" var="item">
												<tr class="card-text col-sm-6">${item}</tr>
											</c:forEach>
										</tr>
									</table>
								</div>
							</div>
						</div>		
					</c:if>
					</div>
					<input type="hidden" name="orgao" id="orgao" value="${orgao}" />
					<input type="hidden" name="lotacaoId" id="lotacaoId" value="${lotacaoSel.id}" />
					<input type="hidden" name="siglaLotacao" id="siglaLotacao" value="${lotacaoSel.sigla}" />
					<input type="hidden" name="usuarioId" id="usuarioId" value="${usuarioSel.id}" />
					<input type="hidden" name="siglaUsuario" id="siglaUsuario" value="${usuarioSel.sigla}" />
				</form>
			</div>
		</div>
	</div>
</siga:pagina>