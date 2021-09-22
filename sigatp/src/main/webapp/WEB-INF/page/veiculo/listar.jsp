<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Ve&iacute;culos</h2>
			
			<sigatp:erros/>

			<c:choose>
				<c:when test="${veiculos.size() > 0}">
					<div class="gt-content-box gt-for-table">     
					 	<table id="htmlgrid" class="gt-table tablesorter" >
						 	<thead>
						    	<tr>
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>						    	
						    	    <th width="5%">Placa</th>
							   		<th width="5%">Ano</th>
							   		<th width="15%">Modelo</th>
							   		<th width="15%">Grupo</th>
							   		<th width="20%">Lotacao</th>
							   		<th width="10%">Situacao</th>
								</tr>
							</thead>
							<tbody>	
								<c:forEach items="${veiculos}" var="veiculo">
								   	<tr>	
									   	<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[VeiculoController].editar(veiculo.id)}" titulo="ve&iacute;culo" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[VeiculoController].excluir(veiculo.id)}" titulo="ve&iacute;culo"
											onclick="javascript:return confirm('Tem certeza de que deseja excluir este veiculo?');"/>
										</td>

							    	    <td>${veiculo.placa}</td>
							    		<td>${veiculo.anoFabricacao}</td>
							    		<td>${veiculo.modelo}</td>
							    		<td>${veiculo.grupo.nome}</td>
							    		<td>${veiculo.lotacoes[0].lotacao.descricaoAmpliada}</td>
							    		<td>${veiculo.situacao}</td>		    		
									</tr>
								</c:forEach>
							</tbody>	
					     </table>
					</div>
		   		    <sigatp:paginacao/>    
				</c:when>
				<c:otherwise>
					<br/>
					<h3>N&atilde;o existem ve&iacute;culos cadastrados.</h3>
				</c:otherwise>
			</c:choose>
				
			<div class="gt-table-buttons">
				<a href="${linkTo[VeiculoController].incluir}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
			</div>
		</div>
	</div>
</siga:pagina>