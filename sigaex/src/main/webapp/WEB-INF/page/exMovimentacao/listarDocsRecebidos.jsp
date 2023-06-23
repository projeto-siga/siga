<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de Transferência" popup="true">

	<c:set var="thead_color" value="${thead_color}" scope="session" />

	<div class="container-fluid">
		<form name="frm">
			<input type="hidden" name="isTransf" value="true" />

			<!-- main content bootstrap -->
			<c class="container-fluid">
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>
						<fmt:message key='protocolo.recebimento' />
					</h5>
				</div>

				<div class="card-body">
					<table class="table table-hover table-striped">
						<thead class="${thead_color} align-middle text-center">
							<tr>
								<td>De</td>
								<td>${lotaTitular.descricao}- ${cadastrante.descricao}</td>
								<input type="hidden" name="sigla" id="pessoa"
									value="${cadastrante.sigla}" />
							</tr>
							<td>Para</td>
							<td>${movIni.respString}</td>
							</tr>
							<tr>
								<td>Data</td>
								<td colspan="2">${movIni.dtRegMovDDMMYYYYHHMMSS}</td>
								<input type="hidden" name="dtIni" value="${dtIni}" />
								<input type="hidden" name="dtFim" value="${dtFim}" />
							</tr>
					</table>

				</div>
			</div>

			<c:if test="${not empty movsDocumentosNaoRecebidos}">
				<div class="card bg-light mb-3">
					<div class="card-header">
						<h5>
							<fmt:message key='documentos.nao.recebidos' />
						</h5>
					</div>

					<div class="card-body">
						<table class="table table-hover table-striped">
							<col width="20%" />
							<col width="10%" />
							<col width="30%" />
							<col width="40%" />
							<thead class="${thead_color} align-middle text-center">
								<tr>
									<th>Documento</th>
									<th>Lotação</th>
									<th>Descrição</th>
									<th>Mensagem</th>
								</tr>
							</thead>
							<c:forEach var="mov" items="${movsDocumentosNaoRecebidos}">
								<tr>
									<td align="center"><a
										href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mov.exMobil.sigla}">
											${mov.exMobil.codigo} </a>
										<td align="center"><siga:selecionado
												sigla="${mov.exMobil.exDocumento.lotaSubscritor.sigla}"
												descricao="${mov.exMobil.exDocumento.lotaSubscritor.descricao}" />
									</td>
										<td align="center"><siga:selecionado
												sigla="${mov.exMobil}" descricao="${mov.exMobil}" /></td>
										<td class="text-left" style="color: red;">${mov.descrMov}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</c:if> <c:if test="${not empty mobisDocumentosRecebidos}">
				<div class="card bg-light mb-3">
					<div class="card-header">
						<h5>
							<fmt:message key='documentos.recebidos' />
						</h5>
					</div>

					<div class="card-body">
						<table class="table table-hover table-striped">
							<col width="22%" />
							<col width="5%" />
							<col width="4%" />
							<col width="4%" />
							<col width="5%" />
							<col width="4%" />
							<col width="4%" />
							<col width="4%" />
							<col width="4%" />
							<col width="44%" />
							<thead class="${thead_color} align-middle text-center">
								<tr>
									<th rowspan="2" class="text-right">Número</th>
									<th colspan="3">Documento</th>
									<th colspan="3">Última Movimentação</th>
									<th colspan="2"><fmt:message
											key="tela.tramitarLote.tipoResponsavel" /></th>
									<th rowspan="2" class="text-left">Descrição</th>
								</tr>
								<tr>
									<th>Data</th>
									<th>Lotação</th>
									<th>Pessoa</th>
									<th>Data</th>
									<th>Lotação</th>
									<th>Pessoa</th>
									<th>Lotação</th>
									<th>Pessoa</th>
								</tr>
							</thead>
							<c:forEach var="mob" items="${mobisDocumentosRecebidos}">
								<tr>
									<td class="text-right"><a
										href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mob.sigla}">
											${mob.codigo} </a> <c:if test="${not mob.geral}">
											<td>${mob.doc.dtDocDDMMYY}</td>
											<td><siga:selecionado
													sigla="${mob.doc.lotaSubscritor.sigla}"
													descricao="${mob.doc.lotaSubscritor.descricao}" /></td>
											<td><siga:selecionado
													sigla="${mob.doc.subscritor.iniciais}"
													descricao="${mob.doc.subscritor.descricao}" /></td>
											<td>${movIni.dtMovDDMMYY}</td>
											<td><siga:selecionado
													sigla="${movIni.lotaSubscritor.sigla}"
													descricao="${movIni.lotaSubscritor.descricao}" /></td>
											<td><siga:selecionado
													sigla="${movIni.subscritor.iniciais}"
													descricao="${movIni.subscritor.descricao}" /></td>
											<td><siga:selecionado sigla="${movIni.lotaResp.sigla}"
													descricao="${movIni.lotaResp.descricao}" /></td>
											<td><siga:selecionado sigla="${movIni.resp.iniciais}"
													descricao="${movIni.resp.descricao}" /></td>
										</c:if> <c:if test="${mob.geral}">
											<td>${mob.doc.dtDocDDMMYY}</td>
											<td><siga:selecionado sigla="${mob.subscritor.iniciais}"
													descricao="${mob.subscritor.descricao}" /></td>
											<td><siga:selecionado
													sigla="${mob.lotaSubscritor.sigla}"
													descricao="${mob.lotaSubscritor.descricao}" /></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</c:if>
										<td class="text-left"><c:choose>
												<c:when test="${siga_cliente == 'GOVSP'}">
	                                            ${mob.doc.descrDocumento}
	                                        </c:when>
												<c:otherwise>
	                                            ${f:descricaoConfidencial(mob, lotaTitular)}
	                                        </c:otherwise>
											</c:choose></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>

			</c:if> <a href="/sigaex/app/expediente/mov/receber_lote"
				class="btn btn-cancel btn-primary">Voltar</a>
		</form>
	</div>
	<br />
</siga:pagina>