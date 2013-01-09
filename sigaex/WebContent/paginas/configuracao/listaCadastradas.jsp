<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
		 
		<h1>${tpConfiguracao.dscTpConfiguracao}</h1>
		<b> Situação default: </b> ${tpConfiguracao.situacaoDefault.dscSitConfiguracao} <br/>		
		<br />
		
		<table class="list" width="100%" border="1">
			<tr class="header">
				<td align="center" width="03%"><b>ID</b></td>
				<td align="center" width="05%"><b>Nível de acesso</b></td>
				<td align="center" width="05%"><b>Pessoa</b></td>
				<td align="center" width="05%"><b>Lotação</b></td>
				<td align="center" width="05%"><b>Função</b></td>
				<td align="center" width="10%"><b>Órgão</b></td>
				<td align="center" width="05%"><b>Cargo</b></td>
				<td align="center" width="08%"><b>Tipo de Movimentação</b></td>
				<td align="center" width="05%"><b>Via</b></td>
				<td align="center" width="10%"><b>Modelo</b></td>
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
					<td><c:if test="${not empty configuracao.orgaoUsuario}">${configuracao.orgaoUsuario.nmOrgaoUsu}</c:if></td>
					<td><c:if test="${not empty configuracao.cargo}">${configuracao.cargo.nomeCargo}</c:if></td>
					<td><c:if test="${not empty configuracao.exTipoMovimentacao}">${configuracao.exTipoMovimentacao.descrTipoMovimentacao}</c:if></td>
					<td><c:if test="${not empty configuracao.exVia}">${configuracao.exVia.destinacao}(${configuracao.exVia.codVia})</c:if></td>
					<td><c:if test="${not empty configuracao.exModelo}">${configuracao.exModelo.nmMod}</c:if></td>
					<td><c:if test="${not empty configuracao.exClassificacao}">${configuracao.exClassificacao.descrClassificacao}</c:if></td>
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
					
			<%--		<ww:url id="url" action="excluir"
						namespace="/expediente/configuracao">
						<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
					</ww:url> <ww:a href="%{url}">Excluir</ww:a>
			 --%>	
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
	










<%--
<ww:if test="${(not empty mobilVO.movs)}">
    <h2>Anexos Assinados</h2> 	
	<div class="gt-content-box gt-for-table">
	    <ww:form name="frm_anexo" id="frm_anexo" cssClass="form"
		         theme="simple">
			<ww:hidden name="popup" value="true" />
			<ww:hidden name="copia" id="copia" value="false" />			
			<table class="gt-table mov">
			    <thead>
				    <tr>
							<td></td>
							<th align="center" rowspan="2">&nbsp;&nbsp;&nbsp;Data</th>
							<th colspan="2" align="center">Cadastrante</th>
							<th colspan="2" align="center">Atendente</th>
							<th rowspan="2">Descrição</th>
						</tr>
						<tr>
						    <ww:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;EXT:Extensão')}">
								<td align="center"><input type="checkbox" name="checkall"
									onclick="checkUncheckAll(this)" /></td>
							</ww:if>
							<ww:else><td></td></ww:else>	
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
						</tr>
					<thead>
     				<c:set var="i" value="${0}" />
					<c:forEach var="mov" items="${mobilVO.movs}">
						<c:if test="${(not mov.cancelada)}">
							<tr class="${mov.classe} ${mov.disabled}">
								<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />
								<ww:if test="${dt == dtUlt}">
									<c:set var="dt" value="" />
								</ww:if>
								<ww:else>
									<c:set var="dtUlt" value="${dt}" />
								</ww:else>
								<ww:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;EXT:Extensão')}">
									<c:set var="x" scope="request">chk_${mov.mov.idMov}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<td align="center"><input type="checkbox" name="${x}"
										value="true" ${x_checked} /></td>
								</ww:if>
								<ww:else><td></td></ww:else>		
								<td align="center">${dt}</td>
								<td align="left"><siga:selecionado
										sigla="${mov.parte.lotaCadastrante.sigla}"
										descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}" />
								</td>
								<td align="left"><siga:selecionado
										sigla="${mov.parte.cadastrante.nomeAbreviado}"
										descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}" />
								</td>
								<td align="left"><siga:selecionado
										sigla="${mov.parte.lotaResp.sigla}"
										descricao="${mov.parte.lotaResp.descricaoAmpliada}" /></td>
								<td align="left"><siga:selecionado
										sigla="${mov.parte.resp.nomeAbreviado}"
										descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" />
								</td>
								<td>${mov.descricao}<c:if test='${mov.idTpMov != 2}'> ${mov.complemento}</c:if>
									<c:set var="assinadopor" value="${true}" /> <siga:links
										inline="${true}"
										separator="${not empty mov.descricao and mov.descricao != null}">
									<c:forEach var="acao" items="${mov.acoes}">
										<c:choose>
											<c:when test='${mov.idTpMov == 32}'>
												<ww:url id="url" value="${acao.nameSpace}/${acao.acao}">
													<c:forEach var="p" items="${acao.params}">
														<ww:param name="${p.key}">${p.value}</ww:param>
													</c:forEach>
												</ww:url>
											</c:when>
											<c:otherwise>
												<ww:url id="url" action="${acao.acao}"
													namespace="${acao.nameSpace}">
													<c:forEach var="p" items="${acao.params}">
														<ww:param name="${p.key}">${p.value}</ww:param>
													</c:forEach>
												</ww:url>
											</c:otherwise>
										</c:choose>
										<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
											pos="${acao.pos}" url="${url}" test="${true}"
											popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
											ajax="${acao.ajax}" idAjax="${mov.idMov}" />
										<c:if test='${assinadopor and mov.idTpMov == 2}'> ${mov.complemento}
											<c:set var="assinadopor" value="${false}" />
										</c:if>
									</c:forEach>
									<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;EXT:Extensão')}">
										<ww:hidden name="pdf${x}" value="${mov.mov.referencia}" />
										<ww:hidden name="url${x}" value="${mov.mov.nmPdf}" />
									</c:if>	
								</siga:links></td>
							</tr>
						</c:if>
     				</c:forEach>
			</table>	
		</ww:form>
    </div>					
    <c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;EXT:Extensão')}">
	     <c:set var="jspServer"
	            value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_mov_gravar.action" />
		 <c:set var="nextURL"
	            value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/doc/atualizar_marcas.action?sigla=${mobilVO.sigla}" />
		 <c:set var="urlPath" value="/${request.contextPath}" />						
		 <c:set var="botao" value="ambos" />
		 <c:set var="lote" value="true" />			
		  ${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}										
	</c:if>	    
</ww:if>
<ww:else>
		<b>Não há anexos assinados</b>
</ww:else>			

 --%>
