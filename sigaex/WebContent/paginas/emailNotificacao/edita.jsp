<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Cadastro de email">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<ww:url id="url" action="editar" namespace="/expediente/emailNotificacao"></ww:url>

			<script>
				function sbmt() {
					editar_gravar.action = '<ww:property value="%{url}"/>';
					editar_gravar.submit();
				}

				function hideShowSel(combo) {
					<%--					var sel1Span = $('#spanPess' + combo.name.substring(4)).val(); --%>

					var sel1Span = document.getElementById('spanPess'
							+ combo.name.substring(4));  
					var sel2Span = document.getElementById('spanLota'
							+ combo.name.substring(4));
					var sel3Span = document.getElementById('spanText'
							+ combo.name.substring(4));
					if (combo.selectedIndex == 0) {
						sel1Span.style.display = "";
						sel2Span.style.display = "none";
						sel3Span.style.display = "none";
					} else {
						if (combo.selectedIndex == 1) {
						sel1Span.style.display = "none";
						sel2Span.style.display = "";
						sel3Span.style.display = "none";
						} else {
							sel1Span.style.display = "none";
							sel2Span.style.display = "none";
							sel3Span.style.display = "";

						  }
					}
				}			
</script>
			
			
			   
			<h2 class="gt-table-head">Cadastrar email de notificação</h2>
			<div class="gt-content-box gt-for-table">
				<form action="editar_gravar.action">
					<input type="hidden" name="postback" value="1" />
					<ww:hidden name="id" />					
					<table class="gt-form-table" width="100%">
						<tr>
							<td width="30%">Tipo de destinatário da movimentação:</td>							
							<td width="70%"><ww:select theme="simple" name="tipoDest"
									list="listaTipoDest"
									onchange="javascript:hideShowSel(this);" />																					
									 <c:choose>									 
									<c:when test="${tipoDest == 1}">
										<c:set var="destMovStyle" value="" />
										<c:set var="lotaDestMovStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoDest == 2}">
										<c:set var="destMovStyle" value="display:none" />
										<c:set var="lotaDestMovStyle" value="" />
									</c:when>								
								</c:choose>															
								<span id="spanPessDest" style="${destMovStyle}">
								 <siga:selecao propriedade="pess" tema="simple" /> </span> 
								<span id="spanLotaDest" style="${lotaDestMovStyle}"> 
								<siga:selecao propriedade="lota" tema="simple" paramList="${strBuscarFechadas}"/> </span>										
							</td>							
						</tr>
						
						<tr>
							<td width="30%">Tipo de interessado na movimentação:</td>							
							<td width="70%"><ww:select theme="simple" name="tipoEmail"
									list="ListaTipoEmail"
									onchange="javascript:hideShowSel(this);" />																
									 <c:choose>
									<c:when test="${tipoEmail == 1}">
										<c:set var="emailMovStyle" value="" />
										<c:set var="lotaEmailMovStyle" value="display:none" />
										<c:set var="emailStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoEmail == 2}">
										<c:set var="emailMovStyle" value="display:none" />
										<c:set var="lotaEmailMovStyle" value="" />
										<c:set var="emailStyle" value="display:none" />
									</c:when>
					 				<c:otherwise>
										<c:set var="emailMovStyle" value="display:none" />
										<c:set var="lotaEmailMovStyle" value="display:none" />
										<c:set var="emailStyle" value="" />														
									</c:otherwise> 																	
								</c:choose>																									
								<span id="spanPessEmail" style="${emailMovStyle}">
								 <siga:selecao propriedade="pessEmail" tema="simple" /> </span> 
								<span id="spanLotaEmail" style="${lotaEmailMovStyle}"> 
								<siga:selecao propriedade="lotaEmail" tema="simple" paramList="${strBuscarFechadas}"/> </span>
								<span id="spanTextEmail" style="${emailStyle}">
								 <ww:textfield name="email" size="40" maxLength="40" theme="simple" /> </span> 										
							</td>							
						</tr>
						<tr><td></td><td></td></tr>

<%--					<tr>
							<td>Substituto:</td>

							<td><ww:select theme="simple" name="tipoSubstituto"
									list="listaTipoDestMov"
									onchange="javascript:hideShowSel(this);" /> <c:choose>
									<c:when test="${tipoSubstituto == 1}">
										<c:set var="substitutoStyle" value="" />
										<c:set var="lotaSubstitutoStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoSubstituto == 2}">
										<c:set var="substitutoStyle" value="display:none" />
										<c:set var="lotaSubstitutoStyle" value="" />
									</c:when>
								</c:choose> <span id="spanSubstituto" style="${substitutoStyle}"> <siga:selecao
										propriedade="substituto" tema="simple" /> </span> <span
								id="spanLotaSubstituto" style="${lotaSubstitutoStyle}"> <siga:selecao
										propriedade="lotaSubstituto" tema="simple" /> </span>
							</td>
						</tr>

						<tr>
							<td>Data de Início</td>
							<td><ww:textfield name="dtIniSubst" label="Data de Início"
									onblur="javascript:verifica_data(this, true);" theme="simple" />
								(opcional)</td>
						</tr>

						<tr>
							<td>Data de Fim</td>
							<td><ww:textfield name="dtFimSubst" label="Data de Fim"
									onblur="javascript:verifica_data(this, true);" theme="simple" />
								(opcional)</td>
						</tr>

						<!-- Incluido para retorno de mensagem de campo nao preenchido -->
						<c:if test="${ empty dataFim }">
							<tr>
								<td align="right"><b><span id="atencao">
								</b></span></td>
								<td><span id="dataFim"></span></td>
							</tr>
						</c:if>

						<c:set var="atencao" value="" />
						<c:set var="dataFim" value="" />

						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok"
								class="gt-btn-medium gt-btn-left" /> <input type="button"
								value="Cancela" onclick="javascript:history.back();"
								class="gt-btn-medium gt-btn-left" />
 						</tr>
  --%>							
					
					</table>				
				</form>
			</div>		
		</div>
	</div>
</siga:pagina>