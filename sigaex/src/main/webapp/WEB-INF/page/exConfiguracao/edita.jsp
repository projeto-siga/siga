<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:cfg-edita>
	<script type="text/javascript" language="Javascript1.1">
		$(document).ready(function() {
			alteraTipoDaForma();
			setTimeout("alteraForma()", 5000);
		});

		function alteraTipoDaForma() {
			ReplaceInnerHTMLFromAjaxResponse(
					'${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_formas?tipoForma='
							+ document.getElementById('tipoForma').value
							+ '&idFormaDoc=' + '${idFormaDoc}', null, document
							.getElementById('comboFormaDiv'), function() {
						transformarEmSelect2(null, '#idFormaDoc',
								'#idFormaDocGroup');
						alteraForma()
					});
		}

		function alteraForma() {
			// console.log('altera-forma', document.getElementById('idFormaDoc').value)
			var especie
			var inpEspecie = document.getElementById('idFormaDoc')
			if (inpEspecie)
				especie = inpEspecie.value
			var modelo = '${idMod}'
			var inpModelo = document.getElementById('idMod')
			if (inpModelo)
				modelo = inpModelo.value
				// if (modelo === undefined)
				// 	modelo = '';

			ReplaceInnerHTMLFromAjaxResponse(
					'${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_modelos?forma='
							+ especie + '&idMod=' + modelo, null, document
							.getElementById('comboModeloDiv'), function() {
						transformarEmSelect2(null, '#idMod', '#idModGroup')
					});
		}
	</script>

	<c:if
		test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('TIPO_MOVIMENTACAO,PERFIL')}">
		<div class="row">
			<div class="col col-12">
				<p class="alert alert-dark">Restrições Específicas do Módulo de
					Documentos</p>
			</div>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('TIPO_MOVIMENTACAO')}">
				<div class="col-sm-6 col-lg-4">
					<div class="form-group">
						<label>Tipo de Movimentação</label>
						<c:choose>
							<c:when
								test="${campoFixo && not empty config.exTipoMovimentacao}">
								<input type="hidden" name="idTpMov"
									value="${config.exTipoMovimentacao.idTpMov}" />
												${config.exTipoMovimentacao.descrTipoMovimentacao}
											</c:when>
							<c:otherwise>
								<siga:select name="idTpMov" list="listaTiposMovimentacao"
									listKey="idTpMov" listValue="descrTipoMovimentacao"
									theme="simple" headerValue="[Indefinido]" headerKey="0"
									value="${idTpMov}" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('PERFIL')}">
				<div class="col-sm-6 col-lg-4">
					<div class="form-group">
						<label>Perfil</label>
						<siga:select name="idPapel" list="listaPapel" theme="simple"
							listKey="idPapel" listValue="descPapel"
							headerValue="[Indefinido]" headerKey="0" value="${idPapel}" />
						</td>
					</div>
				</div>
			</c:if>
		</div>
	</c:if>
	<c:if
		test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('TIPO_DOCUMENTO,TIPO_FORMA_DOCUMENTO,MODELO,CLASSIFICACAO,NIVEL_DE_ACESSO')}">
		<div class="row">
			<div class="col col-12">
				<p class="alert alert-dark">Restrições Relativas aos Dados do
					Documento</p>
			</div>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('TIPO_DOCUMENTO')}">
				<div class="col-sm-6 col-lg-2">
					<div class="form-group">
						<label>Origem</label>
						<siga:select name="idTpDoc" list="listaTiposDocumento"
							listKey="idTpDoc" listValue="descrTipoDocumento" theme="simple"
							headerValue="[Indefinido]" headerKey="0" value="${idTpDoc}" />
					</div>
				</div>
			</c:if>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('TIPO_FORMA_DOCUMENTO')}">
				<div class="col-sm-6 col-lg-2">
					<div class="form-group">
						<label>Tipo da Espécie</label>

						<c:choose>
							<c:when test="${campoFixo && not empty config.exModelo}">
													${config.exModelo.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc} - ${config.exModelo.exFormaDocumento.descrFormaDoc}
												</c:when>
							<c:when test="${campoFixo && not empty config.exFormaDocumento}">
													${config.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc} - ${config.exFormaDocumento.descrFormaDoc}
												</c:when>
							<c:otherwise>
								<siga:select name="idTpFormaDoc" list="tiposFormaDoc"
									listKey="idTipoFormaDoc" listValue="descTipoFormaDoc"
									theme="simple" headerKey="0" headerValue="[Indefinido]"
									onchange="javascript:alteraTipoDaForma();" id="tipoForma"
									value="${idTpFormaDoc}" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('TIPO_FORMA_DOCUMENTO')}">
				<div class="col-sm-6 col-lg-4">
					<c:choose>
						<c:when test="${campoFixo && not empty config.exModelo}">
						</c:when>
						<c:when test="${campoFixo && not empty config.exFormaDocumento}">
						</c:when>
						<c:otherwise>
							<div style="display: inline" id="comboFormaDiv"></div>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('MODELO')}">
				<div class="col-sm-6 col-lg-4">
					<div class="form-group">
						<c:choose>
							<c:when test="${campoFixo && not empty config.exModelo}">
								<input type="hidden" name="idMod" value="${config.exModelo.id}" />
													${config.exModelo.descMod}
											</c:when>
							<c:when test="${campoFixo && not empty config.exFormaDocumento}">
								<input type="hidden" name="idFormaDoc" value="${idFormaDoc}" />
												${config.exFormaDocumento.descrFormaDoc}
											</c:when>
							<c:otherwise>
								<div style="display: inline" id="comboModeloDiv"></div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('CLASSIFICACAO')}">
				<div class="col-sm-6 col-lg-4">
					<div class="form-group">
						<label>Classificação</label>
						<siga:selecao propriedade="classificacao" tema="simple"
							modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar" />
					</div>
				</div>
			</c:if>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('NIVEL_DE_ACESSO')}">
				<div class="col-sm-6 col-lg-4">
					<div class="form-group">
						<label>Nível de acesso</label>
						<siga:select name="idNivelAcesso" list="listaNivelAcesso"
							theme="simple" listKey="idNivelAcesso" listValue="nmNivelAcesso"
							headerValue="[Indefinido]" headerKey="0" value="${idNivelAcesso}" />
					</div>
				</div>
			</c:if>
		</div>
	</c:if>

	<script>
		$(document).ready(function() {
			$('[name=idTpMov]').addClass('siga-select2');
		});
	</script>
</siga:cfg-edita>
