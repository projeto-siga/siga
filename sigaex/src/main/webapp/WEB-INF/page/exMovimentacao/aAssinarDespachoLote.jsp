<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Assinatura de Despacho em Lote" onLoad="javascript: TestarAssinaturaDigital()" incluirJs="/sigaex/javascript/assinatura.js" compatibilidade="IE=EmulateIE9">
<script type="text/javascript" language="Javascript1.1">
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
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<div class="container-fluid">
	<form  name="frm" id="frm" cssClass="form" theme="simple">
		<input type="hidden" name="postback" value="1" />
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Assinatura de Despacho em Lote</h5>
			</div>
			<div class="card-body">
				<div id="dados-assinatura" style="visible: hidden">
				    <c:set var="jspServer" value="${pageContext.request.contextPath}/app/expediente/mov/assinar_mov_gravar" />
				    <c:set var="jspServerSenha" value="${pageContext.request.contextPath}/app/expediente/mov/assinar_mov_login_senha_gravar" />

		   	 	    <c:set var="nextURL" value="/siga/app/principal"  />
		    	    <c:set var="urlPath" value="${pageContext.request.contextPath}" />

					<input type="hidden" id="jspserver" name="jspserver" value="${jspServer}" />
					<input type="hidden" id="jspServerSenha" name="jspServerSenha" value="${jspServerSenha}" />
					<input type="hidden" id="nexturl" name="nextUrl" value="${nextURL}" />
					<input type="hidden" id="urlpath" name="urlpath" value="${urlPath}" />
					<c:set var="urlBase" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}" />
					<input type="hidden" id="urlbase" name="urlbase" value="${urlBase}" />

					<c:set var="botao" value=""/>
				    <c:set var="lote" value="true"/>
				</div>
				<div class="row">
					<div class="col-sm">
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
							<div class="form-group form-inline">
							<c:import url="/javascript/inc_assina_js.jsp" />
								<div id="capicom-div">
									<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="btn btn-primary">Assinar em Lote</a>
								</div>
							<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> é permitida.</p>
							<p id="capicom-missing" style="display: none;">Não foi possível localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o método padrão do SIGA-DOC, será necessário instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://drive.google.com/file/d/0B_WTuFAmL6ZERGhIczRBS0ZMaVE/view"><u>aqui</u></a>. Será necessário expandir o <i>ZIP</i> e depois executar o arquivo de instalação.</p>
							<script type="text/javascript">
								 if (window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) {
									 document.getElementById("capicom-div").style.display = "block";
									 document.getElementById("ie-missing").style.display = "none";
								} else {
									 document.getElementById("capicom-div").style.display = "none";
									 document.getElementById("ie-missing").style.display = "block";
								}
							 </script>
							</div>
						</c:if>
						<div class="form-group form-inline">
							<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
						   		${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,pageContext.request.scheme,pageContext.request.serverName,pageContext.request.serverPort,urlPath,jspServer,nextURL,botao,lote)}
				         	</c:if>
			
				         	<c:if test="${(not empty movimentacoesQuePodemSerAssinadasComSenha)}">
				         		<a id="bot-assinar-senha" href="#" onclick="javascript: assinarComSenha();" class="btn btn-primary ml-3">Assinar com Senha</a>
				         	</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${(not empty itens)}">
		    <h5>Despachos pendentes de assinatura: Como Subscritor</h5>
			<div>
		    <table class="table table-hover table-striped">
				<thead class="${thead_color} align-middle text-center">
				    <tr>
				        <th width="3%"></th>
				        <th width="3%"></th>
				        <th width="17%" align="left">Número</th>
				        <th  width="5%"></th>
				        <th width="15%" colspan="2" align="left">Cadastrante</th>
				        <th width="15%"></th>	 <th width="49%"></th>
				    </tr>
				    <tr>
				    	<th width="3%"></th>
				        <th width="3%" align="right"><input type="checkbox" name="checkall"
				    					onclick="checkUncheckAll(this)" /></th>
				     	<th width="13%"></th>
				        <th width="7%" align="left">Data</th>
				        <th width="8%" align="left">Lotação</th>
				        <th width="7%" align="left">Pessoa</th>
				        <th width="18%" align="left">Tipo</th>
				        <th width="40%" align="left">Descrição</th>
				    </tr>
				</thead>
				<tbody class="table-bordered">
			    <c:forEach var="mov" items="${itens}">
			       <%--  <c:set var="x" scope="request">chk_${mov.exMobil.id}</c:set> --%>
			        <c:set var="x" scope="request">chk_${mov.idMov}</c:set>
				    <c:remove var="x_checked" scope="request" />
				    <c:if test="${param[x] == 'true'}">
				       <c:set var="x_checked" scope="request">checked</c:set>
				    </c:if>
				    <c:set var="podeAssinarComSenha" value="${f:podeAssinarMovimentacaoComSenha(titular,lotaTitular,mov)}"/>
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
				        <td width="3%"align="center"><input type="checkbox" name="${x}"
				           value="true" ${x_checked} class="${classAssinarComSenha}" /></td>
     			        <td width="17%"align="left">
				            <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mov.exMobil.sigla}">${mov.exMobil.codigo}</a>
			            </td>
			            <td width="7%" align="left">${mov.dtRegMovDDMMYY}</td>
			            <td width="8%" align="left">${mov.lotaCadastrante.sigla}</td>
			            <td width="7%" align="left">${mov.cadastrante.sigla}</td>
			            <td width="18%" align="left">${mov.exTipoMovimentacao.sigla}</td>
			            <td width="40%"align="left">${mov.obs}</td>
			        </tr>
					<input type="hidden" name="pdf${x}" value="${mov.referencia}" />
					<input type="hidden" name="url${x}" value="/app/arquivo/exibir?arquivo=${mov.nmPdf}" />
				</c:forEach>
				</tbody>
			 </table>
	    	</div>
		</c:if>
	</form>
	<c:if test="${(not empty movimentacoesQuePodemSerAssinadasComSenha)}">
		<div id="dialog-form" title="Assinar com Senha">
			<form id="form-assinarSenha" method="post" action="/sigaex/app/expediente/mov/assinar_mov_login_senha_gravar" >
				<input type="hidden" id="id" name="id" value="${mov.idMov}" />
				<input type="hidden" id="tipoAssinaturaMov" name="tipoAssinaturaMov" value="A" />
 				<div class="form-group">
 					<label>Matrícula</label>
 					<input id="nomeUsuarioSubscritor" type="text" name="nomeUsuarioSubscritor" class="form-control" onblur="javascript:converteUsuario(this)"/>
 				</div>
    			<div class="form-group">
 					<label>Senha</label>
 					<input type="password" id="senhaUsuarioSubscritor" name="senhaUsuarioSubscritor"  class="form-control"  autocomplete="off" />
 				</div>
			</form>
		</div>
		<div id="dialog-message" title="Alerta">
			<p id="mensagemAssinaSenha"></p>
		</div>
		<script>
			dialog = $("#dialog-form").dialog({
		      autoOpen: false,
		      height: 360,
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
				height: 230,
				width: 550,
				modal: true,
				buttons: {
				    "Ok": function() {
				      dialogM.dialog( "close" );
				    }
				},
		    });

		    function assinarComSenha() {
		      var n = $("input.nao-pode-assinar-senha:checked").length;

		      if(n > 0) {
		      	$("#mensagemAssinaSenha").html( n + (n === 1 ? " documento selecionado não pode ser assinado somente com senha." : " documentos selecionados não podem ser assinados somente com senha.") + " Selecione somente os documentos que estão marcados com ");
		      	$("#mensagemAssinaSenha").append("<img src=\"/siga/css/famfamfam/icons/keyboard.png\" alt=\"Permite assinatura com senha\" title=\"Permite assinatura com senha\" />" );

		      	dialogM.dialog("open");
		      } else {
		    	    var nPode = $("input.pode-assinar-senha:checked").length;

                    if(nPode == 0) {
				      	$("#mensagemAssinaSenha").html("Nenhum documento selecionado. Selecione somente os documentos que estão marcados com ");
				      	$("#mensagemAssinaSenha").append("<img src=\"/siga/css/famfamfam/icons/keyboard.png\" alt=\"Permite assinatura com senha\" title=\"Permite assinatura com senha\" />" );
				      	dialogM.dialog("open");
                    } else {
       		    	    dialog.dialog("open");
                    }
			  }
		    }
		</script>
	</c:if>
	</div>
</siga:pagina>
