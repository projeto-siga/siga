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
		<fmt:setLocale value="pt-BR" />
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Relatório de Cobrança - Órgão ${lotaTitular.orgaoUsuario.descricaoMaiusculas}</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios" action="/sigaex/app/expediente/rel/relCobranca" theme="simple" method="get">
					<div class="row">
						<div class="col-sm-1 mt-4">
							<strong>Período </strong>
						</div>
						<div class="form-group col-sm-2">
							<label>De</label>
							<input class="form-control" type="text" name="dataInicial"
								id="dataInicial" value="${dataInicial}" onblur="javascript:verifica_data(this,0);" />
					    </div>
						<div class="form-group col-sm-2">
							<label>até</label>
							<input class="form-control" type="text" name="dataFinal"
								id="dataFinal" value="${dataFinal}" onblur="javascript:verifica_data(this,0);" />

						</div>
						<div class="form-group col-sm-3">
							<label><fmt:message key="usuario.lotacao"/></label>
							<siga:selecao propriedade="lotacao" siglaInicial="${lotacao}" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
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
					<c:if test="${totalDocumentos != null}">
						<div class="row">
							<div class="col-sm-12">
								<div class="card">
									<div class="card-header">Indicadores de Produção</div>
									<div class="card-body">
										<table class="table table-sm table-hover table-striped">
											<tr class="card-text col-sm-6">
												<td class='w-80'>Total de Documentos Produzidos</td>
												<td class='text-right'>
													<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalDocumentos}" />
												</td>
											</tr>
											<tr>
												<td class='w-80'>Total de Páginas Geradas</td>
												<td class='text-right'>
													<fmt:formatNumber type = "number" pattern = "###,###,###,##0" value = "${totalPaginas}" />
												</td>
											</tr>
											<tr>
												<td class='w-80'>Total de MBytes dos Documentos)</td>
												<td class='text-right'>
													<fmt:formatNumber type = "number" pattern = "###,###,###,##0" value = "${totalBlobsDoc}" />
												</td>
											</tr>
											<tr>
												<td class='w-80'>Total de MBytes dos Anexos</td>
												<td class='text-right'>
													<fmt:formatNumber type = "number" pattern = "###,###,###,##0" value = "${totalBlobsAnexos}" />
												</td>
											</tr>
										</table>
									</div>
								</div>		
							</div>
						</div>
					</c:if>
					<c:if test="${listLinhas != null}">
						<div class="row">
							<table class="table table-hover table-striped">
								<thead class="thead-dark align-middle text-center">
									<tr>
										<th class="text-left">Unidade</th>
										<th class="text-left">Nome do Documento</th>
										<th class="text-left">Núm.</th>
										<th class="text-left">Qtd. Pág.</th>
										<th class="text-left">Tam.Doc.</th>
										<th class="text-left">Tam.Anexos</th>
									</tr>
								</thead>
								<tbody class="table-bordered">
								<c:forEach items="${listLinhas}" var="item" varStatus="status">
									<tr>
										<td>
											${item[0]}
										</td>
										<td>
											${item[1]}
										</td>
										<td>
											${item[2]}
										</td>
										<td>
											${item[3]}
										</td>
										<td>
											${item[4]}
										</td>
										<td>
											${item[5]}
										</td>
									</tr>
								</c:forEach> 
								</tbody>
							</table>
						</div>
					</c:if>
					<input type="hidden" name="orgao" id="orgao" value="${orgao}" />
				</form>
			</div>
		</div>
	</div>
</siga:pagina>