<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- 
     Modelo : Ata de Registro de Preco Sem Contrato
     Data da Criacao : 16/01/2007
     Ultima alteracao : 17/01/2007 
-->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Detalhes do Contrato" />
		<mod:grupo>
			<mod:monetario titulo="Nº da Ata de Registro de Preços"
				var="numeroAta" largura="10" maxcaracteres="8" verificaNum="sim" />
			<mod:monetario titulo="Ano" var="anoAta" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:monetario titulo="Processo Nº" var="numProcesso" largura="8"
				maxcaracteres="6" verificaNum="sim" />
			<mod:monetario titulo="Ano" var="anoProcesso" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo titulo="Dados do Juiz" />
		<mod:grupo>
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
		<mod:grupo titulo="Dados do Pregão"/>		
		<mod:grupo>
			<mod:monetario titulo="Número do Pregão" var="numeroPregao"
				largura="12" maxcaracteres="10" verificaNum="sim" />
			<mod:monetario titulo="Ano do Pregão" var="anoPregao" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo titulo="Dados da Empresa Fornecedora" />		
		<mod:grupo>
			<mod:texto titulo="Empresa Vencedora" var="nomeEmpresa" largura="70"
				maxcaracteres="60" />
			<mod:monetario titulo="CGC/CNPJ" var="cgcEmpresa" largura="16"
				maxcaracteres="14" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo titulo="Dados do Objeto Fornecido" />		
		<mod:grupo>
			<mod:texto titulo="Descrição do Fornecimento"
				var="descricaoFornecimento" largura="55" maxcaracteres="50" />
		</mod:grupo>
		<mod:selecao titulo="Número de Itens do Registro de Preço"
			var="numItens" opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
		<c:forEach var="i" begin="1" end="${numItens}">
			<mod:grupo>
				<mod:texto titulo="Item a ser fornecido" var="material${i}"
					largura="32" maxcaracteres="30" />
				<mod:monetario titulo="Quantidade" var="quantidadeItem${i}"
					largura="12" maxcaracteres="10" verificaNum="sim" />
				<mod:monetario titulo="Preço Unitário Registrado"
					var="precoItem${i}" largura="12" maxcaracteres="10"
					formataNum="sim" />
			</mod:grupo>
		</c:forEach>
		<mod:grupo>
			<mod:numero titulo="Prazo em dias para entrega" var="prazoDias"
				largura="6" maxcaracteres="4" extensoNum="sim" />
			<mod:oculto var="prazoDiasnumextenso" valor="${prazoDiasnumextenso}" />
		</mod:grupo>
		<mod:grupo titulo="Local para Entrega"/>		
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
		<mod:selecao titulo="mesmo local para entrega das Notas Fiscais"
			var="entregaNF" opcoes="SIM;NÃO" reler="sim" />
		<c:if test="${entregaNF=='NÃO'}">
			<mod:grupo>
				<mod:texto titulo="Endereço para entrega da NF" var="enderecoNota"
					largura="70" maxcaracteres="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Bairro" var="bairroNota" largura="32"
					maxcaracteres="30" />
				<mod:texto titulo="Estado" var="estadoNota" largura="32"
					maxcaracteres="30" />
			</mod:grupo>
		</c:if>
		<mod:grupo>
			<mod:numero titulo="Vigência da Ata de Registro de Preços (em meses)"
				var="prazoMeses" largura="5" maxcaracteres="3" extensoNum="sim" />
			<mod:oculto var="prazoMesesnumextenso"
				valor="${prazoMesesnumextenso}" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Programa de Trabalho" var="programaTrabalho"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Elemento de Despesa" var="elementoDespesa"
				largura="70" maxcaracteres="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto
				titulo="Nome da Subsecretaria responsável pela especificação"
				var="nomeSubsecretaria" largura="70" maxcaracteres="60" />
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
                              <u><font size="2">Processo nº${numProcesso}/${anoProcesso}-EOF -Ata de RP nº${numeroAta}/${anoAta} - Fornecimento de ${descricaoFornecimento} </font></u>                              
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
             <u><font size="2">Processo nº${numProcesso}/${anoProcesso}-EOF -Ata de RP nº${numeroAta}/${anoAta} - Fornecimento de ${descricaoFornecimento}</font></u>
           </td>
         </tr>
        </table>     
		FIM CABECALHO -->
		<!--    daqui pra baixo  -->
		<font size="3">
		<p align="center"><b> ATA DE REGISTRO DE PREÇOS N.º
		${numeroAta}/${anoAta}<br>
		PROCESSO Nº ${numProcesso}/${anoProcesso} -EOF </b></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">A Justiça Federal de
		1º Grau no Rio de Janeiro, com sede na Av. Rio Branco, 243 - Anexo I -
		14º andar, Centro/RJ, inscrita no C.N.P.J. sob o nº
		05.424.540/0001-16, neste ato representada pelo Dr.<b>${nomeJuiz}</b>,
		Juiz Federal - Diretor do Foro, Identidade nº <b>${docIdentidade}-${orgaoEmissor}</b>,
		CPF:<b>${cpfJuiz}</b>, doravante denominada JUSTIÇA FEDERAL, resolve,
		em face das propostas apresentadas no <b>Pregão n.º
		${numeroPregao}/${anoPregao}, REGISTRAR O PREÇO</b> da empresa
		classificada em primeiro lugar para o objeto da licitação e igualmente
		daquelas que manifestaram interesse em se registrar também pelo menor
		preço, doravante denominadas FORNECEDORAS, em conformidade com o
		disposto na Lei nº 10.520, de 17/07/2002, Decreto nº 3.555, de
		08/08/2000 e nº 3.931, de 19/072001 e, subsidiariamente, a Lei nº
		8.666, de 21/06/93 e suas alterações, mediante as cláusulas e
		condições a seguir:<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"><u><strong>CLÁUSULA
		PRIMEIRA - DO OBJETO</strong></u><br>
		<br>
		1.1 - A presente ata tem por objeto o <b>Registro de Preços</b> para o
		fornecimento de ${descricaoFornecimento}, conforme especificações
		constantes do Termo de Referência do Edital do <b>Pregão n.º
		${numeroPregao}/${anoPregao}</b>, que integram a presente Ata, e Preço(s)
		Registrado(s) e Empresa(s) Fornecedora(s).<br>
		1.2 - A aquisição dos materiais objeto da presente Ata será de acordo
		com as necessidades e conveniências da Justiça Federal.<br>
		1.3- Qualquer órgão ou entidade da Administração Pública Federal
		poderá utilizar a Ata de Registro de de Preços durante a sua vigência,
		desde que manifeste interesse e mediante prévia consulta à Justiça
		Federal do Rio de Janeiro.<br>
		1.4- Caberá ao fornecedor beneficiário da Ata de Registro de Preços,
		observadas as condições nela estabelecidas, optar pela aceitação ou
		não do fornecimento, independentemente dos quantitativos registrados
		em Ata, desde que este fornecimento não prejudique as obrigações
		anteriormente assumidas.<br>
		1.5- As aquisições adicionais de que trata o subitem 1.3 não poderão
		exceder, por órgão ou entidade, a 100% (cem por cento) dos
		quantitativos registrados em Ata de Registro de Preços.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"><u><strong>CLÁUSULA
		SEGUNDA - DO PRAZO DE ENTREGA</strong></u><br>
		<br>
		2.1. O prazo para entrega dos materiais, objeto do presente Registro
		de Preços, expressamente solicitado, será de, no maximo, <b>${prazoDias}
		( ${prazoDiasnumextenso})</b> dias, contados a partir do recebimento da
		"Solicitação de Fornecimento" <b>(Anexo VI)</b>, respeitando o
		estabelecido nas Especificações constantes do <b>Anexo I</b> - Termo
		de Referência do <b>Pregão n.º ${numeroPregao}/${anoPregao}</b><br>
		<b>OBS:<i>O prazo de entrega dos materiais será preenchido nos
		termos da proposta da licitante vencedora, observado o prazo máximo
		estipulado na Especificação.</i></b><br>
		2.2 - A Justiça Federal fará as aquisições mediante a emissão da
		"Solicitação de Fornecimento" <b>(Anexo VI)</b>, quando então será
		expedida Nota de Empenho correspondente a tal solicitação.<br>
		&nbsp&nbsp 2.2.1 - A(s) Fornecedora(s) receberá(ão), através de
		ofício, com ou sem AR, ou via fax, ou ainda por e-mail, a Solicitação
		de Fornecimento acompanhada da Nota de Empenho.<br>
		2.3 - Será considerada como confirmação de recebimento o recibo dado
		no Ofício expedido, o relatório emitido pelo aparelho de fax, a
		mensagem de e-mail enviada e a certidão, dada pelo servidor
		responsável, de haver entregue o Ofício ou do mesmo haver sido
		recusado.<br>
		2.4 - Os materiais deverão ser entregues na <b>${enderecoEntrega}</b>
		- Bairro:<b>${bairroEntrega}</b> - Estado:<b> ${estadoEntrega}</b>,
		conforme estabelecido nas Especificações constantes do <b>Anexo I</b>
		- Termo de Referência do <b>Pregão nº ${numeroPregao}/${anoPregao}</b><br>
		</p>
		</font>
		<p style="TEXT-INDENT: 1.5cm" align="justify"><u><b>CLÁUSULA
		TERCEIRA - DO PREÇO E PAGAMENTO</b></u><br>
		<br>
		<font size="1">
		<table width="100%" border="0" cellpadding="1">
			<tr>
				<td></td>
				<td width="10%" align="center">ITEM</td>
				<td width="30%" align="center">ESPECIFICAÇÃO</td>
				<td width="20%" align="center">QUANTIDADE</td>
				<td width="40%" align="center">PREÇO UNITÁRIO REGISTRADO</td>
			</tr>
			<table width="100%" border="0" cellpadding="1">
				<c:forEach var="m" begin="1" end="${numItens}">
					<tr>
						<td>${m}</td>
						<td>${requestScope[f:concat('material',m)]}</td>
						<td>${requestScope[f:concat('quantidadeItem',m)]}</td>
						<td>${requestScope[f:concat('precoItem',m)]}</td>
						<td></td>
					</tr>
				</c:forEach>
			</table>
			<table width="100%" cellpading="3">
				<tr>
					<td><font size="1"> EMPRESA VENCEDORA : ${nomeEmpresa}
					</font></td>
				</tr>
			</table>
		</table>
		</font><font size="3">
		<p style="TEXT-INDENT: 1.5cm" align="justify">3.1. A JUSTIÇA
		FEDERAL pagará à(s) FORNECEDORA(S) o valor unitário registrado no
		item, multiplicado pela quantidade solicitada, que constará da
		Solicitação de Fornecimento <b>(Anexo VI)</b> e da Nota de Empenho, e
		ainda do Termo de Contrato, quando for o caso.<br>
		3.2. Incluídos no preço unitário estão todos os impostos, taxas e
		encargos sociais, obrigações trabalhistas, previdenciárias, fiscais e
		comerciais, assim como despesas com transportes, as quais correrão por
		conta das FORNECEDORA(S).<br>
		<c:if test="${numItens > 6}"></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"></c:if> 3.3. Os preços
		registrados deverão sempre ser adequados ao valor de mercado, sob pena
		de não haver a aquisição.<br>
		3.4. As Notas de Empenho, quando for o caso, serão emitidas à medida
		que forem sendo solicitados os materiais.<br>
		3.5 - As Notas Fiscais/Faturas deverão ser entregues na Rua <c:if
			test="${entregaNF=='NÃO'}">
			<b>${enderecoNota}</b>
		</c:if> <c:if test="${entregaNF=='SIM'}">
			<b>${enderecoEntrega}</b>
		</c:if> conforme Especificações constantes do <b>Anexo I</b> - Termo de
		Referência do <b>Pregão nº ${numeroPregao}/${anoPregao}</b> <c:if
			test="${numItens < 6}"></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"></c:if> 3.6 - O pagamento à
		Fornecedora será efetivado, por crédito em conta corrente, mediante
		ordem bancária, até o 10º dia útil após a apresentação da fatura ou
		nota fiscal discriminativa do material entregue, devidamente atestada
		por servidor ou Comissão nomeada pela Administração, salvo eventual
		atraso de distribuição de recursos financeiros efetuados pelo Conselho
		da Justiça Federal, decorrente de execução orçamentária.<br>
		&nbsp&nbsp&nbsp 3.6.1 - No período acima não haverá atualização
		financeira.<br>
		&nbsp&nbsp&nbsp 3.6.2 - A fatura/nota fiscal deverá ser apresentada em
		02 (duas) vias.<br>
		&nbsp&nbsp&nbsp 3.6.3 - Será considerada como data do pagamento a data
		da emissão da Ordem Bancária.<br>
		3.7 - Caso seja necessária a retificação da fatura por culpa da(s)
		Fornecedora(s), a fluência do prazo de 10 (dez) dias úteis será
		suspensa, reiniciando-se a contagem a partir da reapresentação da
		fatura retificada.<br>
		3.8 - A Justiça Federal reserva-se o direito de não efetuar o
		pagamento se, no ato da atestação, o material não estiver em perfeitas
		condições ou de acordo com as especificações apresentadas e aceitas
		pela Justiça Federal.<br>
		3.9 - A Seção Judiciária do Rio de Janeiro poderá deduzir da
		importância a pagar os valores correspondentes a multas ou
		indenizações devidas pela Contratada nos termos do presente ajuste.<br>
		3.10 - A(s) Fornecedora(s) deverá(ão) comprovar, quando da
		apresentação da nota fiscal à Justiça Federal, a regularidade perante
		a Seguridade Social e ao Fundo de Garantia de Tempo de Serviço,
		através da apresentação da CND e do CRF, dentro das respectivas
		validade, sob pena de não pagamento do material fornecido e de
		rescisão contratual, em atendimento ao disposto no § 3º. do art. l95
		da Constituição Federal, no art. 2º da Lei 9.012/95, e nos arts. 55,
		inciso VIII e 78, inciso I, ambos da Lei 8.666/93.<br>
		3.11 - Na ocasião da entrega da nota fiscal a(s) Fornecedora(s)
		deverá(ão) comprovar a condição de optante pelo SIMPLES (Sistema
		Integrado de Pagamento de Impostos e Contribuições das Microempresas e
		Empresas de pequeno Porte), mediante a apresentação da declaração
		indicada em ato normativo da Secretaria da Receita Federal, e dos
		documentos, devidamente autenticados, que comprovem ser o signatário
		da referida declaração representante legal da empresa.<br>
		&nbsp&nbsp&nbsp As pessoas jurídicas não optantes pelo SIMPLES e
		aquelas que ainda não formalizaram a opção sofrerão a retenção de
		impostos/contribuições por esta Seção Judiciária no momento do
		pagamento, conforme disposto no art. 64 da Lei nº 9.430, de 27/12/96,
		regulamentado por ato normativo da Secretaria da Receita Federal.<br>
		</p>
		<p align="left"><u><b>CLÁUSULA QUARTA - DO RECEBIMENTO DO
		MATERIAL </b></u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">4.1 - O recebimento
		do objeto da presente Ata de Registro de Preços dar-se-á:<br>
		</p>
		<c:if test="${numItens < 6}"></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"></c:if> &nbsp&nbsp&nbsp
		4.1.1 - provisoriamente, no ato da entrega do material e da
		apresentação das Faturas/Notas Fiscais discriminativas, pela Seção de
		Almoxarifado/SMA.<br>
		4.1.2 - definitivamente, no prazo de até 05 (cinco) dias úteis
		contados do recebimento provisório, após conferência da conformidade
		do material.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"><u><b>
		CLÁUSULA QUINTA - DA VIGÊNCIA </b></u><br>
		<br>
		5.1 - A presente Ata de Registro de Preços terá vigência de
		${prazoMeses} (${prazoMesesnumextenso}) meses, contada a partir da
		data da assinatura da mesma.<br>
		<font size="2"><b>PARÁGRAFO ÚNICO:</b></font>A presente Ata poderá ser
		prorrogada, na forma autorizada pelo art. 4º do Decreto nº 3.931/2001.<br>
		</p>
		<p align="left"><u> <b> CLÁUSULA SEXTA - DOTAÇÃO
		ORÇAMENTÁRIA </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">6.1 - As despesas
		decorrentes do fornecimento do objeto deste Registro de Preços,
		correrão à conta dos recursos consignados à Seção Judiciária do Rio de
		Janeiro, para o corrente exercício, conforme o especificado a seguir:<br>
		&nbsp&nbsp&nbsp Programa de Trabalho: <b>${programaTrabalho}</b><br>
		&nbsp&nbsp&nbsp Elemento de Despesa:<b> ${elementoDespesa}</b></p>
		<p align="left"><u><b>CLÁUSULA SÉTIMA - DAS OBRIGAÇÕES
		DA(S) FORNECEDORA(S):</b></u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">7.1 - São obrigações
		da(s) Fornecedora(s): &nbsp&nbsp&nbsp 7.1.1 - entregar os materiais,
		objeto deste Registro de Preços, de acordo com as especificações
		constantes do <b>Anexo I</b>- Termo de Referência do <b>Pregão nº
		${numeroPregao}/${anoPregao}</b>, em consonância com a proposta
		respectiva, bem como a cumprir com o prazo de entrega e quantidades
		constantes da Solicitação de Fornecimento<b>(Anexo VI)</b>.<br>
		&nbsp&nbsp&nbsp 7.1.2 - manter, durante todo o período de vigência da
		Ata de Registro de Preços, em compatibilidade com as obrigações
		assumidas, todas as condições de habilitação e qualificação exigidas
		no <b>Pregão nº ${numeroPregao}/${anoPregao}</b>.<br>
		&nbsp&nbsp&nbsp 7.1.3 - fornecer aos seus funcionários crachás de
		identificação, sem os quais não será autorizada a entrada nas
		dependências da Contratante.<br>
		7.2 - A(s) Fornecedora(s) é(são) responsável(eis) por:<br>
		&nbsp&nbsp&nbsp 7.2.1 - qualquer acidente que venha a ocorrer com seus
		empregados e por danos que estes provoquem à Justiça Federal ou a
		terceiros, não excluindo essa responsabilidade a fiscalização ou o
		acompanhamento pela Justiça Federal; <br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">&nbsp&nbsp&nbsp
		7.2.2 - todos os encargos previdenciários e obrigações sociais
		previstos na legislação social trabalhista em vigor relativos a seus
		funcionários, vez que os mesmos não manterão nenhum vínculo
		empregatício com a Seção Judiciária do Rio de Janeiro;<br>
		&nbsp&nbsp&nbsp 7.2.3 - todas as providências e obrigações
		estabelecidas na legislação específica de acidentes de trabalho,
		quando, em ocorrência da espécie, forem vítimas os seus funcionários
		quando da realização da entrega dos materiais, ou em conexão com eles;<br>
		&nbsp&nbsp&nbsp 7.2.4 - todos os encargos fiscais e comerciais
		decorrentes do presente Registro de Preços;<br>
		7.3 - A inadimplência da(s) Fornecedora(s), com referência aos
		encargos sociais, comerciais e fiscais, não transfere a
		responsabilidade por seu pagamento à Administração da Seção Judiciária
		do Rio de Janeiro, razão pela qual a(s) Fornecedora(s) renuncia(m)
		expressamente a qualquer vínculo de solidariedade, ativa ou passiva,
		com a SJRJ.<br>
		7.4 - A(s) Fornecedora(s) deverá(ão) cumprir, ainda, com as demais
		obrigações constantes da especificação elaborada pela Subsecretaria de
		${nomeSubsecretaria} que integra o presente Registro de Preços.<br>
		</p>
		<p align="left"><u> <b> CLÁUSULA OITAVA - DO CANCELAMENTO
		DO REGISTRO </b> </u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">8.1 - O fornecedor
		terá seu registro cancelado quando:<br>
		&nbsp&nbsp&nbsp I - descumprir as condições da Ata de Registro de
		Preços;<br>
		&nbsp&nbsp&nbsp II - não retirar a respectiva nota de empenho ou
		instrumento equivalente, no prazo estabelecido pela Administração, sem
		justificativa aceitável;<br>
		&nbsp&nbsp&nbsp III - não aceitar reduzir o seu preço registrado, na
		hipótese de este se tornar superior àqueles praticados no mercado;<br>
		&nbsp&nbsp&nbsp IV - tiver presentes razões de interesse público.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify"><b>PARÁGRAFO 1º
		-</b> O cancelamento de registro, nas hipóteses previstas, assegurados o
		contraditório e a ampla defesa, será formalizado por despacho da
		autoridade competente do órgão gerenciador.<br>
		<b>PARÁGRAFO 2º -</b> A(s) fornecedora(s) poderá(ão) solicitar o
		cancelamento do seu registro de preço na ocorrência de fato
		superveniente que venha comprometer a perfeita execução contratual,
		decorrentes de caso fortuito ou de força maior devidamente
		comprovados.<br>
		</p>
		<p align="left"><u> <b> CLÁUSULA NONA - DAS PENALIDADES </b> </u>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">9.1 - O não
		cumprimento pela(s) Fornecedora(s) de qualquer uma das obrigações,
		dentro das especificações e/ou condições predeterminadas nesta Ata de
		Registro de Preços, sujeitá-la-á às penalidades previstas nos artigos
		86 a 88 da Lei n° 8.666/93;<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">9.2 - As penalidades
		a que está sujeita a(s) Fornecedora(s) inadimplente(s), nos termos da
		Lei no 8.666/93, são as seguintes:<br>
		&nbsp&nbsp&nbsp a)Advertência;<br>
		&nbsp&nbsp&nbsp b)multa;<br>
		&nbsp&nbsp&nbsp c)suspensão temporária de participar em licitação e
		impedimento em contratar com a<br>
		Administração por prazo não superior a 02 (dois) anos;<br>
		9.3 - A recusa injustificada em assinar a Ata de Registro de Preços,
		aceitar ou retirar o instrumento equivalente, dentro do prazo
		estabelecido pela Administração, sujeita o adjudicatário à penalidade
		de multa de até 10% (dez por cento) sobre o valor da adjudicação,
		independentemente da multa estipulada no subitem 9.4.2.<br>
		9.4 - A inexecução total ou parcial do Registro de Preços poderá
		acarretar, a critério da Administração, a aplicação das multas,
		alternativamente:<br>
		</p>
		<p style="TEXT-INDENT: 2.0cm" align="justify">9.4.1 - Multa
		compensatória de até 30% (trinta por cento) sobre o valor equivalente
		à obrigação inadimplida.<br>
		9.4.2 - Multa correspondente à diferença entre o valor total
		porventura resultante de nova contratação e o valor total que seria
		pago ao adjudicatário.<br>
		9.4.3 - Multa de 50% (cinqüenta por cento) sobre o valor global do
		Registro de Preços, no caso de inexecução total do mesmo.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">9.5 - A atualização
		dos valores correspondentes à multa estabelecida no item 9.4 far-se-á
		a partir do 1º (primeiro) dia, decorrido o prazo estabelecido no item
		9.7.<br>
		9.6 - Os atrasos injustificados no cumprimento das obrigações
		assumidas pela fornecedora sujeitá-la-á à multa diária, até a data do
		efetivo adimplemento, de 0,3% (três décimos por cento), calculada à
		base de juros compostos, sem prejuízo das demais penalidades previstas
		na Lei nº 8.666/93.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">9.7 - A multa deverá
		ser paga no prazo de 30 (trinta) dias, contados da data do recebimento
		da intimação por via postal, ou da data da juntada aos autos do
		mandado de intimação cumprido, conforme o caso.<br>
		9.8 - Caso a multa não seja paga no prazo estabelecido no item 9.7,
		deverá ser descontada dos pagamentos do respectivo Registro de Preços,
		ou, ainda, cobrada judicialmente, se for o caso.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">9.9 - A atualização
		dos valores correspondentes às multas estabelecidas neste Registro de
		Preços dar-se-á através do IPCA-E/IBGE, tendo em vista o disposto no
		art. 1º da Lei nº 8.383, de 30/12/91 ou de outro índice,
		posteriormente determinado em lei.<br>
		9.10 - A contagem dos prazos dispostos neste Registro de Preços
		obedecerá ao disposto no art. 110, da Lei nº 8.666/93.<br>
		9.11 - Os procedimentos de aplicação e recolhimento das multas foram
		regulamentadas pela IN 24-12, do Egrégio Tribunal Regional Federal da
		2ª Região.<br>
		</p>
		<p align="left"><u><b>CLÁUSULA DÉCIMA - DA DOCUMENTAÇÃO
		COMPLEMENTAR: </b></u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">10.1 - Fazem parte
		integrante do presente Registro de Preços, independentemente de
		transcrição, os seguintes documentos:<br>
		<p style="TEXT-INDENT: 2.0cm" align="justify">a) <b>Pregão n°
		${numeroPregao}/${anoPregao}</b> e seus anexos.<br>
		b) Proposta da(s) Fornecedora(s) apresentada à Justiça Federal em
		......./....../20......</p>
		<p align="left"><u><b>CLÁUSULA DÉCIMA PRIMEIRA - DA
		PUBLICAÇÃO</b></u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">11.1 - O presente
		Registro de Preços será publicado no Diário Oficial da União, na forma
		de extrato, de acordo com o que determina do parágrafo Único do artigo
		61 da Lei n° 8.666/93;<br>
		</p>
		<p align="left"><u><b>CLÁUSULA DÉCIMA SEGUNDA - DA
		FISCALIZAÇÃO </b></u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">12.1 - A entrega dos
		materiais será acompanhada e fiscalizada por servidores/Comissão
		designados pela Administração.<br>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">12.2 - O
		representante anotará em registro próprio todas as ocorrências
		relacionadas com a entrega dos materiais, determinando o que for
		necessário à regularização das faltas ou defeitos observados.<br>
		12.3 - As decisões e providências que ultrapassarem a competência do
		representante serão solicitadas a seus superiores em tempo hábil para
		a adoção das medidas convenientes.<br>
		12.4 - O exercício da fiscalização pela Justiça Federal não excluirá a
		responsabilidade da(s) Fornecedora(s).<br>
		</p>
		<p align="left"><u><b>CLÁUSULA DÉCIMA TERCEIRA - DOS
		RECURSOS ADMINISTRATIVOS </b></u></p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">13.1 - Aplica-se o
		disposto no art. 109 da lei 8.666/93.<br>
		</p>
		<p align="left"><u> <b> CLÁUSULA DÉCIMA QUARTA - DAS
		CONSIDERAÇÕES FINAIS: </b></u>
		</font>
		</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">14.1 - O Registro de
		Preços poderá ser aditado nos termos previstos no art. 65 da Lei nº
		8.666/93, com a apresentação das devidas justificativas.<br>
		14.2 - A(s) Fornecedora(s) deverá(ão) manter durante toda a validade
		do Registro de Preços, em compatibilidade com as obrigações por ela
		assumidas, todas as condições de habilitação e qualificação exigidas
		na licitação.<br>
		14.3. Não constitui obrigação da JUSTIÇA FEDERAL a aquisição de
		qualquer quantidade dos itens objeto da presente Ata.<br>
		</p>
		<u><b>CLÁUSULA DÉCIMA QUINTA - DO FORO </b></u>
		<p style="TEXT-INDENT: 1.5cm" align="justify">15.1 - Para dirimir
		as questões oriundas da presente Ata de Registro de Preços, fica
		eleito o Foro da Justiça Federal - Seção Judiciária do Rio de Janeiro.<br>
		</p>
		<p style="TEXT-INDENT: 2.0cm" align="justify">E por estarem
		ajustados, é lavrada a presente Ata, extraída em 03 (três) vias de
		igual teor e forma, que depois de lida e achada conforme vai assinada
		pelas partes.<br>
		<br>
		</p>
		<p align="right">${doc.dtExtenso}<br>
		<br>

		________________________________________________<br>
		JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE JANEIRO<br>
		<br>


		________________________________________________<br>
		FORNECEDORA(S)<br>
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
