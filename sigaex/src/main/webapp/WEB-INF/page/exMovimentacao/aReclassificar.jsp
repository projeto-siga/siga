<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>
<script type="text/javascript" language="Javascript1.1">
function sbmt() {	
	frm.action='/sigaex/app/expediente/mov/reclassificar_gravar?id='+${doc.idDoc};
	frm.submit();
}
</script>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Reclassificação - ${mob.sigla}</h5>
			</div>
			<div class="card-body">
			<form name="frm" action="reclassificar_gravar" method="POST">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="sigla" value="${sigla}"/>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label>Data</label> 
								<input type="text" name="dtMovString" onblur="javascript:verifica_data(this,0);"  class="form-control"/>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label>Responsável</label> 
							<siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<div class="form-check form-check-inline mt-4">
								<input type="checkbox" name="substituicao" onclick="javascript:displayTitular(this);" />
								<label class="form-check-label" for="substituicao">Substituto</label>
							</div>
						</div>
					</div>
				</div>
				<div class="row" id="tr_titular" style="display: ${exDocumentoDTO.substituicao ? '' : 'none'};" >
					<div class="col-12" >
					<input type="hidden" name="campos" value="titularSel.id" />
						<div class="form-group">
							<label>Titular</label>
							<input type="hidden" name="campos" value="titularSel.id" />
							<siga:selecao propriedade="titular" tema="simple" modulo="siga" />
						</div>
					</div>
				</div>				
				<div class="row">
					<div class="col-12" >
						<div class="form-group">
							<siga:selecao titulo="Nova Classificação" propriedade="classificacao" modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
						</div>
					</div>
					<div class="col-12">
						<div class="form-group">
							<label>Motivo</label>
							<input type="text" name="descrMov" maxLength="128" class="form-control"/>
						</div>
					</div>
				</div>				
				<c:if test="${tipoResponsavel == 3}">
				<div class="row">
					<div class="col-12" >
						<div class="form-group">
							<label>Observação</label>
							<input type="text" size="30" name="obsOrgao" class="form-control"/>
						</div>
					</div>
				</div>				
				</c:if>
				<div class="row">
					<div class="col-12">
						<div class="form-group">
							<button type="submit" class="btn btn-primary" >Ok</button>
							<button type="button" onclick="javascript:history.back();" class="btn btn-primary" >Cancela</button>
						</div>
					</div>
				</div>
			</form>
			</div>
		</div>
	</div>
</siga:pagina>
