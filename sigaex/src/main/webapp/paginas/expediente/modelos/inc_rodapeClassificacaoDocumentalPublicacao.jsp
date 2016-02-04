<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table align="left" width="100%" bgcolor="#FFFFFF"> 
			<tr>
				<td width="67%">
					<table align="right" width="70%" border="1" cellspacing="1" bgcolor="#000000">
						<tr>
							<td align="center" width="100%"
									style="font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">
							Publicada no Boletim Interno "online" de<br/>
						____/____/____, nos termos do art 15, § 4º, da Lei nº <br/>
						9527 de 10/12/97, publicada no D.O.U. de 11/12/97 <br/>
						c/c com o art 9º da resolução nº 3/2008-CJF.<br><br>
						__________________________________<br/>
						<sub>Assinatura</sub>
							</td>
						</tr>
					</table>		
				</td>
				<td width="3%"></td>
				<td width="30%"  valign="bottom">
						<table align="right" width="100%" border="1" cellspacing="1" bgcolor="#000000">
							<tr>
								<td align="center"  width="60%" style="font-family:Arial;font-size:8pt;text-decoration:italic;"
									bgcolor="#FFFFFF">Classif. documental</td>
								<td align="center" width="40%"
									style="font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${doc.exClassificacao.sigla}</td>
								</tr>
						</table>
				</td>
			</tr>
</table>
