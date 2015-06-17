<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
LICENÇA PARA AFASTAMENTO DO CONJUGUE
< ESTE DOCUMENTO SE DIVIDE EM 3 PAGINAS >
[OBS AO PROGRAMADOR: CRIAR DIGITO OU METODO P/ SALTAR PAGINA]  -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">

	<mod:documento>
		
		<mod:valor var="texto_requerimento">
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência que se digne encaminhar 
		o requerimento de <b>LICENÇA POR MOTIVO DE AFASTAMENTO DO CÔNJUGE COM EXERCÍCIO 
		PROVISÓRIO</b>, em anexo, ao E. Tribunal Regional Federal da 2ª Região.
		</p>
		</mod:valor>
		<mod:valor var="texto_requerimento2">
		
		<c:import url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegiao.jsp" />

		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência, nos termos do art. 84 da Lei nº 8.112/90, c/c alteração dada pela Lei nº 9.527/97, regulamentado pela Resolução nº 498/2006, do Conselho da Justiça Federal, <B>LICENÇA POR MOTIVO DE AFASTAMENTO DO CÔNJUGE COM EXERCÍCIO 
		PROVISÓRIO</B>.
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
		Declara estar ciente dos termos da norma citada, especialmente no que diz respeito ao seguinte:
		<ul>
		<li>que a concessão será por prazo indeterminado, enquanto perdurar o vínculo matrimonial ou a união estável, comprometendo-se a encaminhar ao órgão de origem, anualmente, declaração que ateste o deslocamento e manutenção da situação mencionada (art. 2º, parágrafo 1º c/c art. 5º, parágrafo 3º);</li>
		<li>que o período de licença sem remuneração não será contado para nenhum efeito, exceto para aposentadoria na hipótese do art. 6º, suspendendo o estágio probatório, a aquisição da estabilidade e a concessão de progressão ou promoção funcional (art. 3º e parágrafo único);</li>
		<li>que o período de exercício provisório será contado para todos os efeitos legais (art. 4º, parágrafo único);</li>
		<li>que, na hipótese de não ser concedido o exercício provisório, será assegurada ao servidor licenciado, mediante requerimento, a manutenção da vinculação ao regime do Plano de Seguridade Social do Servidor Público mediante recolhimento mensal da respectiva contribuição, no mesmo percentual devido pelos servidores em atividade, incidente sobre a remuneração total do cargo a que faz jus no exercício de suas atribuições, computando-se, para esse efeito, inclusive, as vantagens pessoais (art. 6º).</li>		
		</ul>
		</p>
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>					
		
				
		<%--<mod:valor var="texto_requerimento3">
		<c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" />
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		<i>
		Declaro estar ciente de que o § 2º do art. 183 da Lei n.º 8.112/90, 
		acrescentado pela Lei n.º 10.667/2003, prevê que o servidor afastado ou 
		licenciado do cargo efetivo, sem direito à remuneração, inclusive para 
		servir em organismo oficial internacional do qual o Brasil seja membro 
		efetivo ou com o qual coopere, ainda que contribua para regime de 
		previdência social no exterior, terá suspenso o seu vínculo com o 
		regime do Plano de Seguridade Social do Servidor Público enquanto 
		durar o afastamento ou a licença, não lhes assistindo, neste período, 
		os benefícios do mencionado regime de previdência.
		</i>
		</p> 
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		<i>
		Declaro, ainda, estar ciente de que o § 3º do referido artigo, 
		também acrescentado pela Lei n.º 10.667/2003, assegura ao servidor na 
		situação acima descrita, a manutenção da vinculação ao regime do Plano 
		de Seguridade Social do Servidor Público, mediante o recolhimento mensal 
		da respectiva contribuição, no mesmo percentual devido pelos servidores em 
		atividade, incidente sobre a remuneração total do cargo a que faz jus no 
		exercício de suas atribuições, computando-se, para esse efeito, inclusive, 
		as vantagens pessoais.
		</i>
		</p>	
					
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>--%>
	</body>
	</html>
</mod:documento>
</mod:modelo>
