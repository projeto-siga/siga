<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<siga:pagina titulo="Movimentação"
						 onLoad="javascript: TestarAssinaturaDigital();"
						 incluirJs="sigaex/javascript/assinatura.js">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>
	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
		<c:import url="/paginas/expediente/inc_assina_js.jsp" />
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

		/*  converte para maiúscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			re = /^[a-zA-Z]{2}\d{3,6}$/;
			ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
			tmp = nomeusuario.value;
			if (tmp.match(re) || tmp.match(ret2)) {
				nomeusuario.value = tmp.toUpperCase();
			}
		}
	</script>

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
								<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
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
									tema="simple" modulo="siga"/>
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

							<c:set var="pendencias" value="${false}"/>
							<c:forEach var="mov" items="${mobilCompletoVO.movs}">
							    <c:if test="${(not mov.cancelada) and (mov.idTpMov eq 57)}">
										<c:set var="pendencias" value="${true}"/>
							    </c:if>
						    </c:forEach>
						    <c:if test="${pendencias}">
								<tr class="header">
									<td colspan="2">Pendencias de Anexação</td>
								</tr>
								<tr>
									<td colspan="2">
										<div class="gt-form">
											<label>A anexação deste arquivo resolve as seguintes pendências:</label>
											<c:forEach var="mov" items="${mobilCompletoVO.movs}">
											    <c:if test="${(not mov.cancelada) and (mov.idTpMov eq 57)}">
														<label class="gt-form-element-label"><input type="checkbox" class="gt-form-checkbox" name="pendencia_de_anexacao" value="${mov.idMov}"> ${mov.descricao}</label>
											    </c:if>
										    </c:forEach>
										</div>
									</td>
								</tr>
						    </c:if>

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
	             <br/>
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
						  					  <ww:hidden name="url${x}" value="/arquivo/exibir.action?arquivo=${mov.mov.nmPdf}"/>
										</c:if>
									</td>
							    </tr>
						    </c:if>
					    </c:forEach>
					</table>
				</ww:form>
		    </div>
		    <br/>
			<div id="dados-assinatura" style="visible: hidden">
				<c:set var="jspServer"
				       value="${request.contextPath}/expediente/mov/assinar_mov_gravar.action" />
			    <c:set var="jspServerSenha"
			    	   value="${request.contextPath}/expediente/mov/assinar_mov_login_senha_gravar.action" />
				<c:set var="nextURL"
					   value="${request.contextPath}/expediente/doc/atualizar_marcas.action?sigla=${mobilVO.sigla}" />
			    <c:set var="urlPath" value="${request.contextPath}" />

	    		<ww:hidden id="jspserver" name="jspserver" value="${jspServer}" />
				<ww:hidden id="jspServerSenha" name="jspServerSenha" value="${jspServerSenha}" />
				<ww:hidden id="nexturl" name="nextUrl" value="${nextURL}" />
				<ww:hidden id="urlpath" name="urlpath" value="${urlPath}" />
				<c:set var="req" value="${pageContext.request}" />
				<c:set var="url">${req.requestURL}</c:set>
				<c:set var="uri" value="${req.requestURI}" />
				<c:set var="urlBase" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
				<ww:hidden id="urlbase" name="urlbase" value="${urlBase}" />

			    <c:set var="botao" value="ambos" />
			    <c:set var="lote" value="true" />
			</div>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
					<div id="capicom-div">
						<a id="bot-conferir" href="#" onclick="javascript: AssinarDocumentos('true', this);" class="gt-btn-alternate-large gt-btn-left">Autenticar em Lote</a>
						<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar em Lote</a>
					</div>
				<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> é permitida.</p>
				<p id="capicom-missing" style="display: none;">Não foi possível localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o método padrão do SIGA-DOC, será necessário instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://code.google.com/p/projeto-siga/downloads/detail?name=Capicom.zip&can=2&q=#makechanges">aqui</a>. Será necessário expandir o <i>ZIP</i> e depois executar o arquivo de instalação.</p>
				<script type="text/javascript">
					 if (window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) {
						 document.getElementById("capicom-div").style.display = "block";
						 document.getElementById("ie-missing").style.display = "none";
					} else {
						 document.getElementById("capicom-div").style.display = "none";
						 document.getElementById("ie-missing").style.display = "block";
					}
				 </script>
			</c:if>

			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
			    ${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.serverPort,urlPath,jspServer,nextURL,botao,lote)}
			</c:if>

    	 	 <c:set var="podeAssinarMovimentacaoComSenha" value="${f:podeAssinarMovimentacaoDoMobilComSenha(titular,lotaTitular,mob)}" />
			 <c:set var="podeConferirCopiaMovimentacaoComSenha" value="${f:podeConferirCopiaMovimentacaoDoMobilComSenha(titular,lotaTitular,mob)}" />

			 <c:if test="${podeAssinarMovimentacaoComSenha || podeConferirCopiaMovimentacaoComSenha}">
  				     <a id="bot-assinar-senha" href="#" onclick="javascript: assinarComSenha();" class="gt-btn-large gt-btn-left">Assinar/Conferir com Senha</a>

					<div id="dialog-form" title="Assinar com Senha">
			 			<form id="form-assinarSenha" method="post" action="/sigaex/expediente/mov/assinar_mov_login_senha_gravar.action" >
			 				<ww:hidden id="id" name="id" value="${mov.idMov}" />
			 				<ww:hidden id="tipoAssinaturaMov" name="tipoAssinaturaMov" value="A" />
			    			<fieldset>
			    			  <label>Matrícula</label> <br/>
			    			  <input id="nomeUsuarioSubscritor" type="text" name="nomeUsuarioSubscritor" class="text ui-widget-content ui-corner-all" onblur="javascript:converteUsuario(this)"/><br/><br/>
			    			  <label>Senha</label> <br/>
			    			  <input type="password" id="senhaUsuarioSubscritor" name="senhaUsuarioSubscritor"  class="text ui-widget-content ui-corner-all" autocomplete="off"/>
			    			</fieldset>
			  			</form>
					</div>

	  				 <script>
					    dialog = $("#dialog-form").dialog({
						    autoOpen: false,
				      		height: 210,
				      		width: 350,
				      		modal: true,
				      		buttons: {
				    	  		<c:if test="${podeAssinarMovimentacaoComSenha}">
				          			"Assinar": assinarGravar,
				          		</c:if>
				    	  		<c:if test="${podeConferirCopiaMovimentacaoComSenha}">
					          		"Autenticar": conferirCopiaGravar,
				          		</c:if>
				          			"Cancelar": function() {
				            		dialog.dialog( "close" );
				          	}
			      		},
	    		      close: function() {

				      }
			    	});

					    function assinarComSenha() {
					       dialog.dialog( "open" );
					    }

					    function assinarGravar() {
					    	AssinarDocumentosSenha('false', this);
						}

					    function conferirCopiaGravar() {
					    	AssinarDocumentosSenha('true', this);
						}
				  </script>
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
