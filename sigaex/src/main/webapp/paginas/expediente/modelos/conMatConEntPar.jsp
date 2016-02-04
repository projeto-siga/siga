<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- 
     Modelo : Contrato de Material de Consumo - Entrega Parcelada
     Data da Criacao : 05/02/2007
     Ultima alteracao : 05/02/2007 
-->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Detalhes do Contrato" />
		<mod:grupo>
			<mod:monetario titulo="Termo de Contrato Nº" var="numeroTermo"
				largura="10" maxcaracteres="8" verificaNum="sim" />
			<mod:monetario titulo="Ano" var="anoTermo" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:monetario titulo="Processo Nº" var="numProcesso" largura="8"
				maxcaracteres="6" verificaNum="sim" />
			<mod:monetario titulo="Ano" var="anoProcesso" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:grupo>
				<mod:monetario titulo="Nº. do Pregão" var="numeroPregao"
					largura="12" maxcaracteres="10" verificaNum="sim" />
				<mod:monetario titulo="Ano do Pregão" var="anoPregao" largura="6"
					maxcaracteres="4" verificaNum="sim" />
				<mod:monetario titulo="Nº. das Folhas do Auto"
					var="numeroFolhaAutos" largura="12" maxcaracteres="10"
					verificaNum="sim" />
			</mod:grupo>
			<mod:grupo titulo="Dados do Juiz" />
			<mod:texto titulo="Juiz Federal - Diretor do Foro" var="nomeJuiz"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:monetario titulo="Documento de Identidade" var="docIdentidade"
				largura="12" maxcaracteres="10" verificaNum="sim" />
			<mod:texto titulo="Orgão Emissor" var="orgaoEmissor" largura="12"
				maxcaracteres="10" />
			<mod:monetario titulo="CPF" var="cpfJuiz" largura="13"
				maxcaracteres="11" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo titulo="Dados da Empresa Fornecedora" />
		<mod:grupo>
			<mod:texto titulo="Empresa Fornecedora" var="nomeEmpresa"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Endereço Comercial" var="enderecoEmpresa"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Bairro" var="bairroEmpresa" largura="34"
				maxcaracteres="30" />
			<mod:texto titulo="Estado" var="estadoEmpresa" largura="34"
				maxcaracteres="30" />
		</mod:grupo>
		<mod:grupo>
			<mod:monetario titulo="CGC/CNPJ" var="cgcEmpresa" largura="16"
				maxcaracteres="14" verificaNum="sim" />
			<mod:numero titulo="Telefone" var="telefoneEmpresa" largura="10"
				maxcaracteres="8" />
			<mod:numero titulo="FAX" var="faxEmpresa" largura="10"
				maxcaracteres="8" />
		</mod:grupo>
		<mod:grupo titulo="Dados do Representante" />
		<mod:grupo>
			<mod:texto titulo="Nome do Representante" var="nomeRepresentante"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:numero titulo="Documento de Identidade"
				var="identidadeRepresentante" largura="12" maxcaracteres="10" />
			<mod:texto titulo="Orgão Emissor" var="orgaoEmissorRepresentante"
				largura="12" maxcaracteres="10" />
			<mod:numero titulo="CPF" var="cpfRepresentante" largura="13"
				maxcaracteres="11" />
		</mod:grupo>
		<mod:grupo titulo="Dados do Objeto Fornecido" />
		<mod:grupo>
			<mod:texto titulo="Material a ser Fornecido"
				var="descricaoFornecimento" largura="55" maxcaracteres="50" />
		</mod:grupo>
		<mod:grupo>
			<mod:numero titulo="Prazo em dias para entrega" var="prazoDias"
				largura="6" maxcaracteres="4" extensoNum="sim" />
			<mod:oculto var="prazoDiasnumextenso" valor="${prazoDiasnumextenso}" />
			<mod:texto titulo="Seção Solicitante" var="secaoSolicitante"
				largura="34" maxcaracteres="30" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Endereço para entrega" var="enderecoEntrega"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Bairro" var="bairroEntrega" largura="34"
				maxcaracteres="30" />
			<mod:texto titulo="Estado" var="estadoEntrega" largura="34"
				maxcaracteres="30" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Seção Recebedora" var="secaoRecebedora"
				largura="34" maxcaracteres="30" />
		</mod:grupo>
		<mod:selecao titulo="mesmo local para entrega das Notas Fiscais"
			var="entregaNF" opcoes="SIM;NÃO" reler="sim" />
		<c:if test="${entregaNF=='NÃO'}">
			<mod:grupo>
				<mod:texto titulo="Endereço para entrega da NF"
					var="enderecoEntregaNota" largura="70" maxcaracteres="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Bairro" var="bairroEntregaNota" largura="32"
					maxcaracteres="30" />
				<mod:texto titulo="Estado" var="estadoEntregaNota" largura="32"
					maxcaracteres="30" />
			</mod:grupo>
		</c:if>
		<mod:grupo>
			<mod:monetario titulo="Valor do Fornecimento do Objeto"
				var="vrFornecimento" largura="12" maxcaracteres="10"
				formataNum="sim" extensoNum="sim" />
			<mod:oculto var="vrFornecimentovrextenso"
				valor="${vrFornecimentovrextenso}" />
			<mod:selecao titulo="por" var="tipoFornecimento"
				opcoes="Mensal;Unidade" />
		</mod:grupo>
		<mod:grupo>
			<mod:monetario titulo="Valor Global do Contrato" var="vrGlobal"
				largura="12" maxcaracteres="10" formataNum="sim" extensoNum="sim" />
			<mod:oculto var="vrGlobalvrextenso" valor="${vrGlobalvrextenso}" />
		</mod:grupo>
		<mod:grupo titulo="Prazo de Vigência e Execução" />
		<mod:grupo>
			<mod:data titulo="Data Final do Contrato" var="dataFinal" />
			<mod:data titulo="Prazo de Execução de" var="dataInicio" />
			<mod:data titulo="a" var="dataFim" />
		</mod:grupo>
		<mod:grupo titulo="Dotação Orçamentária" />
		<mod:grupo>
			<mod:texto titulo="Programa de Trabalho" var="programaTrabalho"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Elemento de Despesa" var="elementoDespesa"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:numero titulo="Nota de Empenho" var="notaEmpenho" largura="4"
				maxcaracteres="4" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Subsecretaria responsável" var="nomeSubsecretaria"
				largura="52" maxcaracteres="50" />
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			     @page {
					margin-left: 2cm;
					margin-right: 2cm;
					margin-top: 1cm;
					margin-bottom: 2cm;
			 	 }
		</style>
		</head>

		<body>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp"/>
		</td></tr>
			<tr> 
				<td width="100%">
					<table width="100%">				
						<tr>
						   <td>
                              <u><font size="2">Processo nº${numProcesso}/${anoProcesso}-EOF - Minuta de Contrato nº${numeroTermo}/${anoTermo} - (Fornecimento de ${descricaoFornecimento} )</font></u>
						   </td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF">
		 <tr>
		   <td>
		     <c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		   </td>
		 <tr>
		   <td>    
             <u><font size="2">Processo nº${numProcesso}/${anoProcesso}-EOF - Minuta de Contrato nº${numeroTermo}/${anoTermo} - (Fornecimento de ${descricaoFornecimento} )</font></u>
           </td>
         </tr>
        </table>     
		FIM CABECALHO -->

		<!--    daqui pra baixo  -->
		<font size="3"><br>
		<table width="60%" border="0" bgcolor="#FFFFFF" align="right">
			<tr>
				<td width="100%" align="left"><b>TERMO DE CONTRATO Nº
				${numeroTermo}/${anoTermo}</b></td>
			</tr>
			<tr>
				<td width="100%" align="justify"><b>CONTRATO DE
				FORNECIMENTO DE ${descricaoFornecimento}, QUE ENTRE SI CELEBRAM A
				JUSTIÇA FEDERAL DE 1º. GRAU NO RIO DE JANEIRO E A EMPRESA
				${nomeEmpresa}</b></td>
			</tr>
			<tr>
				<td width="100%" align="left"><b>PROCESSO Nº
				${numProcesso}/${anoProcesso} -EOF</b></td>
			</tr>
		</table>
		<p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">A Justiça Federal de
		1º. Grau no Rio de Janeiro, inscrita no CNPJ sob o n°
		05.424.540./0001-16, com sede na Av. Rio Branco, 243 - Anexo I - 14°
		andar, Centro, Rio de Janeiro/RJ, neste ato representada pelo Dr.<b>
		${nomeJuiz}</b>, Juiz Federal - Diretor do Foro, Identidade nº<b>
		${docIdentidade} - ${orgaoEmissor}</b>, CPF: <b>${cpfJuiz}</b>, doravante
		denominada CONTRATANTE, e a empresa <b>${nomeEmpresa}</b>, TEL. <b>${telefoneEmpresa}</b>
		e FAX.<b>${faxEmpresa}</b>, estabelecida na <b>${enderecoEmpresa}
		- ${bairroEmpresa} - ${estadoEmpresa}</b> inscrita no C.N.P.J. sob o n°<b>
		${cgcEmpresa}</b>, representado neste ato pelo Sr.<b>
		${nomeRepresentante}</b>, Identidade n°<b> ${identidadeRepresentante}</b>,
		CPF nº<b> ${cpfRepresentante}</b>, doravante denominada CONTRATADA,
		tendo em vista o decidido no Processo Administrativo n°<b>
		${numProcesso}/${anoProcesso}</b>-EOF, por despacho do Exmº. Sr. Juiz
		Federal - Diretor do Foro (fls <b>${numeroFolhaAutos}</b> dos autos),
		firmam o presente contrato em conformidade com o disposto na Lei nº
		10.520, de 17/07/02, Decreto nº. 3.555, de 08/08/2000 e,
		subsidiariamente, a Lei n° 8.666/93 e suas alterações, mediante às
		cláusulas e condições a seguir:</p>
		<p align="left"><u> <strong> CLÁUSULA PRIMEIRA - DO
		OBJETO: </strong> </u></p>
		<p id="p1" style="TEXT-INDENT: 1.5cm" align="justify">1.1 - O
		presente contrato tem por objeto o fornecimento de <b>${descricaoFornecimento}</b>
		</p>
		<p align="left"><u> <strong> CLÁUSULA SEGUNDA - DAS
		OBRIGAÇÕES DA CONTRATADA: </strong> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">2.1 - A Contratada
		obriga-se a:<br>
		&nbsp&nbsp&nbsp 2.1.1 - proceder ao fornecimento do produto, no prazo
		máximo de <b>${prazoDias}( ${prazoDiasnumextenso})</b> dias úteis, a
		contar da solicitação feita pela <b>${secaoSolicitante}</b><br>
		&nbsp&nbsp&nbsp 2.1.2 - entregar o produto na <b>${secaoRecebedora}</b>,
		situada na <b>${enderecoEntrega} - ${bairroEntrega} -
		${estadoEntrega}</b><br>
		&nbsp&nbsp&nbsp 2.1.3 - cumprir com todas as obrigações constantes no
		Termo de Referência do Pregão nº <b>${numeroPregao}/${anoPregao}</b>,
		que integra o presente ajuste;<br>
		&nbsp&nbsp&nbsp 2.1.4 - manter, durante toda a execução do contrato,
		em compatibilidade com as obrigações assumidas, todas as condições de
		habilitação e qualificação exigidas no Pregão nº <b>${numeroPregao}/${anoPregao}</b>.
		</p>
		<p align="left"><u><b>CLÁUSULA TERCEIRA - DAS OBRIGAÇÕES
		DA CONTRATANTE:</b></u></p>
		<div id="p1" style="TEXT-INDENT: 1.5cm" align="justify">3.1 -
		Caberá à Contratante:<br>
		&nbsp&nbsp&nbsp 3.1.1 - permitir acesso, aos empregados da Contratada,
		às instalações da Contratante para o fornecimento dos materiais
		constantes do objeto.<br>
		&nbsp&nbsp&nbsp 3.1.2 - prestar as informações e os esclarecimentos
		que venham a ser solicitados pelos empregados da Contratada.<br>
		&nbsp&nbsp&nbsp 3.1.3 - rejeitar qualquer material entregue em
		desacordo com as Especificações do Pregão nº <b>${numeroPregao}/${anoPregao}</b>.<br>
		&nbsp&nbsp&nbsp 3.1.4 - solicitar que seja substituído o material que
		não atender às Especificações do Pregão nº <b>${numeroPregao}/${anoPregao}</b>.<br>
		&nbsp&nbsp&nbsp 3.1.5 - atestar as faturas correspondentes, por
		intermédio da Fiscalização, designada pela Administração.<br>
		</div>
		<div align="left"><u> <b> CLÁUSULA QUARTA - DO PREÇO: </b> </u></div>
		<div style="TEXT-INDENT: 1.5cm" align="justify">4.1 - A
		Contratante pagará à Contratada, pelo fornecimento objeto deste
		Contrato, o valor de R$ <b>${vrFornecimento} (
		${vrFornecimentovrextenso} )</b> por <b>${tipoFornecimento}</b>.<br>
		4.2 - O valor global estimado para a presente contratação é de R$ <b>${vrGlobal}
		(${vrGlobalvrextenso})</b>.</div>
		<u><b>CLÁUSULA QUINTA - DO PAGAMENTO: </b></u>
		<p style="TEXT-INDENT: 1.5cm" align="justify">5.1 - O pagamento à
		contratada será efetivado até o 10° (décimo) dia útil após a
		apresentação da Nota Fiscal/Fatura discriminativa dos materiais
		entregues, devidamente atestada por servidor/Comissão de servidores
		competente, salvo eventual atraso de distribuição de recursos
		orçamentários e financeiros pelo Conselho de Justiça Federal,
		decorrente de execução orçamentária.<br>
		&nbsp&nbsp&nbsp a) A Fatura/Nota Fiscal deverá ser apresentada em 02
		(duas) vias e entregue na <b>${secaoRecebedora}</b> da <b>${nomeSubsecretaria}</b>,
		situada na <c:if test="${entregaNF=='NÃO'}">
			<b>${enderecoEntregaNota},${bairroEntregaNota},${estadoEntregaNota}</b>.
		</c:if> <c:if test="${entregaNF=='SIM'}">
			<b>${enderecoEntrega},${bairroEntrega},${estadoEntrega}</b>.
		</c:if> <br>
		&nbsp&nbsp&nbsp b) No período acima não haverá atualização financeira.<br>
		&nbsp&nbsp&nbsp c) Caso seja necessária a retificação da fatura por
		culpa da contratada, a fluência do prazo de 10 (dez) dias úteis será
		suspensa, reiniciando-se a contagem a partir da reapresentação da
		fatura retificada.<br>
		&nbsp&nbsp&nbsp d) A Seção Judiciária do Rio de Janeiro reserva-se o
		direito de recusar o pagamento, se no ato da atestação, o material
		entregue não estiver em conformidade com as especificações
		apresentadas e aceitas que integram o presente contrato.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">&nbsp&nbsp&nbsp e) A
		Seção Judiciária do Rio de Janeiro poderá deduzir da importância a
		pagar os valores correspondentes a multas ou indenizações devidas pela
		contratada nos termos do presente ajuste.<br>
		&nbsp&nbsp&nbsp f) Será considerada como a data do pagamento a data da
		emissão da Ordem Bancária.<br>
		5.2 - A contratada deverá comprovar, quando da apresentação da nota
		fiscal à Contratante, a regularidade perante a Seguridade Social e o
		Fundo de Garantia de Tempo de Serviço, através da CND e do CRF, dentro
		das respectivas validades, sob pena de não pagamento dos serviços
		prestados e de rescisão contratual, em atendimento ao disposto no
		parágrafo 3º do art. 195 da Constituição Federal, no art. 2º da Lei nº
		9.012/95 e nos arts. 55, inciso VIII, e 78, inciso I, ambos da Lei nº
		8.666/93.<br>
		5.3 - Na ocasião da entrega da nota fiscal a contratada deverá
		comprovar a condição de optante pelo SIMPLES (Sistema Integrado de
		Pagamento de Impostos e Contribuições das Microempresas e Empresas de
		pequeno Porte), mediante a apresentação da declaração indicada em ato
		normativo da Secretaria da Receita Federal, e dos documentos,
		devidamente autenticados, que comprovem ser o signatário da referida
		declaração representante legal da empresa.<br>
		As pessoas jurídicas não optantes pelo SIMPLES e aquelas que ainda não
		formalizaram a opção sofrerão a retenção de impostos/contribuições por
		esta Seção Judiciária no momento do pagamento, conforme disposto no
		art. 64 da Lei nº 9.430, de 27/12/96, regulamentado por ato normativo
		da Secretaria da Receita Federal.</p>
		<p align="left"><u> <b> CLÁUSULA SEXTA - DA DOTAÇÃO
		ORÇAMENTÁRIA: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">6.1 - As despesas
		decorrentes da presente contratação, correrão à conta dos recursos
		específicos consignados no Orçamento Geral da União, para o corrente
		exercício, conforme o especificado abaixo:<br>
		&nbsp&nbsp&nbsp Programa de Trabalho: <b>${programaTrabalho}</b><br>
		&nbsp&nbsp&nbsp Elemento de Despesa: <b>${elementoDespesa}</b><br>
		&nbsp&nbsp&nbsp Nota de Empenho: <b>${notaEmpenho}</b><br>
		</p>
		<p align="left"><u> <b> CLÁUSULA SÉTIMA - DO PRAZO DE
		VIGÊNCIA E DE EXECUÇÃO: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">7.1 - O presente
		contrato vigorará a partir da data de sua assinatura até <b>${dataFinal}</b>,
		sendo sua eficácia condicionada à publicação no Diário Oficial da
		União.<br>
		7.2 - O prazo de execução do presente Contrato será de <b>${dataInicio}</b>
		a <b>${dataFim}</b>.</p>
		<p align="left"><u> <b> CLÁUSULA OITAVA - DAS PENALIDADES:
		</b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">8.1 - O não
		cumprimento pela contratada de qualquer uma das obrigações, dentro dos
		prazos estabelecidos por este contrato, sujeitá-la-á às penalidades
		previstas nos artigos 86 a 88 da Lei n° 8.666/93.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">8.2 - As penalidades
		a que está sujeita a contratada inadimplente, nos termos da Lei n°
		8.666/93, são as seguintes:<br>
		&nbsp&nbsp&nbsp a) Advertência;<br>
		&nbsp&nbsp&nbsp b) multa;<br>
		&nbsp&nbsp&nbsp c) suspensão temporária de participar em licitação e
		impedimento em contratar com a Administração por prazo não superior a
		02 (dois) anos.<br>
		8.3 - A recusa injustificada em assinar o Contrato, aceitar ou retirar
		o instrumento equivalente, dentro do prazo estabelecido pela
		Administração, sujeita o adjudicatário à penalidade de multa de até
		10% (dez por cento) sobre o valor da adjudicação, independentemente da
		multa estipulada no subitem 8.4.2.<br>
		8.4 - A inexecução total ou parcial do contrato poderá acarretar, a
		critério da Administração, a aplicação das multas, alternativamente:<br>
		&nbsp&nbsp&nbsp 8.4.1 - Multa compensatória de até 30% (trinta por
		cento) sobre o valor equivalente à obrigação inadimplida.<br>
		&nbsp&nbsp&nbsp 8.4.2 - Multa correspondente à diferença entre o valor
		total porventura resultante de nova contratação e o valor total que
		seria pago ao adjudicatário.<br>
		&nbsp&nbsp&nbsp 8.4.3 - Multa de 50% (cinqüenta por cento) sobre o
		valor global do contrato, no caso de inexecução total do mesmo.<br>
		8.5 - A atualização dos valores correspondentes à multa estabelecida
		no item 8.4 far-se-á a partir do 1º (primeiro) dia, decorrido o prazo
		estabelecido no item 8.7.<br>
		8.6 - Os atrasos injustificados no cumprimento das obrigações
		assumidas pela contratada sujeitá-la-á à multa diária, até a data do
		efetivo adimplemento, de 0,3% (três décimos por cento), calculada à
		base de juros compostos, sem prejuízo das demais penalidades previstas
		na Lei nº 8.666/93.<br>
		&nbsp&nbsp&nbsp 8.6.1 - A multa moratória estabelecida ficará limitada
		à estipulada para inexecução parcial ou total do contrato (subitem
		8.4.1).<br>
		&nbsp&nbsp&nbsp 8.6.2 - O período de atraso será contado em dias
		corridos.<br>
		8.7 - A multa deverá ser paga no prazo de 30 (trinta) dias, contados
		da data do recebimento da intimação por via postal, ou da data da
		juntada aos autos do mandado de intimação cumprido, conforme o caso.<br>
		8.8 - Caso a multa não seja paga no prazo estabelecido no item 8.7,
		deverá ser descontada dos pagamentos do respectivo contrato, ou,
		ainda, cobrada judicialmente, se for o caso.<br>
		8.9 - A atualização dos valores correspondentes às multas
		estabelecidas neste Contrato dar- se-á através do IPCA-E/IBGE, tendo
		em vista o disposto no art. 1º da Lei nº 8.383, de 30/12/91 ou de
		outro índice, posteriormente determinado em lei.<br>
		8.10 - A contagem dos prazos dispostos neste Contrato obedecerá ao
		disposto no art. 110, da Lei nº 8.666/93.<br>
		8.11 - Os procedimentos de aplicação e recolhimento das multas foram
		regulamentados pela IN nº 24-12, do Egrégio Tribunal Regional Federal
		da 2ª Região</p>
		<p align="left"><u> <b> CLÁUSULA NONA - DA RESCISÃO: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">9.1 - A inexecução
		parcial ou total do Contrato enseja a sua rescisão, conforme disposto
		nos artigos 77 a 80 da Lei nº 8.666/93, sem prejuízo da aplicação das
		penalidades previstas na Cláusula Oitava.<br>
		9.2 - A rescisão do Contrato poderá ser:<br>
		&nbsp&nbsp&nbsp 9.2.1 - determinada por ato unilateral e escrito da
		Administração da Seção Judiciária do Rio de Janeiro, nos casos
		enumerados nos inciso I a XII e XVII do art. 78 da lei mencionada.<br>
		&nbsp&nbsp&nbsp&nbsp 9.2.1.1 - Nos casos previstos nos incisos I a
		VIII e XI a XVII, a Administração<br>
		notificará a Contratada, através de Ofício, com prova de recebimento.<br>
		&nbsp&nbsp&nbsp&nbsp 9.2.1.2 - Nos casos previstos nos incisos IX e X,
		a rescisão dar-se-á de pleno direito, independentemente de aviso ou
		interpelação judicial ou extrajudicial.<br>
		&nbsp&nbsp&nbsp 9.2.2 - amigável, por acordo entre as partes, desde
		que haja conveniência para a Administração da Seção Judiciária do Rio
		de Janeiro.<br>
		&nbsp&nbsp&nbsp 9.2.3 - judicial, nos termos da legislação vigente
		sobre a matéria.<br>
		9.3 - A rescisão administrativa ou amigável será precedida de
		autorização escrita e fundamentada da autoridade competente e as
		rescisões determinadas por ato unilateral da Administração serão
		formalmente motivadas nos autos do processo, assegurado o
		contraditório e a ampla defesa da Contratada. 9.4 - A rescisão será
		determinada na forma do art. 79 da Lei nº 8.666/93.</p>
		<p align="left"><u> <b> CLÁUSULA DÉCIMA - DA DOCUMENTAÇÃO
		COMPLEMENTAR: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">10.1 - Fazem parte
		integrante do presente instrumento de contrato, independente de
		transcrição, os documentos a seguir discriminados:<br>
		&nbsp&nbsp&nbsp a) Pregão nº <b>${numeroPregao}/${anoPregao}</b>.<br>
		&nbsp&nbsp&nbsp b) Proposta da Contratada apresentada à Contratante em
		......./......../(ano).</p>
		<font size="2" align="left"><u> <b> CLÁUSULA DÉCIMA
		PRIMEIRA - DAS CONDIÇÕES DE RECEBIMENTO PROVISÓRIO E DEFINITIVO: </b> </u></font>
		<p style="TEXT-INDENT: 1.5cm" align="justify">11.1 - O recebimento
		do objeto da presente contratação dar-se-á:<br>
		&nbsp&nbsp&nbsp 11.1.1 - provisoriamente, no ato da entrega do
		material e da apresentação das Faturas/Notas Fiscais discriminativas,
		pela Seção de Almoxarifado/SMA.<br>
		&nbsp&nbsp&nbsp 11.1.2 - definitivamente, no prazo de até 05 (cinco)
		dias úteis contados do recebimento provisório, após conferência da
		conformidade do material.</p>
		<p align="left"><u> <b> CLÁUSULA DÉCIMA SEGUNDA - DA
		PUBLICAÇÃO: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">12.1 - O presente
		contrato será publicado no Diário Oficial da União, na forma de
		extrato, de acordo com o que determina o parágrafo único do artigo 61
		da Lei n° 8.666/93.</p>
		<p align="left"><u> <b> CLÁUSULA DÉCIMA TERCEIRA - DA
		FISCALIZAÇÃO: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">13.1 - Durante a
		vigência do contrato, a entrega dos materiais será acompanhada e
		fiscalizada por servidores/Comissão, designados pela Administração.<br>
		13.2 - O representante anotará em registro próprio todas as
		ocorrências relacionadas com a entrega dos materiais mencionados,
		determinando o que for necessário à regularização das faltas ou
		defeitos observados.<br>
		13.3 - As decisões e providências que ultrapassarem a competência do
		representante serão solicitadas a seus superiores em tempo hábil para
		a adoção das medidas convenientes.<br>
		13.4 - O exercício da fiscalização pela Contratante não excluirá a
		responsabilidade da Contratada.<br>
		</p>
		<p align="left"><u> <b> CLÁUSULA DÉCIMA QUARTA - DAS
		CONSIDERAÇÕES FINAIS: </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">14.1 - O contrato
		poderá ser alterado nos termos previstos no art. 65 da Lei nº
		8.666/93, com a apresentação das devidas justificativas.<br>
		14.2 - Após o término deste Contrato, a Contratada fornecerá Termo de
		Quitação à Seção de Contratos da Subsecretaria de Material e
		Patrimônio, no prazo máximo de 15 (quinze) dias, em papel timbrado,
		devidamente assinado pelo representante legal, carimbado e datado.<br>
		14.3 - Na hipótese de o Termo de Quitação não ser fornecido dentro do
		prazo supracitado, será considerada como plena, rasa e total a
		quitação em favor da Seção Judiciária do Rio de Janeiro dos débitos
		referentes à presente contratação.<br>
		</p>
		<p align="left"><u> <b> CLÁUSULA DÉCIMA QUINTA - DO FORO:
		</b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">15.1 - Para dirimir
		as questões oriundas do presente contrato, ou de sua execução, com
		renúncia expressa de qualquer outro, por mais privilegiado que seja,
		será competente o Foro da Justiça Federal - Seção Judiciária do Rio de
		Janeiro.</p>
		<p style="TEXT-INDENT: 2.0cm" align="justify">E, assim, por
		estarem as partes justas e acordadas entre si, é lavrado o presente
		termo de contrato, extraído em 03 (três) vias de igual teor e forma,
		que depois de lido e achado conforme vai assinado pelas partes
		contratantes.</p>
		<p align="right">${doc.dtExtenso}<br>
		<br>

		________________________________________________<br>
		JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE JANEIRO<br>
		<br>


		________________________________________________<br>
		EMPRESA<br>
		<br>
		</p>
		</font>
		<!-- INICIO PRIMEIRO RODAPE
       		  <c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
	  	    FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		       <c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
	    	FIM RODAPE -->
		</body>
		</html>
	</mod:documento>
</mod:modelo>
