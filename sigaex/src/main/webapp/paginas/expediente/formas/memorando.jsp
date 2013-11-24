<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head></head>
		<body>
		<p align="right">
		<b>MEMORANDO N&ordm; ${doc.codigo}</b><br/>
		<b>${data}</b></p>
		<p align="left">DE: ${subscritor.lotacao.descricao}<br/>
		PARA: ${destinatario}</p>

		<c:import url="${url_texto}" />
		
		Atenciosamente,
		
		<p align="center"><b>${subscritor.descricao}<br/>
		${subscritor.cargo.nomeCargo}
		<c:if test="${not empty subscritor.funcaoConfianca.nomeFuncao}">
		/ ${subscritor.funcaoConfianca.nomeFuncao}
		</c:if>
		</b></p>
		
		<table width="100%" border="0"><tr><td align="right"><table border="1"><tr><td>Classif. documental</td><td>${doc.exClassificacao.sigla}</td></tr></table></td></tr></table>
		</body>
		</html>
	</mod:documento>
</mod:modelo>
