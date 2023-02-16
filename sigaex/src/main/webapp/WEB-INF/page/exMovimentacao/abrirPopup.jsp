<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de Transferência" popup="true">
	<table style="float: none; clear: both;" width="100%" align="left"
		border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr bgcolor="#FFFFFF">
			<td width="100%">
				<table width="100%" border="0" cellpadding="2">
					<tr>
						<td width="100%" align="center" valign="bottom"><img
							src="/sigaex/imagens/logo_sp_sem_papel_200x200.png" width="65" height="65" />
						</td>
					</tr>
					<tr>
						<td width="100%" align="center">
							<p style="font-size: 11pt;">
								<b>${f:resource('modelos.cabecalho.subtitulo')}</b><br />
							</p>
						</td>
					</tr>
					<tr>
						<td width="100%" align="center"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
	<strong>Motivo: </strong>${motivo}
</siga:pagina>