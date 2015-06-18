<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- este modelo trata de
solicitação de inscrição para capacitação sem onus -->
<script type="text/javascript">

	function alerta(){

		var x = document.getElementById("qtd").value;

		var z = parseFloat(x);

		return alert(z);

	}

</script>


<body onunload="alerta()">

<mod:modelo>

	 <mod:entrevista>
		
			
			
			<mod:grupo titulo="Contrato de Aquisição em Geral">
			
									
				<mod:monetario titulo="Termo de contrato de Nº" largura="12" maxcaracteres="10" var="termo" verificaNum="sim"/>
			
				<mod:texto	titulo="Fornecimento de" largura="20" var="forn" />
			
				<mod:monetario titulo="Quantidade" largura="8" maxcaracteres="6" var="quantidade" verificaNum="sim"/>
			
				<mod:monetario titulo="Ano" largura="6" maxcaracteres="4" var="ano" verificaNum="sim"/>
			
				<mod:grupo titulo="Representante">
			
						<mod:texto	titulo="Nome" largura="20" var="represent" />
							
						<mod:monetario titulo="Identidade" largura="11" maxcaracteres="9" var="ident_represent" verificaNum="sim"/>
				
						<mod:monetario titulo="CPF" largura="12" maxcaracteres="10" var="cpf_represent" verificaNum="sim"/>
						
				</mod:grupo>	
			
			
				<mod:grupo titulo="Processo Administrativo">
			
					<mod:monetario titulo="Nº 1" largura="4" maxcaracteres="3" var="n1" verificaNum="sim"/>
			
					<mod:monetario titulo="Nº 2" largura="4" maxcaracteres="3" var="n2" verificaNum="sim"/>
			
					<mod:monetario titulo="Nº 3" largura="4" maxcaracteres="3" var="n3" verificaNum="sim"/>
			
				</mod:grupo>
							
					<mod:grupo titulo="Juiz Federal">
			
						<mod:texto	titulo="Dr" largura="20" var="jfdr"  />
							
						<mod:monetario titulo="Identidade" largura="11" maxcaracteres="9" var="ident" verificaNum="sim"/>
				
						<mod:monetario titulo="CPF" largura="12" maxcaracteres="11" var="cpf" verificaNum="sim"/>
						
					</mod:grupo>
				
					<mod:grupo titulo="Itens">				
					
						<mod:texto	titulo="Quantidade de itens" largura="9" maxcaracteres="7" var="qtd" relertab="sim"  />	
						
						
			
						<mod:grupo>
						
							<c:forEach var="i" begin="1" end="${qtd}">
								
								<mod:grupo>								
								
									<mod:texto titulo="Descrição${i}" largura="20" maxcaracteres="18" var="desc${i}"/>							
								
									<mod:monetario titulo="Quantidade${i}" largura="6" maxcaracteres="4" var="qtd_item${i}" verificaNum="sim"/>									
								
									<mod:monetario titulo="Valor${i}" largura="6" maxcaracteres="4" var="valor${i}" verificaNum="sim"/>
								
								   
								</mod:grupo>
									  						
		  									
							</c:forEach>
													
											
						</mod:grupo>
											
					</mod:grupo>	
				
					<mod:grupo titulo="Entrega">
					
						<mod:texto	titulo="Local da entrega" largura="20" var="ent"  />
						
						<mod:texto	titulo="Rua" largura="20" var="rua" />				
						
						<mod:texto	titulo="Seção" largura="20" var="secao"  />
						
						<mod:monetario titulo="Prazo (em dias)" largura="6" maxcaracteres="4" var="prazo" verificaNum="sim"/>					
					
										
					</mod:grupo>
					
						<mod:grupo titulo="Empresa">
					
						<mod:texto	titulo="Nome" largura="20" var="emp"  />
						
						<mod:texto	titulo="Endereço" largura="20" var="end"  />
						
						<mod:monetario titulo="Telefone" largura="10" maxcaracteres="8" var="tel" verificaNum="sim"/>
						
						<mod:monetario titulo="Fax" largura="10" maxcaracteres="8" var="fax" verificaNum="sim"/>
						
						<mod:monetario titulo="C.N.P.J." largura="16" maxcaracteres="14" var="cnpj" verificaNum="sim"/>
						
					</mod:grupo>
					
					
								
					<mod:grupo titulo="Pregão">
			
						<mod:monetario titulo="Numero" largura="12" maxcaracteres="10" var="pregao" verificaNum="sim"/>
						
						<mod:monetario titulo="Ano" largura="6" maxcaracteres="4" var="ano_preg" verificaNum="sim"/>
			    
					</mod:grupo>
					
					<mod:grupo titulo="Garantia contratual">
					
						<mod:monetario titulo="Valor" largura="8" maxcaracteres="6" var="garant_val" verificaNum="sim" formataNum="sim"/>
						
						<mod:monetario titulo="Tempo" largura="8" maxcaracteres="6" var="garant_mes" verificaNum="sim" />
					
					</mod:grupo>
					
										
				</mod:grupo>
					
	</mod:entrevista>
	
	
	
		
	<mod:documento>
	
	<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp"/>
	
	<p style="TEXT-INDENT: 4cm" align="left">/TERMO DE CONTRATO Nº${termo}/${ano}<br>
	CONTRATO DE FORNECIMENTO DE ${forn} ,<br>
	QUE ENTRE SI FAZEM A <br>
	JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE JANEIRO <br>
	NO RIO DE JANEIRO E A EMPRESA ${emp}<br>
	PROCESSO N° ${n1}/${n2}/${n3} - EOF</p>
	
	<p style="TEXT-INDENT: 2cm" align="justify">A Justiça Federal de 1º Grau no Rio de Janeiro, com sede na Av. Rio 
	Branco, 243 - Anexo I - 14° andar, Centro/RJ, inscrita no C.N.P.J. sob o nº 05.424.540/0001-16, neste ato 
	representada pelo Dr. ${jfdr}, Juiz Federal - Diretor do Foro, identidade nº ${ident},  
	CPF: ${cpf}, doravante denominada CONTRATANTE, e a empresa ${emp}, estabelecida na ${end},
	 TEL: ${tel}, FAX: ${fax} ,inscrita no C.N.P.J sob o n° ${cnpj}, representada neste ato pelo 
	 Sr.${represent} , cédula de identidade nº ${ident_represent},  CPF: ${cpf_represent}, doravante denominada 
	 CONTRATADA, tendo em vista o decidido no Processo Administrativo n° ${n1}/${n2}/${n3} -EOF, através de 
	 despacho do Exm° Sr Juiz Federal - Diretor do Foro (fls. .......... dos autos), firmam o presente contrato, em 
	 conformidade com o disposto na Lei nº 10.520, de 17/07/2002, Decreto nº 3.555, de 08/08/2000 e, subsidiariamente, 
	 a Lei nº 8.666, de 21/06/93 e suas alterações, mediante as cláusulas e condições a seguir:</p>
	
	<h2>CLÁUSULA PRIMEIRA DO OBJETIVO</h2>
	
	<p style="TEXT-INDENT: 2cm" align="justify">1.1 - Constitui objeto do presente contrato o fornecimento de 
	${quantidade} (por extenso) ${forn}, conforme Termo de Referência do Pregão nº ${pregao}/${ano}, que faz parte integrante 
	deste contrato.</p>
	OBS:<b>O objeto será alterado de acordo com o resultado da licitação.</b>
	
	<h2>CLÁUSULA SEGUNDA - DO RECEBIMENTO E INSTALAÇÃO(QUANDO HOUVER) DOS EQUIPAMENTOS</h2>
	
	<p style="TEXT-INDENT: 2cm" align="justify">2.1	A Contratada fará a entrega dos equipamentos na ${secao},
	 situada na Rua ${rua}, no prazo de ${prazo} dias, após a assinatura do contrato.
