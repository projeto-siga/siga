<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA AFASTAMENTO PARA CURSO DE FORMAÇÃO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>		
		<mod:grupo titulo="DETALHES SOBRE O CURSO DE FORMAÇÃO">
		</mod:grupo>
		<mod:grupo></mod:grupo>
		<mod:texto titulo="Orgão Provedor do Cargo" var="orgao"/>
		<mod:grupo></mod:grupo>
		<mod:texto titulo="Cargo concorrido" var="cargoConcorrido"/>
		<mod:grupo></mod:grupo>
		<mod:data titulo="Data de início" var="dataInicio"/>
		<mod:grupo>
		<mod:data titulo="Data de término" var="dataTermino"/>
		</mod:grupo>
		
		<mod:grupo titulo="DETALHES SOBRE VENCIMENTOS DESTA SEÇÃO JUDICIÁRIA">
		</mod:grupo>
	   <mod:grupo>
		   <mod:selecao titulo="O Funcionario <FONT COLOR=red><b>percebe</b></FONT> 
			   os vencimentos e vantagens referentes ao cargo efetivo desta Seção 
			   Judiciária?<br>
			   <FONT COLOR=blue><b>Não fazendo jus ao auxílio financeiro a ser pago 
			   por aquela Instituição, bem como ao pagamento de <BR>auxílio-transporte
			   e a remuneração da função comissionada ou do cargo em comissão que<BR>
			   eventualmente ocupe nesta Seção Judiciária.</FONT></b>"			
		   var="vencimentosCargo" opcoes="Percebe;Não percebe" />
		</mod:grupo>	   
	</mod:entrevista>	
	<mod:documento>		 
		<mod:valor var="texto_requerimento"><p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		${doc.subscritor.padraoReferenciaInvertido},
		<c:if test="${not empty doc.subscritor.funcaoConfianca}">
		${doc.subscritor.funcaoConfianca.nomeFuncao},</c:if> lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, 
		 
		 
		vem requerer a Vossa Excelência, nos termos do 
		§ 4º do art. 20 da Lei n.º 8.112/90, introduzido pela  Lei n.º 
		9.527/97, c/c art. 14 da Lei n.º 9.624/98, e Resolução n.º 5/2008
		do Conselho da Justiça Federal, <b>AFASTAMENTO PARA PARTICIPAR DE 
		CURSO DE FORMAÇÃO</b>, correspondente à etapa do Processo Seletivo do  
		Concurso Público para o cargo de ${cargoConcorrido}
		a ser realizado pelo(a) ${orgao}, no período de ${dataInicio} até ${dataTermino}.
		</p>
		 
		<p style="TEXT-INDENT: 2cm" align="justify">
		Para tanto, faz a opção de:
		</p>
		<c:if test="${vencimentosCargo == 'Percebe'}">
				<p style="TEXT-INDENT: 2cm" align="justify">	
			    <b>Perceber os vencimentos </b>e vantagens referentes 
			    ao cargo efetivo desta <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas},</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO,</c:otherwise></c:choose> <b>não 
			    fazendo jus </b>ao auxílio financeiro a ser pago por 
			    aquela Instituição, bem como ao pagamento
			    de <i>auxílio-transporte</i> e a remuneração da
			    <i>função comissionada </i>ou do <i>cargo em comissão</i>
			    que eventualmente ocupe nesta <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose>.
			    </p>
		</c:if>
		<c:if test="${vencimentosCargo == 'Não percebe'}">
			   <p style="TEXT-INDENT: 2cm" align="justify">	
			   <b>Não Perceber os vencimentos</b> e vantagens referentes ao cargo
			   efetivo desta <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas},</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO,</c:otherwise></c:choose> <b>fazendo jus</b> ao auxílio 
			   financeiro a ser pago por aquela Instituição.
			   </p>
		</c:if>
		 

			<p style="TEXT-INDENT: 2cm" align="justify">
			Declara, ainda, estar ciente de que o afastamento 
			para participar de curso de formação terá um <b>período 
			definido</b>, que <b>coincidirá com a duração do supracitado 
			curso</b>, findo o qual <b>deverá retornar 
			ao exercício do cargo</b> na <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose>. 
			</p> 
			
	</mod:valor>	
	
	<mod:valor var="texto_requerimento2">	
		<c:if test="${not empty doc.lotaDestinatario and f:lotacaoPorNivelMaximo(doc.lotaDestinatario,4).sigla == 'DIRFO'}">
				<h1 algin="center">Exmo(a). Sr(a). Juiz(a) Federal - Diretor(a) do(a) ${doc.lotaDestinatario.sigla}</h1>
		</c:if>
		<c:if test="${not empty doc.lotaDestinatario and f:lotacaoPorNivelMaximo(doc.lotaDestinatario,4).sigla == 'SG'}">
				<h1 algin="center">Ilmo(a). Sr(a). Diretor(a) da ${doc.lotaDestinatario.descricao}</h1>
		</c:if>
		<br><br><br><br><br><br>
		<center><c:import	url="/paginas/expediente/modelos/inc_tit_termoCompromisso.jsp" /></center>
		 <p style="TEXT-INDENT: 2cm" align="justify">
		 
		 ${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		 ${doc.subscritor.padraoReferenciaInvertido},<c:if test="${not empty doc.subscritor.funcaoConfianca}">
		 ${doc.subscritor.funcaoConfianca.nomeFuncao},</c:if>
		 lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, firma o seguinte compromisso:
		 </p>

 		 
		 <c:if test="${vencimentosCargo == 'Percebe'}">
				 <p style="TEXT-INDENT: 2cm" align="justify">
				 <b>
				 Apresentar, ao término do curso, documento emitido 
				 pelo órgão promotor do evento que não percebeu o referido auxílio.
				 </b>
				 </p> 
		 </c:if>
		 
		<c:if test="${vencimentosCargo == 'Não percebe'}">
				 <p style="TEXT-INDENT: 2cm" align="justify">
				 <b>
				 Apresentar comprovante de recolhimento de contribuição
				 para a Previdência Social do Servidor Público, objetivando
				 o cômputo do tempo para fins de aposentadoria e disponibilidade.
				 </b>
				 </p>	 	
	 	</c:if>
	 	<p style="TEXT-INDENT: 2cm" align="justify">
	 	<b>Declara estar ciente de que tais documentos devem ser apresentados, impreterivelmente, até o pedido de vacância.</b>
	 	</p>
	 	
	 	<p style="TEXT-INDENT: 0cm" align="center">${doc.dtExtenso}</p>
	    <br/>
	 	<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		<%--<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />--%>	
	</mod:valor>
	
		</mod:documento>
	
</mod:modelo>
