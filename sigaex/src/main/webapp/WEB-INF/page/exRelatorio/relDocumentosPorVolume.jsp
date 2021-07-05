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

	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Documentos por Volume</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios"
					action="/sigaex/app/expediente/rel/relDocumentosPorVolume"
					theme="simple" method="get">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="lotacaoTitular"
							value="${lotaTitular.siglaLotacao}" />
					 <input type="hidden"
							name="orgao" id="orgao" value="${orgao}" />
					 <input type="hidden"
							name="idTit" value="${titular.id}" /> <input type="hidden"
							name="nomeArquivoRel" value="${nomeArquivoRel}" />

					<div class="form-row">
						<div class="form-group col-md-2">
							<label for="dtDocString">Data Inicial</label> <input
								class="form-control" type="text" name="dataInicial"
								id="dataInicial" onblur="javascript:verifica_data(this,0);"
								value="${dataInicial}" />
						</div>
						<div class="form-group col-md-2">
							<label for="dtDocFinalString">Data Final</label> <input
								class="form-control" type="text" name="dataFinal" id="dataFinal"
								onblur="javascript:verifica_data(this,0);" value="${dataFinal}" />
						</div>

						<div class="form-group col-md-4">
							<label><fmt:message key="usuario.lotacao" /></label>
							<siga:selecao propriedade="lotacao" tema="simple" reler="sim"
								modulo="siga" />
						</div>
						<div class="form-group col-md-4">
							<label><fmt:message key="usuario.matricula" /></label>
							<siga:selecao propriedade="usuario" tema="simple"
								paramList="buscarFechadas=true" modulo="siga" />
						</div>
						<div class="form-group col-md-4">
							<input type="submit" value="Pesquisar" class="btn btn-primary" />
						</div>
						<input type="hidden" name="lotacaoId" value="${lotacaoSel.id}" />
						<input type="hidden" name="siglalotacao"
							value="${lotacaoSel.sigla}" /> <input type="hidden"
							name="usuarioId" value="${usuarioSel.id}" /> <input
							type="hidden" name="siglaUsuario" value="${usuarioSel.sigla}" />
					</div>
					<c:if test="${primeiraVez == false}">
						<c:if test="${not empty tamanho and tamanho > 0}">
							<h2 class="mt-3">
								<fmt:message key="documento.encontrados" />
							</h2>
							<table class="gt-table table table-sm table-hover">
								<thead class="thead-light">
									<tr>
										<th rowspan="1" align="center"><b>Documentos
												Produzidos: ${totalDocumentos}</b></th>
										<th colspan="1" align="center"><b>Páginas Produzidas:
												${totalPaginas}</b></th>
									</tr>
								</thead>
							</table>
							<c:if test="${indicadoresProducao.size() > 0}">
								<table class="gt-table table table-sm table-hover">
									<thead class="${thead_color}">
										<tr>
											<th rowspan="1" align="center">Unidade</th>
											<th colspan="1" align="center">Nome do documento</th>
											<th rowspan="1" align="center">Quantidade</th>
										</tr>
									</thead>
									<c:forEach var="campos" items="${indicadoresProducao}">
										<th rowspan="2" align="left">${campos}</th>
									</c:forEach>
									<tr>
										<th rowspan="1" align="center"></th>
										<th colspan="1" align="center"></th>
										<th rowspan="1" align="right"><button type="button"
												onclick="javascript:visualizarRelatorio('${pageContext.request.contextPath}/app/expediente/rel/emiteRelDocsPorVolumeDetalhes');"
												class="btn btn-primary float-right">Relatório
												completo</button></th>
									</tr>
								</table>
							</c:if>
						</c:if>
					</c:if>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>