<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:catch var="catchException">
	<siga:pagina titulo="Erro Geral" desabilitarbusca="sim" desabilitarmenu="sim" desabilitarComplementoHEAD="sim">
		<div class="gt-bd clearfix" id="divPrincipal" style="margin-top: 200px">
		<div class="gt-content clearfix">
			<h2>N&atilde;o Foi Poss&iacute;vel Completar a Opera&ccedil;&atilde;o</h2>
			</div>
				<div class="gt-content-box">
					<table width="100%">
						<tr>
							<td align="center" valign="middle">
								<table class="form" width="50%">
									<tr>
										<td style="text-align: center; padding-top: 10px;">
											<h3>${exception.message}</h3>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		
		<script language="javascript">
			document.getElementById('divPrincipal').style.marginTop = screen.height * 0.2 + 'px';
		</script>
	</siga:pagina>
</c:catch>