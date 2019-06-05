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
				<form name="frm" action="gravar" theme="simple" method="POST">

						<c:if test="${!empty exTipoDespacho.idTpDespacho}">
							<input type="hidden" name="exTipoDespacho.idTpDespacho" value="${exTipoDespacho.idTpDespacho}"/>
							<div class="row">
								<div class="col-sm-1">
									<div class="form-group">
										<label>C&oacute;digo</label>
										<label class="form-control"><fmt:formatNumber pattern="0000000" value="${exTipoDespacho.idTpDespacho}"/></label>
									</div>
								</div>
							</div>
						</c:if>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>Descri&ccedil;&atilde;o</label>
									<textarea name="exTipoDespacho.descTpDespacho" cols="60" rows="5" class="form-control">${exTipoDespacho.descTpDespacho}</textarea>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-check">
									  <input class="form-check-input" type="checkbox" value="" id="defaultCheck1"  name="exTipoDespacho.fgAtivo" <c:if test="${exTipoDespacho.fgAtivo == 'S'}">checked</c:if> >
									  <label class="form-check-label" for="defaultCheck1">Ativo</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<input type="submit" value="OK" class="btn btn-primary" />
								</div>
							</div>
						</div>

				</form>
			</div>
		</div>
	</div>
</siga:pagina>

