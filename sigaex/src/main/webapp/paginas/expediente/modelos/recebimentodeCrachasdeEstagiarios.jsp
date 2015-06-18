<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
Inscrição de estagiario -->

<mod:modelo>

	<mod:entrevista>
			
		<mod:grupo titulo="crachas">
			<mod:selecao titulo="Nº de crachas a incluir"
				var="crachas" opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
			<c:forEach var="i" begin="1" end="${crachas}">
				<mod:grupo>
					<mod:texto titulo="${i}) Nome" largura="20" var="nome${i}" />
					<mod:texto titulo="${i}) Matricula" largura="20" var="matricula${i}" />
					<mod:texto titulo="${i}) Lotação" largura="20" var="lotacao${i}" />
					<mod:data titulo="Data" var="data${i}" />
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
		
		
</mod:entrevista>

	<mod:documento>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp"/>		

		<h1>Termo de compromisso e recebimento do crachá de identificação funcional</h1>
		<body>	
		
				
		<p style="TEXT-INDENT: 2cm" align="justify">
		Recebi nesta data o crahá de identificação no âmbito da <b>JUSTIÇA FEDERAL DE 1º INSTÂNCIA - <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose><b>, e me comprometo em caso de perda, roubo ou furto a comunicar, <b>incontinente<b>
		á subsecretaria de Gestão de Pessoas, bem como, em caso de desligamento, devolve-lo áquela Subsecretaria-Seção 
		de Avaliação de Desempenho.
		</p>		
		<p style="TEXT-INDENT: 2cm" align="justify">
		Declaro, outrossim que, em caso de pedido de 2º via do mesmo, deverei efetuar o pagamento, diretamente na Subsecretaria
		de Planejamento, Orçamento e Finanças, do valor para sua confecção.
		</p>
		<h2>Observação:</h2>
		1- Executam-se do pagamento acima descrito os casos de roubo, furto e inutilização acidental do cracha, quando não se caracterizar
		, neste último caso, negligência do estagiário.<br>
		2- Nessas situações, deverá ser anexada à solicitação de segunda via uma declaração firmada pelo requerente com 
		o relato do ocorrido.<br>
		3- Tambem se excetua o caso de mudança de nome do requerente, que deverá apresentar, com a solicitação, cópia 
		autênticada de documento comprobatório.<br> 
		<table width="100%" border="0" cellpadding="1">
		<tr><td>Matrícula</td><td>Nome</td><td>Lotação</td><td>Data</td><td>Assinatura</td></tr>
		</table> 
        
       <table width="100%" border="0" cellpadding="1">
       		
			<c:forEach var="i" begin="1" end="${crachas}">
			<tr>			
					<td>${i} - ${requestScope[f:concat('matricula',i)]}</td>
					<td>${requestScope[f:concat('nome',i)]}</td>
					<td>${requestScope[f:concat('lotacao',i)]}</td>
					<td>${requestScope[f:concat('data',i)]}</td>
					<td></td>
			</tr>
			</c:forEach>
			
		</table>

			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
				
			<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
			
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>

			</p>
		</blockquote>
	</body>
	</html>
	</mod:documento>
</mod:modelo>
