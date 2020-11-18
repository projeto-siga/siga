<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>
	
	<script type="text/javascript" language="Javascript1.1">
	function sbmt() {	
		frm.action='/sigaex/app/expediente/mov/avaliar_gravar?id=' + ${doc.idDoc};
		frm.submit();
	}
	</script>
	
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Avaliação - ${mob.sigla}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="avaliar_gravar" method="POST">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					<div class="row">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label>Data</label>
								<input class="form-control" type="text" name="dtMovString"
									onblur="javascript:verifica_data(this,0);" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-2 mt-4">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" 
									theme="simple" name="substituicao"  value="${substituicao}" 
									onclick="javascript:displayTitular(this);" />
								<label class="form-check-label">Substituto</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-12">
							<div class="form-group">
							<c:choose>
								<c:when test="${!substituicao}">
									<div id="tr_titular" style="display: none">
								</c:when>
								<c:otherwise>
									<div id="tr_titular" style="">
								</c:otherwise>
							</c:choose>
									<label>Titular</label>
									<input class="form-control" type="hidden" name="campos" value="titularSel.id" />
									<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-12">
							<div class="form-group">
								<siga:selecao titulo="Nova Classificação" propriedade="classificacao" modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
								<small class="form-text text-muted">(opcional)</small>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Motivo da reclassificação</label>
								<input class="form-control" type="text" name="descrMov" size="50" maxLength="128" />
								<small class="form-text text-muted">(obrigatório se informada nova classificação)</small>
							</div>
						</div>
						<c:if test="${tipoResponsavel == 3}">
							<div class="col-12">
								<div class="form-group">
									<label>Observação</label>
									<input class="form-control" type="text" size="30" name="obsOrgao" />
								</div>
							</div>
						</c:if>
		
					</div>
					<div class="row">
						<div class="col-12">
							<input type="submit" value="Ok" class="btn btn-primary" />
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
