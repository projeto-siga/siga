<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="arquivo" required="false"%>

<a href="${linkto[SolicitacaoController].baixar(requestScope[arquivo].idArquivo)}" >${arquivo.nomeArquivo}</a>
