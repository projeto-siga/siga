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
			sigaModal.alerta("Preencha a data de início");
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
	<div class="container-fluid">
		<div class="card bg-light mb-3" >		
			<form name="frm" method="post" action="/siga/app/feriado/gravar-ocorrencia">
				<input type="hidden" name="postback" value="1" />		
				<input type="hidden" name="idOcorrencia" value="${idOcorrencia}" />
				<input type="hidden" name="id" value="${id}" />   
				<c:set var="aplicFer" value="${listaAplicacoes}" />
				<div class="card-header"><h5>Cadastrar ocorrência de feriado</h5></div>
					<div class="card-body">
						<div class="row">
							<div class="col-sm">
								<div class="form-group"><b>${dscFeriado}</b></div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2">
								<div class="form-group">
									<label>Data de Início</label>
									<input type="text" name="dtIniFeriado" id="dtIniFeriado" title="Data de Início" onblur="javascript:verifica_data(this, true);" value="${dtIniFeriado}" class="form-control"/>
								</div>
							</div>
							<div class="col-sm-2">
								<div class="form-group">
									<label>Data de Fim</label>
									<input name="dtFimFeriado" title="Data de Fim" onblur="javascript:verifica_data(this, true);" value="${dtFimFeriado}" class="form-control" />	
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-4">
								<div class="form-group">
									<label>Órgão</label>
									<select name="idOrgaoUsu" class="form-control" >
										<option value="0">[Todos]</option>
										<c:forEach var="item" items="${orgaosUsu}">
											<option value="${item.idOrgaoUsu}">${item.nmOrgaoUsu}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">		
									<label>Lotação:</label>
									<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2">
								<div class="form-group">
									<label>UF</label>
									<select class="form-control" name="idUF" onchange="javascript:listaLocalidades()">
										<option value="0">[Todas]</option>
										<c:forEach var="item" items="${listaUF}">
											<option value="${item.idUF}">${item.nmUF}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">		
									<label>Localidade</label>
									<div style="display: inline" id="localidades">Todas</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm">
								<div class="form-group">	
									<input type="button" value="Ok" onclick="javascript:validar();" class="btn btn-primary" />
									<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/feriado/listar';" class="btn btn-primary" />
								</div>
							</div>
						</div>
					</div>
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