<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- ESSE DEFINE OS ESPAÇOS QUE OCORRERAM EM TODOS OS INICIOS DE DOCUMENTOS IMPRESSOS
	 A PARTIR DOS MODELOS -->
	 <c:if test="${empty semEspacos}">
		<BR>
		<BR>
	</c:if>
