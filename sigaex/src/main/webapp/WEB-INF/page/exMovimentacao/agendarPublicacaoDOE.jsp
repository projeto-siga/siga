<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	
	<script type="text/javascript" src="../../../javascript/agendarPublicacaoDOE.js"></script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Agendamento de Publicação - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="${request.contextPath}/app/expediente/mov/agendar_publicacao_doe_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					<input type="hidden" name="id" value="${id}"/>
					<input type="hidden" name="urlRedirecionar" value="${urlRedirecionar}"/>
					
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label>Data para disponibilização</label>
								<input class="form-control" type="text" name="dtDispon" value="${dtDispon}" id="dt_dispon"  onblur="javascript:verifica_data(this,true)" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-9">
							<div class="form-group">
								<label>Lotação de Publicação</label>
									<siga:selecao tema="simple" propriedade="lotaSubscritor" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
								<label>Selecionar Macro</label>
								<select class="form-control" id="macro" name="macro" onchange="aplicarMacro(this.value)">
									<option value="0">Selecione</option>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Descrição do documento</label>
								<textarea class="form-control" name="descrPublicacao" cols="80" id="descrPublicacao" rows="15" class="gt-form-textarea">${descrPublicacao}</textarea>	
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="button" value="Salvar" onclick="javascript: validar();" class="btn btn-primary" ${disabled}/>
							<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-primary ml-2" />
						</div>
					</div>
					
				</form>	
			</div>
		</div>
	</div>
</siga:pagina>
<script type="text/javascript">
	javascript:selectFunction();
</script>
