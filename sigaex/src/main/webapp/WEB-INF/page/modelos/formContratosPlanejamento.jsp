<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<mod:modelo>
	<mod:entrevista>
		<div id="desDiv"
			style="visibility: hidden; position: absolute; left: 0px; top: 0px; width: 50%; height: 50%; text-align: left; z-index: 1000;">
		<div
			style="width: 600px; margin: 100px auto; background-color: #fff; border: 1px solid #000; padding: 15px; text-align: center;">
		<p align=justify>Justificar a necessidade da contratação:
		objetivos, problema a ser resolvido, benefícios da contratação,
		quantitativo estimado e parâmetros técnicos utilizados para o total
		pretendido.</p>
		</div>
		</div>
		<div id="viaDiv"
			style="visibility: hidden; position: absolute; left: 0px; top: 0px; width: 50%; height: 50%; text-align: left; z-index: 1000;">
		<div
			style="width: 600px; margin: 100px auto; background-color: #fff; border: 1px solid #000; padding: 15px; text-align: center;">
		<p align=justify>Verificar, com outras áreas pertinentes, se há
		viabilidade técnica para a aquisição do objeto. Ex: infra-estrutura,
		elétrica, peso, compatibilidade etc. Obs: caso a outra área não só
		afirme a viabilidade técnica, mas também esteja diretamente envolvida
		na contratação, preencha o item 5.</p>
		</div>
		</div>
		<div id="jusDiv"
			style="visibility: hidden; position: absolute; left: 0px; top: 0px; width: 50%; height: 50%; text-align: left; z-index: 1000;">
		<div
			style="width: 600px; margin: 100px auto; background-color: #fff; border: 1px solid #000; padding: 15px; text-align: center;">
		<p align=justify>Serviço contínuo é aquele essencial à
		Administração Pública, habitual em razão da sua própria destinação,
		não podendo sofrer interrupção de continuidade, sob pena de causar
		graves prejuízos ao interesse público.</p>
		</div>
		</div>
		<div id="lotDiv"
			style="visibility: hidden; position: absolute; left: 0px; top: 0px; width: 50%; height: 50%; text-align: left; z-index: 1000;">
		<div
			style="width: 600px; margin: 100px auto; background-color: #fff; border: 1px solid #000; padding: 15px; text-align: center;">
		<p align=justify>Lote é qualquer remessa composta por várias
		unidades de um mesmo item. A regra geral estabelecida pela lei de
		licitações é que o objeto a ser licitado deve ser divisível em tantas
		partes quanto possível, de forma a ampliar a participação do maior
		número de licitantes. Porém em casos justificados, poderá ser feita
		licitação com itens agregados em lotes. Neste momento, deve ser
		verificada e justificada a formação de lotes por razões de ordens
		técnica. Após a cotação, pode ser necessária a análise de formação de
		lotes para que a contratação de lotes para que a contrataçãotorne-se
		que a contratação torna-se econocmicamente viável.</p>
		</div>
		</div>
		<div id="conDiv"
			style="visibility: hidden; position: absolute; left: 0px; top: 0px; width: 50%; height: 50%; text-align: left; z-index: 1000;">
		<div
			style="width: 600px; margin: 100px auto; background-color: #fff; border: 1px solid #000; padding: 15px; text-align: center;">
		<p align=justify>Verificar, junto aos órgãos contratantes, se
		houve alguma restrição na licitação (impugnação e recursos) e o
		sucesso da execução do contrato. Anexar, se possível, os documentos da
		contratação.</p>
		</div>
		</div>
		<div id="relDiv"
			style="visibility: hidden; position: absolute; left: 0px; top: 0px; width: 50%; height: 50%; text-align: left; z-index: 1000;">
		<div
			style="width: 600px; margin: 100px auto; background-color: #fff; border: 1px solid #000; padding: 15px; text-align: center;">
		<p align=justify>Deve ser revisto o processo anterior de
		contratação de mesmo objeto, analisando as dificuldades enfrentadas,
		discutindo-as, se for o caso, com o gerente do contrato anterior e
		outros setores envolvidos, para sanear equívocos no tempo referência.</p>
		</div>
		</div>

		<mod:grupo
			titulo="1. Identificação da(s) unidade(s)/Servidor(es) responsável(is):">
			<mod:grupo>
				<mod:lotacao titulo="Subsecretaria/Núcleo" var="subNucleo"
					obrigatorio="Sim" />
			</mod:grupo>				
			<mod:grupo>
				<mod:selecao var="numFiscais"
					titulo="Número de Fiscais Técnicos/Suplentes" reler="ajax"
					idAjax="numFiscaisAjax" opcoes="1;2;3;4;5;6;7;8;9;10" />
			</mod:grupo>
			<mod:grupo depende="numFiscaisAjax">					
				<c:forEach var="i" begin="1" end="${numFiscais}">
					<mod:grupo>						
						${i})&nbsp;&nbsp;<mod:pessoa titulo="Fiscal Técnico" var="fisTecnico${i}" obrigatorio="Sim" />						
					</mod:grupo>
					<mod:grupo>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<mod:pessoa titulo="Suplente" var="suplente${i}" obrigatorio="Sim" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
			<mod:grupo>					
				<mod:memo titulo="Comissão de Recebimento (caso haja)"
					var="comRecebimento" colunas="80" linhas="2" />
			</mod:grupo>
		</mod:grupo>
		

		<hr>

		<mod:grupo>
			<mod:grupo titulo="2. Justificativa:">
				<table>
					<tr>
						<td><span
							onmouseover="javascript: var desDiv = document.getElementById('desDiv'); 
							desDiv.style.visibility = 'visible'; desDiv.style.left = event.clientX + document.body.scrollLeft; 
							desDiv.style.top = event.clientY + document.body.scrollTop - 100"
							onmouseout="document.getElementById('desDiv').style.visibility = 'hidden'">
						<img src="<c:url value='/imagens/interrogation.jpg'/>" width=20
							height=20 /></span></td>
					</tr>
				</table>

				<mod:memo titulo="Descrição da necessidade/quantitativo"
					var="desNecQuantitativo" colunas="80" linhas="2" obrigatorio="Sim" />

				<table>
					<tr>
						<td><span
							onmouseover="javascript: var viaDiv = document.getElementById('viaDiv'); 
				viaDiv.style.visibility = 'visible'; viaDiv.style.left = event.clientX + document.body.scrollLeft; 
				viaDiv.style.top = event.clientY + document.body.scrollTop - 100"
							onmouseout="document.getElementById('viaDiv').style.visibility = 'hidden'">
						<img src="<c:url value='/imagens/interrogation.jpg'/>" width=20
							height=20 /></span></td>
					</tr>
				</table>

				<mod:memo titulo="Viabilidade técnica" var="viaTecnica" colunas="80"
					linhas="2" obrigatorio="Sim" />
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:grupo titulo="3. Identificação da contratação:">
				<mod:grupo>
					<mod:grupo largura="10">
						<mod:radio titulo="Material" var="identificacao" valor="1"
							reler="ajax" idAjax="identAjax" marcado="Sim" />
					</mod:grupo>
					<mod:grupo largura="90">
						<mod:radio titulo="Serviço" var="identificacao" valor="2"
							reler="ajax" idAjax="identAjax" />
					</mod:grupo>
				</mod:grupo>
				<c:set var="valIdentContrat" value="${identificacao}" />
				<c:if test="${empty valIdentContrat}">
					<c:set var="valIdentContrat" value="${param['identificacao']}" />
				</c:if>
				<c:if test="${empty valIdentContrat}">
					<c:set var="valIdentContrat" value="1" />
				</c:if>
				<mod:grupo depende="identAjax">
					<c:if test="${valIdentContrat == 1}">
						<mod:grupo titulo="Há necessidade técnica de indicação de marca?">
							<mod:grupo>
								<mod:radio titulo="Não." var="indicMarca" valor="1"
									marcado="Sim" reler="ajax" idAjax="indicMarcaAjax" />
							</mod:grupo>
							<mod:grupo largura="7">
								<mod:radio titulo="Sim." var="indicMarca" valor="2" reler="ajax"
									idAjax="indicMarcaAjax" />
							</mod:grupo>
							<c:set var="valIndicMarca" value="${indicMarca}" />
							<c:if test="${empty valIndicMarca}">
								<c:set var="valIndicMarca" value="${param['indicMarca']}" />
							</c:if>
							<mod:grupo largura="93">
								<mod:grupo depende="indicMarcaAjax">
									<c:if test="${valIndicMarca == 2}">
										<mod:texto titulo="Justifique" var="jusIndicMarca"
											largura="130" maxcaracteres="190" obrigatorio="Sim" />
									</c:if>
								</mod:grupo>
							</mod:grupo>
						</mod:grupo>
					</c:if>
					<c:if test="${valIdentContrat == 2}">
						<mod:grupo titulo="Haverá alocação de mão de obra?">
							<mod:grupo>
								<mod:radio titulo="Não." var="servico" valor="1" marcado="Sim" />
							</mod:grupo>
							<mod:grupo>
								<mod:radio
									titulo="Sim. Houve estudo prévio sobre a possibilidade de execução do serviço sem
									a presença permanente (jornada fixa) <br/>da mão-de-obra nas dependências da SJRJ."
									var="servico" valor="2" />
							</mod:grupo>
						</mod:grupo>
					</c:if>
				</mod:grupo>

				<mod:grupo titulo="Tipo de Entrega:">
					<mod:grupo>
						<mod:radio titulo="Prestação única." var="tipEntrega" valor="1"
							reler="ajax" marcado="Sim" idAjax="tipEntregaAjax" />
					</mod:grupo>
					<mod:grupo>
						<mod:radio titulo="Prestação parcelada." var="tipEntrega"
							valor="2" reler="ajax" idAjax="tipEntregaAjax" />
					</mod:grupo>
					<mod:grupo>
						<mod:radio titulo="Prestação continuada. " var="tipEntrega"
							valor="3" reler="ajax" idAjax="tipEntregaAjax" />
					</mod:grupo>
					<c:set var="valTipEntrega" value="${tipEntrega}" />
					<c:if test="${empty valTipEntrega}">
						<c:set var="valTipEntrega" value="${param['tipEntrega']}" />
					</c:if>
					<mod:grupo depende="tipEntregaAjax">
						<c:if test="${valTipEntrega == 3}">
							<mod:grupo>
								<table>
									<tr>
										<td><span
											onmouseover="javascript: var jusDiv = document.getElementById('jusDiv'); 
											jusDiv.style.visibility = 'visible'; jusDiv.style.left = event.clientX + document.body.scrollLeft; 
											jusDiv.style.top = event.clientY + document.body.scrollTop - 100"
											onmouseout="document.getElementById('jusDiv').style.visibility = 'hidden'">
										<img src="<c:url value='/imagens/interrogation.jpg'/>"
											width=20 height=20 /></span></td>
										<td><mod:texto titulo="Justifique" var="jusTipEntrega"
											largura="138" maxcaracteres="190" obrigatorio="Sim" /></td>
									</tr>
								</table>
							</mod:grupo>
						</c:if>
					</mod:grupo>
				</mod:grupo>
				<mod:grupo>
					<b>A contratação será por Registro de Preço? (consultar <a
						href="http://intranet/documentos/grupo_97/rp_comentario_checklist_planej.pdf" target="_blank"><u>Prós
					e Contras do RP</u></a>):</b>
					<mod:grupo largura="100">
						<mod:radio titulo="Não." var="contratacao" valor="1" marcado="Sim"
							reler="ajax" idAjax="contratacaoAjax" />
					</mod:grupo>
					<mod:grupo largura="7">
						<mod:radio titulo="Sim." var="contratacao" valor="2" reler="ajax"
							idAjax="contratacaoAjax" />
					</mod:grupo>
					<c:set var="valContratacao" value="${contratacao}" />
					<c:if test="${empty valContratacao}">
						<c:set var="valContratacao" value="${param['contratacao']}" />
					</c:if>
					<mod:grupo largura="93">
						<mod:grupo depende="contratacaoAjax">
							<c:if test="${valContratacao == 2}">
								<mod:texto titulo="Justifique" var="jusContratacao" largura="130"
									maxcaracteres="190" obrigatorio="Sim" />
							</c:if>
						</mod:grupo>
					</mod:grupo>
				</mod:grupo>
				<mod:grupo>
					<table>
						<tr>
							<td><span
								onmouseover="javascript: var lotDiv = document.getElementById('lotDiv'); 
						lotDiv.style.visibility = 'visible'; lotDiv.style.left = event.clientX + document.body.scrollLeft; 
						lotDiv.style.top = event.clientY + document.body.scrollTop - 100"
								onmouseout="document.getElementById('lotDiv').style.visibility = 'hidden'">
							<img src="<c:url value='/imagens/interrogation.jpg'/>" width=20
								height=20 /></span></td>
							<td><b>Há necessidade de formação de lote por motivos
							técnicos?</b></td>
						</tr>
					</table>

					<mod:grupo largura="7">
						<mod:radio titulo="Sim." var="necFormacao" valor="1" reler="ajax"
							idAjax="idFormacaoAjax" />
					</mod:grupo>
					<c:set var="valNecFormacao" value="${necFormacao}" />
					<c:if test="${empty valNecFormacao}">
						<c:set var="valNecFormacao" value="${param['necFormacao']}" />
					</c:if>
					<mod:grupo largura="93">
						<mod:grupo depende="idFormacaoAjax">
							<c:if test="${valNecFormacao == 1}">
								<mod:texto titulo="Justifique" var="jusNecFormacao" largura="130"
									maxcaracteres="500" obrigatorio="Sim" />
							</c:if>
						</mod:grupo>
					</mod:grupo>
					<mod:grupo>
						<mod:radio
							titulo="Não. A formação de lotes em razão de valores deve ser avaliada após a cotação."
							var="necFormacao" valor="2" marcado="Sim" reler="ajax"
							idAjax="idFormacaoAjax" />
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo titulo="4. Vinculação estratégica:">
			<mod:grupo>
				<b>A contratação está vinculada a algum <a
					href="http://intranet/documentos/grupo_113/Acompanhamento_de_Projetos_e_Acoes_%20Bienio%202011_2012.mht" target="_blank"> <u>Projeto
				Estratégico Anual da SJRJ</u></a>?</b>
				<mod:grupo>
					<mod:grupo>
						<mod:radio titulo="Não." var="vinculacao" valor="1" marcado="Sim"
							reler="ajax" idAjax="idVinculacaoAjax" />
					</mod:grupo>
					<mod:grupo largura="7">
						<mod:radio titulo="Sim." var="vinculacao" valor="2" reler="ajax"
							idAjax="idVinculacaoAjax" />
					</mod:grupo>
					<c:set var="valVinculacao" value="${vinculacao}" />
					<c:if test="${empty valVinculacao}">
						<c:set var="valVinculacao" value="${param['vinculacao']}" />
					</c:if>
					<mod:grupo largura="93">
						<mod:grupo depende="idVinculacaoAjax">
							<c:if test="${valVinculacao == 2}">
								<mod:texto titulo="Identifique-o" var="identVincProj"
									largura="80" maxcaracteres="80" obrigatorio="Sim" />
							</c:if>
						</mod:grupo>
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>
			<mod:grupo>
				<b>A partir do <a
					href="http://intranet/documentos/grupo_97/mapa_estrategico.pdf" target="_blank">
				<u>Mapa Estratégico da Justiça Federal</u></a> elaborado pelo CJF,
				identifique <u>um ou mais</u> objetivos estratégicos aos quais a
				contratação está vinculada:</b>
			</mod:grupo>

			<mod:oculto var="obj1"
				valor="1. agilizar os trâmites judiciais e administrativos;" />
			<mod:oculto var="obj2"
				valor="2. otimizar a gestão dos custos operacionais;" />
			<mod:oculto var="obj3" valor="3. otimizar os processos de trabalho;" />
			<mod:oculto var="obj4"
				valor="4. facilitar o acesso à Justiça Federal da 2ª Região;" />
			<mod:oculto var="obj5"
				valor="5. promover a efetividade no cumprimento das decisões;" />
			<mod:oculto var="obj6" valor="6. promover a cidadania;" />
			<mod:oculto var="obj7"
				valor="7. incentivar e promover a responsabilidade ambiental;" />
			<mod:oculto var="obj8"
				valor="8. garantir o alinhamento estratégico e a integração da 
					Justiça Federal da 2ª Região;" />
			<mod:oculto var="obj9"
				valor="9. fortalecer as relações da Justiça Federal da 2ª Região
					com outros órgãos e instituições;" />
			<mod:oculto var="obj10"
				valor="10. fortalecer a imagem e aperfeiçoar a comunicação 
					da Justiça Federal da 2ª Região;" />
			<mod:oculto var="obj11"
				valor="11. desenvolver conhecimentos, habilidades e atitudes 
					dos magistrados e servidores com foco em resultados;" />
			<mod:oculto var="obj12"
				valor="12. fortalecer o clima organizacional e o bem estar dos magistrados 
					e servidores;" />
			<mod:oculto var="obj13"
				valor="13. garantir a infraestrutura suficiente à execução das 
					atividades administrativas e judiciais;" />
			<mod:oculto var="obj14"
				valor="14. garantir o acesso e funcionamento de sistemas 
					essenciais de tecnologia de informação;" />
			<mod:oculto var="obj15"
				valor="15. assegurar recursos orçamentários e priorizar sua execução 
					na estratégia." />

			<table cellpadding="4" width="100%" border="0">
				<tr>
					<td><mod:caixaverif titulo="${obj1}" var="objetivo1" /></td>
					<td><mod:caixaverif titulo="${obj8}" var="objetivo8" /></td>
				</tr>
				<tr>
					<td><mod:caixaverif titulo="${obj2}" var="objetivo2" /></td>
					<td><mod:caixaverif titulo="${obj9}" var="objetivo9" /></td>
				</tr>
				<tr>
					<td><mod:caixaverif titulo="${obj3}" var="objetivo3" /></td>
					<td><mod:caixaverif titulo="${obj10}" var="objetivo10" /></td>
				</tr>
				<tr>
					<td><mod:caixaverif titulo="${obj4}" var="objetivo4" /></td>
					<td><mod:caixaverif titulo="${obj11}" var="objetivo11" /></td>
				</tr>
				<tr>
					<td><mod:caixaverif titulo="${obj5}" var="objetivo5" /></td>
					<td><mod:caixaverif titulo="${obj12}" var="objetivo12" /></td>
				</tr>
				<tr>
					<td><mod:caixaverif titulo="${obj6}" var="objetivo6" /></td>

					<td><mod:caixaverif titulo="${obj13}" var="objetivo13" /></td>
				</tr>
				<tr>
					<td><mod:caixaverif titulo="${obj7}" var="objetivo7" /></td>
					<td><mod:caixaverif titulo="${obj14}" var="objetivo14" /></td>
				</tr>
				<tr>
					<td></td>
					<td><mod:caixaverif titulo="${obj15}" var="objetivo15" /></td>
				</tr>
			</table>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:grupo
				titulo="5. Identificação e observações de unidades envolvidas na contratação:">
				<mod:grupo>
					<mod:selecao var="numSubsecretarias"
						titulo="Número de Subsecretaria(s)" reler="ajax"
						idAjax="numSubsecretariasAjax" opcoes="0;1;2;3;4;5" />
				</mod:grupo>

				<mod:grupo depende="numSubsecretariasAjax">
					<c:if test="${numSubsecretarias != 0}">
						<mod:grupo titulo="Subsecretaria:" largura="26">
						</mod:grupo>
						<mod:grupo titulo="Atuação/Observações:" largura="74">
						</mod:grupo>					
						<c:forEach var="i" begin="1" end="${numSubsecretarias}">
							<mod:grupo titulo="" largura="25">
								<mod:texto titulo="" var="subsecretaria${i}" largura="30"
									maxcaracteres="30" />
							</mod:grupo>
							<mod:grupo titulo="" largura="75">
								<mod:texto titulo="" var="atuObservacao${i}" largura="110"
									maxcaracteres="140" />
							</mod:grupo>
						</c:forEach>
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo
			titulo="6. Há outras possibilidades de alcance do objetivo almejado?">
			<mod:grupo>
				<mod:radio titulo="Não." var="posObjetivo" valor="1" marcado="Sim"
					reler="ajax" idAjax="idposObjetivoAjax" />
			</mod:grupo>
			<mod:grupo>
				<mod:radio titulo="Sim." var="posObjetivo" valor="2" reler="ajax"
					idAjax="idposObjetivoAjax" />
			</mod:grupo>
			<c:set var="valposObjetivo" value="${posObjetivo}" />
			<c:if test="${empty valposObjetivo}">
				<c:set var="valposObjetivo" value="${param['posObjetivo']}" />
			</c:if>
			<mod:grupo depende="idposObjetivoAjax">
				<c:if test="${valposObjetivo == 2}">
					<mod:memo
						titulo="(Justificar, analisando a relação custo x benefício)"
						var="jusObjetivo" colunas="79" linhas="2" obrigatorio="Sim" />
				</c:if>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:grupo titulo="7. Custos estimados:">
				<mod:grupo largura="15">
					<mod:monetario var="valEstimado" formataNum="sim"
						titulo="Valor estimado" reler="sim" />
				</mod:grupo>
				<mod:grupo largura="85">
					<mod:texto
						titulo="Fonte de Recursos (convênios/orçamento vigente/crédito a ser 
					solicitado/outros)"
						var="fonRecursos" largura="82" maxcaracteres="82" />
				</mod:grupo>

				<br />

				<b>Integra a programação orçamentária do exercício?</b>
				<mod:grupo largura="100">
					<mod:radio titulo="Sim." var="intProOrcamentaria" valor="1"
						marcado="Sim" reler="ajax" idAjax="intProOrcamentariaAjax" />
				</mod:grupo>
				<mod:grupo largura="100">
					<mod:radio titulo="Sim. (com alteração do valor original)"
						var="intProOrcamentaria" valor="2" reler="ajax"
						idAjax="intProOrcamentariaAjax" />
				</mod:grupo>
				<mod:grupo largura="100">
					<mod:radio titulo="Não." var="intProOrcamentaria" valor="3"
						reler="ajax" idAjax="intProOrcamentariaAjax" />
				</mod:grupo>
				<c:set var="valIntProOrcamentaria" value="${intProOrcamentaria}" />
				<c:if test="${empty valIntProOrcamentaria}">
					<c:set var="valIntProOrcamentaria"
						value="${param['intProOrcamentaria']}" />
				</c:if>
				<mod:grupo depende="intProOrcamentariaAjax">
					<c:if test="${valIntProOrcamentaria == 3}">
						<b>Neste caso, há possibilidade de compensação com outro item
						da programação autorizada (cota orçamentária)?</b>
						<mod:grupo largura="7">
							<mod:radio titulo="Sim." var="intProOrc" valor="1" reler="ajax"
								idAjax="intProOrcAjax" />
						</mod:grupo>

						<c:set var="valIntProOrc" value="${intProOrc}" />
						<c:if test="${empty valIntProOrc}">
							<c:set var="valIntProOrc" value="${param['intProOrc']}" />
						</c:if>
						<mod:grupo largura="93">
							<mod:grupo depende="intProOrcAjax">
								<c:if test="${valIntProOrc == 1}">
									<mod:texto titulo="Qual" var="jusIntProOrc" largura="90"
										maxcaracteres="90" obrigatorio="Sim" />
								</c:if>
							</mod:grupo>
						</mod:grupo>
						<mod:grupo>
							<mod:radio titulo="Não." var="intProOrc" valor="2" marcado="Sim"
								reler="ajax" idAjax="intProOrcAjax" />
						</mod:grupo>
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:grupo
				titulo="8. Custos adicionais/decorrentes da contratação (caso a utilização/consumo 
				do objeto gere outra contratação):">
				<mod:grupo>
					<mod:selecao var="numCusAdicionais"
						titulo="Número de Custo(s) Adicional(is)" reler="ajax"
						idAjax="numCusAdicionaisAjax" opcoes="0;1;2;3;4;5" />
				</mod:grupo>
				<mod:grupo depende="numCusAdicionaisAjax">
					<c:if test="${numCusAdicionais != 0}">
						<mod:grupo titulo="Contratação/Aquisição:" largura="68">
						</mod:grupo>
						<mod:grupo titulo="Valor estimado:" largura="32">
						</mod:grupo>					
						<c:forEach var="i" begin="1" end="${numCusAdicionais}">
							<mod:grupo titulo="" largura="68">
								<mod:texto titulo="" var="conAquisicao${i}" largura="83"
									maxcaracteres="83" />
							</mod:grupo>
							<mod:grupo titulo="" largura="32">
								<mod:monetario var="valEstimado${i}" formataNum="sim" titulo=""
									reler="sim" />
							</mod:grupo>
						</c:forEach>
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:grupo titulo="">
				<table>
					<tr>
						<td><span
							onmouseover="javascript: var conDiv = document.getElementById('conDiv'); 
					conDiv.style.visibility = 'visible'; conDiv.style.left = event.clientX + document.body.scrollLeft; 
					conDiv.style.top = event.clientY + document.body.scrollTop - 100"
							onmouseout="document.getElementById('conDiv').style.visibility = 'hidden'">
						<img src="<c:url value='/imagens/interrogation.jpg'/>" width=20
							height=20 /></span></td>
						<td><b>9. Contratações semelhantes em outros órgãos
						(preferencialmente no Poder Judiciário Federal):</b></td>
					</tr>
				</table>
				<mod:grupo>
					<mod:selecao var="numContratacao"
						titulo="Número de Contratação(ões)" reler="ajax"
						idAjax="numContratacaoAjax" opcoes="0;1;2;3;4;5" />
				</mod:grupo>
				<mod:grupo depende="numContratacaoAjax">
					<c:if test="${numContratacao != 0}">
						<mod:grupo titulo="Órgão:" largura="55">
						</mod:grupo>
						<mod:grupo titulo="Valor contratado:" largura="20">
						</mod:grupo>
						<mod:grupo titulo="Exercício:" largura="25">
						</mod:grupo>					
						<c:forEach var="i" begin="1" end="${numContratacao}">
							<mod:grupo titulo="" largura="55">
								<mod:texto titulo="" var="orgao${i}" largura="67"
									maxcaracteres="67" />
							</mod:grupo>
							<mod:grupo titulo="" largura="18">
								<mod:monetario var="valContratado${i}" formataNum="sim" titulo=""
									reler="sim" />
							</mod:grupo>
							<mod:grupo titulo="" largura="27">
								<mod:texto titulo="" var="exercicio${i}" largura="12"
									maxcaracteres="12" />
							</mod:grupo>
						</c:forEach>	
						<mod:grupo>	
							<b>Observação(ões):</b>
							<mod:memo titulo="" var="observacao" colunas="79" linhas="2" />
						</mod:grupo>					
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:grupo
				titulo="10. Fatores que podem colocar em risco* a contratação:">
				<mod:grupo>
					<mod:selecao var="numFator" titulo="Número de Fator(es)"
						reler="ajax" idAjax="numFatorAjax" opcoes="0;1;2;3;4;5" />
				</mod:grupo>
				<mod:grupo depende="numFatorAjax">
					<c:if test="${numFator != 0}">
						<table border="0" cellspacing="0" width="100%">
							<tr>
								<td width="43%" align="left"><br>
								<b>Risco:</b></td>
								<td width="43%" align="left"><br>
								<b>Resposta (ação para evitar ou diminuir ocorrência do
								risco):</b></td>
								<td width="14%" align="left"><b>U.O. responsável<br />
								pela resposta:</b></td>
							</tr>
						</table>
						<table border="0" cellspacing="0" width="100%">
							<c:forEach var="i" begin="1" end="${numFator}">
								<tr>
									<td width="43%" align="left"><mod:memo titulo=""
										var="risco${i}" colunas="50" linhas="2" /></td>
									<td width="43%" align="left"><mod:memo titulo=""
										var="resposta${i}" colunas="50" linhas="2" /></td>
									<td width="14%" align="left"><mod:texto titulo=""
										var="responsavel${i}" largura="15" maxcaracteres="15" /></td>
								</tr>
							</c:forEach>
						</table>
											
						*Risco é um evento incerto que, se ocorrer, impacta em pelo menos um dos objetos do projeto.
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

		<hr>

		<mod:grupo>
			<table>
				<tr>
					<td><span
						onmouseover="javascript: var relDiv = document.getElementById('relDiv'); 
						relDiv.style.visibility = 'visible'; relDiv.style.left = event.clientX + document.body.scrollLeft; 
						relDiv.style.top = event.clientY + document.body.scrollTop - 100"
						onmouseout="document.getElementById('relDiv').style.visibility = 'hidden'">
						<img src="<c:url value='/imagens/interrogation.jpg'/>" width=20
						height=20 /></span></td>
					<td><b>11. Relação com contratações anteriores:</b></td>
				</tr>
			</table>
			<mod:grupo largura="60">
				<mod:texto titulo="Contratação anterior/Processo Administrativo"
					var="conAntProAdm" largura="74" maxcaracteres="74" />
			</mod:grupo>
			<mod:grupo largura="40">
				<mod:texto titulo="Vigência - Valor atual" var="vigValAtual"
					largura="30" maxcaracteres="30" />
			</mod:grupo>
			<mod:grupo>
				<mod:memo titulo="Dificuldades encontradas" var="difEncontrada"
					colunas="80" linhas="2" />
			</mod:grupo>
		</mod:grupo>

		<hr>


		<mod:grupo>
			<mod:memo titulo="<b>12. Legislação específica pertinente</b>"
				var="legEspPertinente" colunas="80" linhas="2" />
		</mod:grupo>

		<hr>

		<mod:grupo>
			<mod:memo titulo="<b>13. Informações complementares</b>"
				var="infComplementar" colunas="80" linhas="2" />
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<c:set var="tl" value="10pt" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>


		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
				</td>
			</tr>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->

		<br />

		<table width="100%" border="0" align="center">
			<tr>
				<td><b>CHECK LIST DE</b><br />
				<b>PLANEJAMENTO DA CONTRATAÇÃO</b><br />
				</td>
			</tr>
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">1. Identificação
				da(s) unidade(s)/Servidor(es)responsável(is):</p>
				</td>
			</tr>
		</table>

		<table cellpadding="4" width="100%" border="1">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;"><b>Subsecretaria/Núcleo:&nbsp;&nbsp;&nbsp;</b></p>
				<p style="font-family: Arial; font-size: 10pt;">${requestScope['subNucleo_lotacaoSel.sigla']}&nbsp;-&nbsp;${requestScope['subNucleo_lotacaoSel.descricao']}</p>
				</td>
			</tr>

			<c:forEach var="i" begin="1" end="${numFiscais}">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">${i})&nbsp;<b>Fiscal Técnico:&nbsp;&nbsp;&nbsp;</b></p>
				<p style="font-family: Arial; font-size: 8pt;">${requestScope[f:concat(f:concat('fisTecnico',i),'_pessoaSel.descricao')]}</p>	<br>			
				<p style="font-family: Arial; font-size: 10pt;"><b>&nbsp;&nbsp;&nbsp;&nbsp;Suplente:&nbsp;&nbsp;&nbsp;</b></p>			
				<p style="font-family: Arial; font-size: 8pt;">${requestScope[f:concat(f:concat('suplente',i),'_pessoaSel.descricao')]}</p>
				</td>

			</tr>
			</c:forEach>

			<c:if test="${not empty comRecebimento}">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Comissão
					de Recebimento (caso haja):&nbsp;&nbsp;&nbsp;</b></p>
					<p style="font-family: Arial; font-size: 8pt;">${comRecebimento}</p>
					</td>
				</tr>
			</c:if>
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">2.
				Justificativa:</p>
				</td>
			</tr>
		</table>

		<table cellpadding="4" width="100%" border="1">
			<ww:if test="${not empty desNecQuantitativo}">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Descrição
					da necessidade/quantitativo:</b></p>
					</td>
				</tr>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">${desNecQuantitativo}</p>
					</td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Descrição
					da necessidade/quantitativo:</b></p>
					</td>
				</tr>
				<tr>
					<td align="left" width="70%">
					<p style="font-family: Arial; font-size: 8pt;">Não há
					necessidade/quantitativo.</p>
					</td>
				</tr>
			</ww:else>
			<ww:if test="${not empty viaTecnica}">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Viabilidade
					técnica:&nbsp;&nbsp;&nbsp;</b></p>
					</td>
				</tr>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">${viaTecnica}</p>
					</td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Viabilidade
					técnica:&nbsp;&nbsp;&nbsp;</b></p>
					</td>
				</tr>
				<tr>
					<td align="left" width="70%">
					<p style="font-family: Arial; font-size: 8pt;">Não há
					viabilidade técnica.</p>
					</td>
				</tr>
			</ww:else>
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">3. Identificação
				da contratação:&nbsp;</p>
				<ww:if test="${identificacao == '1'}">
					<p style="font-family: Arial; font-size: 10pt;"><b>Material</b></p>
				</ww:if> <ww:else>
					<p style="font-family: Arial; font-size: 10pt;"><b>Serviço</b></p>
				</ww:else></td>
			</tr>
		</table>

		<c:if test="${identificacao == '1'}">
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Há
					necessidade técnica de indicação de marca?</b></p>
					</td>
				</tr>
				<ww:if test="${indicMarca == '1'}">
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Não.</p>
						</td>
					</tr>
				</ww:if>
				<ww:else>
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Sim.&nbsp;${jusIndicMarca}</p>
						</td>
					</tr>
				</ww:else>
			</table>
		</c:if>
		<c:if test="${identificacao == '2'}">
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 10pt;"><b>Haverá
					alocação de mão-de-obra?</b></p>
					</td>
				</tr>
				<ww:if test="${servico == '1'}">
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Não.</p>
						</td>
					</tr>
				</ww:if>
				<ww:else>
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Sim,<b>houve
						estudo prévio sobre a possibilidade de execução do serviço sem a
						presença permanente (jornada fixa) da mão-de-obra nas dependências
						da SJRJ.<b></p>
						</td>
					</tr>
				</ww:else>
			</table>
		</c:if>

		<table cellpadding="4" width="100%" border="1">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;"><b>Tipo de
				entrega:</b></p>
				</td>
			</tr>
			<c:choose>
				<c:when test="${tipEntrega == '1'}">
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Prestação
						única.</p>
						</td>
					</tr>
				</c:when>
				<c:when test="${tipEntrega == '2'}">
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Prestação
						parcelada.</p>
						</td>
					</tr>
				</c:when>

				<c:when test="${tipEntrega == '3'}">
					<tr>
						<td>
						<p style="font-family: Arial; font-size: 8pt;">Prestação
						continuada.&nbsp;${jusTipEntrega}</p>
						</td>
					</tr>
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</table>

		<table cellpadding="4" width="100%" border="1">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;"><b>A
				contratação será por Registro de Preço? (Consultar Prós e Contras do
				RP):</b></p>
				</td>
			</tr>
			<ww:if test="${contratacao == '1'}">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">Não.</p>
					</td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">
					Sim.&nbsp;${jusContratacao}</p>
					</td>
				</tr>
			</ww:else>
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;"><b>Há
				necessidade de formação de lote por motivos técnicos?</b></p>
				</td>
			</tr>
			<ww:if test="${necFormacao == '1'}">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">
					Sim.&nbsp;${jusNecFormacao}</p>
					</td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">Não. A formação
					de lotes em razão de valores deve ser avaliada após a cotação.</p>
					</td>
				</tr>
			</ww:else>
		</table>
		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">4. Vinculação
				estratégica:</p>
				</td>
			</tr>
		</table>

		<table cellpadding="4" width="100%" border="1">
			<tr>
				<td align="center">
				<p style="font-family: Arial; font-size: 10pt;"><b>Projeto
				Estratégico da SJRJ</b></p>
				</td>
				<td align="center">
				<p style="font-family: Arial; font-size: 10pt;"><b>Mapa
				Estratégico da Justiça Federal</b></p>
				</td>
			</tr>
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 9pt;"><b>A
				contratação está vinculada a algum Projeto Estratégico Anual da
				SJRJ?</b></p>
				<br>
				<ww:if test="${vinculacao == '1'}">
					<p style="font-family: Arial; font-size: 8pt;">Não.</p>
				</ww:if> <ww:else>
					<p style="font-family: Arial; font-size: 8pt;">Sim.&nbsp;${identVincProj}</p>
				</ww:else></td>
				<td>
				<p style="font-family: Arial; font-size: 9pt;"><b>Objetivo(s)
				estratégico(s) ao(s) qual(is) a contratação está vinculada, a partir
				do Mapa Estratégico da Justiça Federal elaborado pelo CJF:</b></p>
				<br>
				<c:forEach var="i" begin="1" end="15">
					<c:if test="${requestScope[f:concat('objetivo',i)] == 'Sim'}">
						<p style="font-family: Arial; font-size: 8pt;">${requestScope[f:concat('obj',i)]}</p>
						<br>
					</c:if>
				</c:forEach></td>
			</tr>
		</table>




		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td colspan="3">
				<p style="font-family: Arial; font-size: 10pt;">5. Identificação
				e observações de unidades envolvidas na contratação:</p>
				</td>
			</tr>
		</table>

		<ww:if test="${numSubsecretarias != 0}">
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="center" width="70%">
					<p style="font-family: Arial; font-size: 10pt;"><b>Subsecretaria</b></p>
					</td>
					<td align="center" width="235%">
					<p style="font-family: Arial; font-size: 10pt;"><b>Atuação/Observações</b></p>
					</td>
				</tr>
			</table>

			<table cellpadding="4" width="100%" border="1">
				<c:forEach var="i" begin="1" end="${numSubsecretarias}">
					<tr>
						<td align="center" width="70%">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('subsecretaria',i)]}</p>
						</td>
						<td align="left" width="235%">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('atuObservacao',i)]}</p>
						</td>
					</tr>
				</c:forEach>
			</table>
		</ww:if>
		<ww:else>
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="left" width="70%">
					<p style="font-family: Arial; font-size: 8pt;">Não há
					Subsecretarias e observações.</p>
					</td>
				</tr>
			</table>
		</ww:else>

		<br>

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td colspan="3">
				<p style="font-family: Arial; font-size: 10pt;">6. Há outras
				possibilidades de alcance do objetivo almejado?</p>
				</td>
			</tr>
		</table>

		<table cellpadding="4" width="100%" border="1">
			<ww:if test="${posObjetivo == '1'}">
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">Não.</p>
					</td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td>
					<p style="font-family: Arial; font-size: 8pt;">
					Sim.&nbsp;${jusObjetivo}</p>
					</td>
				</tr>
			</ww:else>
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">7. Custos
				estimados:</p>
				</td>
			</tr>
		</table>

		<table cellpadding="4" width="100%" border="1">
			<tr>
				<td align="center" width="70">
				<p style="font-family: Arial; font-size: 10pt;"><b>Valor
				estimado</b></p>
				</td>
				<td width="235">
				<p style="font-family: Arial; font-size: 10pt;"><b>Fonte de
				Recursos<br />
				(convênios/orçamento vigente/crédito a ser solicitado/outros)</b></p>
				</td>
			</tr>
			<tr>
				<td align="center" width="70">
				<p style="font-family: Arial; font-size: 8pt;">${valEstimado}</p>
				</td>
				<td width="235">
				<p style="font-family: Arial; font-size: 8pt;">${fonRecursos}</p>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				<p style="font-family: Arial; font-size: 10pt;"><b>Integra a
				programação orçamentária do exercício?</b></p>
				</td>
			</tr>
			<c:choose>
				<c:when test="${intProOrcamentaria == '1'}">
					<tr>
						<td colspan="2">
						<p style="font-family: Arial; font-size: 8pt;">Sim.</p>
						</td>
					</tr>
				</c:when>
				<c:when test="${intProOrcamentaria == '2'}">
					<tr>
						<td colspan="2">
						<p style="font-family: Arial; font-size: 8pt;">Sim. (com
						alteração do valor original).</p>
						</td>
					</tr>
				</c:when>
				<c:when test="${intProOrcamentaria == '3'}">
					<tr>
						<td colspan="2">
						<p style="font-family: Arial; font-size: 8pt;">Não.</p>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<p style="font-family: Arial; font-size: 10pt;"><b>Neste
						caso, há possibilidade de compensação com outro item da
						programação autorizada (cota orçamentária)?</b></p>
						</td>
					</tr>
					<ww:if test="${intProOrc == '1'}">
						<tr>
							<td colspan="2">
							<p style="font-family: Arial; font-size: 8pt;">Sim.
							${jusIntProOrc}</p>
							</td>
						</tr>
					</ww:if>
					<ww:else>
						<tr>
							<td colspan="2">
							<p style="font-family: Arial; font-size: 8pt;">Não.</p>
							</td>
						</tr>
					</ww:else>
				</c:when>
			</c:choose>
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">8. Custos
				adicionais/decorrentes da contratação (caso a utilização/consumo do
				objeto gere outra contratação):</p>
				</td>
			</tr>
		</table>

		<ww:if test="${numCusAdicionais != 0}">
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="center" width="230">
					<p style="font-family: Arial; font-size: 10pt;"><b>Contratação/Aquisição</b></p>
					</td>
					<td align="center" width="70">
					<p style="font-family: Arial; font-size: 10pt;"><b>Valor
					estimado</b></p>
					</td>
				</tr>
			</table>

			<table cellpadding="4" width="100%" border="1">
				<c:forEach var="i" begin="1" end="${numCusAdicionais}">
					<tr>
						<td width="230">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('conAquisicao',i)]}</p>
						</td>
						<td align="center" width="70">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('valEstimado',i)]}</p>
						</td>
					</tr>
				</c:forEach>
			</table>
		</ww:if>
		<ww:else>
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="left" width="70%">
					<p style="font-family: Arial; font-size: 8pt;">Não há custos
					adicionais.</p>
					</td>
				</tr>
			</table>
		</ww:else>

		<br />


		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">9. Contratações
				semelhantes em outros órgãos (preferencialmente no Poder Judiciário
				Federal):</p>
				</td>
			</tr>
		</table>
		<ww:if test="${numContratacao != 0}">
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="center" width="170">
					<p style="font-family: Arial; font-size: 10pt;"><b>Órgão</b></p>
					</td>
					<td align="center" width="65">
					<p style="font-family: Arial; font-size: 10pt;"><b>Valor
					contratado</b></p>
					</td>
					<td align="center" width="65">
					<p style="font-family: Arial; font-size: 10pt;"><b>Exercício</b></p>
					</td>
				</tr>
			</table>
			<table cellpadding="4" width="100%" border="1">
				<c:forEach var="i" begin="1" end="${numContratacao}">
					<tr>
						<td align="left" width="170">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('orgao',i)]}</p>
						</td>
						<td align="center" width="65">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('valContratado',i)]}</p>
						</td>
						<td align="center" width="65">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('exercicio',i)]}</p>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td colspan="3">
					<p style="font-family: Arial; font-size: 10pt;"><b>Observação(ões):</b>&nbsp;</p>
					<p style="font-family: Arial; font-size: 8pt;">${observacao}</p>
					</td>
			</table>
		</ww:if>
		<ww:else>
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="left" width="70%">
					<p style="font-family: Arial; font-size: 8pt;">Não há
					contratações semelhantes.</p>
					</td>
				</tr>
			</table>
		</ww:else>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td>
				<p style="font-family: Arial; font-size: 10pt;">10. Fatores que
				podem colocar em risco a contratação:</p>
				</td>
			</tr>
		</table>
		<ww:if test="${numFator != 0}">
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="center" width="43%">
					<p style="font-family: Arial; font-size: 8pt;"><b>Risco*</b></p>
					</td>
					<td align="left" width="43%">
					<p style="font-family: Arial; font-size: 8pt;"><b>Resposta
					(ação para evitar ou diminuir ocorrência do risco)</b></p>
					</td>
					<td align="center" width="14%">
					<p style="font-family: Arial; font-size: 8pt;"><b>U.O.
					responsável<br />
					pela resposta</b></p>
					</td>
				</tr>
			</table>

			<table cellpadding="4" width="100%" border="1">
				<c:forEach var="i" begin="1" end="${numFator}">
					<tr>
						<td align="left" width="43%">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('risco',i)]}</p>
						</td>
						<td align="left" width="43%">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('resposta',i)]}</p>
						</td>
						<td align="center" width="14%">
						<p style="font-family: Arial; font-size: 8pt;">
						${requestScope[f:concat('responsavel',i)]}</p>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td colspan="3">
					<p style="font-family: Arial; font-size: 10pt;"><b>*</b>Risco é
					um evento que, se ocorrer, impacta em pelo menos um dos objetivos
					do projeto.</p>
					</td>
				</tr>
			</table>
		</ww:if>
		<ww:else>
			<table cellpadding="4" width="100%" border="1">
				<tr>
					<td align="left" width="70%">
					<p style="font-family: Arial; font-size: 8pt;">Não há fatores
					de risco.</p>
					</td>
				</tr>
			</table>
		</ww:else>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td colspan="2"><p style="font-family: Arial; font-size: 10pt;">11. Relação com contratações anteriores:</p></td>
			</tr>
		</table>
		<table cellpadding="4" width="100%" border="1">
			<ww:if test="${not empty conAntProAdm || not empty vigValAtual}">
				<tr>
					<td align="left" width="220"><p style="font-family: Arial; font-size: 10pt;"><b>Contratação anterior/Processo Administrativo</b></p></td>
					<td align="center" width="80"><p style="font-family: Arial; font-size: 10pt;"><b>Vigência - Valor atual</b></p></td>
				</tr>
				<tr>
					<td><p style="font-family: Arial; font-size: 8pt;">${conAntProAdm}</p></td>
					<td align="center"><p style="font-family: Arial; font-size: 8pt;">${vigValAtual}</p></td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td colspan="2" align="left" width="70%"><p style="font-family: Arial; font-size: 8pt;">Não houve contratações anteriores.</p></td>
				</tr>
			</ww:else>
			
			<ww:if test="${not empty difEncontrada}">
				<tr>
					<td colspan="2" align="center"><p style="font-family: Arial; font-size: 10pt;"><b>Dificuldades encontradas:</b></p></td>
				</tr>
				
				<tr>
					<td colspan="2"><p style="font-family: Arial; font-size: 8pt;">${difEncontrada}</p></td>
				</tr>
			</ww:if>
			
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td><p style="font-family: Arial; font-size: 10pt;">12. Legislação específica pertinente:</p></td>
			</tr>
		</table>
		<table cellpadding="4" width="100%" border="1">
			<ww:if test="${not empty legEspPertinente}">
				<tr>
					<td><p style="font-family: Arial; font-size: 8pt;">${legEspPertinente}</p></td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td align="left" width="70%"><p style="font-family: Arial; font-size: 8pt;">Não há legislação pertinente.</p></td>
				</tr>
			</ww:else>			
		</table>

		<br />

		<table cellpadding="4" width="100%" border="0">
			<tr>
				<td><p style="font-family: Arial; font-size: 10pt;">13. Informações complementares:</p></td>
			</tr>
		</table>	
		<table cellpadding="4" width="100%" border="1">
			<ww:if test="${not empty infComplementar}">
				<tr>
					<td><p style="font-family: Arial; font-size: 8pt;">${infComplementar}</p></td>
				</tr>
			</ww:if>
			<ww:else>
				<tr>
					<td align="left" width="70%"><p style="font-family: Arial; font-size: 8pt;">Não há informações complementares.</p></td>
				</tr>
			</ww:else>			
		</table>

		<br />

		<!-- INICIO PRIMEIRO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
			FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
			FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
