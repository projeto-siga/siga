<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
	</mod:entrevista>
	<mod:documento>
		<p>NOME: <u>${doc.subscritor.descricao}</u> &nbsp; &nbsp; &nbsp;
		&nbsp; MATRÍCULA: <u>${doc.subscritor.sigla}</u><br />
		CARGO: <u>${doc.subscritor.cargo.nomeCargo}</u><br />
		LOTAÇÃO: <u>${doc.subscritor.lotacao.descricao}</u> &nbsp; &nbsp;
		&nbsp; &nbsp; RAMAL: <u>${ramal}</u></p>
	</mod:documento>
</mod:modelo>
