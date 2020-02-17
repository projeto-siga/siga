<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Movimentação" desabilitarmenu="sim"
	onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};">
	<div class="container-fluid">
		<div class="row">
			<div class="col col-12 col-sm-8">
				<div class="card bg-light mb-3" >
					<div class="card-header">
						<h5>
							Acompanhamento e Autenticação de Protocolo
						</h5>
					</div>
					<div class="card-body">
						<div>
							<c:url var='pdfAssinado'
								value='/public/app/processoArquivoAutenticado_stream?jwt=${jwt}&assinado=true' />
							<c:url var='pdf'
								value='/public/app/processoArquivoAutenticado_stream?jwt=${jwt}&assinado=false' />
							<iframe src="${pdfAssinado}" width="100%" height="600" align="center" style="margin-top: 10px;"> </iframe>
						</div>
					</div>
				</div>
			</div>
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
								<i class="fa fa-angle-double-right"></i> <a href="${pdf}" target="_blank">PDF do documento</a>
							</div>
						</div>
					</div>
				</div>				
			</div>
		</div>
		<div class="row">
			<div class="col-12">
				<c:if test="${not empty docVO}">
					<div class="card bg-light mb-3" >
						<div class="card-header">
							<h5>
								Últimas Movimentações
							</h5>
						</div>	
						<div class="card-body">
						
						<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
							<c:if test="${f:resource('isWorkflowEnabled')}">
								<script type="text/javascript">
									var url = "/sigawf/app/doc?sigla=${m.sigla}&ts=1${currentTimeMillis}";
							        $.ajax({
							        	url: url,
							            type: "GET"
							        }).fail(function(jqXHR, textStatus, errorThrown){
							            var div = $(".wf_div${m.mob.codigoCompacto}:last");
										$(div).html(errorThrown);
							        }).done(function(data, textStatus, jqXHR ){
							            var div = $(".wf_div${m.mob.codigoCompacto}:last");
										$(div).html(data);
							        });
								</script>
							</c:if>
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
								<c:if test="${ (exibirCompleto == true) or (mov.idTpMov != 14 and not mov.cancelada)}">
									<c:set var="temmov" value="${true}" />
								</c:if>
							</c:forEach>
							<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
								<table class="table table-striped">
									<thead class="bg-dark text-white">
										<tr>
											<th align="center" rowspan="2">Data</th>
											<th rowspan="2">Evento</th>
											<th colspan="2" align="left">Cadastrante</th>
											<c:if test="${ (exibirCompleto == 'true')}">
												<th colspan="2" align="left">Responsável</th>
											</c:if>
											<th colspan="2" align="left">Atendente</th>
										</tr>
										<tr>
											<th align="left">Lotação</th>
											<th align="left">Pessoa</th>
											<c:if test="${ (exibirCompleto == 'true')}">
												<th align="left">Lotação</th>
												<th align="left">Pessoa</th>
											</c:if>
											<th align="left">Lotação</th>
											<th align="left">Pessoa</th>
										</tr>
									</thead>
									<c:set var="evenorodd" value="odd" />
									<c:forEach var="mov" items="${movs}">
										<tr>
											<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />
											<c:choose>
												<c:when test="${dt == dtUlt}">
													<c:set var="dt" value="" />
												</c:when>
												<c:otherwise>
													<c:set var="dtUlt" value="${dt}" />
												</c:otherwise>
											</c:choose>
											<td align="center">${dt}</td>
											<td>${mov.descrTipoMovimentacao}</td>
											<td align="left"><siga:selecionado isVraptor="true"
													sigla="${mov.lotaCadastrante.sigla}"
													descricao="${mov.lotaCadastrante.descricaoAmpliada}"
													lotacaoParam="${mov.lotaCadastrante.siglaLotacao}${mov.lotaCadastrante.sigla}" />
											</td>
											<td align="left"><siga:selecionado isVraptor="true"
													sigla="${mov.cadastrante.nomeAbreviado}"
													descricao="${mov.cadastrante.descricao} - ${mov.cadastrante.sigla}"
													pessoaParam="${mov.cadastrante.sigla}" />
											</td>
											<td align="left"><siga:selecionado isVraptor="true"
													sigla="${mov.lotaResp.sigla}"
													descricao="${mov.lotaResp.descricaoAmpliada}"
													lotacaoParam="${mov.lotaResp.siglaLotacao}${mov.lotaResp.sigla}" />
											</td>
											<td align="left"><siga:selecionado isVraptor="true"
													sigla="${mov.resp.nomeAbreviado}"
													descricao="${mov.resp.descricao} - ${mov.resp.sigla}"
													pessoaParam="${mov.resp.sigla}" />
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
									</c:forEach>
								</table>
							</div>							
						</c:forEach>
					</div>
				</c:if>
			</div>		
		</div>
	</div>
	<tags:assinatura_rodape />
</siga:pagina>
