<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
LICENÇA PATERNIDADE -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
			<mod:grupo titulo="DETALHES DO(S) RECÉM-NASCIDO(S)">
				<mod:selecao titulo="Número de Recém-Nascidos"
					var="quantidadeNascidos" opcoes="1;2;3;4;5;6" reler="sim" />
				<c:forEach var="i" begin="1" end="${quantidadeNascidos}">
					<mod:grupo>
						<mod:texto titulo="${i}) Nome" largura="40"
							var="nomeRecemNasc${i}" />
						<mod:data titulo="Data de Nascimento" var="dataNasc${i}" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
	</mod:entrevista>

	<mod:documento>	
		<mod:valor var="texto_requerimento">
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.nomeLotacao}, vem requerer a Vossa Senhoria, nos termos do art. 208 da Lei 8.112/90, <b>LICENÇA-PATERNIDADE</b>, a que faz jus, em virtude do nascimento de <c:forEach var="i" begin="1" end="${quantidadeNascidos}"><b>${requestScope[f:concat('nomeRecemNasc',i)]}</b>, em ${requestScope[f:concat('dataNasc',i)]}<c:if test="${i+1 == quantidadeNascidos}"> e </c:if></c:forEach>, conforme 
					<c:if test="${quantidadeNascidos =='1'}">
					certidão 
					</c:if>
					<c:if test="${quantidadeNascidos >'1'}">
					certidões
					</c:if>
					em anexo.
		</p>
		</mod:valor>
			</mod:documento>
</mod:modelo>
