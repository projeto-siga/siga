<!DOCTYPE1 HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Juntar Documento">
	
	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");</script>
	</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>
				Juntada de Documento - ${mob.siglaEDescricaoCompleta}
			</h2>
			
			<div class="gt-content-box gt-for-table">

            <form action="juntar_gravar" enctype="multipart/form-data" cssClass="form" method="POST">
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="sigla" value="${sigla}"/>

			<table class="gt-form-table">
				<tr class="header">
					<td colspan="2">
						Dados da juntada
					</td>
				</tr>
				<c:choose>
					<c:when test="${!doc.eletronico}">
						<tr>
							<td>
								<label>
									Data:
								</label>
							</td>
							<td>
								<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this, true);" />
							</td>
						<tr>
						<tr>
							<td>
								Responsável:
							</td>
							<td>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
								&nbsp;
								<input type="checkbox" theme="simple" name="substituicao" value="${substituicao}" onclick="javascript:displayTitular(this);" />
								Substituto
							</td>
						</tr>
					</c:when>
				</c:choose>
			</tr>
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
					<siga:selecao propriedade="titular" tema="simple" modulo="siga" />
				</td>
			</tr>
			<tr>
					<td>
						Documento:
					</td>
					<td>
						<siga:escolha var="idDocumentoEscolha">
							<siga:opcao id='1' texto="Documento Interno" >
								<siga:selecao tema='simple'  titulo="Documento Pai:" propriedade="documentoRef" urlAcao="expediente/buscar" urlSelecionar="expediente/selecionar" modulo="sigaex"/>
							</siga:opcao>
							<siga:opcao id='2' texto="Documento Externo ao SIGAEX" >
								<input type="text" theme='simple' name="idDocumentoPaiExterno" value="${idDocumentoPaiExterno}" />
							</siga:opcao>
						</siga:escolha>
					</td>
				</tr>
				<tr class="button">
					<td colspan="2"><input type='submit' value="Ok"  class="gt-btn-medium gt-btn-left"/>
						<input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
				</tr>
			</table>
		</form>

	</div></div></div>
	
	<script> 
		muda_escolha(document.getElementById("idDocumentoEscolha"));
	</script>
</siga:pagina>
