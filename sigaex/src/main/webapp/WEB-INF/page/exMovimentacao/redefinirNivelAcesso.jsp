<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Redefinição de Nível de Acesso">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Redefinição de Nível de Acesso - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="redefinir_nivel_acesso_gravar" namespace="/expediente/mov" theme="simple" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					<div class="row">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label for="dtMovString">Data</label>
								<input class="form-control" type="text" name="dtMovString" onblur="javascript:verifica_data(this,0);" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
							</div>
						</div>
						<div class="col-sm-2 mt-4">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" theme="simple" name="substituicao" onclick="javascript:displayTitular(this);" />
								<label class="form-check-label">Substituto</label>
							</div>
						</div>
					</div>						
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
						<c:choose>
							<c:when test="${!substituicao}">
								<div id="tr_titular" style="display: none">
							</c:when>
							<c:otherwise>
								<div id="tr_titular" style="">
							</c:otherwise>
						</c:choose>
									<label>Titular:</label>
									<input type="hidden" name="campos" value="titularSel.id" />
									<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Nível de Acesso</label>
								<select class="form-control" name="nivelAcesso" theme="simple" value="1">
							      <c:forEach var="item" items="${listaNivelAcesso}">
							      	<c:if test="${(siga_cliente ne 'GOVSP') || ( (item.idNivelAcesso eq 5) )}">
								        <option value="${item.idNivelAcesso}"  <c:if test="${item.idNivelAcesso == nivelAcesso}">selected</c:if> >
								          <c:out value="${item.nmNivelAcesso}" />
								        </option>
							        </c:if>
							      </c:forEach>
							    </select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="submit" value="Ok" class="btn btn-primary"/> 
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2"/> 
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
