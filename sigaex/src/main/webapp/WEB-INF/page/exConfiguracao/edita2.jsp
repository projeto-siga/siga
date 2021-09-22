<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de configuração">	
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
		
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
							.getElementById('comboFormaDiv'), function() {transformarEmSelect2(null, '#idFormaDoc', '#idFormaDocGroup');alteraForma()});						
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
							.getElementById('comboModeloDiv'), function() {transformarEmSelect2(null, '#idMod', '#idModGroup')});						
		}							

		function sbmt() {
			editar_gravar = '${pageContext.request.contextPath}/app/configuracao/editar';
			editar_gravar.submit();
		}							
	</script>

	<body onload="aviso()">

		<div class="container-fluid">
			<div class="card bg-light mb-3" >

				<form action="editar_gravar">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="nmTipoRetorno" value="${nmTipoRetorno}" /> <input
						type="hidden" name="id" value="${id}" />

					<c:set var="dataFim" value="" />

					<div class="card-header"><h5>Cadastro de configuração</h5></div>
						<div class="card-body">
							<div class="row">
								<div class="col-sm-3">
									<div class="form-group">
										<label>Tipo de Configuração</label>
										<c:choose>
											<c:when
												test="${campoFixo && not empty config.cpTipoConfiguracao}">
												<input type="hidden" name="idTpConfiguracao"
													value="${config.cpTipoConfiguracao.id}" /> 
												<label class="form-control">${config.cpTipoConfiguracao.dscTpConfiguracao}</label>
											</c:when>
											<c:otherwise>
												<siga:select name="idTpConfiguracao"
													list="listaTiposConfiguracao" listKey="idTpConfiguracao"
													id="idTpConfiguracao" headerValue="[Indefinido]"
													headerKey="0" listValue="dscTpConfiguracao" theme="simple"
													value="${idTpConfiguracao}" />
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label>Situação</label>
										<siga:select name="idSituacao"
											list="listaSituacao" listKey="idSitConfiguracao"
											listValue="descr" theme="simple"
											headerValue="[Indefinido]" headerKey="0" value="${idSituacao}" />
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label>Nível de acesso</label>
										<siga:select name="idNivelAcesso"
											list="listaNivelAcesso" theme="simple" listKey="idNivelAcesso"
											listValue="nmNivelAcesso" headerValue="[Indefinido]"
											headerKey="0" value="${idNivelAcesso}" />
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label>Perfil</label>
										<siga:select name="idPapel"
											list="listaPapel" theme="simple" listKey="idPapel"
											listValue="descPapel" headerValue="[Indefinido]"
											headerKey="0" value="${idPapel}" /></td>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm">
									<div class="form-group">
										<label>Pessoa</label>
										<siga:selecao propriedade="pessoa" tema="simple" modulo="siga" />
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-9">
									<div class="form-group">
										<label>Lotação</label>
										<siga:selecao propriedade="lotacao" tema="simple" modulo="siga" />
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label>Tipo lotação</label>
										<siga:select name="idTpLotacao"
											list="listaTiposLotacao" theme="simple" listKey="idTpLotacao"
											listValue="dscTpLotacao" headerValue="[Indefinido]"
											headerKey="0" value="${idTpLotacao}" />
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<label>Cargo</label>
										<siga:selecao propriedade="cargo" tema="simple" modulo="siga" />
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label>Função de Confiança</label>
										<siga:selecao propriedade="funcao" tema="simple" modulo="siga" />
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-4">
									<div class="form-group">
										<label>Órgão</label>
										<siga:select name="idOrgaoUsu" list="orgaosUsu"
											listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
											headerValue="[Indefinido]" headerKey="0" value="${idOrgaoUsu}" />
									</div>
								</div>
								<div class="col-sm-4">
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
								<div class="col-sm-4">
									<div class="form-group">
										<label>Origem</label>
										<siga:select name="idTpDoc" list="listaTiposDocumento"
											listKey="idTpDoc" listValue="descrTipoDocumento"
											theme="simple" headerValue="[Indefinido]" headerKey="0"
											value="${idTpDoc}" />
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>Tipo da Espécie</label>
									
										<c:choose>
												<c:when test="${campoFixo && not empty config.exModelo}">
													${config.exModelo.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc} - ${config.exModelo.exFormaDocumento.descrFormaDoc}
												</c:when>
												<c:when
													test="${campoFixo && not empty config.exFormaDocumento}">
													${config.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc} - ${config.exFormaDocumento.descrFormaDoc}
												</c:when>
												<c:otherwise>
													<siga:select name="idTpFormaDoc" list="tiposFormaDoc"
														listKey="idTipoFormaDoc" listValue="descTipoFormaDoc"
														theme="simple" headerKey="0" headerValue="[Indefinido]"
														onchange="javascript:alteraTipoDaForma();" id="tipoForma"
														value="${idTpFormaDoc}" />&nbsp;&nbsp;&nbsp;
												   
							       				 <div style="display: inline" id="comboFormaDiv"></div>
												</c:otherwise>
											</c:choose>
									</div>
								</div>
							</div>
							<!-- Esse timeout no modelo está estranho. Está sendo necessário porque primeiro
      		 precisa ser executado o request ajax referente Ã  FormaDocumento, da qual a lista 
		     de modelos depende. Talvez seria bom tornar síncronos esses dois requests ajax    -->
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<c:choose>
											<c:when test="${campoFixo && not empty config.exModelo}">
												<input type="hidden" name="idMod"
													value="${config.exModelo.id}" />
													${config.exModelo.descMod}
											</c:when>
											<c:when												
												test="${campoFixo && not empty config.exFormaDocumento}">
												<input type="hidden" name="idFormaDoc" value="${idFormaDoc}" />
												${config.exFormaDocumento.descrFormaDoc}
											</c:when>
											<c:otherwise>											
												<div style="display: inline" id="comboModeloDiv"></div>
											</c:otherwise>
										</c:choose>
									</div>
								</div>																																									
							</div>							
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>Classificação</label>
										<siga:selecao propriedade="classificacao" tema="simple"
											modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar" />
									</div>
								</div>
							</div>														
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<label>Pessoa Objeto</label>
										<siga:selecao tipo="pessoa" propriedade="pessoaObjeto"
											tema="simple" modulo="siga" />
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label>Lotação Objeto</label>
										<siga:selecao tipo="lotacao" propriedade="lotacaoObjeto"
											tema="simple" modulo="siga" />
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label>Cargo Objeto</label>
										<siga:selecao tipo="cargo" propriedade="cargoObjeto"
											tema="simple" modulo="siga" />
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label>Função de Confiança Objeto</label>
										<siga:selecao tipo="funcao" propriedade="funcaoObjeto"
											tema="simple" modulo="siga" />
									</div>
								</div>
	
								<div class="col-sm-6">
									<div class="form-group">
										<label>Órgão Objeto</label>
										<siga:select name="idOrgaoObjeto" list="orgaosUsu"
											listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
											headerValue="[Indefinido]" headerKey="0"
											value="${idOrgaoObjeto}" />
									</div>
								</div>
							</div>	
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group mb-0">
										<input type="submit" value="Ok"	class="btn btn-primary" /> 
										<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-primary" />
									</div>
								</div>
							</div>
					</div>
				</form>
			</div>
		</div>				
	<script>
		$(document).ready(function() {								
			$('[name=idTpConfiguracao]').addClass('siga-select2');
			$('[name=idOrgaoUsu]').addClass('siga-select2');
			$('[name=idTpMov]').addClass('siga-select2');		
			$('[name=idOrgaoObjeto]').addClass('siga-select2');			
		});
	</script>
	<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>					
	</body>
</siga:pagina>
