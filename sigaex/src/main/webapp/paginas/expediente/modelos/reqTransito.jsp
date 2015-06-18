
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script language="javascript">
var newwin = null;
</script>

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>


		<c:set var="conteudo1">
Art. 18.  O servidor que deva ter exercício em outro município em razão de ter sido removido, redistribuído, requisitado, cedido ou posto em exercício provisório terá, no mínimo, dez e, no máximo, trinta dias de prazo, contados da publicação do ato, para a retomada do efetivo desempenho das atribuições do cargo, incluído nesse prazo o tempo necessário para o deslocamento para a nova sede. (Redação dada pela Lei nº 9.527, de 10.12.97)
		</c:set>
		<c:set var="conteudo2">
§ 1o  Na hipótese de o servidor encontrar-se em licença ou afastado legalmente, o prazo a que se refere este artigo será contado a partir do término do impedimento. (Parágrafo renumerado e alterado pela Lei nº 9.527, de 10.12.97)
			
		</c:set>
		<c:set var="conteudo3">
§ 2o  É facultado ao servidor declinar dos prazos estabelecidos no caput.  (Incluído pela Lei nº 9.527, de 10.12.97)
			
		</c:set>
		<c:set var="conteudo4">
Art. 102.  Além das ausências ao serviço previstas no art. 97, são considerados como de efetivo exercício os afastamentos em virtude de:
			
		</c:set>
		<c:set var="conteudo5">
I - férias;
			
		</c:set>
		<c:set var="conteudo6">
II - exercício de cargo em comissão ou equivalente, em órgão ou entidade dos Poderes da União, dos Estados, Municípios e Distrito Federal;
			
		</c:set>
		<c:set var="conteudo7">
III - exercício de cargo ou função de governo ou administração, em qualquer parte do território nacional, por nomeação do Presidente da República; 
			
		</c:set>
		<c:set var="conteudo8">
IV - participação em programa de treinamento regularmente instituído ou em programa de pós-graduação stricto sensu no País, conforme dispuser o regulamento; (Redação dada pela Lei nº 11.907, de 2009)
			
		</c:set>
		<c:set var="conteudo9">
V - desempenho de mandato eletivo federal, estadual, municipal ou do Distrito Federal, exceto para promoção por merecimento;
			
		</c:set>
		<c:set var="conteudo10">
VI - júri e outros serviços obrigatórios por lei; 
			
		</c:set>
		<c:set var="conteudo11">
VII - missão ou estudo no exterior, quando autorizado o afastamento, conforme dispuser o regulamento; (Redação dada pela Lei nº 9.527, de 10.12.97)
			
		</c:set>
		<c:set var="conteudo12">
VIII - licença:
			
		</c:set>
		<c:set var="conteudo13">
a) à gestante, à adotante e à paternidade; 
			
		</c:set>
		<c:set var="conteudo14">
b) para tratamento da própria saúde, até o limite de vinte e quatro meses, cumulativo ao longo do tempo de serviço público prestado à União, em cargo de provimento efetivo; (Redação dada pela Lei nº 9.527, de 10.12.97)
			
		</c:set>
		<c:set var="conteudo15">
c) para o desempenho de mandato classista ou participação de gerência ou administração em sociedade cooperativa constituída por servidores para prestar serviços a seus membros, exceto para efeito de promoção por merecimento; (Redação dada pela Lei nº 11.094, de 2005)
			
		</c:set>
		<c:set var="conteudo16">
d) por motivo de acidente em serviço ou doença profissional; 
			
		</c:set>
		<c:set var="conteudo17">
e) para capacitação, conforme dispuser o regulamento; (Redação dada pela Lei nº 9.527, de 10.12.97)
			
		</c:set>
		<c:set var="conteudo18">
f) por convocação para o serviço militar;
			
		</c:set>
		<c:set var="conteudo19">
IX - deslocamento para a nova sede de que trata o art. 18;
			
		</c:set>
		<c:set var="conteudo20">
X - participação em competição desportiva nacional ou convocação para integrar representação desportiva nacional, no País ou no exterior, conforme disposto em lei específica;
			
		</c:set>
		<c:set var="conteudo21">
XI - afastamento para servir em organismo internacional de que o Brasil participe ou com o qual coopere. (Incluído pela Lei nº 9.527, de 10.12.97)
			
		</c:set>
		<c:set var="conteudo22">
Art. 44. Considera-se período de trânsito, para os fins desta Resolução, o prazo concedido ao servidor que deva ter exercício funcional em outra localidade por motivo de remoção, redistribuição, cessão ou exercício provisório, desde que implique mudança de residência.
			
		</c:set>
		<c:set var="conteudo23">
Parágrafo único. O afastamento de que trata este artigo é considerado como de efetivo exercício, fazendo jus o servidor durante esse período à remuneração do cargo efetivo.
			
		</c:set>
		<c:set var="conteudo24">
Art. 45. O período de trânsito será de, no mínimo, dez e, no máximo, trinta dias, contados da publicação do ato de remoção, redistribuição, cessão ou exercício provisório.
			
		</c:set>
		<c:set var="conteudo25">
&sect; 1&ordm; No caso de retorno do servidor, o prazo de que trata este artigo será contado:
			
		</c:set>
		<c:set var="conteudo26">
I - na hipótese de cessão, da publicação do ato de exoneração do cargo em comissão ou de dispensa da função comissionada ocupado no órgão cessionário;
			
		</c:set>
		<c:set var="conteudo27">
II - na hipótese de exercício provisório, da publicação do ato que determinar o retorno.
			
		</c:set>
		<c:set var="conteudo28">
