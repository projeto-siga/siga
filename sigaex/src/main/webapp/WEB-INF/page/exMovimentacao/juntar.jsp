<!DOCTYPE1 HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Juntar Documento">
<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Juntada de Documento - ${mob.siglaEDescricaoCompleta}</h5>
			</div>
			<div class="card-body">
            <form action="juntar_gravar" enctype="multipart/form-data" cssClass="form" method="POST">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="sigla" value="${sigla}"/>
				<c:choose>
					<c:when test="${!doc.eletronico}">
						<div class="row">
							<div class="col-2">
								<div class="form-group">
									<label>Data</label> 
									<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this, true);" class="form-control"/>
								</div>
							</div>
							<div class="col-8">
								<div class="form-group">
									<label>Responsável</label> 
									<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
								</div>
							</div>
							<div class="col-2">
								<div class="form-group">
									<div class="form-check form-check-inline mt-4">
										<input type="checkbox" theme="simple" name="substituicao" value="${substituicao}" onclick="javascript:displayTitular(this);" />
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
					</c:when>
				</c:choose>
				<div class="row">
					<div class="col-12" >
						<div class="form-group">
							<label><fmt:message key="documento.mestre"/></label> 
							<siga:escolha id="idDocumentoEscolha" var="idDocumentoEscolha">
								<siga:opcao id='1' texto="Documento Interno" >
									<p></p>
									<siga:selecao tema='simple'  titulo="Documento Pai" propriedade="documentoRef" urlAcao="buscar" urlSelecionar="selecionar" modulo="sigaex"/>
								</siga:opcao>
								<c:if test="${siga_cliente!='GOVSP'}">
									<siga:opcao id='2' texto="Documento Externo ao Siga-Doc" >
										<input type="text" theme='simple' name="idDocumentoPaiExterno" value="${idDocumentoPaiExterno}" class="form-control mt-3"/>
									</siga:opcao>
								</c:if>
							</siga:escolha>
						</div>						
					</div>
				</div>
				<div class="row">
					<div class="form-group col-md-4">
						<button type="submit" class="btn btn-primary mt-auto" >Ok</button>						
						<a href="${linkTo[ExDocumentoController].exibe()}?sigla=${sigla}" class="btn btn-cancel ml-2">Cancela</a>
					</div>
				</div>
			</form>
			</div>
		</div>
	</div>
<script>  
	muda_escolha(document.getElementById("idDocumentoEscolha"));
</script>
</siga:pagina>
