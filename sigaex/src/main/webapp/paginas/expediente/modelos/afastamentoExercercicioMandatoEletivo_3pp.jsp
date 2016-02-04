<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- este modelo trata de 
AFASTAMENTO PARA EXERCÍCIO DE MANDATO ELETIVO -->


<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
<mod:documento>
		
		<mod:valor var="texto_requerimento">
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}
		
		vem requerer a Vossa Excelência que se digne encaminhar
		o requerimento de <b>AFASTAMENTO PARA EXERCÍCIO DE MANDATO ELETIVO</b>, 
		em anexo, ao E. Tribunal Regional Federal da 2ª Região.
		</p>
		
		</mod:valor>
		<mod:valor var="texto_requerimento2">
			<c:import url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegiao.jsp" />
	
			<p style="TEXT-INDENT: 2cm" align="justify">
			<br/>${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
			${doc.subscritor.padraoReferenciaInvertido},		 
			lotado(a) no(a) ${doc.subscritor.lotacao.descricao},
	
			vem requerer a Vossa Excelência, nos termos do art. 94 da Lei 
			n.º 8.112/90, <B>AFASTAMENTO PARA EXERCÍCIO DE MANDATO ELETIVO</B>.
			</p>
			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
			<br/>
			<br/>
			<p align="center">${f:replace(doc.dtExtenso,'.','')}</p>
			<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			<c:import
				url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
			<%--<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />--%>
				
		</mod:valor>	
		

		<mod:valor var="texto_requerimento3">
			<c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" />
			<p style="TEXT-INDENT: 2cm" align="justify">
			
			Declaro estar ciente de que o § 2º do art. 183 da Lei n.º 8.112/90, 
			acrescentado pela Lei n.º 10.667/2003, prevê que <i>o servidor afastado ou 
			licenciado do cargo efetivo, sem direito à remuneração, inclusive para 
			servir em organismo oficial internacional do qual o Brasil seja membro 
			efetivo ou com o qual coopere, ainda que contribua para regime de previdência 
			social no exterior, terá suspenso o seu vínculo com o regime do Plano de 
			Seguridade Social do Servidor Público enquanto durar o afastamento ou a 
			licença, não lhe assistindo, neste período, os benefícios do mencionado 
			regime de previdência.
			</i></p>
	
	
			<p style="TEXT-INDENT: 2cm" align="justify">	
			Declaro, ainda, estar ciente de que o § 3º do referido artigo, 
			também acrescentado pela Lei n.º 10.667/2003, <i>assegura ao servidor, na situação 
			acima descrita, a manutenção da vinculação ao regime do Plano de Seguridade 
			Social do Servidor Público, mediante o recolhimento mensal da respectiva 
			contribuição, no mesmo percentual devido pelos servidores em atividade, 
			incidente sobre a remuneração total do cargo a que faz jus no exercício de 
			suas atribuições, computando-se, para esse efeito, inclusive, as vantagens pessoais.
			</i></p>
			<br/>
			<br/>
			<p align="center">${doc.dtExtenso}</p>
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>
	</mod:documento>
</mod:modelo>
