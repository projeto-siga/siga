<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<siga:pagina titulo="Erro Geral">

<table width="100%" height="100%">
	<tr>
		<td align="center" valign="center">
		<table class="login" width="50%">
			<tr class="header" align="center">
				<td style="padding:2pt; font-size:14pt;">Documento Reservado</td>
			</tr>
			<tr align="center">
				<td style="padding:2pt; font-size:14pt;">O documento que está
				para ser mostrado é reservado, e seu acesso será registrado. Deseja
				continuar?</td>
			</tr>
			<tr align="center">
				<td style="padding:2pt; font-size:14pt;"><input value="Sim"
					type="button"
					onclick="javascript:window.location='exibir.action?id=${doc.idDoc}&via=${via}&confirmadoReservado=ok'">&nbsp&nbsp&nbsp<input
					type="button" onclick="javascript:history.back()" value="Não"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</siga:pagina>



