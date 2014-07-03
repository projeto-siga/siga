<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

  <div class="gt-content clearfix">	
		 
		<h1>${tpConfiguracao.dscTpConfiguracao}</h1>
		<b> Situação default: </b> ${tpConfiguracao.situacaoDefault.dscSitConfiguracao} <br/>		
		<br />
		
		<div class="gt-content-box gt-for-table">
		
		<table class="gt-list-table" width="100%" border="1">
			<tr class="header">
				<td align="center" width="03%"><b>ID</b></td>
				<td align="center" width="05%"><b>Nível de acesso</b></td>
				<td align="center" width="05%"><b>Pessoa</b></td>
				<td align="center" width="05%"><b>Lotação</b></td>
				<td align="center" width="05%"><b>Função</b></td>
				<td align="center" width="03%"><b>Órgão</b></td>
				<td align="center" width="03%"><b>Órgão Objeto</b></td>
				<td align="center" width="05%"><b>Cargo</b></td>
				<td align="center" width="12%"><b>Tipo de Movimentação</b></td>
				<td align="center" width="05%"><b>Via</b></td>
				<td align="center" width="13%"><b>Modelo</b></td>
				<td align="center" width="05%"><b>Classificação</b></td>
				<td align="center" width="05%"><b>Tipo forma de documento</b></td>
				<td align="center" width="10%"><b>Forma de documento</b></td>
				<td align="center" width="05%"><b>Tipo de Documento</b></td>
				<td align="center" width="03%"><b>Vínculo</b></td>
				<td align="center" width="05%"><b>Serviço</b></td>
				<td align="center" width="05%"><b>Situação</b></td>
				<td align="right" width="10%"></td>
			</tr>
			<c:set var="evenorodd" value="" />
			<c:set var="tamanho" value="0" />

			<c:forEach var="configuracao" items="${listConfig}">		
				<tr class="${evenorodd}">				
					<td align="right">${configuracao.idConfiguracao}</td>
					<td><c:if test="${not empty configuracao.exNivelAcesso}">${configuracao.exNivelAcesso.nmNivelAcesso}(${configuracao.exNivelAcesso.grauNivelAcesso})</c:if></td>
					<td><c:if test="${not empty configuracao.dpPessoa}">
						<siga:selecionado sigla="${configuracao.dpPessoa.iniciais}"
							descricao="${configuracao.dpPessoa.descricao}" />
					</c:if></td>
					<td><c:if test="${not empty configuracao.lotacao}">
						<siga:selecionado sigla="${configuracao.lotacao.sigla}"
							descricao="${configuracao.lotacao.descricao}" />
					</c:if></td>
					<td><c:if test="${not empty configuracao.funcaoConfianca}">${configuracao.funcaoConfianca.nomeFuncao}</c:if></td>
					<td><c:if test="${not empty configuracao.orgaoUsuario}">${configuracao.orgaoUsuario.acronimoOrgaoUsu}</c:if></td>
					<td><c:if test="${not empty configuracao.orgaoObjeto}">${configuracao.orgaoObjeto.acronimoOrgaoUsu}</c:if></td>
					<td><c:if test="${not empty configuracao.cargo}">${configuracao.cargo.nomeCargo}</c:if></td>
					<td><c:if test="${not empty configuracao.exTipoMovimentacao}">${configuracao.exTipoMovimentacao.descrTipoMovimentacao}</c:if></td>
					<td><c:if test="${not empty configuracao.exVia}">${configuracao.exVia.exTipoDestinacao}(${configuracao.exVia.codVia})</c:if></td>
					<td><c:if test="${not empty configuracao.exModelo}">${configuracao.exModelo.nmMod}</c:if></td>
					<td><c:if test="${not empty configuracao.exClassificacao}">${configuracao.exClassificacao.classificacaoAtual.codificacao}</c:if></td>
					<td><c:if test="${not empty configuracao.exFormaDocumento.exTipoFormaDoc}">${configuracao.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc}</c:if></td>
					<td><c:if test="${not empty configuracao.exFormaDocumento}">${configuracao.exFormaDocumento.descrFormaDoc}</c:if></td>
					<td><c:if test="${not empty configuracao.exTipoDocumento}">${configuracao.exTipoDocumento.descrTipoDocumento}</c:if></td>
					<td><c:if test="${not empty configuracao.exPapel}">${configuracao.exPapel.descPapel}</c:if></td>
					<td><c:if test="${not empty configuracao.cpServico}">${configuracao.cpServico.dscServico}</c:if></td>
					<td><c:if
						test="${not empty configuracao.cpSituacaoConfiguracao}">${configuracao.cpSituacaoConfiguracao.dscSitConfiguracao}</c:if></td>
					<td><ww:url id="url" action="editar"
						namespace="/expediente/configuracao">
						<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
					</ww:url> <ww:a href="%{url}">Alterar<br></ww:a> 
					
					
					<ww:url id="urlExcluir" action="excluir" namespace="/expediente/configuracao">
						<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
	                </ww:url>					
					<siga:link title="Excluir" url="${urlExcluir}" 
							popup="excluir" confirm="Deseja excluir configuração?" />				
			
			     </td>	
				</tr>					
				<c:choose>
					<c:when test='${evenorodd == "even"}'>
						<c:set var="evenorodd" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="evenorodd" value="even" />
					</c:otherwise>
				</c:choose>
				<c:set var="tamanho" value="${tamanho + 1 }" />			
			</c:forEach>
			<tr class="footer">
				<td colspan="8">Total Listado: ${tamanho}</td>
			</tr>
		</table>
		<br />
		
		</div>
		
	</div>	