&sect; 2&ordm; Na hipótese de o servidor encontrar-se em licença ou afastado legalmente, o período de trânsito será contado a partir do término do impedimento.
			
		</c:set>
		<c:set var="conteudo29">
&sect; 3&ordm; As licenças e afastamentos legais ocorridos durante o trânsito não suspendem o seu transcurso, podendo ser concedidos pelo tempo que sobejar.
			
		</c:set>
		<c:set var="conteudo30">
&sect; 4&ordm; Ao servidor é facultado desistir, total ou parcialmente, do período de trânsito.
			
		</c:set>
		<c:set var="conteudo31">
Art. 46. A concessão do período de trânsito caberá ao órgão competente para emissão do ato de cessão, remoção, exercício provisório e redistribuição.
			
		</c:set>
		<c:set var="conteudo32">
&sect; 1&ordm; Caberá ao órgão de origem o pagamento da remuneração do seu cargo efetivo durante o período de trânsito.
			
		</c:set>
		<c:set var="conteudo33">
&sect; 2&ordm; O período de trânsito deverá ser concedido juntamente com o ato de movimentação, mediante requerimento do servidor.
			
		</c:set>

		<mod:grupo>
			<mod:texto titulo="Lotação de origem" var="lotOrigem" largura="50"
				obrigatorio="Sim" />
			<br />
			<mod:texto titulo="Lotação de destino" var="lotDestino" largura="50"
				obrigatorio="Sim" />
			<br />
			<mod:texto titulo="E-mail" var="email" largura="50" obrigatorio="Sim" />
			<br />
			<mod:texto titulo="Número total dos dias de trânsito pretendidos"
				var="dias" largura="5" obrigatorio="Sim" />
			<br />
			<mod:texto titulo="Período do trânsito" var="periodo" largura="30"
				obrigatorio="Sim" />
			<br />
			<c:set var="textoCiencia">
Estou ciente:
			<b><br />
				- dos termos da Lei nº 8.112/90 <span
					onmouseover="this.style.cursor='hand';"
					onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('',null,'height=450,width=550,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes'); newwin.document.write('${conteudo1}<br /><br />'); newwin.document.write('${conteudo2}<br /><br />');newwin.document.write('${conteudo3}<br /><br />');newwin.document.write('${conteudo4}<br /><br />');newwin.document.write('${conteudo5}<br /><br />');newwin.document.write('${conteudo6}<br /><br />');newwin.document.write('${conteudo7}<br /><br />');newwin.document.write('${conteudo8}<br /><br />');newwin.document.write('${conteudo9}<br /><br />');newwin.document.write('${conteudo10}<br /><br />');newwin.document.write('${conteudo11}<br /><br />');newwin.document.write('${conteudo12}<br /><br />');newwin.document.write('${conteudo13}<br /><br />');newwin.document.write('${conteudo14}<br /><br />');newwin.document.write('${conteudo15}<br /><br />');newwin.document.write('${conteudo16}<br /><br />');newwin.document.write('${conteudo17}<br /><br />');newwin.document.write('${conteudo18}<br /><br />');newwin.document.write('${conteudo19}<br /><br />');newwin.document.write('${conteudo20}<br /><br />');newwin.document.write('${conteudo21}<br /><br />');"><u>artigos
				18 e 102</u></span> c/c os <span onmouseover="this.style.cursor='hand';"
					onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste',null,'height=450,width=550,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes'); newwin.document.write('${conteudo22}<br /><br />');newwin.document.write('${conteudo23}<br /><br />');newwin.document.write('${conteudo24}<br /><br />');newwin.document.write('${conteudo25}<br /><br />');newwin.document.write('${conteudo26}<br /><br />');newwin.document.write('${conteudo27}<br /><br />');newwin.document.write('${conteudo28}<br /><br />');newwin.document.write('${conteudo29}<br /><br />');newwin.document.write('${conteudo30}<br /><br />');newwin.document.write('${conteudo31}<br /><br />');newwin.document.write('${conteudo32}<br /><br />');newwin.document.write('${conteudo33}<br /><br />');"><u>artigos
				44, 45 e 46</u></span> da Resolução nº 03, de 10/3/2008 do Conselho da Justiça
				Federal. <br />
				- de que a concessão de trânsito implica mudança de residência e,
				assim, compromete-se a atualizar o endereço na Seção de Cadastro
				(SECAD), até o término do trânsito. </b>
			</c:set>
			<br />
			<mod:caixaverif titulo="${textoCiencia}" var="ciente"
				obrigatorio="Sim" marcado="Sim" />
			<br />
			<mod:obrigatorios />

		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<c:set var="tl" value="10pt" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 3cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF">
			<tr><td>
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
			</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >REQUERIMENTO DE TR&Acirc;NSITO</p></mod:letra></td>
						</tr>
						<tr>
							<td><br/><br/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->


		<mod:valor var="texto_requerimento">

			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, matrícula nº ${doc.subscritor.sigla},
			${doc.subscritor.cargo.nomeCargo}, lotado(a) no(a)
			${f:removeAcentoMaiusculas(lotOrigem)}, e-mail: ${email}, vem
			requerer a Vossa Excelência concessão de ${dias} dias de trânsito, no
			período de ${periodo}, tendo em vista remoção para
			${f:removeAcentoMaiusculas(lotDestino)}, nos termos da Lei Nº
			8.112/90 c/c artigos 44, 45 e 46 da Resolução Nº 03/2008-CJF. <br />
			<br />
			Declara estar ciente de que a concessão de trânsito implica mudança de
			residência e, assim, compromete-se a atualizar o endereço na Seção de
			Cadastro (SECAD), até o término do trânsito.</p>
		</mod:valor>




		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->


		</body>
		</html>
	</mod:documento>
</mod:modelo>