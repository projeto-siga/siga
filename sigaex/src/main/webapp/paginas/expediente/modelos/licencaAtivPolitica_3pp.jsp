<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
		<mod:grupo titulo="QUANTO AO PERÍODO DE LICENÇA">
			<mod:data titulo="Data de Incio" var="dataInicio" />
			<mod:data titulo="Data de Fim" var="dataFim" />
			<mod:grupo>
				<mod:texto var="cargoEletivo" titulo="Cargo Eletivo" />
			</mod:grupo>
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>

		<mod:valor var="texto_requerimento">
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a)
		${doc.subscritor.lotacao.descricao}, vem requerer a Vossa
		Excelência, que se digne encaminhar o requerimento de <B>LICENÇA PARA
		ATIVIDADE POLÍTICA</B>, no período compreendido entre ${dataInicio} e
		${dataFim}, em anexo, ao E. Tribunal Regional Federal da 2ª Região.
		</p>
		</mod:valor>	
		<mod:valor var="texto_requerimento2">
		<c:set var="semEspacos" value="Sim" scope="request" />
		<c:import url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegiao.jsp" />			
			<p style="TEXT-INDENT: 2cm; font-size: 12px" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, 
			vem requerer a Vossa Excelência, nos termos da LC n.º 64/90 c/c o 
			art. 86 e §§ da Lei n.º 8.112/90, com a redação dada pela 
			Lei n.º 9.527/97, <b>LICENÇA PARA ATIVIDADE POLÍTICA</b> no período 
			compreendido entre ${dataInicio} e ${dataFim}, tendo em vista a candidatura ao cargo eletivo de ${cargoEletivo}, 
			conforme cópia autenticada da ata da convenção partidária e/ou protocolo do pedido de registro na Justiça Eleitoral.
			</p>
			<p style="TEXT-INDENT: 2cm; font-size: 12px" align="justify">
			Declara estar ciente das situações abaixo relacionadas:</p>
			<ul style="font-size: 12px">
			<li>
				que a licença será sem remuneração, a partir da data em que for escolhido em convenção partidária, até o dia imediatamente anterior ao do registro da candidatura perante a Justiça Eleitoral, e com a remuneração do cargo efetivo a partir do protocolo do pedido de registro da candidatura na Justiça Eleitoral até o décimo dia seguinte ao da eleição, por até 3 (três) meses. 
			</li>
			<li>
				que deverá apresentar o comprovante do registro, no prazo máximo de quinze dias, a contar de sua homologação na Justiça Eleitoral. 
			</li>
			<li>
				que a licença poderá ser interrompida, a qualquer tempo, a seu pedido.
			</li>
			<li>
				que em caso de desistência à candidatura, reassumirá imediatamente as atividades do cargo.
			</li>
			<li>
				que em caso de cancelamento ou indeferimento do registro, mediante decisão transitada em julgado, reassumirá imediatamente as atividades do cargo, devolvendo as quantias recebidas desde o início do afastamento. 
			</li>
			<li>
				que uma vez concedida a licença sem remuneração, a concessão da licença com remuneração será considerada como prorrogação da primeira, não havendo necessidade de retorno ao serviço.
			</li>
			<li>
				que o período de licença, com remuneração, contar-se-á apenas para os efeitos de aposentadoria e disponibilidade. 
			</li>
			<li>
				que o período concedido sem remuneração, contará apenas para aposentadoria, caso o servidor opte pela manutenção da vinculação ao Plano de Seguridade Social do Servidor Público, mediante recolhimento mensal da respectiva contribuição, no mesmo percentual devido pelos servidores em atividade, que terá como base de cálculo a remuneração contributiva do cargo efetivo a que faria jus se em exercício estivesse, computando-se para esse efeito, inclusive, as vantagens pessoais, nos termos do art. 183 da Lei nº 8.112 de 1990, com as alterações da Lei nº 10.667, de 14 de maio de 2003. 
			</li>
			<li>
				que o período de afastamento, com ou sem remuneração, suspende o estágio probatório e o prazo para aquisição de estabilidade, bem como não lhe dá direito a receber o auxílio-alimentação.		
			</li>
			</ul>
			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
			<p align="center">${doc.dtExtenso}</p>			
			<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		
			</mod:valor>
					
			<%--<mod:valor var="texto_requerimento3">
			<c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" /> 
			
			<p style="TEXT-INDENT: 2cm" align="justify">
			<i>
			Declaro estar ciente de que o § 2º do art. 183 da Lei n.º 8.112/90, acrescentado
			pela Lei n.º 10.667/2003, prevê que o servidor afastado ou licenciado do cargo 
			efetivo, sem direito à remuneração, inclusive para servir em organismo oficial 
			internacional do qual o Brasil seja membro efetivo ou com o qual coopere, ainda 
			que contribua para regime de previdência social no exterior, terá suspenso o seu 
			vínculo com o regime do Plano de Seguridade Social do Servidor Público enquanto 
			durar o afastamento ou a licença, não lhes assistindo, neste período, 
			os benefícios do mencionado regime de previdência.
			</i></p>
			<p style="TEXT-INDENT: 2cm" align="justify">
			<i> 
			Declaro, ainda, estar ciente de que o § 3º do referido artigo, também acrescentado 
			pela Lei n.º 10.667/2003, assegura ao servidor na situação acima descrita, a 
			manutenção da vinculação ao regime do Plano de Seguridade Social do Servidor 
			Público, mediante o recolhimento mensal da respectiva contribuição, no mesmo 
			percentual devido pelos servidores em atividade, incidente sobre a remuneração 
			total do cargo a que faz jus no exercício de suas atribuições, computando-se, 
			para esse efeito, inclusive, as vantagens pessoais.
			</i></p>
			<p align="center">${doc.dtExtenso}</p>
			<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</mod:valor>--%>
		
	</mod:documento>
</mod:modelo>
