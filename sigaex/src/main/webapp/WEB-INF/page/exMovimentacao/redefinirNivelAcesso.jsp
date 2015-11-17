<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Redefinição de Nível de Acesso">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Redefinição de Nível de Acesso - ${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">
				<form name="frm" action="redefinir_nivel_acesso_gravar" namespace="/expediente/mov" theme="simple" method="POST">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Redefinição de Nível de Acesso</td>
						</tr>
						<tr>
							<td>Data:</td>
							<td>
								<input type="text" name="dtMovString" onblur="javascript:verifica_data(this,0);" />
							</td>
						</tr>
						<tr>
							<td>Responsável:</td>
							<td>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
								&nbsp;&nbsp;
								<input type="checkbox" theme="simple" name="substituicao" onclick="javascript:displayTitular(this);" /> &nbsp;&nbsp;Substituto
							</td>
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
							<td colspan="3">
								<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
							</td>
						</tr>
						<tr>
							<td>Nível de Acesso</td>
							<td>
								<select name="nivelAcesso" theme="simple" value="1">
							      <c:forEach var="item" items="${listaNivelAcesso}">
							        <option value="${item.idNivelAcesso}" <c:if test="${item.idNivelAcesso == nivelAcesso}">selected</c:if> >
							          <c:out value="${item.nmNivelAcesso}" />
							        </option>
							      </c:forEach>
							    </select>
							</td>
						</tr>
		
						<tr class="button">
							<td colspan="2">
								<input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/> 
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/> 
							</td>
						</tr>
					</table>
				</form>
			<div>
		<div>
	<div>

</siga:pagina>