OBS: O prazo de entrega será preenchido nos termos da proposta da licitante vencedora, observado o prazo máximo 
estipulado na Especificação.
	<p style="TEXT-INDENT: 2cm" align="justify">2.2 - O equipamento será recebido provisoriamente para inspeção
	, visando a comprovar o atendimento às especificações técnicas, no ato da entrega.</p>
	<p style="TEXT-INDENT: 2cm" align="justify">2.3 - No caso de sinais externos de avaria, o equipamento deverá
	ser imediatamente substituído pela Contratada.</p>
	<p style="TEXT-INDENT: 2cm" align="justify">2.4 - O objeto do presente contrato será recebido definitivamente, por servidor ou Comissão nomeada pela 
	Administração, no prazo de até 15 (quinze) dias úteis, contados a partir do recebimento provisório, após 
	verificada e comprovada a conformidade do material com as especificações exigidas no Edital do Pregão</p>
	<p style="TEXT-IDENT: 2cm" aligen="justify">2.4.1 - A nota fiscal encaminha a pela contratad será atestada na mesma
	 data da expedição do termo de Recebimento Definitivo</p>
	 <h2>CLÁUSULA TERCEIRA - DAS OBRIGAÇÕES DA CONTRATADA:</h2>
	<p style="TEXT-INDENT: 2cm" align="justify">3.1 - A empresa contratada é responsável por:</p>
	<p style="TEXT-INDENT: 3cm" align="justify">a) qualquer acidente que venha a ocorrer com seus empregados e por danos que estes provoquem à Justiça Federal ou a 
