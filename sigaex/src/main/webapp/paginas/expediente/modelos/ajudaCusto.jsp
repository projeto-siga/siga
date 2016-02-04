<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO DE CONCESSÃO DE AJUDA DE CUSTO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
		<mod:grupo titulo="DETALHES DO REQUERIMENTO:">
			<%--<mod:texto titulo="Lotação Originaria" largura="60" var="sedeOriginaria"/>
			<mod:grupo></mod:grupo>--%>
			<mod:texto titulo="Lotação de Origem" largura="60" var="sedeOrigem"/>
            <mod:grupo></mod:grupo>
			<mod:texto titulo="Lotação de Destino" largura="60" var="sedeDestino"/>
			<mod:grupo></mod:grupo>
			<mod:texto titulo="Município Destinado de Residência" var="localidadeDestinada"/>
			<mod:texto titulo="Estado" var="estadoLocal" largura="2" maxcaracteres="2"/>			
			<mod:data titulo="Data da remoção" var="dataVigencia"/>
		</mod:grupo>
		
		<mod:grupo titulo="DEPENDENTES">
			<mod:selecao titulo="Nº de dependentes a incluir"
				var="dependentes" opcoes="1;2;3;4;5" reler="sim" />
			<c:forEach var="i" begin="1" end="${dependentes}">
				<mod:grupo>
					<mod:texto titulo="${i}) Nome" largura="45" var="nome${i}" />
					<mod:texto titulo="Parentesco" largura="20" var="grau${i}" />
					<mod:data titulo="Data de nascimento" var="dataNasc${i}" />
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
		
		
		</mod:entrevista>

	<mod:documento>
	<mod:valor var="texto_requerimento">						
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, removido no interesse da Administração, do(a) ${sedeOrigem} para o(a) ${sedeDestino}, com efeitos a contar de ${dataVigencia}, vem requerer a Vossa Excelência a concessão 
		da <b> AJUDA DE CUSTO </b> prevista no artigo 53 da 
		Lei n.º 8.112/90, com a redação dada pela Lei n.º 9.527/97
		, tendo em vista que efetivou, juntamente com os dependentes abaixo relacionados, 
		mudança de domicílio em caráter permanente para a localidade de ${localidadeDestinada}/${estadoLocal}, 
		conforme documentação comprobatória em anexo.
		</p>
		
		<p style="TEXT-INDENT: 2cm" align="justify">
	    Outrossim, está ciente de que a Justiça Federal possui contrato 
	    com transportadora, para o transporte dentro do Estado do Rio de Janeiro. </p>
	    <p style="TEXT-INDENT: 2cm" align="justify">Relaciona a seguir, o(s) nome(s) do(s) dependente(s):
	    </p>
        
        <table width="100%" border="0" cellpadding="1">
			<c:forEach var="i" begin="1" end="${dependentes}">
				<tr>
					<td>${i} - ${requestScope[f:concat('nome',i)]}</td>
					<td>${requestScope[f:concat('grau',i)]}</td>
					<td>${requestScope[f:concat('dataNasc',i)]}</td>
				</tr>
			</c:forEach>
		</table>
	</mod:valor>

	</mod:documento>
</mod:modelo>
