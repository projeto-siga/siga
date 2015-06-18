<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
	</mod:entrevista>
	<mod:documento>
		<b>${doc.subscritor.descricao}</b>, matrícula N&ordm; <b>${doc.subscritor.sigla}</b>, 
		<b>${doc.subscritor.cargo.nomeCargo}</b>, 
		<b>${doc.subscritor.padraoReferenciaInvertido}</b>, 
		lotado(a) no(a) <b>${doc.subscritor.lotacao.descricao}</b>,
	</mod:documento>
</mod:modelo>