terceiros, não excluindo essa responsabilidade a fiscalização ou o acompanhamento pela Contratante;</p>
		<p style="TEXT-INDENT: 3cm" align="justify">b) todos os encargos previdenciários e obrigações sociais previstos na 
	legislação social trabalhista em vigor relativos a seus funcionários, vez que os mesmos não manterão nenhum vínculo 
	empregatício com a Seção Judiciária do Rio de Janeiro;</p>
	<p style="TEXT-INDENT: 3cm" align="justify">c) todas as providências e obrigações estabelecidas na legislação 
	específica de acidentes de trabalho, quando, em ocorrência da espécie, forem vítimas os seus funcionários quando da 
	realização dos serviços contratados, ou em conexão com eles;</p>
	<p style="TEXT-INDENT: 3cm" align="justify">d) todos os encargos de eventual demanda trabalhista, civil ou penal, 
	relacionados à prestação dos serviços, 
	originalmente ou vinculada por prevenção, conexão ou continência;</p>
	<p style="TEXT-INDENT: 3cm" align="justify">e) todos os encargos fiscais e comerciais decorrentes do presente 
	contrato;</p>
	<p style="TEXT-INDENT: 3cm" align="justify">f) todas as despesas referentes  ao suporte de serviços, durante  o prazo 
	de garantia, bem como pelo transporte de técnicos e equipamentos necessários ao cumprimento do presente contrato, sem
	ônus para a contratante.</p>
	<p style="TEXT-INDENT: 3cm" align="justify">e) todos os encargos fiscais e comerciais decorrentes do presente 
	contrato;</p>
	<p style="TEXT-INDENT: 3cm" align="justify">f) todas as despesas referentes  ao suporte de serviços, durante  o prazo 
	de garantia, bem como pelo transporte de técnicos e equipamentos necessários ao cumprimento do presente contrato, sem 
	ônus para a contratante.</p>
	<p style="TEXT-INDENT: 2cm" align="justify">3.2 - A inadiplência da contratada, com referência aos encargos sociais,
	comerciais e fiscais, não transfere a responsabilidade por seu pagamento à Administração da Seção Judiciária do Rio de
	Janeiro, razão pela qual a contratada renuncia expressamente a qualquer vínculo de solidariedade, ativa ou passiva, com
	a SJRJ.</p>
	<p style="TEXT-INDENT: 2cm" align="justify">3.3 - A contratada deverá cumprir, ainda com as demis obrigações constantes 
	da especificação elaborada pela Subsecretaria de Informática, que integra o presente contrato.</p>
	<h2>CLÁUSULA QUARTA - DAS OBRIGAÇÕES DA CONTRATANTE:</h2>
	<p style="TEXT-INDENT: 2cm" align="justify">4.1 - Caberá à Contratante:
	 <p style="TEXT-INDENT: 3cm" align="justify">4.1.1 - permitir acesso, aos empregados da Contratada, às instalações da 
	 Contratante para a entrega/execução dos serviços constantes do objeto.</p>
	 
	<p style="TEXT-INDENT: 3cm" align="justify">4.1.2 - prestar as informações e os esclarecimentos que venham a ser 
	solicitados pelos empregados da Contratada.</p>
	<p style="TEXT-INDENT: 3cm" align="justify">4.1.3 - rejeitar qualquer material/serviço executado em desacordo 
	com as Especificações do Pregão nº ${pregao}/${ano}.</p>
	<p style="TEXT-INDENT: 3cm" align="justify">4.1.4 - solicitar que seja substituído o material que não atender
	 às Especificações do Pregão nº ${pregao}/${ano}</p>
	<p style="TEXT-INDENT: 3cm" align="justify">4.1.5 - atestar as faturas correspondentes, por intermédio da 
	Fiscalização, designada pela Administração.</p>
	<h2>CLÁUSULA QUINTA - DO PREÇO E DO PAGAMENTO:</h2>
	<p style="TEXT-INDENT: 2cm" align="justify">5.1 - A Contratante pagará à Contratada pelo fornecimento objeto deste Contrato, o valor total de 
	R$ ${valor} , inclusos todos os impostos e taxas vigentes, conforme tabela a seguir:</p>
	
        
       
       
       <table width="100%" border="0" cellpadding="1">       		
		
		  <c:forEach var="i" begin="1" end="${qtd}">
		
		 	<tr>								
		
			  <td>${requestScope[f:concat('desc',i)]}</td>
		
			  <td>${requestScope[f:concat('qtd_item',i)]}</td>
		
			  <td>${requestScope[f:concat('valor',i)]}</td>	
			 	
			</tr>
		
		  </c:forEach>					
        
        </table>
        
        		
		<p style="TEXT-INDENT:2cm" align="justify">5.2 - As Notas Fiscais/Faturas deverão ser entregues 
		na Rua ${rua}, para serem devidamente atestadas.</p>
		
		<p style="TEXT-INDENT:2cm" align="justify">5.3 - O pagamento à Contratada será efetivado, por crédito em conta corrente, mediante ordem bancária, 
		até o 10º dia útil após a apresentação da fatura ou nota fiscal discriminativa do material entregue, 
		devidamente atestada por servidor ou Comissão nomeada pela Administração, salvo eventual atraso de distribuição
		 de recursos financeiros efetuados pelo Conselho da Justiça Federal, decorrente de execução orçamentária.</p>
		 
		 <p style="TEXT-INDENT:3cm" align="justify">5.3.1 - No período acima não haverá atualização financeira.</p>
		 
		 <p style="TEXT-INDENT:3cm" align="justify">5.3.2 - A fatura/nota fiscal deverá ser apresentada em 02 (duas) vias.</p>
		 
		 <p style="TEXT-INDENT:3cm" align="justify">5.3.3 - Será considerada como data do pagamento a data da emissão
		  da Ordem Bancária.</p>
		 <p style="TEXT-INDENT:2cm" align="justify">5.4 - Caso seja necessaria a retificação da fatura por culpa da 
		 contratada a fluência do prazo de 10(dez) dias úteis será suspensa, reiniciando-se a contagem a partir da
		 reapresentação da fatura retificada</p>
		 
		 <p style="TEXT-INDENT:2cm" align="justify">5.5 - A Contratante reserva-se o direito de não efetuar o pagamento se, no ato da atestação, o material não
		  estiver em perfeitas condições ou de acordo com as especificações apresentadas e aceitas pela Contratada.
		  
		 <p style="TEXT-INDENT:2cm" align="justify">5.6 - A Seção Judiciária do rio de janeiro poderá deduzir da importância a pagar os valores correspondentes a 
		 multas ou indenizações devidas pela contratada nos termos da presente ajuste.</p>
		 
		 <p style="TEXT-INDENT:2cm" align="justify">5.7 - A Contratada deverá confirmar, quando da apresentação da nota
		  fiscal à Contratante, a regularidade perante a Seguridade Social e ao Fundo de Garantia de Tempo de Serviço,
		   através da apresentação da CND e do CRF, dentro das respectivas validades, sob pena de não pagamento do
		    material fornecido e de rescisão contratual, em atendimento ao disposto no § 3º. do art. l95 da 
		    Constituição Federal, no art. 2º da Lei 9.012/95, e nos art. 55, inciso VIII e 78, inciso I, ambos da 
		    Lei 8.666/93.</p>
		    
		  <p style="TEXT-INDENT:2cm" align="justify">5.8 - Na ocasião da entrega da nota fiscal, a contratada deverá
		   comprovar a condição de optante pelo SIMPLES (Sistema Integrado de Pagamento de Impostos e Contribuições das
		    Microempresas e Empresas de pequeno Porte), mediante a apresentação da declaração indicada em ato 
		    normativo da Secretaria da Receita Federal e dos documentos, devidamente autenticados, que comprovem ser o
		     signatário da referida declaração representante legal da empresa.</p>
		     
		  <p style="TEXT-INDENT:2cm" align="justify">As pessoas jurídicas não optantes pelo SIMPLES e aquelas que ainda
		   não formalizaram a opção sofrerão a retenção de impostos/contribuições por esta Seção Judiciária no momento
		    do pagamento, conforme disposto no art. 64 da Lei nº 9.430, de 27/12/96, regulamentado por ato normativo
		     da Secretaria da Receita Federal.</p>
		
		   <h2>CLÁUSULA SEXTA - DA GARANTIA CONTRATUAL:</h2>
		   
		   <p style="TEXT-INDENT:2cm" align="justify">6.1 - A contratada presta, neste ato, garantia contratual, nos termos do art. 56, parágrafo 1º , da Lei 8.666/93, no valor de 
		   R$ ${garant_val}, equivalente a 5% (cinco por cento) do valor global deste contrato.</p>
		   
		   <h2>CLÁUSULA SÉTIMA - DA DOTAÇÃO ORÇAMENTÁRIA:</h2>
		   
		   <p style="TEXT-INDENT:2cm" align="justify">7.1 - As despesas decorrentes do objeto deste contrato, correrão
		    à conta dos recursos consignados à Seção Judiciária do Rio de Janeiro, para o corrente exercício, conforme
		     o especificado abaixo:</p>
		     
		   <p style="TEXT-INDENT:4cm" align="justify">	Programa de Trabalho:</p> 
	       <p style="TEXT-INDENT:4cm" align="justify">  Elemento de Despesa:</p>
           <p style="TEXT-INDENT:4cm" align="justify">	Nota de Empenho:</p>
           
           <h2>CLÁUSULA OITAVA - DO PRAZO DE GARANTIA E DA ASSISTÊNCIA TÉCNICA:</h2>
           
           <p style="TEXT-INDENT:2cm" align="justify">8.1 - O prazo de garantia dos equipamentos é de ${garant_mes}
            meses, contados do recebimento definitivo dos mesmos.</p>
            
            <p style="TEXT-INDENT:2cm" align="justify"><b>* O prazo de garantia será o constante na proposta da licitante 
            vencedora, respeitado o prazo mínimo constante no Termo de Referência, que integra o presente Contrato.<b></p>
           
           <p style="TEXT-INDENT:2cm" align="justify">8.2 - A Contratada deverá dar atendimento total durante o período
            de garantia dirigindo-se ao local onde o equipamento estiver instalado na Seção Judiciária do Rio de 
            Janeiro, conforme constante no Termo de Referência do Pregão nº${pregao}/ ${ano_preg}, que integra o presente 
            contrato.</p>
            <p style="TEXT-INDENT:2cm" align="justify">8.2.1 - O prazo de garantia será contado a partir de entrega e
            recebimento definitivo de equipamento, durante o qual, caso apresente algum defeito deverá ser substituido 
             por outro novo de mesma característia ou superior </p>
             
             <h2>CLÁUSULA NONA - DAS PENALIDADES:</h2>
             
             <p style="TEXT-INDENT:2cm" align="justify">9.1 - O não cumprimento pela contratada de qualquer uma das 
             obrigações, dentro dos prazos estabelecidos por este contrato, sujeitá-la-á às penalidades previstas nos 
             artigos 86 a 88 da Lei n° 8.666/93;</p>
             
             <p style="TEXT-INDENT:2cm" align="justify">9.2 - As penalidades a que está sujeita a contratada 
             inadimplente, nos termos da Lei no 8.666/93, são as seguintes:</p>
             
             <p style="TEXT-INDENT:3cm" align="justify">a) Advertência;
             <p style="TEXT-INDENT:3cm" align="justify">b) Multa;
             <p style="TEXT-INDENT:3cm" align="justify">c) Suspenção temporária de participar emlicitação e 
             impedimento em contratar com a Administração por prazo não superior a 2(dois) anos.
             
             <p style="TEXT-INDENT:2cm" align="justify">9.3 - A recusa injustificada em assinar o Contrato, aceitar ou
              retirar o instrumento equivalente, dentro do prazo estabelecido pela Administração, sujeita o 
              adjudicatário à penalidade de multa de até 10% (dez por cento) sobre o valor da adjudicação, 
              independentemente da multa estipulada no subitem 9.4.2.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.4 - A inexecução total ou parcial do contrato poderá 
              acarretar, a critério da Administração, a aplicação das multas, alternativamente:</p>
              <p style="TEXT-INDENT:3cm" align="justify">9.4.1 - Multa compensatória de até 30% (trinta por cento)
               sobre o valor equivalente à obrigação inadimplida.</p>
              <p style="TEXT-INDENT:3cm" align="justify">9.4.2 - Multa correspondente à diferença entre o valor total porventura resultante de nova 
              contratação e o valor total que seria pago ao adjudicatário.</p>
              <p style="TEXT-INDENT:3cm" align="justify">9.4.3 - Multa de 50% (cinqüenta por cento) sobre o valor 
              global do contrato, no caso de inexecução total do mesmo.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.5 - A atualização dos valores correspondentes à multa estabelecida no item 9.4  far-se-á a partir do 1º (primeiro) 
              dia, decorrido o prazo estabelecido no item 9.7.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.6 - Os atrasos injustificados no cumprimento das obrigações 
              assumidas pela contratada sujeita-la à multa diária, até a data do efetivo adimplemento, de 0,3% 
              (três décimos por centavo calculada a bese de juros compostos sem prejuízodas demais penalidades 
              previstas na Lei n° 8.666/93</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.6.1 - A multa moratória estabelecida ficará limitada à 
              estipulada para inexecução parcial ou total do contrato (subitem 9.4.1).</p>
              <p style="TEXT-INDENT:3cm" align="justify">9.6.2 - O periodo de atraso será contado em dias corridos.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.7 - A multa deverá ser paga no prazo de 30 (trinta) dias, 
              
              contados da data do recebimento da intimação por via postal, ou da data da juntada aos autos do mandato
              de intimação cumprido, conforme o caso.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.8 - Caso a multa não seja paga no prazo estabelecido no item 9.7, deverá ser descontada dos 
              pagamentos (ou da garantia) do respectivo contrato, ou, ainda, cobrada judicialmente, se for o caso.</p>
              <p style="TEXT-INDENT:3cm" align="justify">9.8.1 - Se a multa for superior ao valor da garantia prestada, além da perda desta, responderá o 
              contratado pela diferença faltante. (este subitem só entra se houver cláusula de garantia).</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.9 - A atualização dos valores correspondentes às multas 
              estabelecidas neste Contrato dar-se-á através do IPCA-E/IBGE, tendo em vista o disposto no art. 1º da 
              Lei nº 8.383, de 30/12/91 ou de outro índice, posteriormente determinado em lei.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.10 - A contagem dos prazos dispostos neste Contrato
              obedecerá ao disposto no art. 110, da Lei nº 8.666/93.</p>
              <p style="TEXT-INDENT:2cm" align="justify">9.11 - Os procedimentos de aplicação e recolhimento das 
              multas foram regulamentadas pela IN nº 24-12, do Egrégio Tribunal Regional Federal da 2ª Região.</p>
             <h2>CLÁUSULA DÉCIMA - DA RESCISÃO:</h2>
             <p style="TEXT-INDENT:2cm" align="justify">10.1 - A inexecução parcial ou total do Contrato enseja a
             sua rescisão, conforme disposto nos artigos 77 a 80 da Lei nº 8.666/93, sem prejuízo da aplicação das 
             penalidades previstas na Cláusula Nona.</p>
             <p style="TEXT-INDENT:2cm" align="justify">10.2 - A rescisão do Contrato poderá ser:</p>
             <p style="TEXT-INDENT:3cm" align="justify">10.2.1 - determinada por ato unilateral e escrito da 
             Administração da Seção Judiciária do Rio de Janeiro, nos casos enumerados nos inciso I a XII e XVII 
             do art. 78 da lei mencionada.</p>
             <p style="TEXT-INDENT:4cm" align="justify">10.2.1.1 - Nos casos previstos nos incisos I a VIII e XI a
             XVII, a Administração notificará a Contratada, através de Ofício, com prova de recebimento.</p>
             <p style="TEXT-INDENT:4cm" align="justify">10.2.1.2 - Nos casos previstos nos incisos IX e X, a rescisão dar-se-á de pleno direito, 
             independentemente de aviso ou interpelação judicial ou extrajudicial.</p>
             <p style="TEXT-INDENT:3cm" align="justify">10.2.2 - amigável, por acordo entre as partes, desde que 
              haja conveniência para a Administração da Seção Judiciária do Rio de Janeiro.</p>
             <p style="TEXT-INDENT:3cm" align="justify">10.2.3 - judicial, nos termos da legislação vigente sobre a
              matéria.</p>
             <p style="TEXT-INDENT:2cm" align="justify">10.4 - A rescisão será determinada na forma do art. 79 da Lei nº 8.666/93.</p>
             <h2>CLÁUSULA DÉCIMA PRIMEIRA - DA DOCUMENTAÇÃO COMPLEMENTAR:</h2> 
             <p style="TEXT-INDENT:2cm" align="justify">11.1 - Fazem parte integrante do presente contrato, independentemente de 
             transcrição, os  seguintes documentos:</p>
             <p style="TEXT-INDENT:3cm" align="justify">a) Pregão n° ${pregao}/ ${ano_preg} e seus anexos.</p>
             <p style="TEXT-INDENT:3cm" align="justify">b) Proposta da Contratada apresentada à Contratante em (data).</p>
             <h2>CLÁUSULA DÉCIMA SEGUNDA - DA PUBLICAÇÃO:</h2>
             	
             <p style="TEXT-INDENT:2cm" align="justify">12.1 - O presente contrato será publicado no Diário Oficial
              da União, na forma de extrato, de acordo com o que determina do parágrafo Único do artigo 61 da Lei n°
              8.666/93.
             <h2>CLÁUSULA DÉCIMA TERCEIRA - DA FISCALIZAÇÃO:</h2>
             <p style="TEXT-INDENT:2cm" align="justify">13.1 - A execução dos serviços será acompanhada e fiscalizada por servidores/comissão, designados pela 
            	Administração.</p>
            <p style="TEXT-INDENT:2cm" align="justify">13.2 - O representante anotará em registro próprio todas as ocorrências relacionadas com a execução 
            dos serviços mencionados, determinando o que for necessário à regularização das faltas ou defeitos 
            observados.</p>
            <p style="TEXT-INDENT:2cm" align="justify">13.3 - As decisões e providências que ultrapassarem a 
            competência do representante serão solicitadas a seus superiores em tempo hábil para a adoção das medidas
             convenientes.</p>
            <p style="TEXT-INDENT:2cm" align="justify">13.4 - O exercício da fiscalização pela contratante não excluirá
            a responsabilidade da contratada.</p>
            <h2>CLÁUSULA DÉCIMA QUARTA - DOS RECURSOS ADMINISTRATIVOS</h2>
            <p style="TEXT-INDENT:2cm" align="justify">14.1 - Aplica-se o disposto no art. 109 da lei 8.666/93.</p>
            <H2>CLÁUSULA DÉCIMA QUINTA - DAS CONSIDERAÇÕES FINAIS:</H2>
            
            <p style="TEXT-INDENT:2cm" align="justify">15.1 - O contrato poderá ser aditado nos termos previstos no 
            art. 65 da Lei nº 8.666/93, com a apresentação das devidas justificativas</p>
            <p style="TEXT-INDENT:2cm" align="justify">15.2 - A Contratada deverá manter durante toda a execução do 
            contrato, em compatibilidade com as obrigações por ela assumidas, todas as condições de habilitação e 
            qualificação exigidas na licitação</p>
            <h2>CLÁUSULA DÉCIMA SEXTA - DO FORO:</h2>
            <p style="TEXT-INDENT:2cm" align="justify">16.1 - Para dirimir as questões oriundas do presente contrato, 
            fica eleito o Foro da Justiça Federal - Seção Judiciária do Rio de Janeiro.</p>
            <p style="TEXT-INDENT:2cm" align="justify">E por estarem ajustados, é lavrado o presente termo de contrato, 
            extraído em 03 (três) vias de igual teor e forma, que depois de lido e achado conforme vai assinado pelas 
            partes contratantes.</p>
            
            <p style="TEXT-INDENT:5cm" align="justify">Rio de janeiro,&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
            &nbsp&nbsp de &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp de &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
            &nbsp .<br><br>
            
           &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ____________________________________________
           &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE JANEIRO<br><br>             
           &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ____________________________________________
           &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp EMPRESA
                      
	</mod:documento>

</mod:modelo>
</body>
