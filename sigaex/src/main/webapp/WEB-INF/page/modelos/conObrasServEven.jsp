<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- 
Este modelo trata do Contrato de 
Obras e Serviços Eventuais.
 -->
<mod:modelo>
	<mod:entrevista>

		<mod:grupo titulo="Detalhe do Contrato">
			<mod:texto titulo="Nº do Termo de Contrato" largura="15"
				maxcaracteres="10" var="termoContrato" />
			<mod:texto titulo="Ano" largura="5" var="anoContrato" />
			<mod:grupo>
				<mod:texto titulo="Serviços de Engenharia para" largura="40"
					var="objetivo" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Nº Processo" largura="6" var="n1" />
				<mod:texto largura="6" var="n2" />
				<mod:texto largura="6" var="n3" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Dados do Juiz">
			<mod:texto titulo="Juiz(a) Federal (Diretor(a) do Foro)" largura="60"
				maxcaracteres="40" var="nomeJuiz" />
			<mod:grupo>
				<mod:texto titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identJuiz" />
				<mod:texto titulo="Orgão Emissor" largura="5" var="orgEmissorJuiz" />
				<mod:texto titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfJuiz" />
				<mod:texto titulo="Nº Folhas do auto" largura="4" var="folhas" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Dados da Empresa Contratada">
			<mod:texto titulo="Nome da Empresa" largura="60" maxcaracteres="40"
				var="nomeEmpresa" />
			<mod:grupo>
				<mod:texto titulo="Endereço" largura="70" maxcaracteres="50"
					var="endEmpresa" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="CGC/CNPJ" largura="15" maxcaracteres="13"
					var="cnpj" />
				<mod:texto titulo="Telefone" largura="13" maxcaracteres="12"
					var="tel" />
				<mod:texto titulo="Fax" largura="13" maxcaracteres="12" var="fax" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Nome do Representante SR(a)" largura="60"
					maxcaracteres="40" var="nomeRepresentante" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identRepresentante" />
				<mod:texto titulo="Orgão Emissor" largura="5"
					var="orgEmissorRepresentante" />
				<mod:texto titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfRepresentante" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Dados do Objeto">
			<mod:texto titulo="Nome do Objeto" largura="60" maxcaracteres="40"
				var="objeto" />
			<mod:grupo>
				<mod:texto titulo="Nº do Edital" largura="5" var="numEdital" />
				<mod:texto titulo="Ano" largura="5" var="anoEdital" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Edital" largura="60" maxcaracteres="40"
					var="edital" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Dados do Contratante">
			<mod:texto titulo="Nome da Seção da Área de Infra-estrutura"
				largura="30" var="secao" />
			<mod:texto titulo="Sigla" largura="5" var="sigla" />
		</mod:grupo>

		<mod:grupo titulo="Preço e Forma de Pagamento">
			<mod:grupo>
				<mod:monetario titulo="Valor Global do Contrato R$" largura="12"
					maxcaracteres="10" var="valorContrato" formataNum="sim"
					extensoNum="sim" reler="sim" />
				<mod:oculto var="valorContratovrextenso"
					valor="${valorContratovrextenso}" />
			</mod:grupo>

			<mod:grupo>
				<mod:selecao titulo="Deseja parcelar o pagamento" var="parcelas"
					opcoes="Não;Sim" reler="sim" />
			</mod:grupo>
			<c:if test="${parcelas == 'Sim'}">
				<mod:grupo>
					<mod:numero titulo="Informe o nº de parcelas" largura="3"
						var="nparcelas" extensoNum="sim" />
					<mod:oculto var="nparcelasnumextenso" valor="${nnumextenso}" />
				</mod:grupo>
				<mod:grupo>
					<mod:numero titulo="Informe o dia da primeira parcela" largura="3" var="dia" extensoNum="sim" />
					<mod:oculto var="dianumextenso" valor="${dianumextenso}" />
				</mod:grupo>
				<!-- 
					<c:forEach var="i" begin="1" end="${n}">
				<mod:grupo>
					<mod:texto titulo="${i}º - Parcela" largura="10" var="medicao${i}" />
					<mod:data titulo="dia" var="diaMedicao${i}" />
				</mod:grupo>
			</c:forEach>
			-->
			</c:if>
		</mod:grupo>

		<mod:grupo titulo="Dotação Orçamentária">
			<mod:texto titulo="Programa de Trabalho" largura="40"
				var="progTrabalho" />
			<mod:grupo>
				<mod:texto titulo="Elemento de Despesa" largura="40"
					var="elemDespesa" />
			</mod:grupo>
			<mod:grupo>
				<mod:numero titulo="Nota de Empenho" largura="5" var="notaEmpenho"
					extensoNum="sim" />
				<mod:oculto var="notaEmpenhonumextenso"
					valor="${notaEmpenhonumextenso}" />
			</mod:grupo>
		</mod:grupo>

		<mod:grupo titulo="Prazo de Vigência e de Execução">
			<mod:numero titulo="Prazo para execução dos serviços" largura="10"
				var="prazExecucao" extensoNum="sim" />
			<mod:oculto var="prazExecucaonumextenso"
				valor="${prazExecucaonumextenso }" />
			<mod:grupo>
				<mod:numero titulo="Prazo de vigência do contrato" largura="10"
					var="prazVigencia" extensoNum="sim" />
				<mod:oculto var="prazVigencianumextenso"
					valor="${prazVigencianumextenso}" />
			</mod:grupo>
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

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } -
		Contrato nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1</font></u>

		</head>
		<body>
		<font size="2">
		<p align="center"><b>TERMO DE CONTRATO N.º&nbsp;
		${termoContrato }/(${anoContrato }) </b></p>

		<p align="right"><b>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CONTRATO
		DE PRESTAÇÃO DE SERVIÇOS DE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ENGENHARIA
		PARA ${objetivo }, QUE ENTRE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SI
		FAZEM A JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JANEIRO
		E A EMPRESA ${nomeEmpresa }</b></p>

		<p align="center"><b>PROCESSO Nº &nbsp; ${n1 }/${n2 }/${n3 } -
		EOF</b></p>
		
		<p style="TEXT-INDENT: 1.5cm" align="justify">A justiça Federal de
		1&deg; Grau no Rio de Janeiro, com sede na Av.Rio Branco, 243 - Anexo
		I - 14&deg; andar, Centro/RJ, inscrita no C.N.P.J. sob o n&deg;
		05.424.540./0001-16, neste ato representada pelo Dr. <b>${nomeJuiz
		}</b>, Juiz Federal - Diretor do Foro, Identidade n&deg; <b>${identJuiz
		}</b> - <b>${orgEmissorJuiz }</b>, CPF: <b>${cpfJuiz }</b> doravante
		denominada CONTRATANTE, e a empresa <b>${nomeEmpresa }</b>,
		estabelecida na <b>${endEmpresa }</b>, inscrita no C.N.P.J sob o nº <b>${cnpj
		}</b>, TEL: <b>${tel }</b>, FAX: <b>${fax }<b>, representada neste
		ato pelo Sr.(a) <b>${nomeRepresentante }<b>, Identidade nº <b>${identRepresentante}</b>
		- <b>${orgEmissorRepresentante }</b>, CPF: <b>${cpfRepresentante }</b>,
		doravante denominada CONTRATADA, tendo em vista o decidido no Processo
		Administrativo nº <b>${n1 }</b>/<b>${n2 }</b>/<b>${n3 }</b> - EOF, por
		despacho do Exmº Sr. <b>${nomeJuiz }</b> Juiz Federal - Diretor do
		Foro, às Fls. <b>${folhas }</b> dos autos, firmam o presente contrato,
		nos termos e sujeitas as partes às normas da Lei nº 8.666/93 e às
		cláusulas contratuais a seguir:</p>


		<p align="left"><u><strong>CL&Aacute;USULA PRIMEIRA -
		DO OBJETO:</strong></u></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">1.1 - Constitui
		objeto do presente contrato a execução dos serviços de engenharia, no
		regime de empreitada por preço global R$ <b>${valorContrato}</b>, para
		(<b>${objeto }</b>), conforme especificações constantes nos anexos do
		edital do (<b>${edital }</b>) nº <b>${numEdital }</b>/ <b>${anoEdital
		}</b>, que fazem parte integrante deste contrato.</p>

		<p align="left"><u><strong>CL&Aacute;USULA SEGUNDA -
		DAS OBRIGAÇÕES DA CONTRATADA:</strong></u></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.1 - São obrigações
		da contratada:</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.1 - executar os serviços
		discriminados nas Especificações do (<b>${edital }</b>) nº <b>${numEdital
		}</b>/<b>${anoEdital }</b>, que integra o presente ajuste;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.2 - fornecer aos seus
		empregados crachás com fotografias, uniformes completos, vale-refeição
		no valor acordado no dissídio coletivo da categoria, seguro-saúde,
		seguro de acidentes pessoais e vales-transporte (em conformidade com a
		Lei 7418/85 e Decreto 95.247/87), bem como os equipamentos de proteção
		individual, adequados à execução de todos os serviços.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.3 - manter em situação
		empregatícia regular e legal os empregados que prestarem serviços em
		todas as dependências da Contratante, obedecendo as normas do
		Ministério do Trabalho, reservando-se a Contratante o direito de
		exigir a sua comprovação sempre que julgar necessário.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 1º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.4 - obedecer
		rigorosamente às normas vigentes de segurança e medicina do trabalho,
		para todos os tipos de atividade, sendo responsável por quaisquer
		danos físicos ou pessoais decorrentes de acidentes que venham a
		provocar.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.5 - cumprir com todas as
		obrigações constantes nas Especificações do (<b>${edital }</b>) nº <b>${numEdital
		}</b>/<b>${anoEdital }</b>, que integra o presente ajuste;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.6 - manter, durante toda
		a execução do contrato, em compatibilidade com as obrigações
		assumidas, todas as condições de habilitação e qualificação exigidas
		no (<b>${edital }</b>) nº <b>${numEdital }</b>/<b>${anoEdital }</b>.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.7 - A Contratada é
		responsável por:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.7.1
		- responder pelos danos causados diretamente à SJRJ ou a terceiros,
		decorrentes de sua culpa ou dolo, quando da execução dos serviços, não
		excluindo ou reduzindo essa responsabilidade a fiscalização ou o
		acompanhamento pela Contratante.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1.7.2
		- arcar com despesas decorrentes de qualquer infração, seja qual for,
		desde que praticadas por seus funcionários durante a execução dos
		serviços, ainda que no recinto da Contratante.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">2.2 - À Contratada
		caberá, ainda:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a) assumir todos os encargos
		previdenciários e obrigações sociais previstos na legislação social e
		trabalhista em vigor, obrigando-se a saldá-los na época própria, vez
		que os seus empregados não manterão nenhum vínculo empregatício com a
		Contratante;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b) responsabilizar-se por
		todas as providências e obrigações estabelecidas na legislação
		específica de acidentes do trabalho, quando, em ocorrência da espécie,
		forem vítimas os seus técnicos no desempenho dos serviços ou em
		conexão com eles, ainda que acontecido em dependência da Contratante;
		</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c) assumir todos os encargos
		de eventual demanda trabalhista, civil ou penal, relacionada aos
		serviços, originariamente ou vinculada por prevenção, conexão ou
		contingência;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;d) assumir, ainda, todos os
		encargos fiscais e comerciais resultantes deste Contrato.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 2º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">2.3 - A
		inadimplência da Contratada, com referência aos encargos estabelecidos
		no subitem 2.2, não transfere à Contratante a responsabilidade por seu
		pagamento, nem poderá onerar o objeto deste Contrato, razão pela qual
		a Contratada renuncia expressamente a qualquer vínculo de
		solidariedade, ativa ou passiva, com a Seção Judiciária do Rio de
		Janeiro.</p>

		<p><b><u>CL&Aacute;USULA TERCEIRA - DAS OBRIGAÇÕES DA
		CONTRATANTE:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">3.1 - Caberá à
		Contratante:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.1 - prestar as
		informações e os esclarecimentos que venham a ser solicitados pela
		Contratada;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.2 - assegurar-se da boa
		prestação dos serviços, verificando sempre o seu bom desempenho;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.3 - fiscalizar o
		cumprimento das obrigações assumidas pela Contratada, inclusive quanto
		à continuidade da prestação dos serviços que, ressalvados os casos de
		força maior, justificados e aceitos pela Contratante, não deva ser
		interrompida;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.4 - emitir, por
		intermédio da (${secao }), da Área de Infra-estrutura - INF, pareceres
		sobre os atos relativos à execução do contrato, em especial, quanto ao
		acompanhamento e fiscalização da prestação dos serviços, à exigência
		de condições estabelecidas nesta Especificação e à proposta de
		aplicação de sanções;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.5 - tornar disponível as
		instalações e os equipamentos necessários à prestação dos serviços,
		quando for o caso;</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.6 - acompanhar e
		fiscalizar o andamento dos serviços, por intermédio da <b>${secao
		}</b> - (<b>${sigla }</b>), da Área de Infra-estrutura - INF.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 3º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 4</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA QUARTA - DO PREÇO:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">4.1 - O valor global
		deste contrato é de R$ <b>${valorContrato }</b> <b>${valorContratovrextenso
		}</b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">4.2 - Nos preços
		oferecidos pela contratada já estão incluídas todas as despesas com
		encargos tributários, sociais, trabalhistas e quaisquer outras que se
		fizerem necessárias ao cumprimento das obrigações decorrentes deste
		contrato, bem como os demais ônus determinados nos Anexos do Edital do
		(<b>${edital }</b>) nº <b>${numEdital }</b>/ <b>${anoEdital }</b>,
		que faz parte integrante deste Contrato.</p>

		<p><b><u>CL&Aacute;USULA QUINTA - DO PAGAMENTO:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.1 - O pagamento
		será efetuado com base em <b>${nparcelas}</b> (<b>${nparcelasnumextenso
		}</b>) medições mensais, as quais contemplarão, apenas, serviços
		concluídos e aprovados pela Fiscalização, não sendo efetuados
		pagamentos parciais por entrega de materiais.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.1.1 - A primeira medição
		será realizada após <b>${dia }</b> (<b>${dianumextenso}</b>) dias da assinatura do Contrato, estando
		o pagamento da última medição sujeito ao disposto no item
		(&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;)
		das Especificações, que constituem o Anexo I do (<b>${edital }</b>) nº <b>${numEdital
		}</b>/ <b>${anoEdital }</b>, mediante crédito em conta corrente da
		contratada, por meio de ordem bancária, até o 10º (décimo) dia útil da
		apresentação da nota fiscal, devidamente atestada pela Área de
		Infra-Estrutura, salvo eventual atraso de distribuição de recursos
		financeiros efetuados pelo Conselho da Justiça Federal.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.2 - Será
		considerada como data do pagamento a data da emissão da Ordem
		Bancária.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.2.1 - No período acima não
		haverá atualização financeira.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.2.1.1
		- Nos casos de eventuais atrasos de pagamento, desde que a Contratada
		não tenha concorrido de alguma forma para tanto, fica convencionado
		que o índice de compensação financeira devida pela Contratante, entre
		a data acima referida e a correspondente ao efetivo adimplemento da
		parcela, terá a aplicação da seguinte fórmula:</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 4º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 5</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EM = I x N x VP</p>

		<br>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Onde:</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EM = Encargos Moratórios</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;N = Número de dias entre a
		data prevista para o pagamento e a do efetivo pagamento.</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;VP = Valor da parcela a ser
		paga</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TX = Percentual da taxa
		anual = 6%</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;I = Índice de compensação
		financeira = 0,0001644, assim apurado:</p>
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;I =
		(TX)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;I =
		(6/100)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;I = 0,0001644 <br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;365&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;365
		</p>


		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		5.2.1.1.1 - A compensação financeira prevista nesta Condição será
		incluída na nota fiscal/fatura seguinte ao da ocorrência.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.3 - Caso seja
		necessária a retificação da fatura por culpa da Contratada, a fluência
		do prazo de 10 (dez) dias úteis será suspensa, reiniciando-se a
		contagem a partir da reapresentação da fatura retificada.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.4 - A Seção
		Judiciária do Rio de Janeiro reserva-se o direito de não efetuar o
		pagamento se, no ato da atestação, os materiais fornecidos/serviços
		executados não estiverem em conformidade com as Especificações
		apresentadas e aceitas, nos termos do (<b>${edital }</b>) nº <b>${numEdital
		}</b>/ <b>${anoEdital }</b>, que integra o presente ajuste.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.5 - A Seção
		Judiciária do Rio de Janeiro poderá deduzir da importância a pagar os
		valores correspondentes a multas ou indenizações devidas pela
		licitante vencedora nos termos do (<b>${edital }</b>) nº <b>${numEdital
		}</b>/ <b>${anoEdital }</b>, que faz parte integrante do presente
		Contrato.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 5º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 6</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">5.6 - As notas fiscais deverão ser
		apresentada em 02 (duas) vias e entregues na <b>${secao }</b> /
		Infra-Estrutura, situada na Av. Rio Branco, 243 - Anexo I - 8º andar -
		Centro - Rio de Janeiro/RJ.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.7 - A contratada deverá comprovar,
		quando da apresentação da nota fiscal à Contratante, a regularidade
		perante a Seguridade Social e o Fundo de Garantia de Tempo de Serviço,
		através da CND e do CRF, dentro das respectivas validades, sob pena de
		não pagamento dos serviços prestados e de rescisão contratual, em
		atendimento ao disposto no parágrafo 3º do art. 195 da Constituição
		Federal, no art. 2º da Lei nº 9.012/95 e nos art. 55, inciso VIII, e
		78, inciso I, ambos da Lei nº 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.8 - Para fins de cumprimento do
		disposto no art. 31 da Lei nº 8.212/91, com a redação dada pela Lei nº
		9.711, de 20/11/98, e regulamentado por ato normativo do MPS/SRP, será
		retido, a cada pagamento, o percentual de 11% (onze por cento) do
		valor dos serviços contidos na Nota Fiscal apresentada pela
		Contratada.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">5.9 - As pessoas jurídicas não
		optantes pelo SIMPLES e aquelas que ainda não formalizaram a opção
		sofrerão a retenção de impostos/contribuições por esta Seção
		Judiciária no momento do pagamento, conforme disposto no art. 64 da
		Lei nº 9.430, de 27/12/96, regulamentado por ato normativo da
		Secretaria da Receita Federal e ISS, conforme normatização da
		Secretaria Municipal de Fazenda do local da prestação de serviços.</p>

		<p><b><u>CL&Aacute;USULA SEXTA - DAS PENALIDADES:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.1 - O não
		cumprimento pela contratada de qualquer uma das obrigações, dentro dos
		prazos estabelecidos por este contrato, sujeitá-la-á às penalidades
		previstas nos artigos 86 a 88 da Lei n° 8.666/93;</p>


		<p style="TEXT-INDENT: 1.5cm" align="justify">6.2 - As penalidades
		a que está sujeita a contratada inadimplente, nos termos da Lei no
		8.666/93, são as seguintes:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a) advertência;</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b) multa;</p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c) suspensão temporária de
		participar em licitação e impedimento em contratar com a Administração
		por prazo não superior a 02 (dois) anos.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 6º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 7</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">6.3 - A recusa
		injustificada em assinar o Contrato, aceitar ou retirar o instrumento
		equivalente, dentro do prazo estabelecido pela Administração, sujeita
		o adjudicatário à penalidade de multa de até 10% (dez por cento) sobre
		o valor da adjudicação, independentemente da multa estipulada no
		subitem 6.4.2.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.4 - A inexecução
		total ou parcial do contrato poderá acarretar, a critério da
		Administração, a aplicação das multas, alternativamente:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.4.1 - Multa compensatória
		de até 30% (trinta por cento) sobre o valor equivalente à obrigação
		inadimplida.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.4.2 - Multa correspondente
		à diferença entre o valor total porventura resultante de nova
		contratação e o valor total que seria pago ao adjudicatário.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.4.3 - Multa de 50%
		(cinqüenta por cento) sobre o valor global do contrato, no caso de
		inexecução total do mesmo.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.5 - A atualização
		dos valores correspondentes à multa estabelecida no item 6.4 far-se-á
		a partir do 1º(primeiro) dia, decorrido o prazo estabelecido no item
		6.7.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.6 - Os atrasos
		injustificados no cumprimento das obrigações assumidas pela contratada
		sujeitá-la-á à multa diária, até a data do efetivo adimplemento, de
		0,3% (três décimos por cento), calculada à base de juros compostos,
		sem prejuízo das demais penalidades previstas na Lei nº 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.6.1 - A multa moratória
		estabelecida ficará limitada à estipulada para inexecução parcial ou
		total do contrato (subitem 6.4.1).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.6.2 - O período de atraso
		será contato em dias corridos.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.7 - A multa deverá
		ser paga no prazo de 30 (trinta) dias, contados da data do recebimento
		da intimação por via postal, ou da data da juntada aos autos do
		mandado de intimação cumprido, conforme o caso.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 7º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">6.8 - Caso a multa
		não seja paga no prazo estabelecido no item 6.7, deverá ser descontada
		dos pagamentos ou da garantia do respectivo contrato, ou, ainda,
		cobrada judicialmente, se for o caso.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.8.1 - Se a multa for
		superior ao valor da garantia prestada, além da perda desta,
		responderá o contratado pela diferença faltante.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.9 - A atualização
		dos valores correspondentes às multas estabelecidas neste Contrato
		dar-se-á através do IPCA-E/IBGE, tendo em vista o disposto no art. 1º
		da Lei nº 8.383, de 30/12/91 ou de outro índice, posteriormente
		determinado em lei.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.10 - A contagem
		dos prazos dispostos neste Contrato obedecerá ao disposto no art. 110,
		da Lei nº 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">6.11 - Os
		procedimentos de aplicação e recolhimento das multas foram
		regulamentadas pela pela IN 24-12, do Egrégio Tribunal Regional
		Federal da 2ª Região.</p>

		<p><b><u>CL&Aacute;USULA SÉTIMA - DA GARANTIA CONTRATUAL:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">7.1 - A contratada
		presta, neste ato, garantia contratual nos termos do art. 56,
		parágrafo 1º, da Lei 8.666/93, no valor de R$, equivalente a 5% (cinco
		por cento) do valor global deste contrato.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">7.2 - A garantia
		acima mencionada somente poderá ser levantada após o término deste
		contrato e emissão do Termo de Recebimento Definitivo dos Serviços.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">7.3 - A garantia
		acima mencionada responderá, ainda, pelas multas que porventura venham
		a ser aplicadas à Contratada, em virtude de inadimplemento.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 8º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 9</font></u>

		<font size="2">
		<p><b><u>CL&Aacute;USULA OITAVA - DA DOTAÇÃO ORÇAMENTÁRIA:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">8.1 - As despesas
		decorrentes da Contratação dos serviços, objeto deste contrato,
		correrão à conta dos recursos consignados à Seção Judiciária do Rio de
		Janeiro, para o corrente exercício, conforme o especificado abaixo:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Programa de Trabalho: <b>${progTrabalho
		}</b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Elemento de Despesa: <b>${elemDespesa
		}</b></p>

		<p style="TEXT-INDENT: 1.5cm">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nota de Empenho: <b>${notaEmpenho
		}</b></p>

		<p><b><u>CL&Aacute;USULA NONA - DO PRAZO DE VIGÊNCIA E DE
		EXECUÇÃO:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">9.1 - O prazo para
		execução dos serviços objeto deste Contrato é de <b>${prazExecucao
		}</b> (<b>${prazExecucaonumextenso}</b>) dias corridos, a contar da data
		da assinatura do mesmo, conforme item
		(&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;)
		do Anexo I do (<b>${edital }</b>) nº <b>${numEdital }</b>/<b>${anoEdital
		}</b> (Especificação).</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">9.2 - O prazo de
		vigência do contrato será de <b>${prazVigencia }</b> (<b>${prazVigencianumextenso
		}</b>) dias, a contar da assinatura do contrato.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA - DA RESCISÃO:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">10.1 - A inexecução
		parcial ou total do Contrato enseja a sua rescisão, conforme disposto
		nos artigos 77 a 80 da Lei nº 8.666/93, sem prejuízo da aplicação das
		penalidades previstas na Cláusula Sexta.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">10.2 - A rescição do
		Contrato poderá ser:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.2.1 - determinada por ato
		unilateral e escrito da Administração da Seção Judiciária do Rio de
		Janeiro, nos casos enumerados nos inciso I a XII e XVII do art. 78 da
		lei mencionada.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		10.2.1.1 - Nos casos previstos nos incisos I a VIII e XI a XVII, a
		Administração notificará a Contratada, através de Ofício, com prova de
		recebimento.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		10.2.1.2 - Nos casos previstos nos incisos IX e X, a rescisão dar-se-á
		de pleno direito, independentemente de aviso ou interpelação judicial
		ou extrajudicial.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 9º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 10</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.2.2 - amigável, por
		acordo entre as partes, desde que haja conveniência para a
		Administração da Seção Judiciária do Rio de Janeiro.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.2.3 - judicial, nos
		termos da legislação vigente sobre a matéria.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">10.3 - A rescisão
		administrativa ou amigável será precedida de autorização escrita e
		fundamentada da autoridade competente e as rescisões determinadas por
		ato unilateral da Administração serão formalmente motivadas nos autos
		do processo, assegurado o contraditório e a ampla defesa da
		Contratada.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">10.4 - A rescisão
		será determinada na forma do art. 79 da Lei nº 8.666/93.</p>

		<p><b><u>CLÁUSULA DÉCIMA PRIMEIRA - DO PRAZO DE GARANTIA E
		MANUTENÇÃO DA OBRA:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">11.1 - O prazo de
		garantia de materiais e serviços componentes da reforma executada pela
		Contratada, será de 05 (cinco) anos, a contar do Recebimento
		Definitivo, respeitado o que dispõe o Código Civil Brasileiro, bem
		como as demais normas legais e a jurisprudência aplicáveis.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA SEGUNDA - DOS RECURSOS
		ADMINISTRATIVOS:</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">12.1 - Aplica-se o
		disposto no art. 109 da lei nº 8.666/93.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA TERCEIRA - DA DOCUMENTAÇÃO
		COMPLEMENTAR: </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">13.1 - Os serviços
		contratados obedecerão ao estipulado neste instrumento, bem como as
		obrigações assumidas nos documentos a seguir indicados, os quais ficam
		fazendo parte integrante e complementar deste contrato,
		independentemente de transcrição, no que não contrariem as
		estipulações aqui firmadas:</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a) (<b>${edital }</b>) nº <b>${numEdital
		}</b>/ <b>${anoEdital }</b> e seus ANEXOS.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b) Proposta datada de
		______/_______/_______, exibida pela Contratada, contendo prazo,
		preço, discriminação e especificação dos serviços a serem executados.
		</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 10º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 11</font></u>

		<font size="2">
		<p><b><u>CLÁUSULA DÉCIMA QUARTA - DAS CONDIÇÕES DE
		RECEBIMENTO PROVISÓRIO E DEFINITIVO: </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">14.1 - Os serviços
		serão recebidos, provisoriamente, pelo responsável por seu
		acompanhamento e fiscalização, mediante termo circunstanciado,
		assinado pelas partes, em até 15 (quinze) dias da comunicação escrita
		da Contratada, notificando o término e aprovação dos serviços,
		realizados pela Fiscalização da Contratante, consoante disposto no
		art. 73, I da Lei nº 8.666/93.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">14.2 - Os serviços
		serão recebidos definitivamente, por servidor ou Comissão designada
		pela Administração, mediante Termo de Recebimento Definitivo, assinado
		pelas partes, lavrado até 90 (noventa) dias contados do recebimento
		provisório, desde que tenham sido atendidas todas as solicitações da
		Fiscalização, referentes a defeitos ou imperfeições registrados no
		Termo de Recebimento Provisório ou que venham a ser verificados, após
		a lavratura do Termo, em qualquer elemento constante dos serviços
		executados.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;14.2.1 - Havendo rejeição do
		serviço por parte do servidor ou Comissão de Recebimento da
		Contratante, na hipótese de estarem em desacordo com as especificações
		e condições com que foram licitados, a Contratante estipulará prazo
		para a Contratada repará-los, sem ônus para a Contratante, ficando
		suspensa a concessão do recebimento definitivo, até que todas as
		pendências apontadas sejam solucionadas.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">14.3 - Os
		recebimentos provisório e definitivo não excluem a responsabilidade da
		Contratada, conforme disposto no parágrafo 2º do art. 73 da Lei nº
		8.666/93.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA QUINTA - DA FISCALIZAÇÃO:
		</u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">15.1 - A execução
		dos serviços será acompanhada e fiscalizada pelo representante da
		Contratante, expressamente designado pela Administração.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">15.2 - O
		representante anotará em registro próprio todas as ocorrências
		relacionadas com a execução dos serviços mencionados, determinando o
		que for necessário à regularização das faltas ou defeitos observados.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">15.3 - As decisões e
		providências que ultrapassarem a competência do representante serão
		solicitadas a seus superiores em tempo hábil para a adoção das medidas
		convenientes.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">15.4 - O exercício
		da fiscalização pela Contratante não excluirá a responsabilidade da
		Contratada.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA SEXTA - DAS CONSIDERAÇÕES
		FINAIS: </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">16.1 - O contrato
		poderá ser aditado nos termos previstos no art. 65 da Lei nº 8.666/93,
		com a apresentação das devidas justificativas.</p>
		</font>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<!-- Fim da 11º página -->

		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		<u><font size="2">Processo nº ${n1}/${n2 }/${n3 } - Contrato
		nº ${termoContrato }/(${anoContrato }) - ${nomeEmpresa
		}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 12</font></u>

		<font size="2">
		<p style="TEXT-INDENT: 1.5cm" align="justify">16.2 - Após o
		término deste Contrato, a Contratada fornecerá Termo de Quitação à
		Seção de Contratos da Subsecretaria de Material e Patrimônio, no prazo
		máximo de 15 (quinze) dias, em papel timbrado, devidamente assinado
		pelo representante legal, carimbado e datado.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">16.3 - Na hipótese
		de o Termo de Quitação não ser fornecido dentro do prazo supracitado,
		será considerada como plena, rasa e total a quitação em favor da Seção
		Judiciária do Rio de Janeiro dos débitos referentes à presente
		contratação.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA SÉTIMA - DA PUBLICAÇÃO: </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">17.1 - O presente
		contrato será publicado no Diário Oficial da União, na forma de
		extrato, de acordo com o que determina o parágrafo único do artigo 61
		da Lei n° 8.666/93.</p>

		<p><b><u>CL&Aacute;USULA DÉCIMA OITAVA - DO FORO: </u></b></p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">18.1 - Para dirimir
		as questões oriundas do presente contrato, fica eleito o Foro da
		Justiça Federal - Seção Judiciária do Rio de Janeiro.</p>

		<p style="TEXT-INDENT: 1.5cm" align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;E por estarem ajustados, é
		lavrado o presente termo de contrato, extraído em 03 (três) vias de
		igual teor e forma, que depois de lido e achado conforme vai assinado
		pelas partes contratantes.</p>
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
							EMPRESA
		<br>
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


