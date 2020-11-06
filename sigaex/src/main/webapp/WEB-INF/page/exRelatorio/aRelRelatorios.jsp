<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@
page
	language="java" contentType="text/html; charset=UTF-8" buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Relatório">

	<script type="text/javascript" language="Javascript1.1">
		function sbmt() {
			frmRelatorios.action = '${actionName}';
			frmRelatorios.submit();
		}

		var newwindow = '';
		function visualizarRelatorio(rel) {
			// Alterado para gerar o relatório na própria página, pois assim os erros podem ser tratados com o botão de "Voltar".
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

	<c:choose>
		<c:when test='${nomeArquivoRel eq "relFormularios.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelFormularios</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Formulários</c:set>
			<c:set var="nomeRelatorio" scope="request">relFormularios.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relConsultaDocEntreDatas.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelDocEntreDatas</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de documentos entre datas</c:set>
			<c:set var="nomeRelatorio" scope="request">relConsultaDocEntreDatas.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relModelos.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelModelos</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Modelos</c:set>
			<c:set var="nomeRelatorio" scope="request">relModelos.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relDocumentosSubordinados.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelDocumentosSubordinados</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Documentos em Setores Subordinados</c:set>
			<c:set var="nomeRelatorio" scope="request">relDocumentosSubordinados.jsp</c:set>
		</c:when>
		<c:when
			test='${nomeArquivoRel eq "relMovimentacaoDocSubordinados.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelMovDocsSubordinados</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Movimentação de Documentos em Setores Subordinados</c:set>
			<c:set var="nomeRelatorio" scope="request">relMovimentacaoDocSubordinados.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relCrDocSubordinados.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelDocsSubCriados</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Criação de Documentos em Setores Subordinados</c:set>
			<c:set var="nomeRelatorio" scope="request">relCrDocSubordinados.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relMovimentacao.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelMovimentacao</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Movimentações</c:set>
			<c:set var="nomeRelatorio" scope="request">relMovimentacao.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relMovCad.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelMovCad</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Movimentações por Cadastrante</c:set>
			<c:set var="nomeRelatorio" scope="request">relMovCad.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relOrgao.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelOrgao</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Despachos e Transferências</c:set>
			<c:set var="nomeRelatorio" scope="request">relOrgao.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relTipoDoc.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelTipoDoc</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Documentos Criados</c:set>
			<c:set var="nomeRelatorio" scope="request">relTipoDoc.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relMovProcesso.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelMovProcesso</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Movimentações de Processos</c:set>
			<c:set var="nomeRelatorio" scope="request">relMovProcesso.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relClassificacao.jsp"}'>
			<c:set var="actionName" scope="request">aRelClassificacao</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Classificação Documental</c:set>
			<c:set var="nomeRelatorio" scope="request">relClassificacao.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relDocsClassificados.jsp"}'>
			<c:set var="actionName" scope="request">emiteRelClassDocDocumentos</c:set>
			<c:set var="titulo_pagina" scope="request">Relação de Documentos Classificados</c:set>
			<c:set var="nomeRelatorio" scope="request">relDocsClassificados.jsp</c:set>
		</c:when>
		<c:when test='${nomeArquivoRel eq "relDocumentosPorVolume.jsp"}'>
			<c:set var="actionName" scope="request">relRelatorios</c:set>
			<c:set var="titulo_pagina" scope="request">Relatórios Gerenciais</c:set>
			<c:set var="nomeRelatorio" scope="request">relDocumentosPorVolume.jsp</c:set>
		</c:when>

		<c:otherwise>
			<c:set var="actionName" scope="request">emiteRelExpedientes</c:set>
			<c:set var="titulo_pagina" scope="request">Relatório de Expedientes</c:set>
			<c:set var="nomeRelatorio" scope="request">relExpedientes.jsp</c:set>
			<c:set var="tipoRelatorio" scope="request">relExpedientes.jrxml</c:set>
		</c:otherwise>
	</c:choose>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>${titulo_pagina}</h5>
			</div>
			<div class="card-body">
				<form name="frmRelatorios" action="${actionName}" theme="simple"
					method="get" enctype="multipart/form-data">
					<input type="hidden" name="newWindow" value="1" />
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="secaoUsuario"
						value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" /> <input
						type="hidden" name="tipoRelatorio" value="${tipoRelatorio}" /> <input
						type="hidden" name="botao" value="${tipoRelatorio}" />
					<c:import url="/WEB-INF/page/exRelatorio/${nomeRelatorio}" />

					<c:if test="${botao != 'pesquisar'}">
						<div class="row mt-2">
							<div class="col-sm-8">
								<button type="button"
									onclick="javascript:visualizarRelatorio('${pageContext.request.contextPath}/app/expediente/rel/${actionName}');"
									class="btn btn-primary">Gerar</button>
							</div>
						</div>
					</c:if>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
