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
							Acompanhamento e Autenticação de Protocolo - Documento <b>${sigla}</b> 
						</h5>
					</div>
					<!--
					<div class="card-body">
						<div>
							<c:url var='pdfAssinado'
								value='/public/app/processoArquivoAutenticado_stream?jwt=${jwt}&assinado=true' />
							<c:url var='pdf'
								value='/public/app/processoArquivoAutenticado_stream?jwt=${jwt}&assinado=false' />
							<iframe src="${pdfAssinado}" width="100%" height="600" align="center" style="margin-top: 10px;"> </iframe>
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
								<i class="fa fa-angle-double-right"></i> <a href="${pdf}" target="_blank">PDF do documento</a>
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
					<div class="card bg-light mb-3" >
						<div class="card-header">
							<h5>
								Últimas Movimentações
							</h5>
						</div>	
						<div class="card-body">
						
						<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
							<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')}">
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
											<th align="left" rowspan="2">Data</th>
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
										<tr class="${mov.cancelada ? 'disabled' : ''}">
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
											<td align="left">${mov.descrTipoMovimentacao} 
												<c:if test="${mov.idTpMov == 12}">
													<span style="font-size: .8rem;color: #9e9e9e;">| documento juntado&nbsp
														<c:choose>
															<c:when test="${m.sigla ne mov.exMobil.sigla}">
																${mov.exMobil}
																<c:if test="${(((not isProtocoloFilho) 
																			or (mov.exMobil.doc.descricaoEspecieDespacho)) 
																		and (mov.exMobil.exibirNoAcompanhamento))}">
																	&nbsp<a class="showConteudoDoc link-btn btn btn-sm btn-light" href="#" 
																		onclick="popitup('/sigaex/public/app/processoArquivoAutenticado_stream?sigla=${mov.exMobil}');"
																		rel="popover" data-title="${mov.exMobil}" 
				    													data-content="" onmouseenter="exibeDoc(this);"
																		>Ver</a>
			    												</c:if>
															</c:when>
															<c:otherwise>
																<a class="link-btn btn btn-sm btn-light"  
																	href="/sigaex/public/app/processoArquivoAutenticado?idMovJuntada=${mov.idMov}"
																	>${mov.exMobilRef.sigla}</a>
															</c:otherwise>
														</c:choose>
													</span>
												</c:if>
												<c:if test="${mov.idTpMov == 13}">
													<span style="font-size: .8rem;color: #9e9e9e;"
														>| documento desentranhado ${mov.exMobil}
													</span>
												</c:if>
											</td>
											<td align="left">${mov.lotaCadastrante.sigla} </td>
											<td align="left">${mov.cadastrante.nomeAbreviado}</td>
											<td align="left">${mov.lotaResp.sigla}</td>
											<td align="left">${mov.resp.nomeAbreviado}</td>
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
<script type="text/javascript">
	function exibeDoc(elem) {
		var timeOut;
		$(elem).popover({
			trigger: "hover",
			placement: "auto",
			html : true
		});

		timeOut = setTimeout(setDataContent, 700, elem);
	}
	
	function setDataContent(elem) {
	   	if ($(elem).attr('data-content') === "" 
				&& ($("." + $(elem).context.className.split(' ')[0] + ":hover")).length > 0) {
			sigladoc = $(elem).attr("data-title").replace("/", "").replace("-", "");
			var div_id = "tmp-id-" + sigladoc;
			$(elem).attr("data-content", '<div id="'+ div_id +'" class="spinner-border"></div>');
			$(elem).popover('show');
			conteudodoc = exibirBodyDoc(div_id, sigladoc, elem);
		}
	}
	
	function exibirBodyDoc(div_id, sigla, elem) {
		$.ajax({
		    url: "/sigaex/api/v1/documentos/"+ sigla + "/html",
		    contentType: "application/json",
		    headers: {"Authorization": "${jwt}"},
		    dataType: 'json',
		    success: function(result) {
			    conteudoDoc = new DOMParser().parseFromString(result.html, "text/html");
				strhtml = conteudoDoc.getElementsByTagName("BODY")[0].outerHTML
					.replace(/(\r\n|\n|\r)/gm, "")
					.replace(/<p[^>]*>/gi,'PARAGRAFOQUEBRA');
				conteudo = new DOMParser().parseFromString(strhtml, "text/html") 
        			.documentElement.textContent
					.replace(/PARAGRAFOQUEBRA?/g,'<p>').substring(0, 500)
					+ "... <br /><br /><i>Clique no botão para ver o documento completo.</i>";
				conteudo = conteudo.split('<p>').slice(0,16).join('<p>');
				$('#'+div_id).removeClass("spinner-border");
 				$('#'+div_id).html(conteudo);
				$(elem).attr("data-content", conteudo);
				$(elem).attr("data-container", "body");
		    },				
		    error: function (response, status, error) {
	        	msgErro = "Ocorreu um erro na solicitação: " + response.responseJSON.errormsg;
				$('#'+div_id).removeClass("spinner-border");
				$('#'+div_id).html(msgErro);
				$(elem).attr("data-content", "");
	    	}
	    });
	}
</script>
<style type="text/css">
    .popover{
        max-width:600px;
    }
    .popover p{
		margin-bottom: 5px; /* between paragraphs */
    }
</style>