<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="arquivo" required="false" type="br.gov.jfrj.siga.sr.model.SrArquivo"%>
<%@ attribute name="descricao" required="false" %>

<a href="${linkTo[SolicitacaoController].baixar(arquivo.idArquivo)}" target="${arquivo.idArquivo}" >${arquivo.nomeArquivo}</a>
<c:if test="${not empty descricao}">
	&nbsp;${arquivo.descricaoComplementar}
</c:if>
