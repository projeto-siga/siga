<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de ocorrência de feriado">
	<!-- main content -->
	<script type="text/javascript">
	function validar() {
		var dataIni = $('#dtIniFeriado').val();		
		if (dataIni==null || dataIni=="") {			
			alert("Preencha a data de início");
			document.getElementById('dtIniFeriado').focus();		
		}else
			frm.submit();					
	}
	function listaLocalidades(){	
		var uf = $('[name=idUF] option:selected').text();
		
		if(uf != null && uf != '[Todas]') {							
			$.ajax({				     				  
				  url:'/siga/app/feriado/localidades?nmUF=' + uf,					    					   					 
				  success: function(data) {
			    	$('#localidades').html(data);				    
			 	 }
			});
		}else 
			if(uf == '[Todas]')
				($('#localidades').html('Todas'));
			else 
				($('#localidades').html(''));		
	}		
	</script>
<body>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<form name="frm" method="post" action="/siga/app/feriado/gravar-ocorrencia">
				<input type="hidden" name="postback" value="1" />		
				<input type="hidden" name="idOcorrencia" value="${idOcorrencia}" />
				<input type="hidden" name="id" value="${id}" />   
				<c:set var="aplicFer" value="${listaAplicacoes}" />
				<h2 class="gt-table-head">Cadastrar ocorrência de feriado</h2>
					<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2"><b>${dscFeriado}</h1></td>
						</tr>
						<tr>
							<td width="20%">Data de Início</td>
							<td width="80%" align="left">
								<input type="text" name="dtIniFeriado" id="dtIniFeriado"  title="Data de Início"	onblur="javascript:verifica_data(this, true);" value="${dtIniFeriado}"/>
							</td>
						</tr>
						<tr>
							<td width="20%">Data de Fim</td>
							<td width="80%" align="left">
								<input name="dtFimFeriado" title="Data de Fim" onblur="javascript:verifica_data(this, true);" value="${dtFimFeriado}"/></td>	
						</tr>
						<tr>		
							<td>Órgão:</td>
							<td>
								<select name="idOrgaoUsu">
									<option value="0">[Todos]</option>
									<c:forEach var="item" items="${orgaosUsu}">
										<option value="${item.idOrgaoUsu}">${item.nmOrgaoUsu}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>Lotação:</td>
							<td>
								<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" />
							</td>
						</tr>	
						<tr>
							<td>UF:</td>
							<td>
								<select name="idUF" onchange="javascript:listaLocalidades()">
									<option value="0">[Todas]</option>
									<c:forEach var="item" items="${listaUF}">
										<option value="${item.idUF}">${item.nmUF}</option>
									</c:forEach>
								</select>
							</td>
						</tr>	
						<tr>
							<td>Localidade:</td>
							<td>
								<div style="display: inline" id="localidades">Todas</div>
							</td>
						</tr>								
						<tr class="button">
							<td colspan="2">
								<input type="button" value="Ok" onclick="javascript:validar();" class="gt-btn-large gt-btn-left" />
								<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/feriado/listar';" class="gt-btn-medium gt-btn-left" />
							</td>				
						</tr>
					</table>
				</div>
			</form>
		<br />	
		
		<c:if test="${(not empty aplicFer)}">
			<h2 class="gt-table-head">Local onde a ocorrência se aplica</h2>
				<div class="gt-content-box gt-for-table">
					<table border="0" class="gt-table">
						<thead>
							<tr>							
								<th>Órgao</th>	
								<th>Lotação</th>
								<th>Localidade</th>		
								<th>UF</th>								
							</tr>
						</thead>
						<tbody>
							<c:forEach var="apl" items="${aplicFer}">
								<input type="text" name="idAplicacao" />  
								<tr>
									<td>${apl.orgaoUsu.nmOrgaoUsu}</td>	
									<td>${apl.dpLotacao.siglaLotacao}</td>	
									<td>${apl.localidade.nmLocalidade}</td>	
									<td>${apl.localidade.UF.descricao}</td>	
									<td align="center" width="10%">									
	 			 						<a href="javascript:if (confirm('Deseja excluir?')) location.href='/siga/app/feriado/excluir-aplicacao?idAplicacao=${apl.id}&idOcorrencia=${idOcorrencia}';">
											<img style="display: inline;"
												src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir feriado"							
												onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
												onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
										</a>															
									</td>
								</tr>	
							</c:forEach>
						</tbody>
					</table>
				</div>			
			</c:if>			
		</div>
	</div>
</body>
</siga:pagina>