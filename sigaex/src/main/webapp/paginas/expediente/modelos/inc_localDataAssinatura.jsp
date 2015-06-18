<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- ESTE ARQUIVO SERVE PARA EXIBIR O LOCAL A DATA E A ASSINATURA DOS MODELOS -->
	        <p style="TEXT-INDENT: 2cm">${doc.dtExtenso}</p>
	        <br/>
	        <br/>
	        <br/>
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
