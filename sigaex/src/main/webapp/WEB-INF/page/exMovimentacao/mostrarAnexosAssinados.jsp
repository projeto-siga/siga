<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${(not empty mobilVO.movs)}">
	    <br/>
	    <h2>Anexos Assinados</h2>
		<div class="gt-content-box gt-for-table">
		    <form name="frm_anexo" id="frm_anexo" class="form">
				<input type="hidden" name="popup" value="true" />
				<input type="hidden" name="copia" id="copia" value="false" />
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
								<c:choose>
								    <c:when test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
										<td align="center"><input type="checkbox" name="checkall"
											onclick="checkUncheckAll(this)" /></td>
									</c:when>
									<c:otherwise><td></td></c:otherwise>
								</c:choose>
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
									<c:choose>
										<c:when test="${dt == dtUlt}">
											<c:set var="dt" value="" />
										</c:when>
										<c:otherwise>
											<c:set var="dtUlt" value="${dt}" />
										</c:otherwise>
									</c:choose>
									<c:set var="x" scope="request">ad_chk_${mov.mov.idMov}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<td align="center"><input type="checkbox" name="${x}"
										value="true" ${x_checked} /></td>
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
									<td>${mov.descricao}<c:if test='${mov.exTipoMovimentacao != ANEXACAO}'> ${mov.complemento}</c:if>
										<c:set var="assinadopor" value="${true}" /> <siga:links
											inline="${true}"
											separator="${not empty mov.descricao and mov.descricao != null}">
										<c:forEach var="acao" items="${mov.acoes}">
											<c:choose>
												<c:when test='${mov.exTipoMovimentacao == AGENDAMENTO_DE_PUBLICACAO}'>
													<c:url var="url" value="${acao.nameSpace}/${acao.acao}">
														<c:forEach var="p" items="${acao.params}">
															<c:param name="${p.key}" value="${p.value}"/>
														</c:forEach>
													</c:url>
												</c:when>
												<c:otherwise>
													<c:url var="url" value="${acao.nameSpace}/${acao.acao}">
														<c:forEach var="p" items="${acao.params}">
															<c:param name="${p.key}" value="${p.value}"/>
														</c:forEach>
													</c:url>
												</c:otherwise>
											</c:choose>
											<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
												pos="${acao.pos}" url="${url}" test="${true}"
												popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
												ajax="${acao.ajax}" idAjax="${mov.idMov}" />
											<c:if test='${assinadopor and mov.exTipoAnexacao == ANEXACAO}'> ${mov.complemento}
												<c:set var="assinadopor" value="${false}" />
											</c:if>
										</c:forEach>
										<input type="hidden" name="ad_descr_${mov.idMov}" value="${mov.mov.referencia}" /> 
										<input type="hidden" name="ad_url_pdf_${mov.idMov}" value="/sigaex/app/arquivo/exibir?arquivo=${mov.mov.nmPdf}" />
										<input type="hidden" name="ad_url_post_${mov.idMov}" value="/sigaex/app/expediente/mov/assinar_mov_gravar" />
										<input type="hidden" name="ad_url_post_password_${mov.idMov}" value="/sigaex/app/expediente/mov/assinar_mov_login_senha_gravar" />
										<input type="hidden" name="ad_id_${mov.idMov}" value="${fn:replace(mov.mov.referencia, ':', '_')}" />
										<input type="hidden" name="ad_description_${mov.idMov}" value="${mov.mov.obs}" />
										<input type="hidden" name="ad_kind_${mov.idMov}" value="${mov.mov.exTipoMovimentacao.sigla}" />
									</siga:links></td>
								</tr>
							</c:if>
	     				</c:forEach>
				</table>				
			</form>			
	    </div>	    

	</c:when>
	<c:otherwise>
			<b>Não há anexos assinados</b>
	</c:otherwise>	
</c:choose>
<br/>


