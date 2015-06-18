<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Assinatura em Lote"
						 onLoad="javascript: TestarAssinaturaDigital()"
						 incluirJs="sigaex/javascript/assinatura.js">

	<script type="text/javascript" language="Javascript1.1">
		/*  converte para maiÃºscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			re = /^[a-zA-Z]{2}\d{3,6}$/;
			ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
			tmp = nomeusuario.value;
			if (tmp.match(re) || tmp.match(ret2)) {
				nomeusuario.value = tmp.toUpperCase();
			}
		}
	</script>

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">

	function checkUncheckAll(theElement) {
		var theForm = theElement.form, z = 0;
		for(z=0; z<theForm.length;z++) {
	    	if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall') {
				theForm[z].checked = !(theElement.checked);
				theForm[z].click();
			}
		}
	}

	function displaySel(chk, el) {
		document.getElementById('div_' + el).style.display=chk.checked ? '' : 'none';
		if (chk.checked == -2)
			document.getElementById(el).focus();
	}

	function displayTxt(sel, el) {
		document.getElementById('div_' + el).style.display=sel.value == -1 ? '' : 'none';
		document.getElementById(el).focus();
	}


</script>

	<div  class="gt-bd clearfix">
		<div class="gt-content clearfix">
        <h2>Assinatura em Lote</h2>
        <ww:form name="frm" id="frm" cssClass="form" theme="simple">
		<ww:token />
		<ww:hidden name="postback" value="1" />
		<div class="gt-content-box gt-for-table">
		<table class="gt-form-table">
			<tr class="header">
				<td>Assinatura</td>
			</tr>
			<tr class="button">
			<td>
				<div id="dados-assinatura" style="visible: hidden">
				    <c:set var="jspServer" value="${request.contextPath}/expediente/mov/assinar_gravar.action" />
				    <c:set var="jspServerSenha" value="${request.contextPath}/expediente/mov/assinar_senha_gravar.action" />
		   	 	    <c:set var="nextURL" value="/siga/principal.action"  />
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

					<c:set var="botao" value=""/>
				    <c:set var="lote" value="true"/>
				</div>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de GestÃ£o Administrativa;DOC:MÃ³dulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
					<c:import url="/paginas/expediente/inc_assina_js.jsp" />
						<div id="capicom-div">
							<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar em Lote</a>
						</div>
					<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrÃ£o do SIGA-DOC sÃ³ poderÃ¡ ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> Ã© permitida.</p>
					<p id="capicom-missing" style="display: none;">NÃ£o foi possÃ­vel localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o mÃ©todo padrÃ£o do SIGA-DOC, serÃ¡ necessÃ¡rio instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://code.google.com/p/projeto-siga/downloads/detail?name=Capicom.zip&can=2&q=#makechanges"><u>aqui</u></a>. SerÃ¡ necessÃ¡rio expandir o <i>ZIP</i> e depois executar o arquivo de instalaÃ§Ã£o.</p>
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

				<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de GestÃ£o Administrativa;DOC:MÃ³dulo de Documentos;ASS:Assinatura digital;EXT:ExtensÃ£o')}">
			   		${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.serverPort,urlPath,jspServer,nextURL,botao,lote)}
	         	</c:if>
	         	<c:if test="${(not empty documentosQuePodemSerAssinadosComSenha)}">
	         		<a id="bot-assinar-senha" href="#" onclick="javascript: assinarComSenha();" class="gt-btn-large gt-btn-left">Assinar com Senha</a>
	         	</c:if>
	        </td>
			</tr>
		</table>
		</div>
		<br />
		<c:if test="${(not empty itensSolicitados)}">
		    <h2>Documentos pendentes de assinatura: Como Subscritor</h2>
			<div  class="gt-content-box gt-for-table">
		    <table class="gt-table">
			    <tr>
			        <th width="3%"></th>
			        <th width="3%"></th>
			        <th width="13%" align="left">NÃºmero</th>
			        <th  width="5%"></th>
			        <th width="15%" colspan="2" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cadastrante</th>
			        <th width="15%"></th>	 <th width="49%"></th>
			    </tr>
			    <tr>
			        <th width="3%"></th>
			        <th width="3%" align="right"><input type="checkbox" name="checkall"
			    					onclick="checkUncheckAll(this)" /></th>
			     	<th width="13%"></th>
			        <th width="5%" align="center">Data</th>
			        <th width="10%" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Lotacao</th>
			        <th width="5%" align="left">Pessoa</th>
			        <th width="15%" align="left">Tipo</th>
			        <th width="49%" align="left">DescriÃ§Ã£o</th>
			    </tr>
			    <c:forEach var="doc" items="${itensSolicitados}">
			        <c:set var="x" scope="request">chk_${doc.idDoc}</c:set>
				    <c:remove var="x_checked" scope="request" />
				    <c:if test="${param[x] == 'true'}">
				       <c:set var="x_checked" scope="request">checked</c:set>
				    </c:if>
				   <c:set var="podeAssinarComSenha" value="${f:podeAssinarComSenha(titular,lotaTitular,doc.mobilGeral)}"/>
                   <c:set var="classAssinarComSenha" value="nao-pode-assinar-senha"/>
		           <c:if test="${podeAssinarComSenha}">
		              <c:set var="classAssinarComSenha" value="pode-assinar-senha"/>
		           </c:if>
			        <tr class="even">
				        <td width="3%"align="center">
        		           <c:if test="${podeAssinarComSenha}">
								<img src="/siga/css/famfamfam/icons/keyboard.png" alt="Permite assinatura com senha" title="Permite assinatura com senha" />
						   </c:if>
				        </td>
				        <td width="3%"align="center">
				           <input type="checkbox" name="${x}"
				           value="true" ${x_checked} class="${classAssinarComSenha}" />
				        </td>
     			        <td width="13%"align="left">
	    		            <ww:url id="url" action="exibir" namespace="/expediente/doc">
		    		            <ww:param name="sigla">${doc.sigla}</ww:param>
			    		    </ww:url>
				            <ww:a href="%{url}">${doc.codigo}</ww:a>
			            </td>
			            <td width="5%" align="center">${doc.dtDocDDMMYY}</td>
			            <td width="10%" align="center">${doc.lotaCadastrante.siglaLotacao}</td>
			            <td width="5%" align="left">${doc.cadastrante.sigla}</td>
			            <td width="15%" align="left">${doc.descrFormaDoc}</td>
			            <td width="49%"align="left">${doc.descrDocumento}</td>
			        </tr>
			        <ww:hidden name="pdf${x}" value="${doc.sigla}" />
				    <ww:hidden name="url${x}" value="/arquivo/exibir.action?arquivo=${doc.codigoCompacto}.pdf"/>
			    </c:forEach>
			 </table>
	         </div>
	      </c:if>
	  </ww:form>
	</div></div>

	<c:if test="${(not empty documentosQuePodemSerAssinadosComSenha)}">
		<div id="dialog-form" title="Assinar com Senha">
 			<form id="form-assinarSenha" method="post" action="/sigaex/expediente/mov/assinar_mov_login_senha_gravar.action" >
 				<ww:hidden id="id" name="id" value="${mov.idMov}" />
 				<ww:hidden id="tipoAssinaturaMov" name="tipoAssinaturaMov" value="A" />
    			<fieldset>
    			  <label>MatrÃ­cula</label> <br/>
    			  <input id="nomeUsuarioSubscritor" type="text" name="nomeUsuarioSubscritor" class="text ui-widget-content ui-corner-all" onblur="javascript:converteUsuario(this)"/><br/><br/>
    			  <label>Senha</label> <br/>
    			  <input type="password" id="senhaUsuarioSubscritor" name="senhaUsuarioSubscritor"  class="text ui-widget-content ui-corner-all"  autocomplete="off" />
    			</fieldset>
  			</form>
		</div>

		<div id="dialog-message" title="Basic dialog">
 				<p id="mensagemAssinaSenha"></p>
		</div>

		 <script>
		    dialog = $("#dialog-form").dialog({
		      autoOpen: false,
		      height: 210,
		      width: 350,
		      modal: true,
		      buttons: {
                  "Assinar": assinarGravar,
		          "Cancelar": function() {
		            dialog.dialog( "close" );
		          }
		      },
		      close: function() {

		      }
		    });

		    function assinarGravar() {
		    	AssinarDocumentosSenha('false', this);
			}

		    dialogM = $("#dialog-message").dialog({
		        autoOpen: false,
		    });

		    function assinarComSenha() {
		      var n = $("input.nao-pode-assinar-senha:checked").length;

		      if(n > 0) {
		      	$("#mensagemAssinaSenha").html( n + (n === 1 ? " documento selecionado nÃ£o pode ser assinado somente com senha." : " documentos selecionados nÃ£o podem ser assinados somente com senha.") + " Selecione somente os documentos que estÃ£o marcados com ");
		      	$("#mensagemAssinaSenha").append("<img src=\"/siga/css/famfamfam/icons/keyboard.png\" alt=\"Permite assinatura com senha\" title=\"Permite assinatura com senha\" />" );

		      	dialogM.dialog("open");
		      } else {
		    	    var nPode = $("input.pode-assinar-senha:checked").length;

                    if(nPode == 0) {
				      	$("#mensagemAssinaSenha").html("Nenhum documento selecionado. Selecione somente os documentos que estÃ£o marcados com ");
				      	$("#mensagemAssinaSenha").append("<img src=\"/siga/css/famfamfam/icons/keyboard.png\" alt=\"Permite assinatura com senha\" title=\"Permite assinatura com senha\" />" );
				      	dialogM.dialog("open");
                    } else {
       		    	    dialog.dialog("open");
                    }
			  }
		    }
		  </script>
	</c:if>
</siga:pagina>
