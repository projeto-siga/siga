<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA DEPENDENTE MAIOR INVALIDO-->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		
		<mod:grupo>
			<mod:texto titulo="Dependente" var="dependente" />
		</mod:grupo>
		
	</mod:entrevista>

	<mod:documento>
	
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			Eu, ${doc.subscritor.descricao}, matrícula RJ${doc.subscritor.matricula}, venho por meio deste solicitar a V.S.ª., 
			s.m.e., o deferimento do pedido de inclusão de ${dependente}, como meu/minha dependente 
			no Plano de Saúde da Justiça Federal, na qualidade de maior inválido(a).</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>
