<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--  ESSE MODELO DEFINE O TITULO DE INICIO DOS DOCUMENTOS -->		
		
		  
		<p align="center">
		<b>De acordo</b>.
		</p>
        <br> 
        <c:if test="${not empty comData}">
        <p align="center">               
        ${doc.dtExtenso}
        </p> 
        </c:if>
		<br>  
		<br>
		<p align="center"> 
		<b>ASSINATURA E MATRÍCULA DO SUPERIOR HIERÁRQUICO</b>
		</p>
