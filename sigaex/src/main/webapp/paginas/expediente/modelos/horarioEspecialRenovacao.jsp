<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CONCESSÃO DE HORARIO ESPECIAL AO SERVIDOR PUBLICO ESTUDANTE 
- RENOVAÇÃO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		
		<font color="blue"><b>
		<mod:grupo titulo="DETALHES DO HORÁRIO"></mod:grupo>
		</font></b>	
		<mod:selecao titulo="Quais alterações fará?"
				var="opcaoHorario" opcoes="manutenção do horário anterior
				;alteração do horário anterior para" reler="sim" />
			
				
		<c:if test="${opcaoHorario == 'alteração do horário anterior para'}">
					<mod:memo colunas="65" linhas="2" 
					titulo="DETALHES DA PROPOSTA DE HORARIO" var="ComentarAlteracaoHorario"/>
        </c:if>
        					        
        
	</mod:entrevista>	
	<mod:documento>
	<mod:valor var="texto_requerimento">
				
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, 
		 lotado(a) no(a)${doc.subscritor.lotacao.descricao},
	
		vem requerer a Vossa Senhoria, <B> RENOVAÇÃO DO HORÁRIO ESPECIAL AO SERVIDOR 
		ESTUDANTE</B>, apresentando, para tanto, declaração de freqüência regular no 
		período anterior, expedida pela Instituição de Ensino, em atendimento ao 
		disposto nos art. 8º, I, e 9º da Resolução n.º 5/2008, do Conselho 
		da Justiça Federal.
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
		Outrossim, conforme documentação comprobatória de matrícula atual e do 
		horário das respectivas aulas, encaminhada através do titular da Unidade, 
		solicita a 
		<c:choose>
	    <c:when test="${opcaoHorario == 'alteração do horário anterior para'}">	
			${opcaoHorario} ${ComentarAlteracaoHorario}.
        </c:when>
        <c:otherwise>
        	${opcaoHorario}.
        </c:otherwise>
        </c:choose>
        </p><br>
        </mod:valor>
        <mod:valor var="texto_requerimento4">
			<p align="center">
			De acordo.
			</p>   
			<p align="center">
			____________________________________________________________
			</p>
			<p align="center">
			Assinatura e Matrícula do Superior Hierárquico
			</p>
		</mod:valor>	

	
</mod:documento>
</mod:modelo>
