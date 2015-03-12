<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de email de notificação">
	<script>
		function hideShowSel(combo) {
			var sel1Span = $('#spanPess' + combo.name.substring(4)); 
			var sel2Span = $('#spanLota' + combo.name.substring(4)); 
			var sel3Span = $('#spanText' + combo.name.substring(4));	
	
			switch (combo.selectedIndex) {
				case 0:	if(combo.name == $('#tipoDest').attr('name')){	
							sel1Span.show();
							sel2Span.hide();
							sel3Span.hide();																
							} else {	
								sel1Span.hide();
								sel2Span.hide();
								sel3Span.hide();									
							}
							break;
				case 1: if(combo.name == $('#tipoDest').attr('name')){
							sel1Span.hide();
							sel2Span.show();
							sel3Span.hide();					
						} else {
							sel1Span.show();
							sel2Span.hide();
							sel3Span.hide();
						}						
						break;
				case 2: sel1Span.hide();
						sel2Span.show();
						sel3Span.hide();
						break;
				case 3: sel1Span.hide();
						sel2Span.hide();
						sel3Span.show();
						break;
				default:sel1Span.hide();
						sel2Span.hide();
						sel3Span.hide();			
			}					
		}	
	
		function validar() {
			if ($('#pessSel_id').val() == "" && $('#lotaSel_id').val() == "")
				alert("Preencha o destinatário.");
			else
				frm.submit();
		}	
			
	</script>	
	<body>
		<!-- main content -->
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">				   
				<h2 class="gt-table-head">Cadastrar email de notificação</h2>
				<div class="gt-content-box gt-for-table">
					<form name="frm" action="editar_gravar">
						<input type="hidden" name="postback" value="1" />
						<input type="hidden" name="id" />					
						<table class="gt-form-table" width="100%">
							<tr>
								<td width="25%">Tipo de destinatário da movimentação:</td>							
								<td width="75%">
									<select name="tipoDest"  id="tipoDest" 
										onchange="javascript:hideShowSel(this);" >
										<c:forEach items="#{listaTipoDest}" var="item">
											<option value="${item.key}" ${item.key == tipoDest ? 'selected' : ''}>
												${item.value}
											</option> 
										</c:forEach>
									</select>
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
									 <siga:selecao propriedade="pess" tema="simple" modulo="siga"/> </span> 
									<span id="spanLotaDest" style="${lotaDestMovStyle}"> 
									<siga:selecao propriedade="lota" tema="simple" paramList="${strBuscarFechadas}" modulo="siga"/> </span>										
								</td>							
							</tr>
							
							<tr>
								<td width="25%">Tipo de interessado na movimentação:</td>							
								<td width="75%">
									<select name="tipoEmail" id="tipoEmail" 
										onchange="javascript:hideShowSel(this);" >
										<c:forEach items="${listaTipoEmail}" var="item">
											<option value="${item.key}" ${item.key == tipoEmail ? 'selected' : ''}>
												${item.value}
											</option> 
										</c:forEach>
									</select>
									<c:choose>
										<c:when test="${tipoEmail == 2}">
											<c:set var="emailMovStyle" value="" />
											<c:set var="lotaEmailMovStyle" value="display:none" />
											<c:set var="emailStyle" value="display:none" />
										</c:when>
										<c:when test="${tipoEmail == 3}">
											<c:set var="emailMovStyle" value="display:none" />
											<c:set var="lotaEmailMovStyle" value="" />
											<c:set var="emailStyle" value="display:none" />
										</c:when>
										<c:when test="${tipoEmail == 4}">
											<c:set var="emailMovStyle" value="display:none" />
											<c:set var="lotaEmailMovStyle" value="display:none" />
											<c:set var="emailStyle" value="" />						
										</c:when>									
						 				<c:otherwise>
											<c:set var="emailMovStyle" value="display:none" />
											<c:set var="lotaEmailMovStyle" value="display:none" />
											<c:set var="emailStyle" value="display:none" />														
										</c:otherwise> 																	
									</c:choose>																									
									<span id="spanPessEmail" style="${emailMovStyle}">
									 <siga:selecao propriedade="pessEmail" tema="simple" modulo="siga"/> </span> 
									<span id="spanLotaEmail" style="${lotaEmailMovStyle}"> 
									<siga:selecao propriedade="lotaEmail" tema="simple" paramList="${strBuscarFechadas}" modulo="siga"/> </span>
									<span id="spanTextEmail" style="${emailStyle}">
									 <input type="text" name="emailTela" size="40" maxLength="40" /> </span> 										
								</td>							
							</tr>
							<tr><td></td><td></td></tr>
							
							<tr class="button">
								<td colspan="2"><input type="button" value="Ok" onclick="javascript: validar();"
									class="gt-btn-medium gt-btn-left" /> <input type="button"
									value="Cancela" onclick="javascript:history.back();"
									class="gt-btn-medium gt-btn-left" />
	 						</tr>					
						</table>				
					</form>
				</div>		
			</div>
		</div>
	</body>	
</siga:pagina>