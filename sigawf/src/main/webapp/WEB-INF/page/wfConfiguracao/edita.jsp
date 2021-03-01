<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:cfg-edita>
	<c:if
		test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('DEFINICAO_DE_PROCEDIMENTO')}">
		<div class="row">
			<div class="col col-12">
				<p class="alert alert-dark">Restrições Específicas do Módulo de
					Workflow</p>
			</div>
			<c:if
				test="${not empty erroEmConfiguracao or tipoDeConfiguracao.ativo('DEFINICAO_DE_PROCEDIMENTO')}">
				<div class="col-sm-6 col-lg-4">
					<div class="form-group">
						<label>Definição de Procedimento</label>
						<siga:select name="idDefinicaoDeProcedimento"
							list="definicoesDeProcedimentos" listKey="id" listValue="nome"
							theme="simple" headerValue="[Indefinido]" headerKey="0"
							value="${idDefinicaoDeProcedimento}"
							required="${tipoDeConfiguracao.obrigatorio('DEFINICAO_DE_PROCEDIMENTO')}" />
					</div>
				</div>
			</c:if>
		</div>
	</c:if>
	<script>
		$(document).ready(function() {
			$('[name=idDefinicaoDeProcedimento]').addClass('siga-select2');
		});
	</script>
</siga:cfg-edita>