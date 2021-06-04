<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<script type="text/javascript">
		$(document).ready(function() {
			$("#comboorgao").val("${cpOrgaoUsuario.id}");
			
			$('#comboorgao').change(function () {
				var optionSelected = $(this).find("option:selected");	
			    var valueSelected = this.value;
			    var link = $("#btnPesqOrgao").attr('href');
			    var equalPosition = link.indexOf('='); //Get the position of '='
			    var hreflink = link.substring(0,equalPosition + 1);
			    hreflink = hreflink + valueSelected;
				$("#btnPesqOrgao").attr("href", hreflink);
				window.location.href = hreflink;
			});
		});
	</script>
	
	<form id="formulario" enctype='multipart/form-data'>
		<div class="gt-bd clearfix">
	
			<div class="gt-content clearfix">
	
				<h2>Configura&ccedil;&otilde;es no GI</h2>
	
				<div class="gt-form clearfix">			
					<div class="coluna margemDireitaG">
						<label for="cpOrgaoUsuario.idOrgaoUsu">Org&atilde;o do usu&aacute;rio </label>
						<siga:select id="comboorgao" name="comboorgao" list="cpOrgaoUsuarios" listKey="idOrgaoUsu" listValue="nmOrgaoUsu" value="${orgaoUsuario.idOrgaoUsu}" headerKey="0" headerValue="Nenhum"/>
					</div>
					<div class="coluna gt-table-buttons">
						<a class="invisivel" id="btnPesqOrgao" href="${linkTo[ConfiguracaoGIController].pesquisar(cpOrgaoUsuario.idOrgaoUsu)}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.buscar"/></a>
					</div>
				</div>
		
				<c:choose>
					<c:when test="${cpConfiguracoes.size()>0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table">
							<thead>
								<tr>
									<th width="1%" class="noSort"></th>
									<th>Lota&ccedil;&atilde;o</th>
									<th>Matr&iacute;cula</th>
									<th>Pode/N&atilde;o Pode</th>
									<th>Tipo</th>
									<th>Sigla Servi&ccedil;o</th>
									<th>Complexo</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${cpConfiguracoes}" var="cpConfiguracao">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[ConfiguracaoGIController].editar(cpConfiguracao.idConfiguracao)}" titulo="cpConfiguracao"/>
										</td>
										<td>${cpConfiguracao.lotacao != null ? cpConfiguracao.lotacao.nomeLotacao : ""}</td>
										<td>${cpConfiguracao.dpPessoa != null ? cpConfiguracao.dpPessoa.nomePessoa : "" }</td>	
										<td>${cpConfiguracao.cpSituacaoConfiguracao != null ? cpConfiguracao.cpSituacaoConfiguracao.dscSitConfiguracao : "" }</td>
										<td>${cpConfiguracao.cpTipoConfiguracao != null ? cpConfiguracao.cpTipoConfiguracao.dscTpConfiguracao : "" }</td>
										<td>${cpConfiguracao.cpServico != null ? cpConfiguracao.cpServico.siglaServico : "" }</td>
										<td>${cpConfiguracao.complexo != null ? cpConfiguracao.complexo.nomeComplexo : "" }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
					</c:when>
					<c:otherwise>
						<br />
						<h3>N&atilde;o existem configuracoes cadastradas.</h3>
					</c:otherwise>
				</c:choose>
	
				<div class="gt-table-buttons">
					<a href="${linkTo[ConfiguracaoGIController].incluir(cpOrgaoUsuario.idOrgaoUsu)}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>
				</div>
			</div>
		</div>	
	</form>
</siga:pagina>