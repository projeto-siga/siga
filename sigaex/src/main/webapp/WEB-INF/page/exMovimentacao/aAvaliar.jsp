<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");</script>
	</c:if>
	
	<script type="text/javascript" language="Javascript1.1">
	function sbmt() {	
		frm.action='/sigaex/app/expediente/mov/avaliar_gravar?id=' + ${doc.idDoc};
		frm.submit();
	}
	</script>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Avaliação - ${mob.sigla}</h2>

			<div class="gt-content-box gt-for-table">
			
				<form name="frm" action="avaliar_gravar" method="POST">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
		
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Avaliação</td>
						</tr>
						<tr>
							<td>Data:</td>
							<td><input type="text" name="dtMovString"
								onblur="javascript:verifica_data(this,0);" /></td>
						</tr>
						<tr>
							<td>Responsável:</td>
							<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
							&nbsp;&nbsp;<input type="checkbox" name="substituicao"
								onclick="javascript:displayTitular(this);" />Substituto</td>
						</tr>
						<c:choose>
							<c:when test="${!substituicao}">
								<tr id="tr_titular" style="display: none">
							</c:when>
							<c:otherwise>
								<tr id="tr_titular" style="">
							</c:otherwise>
						</c:choose>
		
						<td>Titular:</td>
							<input type="hidden" name="campos" value="titularSel.id" />
						<td colspan="3"><siga:selecao propriedade="titular"
									tema="simple" modulo="siga" /></td>
						</tr>
						<siga:selecao titulo="Nova Classificação (opcional):" propriedade="classificacao" modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
						<tr>
							<td>Motivo da reclassificação<br/> (obrigatório se informada nova classificação):</td>
							<td width="75%">
								<input type="text" name="descrMov" size="50" maxLength="128" />
							</td>
						</tr>
						<c:if test="${tipoResponsavel == 3}">
							<tr>
								<td>Observação</td>
								<td><input type="text" size="30" name="obsOrgao" /></td>
							</tr>
						</c:if>
		
						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
								value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
