<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
LICENÇA PARA TRATAR DE INTERESSSES PARTICULARES -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
			<mod:memo colunas="60" linhas="3" titulo="MOTIVO DA LICENÇA" var="motivo" />
			
	</mod:entrevista>

	<mod:documento>

		<mod:valor var="texto_requerimento">
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a)
		${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência que se digne encaminhar
		o requerimento de <b>LICENÇA PARA TRATAR DE INTERESSES PARTICULARES</b>, 
		em anexo, ao E. Tribunal Regional Federal da 2ª Região.
		</p>
		</mod:valor>
		
		<mod:valor var="texto_requerimento2">
		<c:import url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegiao.jsp" />		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a)
		${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência, nos termos do artigo 91 da Lei n.º 8.112/90, 
		com a redação dada pela Lei n.º 9.527/97, alterado pela Medida Provisória 
		n.º 2.225-45, em vigor por força do art. 2º da Emenda Constitucional 
		n.º 32/2001, regulamentado pela Resolução n.º 5/2008 do Conselho da 
		Justiça Federal, <b>LICENÇA PARA TRATAR DE INTERESSES PARTICULARES</b>
		, pelos motivos expostos a seguir:
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
		${motivo}
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
		Está ciente de que, nos termos do art. 79, b e c, da 
		Resolução n.º 5/2008 do Conselho da Justiça Federal, 
		<b>continuará na titularidade do cargo</b>, permanecendo sujeito às proibições 
		e aos deveres contidos na Lei nº 8.112/90, assim como terá <b>suspensa</b> 
		a contagem do período aquisitivo para fins de férias, retomando-se a 
		contagem <b>na data do retorno da licença</b>.
		</p>
		
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
		<br/>
		<br/>
		<p align="center">${doc.dtExtenso}</p>			
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		
		
		</mod:valor>
		
		
		
		
		
		
		<mod:valor var="texto_requerimento3">
		
		<c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" />
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		<i>
		Declaro estar ciente de que o § 2º do art. 183 da Lei n.º 8.112/90, 
		acrescentado pela Lei n.º 10.667/2003, prevê que o servidor afastado ou 
		licenciado do cargo efetivo, sem direito à remuneração, inclusive para 
		servir em organismo oficial internacional do qual o Brasil seja membro 
		efetivo ou com o qual coopere, ainda que contribua para regime de previdência 
		social no exterior, terá suspenso o seu vínculo com o regime do Plano de 
		Seguridade Social do Servidor Público enquanto durar o afastamento ou a 
		licença, não lhe assistindo, neste período, os benefícios do mencionado 
		regime de previdência.
		</i>
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
		<i>
		Declaro, ainda, estar ciente de que o § 3º do referido artigo, também 
		acrescentado pela Lei n.º 10.667/2003, assegura ao servidor, na situação 
		acima descrita, a manutenção da vinculação ao regime do Plano de Seguridade 
		Social do Servidor Público, mediante o recolhimento mensal da respectiva 
		contribuição, no mesmo percentual devido pelos servidores em atividade, 
		incidente sobre a remuneração total do cargo a que faz jus no exercício de 
		suas atribuições, computando-se, para esse efeito, inclusive, as vantagens 
		pessoais.
		</i>
		</p>
		<p align="center">${doc.dtExtenso}</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>
	</mod:documento>
</mod:modelo>
