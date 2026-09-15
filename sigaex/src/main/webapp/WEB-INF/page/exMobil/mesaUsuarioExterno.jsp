<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>

<siga:pagina titulo="Lista de Expedientes" popup="${popup}">
	<div id="inicio" class="container-fluid content mb-3">
		<h5>Mesa do Usuário Externo</h5>
		
		<p>
		<c:if
			test="${empty msgPesqErro and (not empty tamanho or tamanho > 0)}">
			Consulte o andamento de seus documentos abaixo.
		</c:if>
		<c:if test="${not empty paginaModelosUrl}">
			Clique <a id="ver-modelos" href="${paginaModelosUrl}">aqui</a> para ver a lista de modelos de documentos que o usuário externo pode criar.
		</c:if>
		</p>

		<c:if test="${not empty msgPesqErro}">
			<div id="msgPesqErro"
				class="alert alert-danger alert-dismissible fade show m-3"
				role="alert">${msgPesqErro}</div>
		</c:if>
		<c:if test="${empty msgPesqErro and (empty tamanho or tamanho == 0)}">
			<div id="msgPesqNaoEncontrou"
				class="alert alert-warning alert-dismissible fade show m-3"
				role="alert">A pesquisa não retornou resultados.</div>
		</c:if>

		<c:if
			test="${empty msgPesqErro and (not empty tamanho or tamanho > 0)}">
			<table class="table table-sm table-hover">
				<thead class="${thead_color}">
					<tr>
						<th colspan="5" align="center">Documento</th>
						<th colspan="2" align="center">Atendente</th>
						<th colspan="2" align="center">Situação</th>
					</tr>
					<tr>
						<th rowspan="1" align="right">Número</th>
						<th rowspan="1" align="center">Data</th>
						<th rowspan="1">Tipo</th>
						<th rowspan="1"><fmt:message key="documento.modelo2" /></th>
						<th rowspan="1"><fmt:message key="documento.descricao" /></th>
						<th align="center"><fmt:message key="usuario.lotacao" /></th>
						<th align="center"><fmt:message key="usuario.pessoa" /></th>
						<th rowspan="1" align="center">Estado</th>
						<th rowspan="1" align="center">Data</th>
					</tr>
				</thead>

				<siga:paginador maxItens="${itemPagina}" maxIndices="10"
					totalItens="${tamanho}" itens="${itens}" var="documento">
					<c:choose>
						<c:when test="${documento[0].eletronico}">
							<c:set var="exibedoc" value="even" />
						</c:when>
						<c:otherwise>
							<c:set var="exibedoc" value="fisicoeven" />
						</c:otherwise>
					</c:choose>
					<c:set var="url"
						value="/sigaex/app/arquivo/exibir?completo=1&arquivo=${documento[0].getMobilDefaultParaReceberJuntada().getSiglaCompacta()}.pdf" />

					<tr class="${exibedoc}">
						<td><a href="${url}" target="_blank">${documento[1].sigla}</a></td>
						<td>${documento[0].dtDocDDMMYY}</td>
						<c:if test="${documento[1].numSequencia != 0}">
							<td>${documento[0].descrFormaDoc}</td>
							<td>${documento[0].nmMod}</td>

							<c:set var="acessivel" value="" />
							<c:set var="acessivel"
								value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular,documento[1])}" />
							<c:if test="${formOrigem eq 'lista'}">
								<c:set var="acessivel" value="true" />
							</c:if>
							<c:choose>
								<c:when test="${acessivel eq true}">
									<c:set var="estilo" value="" />
									<c:if
										test="${f:mostraDescricaoConfidencial(documento[0], titular, lotaTitular) eq true}">
										<c:set var="estilo" value="confidencial" />
									</c:if>
									<td class="${estilo}">
										${f:descricaoSePuderAcessar(documento[0], titular, lotaTitular)}</td>
									<c:if test="${visualizacao == 1}">
										<td class="${estilo}">
											${f:anotacaoConfidencial(documento[1], titular,lotaTitular)}</td>
									</c:if>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${formOrigem eq 'lista'}">
											<a
												href="javascript:opener.retorna_${propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">
												${documento[1].codigo} </a>
										</c:when>
										<c:otherwise>
											<td>CONFIDENCIAL</td>
											<c:if test="${visualizacao == 1}">
												<td>CONFIDENCIAL</td>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<td align="left">${documento[2].dpLotacaoIni.lotacaoAtual.descricao}</td>
							<td align="left">${documento[2].dpPessoaIni.descricao}</td>
							<td align="left">${documento[2].cpMarcador.descrMarcador}</td>
							<td align="left">${documento[2].dtIniMarcaDDMMYYYY}</td>
						</c:if>
					</tr>
				</siga:paginador>
			</table>
		</c:if>
	</div>
	
    <script type="text/javascript">
        function listarDocumentosMesaUsuarioExterno(offset) {
            sigaSpinner.mostrar();

            offset = offset == null ? 0 : offset;

            window.location = '/sigaex/app/expediente/doc/mesa-usuario-externo'
                + '?offset=' + offset;
        }
        function sbmt(offset) {
        	listarDocumentosMesaUsuarioExterno(offset);
        }
    </script>
</siga:pagina>