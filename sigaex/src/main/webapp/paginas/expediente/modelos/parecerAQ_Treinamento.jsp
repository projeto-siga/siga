<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>

		<mod:texto titulo="Número do processo ao qual o parecer se refere"
			var="numProc" largura="30" reler="ajax" idAjax="numProcAjax" />
		<mod:grupo depende="numProcAjax">
			<c:if test="${empty numProc}">
				<mod:mensagem titulo="Alerta"
					texto="o processo ao qual o parecer se refere deve ser preenchido."
					vermelho="Sim" />
			</c:if>
		</mod:grupo>
		<mod:texto titulo="Folhas" var="folhas" largura="6" reler="ajax"
			idAjax="folhasAjax" maxcaracteres="10" />
		<mod:grupo depende="folhasAjax">
			<c:if test="${empty folhas}">
				<mod:mensagem titulo="Alerta"
					texto="o campo acima deve ser preenchido com a seleção de páginas."
					vermelho="Sim" />
			</c:if>
		</mod:grupo>
		<mod:mensagem
			texto=""
			titulo="Observações:" vermelho="Sim"></mod:mensagem><br />
		<mod:mensagem
			texto="1) Não preencha o campo 'Destinatário';"
			titulo="" vermelho="Não"></mod:mensagem><br />
		<mod:mensagem
			texto="2) Antes de finalizar o documento, deverá ser incluído o cossignatário."
			titulo="" vermelho="Não"></mod:mensagem>

	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br /><br /><br /></tr>
						<tr>
							<td align="left"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							Processo N&ordm; ${numProc}</font></td>
						</tr>
						
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		<br />
		
		<p style="font-size: 11pt; text-indent: 3cm; font-weight: bold;">
		Exmo. Juiz Federal - Diretor do Foro:</p>
		
		
		<p style="text-align: justify; text-indent: 3cm;">
		Trata o presente processo da concessão e/ou alteração de
		<b>Adicional de Qualificação</b> (AQ), instituído pela Lei n&ordm;
		11.416/2006, em seu art. 14, abaixo transcrito, destinado aos
		servidores das carreiras dos Quadros de Pessoal do Poder Judiciário da
		União, em razão dos conhecimentos adicionais adquiridos em ações de
		treinamento.</p>
		
		<p style="text-align: justify; margin-left: 5cm; text-indent: 3cm;"><b>
		Art. 14. É instituído o Adicional de Qualificação - AQ destinado aos
		servidores das Carreiras dos Quadros de Pessoal do Poder Judiciário,
		em razão dos conhecimentos adicionais adquiridos em <u>ações de
		treinamento</u>, títulos, diplomas ou certificados de cursos de
		pós-graduação, em sentido amplo ou estrito, em áreas de interesse dos
		órgãos do Poder Judiciário a serem estabelecidas em regulamento.</b> (grifos nossos)</p>
		
		<p style="text-align: justify; text-indent: 3cm;">
		O AQ foi regulamentado pela Portaria Conjunta n&ordm; 01, de 07/03/2007, em seu
		Anexo I, da Presidente do STF e do CNJ e dos Presidentes dos Tribunais
		Superiores, do CJF, do Conselho Superior da Justiça do Trabalho e do
		TJ-DF, publicada no D.O.U. de 09/03/2007 (retificada pela Portaria
		n&ordm; 022, de 17/04/2007, do CJF, publicada no D.O.U.II de
		19/04/2007), e pela Resolução nº 126, de 22/11/2010, do CJF, publicada 
		no D.O.U. de 24/11/2010, além das orientações operacionais produzidas no 
		Encontro de Dirigentes de Recursos Humanos do Conselho da Justiça Federal e dos
		Tribunais Regionais Federais, realizado no período de 26 a 28/03/2007.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">
		A norma regulamentadora estabeleceu no art. 5&ordm; as áreas de interesse do
		Poder Judiciário Federal a seguir destacadas: serviços de
		processamento de feitos; execução de mandados; análise e pesquisa de
		legislação, doutrina e jurisprudência nos vários ramos do Direito;
		estudo e pesquisa do sistema judiciário brasileiro; organização e
		funcionamento dos ofícios judiciais e as inovações tecnológicas
		introduzidas; elaboração de pareceres jurídicos; redação; gestão
		estratégica, de pessoas, de processos, e da informação; material e
		patrimônio; licitações e contratos; orçamento e finanças; controle
		interno; segurança; transporte; tecnologia da informação; comunicação;
		saúde; engenharia; arquitetura, além dos vinculados a especialidades
		peculiares a cada órgão do Poder Judiciário da União, bem como aquelas
		que venham a surgir no interesse do serviço.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">
		Para a concessão do AQ decorrente de ações de treinamento, custeadas ou não pela 
		Administração, devem ser observadas as áreas de interesse especificadas 
		acima juntamente com as atribuições do cargo efetivo, ou com as atribuições 
		do cargo em comissão/função comissionada que, porventura, estejam sendo exercidas pelo servidor, ou, 
		ainda, com as atividades desenvolvidas pelo servidor em sua unidade de 
		lotação, conforme disposto no art. 17 da Resolução n&ordm; 126.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">Destaca-se que,
		no que se refere às ações de treinamento custeadas por esta Seção 
		Judiciária, os registros são averbados automaticamente pelo Núcleo de Capacitação e Desenvolvimento - NCDE.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">No que tange às ações 
		de treinamento não custeadas pela Administração, deve ser observado 
		o artigo 21 da Resolução nº 126.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">Norteando-se
		pelas normas legais anteriormente mencionadas, após a análise de todos
		os certificados e declarações enviadas, bem como dos registros
		constantes no NCDE, sugiro a <b>concessão e/ou
		alteração</b> do Adicional de Qualificação decorrente de Ações de
		Treinamento, conforme especificado no <b>Anexo I</b> (fls. ${folhas }), 
		nos percentuais indicados para cada servidor, observando-se os respectivos
		efeitos financeiros, pois preencheram todos os requisitos e condições para 
		a sua implementação.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">É o Parecer.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">À Superior
		Consideração.</p>

		<p style="text-align: justify; text-indent: 3cm;">RJ,
		${doc.dtDocDDMMYY}.</p>

		<c:import url="/paginas/expediente/modelos/inc_assinaturaSemCosig.jsp?formatarOrgao=sim" />
		
		<p style="text-align: justify; text-indent: 3cm;">Ratifico o
		parecer. À DIRFO.</p>
		
		<p style="text-align: justify; text-indent: 3cm;">RJ,
		${doc.dtDocDDMMYY}.</p>
		
		<c:import url="/paginas/expediente/modelos/inc_assinaturaSemSubsc.jsp?formatarOrgao=sim" />
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		</body>
		</html>
	</mod:documento>
</mod:modelo>
