<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- INICIO PRIMEIRO RODAPE
		
		<table align="left" width="100%" bgcolor="#FFFFFF">
		    <tr>
		        <td width="70%">
		        	<c:if test="${not empty textoPrimeiroRodape}">
		    			${textoPrimeiroRodape}
		    		</c:if>
		        </td>
		        <td width="30%" >
		        <table align="right" width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" bgcolor="#000000">
		            <tr>
		                <td align="center" width="60%" style="border-collapse: collapse; border-color: black; font-family:Arial; font-size:8pt;" bgcolor="#FFFFFF"><i>Classif. documental</i></td>
		                <td align="center" width="40%" style="border-collapse: collapse; border-color: black; font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${doc.exClassificacao.sigla}</td>
		            </tr>
		        </table>
		        </td>
		    </tr>
		    
		</table>
		
		FIM PRIMEIRO RODAPE -->
		
		<!-- INICIO RODAPE
		
		<table width="100%" border="0" cellpadding="0" bgcolor="#FFFFFF">
			<c:if test="${not empty textoRodape}">
		   	 <tr>
		   	 	<td>${textoRodape}</td>
		   	 </tr>
		    </c:if>
			<tr>
				<td width="100%" align="right">#pg</td>
			</tr>
		</table>
		
		FIM RODAPE -->