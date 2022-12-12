<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<siga:pagina titulo="Inativar">
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Inativar <fmt:message key="usuario.lotacao"/> - ${lotacao.sigla}</h5>
			</div>
			<div class="card-body">
				<div class="form-group">
					<div class="form-group">
						<label for="motivo">Motivo</label>
						<textarea class="form-control" name="motivo" value="" cols="60"
							rows="5" onkeydown="corrige();tamanho();" maxlength="500"
							onblur="tamanho();" onclick="tamanho();"></textarea>
						<small class="form-text text-muted float-right" id="Qtd">Restam&nbsp;500&nbsp;Caracteres</small>
					</div>
				</div>
			
			</div>
		</div>
	</div>
</siga:pagina>
