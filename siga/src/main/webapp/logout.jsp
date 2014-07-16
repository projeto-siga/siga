<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<c:catch var="catchException">
	<siga:pagina titulo="Login Inválido" desabilitarbusca="sim">
		<%-- cria a url para redirecionar --%>
		<c:url var="urlPagina" value="${pagina}" />
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">

				<div class="gt-content-box">
					<table width="100%">
						<tr>
							<td align="center" valign="middle">
								<table class="form" width="50%">
									<tr>
										<td style="text-align: center; padding-top: 10px;">
											<h3>Saindo...</h3>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
						
						</tr>
					</table>
				</div>
				<div style="padding-top: 10px;">
					<input type="button" id="show_stack" value="Tentar Novamente"
						class="gt-btn-large gt-btn-right"
						onclick="javascript:history.back();" />
				</div>
			</div>
		</div>

		<c:remove var="pagina" scope="session" />
	</siga:pagina>
</c:catch>