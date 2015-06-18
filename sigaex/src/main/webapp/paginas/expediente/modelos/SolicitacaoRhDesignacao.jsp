<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/solicitacao.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Servidor" var="servidor" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Função" var="funcao" />
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_solicitacao">
			<p style="TEXT-INDENT: 2cm" align="justify">Solicito a <b>designação</b>
			do(a) servidor(a)<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" negrito="sim" nivelHierarquicoMaximoDaLotacao="4" /> para exercer a
			função comissionada de <b>${funcao}</b>,
			a partir da publicação da respectiva portaria.
			</p>
		</mod:valor>	
	</mod:documento>
</mod:modelo>

