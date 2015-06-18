<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
Termo de Compromisso e Recebimento segunda via de Crachá
Ultima atualização 13/03/2007
  -->
      
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="SEGUNDA VIA DE CRACHÁ">
			<mod:selecao titulo="Nº de crachás a incluir"
				var="crachas" opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
			<c:forEach var="i" begin="1" end="${crachas}">
				<mod:grupo>
					<mod:texto titulo="Nome" largura="40" var="nome${i}" />
					<mod:texto titulo="Matricula" largura="15" maxcaracteres="10" var="matricula${i}" />
					<mod:data titulo="Data" var="data${i}" />
					<mod:grupo>
						<mod:lotacao titulo="Lotação" var="lotacao${i}" />
					</mod:grupo>
				</mod:grupo>
				
			</c:forEach>
		</mod:grupo>	
	</mod:entrevista>

	<mod:documento>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp"/>	
			
		<body>
		
		<p align="center"><b> TERMO DE COMPROMISSO E RECEBIMENTO DE SEGUNDA VIA DO CRACHÁ   </b></p>
	
		<p style="TEXT-INDENT: 2cm" align="justify">
		Recebi, nesta data, o crahá de identificação no âmbito da <b>JUSTIÇA FEDERAL DE 1º INSTÂNCIA - <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose></b>, e me comprometo, em caso de perda, roubo ou furto a comunicar, <b>incontinenti</b>
		à Subsecretaria de Gestão de Pessoas, bem como, em caso de desligamento, devolvê-lo àquela Subsecretaria - Seção 
		de Avaliação de Desempenho.
		</p>	
			
		<p style="TEXT-INDENT: 2cm" align="justify">
		Declaro, outrossim que, em caso de pedido de 2ª via do mesmo, deverei efetuar o pagamento, diretamente na Subsecretaria
		de Planejamento, Orçamento e Finanças, do valor para sua confecção.
		</p>
		
		<b>Observação:</b>
		<ul type="square">
			<li>Excetuam-se do pagamento acima descrito os casos de roubo, furto e inutilização acidental do cracha, quando não se caracterizar
		, neste último caso, negligência do estagiário.</li>
			<li>Nessas situações, deverá ser anexada à solicitação de segunda via uma declaração firmada pelo requerente com 
		o relato do ocorrido.</li>
			<li>Tambem se excetua o caso de mudança de nome do requerente, que deverá apresentar, com a solicitação, cópia 
		autênticada de documento comprobatório. </li> 
		</ul>
		<br>
		<p style="TEXT-INDENT: 2cm" align="center">
		<b> ESTAGIÁRIOS ÁREA ADMINISTRATIVA - NÍVEL MÉDIO <br>
			Localidade: RIO BRANCO </b>
		</p>
		<br>
		
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
		<tr><td  bgcolor="#FFFFFF" width="15%" align="center">Matrícula</td><td bgcolor="#FFFFFF" width="35%" align="center">Nome</td><td bgcolor="#FFFFFF" width="10%" align="center">Lotação</td><td bgcolor="#FFFFFF" width="15%" align="center">Data</td><td bgcolor="#FFFFFF" width="25%" align="center">Assinatura</td></tr>
		
			<c:forEach var="i" begin="1" end="${crachas}">
			<tr>			
					<td bgcolor="#FFFFFF" width="15%" align="center">${requestScope[f:concat('matricula',i)]}</td>
					<td bgcolor="#FFFFFF" width="35%" align="center">${requestScope[f:concat('nome',i)]}</td>
					<td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat(f:concat('lotacao',i),'_lotacaoSel.sigla')]}</td>
					<td bgcolor="#FFFFFF" width="15%" align="center">${requestScope[f:concat('data',i)]}</td>
					<td bgcolor="#FFFFFF" width="25%"></td>
			</tr>
			</c:forEach>
		</table>	
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
	</body>
	</html>
	</mod:documento>
</mod:modelo>
