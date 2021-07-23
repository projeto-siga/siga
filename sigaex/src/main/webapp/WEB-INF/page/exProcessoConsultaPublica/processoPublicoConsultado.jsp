<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<c:set var="titulo">
	<fmt:message key="tela.consultaprocessopublico.tramitacao.titulo" />
</c:set>

<siga:pagina titulo="Movimentação" desabilitarmenu="sim"
	onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};">
	<div class="container-fluid">
		<div class="row">
			<div class="col col-12">
				<div class="card bg-light mb-3">
					<div class="card-header">
						<h5>

							<b>${titulo} - ${sigla}</b> <a
								href="${request.contextPath}/public/app/arquivoConsultado_stream?jwt=${jwt}&sigla=${sigla}"
								id="linkDocPdf" target="_blank"> <img
								src="/siga/css/famfamfam/icons/page_white_acrobat.png"></a>



						</h5>
					</div>

					<!-- 
						<div class="card-body">
							<div>
								<input type="hidden" id="visualizador" value="${f:resource('/sigaex.pdf.visualizador') }"/>
								<c:url var='pdf'
									value='/public/app/arquivoConsultado_stream?jwt=${jwt}&sigla=${sigla}' />
								<iframe id="frameDoc" width="100%" height="400"
									align="center" style="margin-top: 10px;"> </iframe>
							</div>
						</div>
					  -->

				</div>
			</div>
			<!-- 
			<div class="col">
				<div class="row">
					<div class="col">
						<div class="card bg-light mb-3" >
							<div class="card-header">
								<h5>
									<i class="fa fa-file-pdf"></i> Arquivos para Download
								</h5>
							</div>
							<div class="card-body">
								<i class="fa fa-angle-double-right"></i> <a href="${request.contextPath}/public/app/arquivoConsultado_stream?jwt=${jwt}" id="linkDoc" target="_blank">PDF do documento</a>
							</div>
						</div>
					</div>
				</div>
				 
			</div>
			-->

		</div>


		<div class="row">
			<div class="col-12">
				<c:if test="${not empty docVO}">
					<div class="card bg-light mb-3">
						<div class="card-header">
							<h5>Últimas Movimentações</h5>
						</div>
						<div class="card-body">

							<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
								<li style="margin-top: 10px; margin-bottom: 0px;">
									${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}
									<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}">
									- ${m.tamanhoDeArquivo}
							 	</c:if>
								</li>
								<c:set var="ocultarCodigo" value="${true}" />
								<c:set var="dtUlt" value="" />
								<c:set var="temmov" value="${false}" />
								<c:forEach var="mov" items="${movs}">
									<c:if
										test="${ (exibirCompleto == true) or (mov.idTpMov != 14 and not mov.cancelada)}">
										<c:set var="temmov" value="${true}" />
									</c:if>
								</c:forEach>
								<div class="gt-content-box gt-for-table"
									style="margin-bottom: 25px;">
									<table class="table table-striped">
										<thead class="bg-dark text-white">
											<tr>
												<th align="left" rowspan="2">Data</th>
												<th rowspan="2">Evento</th>
												<th colspan="2" align="left">Cadastrante</th>
												<c:if test="${ (exibirCompleto == 'true')}">
													<th colspan="2" align="left">Responsável</th>
												</c:if>
												<th colspan="2" align="left">Atendente</th>
											</tr>
											<tr>
												<th colspan="2" align="left">Lotação</th>
												<c:if test="${ (exibirCompleto == 'true')}">
													<th colspan="2" align="left">Lotação</th>
												</c:if>
												<th colspan="2" align="left">Lotação</th>
											</tr>
										</thead>
										<c:set var="evenorodd" value="odd" />
										<c:forEach var="mov" items="${movs}">
											<tr>
												<c:set var="dt" value="${mov.dtRegMovDDMMYYYYHHMMSS}" />
												<c:choose>
													<c:when test="${dt == dtUlt}">
														<c:set var="dt" value="" />
													</c:when>
													<c:otherwise>
														<c:set var="dtUlt" value="${dt}" />
													</c:otherwise>
												</c:choose>
												<td align="left">${dt}</td>
												<td align="left">${mov.descrTipoMovimentacao}<c:if
														test="${mov.idTpMov == 2 or  mov.idTpMov ==64}">
														<span style="font-size: .8rem; color: #9e9e9e;">|
															${mov.descrMov} ${mov.nmArqMov} </span>
													</c:if> <c:if test="${mov.idTpMov == 12}">

														<c:choose>
															<c:when test="${mov.exDocumento.sigla == sigla}">
																<c:set var="movSigla" value="${mov.exMobilRef}" />
															</c:when>
															<c:otherwise>
																<c:set var="movSigla" value="${mov.exMobil}" />
															</c:otherwise>
														</c:choose>

														<span style="font-size: .8rem; color: #9e9e9e;">|
															documento juntado ${movSigla} </span>

														<c:if
															test="${mov.exMobil.exDocumento.exNivelAcesso.grauNivelAcesso == 10}">
															<a
																href="${request.contextPath}/public/app/arquivoConsultado_stream?jwt=${jwt}&sigla=${movSigla}"
																target="_blank"> <img
																src="/siga/css/famfamfam/icons/page_white_acrobat.png" />
															</a>
														</c:if>
													</c:if>


												</td>
												<td colspan="2" align="left">${mov.lotaCadastrante.nomeLotacao}(${mov.lotaCadastrante.sigla})</td>
												<td colspan="2" align="left">${mov.lotaResp.nomeLotacao}(${mov.lotaResp.sigla})</td>
											</tr>
											<c:choose>
												<c:when test='${evenorodd == "even"}'>
													<c:set var="evenorodd" value="odd" />
												</c:when>
												<c:otherwise>
													<c:set var="evenorodd" value="even" />
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</table>
								</div>
							</c:forEach>
						</div>
				</c:if>
			</div>
			
			<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-primary">
			
		</div>
	</div>
	<tags:assinatura_rodape />
	<script>
		window.onload = function() {
			document.getElementById('frameDoc').src = montarUrlDocPDF(
					'${pdfAssinado }',
					document.getElementById('visualizador').value);
			document.getElementById('linkDoc').href = montarUrlDocPDF('${pdf}',
					document.getElementById('visualizador').value);
		}
	</script>
</siga:pagina>
