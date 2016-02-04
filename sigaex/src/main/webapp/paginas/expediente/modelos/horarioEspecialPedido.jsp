<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CONCESSÃO DE HORARIO ESPECIAL AO SERVIDOR PUBLICO ESTUDANTE 
- PEDIDO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
			<mod:memo colunas="60" linhas="3" titulo="PROPOSTA" var="detalhesProposta" />
	</mod:entrevista>
	<mod:documento>
		<%--<mod:valor var="texto_requerimento">	
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria <B>HORÁRIO ESPECIAL AO SERVIDOR ESTUDANTE</B>, conforme o art. 98 e § 1º da Lei n.º 8.112/90, com a redação dada pela 
		Lei n.º 9.527/97 e Resolução n.º 5/2008 do Conselho da Justiça 
		Federal, em face da incompatibilidade entre o horário escolar e o da SJRJ, de 
		acordo com a proposta abaixo:
		</p>
		${detalhesProposta}
		</mod:valor>--%>
		<mod:valor var="texto_requerimento2">
			<c:import url="/paginas/expediente/modelos/inc_tit_SraDiretoraSubsecretariaRH.jsp" />
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria <B>HORÁRIO ESPECIAL AO SERVIDOR ESTUDANTE</B>, conforme o art. 98 e § 1º da Lei n.º 8.112/90, com a redação dada pela 
			Lei n.º 9.527/97 e Resolução n.º 5/2008 do Conselho da Justiça 
			Federal, face à incompatibilidade entre o horário escolar e o da SJRJ, de 
			acordo com a proposta abaixo:
			</p>
			${detalhesProposta}
			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
			<p align="center">${doc.dtExtenso}</p>
			<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?apenasCargo=sim" />
			<c:import url="/paginas/expediente/modelos/inc_cienteAssSupHierarquico.jsp" />
				<p><font size='1'> 		 
				<b>Anexar:</b>
				<ul>
				<li><i>
					Documentação comprobatória de matrícula no estabelecimento de 
					ensino e do horário das respectivas aulas, encaminhada através 
					do titular da unidade.
				</i></li>
				<li><i>
					Declaração do servidor, comprometendo-se a apresentar, até o 30º (trigésimo) dia após o início de cada semestre, ou ano letivo, conforme o caso, documento comprobatório de freqüência regular no período anterior, bem como solicitar o cancelamento do horário especial, quando cessarem os motivos 
					que ensejaram a sua concessão, sob pena de cancelamento, 
					atendendo ao disposto nos <b>art. 8º, I e 9º da Resolução 
					n.º 5/2008</b> - CJF.
				</i></li>
				</ul>
			</font>
			</p>
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>	
</mod:documento>
</mod:modelo>
