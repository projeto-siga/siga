<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA A EXONERAÇÃO DO FUNCIONÁRIO PÚBLICO-->

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
		<mod:grupo titulo="DETALHES DO SERVIDOR">
			<mod:data titulo="Data da Exoneração" var="dataExoneracao" />
			<%--<mod:grupo>
				<mod:selecao
					titulo="Número de dependentes cadastrados no plano de saúde"
					var="numDependentes" opcoes="0;1;2;3;4;5;6;7;8;9;10" reler="sim" />
			</mod:grupo>--%>
		</mod:grupo>

		<mod:grupo titulo="CONTATOS PARA CORRESPONDÊNCIA">
			<mod:grupo><mod:texto titulo="Endereço" var="endereco" largura="70" />
			<mod:texto titulo="Cep" largura="13" var="cep" /></mod:grupo>
			<mod:texto titulo="Telefone (1)" var="tel1" />
			<mod:texto titulo="Telefone (2)" var="tel2" />
		</mod:grupo>

		<mod:grupo
			titulo="DETALHES DOS DOCUMENTOS QUE ESTÃO ANEXADOS PARA A EXONERAÇÃO">
			<mod:caixaverif titulo="Declaração de Devolução do Token;"
				var="declaraDevolvToken" reler="Não" />
			<mod:grupo></mod:grupo>
			<mod:caixaverif
				titulo="Declaração de bens atualizada até a data da exoneração, com os respectivos valores;"
				var="declaraBensAnex" reler="Não" />
			<mod:grupo></mod:grupo>
			<mod:caixaverif
				titulo="Cópias da declaração de Imposto de renda e recibo de entrega ou declaração de isenção;"
				var="IrpfCopiaAnex" reler="Não" />
			<mod:grupo></mod:grupo>
			<mod:caixaverif titulo="Cópia do CPF;" var="cpfAnex" reler="Não" />
			<mod:grupo></mod:grupo>
			<mod:memo colunas="80" linhas="3" titulo="Outro Documento"
				var="outroDoc" />
		</mod:grupo>

		<mod:grupo
			titulo="DETALHES DOS ITENS QUE ESTÃO SENDO DEVOLVIDOS">
			<mod:caixaverif
				titulo="Crachá Funcional e Carteira Funcional à SECAD;"
				var="crachaDev" reler="Não" />
			<mod:grupo></mod:grupo>
			<%--<c:if test="${numDependentes ==0}">
				<mod:caixaverif
					titulo="Carteira do Plano de Saúde do Titular à SEBEN;"
					var="carteiraSaudeDev" reler="Não" />
			</c:if>--%>
			<%--<c:if test="${numDependentes ==1}">
				<mod:caixaverif
					titulo="Carteiras do Plano de Saúde do Titular, e 1 dependente à SEBEN;"
					var="carteiraSaudeDev" reler="Não" />
			</c:if>
			<c:if test="${numDependentes >1}">--%>
				<mod:caixaverif
					titulo="Carteiras do Plano de Saúde do Titular e dependentes à SEBEN;"
					var="carteiraSaudeDev" reler="Não" />
			<%--</c:if>--%>
			<mod:grupo></mod:grupo>
			<mod:memo colunas="80" linhas="2" titulo="Outro Item" var="outroItem" />
		</mod:grupo>
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
			<span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=125,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${conteudo2}');"><u>
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
			${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a)
			${doc.subscritor.lotacao.descricao} vem requerer a Vossa Excelência
			que se digne encaminhar o requerimento de <b>exoneração,</b> com efeitos a partir do dia ${dataExoneracao}, em anexo, ao E. Tribunal Regional Federal
			da 2ª Região.</p>
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
			<c:import
				url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegi.jsp" />

			<p style="TEXT-INDENT: 1.5cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo},
			${doc.subscritor.padraoReferenciaInvertido}, matrícula
			${doc.subscritor.matricula} e lotado(a) no(a)
			${doc.subscritor.lotacao.descricao}, vem requerer a
			Vossa Excelência <b>exoneração</b> (art. 34, da Lei 8.112/90) do
			cargo que ora ocupa, a partir do dia <b>${dataExoneracao}</b> .</p>
			<p style="TEXT-INDENT: 1.5cm" align="justify">Está ciente das diferentes conseqüências entre exoneração a pedido e vacância por posse
			em outro cargo inacumulável.</p>

			<p style="TEXT-INDENT: 1.5cm" align="justify"><b><i>Em
			anexo, encontram-se o(s) seguinte(s) documento(s):</b></i></p>
			<ul>
				<c:if test="${declaraDevolvToken=='Sim'}">
					<li>Declaração de Devolução do Token;</li>
				</c:if>
				<c:if test="${declaraBensAnex=='Sim'}">
					<li>Declaração de bens atualizada até a data da exoneração,
					com os respectivos valores;</li>
				</c:if>
				<c:if test="${IrpfCopiaAnex=='Sim'}">
					<li>Cópias da declaração de Imposto de renda e recibo de
					entrega ou declaração de isenção;</li>
				</c:if>
				<c:if test="${cpfAnex=='Sim'}">
					<li>Cópia do CPF;</li>
				</c:if>
				<c:if test="${not empty outroDoc}">
					<li>${outroDoc}</li>
				</c:if>
			</ul>
			<p style="TEXT-INDENT: 1.5cm" align="justify"><b><i>Declara,
			ainda, ter devolvido às respectivas Seções:</b></i></p>
			<ul>
				<c:if test="${crachaDev=='Sim'}">
					<li>Crachá Funcional e Carteira Funcional à SECAD;</li>
				</c:if>
				<c:if test="${carteiraSaudeDev=='Sim'}">
					<li>Carteira(s) do Plano de Saúde do Titular e do(s)
					dependente(s) à SEBEN;</li>
				</c:if>
				<c:if test="${not empty outroItem}">
					<li>${outroItem}</li>
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
