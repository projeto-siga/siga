<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Movimentação">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

	<script type="text/javascript" language="Javascript1.1">
		var frm = document.getElementById('frm');
		function sbmt() {
			ExMovimentacaoForm.page.value = '';
			ExMovimentacaoForm.acao.value = 'aAnexar';
			ExMovimentacaoForm.submit();
		}

		function testpdf(x) {
			padrao = /\.pdf/;
			a = x.arquivo.value;
			OK = padrao.exec(a);
			if (a != '' && !OK) {
				window.alert("Somente é permitido anexar arquivo PDF!");
				x.arquivo.value = '';
				x.arquivo.focus();
			}
		}
		
		function checkUncheckAll(theElement) {
			var theForm = theElement.form, z = 0;		
			for(z=0; z<theForm.length;z++) {
		    	if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall') {
					theForm[z].checked = !(theElement.checked);
					theForm[z].click();
				}
			}
		}

		function montaTableAssinados(carregaDiv){	
			if(carregaDiv == true) {
				$('#tableAssinados').html('Carregando...');			
				$.ajax({				     				  
					  url:'/sigaex/expediente/mov/mostrar_anexos_assinados.action?sigla=${mobilVO.sigla}',					    					   					 
					  success: function(data) {
				    	$('#tableAssinados').html(data);				    
				 	 }
				});
			}	
			else ($('#tableAssinados').html(''));		
		}		

		/**
		 * Valida se o anexo foi selecionado ao clicar em OK
		 */
		function validaSelecaoAnexo( form ) {
			var result = true;
			var arquivo = form.arquivo;
			if ( arquivo == null || arquivo.value == '' ) {
				alert("O arquivo a ser anexado não foi selecionado!");
				result = false;
			}
			return result;
		}	

	</script>
	
	<ww:url id="urlExibir" action="exibir" namespace="/expediente/doc">
		<ww:param name="sigla">${mobilVO.sigla}</ww:param>
	</ww:url>
	
	<div class="gt-bd clearfix">
	    <div class="gt-content clearfix">		
	        <c:if test="${!assinandoAnexosGeral}">
				<h2>Anexação de Arquivo - ${mob.siglaEDescricaoCompleta}</h2>
				<div class="gt-content-box gt-for-table">
					<ww:form action="anexar_gravar" namespace="/expediente/mov"
						method="POST" enctype="multipart/form-data" cssClass="form">

						<input type="hidden" name="postback" value="1" />
						<ww:hidden name="sigla" value="%{sigla}" />

						<table class="gt-form-table">
							<tr class="header">
								<td colspan="2">Dados do Arquivo</td>
							</tr>
							<tr>
								<td><ww:textfield name="dtMovString" label="Data"
										onblur="javascript:verifica_data(this, true);" /></td>
							</tr>
							<tr>
								<td>Responsável:</td>
								<td><siga:selecao tema="simple" propriedade="subscritor" />
									&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
										onclick="javascript:displayTitular(this);" />Substituto</td>
							</tr>
							<c:choose>
								<c:when test="${!substituicao}">
									<tr id="tr_titular" style="display: none">
								</c:when>
								<c:otherwise>
									<tr id="tr_titular" style="">
								</c:otherwise>
							</c:choose>

							<td>Titular:</td>
							<input type="hidden" name="campos" value="titularSel.id" />
							<td colspan="3"><siga:selecao propriedade="titular"
									tema="simple" />
							</td>
							</tr>
							<tr>
								<ww:textfield name="descrMov" label="Descrição" maxlength="80"
									          size="80" />
							</tr>
							<tr>
								<ww:file name="arquivo" label="Arquivo" accept="application/pdf"
									     onchange="testpdf(this.form)" />
							</tr>
							<tr>
								<td colspan="2"><input type="submit" value="Ok"
									class="gt-btn-medium gt-btn-left" onclick="javascript: return validaSelecaoAnexo( this.form );" /> 
									<input  type="button" value="Voltar" onclick="javascript:window.location.href='${urlExibir}'"
									        class="gt-btn-medium gt-btn-left" />	
									<br/>        								
									<input type="checkbox"  theme="simple" name="check"
	                                       onclick="javascript:montaTableAssinados(check.checked);" /> <b>Exibir anexos assinados</b>
								</td>
							</tr>
						</table>						
					</ww:form>							
				</div>			
	        </c:if>
		
	        <ww:if test="${(not empty mobilVO.movs)}">	 
	        
		        <c:if test="${assinandoAnexosGeral}">
		           <input style="display: inline"
					    type="checkbox"  theme="simple" name="check" 
	                    onclick="javascript:montaTableAssinados(check.checked);" /><b>Exibir anexos assinados</b>
	                <br/>  
	             </c:if>  		     
				 <h2>Anexos Pendentes de Assinatura
				
				 <ww:if test="${assinandoAnexosGeral}">
					      - ${mob.siglaEDescricaoCompleta}</h2> 
			     </ww:if>	
			     <ww:else></h2></ww:else>
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
							    <ww:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
									<td align="center"><input type="checkbox" name="checkall"
										onclick="checkUncheckAll(this)" /></td>
								</ww:if>
								<ww:else><td></td></ww:else>	
								<th align="left">Lotação</th>
								<th align="left">Pessoa</th>
								<th align="left">Lotação</th>
								<th align="left">Pessoa</th>
							</tr>
						</thead>
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
									<ww:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
									    <c:set var="x" scope="request">chk_${mov.mov.idMov}</c:set>
										<c:remove var="x_checked" scope="request" />
										<c:if test="${param[x] == 'true'}">
												<c:set var="x_checked" scope="request">checked</c:set>
										</c:if>
										<td align="center"><input type="checkbox" name="${x}"
												value="true" ${x_checked} /></td>
									</ww:if>
									<ww:else>
									    <td></td>
									</ww:else>		
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
										    </siga:links>    
										
										<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
										      <ww:hidden name="pdf${x}" value="${mov.mov.referencia}" />
											   <ww:hidden name="url${x}" value="${mov.mov.nmPdf}" />
										</c:if>	
									</td>
							    </tr>
						    </c:if>
					    </c:forEach>
					</table>	
				</ww:form>
		    </div>				
			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
				<c:set var="jspServer"
				       value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_mov_gravar.action" />
				<c:set var="nextURL"
					   value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/doc/atualizar_marcas.action?sigla=${mobilVO.sigla}" />
			    <c:set var="urlPath" value="/${request.contextPath}" />						
			    <c:set var="botao" value="ambos" />
			    <c:set var="lote" value="true" />			
					    ${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}						
			</c:if>
		</div>				   	
	    </ww:if>
	    <ww:else>
		    <c:if test="${assinandoAnexosGeral}">
			    <script language="javascript">
			        montaTableAssinados(true);
				</script>
		    </c:if>
	    </ww:else>    
	    <div class="gt-content clearfix">	  
		<div id="tableAssinados"></div>		
		</div>    	
</div></div>      	
	
		

</siga:pagina>
