<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Movimentação">

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
	
		
    <c:if test="${!assinandoAnexosGeral}">
	<table width="100%">
		<tr>
			<td><ww:form name="frm" id="frm" action="anexar_gravar" namespace="/expediente/mov"
					method="POST" enctype="multipart/form-data" cssClass="form">
           
                    <input type="hidden" name="postback" value="1" />
	                <ww:hidden name="sigla" value="%{sigla}" />

					
					<h1>
						Anexação de Arquivo - ${mobilVO.sigla}
						
					</h1>

					<tr class="header">
						<td colspan="2">Dados do Arquivo</td>
					</tr>					
					<ww:textfield name="dtMovString" label="Data"
				    	onblur="javascript:verifica_data(this, true);" />
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

		<ww:textfield name="descrMov" label="Descrição" maxlength="80" size="80" />

		<ww:file name="arquivo" label="Arquivo" accept="application/pdf"
			onchange="testpdf(this.form)" />
	<%--		<ww:checkbox theme="simple" name="copiaOriginal"/>Cópia de original  --%>
				
		<ww:submit value="Ok" cssClass="button" align="center" />	
		</ww:form>			
		</td>
		</tr>
		
	
	</table>
    </c:if>	
	
	
	<ww:form name="frm_anexo" id="frm_anexo" cssClass="form" theme="simple">
		
	
	<ww:hidden name="idMob" value="${mobilVO.mob.id}" />
	<ww:hidden name="popup" value="true" />
	<ww:hidden name="copia" id = "copia" value="false" />	
		
	<c:if test="${(not empty mobilVO.movs)}">
		
		<table border="0" cellpadding="0" cellspacing="0" width="80%" >
			<tr>  
			   <ww:if test="${!assinandoAnexosGeral}">
				    <h1 colspan="4" > Anexos Pendentes de Assinatura </h1>
			   </ww:if>
			   <ww:else>	    
				    <h1 colspan="4" > Anexos Pendentes de Assinatura - ${docVO.sigla}</h1>				     
		       </ww:else>				
			</tr> 
		</table>
		<table class="mov" width="80%">		
			<tr class="${docVO.classe}">
				<td></td>			    
				<td align="center" rowspan="2">Data</td>			
				<td colspan="2" align="left">Cadastrante</td>			
				<td colspan="2" align="left">Atendente</td>
				<td rowspan="2">Descrição</td>			
			</tr>
			<tr class="${docVO.classe}">			   
				<td align="center"><input type="checkbox"
									name="checkall" onclick="checkUncheckAll(this)" /></td>
				<td align="left">Lotação</td>
				<td align="left">Pessoa</td>			
				<td align="left">Lotação</td>
				<td align="left">Pessoa</td>
			</tr>
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
						<c:set var="x" scope="request">chk_${mov.mov.idMov}</c:set>
			    		<c:remove var="x_checked" scope="request" />
						<c:if test="${param[x] == 'true'}">
							<c:set var="x_checked" scope="request">checked</c:set>
						</c:if>						
		                <td align="center"><input type="checkbox"
									name="${x}" value="true" ${x_checked} /></td>		          
						<td align="center">${dt}</td>				
						<td align="left"><siga:selecionado
							sigla="${mov.parte.lotaCadastrante.sigla}"
							descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}" /></td>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.cadastrante.nomeAbreviado}"
							descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}" /></td>				
						<td align="left"><siga:selecionado
							sigla="${mov.parte.lotaResp.sigla}"
							descricao="${mov.parte.lotaResp.descricaoAmpliada}" /></td>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.resp.nomeAbreviado}"
							descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" /></td>
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
							<ww:hidden name="pdf${x}" value="${mov.mov.nmPdf}" />							
						</siga:links></td>			
					</tr>										
				</c:if>			
				
			</c:forEach>	
		</table>	
		<c:set var="jspServer" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_mov_gravar.action"/>
	    <c:set var="nextURL" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/doc/atualizar_marcas.action?sigla=${mobilVO.sigla}" />
		<table border="0" cellpadding="0" cellspacing="0" width="80%" >
			<tr><td colspan="4" align="right"> 
			  ${f:obterExtensaoAssinadorLote(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,request.contextPath,mobilVO.sigla,doc.codigoCompacto,jspServer,nextURL)}
			</td></tr> 
		</table>	
	</c:if>	
	   
	<ww:url id="url" action="exibir" namespace="/expediente/doc">
		<ww:param name="sigla" value="%{sigla}" />
	</ww:url>	
	
	<siga:link title="Voltar" url="${url}" test="${true}" />	

	</ww:form>
	
	
	
	
 
</siga:pagina>