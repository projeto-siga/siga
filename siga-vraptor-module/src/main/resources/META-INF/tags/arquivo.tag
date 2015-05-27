<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="arquivo" required="false"%>

<a href="${linkto[SolicitacaoController].baixar(requestScope[arquivo].idArquivo)}" >${_arquivo.nomeArquivo}</a>
