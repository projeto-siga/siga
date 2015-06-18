<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- ESSE MODELO DEFINE O TITULO DE INICIO DOS DOCUMENTOS -->
		<p align="center"><span style="font-size:14pt;">Ilmo(a). Sr(a). Diretor(a) de Secretaria/Subsecretaria<br/>
		<c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose>
		</span></p>
	
		<c:import url="/paginas/expediente/modelos/inc_tit_espacos.jsp" />