<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:catch var="catchException">
	<siga:pagina titulo="Siga - Logout" desabilitarbusca="sim"
                 meta="<META HTTP-EQUIV='refresh' CONTENT='1;URL='/siga/'>" >

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
						<tr></tr>
					</table>
				</div>
			</div>
		</div>
	</siga:pagina>
</c:catch>