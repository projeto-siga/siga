<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
EXCLUSÃO DE DEPENDENTE(S) DO ABATIMENTO DO 
IMPOSTO DE RENDA RETIDO NA FONTE  -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
		<mod:grupo titulo="DETALHES DO(S) DEPENDENTE(S)">
			<mod:selecao titulo="Nº de dependentes a informar para Excluir do IRPF"
				var="dependentes" opcoes="1;2;3;4;5" reler="sim" />
				<c:forEach var="i" begin="1" end="${dependentes}">
					<mod:grupo>
						<mod:texto titulo="${i}) Nome" var="nome${i}" largura="60" />
						<br/> 
						<mod:selecao titulo="Grau de parentesco" var="grau${i}"
						opcoes="absolutamente incapaz do qual o contribuinte seja tutor ou curador;companheiro(a);cônjuge;enteado(a);filho(a);irmão(a)/neto(a)/bisneto(a)(sem arrimo dos pais);menor que o contribuinte crie ou eduque;pais/avós/bisavós;" />
					</mod:grupo>
				</c:forEach>
		</mod:grupo>
	
	<mod:grupo titulo="EXCLUSÃO DO DEPENDENTE">
		<mod:memo colunas="60" linhas="3" titulo="Motivo" var="motivoDaExclusao" />
	</mod:grupo>

	</mod:entrevista>
			
	<mod:documento>
		<mod:valor var="texto_requerimento">	
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, requer a Vossa 
		Senhoria a <b>exclusão</b> de: 
			<c:forEach var="i" begin="1" end="${dependentes}">
				<b>${requestScope[f:concat('nome',i)]}</b> (${requestScope[f:concat('grau',i)]}), 
					<c:if test="${i+1 == dependentes}">
					e
					</c:if>
			</c:forEach>
			
        <c:if test="${dependentes =='1'}">
			  como dependente 
        </c:if>
        
    	<c:if test="${dependentes >'1'}">	
 			  como dependentes 
        </c:if>
 		para fins de dedução no Imposto de Renda retido na fonte,  tendo em vista que:
		<b><i>${motivoDaExclusao} </i></b>.
		</p>		
		</mod:valor>
</mod:documento>
</mod:modelo>
