<!DOCTYPE1 HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Apensar Documento">
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");</script>
	</c:if>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Apensação de Documento - ${mob.siglaEDescricaoCompleta}</h2>
			
			<div class="gt-content-box gt-for-table">
				<form action="apensar_gravar" enctype="multipart/form-data" class="form" method="POST">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados da apensação</td>
						</tr>

						<!-- Checa se o documento é eletronico ou não. Caso seja, seu valor default para Data é o atual e o Responsável é quem fez o Login. -->
						<c:choose>
							<c:when test="${!doc.eletronico}">
								<!-- Documento Eletronico -->
								<tr>
									<td>
										Data:
									</td>
									<td>
										<input name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this, true);" />
									</td>
								</tr>
								<tr>
									<td>Responsável:</td>
									<td>
										<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
										&nbsp;&nbsp;
										<input type="checkbox" name="substituicao" onclick="javascript:displayTitular(this);" />&nbsp;&nbsp;Substituto
									</td>
								</tr>
							</c:when>
						</c:choose>
						
						<!-- abre o TR  -->
						<c:choose>
							<c:when test="${!substituicao}">
								<tr id="tr_titular" style="display: none">
							</c:when>
							<c:otherwise>
								<tr id="tr_titular" style="">
							</c:otherwise>
						</c:choose>
						<td>
							Titular:
						</td>
						<input type="hidden" name="campos" value="titularSel.id" />
						<td colspan="3">
							<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
						</td>
						</tr>
						<!-- fim do TR -->
						<tr>
							<td>Documento Mestre:</td>
							<td>
								<siga:selecao tema='simple' titulo="Documento Mestre:" propriedade="documentoRef" urlAcao="expediente/buscar" urlSelecionar="expediente/selecionar" modulo="sigaex" />
							</td>
						</tr>
						<tr class="button">
							<td colspan="2">
								<input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> 
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
