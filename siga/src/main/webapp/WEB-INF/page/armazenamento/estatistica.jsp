<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="Estatística de Armazenamento - ${f:resource('/siga.armazenamento.arquivo.tipo')}">	
	<script type="text/javascript">
		$( document ).ready(function() {
			
			var url_string = window.location.href;
			var url = new URL(url_string);
			var scale = url.searchParams.get("scale") != null ? url.searchParams.get("scale") : 'MB';
			$('#selectUnidadeDados').val(scale).change();
			
			$('#ultima-atualizacao').text('Última atualização: '+getLastRefreshTime());
		});
		
		function getLastRefreshTime() {
			var dt = new Date();
			return ("0" + dt.getDate()).slice(-2) + "/" + ("0" + (dt.getMonth() + 1)).slice(-2) + " "
				+ ("0" + dt.getHours()).slice(-2) + ":" + ("0" + dt.getMinutes()).slice(-2) + ":" + ("0" + dt.getSeconds()).slice(-2) ;
		}
		
		function convertBytes(){
			let usedCapacityBytes = $('#usedCapacityBytes').val();
			let totalCapacityBytes = $('#totalCapacityBytes').val();
			let selectorUnidadeDados = $('#selectUnidadeDados').val();
			
			const KILOBYTES = 1024;
			const MEGABYTES = KILOBYTES * 1024;
			const GIGABYTES = MEGABYTES * 1024;
			const TERABYTES = GIGABYTES * 1024;
			
			if (selectorUnidadeDados === 'B') {
				usedCapacityBytes = usedCapacityBytes;
				totalCapacityBytes = totalCapacityBytes;
			} else if (selectorUnidadeDados === 'KB') {
				usedCapacityBytes = usedCapacityBytes / KILOBYTES;
				totalCapacityBytes = totalCapacityBytes  / KILOBYTES;
			} else if (selectorUnidadeDados === 'GB') {
				usedCapacityBytes = usedCapacityBytes / GIGABYTES;
				totalCapacityBytes = totalCapacityBytes  / GIGABYTES;
			} else if (selectorUnidadeDados === 'TB') {
				usedCapacityBytes = usedCapacityBytes / TERABYTES;
				totalCapacityBytes = totalCapacityBytes  / TERABYTES;
			}  else {
				selectorUnidadeDados = 'MB';
				$('#selectUnidadeDados').val(selectorUnidadeDados);
				
				usedCapacityBytes = usedCapacityBytes / MEGABYTES;
				totalCapacityBytes = totalCapacityBytes  / MEGABYTES;
			}
			
			
			$('#spanUsedCapacityBytes').text((usedCapacityBytes).toLocaleString('pt-BR', { maximumFractionDigits: 2 }) + selectorUnidadeDados );
			$('#spanTotalCapacityBytes').text((totalCapacityBytes).toLocaleString('pt-BR', { maximumFractionDigits: 2 }) + selectorUnidadeDados );
			$('#btnRefresh').attr("href", "/siga/app/armazenamento/estatistica?scale=" + selectorUnidadeDados )
			
		}
	
	</script>
	
 	
 	<div class="container-fluid">
		<div class="card mb-3 shadow-lg">
				
			<div class="card-header">
				<div class="row">
					<div class="col-sm-9">
						<h5 class="titulo-principal-etapa" id="tituloPrincipalEtapa"><i class="fa fa-chart-line"></i> Estatística de Armazenamento</h5>
					</div>
				</div>
			</div>
							
			<div class="card-body lead">	

					<div id="estatisticaArmazenamento">
						<div class="row">
							<div class="col-md-12 col-lg-12">
								<div class="py-1 text-center">
						            
									<div class="row text-left">
										<div class="col-md-3 col-lg-3"></div>
										<div class="col-md-6 col-lg-6">
											<small id="ultima-atualizacao" class="my-auto text-muted float-right"></small>
											<br />
											Tecnologia de Armazenamento de Objetos: <strong>${f:resource('/siga.armazenamento.arquivo.tipo')}</strong>
											<hr />
											<h2>${namespaceName}</h2>
											<div class="progress" style="height: 30px;">
											  <div class="progress-bar progress-bar-striped progress-bar-animated ${bgClass} " role="progressbar" style="width: ${percentualUsed}%" aria-valuenow="${percentualUsed}" aria-valuemin="0" aria-valuemax="100">
											  	<b>${percentualUsed}%</b>
											  </div>
											</div>

											
											<div class="row mt-4">
												<div class="col-sm-12">
												 	<select class="form-control custom-select custom-select-sm float-right" id="selectUnidadeDados" onchange="javascript:convertBytes();" style="width:80px;">
												        <option value="B">Bytes</option>
												        <option value="KB">KB</option>
												        <option value="MB" selected>MB</option>
												        <option value="GB">GB</option>
												        <option value="TB">TB</option>
												    </select>
													<p>
														<span><strong>Total Usado: </strong></span>
														<span id="spanUsedCapacityBytes">${usedCapacityBytes}MB</span>
														<input type="hidden" id="usedCapacityBytes" value="${usedCapacityBytes}" />
													</p>
													<p>
														<span><strong>Capacidade Total: </strong></span>
														<span id="spanTotalCapacityBytes">${totalCapacityBytes}MB</span>
														<input type="hidden" id="totalCapacityBytes" value="${totalCapacityBytes}" />
													</p>

												</div>
											</div>
											<hr/>
											<div class="row mt-4">
												<div class="col">
													<p>
														<span><strong><i class="fa fa-file"></i> Total de Objetos Armazenados: </strong></span>
														<span>${objectCount} objetos </span>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-3 col-lg-3"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-12 text-center">
				            <a id="btnRefresh" class="btn btn-outline-primary text-center" href="/siga/app/armazenamento/estatistica" title="Atualizar" style="margin-top:50px;"><i class="fa fa-sync-alt"></i> Atualizar</a>
				            				            					            					           
						</div>						
					</div>
														
			</div>			
		</div>
	</div>
				

</siga:pagina>
