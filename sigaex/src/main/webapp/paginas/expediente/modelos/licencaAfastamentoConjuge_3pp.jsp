<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
LICENÇA PARA AFASTAMENTO DO CONJUGUE
< ESTE DOCUMENTO SE DIVIDE EM 3 PAGINAS >
[OBS AO PROGRAMADOR: CRIAR DIGITO OU METODO P/ SALTAR PAGINA]  -->

<mod:modelo>
	<mod:entrevista>
	
		<mod:grupo titulo="DETALHES DO SERVIDOR">
				<mod:texto titulo="Classe" var="classe"/>
				<mod:texto titulo="Padrão" var="padrao" />
		</mod:grupo>
				 
	</mod:entrevista>
		
	<mod:documento>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head></head>
	<body>
	
		<c:import url="/paginas/expediente/modelos/inc_tit_juizfedDirForo.jsp" />
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		classe ${classe} e padrão ${padrao}, 
		lotado(a) no(a)${doc.subscritor.lotacao.descricao},
	
		
		vem, respeitosamente, requerer a Vossa Excelência, que se digne encaminhar 
		o requerimento de <b>LICENÇA POR MOTIVO DE AFASTAMENTO DO CÔNJUGE COM EXERCÍCIO 
		PROVISÓRIO</b>, em anexo, ao E. Tribunal Regional Federal da 2ª Região
		</p>

		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
				
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		
		
		
		
		
		
		

		<c:import url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegiao.jsp" />

		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		classe ${requestScope.classe} e padrão ${requestScope.padrao}, 
		lotado(a) no(a)${doc.subscritor.lotacao.descricao},
	
		vem, respeitosamente, requerer a Vossa Excelência, que se digne encaminhar 
		o requerimento de <B>LICENÇA POR MOTIVO DE AFASTAMENTO DO CÔNJUGE COM EXERCÍCIO 
		PROVISÓRIO</B>, em anexo, ao E. Tribunal Regional Federal da 2ª Região
		</p>
		
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
					
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
			
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		
		
		
		
		
		
		
		
		
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
					
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		
	</body>
	</html>
</mod:documento>
</mod:modelo>
