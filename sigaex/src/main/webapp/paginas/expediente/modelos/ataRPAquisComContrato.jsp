<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- este modelo trata de
solicitação de inscrição para capacitação sem onus -->

<mod:modelo>
	 <mod:entrevista>
		
			
			<mod:grupo titulo="Ata de registro de preços">
				<mod:texto	titulo="Quantidade de itens" largura="3" var="qtd" relertab="sim" />
							
				
		<c:forEach var="i" begin="1" end="${qtd}">
					<mod:grupo>
							<mod:texto titulo="Item" largura="20" var="item${i}" />
							
							<mod:texto titulo="descrição" largura="20" var="descricao${i}" />
							
							<mod:texto titulo="Quantidade" largura="5" maxcaracteres="3" var="quantidade${i}" />
																					
							<mod:texto titulo="Preço" largura="8" maxcaracteres="6" var="preco${i}" />
							
							
					
					</mod:grupo>
					
				
			</c:forEach>
			<mod:grupo titulo="Representante">
							
				<mod:texto titulo="Nome"  var="nome" />
				
				<mod:monetario titulo="Identidade" largura="11" maxcaracteres="9" var="ident" verificaNum="sim"/>
				
				<mod:monetario titulo="CPF" largura="12" maxcaracteres="10" var="cpf" verificaNum="sim"/>		
					
			</mod:grupo>
			<mod:grupo titulo="Pregão">
			
				<mod:monetario titulo="Numero" largura="12" maxcaracteres="10" var="pregao" verificaNum="sim"/>
			    
				
				<mod:monetario titulo="validade" largura="4" maxcaracteres="2" var="validade" verificaNum="sim"/>
				
			</mod:grupo>
			
				<mod:grupo titulo="Empresas">
				
				<mod:texto	titulo="Quantidade de empresas" largura="5" maxcaracteres="3" var="qtd_emp" relertab="sim" />
			
				<c:forEach var="i" begin="1" end="${qtd_emp}">
				
					<mod:grupo>
					
						<mod:texto titulo="Nome ${i}" var="empresa${i}"/> 
						
						<mod:texto titulo="Representante ${i}" var="repr_emp${i}"/>
						
						<mod:monetario titulo="CPF representante" largura="14" maxcaracteres="11" var="repr_cpf${i}" verificaNum="sim"/>
						
						 
					
					</mod:grupo>
				
				</c:forEach>
			
			</mod:grupo>
			
			<mod:data titulo="Data" var="data"/>
				
		</mod:grupo>				
		
	</mod:entrevista>
	
	<mod:documento>
	
	<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp"/>
	
	<h1> ATA DE REGISTRO DE PREÇOS Nº_____/20_____
		PROCESSO Nº____/_____/_____-EOF
	
	<p style="TEXT-INDENT: 2cm" align="justify"> A justiça Federal de 1º Grau no Rio de Janeiro, com sede na Av. Rio Branco, 243 - Anexo
	14º andar, Centro/RJ, inscrita no C.N.P.J. sob o nº 05.424.540/0001-16, neste ato representada pelo Dr ${nome}, juiz Federal - Diretor do Foro,
	identidade nº ${ident}, CPF${cpf} doravante denominada JUSTIÇA FEDERAL, resolve,
	em face das propostas no<b> Pregão nº&nbsp ${pregao}/ ,REGISTRAR OS PREÇOS  para contratação<b> da(s) empresa(s) denominada(s) FORNECEDORA(S),
	conforme especificado no Anexo 1 do Edital, que passa a fazer parte desta, tendo sido o(s) referido(s) preço(s) oferecido(s) pela(s) empresa(s)
	 cuja(s) proposta(s) foi(ram) classificada(s) em primeiro(s) lugare(es) no certame acima numerado, como segue,em conformidade
	 com o disposto na lei Nº10.520, de 17/07/2002 Decreto nº 3.555, de 08/08/2000 e nº 3931, de 19/09/2001 e, subsidiariamente, a lei nº 8.666, de 
	 21/06/93 e suas alterações , mediante as cláusulas e condições a seguir:</P>  
	
	
	<table width="100%" border="0" cellpadding="1">
	<tr>
		<td>item</td><td>Preço Unitário</td><td>Quantidade</td><td>Descrição</td>
	</tr>
	
	
	<table width="100%" border="0" cellpadding="1">
       		
			<c:forEach var="i" begin="1" end="${qtd}">
			<tr>	
							
					<td>${requestScope[f:concat('preco',i)]}</td>
					<td>${requestScope[f:concat('quantidade',i)]}</td>
					<td>${requestScope[f:concat('descricao',i)]}</td>
					<td>${requestScope[f:concat('item',i)]}</td>
					
					
								
					
			</tr>
			</c:forEach>
						
		</table>
		
		<table width="100%" border="0" cellpadding="1">
		
			<td>A empresa vencedora:</td>
			
		</table>
		
		
		
		<H2>CLÁUSULA PRIMEIRA - DO OBJETIVO</H2>
		
		<p style="TEXT-INDENT: 2cm" align="justify">1.1. Contratação de empresa para fornecimento de produtos, conforme especificações constante do termo de Referência do Edital do Pregão 
		nº${pregao}/.</p>
		    
		<h2>CLÁUSULA SEGUNDA - DA VALIDADE DOS PREÇOS</h2>
		
		<p style="TEXT-INDENT: 2cm" align="justify">2.1. A presente Ata de Registro de Preços terá validade de ${validade} 
		meses, apartir da sua assinatura.</p> 
		
		<p style="TEXT-INDENT: 2cm" align="justify"><b>PARAGRAFO ÚNICO</b>: A presente Ata poderá ser prorrogada, na forma autorizada 
		pelo art. 4º Decreto nº 3.931/2001.</p>
		
		<p style="TEXT-INDENT: 2cm" align="justify">2.2. Durante o prazo de validade desta Ata de Registro de Preços, a Justiça Federal não será obrigada a firmar as contratações que deles poderão adivir
		, facultando-se a realização de licitação específica para aquisição pretendida, sendo assegurado ao beneficiário do registro preferência de fornecimento
		em igualdade de condições.</p>
		
		<h2>CLÁUSULA TERCEIRA - DA UTILIZAÇÃO DA ATA DE REGISTRO DE PREÇOS</h2>

		 <p style="TEXT-INDENT: 2cm" align="justify">3.1. A presente Ata de Registro de Preços poderá ser usada por orgãos usuários, desde que autorizadas pela JUSTIÇA FEDERAL.

		 <p style="TEXT-INDENT: 2cm" align="justify">3.2. O preço ofertado pela(s) empresa(s) siginatárias da presente Ata de Registro de Preços é o especificado em anexo, de a cordo com  a respectiva
		  classificação no pregão nº${pregao}/.

		 <p style="TEXT-INDENT: 2cm" align="justify">3.3. Em cada fornecimento decorrente desta Ata, serão observadas, quanto ao preço, as cláusulas e condiçoes constantes 
		 do Edital do Pregão Nº ${pregao}/ , que a procedeu e integra o presente instrumento de compromisso.

		 <p style="TEXT-INDENT: 2cm" align="justify">3.4. A cada fornecimento, o preço unitário a ser pago será o constante da proposta
		  apresentada no pregão Nº ${pregao} , pela(s) empresa(s) detentora(s) da presente Ata, as quais também a integram.</p>

		 <h2>CLÁUSULA QUARTA DO LOCAL E PRAZO DA ENTREGA </h2>

		 <p style="TEXT-INDENT: 2cm" align="justify">4.1. A cada fornecimento, o prazo de entrega do produto será acordado pela unidade requisitante 
		 ,não podendo, todavia, ultrapassar o prazo estipulado na especificação do Edital do pregão Nº ${pregao}/.</p>

		 <h2>CLÁUSULA QUINTA - DO PAGAMENTO</h2>

		 <p style="TEXT-INDENT: 2cm" align="justify">5.1. A cada fornecimento o pagamento será efetivado, por crédito em conta corrente, mediante ordem bancária, até o 10(décimo) dia útil após a apresentação da fatura 
		 ou nota fiscal discriminativa do material entregue, devidamente atestada por servidor ou comissão nomeada pelo orgão requisitante, conforme item 16 do Edital.</p>

		 <h2>CLÁUSULA SEXTA - DA AUTORIZAÇÃO PARA AQUISIÇÃO E EMISSÃO DAS ORDENS DE FORNECIMENTO</h2>

		 <p style="TEXT-INDENT: 2cm" align="justify">6.1. As aquisições do objeto da presente Ata de Registro de Preços serão autorizadas, conforme a necessidade, pelo Juiz Federal Diretor do Foro da JUSTIÇA FEDERAL - Seção Judiciária do Rio de Janeiro.</p>

		 <p style="TEXT-INDENT: 2cm" align="justify">6.2. A emissão das ordens de fornecimento, sua retificação ou cancelamento total ou parcial serão igualmente autorizadas pelo Juiz Federal Diretor do Foro da JUSTIÇA FEDERAL - Seção Judiciária do Rio de Janeiro.</p>

		 <h2>CLÁUSULA SÉTIMA - DAS DISPOSIÇÕES FINAIS</h2>

		 <p style="TEXT-INDENT: 2cm" align="justify">7.1. Integram esta Ata, o Edital do pregão Nº${pregao} / e seus anexos, as propostas das empresa(s): <c:forEach var="i" begin="1" end="${qtd_emp}">Empresa${i} - ${requestScope[f:concat('empresa',i)]}; </c:forEach>
		 ,classificadas em primeiro(s) lugar(es), respectivamente, no certame supra numerado.</p>

		 <h2>CLÁUSULA OITAVA DO FORO</h2>

		 <p style="TEXT-INDENT: 2cm" align="justify">8.1 - Pra dirimir as questões decorrentes da utilização da presente Ata de Registro de Preços, fica eleito o Foro da Justiça Federal - Seção Judiciaria do Rio de Janeiro, 
		 sendo os casos omissos resolvido de acordo com a lei Nº 10.520/2002 e decreto 3.555/2000, alterada, e demais normas aplicáveis.</p>

		 <p style="TEXT-INDENT: 2cm" align="justify">E por estarem ajustados, é lavrada a presente Ata, extraída em 03(três) vias de igual teor e forma, que depois de lida e achada conforme vai assinada pelas partes.</p>   
		 
		 <p style="TEXT-INDENT: 5cm" align="justify">Rio de Janeiro,&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    de &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp      de &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp      .</p>
		 
		                                                                        
		                                                                        
		                                                                                     ____________________________________________________________<BR>
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp												JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE JANEIRO<BR>
		 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp     JUIZ FEDERAL - DIRETOR DO FORO<BR>   
		 																																															             
		<br>
		<br>          
		 EMPRESAS:<br><br>
		 
		 <c:forEach var="i" begin="1" end="${qtd_emp}">
		 
		_______________________________________________________<br>
		 ${requestScope[f:concat('empresa',i)]}<br>
		 (Representante: ${requestScope[f:concat('repr_emp',i)]},CPF: ${requestScope[f:concat('repr_cpf',i)]})<br><br>
		
		</c:forEach>
		
	</mod:documento>

</mod:modelo>
