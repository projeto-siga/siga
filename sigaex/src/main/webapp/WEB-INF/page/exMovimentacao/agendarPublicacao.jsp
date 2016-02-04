<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>
	
	<script type="text/javascript">
		function prever_data() {
			var dtPublDiv = document.getElementById('dt_publ');
			ReplaceInnerHTMLFromAjaxResponse('${request.contextPath}/app/expediente/mov/prever_data?data=' + document.getElementById('dt_dispon').value, null, dtPublDiv);
		}
		
		function contaLetras() {
			var i = 256 - tamanho();
			$("#Qtd").html('Restam ' + i + ' caracteres');
		}
	
		function tamanho() {
			nota= new String();
			nota = this.frm.descrPublicacao.value;
			return nota.length;		
		}
	
		function validar() {
			var data = document.getElementsByName('dtDispon')[0].value;
			var i = tamanho();
			if (data==null || data=="") {			
				alert("Preencha a data para disponibilização.");
				document.getElementById('dt_dispon').focus();		
			}else {
				if (i>256) {
					alert('Descrição com mais de 256 caracteres');
					document.getElementById('descrPublicacao').focus();	
				}else {
					if (i<=0) {
						alert('Descrição deve ser preenchida');
						document.getElementById('descrPublicacao').focus();	
					}else	
						frm.submit();
				}	
			}
		}
		
		function buscaNomeLota(){
			var siglaLota = $('#idLotPublicacao :selected').text();	
				$.ajax({				     				  
					  url:'/siga/lotacao/selecionar.action?sigla=' + siglaLota ,					    					   					 
					  success: function(data) {
						 var parts = data.split(';');					   
				    	$('#nomeLota').html(parts[3]);				    
				 	 }
				});			
		}
	</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2>Agendamento de Publicação - ${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">
				<form name="frm" action="${request.contextPath}/app/expediente/mov/agendar_publicacao_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					
					<c:choose>
						<c:when test="${not empty mensagem}">
							<c:set var="disabled" value="disabled" />				
						</c:when>
							
						<c:otherwise>
							<c:set var="disabled" value="" />
						</c:otherwise>
					</c:choose>
					
					<table class="gt-form-table">
						<colgroup>
							<col  style="width:30%"/>
							<col  style="width:70%"/>
						</colgroup>
						
						<tr class="header">
							<td colspan="2">Dados do Agendamento</td>
						</tr>
						<c:choose>
							<c:when test="${cadernoDJEObrigatorio}">
								<c:set var="disabledTpMat">true</c:set> 
								<input type="hidden" name="tipoMateria" value="${tipoMateria}" />
								<tr>
									<td>Tipo de Matéria:</td>
									<td>
										<c:choose>
											<c:when test="${tipoMateria eq 'A'}">
												Administrativa 
											</c:when>
											<c:otherwise>
												Judicial
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>
										<label>Tipo de Matéria:</label>
									</td>
									<td>
										<input type="radio" name="tipoMateria" value="J"/>Judicial
										<input type="radio" name="tipoMateria" value="A"/>Administrativa
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
							<td>Próxima data para disponibilização:</td>
							<td>${proximaDataDisponivelStr}</td>
						</tr>
						
						<tr>
							<td>
								<label>Data para disponibilização:</label>
							</td>
							<td>
								<input type="text" name="dtDispon" id="dt_dispon" onblur="javascript:verifica_data(this,true);prever_data();"/>
							</td>
						</tr>
						
						<tr>
							<td>Data de publicação:</td>
							<td>
								<div id="dt_publ" />
							</td>
						</tr>				
						<tr>									
							<td>Lotação de Publicação:</td>
							
							<c:choose>
								<c:when test="${podeAtenderPedidoPubl}">
									<td>
										<siga:selecao tema="simple" propriedade="lotaSubscritor" modulo="siga" />
									</td>
								</c:when>
									
								<c:otherwise>
									<td>
										<select id="idLotPublicacao" name="idLotPublicacao" onchange="javascript:buscaNomeLota();">
											<c:forEach items="${listaLotPubl}" var="item">
												<option value="${item.idLotacao}">${item.siglaLotacao}</option>
											</c:forEach>
										</select>
										&nbsp;&nbsp;&nbsp;&nbsp;<span id="nomeLota"></span>
									</td>							
								</c:otherwise>
							</c:choose>
						</tr>	
						
						<tr>
							<td>Descrição do documento:</td>
							<td>
								<textarea name="descrPublicacao" cols="80" id="descrPublicacao" rows="5" class="gt-form-textarea" onkeyup="contaLetras();">${descrPublicacao}</textarea>	
							</td>
						</tr>	
						
						<tr>
							<td></td>
							<td>
								<div id="Qtd">Restam&nbsp;${tamMaxDescr}&nbsp;caracteres</div>
							</td>
						</tr>						
						<tr>
							<td colspan="2">
								<input type="button" value="Ok" onclick="javascript: validar();" class="gt-btn-medium gt-btn-left" ${disabled}/>
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" />
								<a href="${request.contextPath}/app/arquivo/download?arquivo=${mob.referenciaRTF}" class="gt-btn-large gt-btn-left">Visualizar Publicação</a>
							</td>
						</tr>
					</table>
				</form>	
				<span style="font-weight:bold; color: red">${mensagem}</span>			
			</div>
			
			<br/>
			<span style="margin-left: 0.5cm;color: red;"><b>Atenção:</b></span>
			<ul>
				<li><span style="font-weight:bold">Data para
				Disponibilização</span> - data em que a matéria efetivamente aparece no
				site</li>
				<li><span style="font-weight:bold">Data de Publicação</span> -
				a Data de Disponibilização + 1, conforme prevê art. 4º, parágrafo 3º
				da Lei 11419 / 2006</li>
				<li><span style="font-weight:bold">Visualizar Publicação</span> -
				Permite visualizar o documento antes de ser enviado para o DJE.</li>
			</ul>
		</div>
	</div>
	
	<script type="text/javascript">
		buscaNomeLota();
		$(":radio[value=${tipoMateria}]").attr('checked', true);
		$("[name=idLotPublicacao]").val('${idLotDefault}');
		
		<c:if test="${disabledTpMat}">
			$(":radio").attr('disabled', true);
		</c:if>
	</script>
</siga:pagina>
