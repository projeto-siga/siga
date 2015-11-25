<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="arquivo" required="false" type="br.gov.jfrj.siga.sr.model.SrArquivo"%>

<a href="${linkTo[SolicitacaoController].baixar[arquivo.idArquivo]}" >${arquivo.nomeArquivo}</a>&nbsp;${arquivo.descricaoComplementar}