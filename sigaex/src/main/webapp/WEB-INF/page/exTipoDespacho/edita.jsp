<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Tipo de Despacho">
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Cadastro de Tipos de despacho</h5></div>

			<div class="card-body">
				<form name="frm" action="gravar" theme="simple" method="post">

						<c:if test="${!empty exTipoDespacho.idTpDespacho}">
							<input type="hidden" name="exTipoDespacho.idTpDespacho" value="${exTipoDespacho.idTpDespacho}"/>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label>C&oacute;digo</label>
										<label class="form-control" style="max-width: 90px"><fmt:formatNumber pattern="0000000" value="${exTipoDespacho.idTpDespacho}"/></label>
									</div>
								</div>
							</div>
						</c:if>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label for="descTpDespacho">Descri&ccedil;&atilde;o</label>
									<textarea id="descTpDespacho" name="exTipoDespacho.descTpDespacho" cols="60" rows="5" maxlength="256" class="form-control">${exTipoDespacho.descTpDespacho}</textarea>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-check">
									  <input class="form-check-input" type="checkbox" value="S" id="ativo" name="exTipoDespacho.fgAtivo" ${exTipoDespacho.fgAtivo == 'S' ? 'checked' : ''} />
									  <label class="form-check-label" for="ativo">Ativo</label>
								</div>
							</div>
						</div>
						<div class="row  mt-3">
							<div class="col-sm-6">
								<div class="form-group">
									<c:choose>
										<c:when test="${empty exTipoDespacho.idTpDespacho}">
											<input type="submit" value="OK" class="btn btn-primary" />
											<a href="${linkTo[ExTipoDespachoController].lista()}" class="btn btn-cancel ml-2">Cancela</a>
										</c:when>
										<c:otherwise>
											<a href="${linkTo[ExTipoDespachoController].lista()}" class="btn btn-secondary">Voltar</a>
										</c:otherwise>
									</c:choose>																	
								</div>
							</div>
						</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>

