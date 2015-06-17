<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
VACÂNCIA -->

<script language="javascript">
var newwin = null;
</script>

<c:set var="textoRodape"
	value="Documentos imprescindíveis para instrução do processo administrativo, consoante Resolução n.º 148, de 26/05/95, do Conselho da Justiça Federal e Ordem de Serviço nº 04, de 03/10/2001, da Direção do Foro."
	scope="request" />
<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>

		<mod:grupo titulo="DETALHES DA VACÂNCIA">
			<mod:texto titulo="Cargo a ser ocupado" var="cargoASerOcupado" />
			<mod:texto titulo="Órgão" var="orgao" />
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="Data da Posse" var="dataDaPosse" />
		</mod:grupo>
		<%--<mod:selecao var="numDependentes" titulo="Numero de dependentes"
			opcoes="1;2;3;4;5;6;7;8;9" />--%>
		<mod:grupo titulo="CONTATOS">
			<mod:texto titulo="Endereço" var="endereco" largura="70" />
			<mod:texto titulo="Cep" largura="13" var="cep" />
		</mod:grupo>

		<mod:grupo>
			<mod:texto titulo="Telefone (1)" var="tel1" />
			<mod:texto titulo="Telefone (2)" var="tel2" />
		</mod:grupo>

		<mod:grupo titulo="DOCUMENTOS ANEXOS AO REQUERIMENTO">
			<mod:caixaverif
				titulo="Declaração de bens atualizada até a data da vacância, com os respectivos valores;"
				var="declaraBensAnex" reler="Não" />
		</mod:grupo>

		<mod:grupo>
			<mod:caixaverif titulo="Declaração de Devolução do Token;"
				var="declaraDevolvToken" reler="Não" />
		</mod:grupo>

		<mod:grupo>
			<mod:caixaverif
				titulo="Cópias da declaração de Imposto de renda e recibo de entrega ou declaração de isenção;"
				var="IrpfCopiaAnex" reler="Não" />
		</mod:grupo>
		<mod:grupo>
			<mod:caixaverif titulo="Cópia do CPF;" var="cpfAnex" reler="Não" />
		</mod:grupo>
		<mod:grupo>
			<mod:radio titulo="Termo de posse no novo cargo"
				var="termoPosseComprom" valor="posse" reler="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:radio titulo="Termo de compromisso"
				var="termoPosseComprom" valor="compromisso" reler="sim" />
		</mod:grupo>

		<c:if test="${(param['termoPosseComprom'] eq 'compromisso') || (requestScope['termoPosseComprom'] eq 'compromisso')}">
			<mod:obrigatorios />
			<br/>
			<mod:grupo>
				<mod:caixaverif titulo="Comprometo-me a entregar o termo de posse no prazo de 02 (dois) dia úteis, contados da data da posse no novo cargo público inacumulável, para fins de instrução do processo administrativo de vacância"
					var="comprometoMe" reler="Não" obrigatorio="Sim" />
			</mod:grupo>	
		</c:if>
		
		<c:set var="labelOutrosDocs">Outros documentos fora da lista. <b>Separar por vírgula</b></c:set>
		<mod:texto titulo="${labelOutrosDocs}" var="outroDoc" largura="50"/>
		<%--<mod:grupo>
			<mod:memo colunas="80" linhas="1"
				titulo="Outros documentos fora da lista. Separar por vírgula." var="outroDoc" />
		</mod:grupo>--%>

		<mod:grupo titulo="ITENS QUE ESTÃO SENDO DEVOLVIDOS">
			<mod:grupo>
				<mod:caixaverif
					titulo="Crachá Funcional e Carteira Funcional à SECAD;"
					var="crachaDev" reler="Não" />
			</mod:grupo>

			<mod:grupo>
				<mod:caixaverif
					titulo="Carteiras do Plano de Saúde do Titular e do(s) dependente(s) à SEBEN;"
					var="carteiraSaudeDev" reler="Não" />
			</mod:grupo>

			<mod:grupo>
				<mod:caixaverif
					titulo="Documento emitido pelo órgão promotor do curso de formação de que não percebeu o auxílio financeiro;"
					var="documentoCurso" reler="Não" />
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif
					titulo="Comprovante de recolhimento de contribuição para a Previdência Social, do servidor público, durante o curso de formação à CLCP;"
					var="comprovanteContribuicao" reler="Não" />

			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif
					titulo="Comprovante de Frequência durante o curso de formação à CLCP."
					var="comprovanteFrequencia" reler="Não" />

			</mod:grupo>
			<c:set var="labelOutrosDocs">Outros itens fora da lista. <b>Separar por vírgula</b></c:set>
		<mod:texto titulo="${labelOutrosDocs}" var="outroItem" largura="50"/>
		</mod:grupo>
		<br/>
		<mod:grupo><mod:mensagem titulo="Atenção"
				texto="preencha o destinatário com Seleg e, após finalizar, transfira para a Seleg." vermelho="Sim" /></mod:grupo>
		<br/>
		<c:set var="conteudo1">
			Acarreta perda de vínculo com a Administração Pública, com pagamento de indenização de férias. 
		</c:set>
		<c:set var="conteudo2">
			Acarreta manutenção do vínculo com a Administração Pública, o que possibilita averbação de tempo para férias (não havendo pagamento de indenização de férias) e manutenção de determinadas vantagens pessoais.
		</c:set>
		<c:set var="textoCiencia">
			<b>Estou ciente das diferentes consequências entre a  
			<span onmouseover="this.style.cursor='hand';" 
			onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste',null,'height=50,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${conteudo1}');">
				<u> exoneração a pedido</u></span> 
			e a 
			<span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close; newwin.close(); newwin = window.open('teste2',null,'height=125,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${conteudo2}');"><u>
			vacância por posse em outro cargo inacumulável</u></span> </b>
		</c:set>
		<mod:obrigatorios />
			<br/>
		<mod:caixaverif titulo="${textoCiencia}" var="ciente" obrigatorio="Sim"/>
	</mod:entrevista>

	<mod:documento>

		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo},
			${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao} vem requerer a Vossa Excelência
			que se digne encaminhar o requerimento de vacância por posse em outro
			cargo inacumulável, em anexo, ao E.
			Tribunal Regional Federal da 2ª Região.</p>

			<p style="TEXT-INDENT: 2cm" align="justify">Informa, ainda, o
			endereço atualizado para fins de correspondência:</p>
	
		${endereco}.
		<br>
		${cep}.
		<br>
			<br>

			<c:if test="${not empty tel1 && not empty tel2}">
				Telefones para contato:
		</c:if>
			<c:if test="${not empty tel1 && empty tel2}">
				Telefone para contato:
		</c:if>
			<br>
			<c:if test="${not empty tel1}">
				<br>
			 	Telefone (1): ${tel1}
			</c:if>
			<c:if test="${not empty tel2}">
				<br>
			 	Telefone (2): ${tel2}
			</c:if>
			<br>
			<br>
		</mod:valor>
		<mod:valor var="texto_requerimento2">
			<%--<c:set var="semEspacos" value="Sim" scope="request" />--%>
			<c:import
				url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegi.jsp" />
			<p style="TEXT-INDENT: 2cm" align="justify">
			<p style="TEXT-INDENT: 1.5cm; font-size: 12px" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo},
			${doc.subscritor.padraoReferencia}, matrícula
			${doc.subscritor.matricula}, lotado(a) no(a)
			${doc.subscritor.lotacao.descricao}, vem requerer a
			Vossa Excelência <b>vacância</b> do cargo público que ora ocupa, com
			base no art. 33, VIII, da Lei 8.112/90, a partir do dia
			${dataDaPosse}, em razão de posse no cargo de ${cargoASerOcupado} do Quadro de
			Pessoal do(a) ${orgao}.
			<p />
			<p style="TEXT-INDENT: 1.5cm; font-size: 12px" align="justify">Está ciente das diferentes conseqüências entre exoneração a pedido e vacância por posse
			em outro cargo inacumulável.<p />
			<p style="TEXT-INDENT: 1.5cm; font-size: 12px" align="justify"><b><i>Em
			anexo, encontram-se o(s) seguinte(s) documento(s):</b></i></p>
			<ul style="font-size: 12px">
				<c:if test="${declaraDevolvToken=='Sim'}">
					<li>Declaração de Devolução do Token;</li>
				</c:if>
				
				<c:if test="${declaraBensAnex=='Sim'}">
					<li>Declaração de bens atualizada até a data da vacância,
					com os respectivos valores;</li>
				</c:if>
				<c:if test="${IrpfCopiaAnex=='Sim'}">
					<li>Cópias da declaração de Imposto de renda e recibo de
					entrega ou declaração de isenção;</li>
				</c:if>
				<c:if test="${cpfAnex=='Sim'}">
					<li>Cópia do CPF;</li>
				</c:if>
				<c:if test="${termoPosseComprom == 'posse'}">
					<li>Termo de posse no novo cargo;</li>
				</c:if>
				
				<c:if test="${termoPosseComprom == 'compromisso'}">
					<li>Termo de compromisso;</li>
				</c:if>

				<c:if test="${not empty outroDoc}">
					<li>${outroDoc}.</li>
				</c:if>
			</ul>
			<p style="TEXT-INDENT: 1.5cm; font-size: 12px" align="justify"><b><i>Declara,
			ainda, ter devolvido às respectivas Seções:</b></i></p>
			<ul style="font-size: 12px">
				<c:if test="${crachaDev=='Sim'}">
					<li>Crachá Funcional e Carteira Funcional à SECAD;</li>
				</c:if>
				<c:if test="${carteiraSaudeDev=='Sim'}">
					<li>Carteira(s) do Plano de Saúde do Titular e
					do(s) dependente(s) à SEBEN;</li>
				</c:if>
				<c:if test="${documentoCurso=='Sim'}">
					<li>Documento emitido pelo órgão promotor do curso de formação
					de que não percebeu o auxílio financeiro á CLCP;</li>
				</c:if>
				<c:if test="${comprovanteContribuicao=='Sim'}">
					<li>Comprovante de recolhimento de contribuição para a Previdência Social, do servidor público, durante o curso de formação à CLCP;</li>
				</c:if>
				<c:if test="${comprovanteFrequencia=='Sim'}">
					<li>Comprovante de Frequência durante o curso de formação á
					CLCP;</li>
				</c:if>
				<c:if test="${ not empty outroItem }">
					<li>${outroItem}.</li>
				</c:if>
			</ul>

			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
			<br/>
			<p align="center">${doc.dtExtenso}</p>
			<c:import
				url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

			<c:import
				url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>
	</mod:documento>
</mod:modelo>
