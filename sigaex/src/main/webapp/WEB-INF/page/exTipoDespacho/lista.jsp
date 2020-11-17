<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<siga:pagina titulo="Lista Tipo de Despacho">
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Listagem dos tipos de despacho</h5></div>
			<div class="card-body">
				<table border="0" class="table table-sm table-striped ">
					<thead class="${thead_color}">
						<th align="right">N&uacute;mero</th>
						<th colspan="3">Descri&ccedil;&atilde;o</th>						
					</thead>
					<tbody class="table-bordered">
					<c:forEach var="tipoDespacho" items="${tiposDespacho}">
						<tr>
							<td>
								<a id="tipoDespacho${tipoDespacho.idTpDespacho}" href="editar?id=${tipoDespacho.idTpDespacho}">
									<fmt:formatNumber pattern="0000000" value="${tipoDespacho.idTpDespacho}" />									
								</a>
							</td>
							<td>${tipoDespacho.descTpDespacho}</td>
							<td class="text-center">
								<a href="#" onclick="confirmarExclusao('${tipoDespacho.idTpDespacho}');return false;"
									data-toggle="tooltip" data-placement="left" title="Excluir" style="color:#040404cc;"><i class="fas fa-times"></i></a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<form name="frm" id="frm" action="editar" theme="simple" method="get">
				<input type="submit" id="editar_0" value="Novo" class="btn btn-primary"/>
			</form>
			</div>					
		</div>			
		<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
			<div class="modal-body">
	       		Deseja realmente excluir?
	     	</div>
	     	<div class="modal-footer">
	       		<button type="button" class="btn btn-success" data-dismiss="modal">N&atilde;o</button>		        
	       		<a href="#" class="btn btn-danger btn-confirmacao-exclusao" role="button" aria-pressed="true">Sim</a>
			</div>
		</siga:siga-modal>	
	</div>	
<script>	
	function confirmarExclusao(idTipoDespacho){	
		var descricao = $('a[id="tipoDespacho' + idTipoDespacho +  '"]').text().trim();		
		
		$('.btn-confirmacao-exclusao').attr('href', 'apagar?id='.concat(idTipoDespacho));		
		sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Deseja realmente excluir o tipo de despacho ' + descricao + '?');
	}
	$(function() {
		$('[data-toggle="tooltip"]').tooltip();
	});
</script>	
</siga:pagina>