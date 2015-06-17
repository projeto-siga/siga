<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<mod:modelo>
	<mod:entrevista>

		<mod:grupo titulo="Detalhe do Contrato">
			<mod:texto titulo="Informe o nº da Ata de Registro de Preços"
				largura="12" maxcaracteres="10" var="ataregpreco" />
			<mod:texto titulo="Ano" largura="5" maxcaracteres="4" var="ano" />
			<mod:grupo>
				<mod:texto titulo="Nº Processo" largura="10" maxcaracteres="6"
					var="n1" />
				<mod:texto largura="10" maxcaracteres="6" var="n2" />
				<mod:texto largura="10" maxcaracteres="6" var="n3" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Dados do Juiz">
			<mod:texto titulo="Juiz Federal - (Diretor do Foro)" largura="60"
				maxcaracteres="40" var="nomeDoutor" />
			<mod:grupo>
				<mod:texto titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identidade" />
				<mod:texto titulo="CPF" largura="15" maxcaracteres="11" var="cpf" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Detalhe do Objeto">
			<mod:texto titulo="Informe o número do <b>PREGÃO</b>" largura="10"
				maxcaracteres="8" var="pregao" />
			<mod:grupo>
				<mod:texto titulo="Material a ser fornecido" largura="30"
					var="forninst" />
			</mod:grupo>
			<mod:grupo>
				<mod:numero titulo="Prazo de Entrega no máximo" largura="6"
					maxcaracteres="5" var="prazo" extensoNum="sim" />
				<mod:oculto var="prazonumextenso" valor="${prazonumextenso}" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Preço e Pagamento">
			<mod:texto titulo="Informe a quantidade de itens" largura="3" var="n"
				reler="sim" />
			<c:forEach var="i" begin="1" end="${n}">
				<mod:grupo>
					<mod:texto titulo="Item" largura="3" var="item${i}" />
					<mod:texto titulo="Especificação" largura="45"
						var="especificacao${i}" />
					<mod:texto titulo="Quantidade" largura="3" var="quantidade${i }" />
					<mod:monetario titulo="Preço Unitário" largura="12"
						maxcaracteres="10" var="preco${i}" formataNum="sim"
						extensoNum="sim" reler="sim" />
				</mod:grupo>
			</c:forEach>
			<!-- <mod:texto titulo="Empresa Vencedora" largura="30" var="empvencedora"/> -->
		</mod:grupo>

		<mod:grupo titulo="Dados da Subsecretaria">
			<mod:texto titulo="Nome" largura="60" maxcaracteres="40"
				var="subsecretaria" />
			<mod:grupo>
				<mod:texto titulo="Endereço" largura="60" maxcaracteres="40"
					var="endereco" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo>
			<mod:texto titulo="Cidade" largura="40" maxcaracteres="30"
				var="bairro" />
			<mod:texto titulo="Bairro" largura="40" maxcaracteres="30"
				var="cidade" />
			<mod:texto titulo="Cep" largura="15" maxcaracteres="9" var="cep" />
		</mod:grupo>

		<mod:grupo titulo="Vigência">
			<mod:numero titulo=" Periodo de Vigência em meses" largura="5"
				maxcaracteres="3" var="vigencia" extensoNum="sim" />
			<mod:oculto var="vigencianumextenso" valor="${vigencianumextenso}" />
		</mod:grupo>

		<mod:grupo titulo="Dotação Orçamentária">
			<mod:texto titulo="Programa de Trabalho" largura="60"
				maxcaracteres="40" var="progtrabalho" />
		</mod:grupo>

		<mod:grupo>
			<mod:texto titulo="Elemento de Despesa" largura="60"
				maxcaracteres="40" var="elemdespesa" />
		</mod:grupo>


	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 1</font></u>
		</head>
		<body>

		<font size="2">
		<p align="center"><b>ATA DE REGISTRO DE PREÇOS N.º&nbsp;
		${ataregpreco }/${ano }</b></p>
		<p align="center"><b>PROCESSO N.º&nbsp; ${n1 }/${n2 }/${n3 } -
		EOF</b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">A justiça Federal de
		1&deg; Grau no Rio de Janeiro, com sede na Av.Rio Branco, 243 - Anexo
		I - 14&deg; andar, Centro/RJ, inscrita no C.N.P.J. sob o n&deg;
		05.424.540./0001-16, neste ato representadas pelo Dr. <b>${nomeDoutor
		}</b>, Juiz Federal - Diretor do Foro, Identidade n&deg; <b>${identidade
		}</b>, CPF: <b>${cpf}</b> doravante denominada JUSTIÇA FEDERAL, resolve,
		em face das propostas apresentadas no <b>Pregão:${pregao }/(${ano
		})</b>, <b>REGISTRAR O PREÇO</b> da empresa classificada em primeiro lugar
		para o objeto da licitação e igualmente daquelas que manifestaram
		interesse em se registrar também pelo menor preço, doravante
		denominadas FORNECEDORAS, em conformidade com o disposto na Lei n&deg;
		10.520, de 17/07/2002, Decreto n&deg; 3.555, de 08/08/2000 e n&deg;
		3.931, de 19/07/2001 e, subsidiariamente, a Lei n&deg; 8.666, de
		21/06/1993 e suas alterações, mediante as cláusulas e condições a
		seguir:</p>

		<p><b><u>CL&Aacute;USULA PRIMEIRA - DO OBJETO</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">1.1. A presente ata
		tem por objeto o <b>Registro de Preços</b> para o fornecimento e
		instalação de <b>${forninst}</b>, conforme
		especifica&ccedil;&otilde;es constantes do Termo de Refer&ecirc;ncia
		do Edital do <b>Preg&atilde;o n&ordm; ${pregao }/(${ano })</b>, que
		integram a presente Ata, e Preços Registrados e Empresa(s)
		Forncedora(s).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">1.2. A
		aquisi&ccedil;&atilde;o dos materiais objeto da presente Ata
		ser&aacute; de acordo com as necessidades e conveni&ecirc;ncias da
		Justi&ccedil;a Federal.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">1.3. Qualquer
		&oacute;rg&atilde;o ou entidade integrante da
		Administra&ccedil;&atilde;o P&uacute;blica Federal poder&aacute;
		utilizar a Ata de Registro de Pre&ccedil;os durante a sua
		vig&ecirc;ncia, desde que manifeste interesse e mediante pr&eacute;via
		consulta &agrave; Justi&ccedil;a Federal do Rio de Janeiro.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">1.4. Caber&aacute;
		ao fornecedor benefici&aacute;rio da Ata de Registro de Pre&ccedil;os,
		observadas as condi&ccedil;&otilde;es nela estabelecidas, optar pela
		aceita&ccedil;&atilde;o ou n&atilde;o do fornecimento,
		independentemente dos quantitativos registrados em Ata, desde que este
		fornecimento n&atilde;o prejudique as obriga&ccedil;&otilde;es
		anteriormente assumidas.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">1.5. As
		aquisi&ccedil;&otilde;es adicionais de que trata o subitem 1.3
		n&atilde;o poder&atilde;o exceder, por &oacute;rg&atilde;o ou
		entidade, a 100% (cem por cento) dos quantitativos registrados na Ata
		de Registro de Preços.</p>
		<br>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 1º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 2</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA SEGUNDA - DO PRAZO DE ENTREGA E
		EXECU&Ccedil;&Atilde;O DOS SERVI&Ccedil;OS</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.1. O <b>prazo</b>
		pra entrega dos materiais e execu&ccedil;&atilde;o dos servi&ccedil;os
		objeto do presente Registro de Pre&ccedil;os, expressamente
		solicitado, ser&aacute; de, no m&aacute;ximo, <b>${prazo }</b> (<b>${prazonumextenso}</b>)
		horas, contados a partir da Solicita&ccedil;&atilde;o de Fornecimento(<b>Anexo
		VI</b>), respeitado o estabelecido nas especifica&ccedil;&otilde;es
		constantes do <b>Anexo I</b> - Termo de Refer&ecirc;ncia do
		Preg&atilde;o n&deg; <b>${pregao }</b>/(<b>${ano }</b>).</p>

		<b> OBS: O prazo de entrega dos materiais e execu&ccedil;&atilde;o
		dos servi&ccedil;os ser&aacute; preenchido nos termos da proposta da
		licitante vencedora, observado o prazo m&aacute;ximo estipulado na
		Especifica&ccedil;&atilde;o.</b>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.2. A
		justi&ccedil;a Federal far&aacute; as aquisi&ccedil;&otilde;es
		mediante a emiss&atilde;o da "Solicita&ccedil;&atilde;o de
		Fornecimento" (<b>Anexo VI</b>), quando ent&atilde;o ser&aacute;
		expedida Nota de Empenho correspondente a tal
		solicita&ccedil;&atilde;o.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.3. Ser&aacute;
		considerada como confirma&ccedil;&atilde;o de recebimento o recibo
		dado no Of&iacute;cio expedido, o relat&oacute;rio emitido pelo
		aparelho de fax, a mensagem de e-mail enviada e a certid&atilde;o,
		dada pelo servidor respons&aacute;vel, de haver entregue o
		Of&iacute;cio ou do mesmo haver sido recusado.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.4. Os materias
		dever&atilde;o ser entregues e os servi&ccedil;os executados no local
		estabelecido pela JUSTI&Ccedil;A FEDERAL, conforme
		especifica&ccedil;&otilde;es constantes do <b> Anexo I </b> - Termo de
		Refer&ecirc;ncia do Preg&atilde;o n&deg; <b>${pregao}</b>/( <b>${ano}</b>).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.5. O recebimento
		do objeto da presente Ata de Registro de Pre&ccedil;os
		dar-se-&aacute;:</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2.5.1 - provisoriamente, no
		ato da entrega do material fornecido e instalado e/ou os
		servi&ccedil;os executados.</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2.5.2 - definitivamente, no
		prazo de at&eacute; 05 (cinco) dias &uacute;teis contatos do
		recebimento provis&oacute;rio, ap&oacute;s confer&ecirc;ncia da
		conformidade do material fornecido e instalado e/ou os servi&ccedil;os
		executados.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 2º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 3</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA TERCEIRA - DO PRE&Ccedil;O E
		PAGAMENTO</u></b></p>
		<br>
		<br>

		<table width="100%" border="0" cellpadding="1">
			<tr>
				<td width="7%" align="center">ITEM</td>
				<td width="53%" align="center">ESPECIFICAÇÃO</td>
				<td width="20%" align="center">QUANTIDADE</td>
				<td width="20%" align="center">PREÇO UNITÁRIO</td>
			</tr>
		</table>

		<table width="100%" border="0" cellpadding="1">

			<c:forEach var="i" begin="1" end="${n}">
				<tr>
					<td width="7%" align="center">${requestScope[f:concat('item',i)]}</td>
					<td width="53%">${requestScope[f:concat('especificacao',i)]}</td>
					<td width="20%" align="center">${requestScope[f:concat('quantidade',i)]}</td>
					<td width="20%" align="center">${requestScope[f:concat('preco',i)]}</td>
				</tr>
			</c:forEach>
		</table>
		<table width="100%" border="0" cellpadding="3">
			<tr>
				<td><b>EMPRESA VENCEDORA:</b></td>
			</tr>
		</table>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.1. A JUSTIÇA
		FEDERAL pagará à(s) FORNECEDORA(S) o valor unitário registrado no
		item, multiplicado pela quantidade solicitada, que constará da
		Solicitação de Fornecimento(<b>Anexo VI</b>)e da Nota de Empenho, e
		ainda do Termo de Contrato, quando for o caso.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.2. Incluídos no
		preço unitário estão todos os impostos, taxas e encargos sociais,
		obrigações trabalhistas, previdenciárias, fiscais e comerciais, assim
		como despesas com transportes, as quais correrão por conta das
		FORNECEDORA(S).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.3. Os preços
		registrados deverão sempre ser adequados ao valor de mercado, sob pena
		de não haver a aquisição.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.4. As Notas de
		Empenho, quando for o caso, serão emitidas à medida que forem sendo
		solicitados os materiais.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.5. As Notas
		Fiscais/Faturas deverão ser entregues diretamente ao titular da
		Subsecretaria de <b>${subsecretaria }</b>, situada na <b>${endereco}</b>,
		<b>${bairro }</b>, <b>${cidade }</b> - Cep: <b>${cep }</b>, para serem
		devidamente atestadas.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.6. O pagamento à
		Fornecedora será efetivado, por crédito em conta corrente, mediante
		ordem bancária, até o 10º dia útil após a apresentação da fatura ou
		nota fiscal discriminativa do material entregue/serviços prestados,
		devidamente atestada por servidor ou Comissão nomeada pela
		Administração, salvo eventual atraso de distribuição de recursos
		financeiros efetuados pelo Conselho da Justiça Federal, decorrente de
		execução orçamentária.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 3º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 4</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3.6.1 - No período acima
		não haverá atualização financeira.</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3.6.2 - A fatura/nota
		fiscal deverá ser apresentada em 02 (duas) vias.</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3.6.3 - Será considerada
		como data do pagamento a data da emissão da Ordem Bancária.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.7. Caso seja
		necessária a retificação da fatura por culpa da(s) Fornecedora(s), a
		fluência do prazo de 10 (dez) dias úteis será suspensa, reiniciando-se
		a contagem a partir da reapresentação da fatura retificada.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.8. A Justiça
		Federal reserva-se o direito de não efetuar o pagamento se, no ato da
		atestação, o material/serviço não estiver em perfeitas condições ou de
		acordo com as especificações apresentadas e aceitas pela Justiça
		Federal.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.9 - A Seção
		Judiciária do Rio de Janeiro poderá deduzir da importância a pagar os
		valores correspondentes a multas ou indenizações devidas pela
		fornecedora nos termos do presente ajuste.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.10 -
		A(s)Fornecedora(s) deverá(ão) comprovar, quando da apresentação da
		nota fiscal à Justiça Federal, a regularidade perante a Seguridade
		Social e ao Fundo de Garantia de Tempo de Serviço, através da
		apresentação da CND e do CRF, dentro das respectivas validade, sob
		pena de não pagamento do material fornecido/serviço executado e de
		rescisão contratual, em atendimento ao disposto no § 3º. do art. l95
		da Constituição Federal, no art. 2º da Lei 9.012/95, e nos arts.
		55,inciso VIII e 78,inciso I, ambos da Lei 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.11 - Na ocasião da
		entrega da nota fiscal a(s) Fornecedora(s) deverá(ão) comprovar a
		condição de optante pelo SIMPLES (Sistema Integrado de Pagamento de
		Impostos e Contribuições das Microempresas e Empresas de pequeno
		Porte), mediante a apresentação da declaração indicada em ato
		normativo da Secretaria da Receita Federal, e dos documentos,
		devidamente autenticados, que comprovem ser o signatário da referida
		declaração representante legal da empresa.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">As pessoas jurídicas
		não optantes pelo SIMPLES e aquelas que ainda não formalizaram a opção
		sofrerão a retenção de impostos/contribuições por esta Seção
		Judiciária no momento do pagamento, conforme disposto no art. 64 da
		Lei nº 9.430, de 27/12/96, regulamentado por ato normativo da
		Secretaria da Receita Federal.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 4º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 5</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA QUARTA - DA VIGÊNCIA</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">4.1 - A presente Ata
		de Registro de Preços terá vigência de <b>${vigencia }</b>&nbsp; (<b>${vigencianumextenso}</b>)
		meses, contada a partir da data da assinatura da mesma.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">&nbsp;&nbsp;&nbsp;<b>PARÁGRAFO
		ÚNICO:</b>A presente Ata poderá ser prorrogada, na forma autorizada pelo
		art. 4º do Decreto nº 3.931/2001.</p>

		<p><b><u>CL&Aacute;USULA QUINTA - DA DOTAÇÃO ORÇAMENTÁRIA</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.1 - As despesas
		decorrentes do fornecimento e execução dos serviços, objeto deste
		Registro de Preços, correrão à conta dos recursos consignados à Seção
		Judiciária do Rio de Janeiro, para o corrente exercício, conforme o
		especificado a seguir:</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Programa de Trabalho: <b>${progtrabalho
		}</b>.<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Elemento de Despesa: <b>${elemdespesa
		}</b>.<br>
		</p>

		<p><b><u>CL&Aacute;USULA SEXTA - DAS OBRIGAÇÕES DA(S)
		FORNECEDORA(S):</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.1 - São obrigações
		da(s) Fornecedora(s):</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.1.1 - entregar os
		materiais e executar os serviços, objeto deste Registro de Preços, de
		acordo com as especificações constantes do <b> Anexo I </b> - Termo de
		Referência do Pregão nº <b>${pregao }</b>/(<b>${ano }</b>), em
		consonância com a proposta respectiva, bem como a cumprir com o prazo
		de entrega e quantidades constantes da Solicitação de Fornecimento(<b>
		Anexo VI</b>).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.1.2 - manter, durante todo
		o período de vigência da Ata de Registro de Preços, em compatibilidade
		com as obrigações assumidas, todas as condições de habilitação e
		qualificação exigidas no Pregão nº <b>${pregao }</b>/(<b>${ano }</b>).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.1.3 - fornecer aos seus
		funcionários crachás de identificação, sem os quais não será
		autorizada a entrada nas dependências da Contratante.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 5º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 6</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">6.2 - A(s)
		Fornecedora(s) é(são) responsável(eis) por:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.2.1 - qualquer acidente
		que venha a ocorrer com seus empregados e por danos que estes
		provoquem à Justiça Federal ou a terceiros, não excluindo essa
		responsabilidade a fiscalização ou o acompanhamento pela Justiça
		Federal;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.2.2 - todos os encargos
		previdenciários e obrigações sociais previstos na legislação social
		trabalhista em vigor relativos a seus funcionários, vez que os mesmos
		não manterão nenhum vínculo empregatício com a Seção Judiciária do Rio
		de Janeiro;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.2.3 - todas as
		providências e obrigações estabelecidas na legislação específica de
		acidentes de trabalho, quando, em ocorrência da espécie, forem vítimas
		os seus funcionários quando da realização da entrega dos
		materiais/prestação dos serviços, ou em conexão com eles;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.2.4 - todos os encargos
		fiscais e comerciais decorrentes do presente Registro de Preços;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.3 - A
		inadimplência da(s) Fornecedora(s), com referência aos encargos
		sociais, comerciais e fiscais, não transfere a responsabilidade por
		seu pagamento à Administração da Seção Judiciária do Rio de Janeiro,
		razão pela qual a(s) Fornecedora(s) renuncia(m) expressamente a
		qualquer vínculo de solidariedade, ativa ou passiva, com a SJRJ.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.4 - A(s)
		Fornecedora(s) deverá(ão) cumprir, ainda, com as demais obrigações
		constantes da especificação elaborada pela Subsecretaria de <b>${subsecretaria
		}</b>, que integra o presente Registro de Preços.</p>

		<p><b><u>CL&Aacute;USULA SÉTIMA - DO CANCELAMENTO DO
		REGISTRO</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">7.1 - O fornecedor
		terá seu registro cancelado quando:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;I - descumprir as condições
		da Ata de Registro de Preços;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;II - não retirar a
		respectiva nota de empenho ou instrumento equivalente, no prazo
		estabelecido pela Administração, sem justificativa aceitável;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;III - não aceitar reduzir o
		seu preço registrado, na hipótese de este se tornar superior àqueles
		praticados no mercado;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IV - tiver presentes razões
		de interesse público.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 6º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 7</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">&nbsp;&nbsp;&nbsp;&nbsp;<b>PARÁGRAFO
		1º</b> - O cancelamento de registro, nas hipóteses previstas, assegurados
		o contraditório e a ampla defesa, será formalizado por despacho da
		autoridade competente do órgão gerenciador.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">&nbsp;&nbsp;&nbsp;&nbsp;<b>PARÁGRAFO
		2º</b> - A(s) fornecedora(s) poderá(ão) solicitar o cancelamento do seu
		registro de preço na ocorrência de fato superveniente que venha
		comprometer a perfeita execução contratual, decorrentes de caso
		fortuito ou de força maior devidamente comprovados</p>

		<p><b><u>CL&Aacute;USULA OITAVA - DAS PENALIDADES</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.1 - O não
		cumprimento pela(s) Fornecedora(s) de qualquer uma das obrigações,
		dentro das especificações e/ou condições predeterminadas nesta Ata de
		Registro de Preços, sujeitá-la-á às penalidades previstas nos artigos
		86 a 88 da Lei n° 8.666/93;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.2 - As penalidades
		a que está sujeita a(s) Fornecedora(s) inadimplente(s), nos termos da
		Lei no 8.666/93, são as seguintes:</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a)advertência;</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b)multa;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c)suspensão temporária de
		participar em licitação e impedimento em contratar com a Administração
		por prazo não superior a 02 (dois) anos;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.3 - A recusa
		injustificada em assinar a Ata de Registro de Preços, aceitar ou
		retirar o instrumento equivalente, dentro do prazo estabelecido pela
		Administração, sujeita o adjudicatário à penalidade de multa de até
		10% (dez por cento) sobre o valor da adjudicação, independentemente da
		multa estipulada no subitem 8.4.2.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.4 - A inexecução
		total ou parcial do Registro de Preços poderá acarretar, a critério da
		Administração, a aplicação das multas, alternativamente:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.4.1 - Multa compensatória
		de até 30% (trinta por cento) sobre o valor equivalente à obrigação
		inadimplida.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.4.2 - Multa correspondente
		à diferença entre o valor total porventura resultante de nova
		contratação e o valor total que seria pago ao adjudicatário.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.4.3 - Multa de 50%
		(cinqüenta por cento) sobre o valor global do Registro de Preços, no
		caso de inexecução total do mesmo.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 7º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 8</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">8.5 - A atualização
		dos valores correspondentes à multa estabelecida no item 8.4 far-se-á
		a partir do 1º (primeiro) dia, decorrido o prazo estabelecido no item
		8.7.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.6 - Os atrasos
		injustificados no cumprimento das obrigações assumidas pela
		fornecedora sujeitá-la-á à multa diária, até a data do efetivo
		adimplemento, de 0,3% (três décimos por cento), calculada à base de
		juros compostos, sem prejuízo das demais penalidades previstas na Lei
		nº 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.6.1 - A multa moratória
		estabelecida ficará limitada à estipulada para inexecução parcial ou
		total do Registro de Preços (subitem8.4.1)</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.6.2 - O período de atraso
		será contado em dias corridos.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.7 - A multa deverá
		ser paga no prazo de 30 (trinta) dias, contados da data do recebimento
		da intimação por via postal, ou data da juntada aos autos do mandado
		de intimação cumprido, conforme o caso.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.8 - Caso a multa
		não seja paga no prazo estabelecido no item 8.7, deverá ser descontada
		dos pagamentos do respectivo Registro de Preços, ou, ainda, cobrada
		judicialmente, se for o caso.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.9 - A atualização
		dos valores correspondentes às multas estabelecidas neste Registro de
		Preços dar-se-á através do IPCA-E/IBGE, tendo em vista o disposto no
		art. 1º da Lei nº 8.383, de 30/12/91 ou de outro índice,
		posteriormente determinado em lei.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.10 - A contagem
		dos prazos dispostos neste Registro de Preços obedecerá ao disposto no
		art. 110, da Lei nº 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.11 - Os
		procedimentos de aplicação e recolhimento das multas foram
		regulamentadas pela IN 24-12, do Egrégio Tribunal Regional Federal da
		2ª Região.</p>

		<p><b><u>CL&Aacute;USULA NONA - DA DOCUMENTAÇÃO
		COMPLEMENTAR</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">9.1 - Fazem parte
		integrante do presente Registro de Preços, independentemente de
		transcrição, os seguintes documentos:</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a) Pregão nº <b>${pregao
		}</b>/(<b>${ano }</b>) e seus anexos.<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b) Proposta da(s)
		Fornecedora(s) apresentada à Justiça Federal em ${ano }.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA - DA PUBLICAÇÃO</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">10.1 - O presente
		Registro de Preços será publicado no Diário Oficial da União, na forma
		de extrato, de acordo com o que determina do parágrafo Único do artigo
		61 da Lei n° 8.666/93;</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 8º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 9</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA DÉCIMA PRIMEIRA - DA FISCALIZAÇÃO</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">11.1 - A entrega dos
		materiais e a execução dos serviços serão acompanhadas e fiscalizadas
		por servidor/Comissão designados pela Administração.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">11.2 - O
		representante anotará em registro próprio todas as ocorrências
		relacionadas com a entrega dos materiais e execução dos serviços
		mencionados, determinando o que for necessário à regularização das
		faltas ou defeitos observados.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">11.3 - As decisões e
		providências que ultrapassarem a competência do representante serão
		solicitadas a seus superiores em tempo hábil para a adoção das medidas
		convenientes.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">11.4 - O exercício
		da fiscalização pela Justiça Federal não excluirá a responsabilidade
		da(s) Fornecedora(s).</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA SEGUNDA - DOS RECURSOS
		ADMINISTRATIVOS</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">12.1 - Aplica-se o
		disposto no art. 109 da lei 8.666/93.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA TERCEIRA - DAS
		CONSIDERAÇÕES FINAIS </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">13.1 - O Registro de
		Preços poderá ser aditado nos termos previstos no art. 65 da Lei nº
		8.666/93, com a apresentação das devidas justificativas.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">13.2 - A(s)
		Fornecedora(s) deverá(ão) manter durante toda a validade do Registro
		de Preços, em compatibilidade com as obrigações por ela assumidas,
		todas as condições de habilitação e qualificação exigidas na
		licitação.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">13.3. Não constitui
		obrigação da JUSTIÇA FEDERAL a aquisição de qualquer quantidade dos
		itens objeto da presente Ata.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 9º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="1">Processo nº ${n1}/${n2 }/${n3 } - EOF -
		Ata de Registro de Preços nº ${ataregpreco}/ ${ano} - Fornec. e
		Instalação de ${forninst } &nbsp;&nbsp; 10</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA DÉCIMA QUARTA - DO FORO </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">14.1 - Para dirimir
		as questões oriundas da presente Ata de Registro de Preços, fica
		eleito o Foro da Justiça Federal - Seção Judiciária do Rio de Janeiro.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;E por estarem ajustados, é
		lavrada a presente Ata, extraída em 03 (três) vias de igual teor e
		forma, que depois de lida e achada conforme vai assinada pelas partes.
		</p>
		</font>

		<br>
		<br>
		<font size="2">
		<p align="right"><b>${doc.dtExtenso}</b><br>
		<br>
		<br>
		<br>
		<br>

		________________________________________________<br>
		JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE JANEIRO<br>
		<br>
		<br>
		<br>
		<br>
		<br>

		________________________________________________<br>
		FORNECEDORA(S) <br>
		<br>
		<br>
		<br>
		</p>
		</font>

		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		</body>
		</html>

	</mod:documento>
</mod:modelo>
