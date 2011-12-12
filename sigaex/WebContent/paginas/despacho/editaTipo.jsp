<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Tipo de Despacho">
	<h1>Cadastro de Tipos de despacho</h1>
	<center>
	<table border="0" width="100%">
		<tr>
			<td><ww:form name="frm" action="gravar"
				namespace="/despacho/tipodespacho" theme="simple" method="POST">

				<table class="form" width="100%">
					<c:if test="${!empty param.id}">
						<ww:hidden name="idTpDespacho" />
						<tr>
							<td width="10%">código:</td>
							<td><fmt:formatNumber pattern="0000000"
								value="${idTpDespacho }" /></td>
						</tr>
					</c:if>
					<tr>
						<td width="10%">Descrição:</td>
						<td><ww:textarea cols="60" rows="5" name="descTpDespacho" /></td>
					</tr>
					<tr>
						<td width="10%">Ativo:</td>
						<td><ww:checkbox name="ativo" fieldValue="false" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><ww:submit value="OK" /></td>
					</tr>
				</table>
			</ww:form></td>
		</tr>

	</table>
	</center>
</siga:pagina>

