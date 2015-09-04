<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>		
		<c:if test="${secao == 'SEGRA' or secao == 'SGS'}">	
			<c:choose>									
				<c:when test="${secao == 'SEGRA'}">
					<p><b>Quanto aos suprimentos (exceto papel):</b></p>
				</c:when>
				<c:otherwise>										
					<p><b>Quanto aos materiais/equipamentos de limpeza:</b></p>
				</c:otherwise>	
			</c:choose>			
			<mod:grupo titulo="A entrega foi realizada no prazo?">		
				<mod:radio titulo="Sim." var="entrNoPrazo" valor="1" marcado="Sim"
							reler="ajax" idAjax="entrNoPrazoAjax" />	
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="entrNoPrazo" valor="2" reler="ajax"
						idAjax="entrNoPrazoAjax" />
				</mod:grupo>
				<c:set var="entrNoPrazoVal" value="${entrNoPrazo}" />
				<c:if test="${empty entrNoPrazoVal}">
					<c:set var="entrNoPrazoVal" value="${param['entrNoPrazo']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="entrNoPrazoAjax">
						<c:if test="${entrNoPrazoVal == '2'}">
							<mod:memo titulo="Ressalvas" var="ressalvaEntrNoPrazo" linhas="2"
								colunas="60" obrigatorio="Sim" />
							<mod:oculto var="entrNoPrazoNao" valor="não"/>								
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>		
			<mod:grupo titulo="As quantidades solicitadas foram entregues?">
				<mod:radio titulo="Sim." var="entrQuant"  valor="1" marcado="Sim"
							reler="ajax" idAjax="entrQuantAjax" />	
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="entrQuant" valor="2" reler="ajax"
						idAjax="entrQuantAjax" />
				</mod:grupo>
				<c:set var="entrQuantVal" value="${entrQuant}" />
				<c:if test="${empty entrQuantVal}">
					<c:set var="entrQuantVal" value="${param['entrQuant']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="entrQuantAjax">
						<c:if test="${entrQuantVal == '2'}">
							<mod:memo titulo="Ressalvas" var="ressalvaEntrQuant" linhas="2"
								colunas="60" obrigatorio="Sim" />
							<mod:oculto var="entrQuantNao" valor="não"/>			
						</c:if>
					</mod:grupo>
				</mod:grupo>	
			</mod:grupo>
		</c:if>
		<c:if test="${secao == 'SIE'}">
			<p><b>Quanto aos materiais/equipamentos/ferramentas:</b></p>
			<mod:grupo titulo="Foram disponibilizados em tempo para a execução do serviço?">
				<mod:radio titulo="Sim." var="tempExec" valor="1" marcado="Sim"
								reler="ajax" idAjax="tempExecAjax" />			
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="tempExec" valor="2" reler="ajax"
						idAjax="tempExecAjax" />
				</mod:grupo>
				<c:set var="tempExecVal" value="${tempExec}" />
				<c:if test="${empty tempExecVal}">
					<c:set var="tempExecVal" value="${param['tempExec']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="tempExecAjax">
						<c:choose>									
							<c:when test="${tempExecVal == '2'}">
								<mod:memo titulo="Ressalvas" var="ressalvaTempExec" linhas="2"
									colunas="60" obrigatorio="Sim" />
								<mod:oculto var="tempExecNao" valor="Não foram"/>		
							</c:when>
							<c:otherwise>										
								<mod:oculto var="tempExecNao" valor="Foram"/>	
							</c:otherwise>	
						</c:choose>						
					</mod:grupo>
				</mod:grupo>			
			</mod:grupo>
		</c:if>		
		<c:if test="${secao == 'STI' or secao == 'DSEG'}">
			<mod:oculto var="servicos" valor="dos serviços prestados"/>	
		</c:if>
		<mod:grupo titulo="Como avalia a qualidade ${servicos}?">
			<mod:radio titulo="Satisfatória." var="qualidade"  valor="1" marcado="Sim"
						reler="ajax" idAjax="qualidadeAjax" />	
			<mod:radio titulo="Regular." var="qualidade" valor="2" reler="ajax"
					   idAjax="qualidadeAjax" />
		    <mod:radio titulo="Insatisfatória." var="qualidade" valor="3" reler="ajax"
						idAjax="qualidadeAjax" />
			<c:set var="qualidadeVal" value="${qualidade}" />
			<c:if test="${empty qualidadeVal}">
				<c:set var="qualidadeVal" value="${param['qualidade']}" />
			</c:if>		
			<mod:grupo depende="qualidadeAjax">
				<c:if test="${qualidadeVal == '2' or qualidadeVal == '3'}">
					<mod:memo titulo="Justificar" var="jusQualidade" linhas="2"
								colunas="60" obrigatorio="Sim" />	
     			</c:if>
			</mod:grupo>			
		</mod:grupo>
		<c:if test="${secao == 'STI'}">		
			<mod:grupo>
				<b><mod:texto titulo="Qual a quantidade de chamados atendidos pelo técnico de informática?"
							 var="numChamados" largura="5" maxcaracteres="5" valor="0"/></b>
			</mod:grupo>
		</c:if>	
		<c:if test="${secao == 'SEGRA' or secao == 'SGS' or secao == 'SIE'}">	
			<c:choose>
				<c:when test="${secao == 'SEGRA'}">			
					<mod:oculto var="tipoEquip" valor="equipamentos"/>	
				</c:when>			
				<c:when test="${secao == 'SGS' or secao == 'SIE'}">
					<mod:oculto var="tipoEquip" valor="mobiliários e eletrodomésticos"/>	
				</c:when>
			</c:choose>
			<mod:grupo titulo="Os ${tipoEquip} estão em boas condições?">
				<mod:radio titulo="Sim." var="boaCondEquip" valor="1" marcado="Sim"
							reler="ajax" idAjax="boaCondEquipAjax" />
				<mod:grupo largura="3">
					<mod:radio titulo="Não." var="boaCondEquip" valor="2" reler="ajax"
						idAjax="boaCondEquipAjax" />
				</mod:grupo>
				<c:set var="boaCondEquipVal" value="${boaCondEquip}" />
				<c:if test="${empty boaCondEquipVal}">
					<c:set var="boaCondEquipVal" value="${param['boaCondEquip']}" />
				</c:if>
				<mod:grupo largura="97">
					<mod:grupo depende="boaCondEquipAjax">  
						<c:if test="${boaCondEquipVal == '2'}">
							<mod:oculto var="boaCondEquipNao" valor="não" />
							A substituição foi solicitada?							
							<mod:radio titulo="Sim." var="substitEquip" valor="1" marcado="Sim"
												reler="ajax" idAjax="substitEquipAjax" />
							<mod:grupo>
							<mod:grupo largura="3">					
								<mod:radio titulo="Não." var="substitEquip" valor="2" 
												reler="ajax" idAjax="substitEquipAjax" />
											
								<c:set var="substitEquipVal" value="${substitEquip}" />
								<c:if test="${empty substitEquipVal}">
									<c:set var="substitEquipVal" value="${param['substitEquip']}" />
								</c:if>
								<mod:grupo largura="97">
									<mod:grupo depende="substitEquipAjax">	
										<c:if test="${substitEquipVal == '2'}">										 
											<mod:memo titulo="Ressalvas" var="ressalvaSubstitEquip" linhas="2"
								             colunas="60" obrigatorio="Sim" />
											<mod:oculto var="substitEquipNao" valor="não" />	
										</c:if>									
									</mod:grupo>
								</mod:grupo>	
							</mod:grupo>														
							</mod:grupo>														
						</c:if>					
					</mod:grupo>
				</mod:grupo>				 
			</mod:grupo>	
			<c:if test="${secao == 'SIE'}">
					<mod:radio titulo="Não se aplica." var="boaCondEquip" valor="3" 
								reler="ajax" idAjax="boaCondEquipAjax" />
			</c:if>				 								
		</c:if>
		<c:if test="${secao == 'SEGRA'}">		
			<mod:grupo>
				<b><mod:texto titulo="Quantas manutenções (preventivas e corretivas) foram feitas nos equipamentos?"
							 var="numManutencoes" largura="5" maxcaracteres="5" valor="0"/></b>
			</mod:grupo>
			<mod:grupo titulo="Como avalia a quantidade dos equipamentos?">
				<mod:radio titulo="Satisfatória." var="quantEquip"  valor="1" marcado="Sim"
							reler="ajax" idAjax="quantEquipAjax" />	
				<mod:radio titulo="Regular." var="quantEquip" valor="2" reler="ajax"
						idAjax="quantEquipAjax" />
				<mod:radio titulo="Insatisfatória." var="quantEquip" valor="3" reler="ajax"
						idAjax="quantEquipAjax" />		
				<c:set var="quantEquipVal" value="${quantEquip}" />
				<c:if test="${empty quantEquipVal}">
					<c:set var="quantEquipVal" value="${param['quantEquip']}" />
				</c:if>
				<mod:grupo depende="quantEquipAjax">
					<c:if test="${quantEquipVal == '2' or quantEquipVal == '3' }">				
						<mod:memo titulo="Justificar" var="jusQuantEquip" linhas="2"
								colunas="60" obrigatorio="Sim" />
				 	 </c:if>
				</mod:grupo>			
			</mod:grupo>	
			<mod:grupo titulo="O total de cópias impressas/imagens digitalizadas ultrapassou a franquia? ">	
				<mod:radio titulo="Não." var="totImag" valor="1" marcado="Sim"
							reler="ajax" idAjax="totImagAjax" />		
				<mod:grupo largura="7">
					<mod:radio titulo="Sim." var="totImag" valor="2" reler="ajax"
						idAjax="totImagAjax" />
				</mod:grupo>
				<c:set var="totImagVal" value="${totImag}" />
				<c:if test="${empty totImagVal}">
					<c:set var="totImagVal" value="${param['totImag']}" />
					<c:if test="${empty totImagVal}">
						<c:set var="totImagVal" value="1" />
					</c:if>
				</c:if>				
				<mod:grupo largura="93">
					<mod:grupo depende="totImagAjax">
						<c:choose>									
							<c:when test="${totImagVal == '2'}">
								<mod:memo titulo="Ressalvas" var="ressalvaTotImag" linhas="2"
									colunas="60" obrigatorio="Sim" />						
							</c:when>
							<c:otherwise>										
								<mod:oculto var="totImagNao" valor="não"/>	
							</c:otherwise>	
						</c:choose>						
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>
		</c:if>
		<mod:grupo titulo="Os valores do vale alimentação foram compatíveis com os dias trabalhados?">	
			<mod:radio titulo="Sim." var="valeAlim" valor="1" marcado="Sim" reler="ajax" idAjax="valeAlimAjax" />		
			<mod:grupo largura="7">
				<mod:radio titulo="Não." var="valeAlim" valor="2" reler="ajax" idAjax="valeAlimAjax" />
			</mod:grupo>
			<c:set var="valeAlimVal" value="${valeAlim}" />
			<c:if test="${empty valeAlimVal}">
				<c:set var="valeAlimVal" value="${param['valeAlim']}" />
			</c:if>
			<mod:grupo largura="93">
				<mod:grupo depende="valeAlimAjax">
					<c:if test="${valeAlimVal == '2'}">
						<mod:memo titulo="Ressalvas" var="ressalvaValeAlim" linhas="2" colunas="60" obrigatorio="Sim" />
						<mod:oculto var="valeAlimNao" valor="não"/>	
					</c:if>
				</mod:grupo>
			</mod:grupo>
			<mod:radio titulo="Não se aplica (cópias encaminhada diretamente para a sede)." var="valeAlim" valor="3" 
						reler="ajax" idAjax="valeAlimAjax" /> 	
		</mod:grupo>
		<mod:grupo titulo="Os valores do vale transporte foram compatíveis com os dias trabalhados?">	
			<mod:radio titulo="Sim." var="valeTransp" valor="1" marcado="Sim"
						reler="ajax" idAjax="valeTranspAjax" />		
			<mod:grupo largura="7">
				<mod:radio titulo="Não." var="valeTransp" valor="2" reler="ajax"
					idAjax="valeTranspAjax" />
			</mod:grupo>
			<c:set var="valeTranspVal" value="${valeTransp}" />
			<c:if test="${empty valeTranspVal}">
				<c:set var="valeTranspVal" value="${param['valeTransp']}" />
			</c:if>
			<mod:grupo largura="93">
				<mod:grupo depende="valeTranspAjax">
					<c:if test="${valeTranspVal == '2'}">
						<mod:memo titulo="Ressalvas" var="ressalvaValeTransp" linhas="2"
								colunas="60" obrigatorio="Sim" />
						<mod:oculto var="valeTranspNao" valor="não"/>		
					</c:if>
				</mod:grupo>
			</mod:grupo>
			<mod:radio titulo="Não se aplica (cópias encaminhada diretamente para a sede)." var="valeTransp" valor="3" 
						reler="ajax" idAjax="valeTranspAjax" />		
		</mod:grupo>
		<c:if test="${secao == 'SIE'}">
			<mod:grupo titulo="Os valores para deslocamento entre subseções foram compatíveis 
								com os dias trabalhados?">
				<mod:radio titulo="Sim." var="deslocamento" valor="1" marcado="Sim"
								reler="ajax" idAjax="deslocamentoAjax" />			
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="deslocamento" valor="2" reler="ajax"
						idAjax="deslocamentoAjax" />
				</mod:grupo>
				<c:set var="deslocamentoVal" value="${deslocamento}" />
				<c:if test="${empty deslocamentoVal}">
					<c:set var="deslocamentoVal" value="${param['deslocamento']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="deslocamentoAjax">
						<c:if test="${deslocamentoVal == '2'}">
							<mod:texto titulo="Ressalvas" var="ressalvaDeslocamento" largura="60"
								maxcaracteres="60" obrigatorio="Sim" />
							<mod:oculto var="deslocamentoNao" valor="não"/>		
						</c:if>										
					</mod:grupo>
				</mod:grupo>			
			</mod:grupo>
		</c:if>	
		<mod:grupo titulo="Os uniformes estão em boas condições?">
			<mod:radio titulo="Sim." var="boaCondUnif" valor="1" marcado="Sim" reler="ajax" idAjax="boaCondUnifAjax"/>
		
			<mod:grupo largura="3">
				<mod:radio titulo="Não." var="boaCondUnif" valor="2" reler="ajax" idAjax="boaCondUnifAjax"/>
			</mod:grupo> 

			<c:set var="boaCondUnifVal" value="${boaCondUnif}"/>
			<c:if test="${empty boaCondUnifVal}">
				<c:set var="boaCondUnifVal" value="${param['boaCondUnif']}"/>
			</c:if>

			<mod:grupo largura="97">
				<mod:grupo depende="boaCondUnifAjax">
					<c:if test="${boaCondUnifVal == '2'}">
						<mod:oculto var="boaCondUnifNao" valor="não"/>
						</br><b>A substituição foi solicitada?</b>
						<mod:radio titulo="Sim." var="substitUnif" valor="1" reler="ajax" idAjax="substitUnifAjax" marcado="Sim"/>
						<mod:grupo>
							<mod:grupo largura="3">
								<mod:radio titulo="Não." var="substitUnif" valor="2" reler="ajax" idAjax="substitUnifAjax"/>
								<c:set var="substitUnifVal" value="${substitUnif}"/>
								<c:if test="${empty substitUnifVal}">
									<c:set var="substitUnifVal" value="${param['substitUnif']}"/>
								</c:if>
								<mod:grupo largura="97">
									<mod:grupo depende="substitUnifAjax">	
										<c:if test="${substitUnifVal == '2'}">
											<mod:memo titulo="Ressalvas" var="ressalvaSubstitUnif" linhas="2"
								             colunas="60" obrigatorio="Sim"/>
											<mod:oculto var="substitUnifNao" valor="não"/>		
										</c:if>
									</mod:grupo>
								</mod:grupo>
							</mod:grupo>
						</mod:grupo>
							
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>	
		<mod:grupo titulo="Os contracheques apresentaram divergências ou irregularidades?">	
			<mod:radio titulo="Não." var="irregContCheq" valor="1" marcado="Sim"
						reler="ajax" idAjax="irregContCheqAjax" />		
			<mod:grupo largura="7">
				<mod:radio titulo="Sim." var="irregContCheq" valor="2" reler="ajax"
					idAjax="irregContCheqAjax" />
			</mod:grupo>
			<c:set var="irregContCheqVal" value="${irregContCheq}" />
			<c:if test="${empty irregContCheqVal}">
				<c:set var="irregContCheqVal" value="${param['irregContCheq']}" />
				<c:if test="${empty irregContCheqVal}">
						<c:set var="irregContCheqVal" value="1" /> 
					</c:if>
			</c:if>			
			<mod:grupo largura="93">
				<mod:grupo depende="irregContCheqAjax">
					<c:choose>									
						<c:when test="${irregContCheqVal == '2'}">
							<mod:memo titulo="Ressalvas" var="ressalvaIrregContCheq" linhas="2"
									colunas="60" obrigatorio="Sim" />						
						</c:when>
						<c:otherwise>										
							<c:if test="${irregContCheqVal == '1'}">
								<mod:oculto var="irregContCheqNao" valor="não"/>	
							</c:if>						
						</c:otherwise>	
					</c:choose>					
				</mod:grupo>
			</mod:grupo>
			<mod:radio titulo="Não se aplica (cópias encaminhada diretamente para a sede)." var="irregContCheq" valor="3" 
						reler="ajax" idAjax="irregContCheqAjax" />
		
		</mod:grupo>
		<c:if test="${secao == 'DSEG'}">
			<mod:grupo>
				<b><mod:memo titulo="Liste abaixo quaisquer observações adicionais (rondas, 
							manuteções de armamentos, etc.)" 
							var="obsAdicionais" linhas="3" colunas="80" /></b>
			</mod:grupo>
		</c:if>
		<c:if test="${secao == 'SEGRA' or secao == 'SGS' or secao == 'SIE'}">
			<mod:grupo titulo="Os funcionários prestaram os serviços em conformidade com a especificação?">		
				<mod:radio titulo="Sim." var="confEspecif" valor="1" marcado="Sim"
							reler="ajax" idAjax="confEspecifAjax" />	
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="confEspecif" valor="2" reler="ajax"
						idAjax="confEspecifAjax" />
				</mod:grupo>
				<c:set var="confEspecifVal" value="${confEspecif}" />
				<c:if test="${empty confEspecifVal}">
					<c:set var="confEspecifVal" value="${param['confEspecif']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="confEspecifAjax">
						<c:if test="${confEspecifVal == '2'}">
							<mod:memo titulo="Justificar" var="jusConfEspecif" linhas="2"
								colunas="60" obrigatorio="Sim" />
							<mod:oculto var="confEspecifNao" valor="não"/>		
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>	
		</c:if>
		<c:if test="${secao == 'SGS'}">
			<mod:grupo titulo="Ocorreu a visita mensal do supervisor da empresa contratada?">	
				<mod:radio titulo="Não." var="visitaSup" valor="1" marcado="Sim"
						reler="ajax" idAjax="visitaSupAjax" />
				<mod:grupo largura="3">
					<mod:radio titulo="Sim." var="visitaSup" valor="2" reler="ajax"
						idAjax="visitaSupAjax" />
				</mod:grupo>
				<c:set var="visitaSupVal" value="${visitaSup}" />
				<c:if test="${empty visitaSupVal}">
					<c:set var="visitaSupVal" value="${param['visitaSup']}" />
				</c:if>
				<mod:grupo largura="97">
					<mod:grupo depende="visitaSupAjax">  
						<c:choose>									
							<c:when test="${visitaSupVal == '2'}">
								<mod:grupo largura="15">
									<b>Como avalia a visita?</b>
									<mod:grupo largura ="85">
									<mod:radio titulo="Satisfatória" var="avalVisita" valor="1" marcado="Sim"
													reler="ajax" idAjax="avalVisitaAjax" />
									<mod:radio titulo="Regular." var="avalVisita" valor="2" 
												reler="ajax" idAjax="avalVisitaAjax" />
									<mod:radio titulo="Insatisfatória." var="avalVisita" valor="3" 
													reler="ajax" idAjax="avalVisitaAjax" />
									<c:set var="avalVisitaVal" value="${avalVisita}" />
									<c:if test="${empty avalVisitaVal}">
										<c:set var="avalVisitaVal" value="${param['avalVisita']}" />
									</c:if>								
									<mod:grupo depende="avalVisitaAjax">	
										<c:if test="${avalVisitaVal == '2' or avalVisitaVal == '3'}">										
											<mod:memo titulo="Justificar" var="jusAvalVisita" linhas="2"
											colunas="60" obrigatorio="Sim" />										
										</c:if>										
									</mod:grupo>						
									</mod:grupo>	
								</mod:grupo>															
							</c:when>
							<c:otherwise>										
								<mod:oculto var="visitaSupNao" valor="não" />
							</c:otherwise>	
						</c:choose>						
					</mod:grupo>
				</mod:grupo> 				
			</mod:grupo>					
		</c:if>
		<c:if test="${secao == 'SIE'}">
			<mod:grupo titulo="Os funcionários estão devidamente identificados (com crachá)? ">
				<mod:radio titulo="Sim." var="comIdentif" valor="1" marcado="Sim"
								reler="ajax" idAjax="comIdentifAjax" />
			
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="comIdentif" valor="2" reler="ajax"
						idAjax="comIdentifAjax" />
				</mod:grupo>
				<c:set var="comIdentifVal" value="${comIdentif}" />
				<c:if test="${empty comIdentifVal}">
					<c:set var="comIdentifVal" value="${param['comIdentif']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="comIdentifAjax">
						<c:if test="${comIdentifVal == '2'}">
							<mod:memo titulo="Observações" var="obsComIdentif" linhas="2"
								colunas="60" obrigatorio="Sim" />
							<mod:oculto var="comIdentifNao" valor="não"/>		
						</c:if>					
					</mod:grupo>
				</mod:grupo>			
			</mod:grupo>
			<mod:grupo titulo="Os equipamentos de segurança estão sendo utilizados?">
				<mod:radio titulo="Sim." var="equipUtil" valor="1" marcado="Sim"
								reler="ajax" idAjax="equipUtilAjax" />			
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="equipUtil" valor="2" reler="ajax"
						idAjax="equipUtilAjax" />
				</mod:grupo>
				<c:set var="equipUtilVal" value="${equipUtil}" />
				<c:if test="${empty equipUtilVal}">
					<c:set var="equipUtilVal" value="${param['equipUtil']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="equipUtilAjax">
						<c:if test="${equipUtilVal == '2'}">
							<mod:memo titulo="Ressalvas" var="ressalvaEquipUtil" linhas="2"
								colunas="60" obrigatorio="Sim" />
							<mod:oculto var="equipUtilNao" valor="não"/>		
						</c:if>										
					</mod:grupo>
				</mod:grupo>			
			</mod:grupo>
			<mod:grupo>
				<b><mod:texto titulo="Qual a quantidade de chamados atendidos pelo eletricista?"
							 var="numChamadosEletr" largura="5" maxcaracteres="5" valor="0"/></b>
			</mod:grupo>
			<mod:grupo>
				<b><mod:texto titulo="Qual a quantidade de chamados atendidos pelo bombeiro hidráulico?"
							 var="numChamadosBomb" largura="5" maxcaracteres="5" valor="0"/></b>
			</mod:grupo>
			<mod:grupo titulo="As rotinas de manutenção preventiva estão sendo realizadas (diárias, semanais, quinzenais, mensais, semestrais)?">		
				<mod:radio titulo="Sim." var="manutPrevent" valor="1" marcado="Sim"
							reler="ajax" idAjax="manutPreventAjax" />	
				<mod:grupo largura="7">
					<mod:radio titulo="Não." var="manutPrevent" valor="2" reler="ajax"
						idAjax="manutPreventAjax" />
				</mod:grupo>
				<c:set var="manutPreventVal" value="${manutPrevent}" />
				<c:if test="${empty manutPrevent}">
					<c:set var="manutPreventVal" value="${param['manutPrevent']}" />
				</c:if>
				<mod:grupo largura="93">
					<mod:grupo depende="manutPreventAjax">
						<c:if test="${manutPreventVal == '2'}">
							<mod:memo titulo="Justificar" var="jusManutPrevent" linhas="2"
								colunas="60" obrigatorio="Sim" />
							<mod:oculto var="manutPreventNao" valor="não"/>		
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>	
		</c:if>
		<c:if test="${secao == 'SGS' or secao == 'SIE'}">
			<c:choose>									
				<c:when test="${secao == 'SGS'}">
					<mod:oculto var="tipoServico" valor="jardinagem"/>	
				</c:when>
				<c:otherwise>										
					<mod:oculto var="tipoServico" valor="limpeza de calha"/>	
				</c:otherwise>	
			</c:choose>			
			<mod:grupo titulo="Possui serviço de ${tipoServico}?">
				<mod:radio titulo="Não." var="possuiTipo" valor="1" marcado="Sim" reler="ajax" idAjax="possuiTipoAjax"/>		
				<mod:grupo largura="3">
					<mod:radio titulo="Sim." var="possuiTipo" valor="2" reler="ajax" idAjax="possuiTipoAjax"/>
				</mod:grupo>	
				<c:set var="possuiTipoVal" value="${possuiTipo}"/>
				<c:if test="${empty possuiTipoVal}">
					<c:set var="possuiTipoVal" value="${param['possuiTipo']}"/>
				</c:if>
				<mod:grupo largura="97">
					<mod:grupo depende="possuiTipoAjax">
						<c:choose>									
							<c:when test="${possuiTipoVal == '2'}">
								<mod:oculto var="possuiTipoNao" valor="Possui"/>												
								<b>O cronograma foi cumprido?</b>
								<mod:radio titulo="Sim." var="cronograma" valor="1" reler="ajax" idAjax="cronogramaAjax" marcado="Sim"/>
							 	<mod:grupo>
									<mod:grupo largura="7">
										<mod:radio titulo="Não." var="cronograma" valor="2" reler="ajax" idAjax="cronogramaAjax"/>
										<c:set var="cronogramaVal" value="${cronograma}"/>
										<c:if test="${empty cronogramaVal}">
											<c:set var="cronogramaVal" value="${param['cronograma']}"/>
										</c:if>							
										<mod:grupo largura="93">
											<mod:grupo depende="cronogramaAjax">	
												<c:if test="${cronogramaVal == '2'}">
													<mod:memo titulo="Justificar" var="jusCronograma" linhas="2"
									                 colunas="60" obrigatorio="Sim"/>
													<mod:oculto var="cronogramaNao" valor="não"/>		
												</c:if>
											</mod:grupo>
										</mod:grupo>
									</mod:grupo>
								</mod:grupo>
								<b>Como avalia a execução dos serviços?</b>								
								<mod:radio titulo="Satisfatória" var="avalServico" valor="1" marcado="Sim"
												reler="ajax" idAjax="avalServicoAjax" />
								<mod:radio titulo="Regular." var="avalServico" valor="2" 
											reler="ajax" idAjax="avalServicoAjax" />
								<mod:radio titulo="Insatisfatória." var="avalServico" valor="3" 
												reler="ajax" idAjax="avalServicoAjax" />
								<c:set var="avalServicoVal" value="${avalServico}" />
								<c:if test="${empty avalServicoVal}">
									<c:set var="avalServicoVal" value="${param['avalServico']}" />
								</c:if>								
								<mod:grupo depende="avalServicoAjax">	
									<c:if test="${avalServicoVal == '2' or avalServicoVal == '3'}">										
										<mod:memo titulo="Justificar" var="jusAvalServico" linhas="2"
									colunas="60" obrigatorio="Sim" />										
									</c:if>										
								</mod:grupo>
								<b>Como avalia a qualidade e quantidade dos equipamentos/insumos?</b>								
								<mod:radio titulo="Satisfatória" var="avalEquip" valor="1" marcado="Sim"
												reler="ajax" idAjax="avalEquipAjax" />
								<mod:radio titulo="Regular." var="avalEquip" valor="2" 
											reler="ajax" idAjax="avalEquipAjax" />
								<mod:radio titulo="Insatisfatória." var="avalEquip" valor="3" 
												reler="ajax" idAjax="avalEquipAjax" />
								<c:set var="avalEquipVal" value="${avalEquip}" />
								<c:if test="${empty avalEquipVal}">
									<c:set var="avalEquipVal" value="${param['avalEquip']}" />
								</c:if>								
								<mod:grupo depende="avalEquipAjax">	
									<c:if test="${avalEquipVal == '2' or avalEquipVal == '3'}">										
										<mod:memo titulo="Justificar" var="jusAvalEquip" linhas="2"
									     colunas="60" obrigatorio="Sim" />										
									</c:if>										
								</mod:grupo>																											
							</c:when>
							<c:otherwise>										
								<mod:oculto var="possuiTipoNao" valor="Não possui"/>
							</c:otherwise>	
						</c:choose>						
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>	
		</c:if>
		<mod:grupo titulo="Frequência de Funcionários">
			<mod:selecao titulo="Índice de Freqüência" var="freqFunc" opcoes="Integral;Parcial" reler="ajax" idAjax="freqFuncAjax" />
			<mod:grupo depende="freqFuncAjax">
				<c:if test="${freqFunc eq 'Parcial'}">
					<c:choose>
					<c:when test="${secao == 'SEGRA' or secao == 'SGS' or secao == 'SIE'}">					
						<mod:selecao titulo="Nº de categorias profissionais a gerenciar" var="numCatProfis" opcoes="1;2;3" 
						reler="ajax" idAjax="numCatProfisAjax" />
						<mod:grupo depende="numCatProfisAjax">
							<c:forEach var="i" begin="1" end="${numCatProfis}">
								<mod:grupo>
									<b>${i}.</b><mod:texto titulo="Categoria profissional" var="catProfis${i}" largura="40" 
												obrigatorio="Sim" />
								</mod:grupo>
								<mod:grupo>	
									 &nbsp;&nbsp;&nbsp;<mod:texto titulo="Número de faltas sem reposição" var="numFaltas${i}" 
									 					largura="10" valor="0" />
								</mod:grupo>
								<mod:grupo>	
									 &nbsp;&nbsp;&nbsp;<mod:texto titulo="Quantidade de minutos em atrasos sem reposição" 
									 					var="quantMinutos${i}" largura="10" valor="0" />
								</mod:grupo>													
							</c:forEach>	
						</mod:grupo>							
					</c:when>	
					<c:when test="${secao == 'STI'}">	
						<mod:grupo>
							<mod:texto titulo="Número de faltas sem reposição" var="numFaltas" 
									 					largura="10" valor="0" />
						</mod:grupo>
						<mod:grupo>
							<mod:texto titulo="Quantidade de minutos em atrasos sem reposição" 
									 					var="quantMinutos" largura="10" valor="0" />
						</mod:grupo>			 					
					</c:when>			
					<c:when test="${secao == 'DSEG'}">
						<mod:grupo>
							<mod:selecao titulo="Informação simplificada" var="infSimpl" opcoes="Sim;Não"
							reler="ajax" idAjax="infSimplAjax" />
						</mod:grupo>
						<mod:grupo depende="infSimplAjax">
							<c:choose>
								<c:when test="${infSimpl == 'Sim'}">	
									<mod:grupo>
										<mod:texto titulo="Número de faltas sem reposição" var="numFaltas" 
										 					largura="10" valor="0" />
									</mod:grupo>
									<mod:grupo>
										<mod:texto titulo="Quantidade de minutos em atrasos sem reposição" 
										 					var="quantMinutos" largura="10" valor="0" />
									</mod:grupo>			
								</c:when>
								<c:otherwise>							
									<mod:radio titulo="com reposição" var="reposicao" valor="1" reler="ajax"
						  						idAjax="reposicaoAjax" marcado="Sim"/>
			    					<mod:radio titulo="sem reposição" var="reposicao" valor="2" reler="ajax"
												idAjax="reposicaoAjax" />
									<c:set var="reposicaoVal" value="${reposicao}" />
									<c:if test="${empty reposicaoVal}">
										<c:set var="reposicaoVal" value="${param['reposicao']}" />
										<c:if test="${empty reposicaoVal}">
											<c:set var="reposicaoVal" value="1" />
										</c:if>
									</c:if>		
									<mod:selecao titulo="Informe a quantidade de funcionários com frequência parcial" 
									var="numFuncParcial" opcoes="1;2;3;4;5;6;7;8;9;10" reler="ajax" idAjax="numFuncParcialAjax" />
									<mod:grupo depende="numFuncParcialAjax">	
										<mod:grupo depende="reposicaoAjax">								
											<c:forEach var="i" begin="1" end="${numFuncParcial}">											
												<mod:grupo>
													<b>${i}.</b><mod:data titulo="De" var="dataIni${i}"  />
																<mod:data titulo="Até" var="dataFim${i}"  />
												</mod:grupo>
												<mod:grupo>	
											 		&nbsp;&nbsp;&nbsp;<mod:texto titulo="Nome do funcionário faltoso" var="nomeFunc${i}" 
											 					largura="73"  />
												</mod:grupo>
												
												<c:choose>									
													<c:when test="${reposicaoVal == '1'}">
														<mod:grupo>	
													 		&nbsp;&nbsp;&nbsp;<mod:texto titulo="Nome do funcionário que cobriu a falta" var="nomeSubst${i}" 
													 					largura="62" />
														</mod:grupo>
														<mod:grupo>	
													 		&nbsp;&nbsp;&nbsp;<mod:texto titulo="Motivo da ausência" 
													 					var="motivo${i}" largura="81" />
														</mod:grupo>
													</c:when>
													<c:otherwise>										
														&nbsp;&nbsp;&nbsp;<mod:texto titulo="Observações" var="obs${i}" 
												 					largura="70" />
													</c:otherwise>	
												</c:choose>	
											</c:forEach>
										</mod:grupo>									
									</mod:grupo>									
								</c:otherwise>
							</c:choose>	
						</mod:grupo> 
					</c:when>
					</c:choose>	
					
					<c:choose>									
						<c:when test="${secao == 'DSEG'}">
							<mod:oculto var="titMemo" valor="Informações adicionais"/>						
						</c:when>
						<c:otherwise>										
							<mod:oculto var="titMemo" valor="Ressalvas"/>					
						</c:otherwise>	
					</c:choose>						
					<mod:grupo>
						<mod:memo titulo="${titMemo}" var="ressalvaFreq" linhas="2"
								colunas="80" />
					</mod:grupo>
				</c:if>				
			</mod:grupo>			
		</mod:grupo>	
		<mod:grupo>
			<c:if test="${secao == 'SIE'}">
				<b><mod:mensagem titulo="Observação" texto="comunicar diariamente ao gestor as faltas e os atrasos."> </mod:mensagem></b>
			</c:if>
			<c:if test="${secao == 'DSEG'}">
				<b><mod:mensagem titulo="Observação" texto="anexar a Planilha de Freqüência a este formulário no SIGA-DOC, em formato Pdf, e enviá-la também por e-mail ao gestor, em formato Excel."> </mod:mensagem></b>
			</c:if>
		</mod:grupo>	
	</mod:entrevista>
	<mod:documento>
		<c:choose>
			<c:when test="${qualidade == '1'}">
				<mod:oculto var="qualidadeNao" valor="satisfatória" />
			</c:when>
			<c:when test="${qualidade == '2'}">
				<mod:oculto var="qualidadeNao" valor="regular" />
			</c:when>
			<c:when test="${qualidade == '3'}">
				<mod:oculto var="qualidadeNao" valor="insatisfatória" />				
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${quantEquip == '1'}">
				<mod:oculto var="quantEquipNao" valor="satisfatória" />
			</c:when>
			<c:when test="${quantEquip == '2'}">
				<mod:oculto var="quantEquipNao" valor="regular" />
			</c:when>
			<c:when test="${quantEquip == '3'}">
				<mod:oculto var="quantEquipNao" valor="insatisfatória" />
			</c:when>
		</c:choose>	
		<c:choose>
			<c:when test="${avalVisita == '1'}">
				<mod:oculto var="avalVisitaNao" valor="satisfatória" />
			</c:when>
			<c:when test="${avalVisita == '2'}">
				<mod:oculto var="avalVisitaNao" valor="regular" />
			</c:when>
			<c:when test="${avalVisita == '3'}">
				<mod:oculto var="avalVisitaNao" valor="insatisfatória" />
			</c:when>
		</c:choose>	
		<c:choose>
			<c:when test="${avalServico == '1'}">
				<mod:oculto var="avalServicoNao" valor="satisfatória" />
			</c:when>
			<c:when test="${avalServico == '2'}">
				<mod:oculto var="avalServicoNao" valor="regular" />
			</c:when>
			<c:when test="${avalServico == '3'}">
				<mod:oculto var="avalServicoNao" valor="insatisfatória" />
			</c:when>
		</c:choose>	
		<c:choose>
			<c:when test="${avalEquip == '1'}">
				<mod:oculto var="avalEquipNao" valor="satisfatórias" />
			</c:when>
			<c:when test="${avalEquip == '2'}">
				<mod:oculto var="avalEquipNao" valor="regulares" />
			</c:when>
			<c:when test="${avalEquip == '3'}">
				<mod:oculto var="avalEquipNao" valor="insatisfatórias" />
			</c:when>
		</c:choose>		
		<c:choose>									
			<c:when test="${freqFunc eq 'Integral'}">
				<mod:oculto var="freqFuncTipo" valor="integral" />	
			</c:when>
			<c:otherwise>										
				<mod:oculto var="freqFuncTipo" valor="parcial" />	
			</c:otherwise>	
		</c:choose>	
		
		<table style="float: none; clear: both" width="100%" border="0" align="left" cellspacing="0" cellpadding="5">			
			<tr><th><b>
				<c:choose>
				<c:when test="${secao == 'SEGRA'}">
					Informações referentes aos suprimentos (exceto papel):
				</c:when>
				<c:when test="${secao == 'SGS'}">
					Informações referentes aos materiais/equipamentos de limpeza:
				</c:when>
				<c:when test="${secao == 'STI' or secao == 'DSEG'}">
					Informações referentes aos serviços:
				</c:when>
				<c:when test="${secao == 'SIE'}">
					Informações referentes aos materiais/equipamentos/ferramentas:
				</c:when>
				</c:choose>					
			</b></th></tr>
			<c:if test="${secao == 'SEGRA' or secao == 'SGS'}">		
				<tr><td>A entrega ${entrNoPrazoNao} foi realizada no prazo.
					<c:if test="${entrNoPrazo == '2'}">	
					<br>${ressalvaEntrNoPrazo} 					
					</c:if>
				</td></tr>	
				<tr><td>As quantidades solicitadas ${entrQuantNao} foram entregues.
					<c:if test="${entrQuant == '2'}">			
						<br>${ressalvaEntrQuant}					
					</c:if>
				</td></tr>	
			</c:if>
			<c:if test="${secao == 'SIE'}">		
				<tr><td>${tempExecNao} disponibilizados em tempo para a execução do serviço.
					<c:if test="${tempExec == '2'}">
						<br> ${ressalvaTempExec}					
					</c:if>
				</td></tr>	
			</c:if>
			
			<tr><td>A qualidade ${servicos} foi avalidada como ${qualidadeNao}.
				<c:if test="${qualidade == '2' or qualidade == '3'}">			
					<br>${jusQualidade}					
				</c:if>
			</td></tr>	
			<c:if test="${secao == 'STI'}">
				<tr><td>
				<c:choose>									
					<c:when test="${not empty numChamados and numChamados != '0'}">
						 Número de chamados atendidos pelo técnico de informática: ${numChamados}				
					</c:when>
					<c:otherwise>										
						Não houve abertura de chamados para o técnico de informática. 
					</c:otherwise>	
				</c:choose>					
				</td></tr>	
			</c:if>	
			<c:if test="${secao == 'SEGRA' or secao == 'SGS' or secao == 'SIE'}">
				<c:choose>									
					<c:when test="${boaCondEquip == '3'}">
						<tr><td>A informação referente às condições dos ${tipoEquip} não se aplica .</td></tr>
					</c:when>
					<c:otherwise>										
						<tr><td>Os ${tipoEquip} ${boaCondEquipNao} estão em boas condições. 
							<c:if test="${boaCondEquip == '2'}">			
								<br>&nbsp; - A substituição ${substitEquipNao} foi solicitada. 		
								<c:if test="${substitEquip == '2'}">			
									&nbsp;&nbsp;${ressalvaSubstitEquip} 					
								</c:if>			
							</c:if>
						</td></tr>
					</c:otherwise>	
				</c:choose>				
			</c:if>
			<c:if test="${secao == 'SEGRA'}">
				<tr><td>
				<c:choose>									
					<c:when test="${not empty numManutencoes and numManutencoes != '0'}">
						 Número de manutenções (preventivas e corretivas) nos equipamentos: ${numManutencoes}				
					</c:when>
					<c:otherwise>										
						Não houve manutenções (preventivas / corretivas) nos equipamentos. 
					</c:otherwise>	
				</c:choose>					
				</td></tr>	
				<tr><td>A quantidade dos equipamentos foi avalidada como ${quantEquipNao}.
					<c:if test="${quantEquip == '2' or quantEquip == '3'}">			
						<br>${jusQuantEquip}					
					</c:if>
				</td></tr>	
				<tr><td>O total de cópias impressas/imagens digitalizadas ${totImagNao} ultrapassou a franquia.
					<c:if test="${totImag == '2'}">	<%-- sim --%>		
						<br>${ressalvaTotImag}					
					</c:if>
				</td></tr>	
			</c:if>
			<c:choose>									
				<c:when test="${valeAlim == '3'}">
					<tr><td>A informação referente à compatibilidade dos valores do vale alimentação com os dias trabalhados não se aplica.
					(cópias encaminhadas diretamente para a sede).</td></tr>
				</c:when>
				<c:otherwise>										
					<tr><td>Os valores do vale alimentação ${valeAlimNao} foram compatíveis com os dias trabalhados.
						<c:if test="${valeAlim == '2'}">		
							<br>${ressalvaValeAlim}					
						</c:if>
					</td></tr>
				</c:otherwise>	
			</c:choose>	
			<c:choose>									
				<c:when test="${valeTransp == '3'}">
					<tr><td>A informação referente à compatibilidade dos valores do vale transporte com os dias trabalhados não se aplica.
					(cópias encaminhadas diretamente para a sede).</td></tr>
				</c:when>
				<c:otherwise>										
					<tr><td>Os valores do vale transporte ${valeTranspNao} foram compatíveis com os dias trabalhados.
						<c:if test="${valeTransp == '2'}">		
							<br>${ressalvaValeTransp}					
						</c:if>
					</td></tr>
				</c:otherwise>	
			</c:choose>	
			<c:if test="${secao == 'SIE'}">		
				<tr><td>Os valores para deslocamento entre subseções ${deslocamentoNao} foram compatíveis com os dias trabalhados.
					<c:if test="${deslocamento == '2'}">
						${ressalvaDeslocamento}					
					</c:if>
				</td></tr>	
			</c:if>	
			<tr><td>Os uniformes ${boaCondUnifNao} estão em boas condições. 
				<c:if test="${boaCondUnif == '2'}">			
					<br>&nbsp; - A substituição ${substitUnifNao} foi solicitada. 		
					<c:if test="${substitUnif == '2'}">			
						&nbsp;&nbsp;${ressalvaSubstitUnif} 					
					</c:if>			
				</c:if>
			</td></tr>	
            <c:if test="${irregContCheq == '2'}">
			<tr><td>Os contracheques ${irregContCheqNao} apresentaram divergências ou irregularidades.
				<c:if test="${irregContCheq == '2'}">	
				<br>${ressalvaIrregContCheq} 					
				</c:if>
			</c:if>
			</td></tr>
			
			     <c:if test="${irregContCheq == '1'}">   
               <tr> <td>Os contracheques não apresentaram divergências ou irregularidades.               
                </c:if>
                </td></tr>
              
               
                <c:if test="${irregContCheq == '3'}">   
                <tr><td>A informação referente às divergências ou irregularidades nos 
                        contracheques não se aplica. (cópias encaminhadas diretamente para a sede).                 
                </c:if>
            </td></tr>  	
			<c:if test="${secao == 'SEGRA' or secao == 'SGS' or secao == 'SIE'}">
				<tr><td>Os funcionários ${confEspecifNao} prestaram os serviços em conformidade com a especificação.
					<c:if test="${confEspecif == '2'}">	
					<br>${jusConfEspecif} 					
					</c:if>
				</td></tr>	
			</c:if>	
			<c:if test="${secao == 'DSEG'}">
				<c:if test="${not empty obsAdicionais}">
					<tr>
						<td>${obsAdicionais}</td>
					</tr>					
				</c:if>					
			</c:if>						
			<c:if test="${secao == 'SGS'}">
				<tr><td>A visita mensal do supervisor da empresa contratada ${visitaSupNao} ocorreu.
					<c:if test="${visitaSup == '2'}">	
						<br>A visita foi avaliada como ${avalVisitaNao}. 		
						<c:if test="${avalVisita == '2' or avalVisita =='3'}">			
							&nbsp;${jusAvalVisita} 					
						</c:if>							
					</c:if>
				</td></tr>
			</c:if>					
			<c:if test="${secao == 'SIE'}">
				<tr><td>Os funcionários ${comIdentifNao} estão devidamente identificados (com crachá).
					<c:if test="${comIdentif == '2'}">
						<br>${obsComIdentif}					
					</c:if>
				</td></tr>	
				<tr><td>Os equipamentos de segurança ${equipUtilNao} estão sendo utilizados.
					<c:if test="${equipUtil == '2'}">
						<br>${ressalvaEquipUtil}					
					</c:if>
				</td></tr>	
				<tr><td>
					<c:choose>									
						<c:when test="${not empty numChamadosEletr and numChamadosEletr != '0'}">
							 Número de chamados atendidos pelo eletricista: ${numChamadosEletr}				
						</c:when>
						<c:otherwise>										
							Não houve abertura de chamados para o eletricista. 
						</c:otherwise>	
					</c:choose>					
				</td></tr>	
				<tr><td>
					<c:choose>									
						<c:when test="${not empty numChamadosBomb and numChamadosBomb != '0'}">
							 Número de chamados atendidos pelo bombeiro hidráulico: ${numChamadosBomb}				
						</c:when>
						<c:otherwise>										
							Não houve abertura de chamados para o bombeiro hidráulico. 
						</c:otherwise>	
					</c:choose>					
				</td></tr>	
				<tr><td>As rotinas de manutenção preventiva ${manutPreventNao} estão sendo realizadas (diárias, semanais, quinzenais, mensais, semestrais).
					<c:if test="${manutPrevent == '2'}">	
						&nbsp;${jusManutPrevent} 					
					</c:if>
				</td></tr>	
			</c:if>
			<c:if test="${secao == 'SGS' or secao == 'SIE'}">
				<tr><td>${possuiTipoNao} serviço de ${tipoServico}. 
					<c:if test="${possuiTipo == '2'}">			
						<br>&nbsp; - O cronograma ${cronogramaNao} foi cumprido. 		
						<c:if test="${cronograma == '2'}">			
							&nbsp;&nbsp;${jusCronograma} 					
						</c:if>						
						<br>&nbsp; - A execução dos serviços foi avaliada como ${avalServicoNao}. 		
						<c:if test="${avalServico == '2' or avalServico =='3'}">			
							&nbsp;&nbsp;${jusAvalServico} 					
						</c:if>	
						<br>&nbsp; - A qualidade e quantidade dos equipamentos/insumos foram avaliadas como &nbsp; ${avalEquipNao}. 		
						<c:if test="${avalEquip == '2' or avalEquip =='3'}">			
							&nbsp;&nbsp;${jusAvalEquip} 					
						</c:if>	
					</c:if>
				</td></tr>		
			</c:if>			
		</table>
		<br>
			
<%--		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />  --%>
		
		
		
		<table style="float: none; clear: both" width="100%" border="0" align="left" cellspacing="0" cellpadding="5">
			<tr>
				<th><b>Informações referentes à frequência de funcionários: </b></th>
			</tr>			
			<tr>
				<td>Informo que o(s) funcionário(s) tiveram frequência ${freqFuncTipo} durante o período.</td>
			</tr>	
		</table>						
		<c:if test="${freqFunc eq 'Parcial'}">	
			<table style="float: none; clear: both" width="100%" border="0" align="left" cellspacing="0" cellpadding="5">
				<c:choose>
				<c:when test="${secao == 'SEGRA' or secao == 'SGS' or secao == 'SIE'}">			
					<c:forEach var="i" begin="1" end="${numCatProfis}">
						<tr><td width="5%">${i}.</td>
							<td width="95%">Categoria profissional: ${requestScope[f:concat('catProfis',i)]}</td>  																
						</tr>
						<tr><td width="5%"></td>
							<td width="95%"> 
								<c:choose>									
									<c:when test="${not empty requestScope[f:concat('numFaltas',i)] and requestScope[f:concat('numFaltas',i)] != '0'}">
										Número de faltas sem reposição: ${requestScope[f:concat('numFaltas',i)]} 			
									</c:when>
									<c:otherwise>										
										Não houve faltas sem reposição. 
									</c:otherwise>	
								</c:choose>										
						</td></tr>
						<tr><td width="5%"></td>	
							<td width="95%"> 	
								<c:choose>									
									<c:when test="${not empty requestScope[f:concat('quantMinutos',i)] and requestScope[f:concat('quantMinutos',i)] != '0'}">
										Quantidade de minutos em atrasos sem reposição: ${requestScope[f:concat('quantMinutos',i)]}	
									</c:when>
									<c:otherwise>										
										Não houve minutos em atrasos sem reposição.
									</c:otherwise>	
								</c:choose>									
						</td></tr>						
					</c:forEach>
				</c:when>				
				<c:when test="${(secao == 'STI') or (secao == 'DSEG' and infSimpl == 'Sim')}">
					<tr><td>
						<c:choose>									
							<c:when test="${not empty numFaltas and numFaltas != '0'}">
								Número de faltas sem reposição: ${numFaltas} 			
							</c:when>
							<c:otherwise>										
								Não houve faltas sem reposição. 
							</c:otherwise>	
						</c:choose>	
					</td></tr>				
					<tr><td>	
						<c:choose>									
							<c:when test="${not empty numFaltas and numFaltas != '0'}">
								Quantidade de minutos em atrasos sem reposição: ${quantMinutos} 			
							</c:when>
							<c:otherwise>										
								Não houve minutos em atrasos sem reposição.
							</c:otherwise>	
						</c:choose>						
					</td></tr>				
				</c:when>	
				<c:when test="${secao == 'DSEG' and infSimpl == 'Não'}">
					<c:forEach var="i" begin="1" end="${numFuncParcial}">
						<tr><td width="5%">${i}.</td>
							<td width="95%">Nome do funcionário faltoso: ${requestScope[f:concat('nomeFunc',i)]}</td>  																
						</tr>
						<c:choose>									
							<c:when test="${reposicao == '1'}">
								<tr><td width="5%"></td>
									<td width="95%">Nome do funcionário que cobriu a falta: ${requestScope[f:concat('nomeSubst',i)]}</td></tr>
								<tr><td width="5%"></td>
									<td width="95%">Motivo da ausência: ${requestScope[f:concat('motivo',i)]}</td></tr>
							</c:when>
							<c:otherwise>										
								<tr><td width="5%"></td>
								<td width="95%">Observações: ${requestScope[f:concat('obs',i)]}</td></tr>
							</c:otherwise>	
						</c:choose>							
					</c:forEach>					
				</c:when>					
				</c:choose>
				<c:if test="${not empty ressalvaFreq}">
					<tr>
						<td colspan="2">${titMemo}: ${ressalvaFreq} </td>
					</tr>
				</c:if>					
			</table>							
		</c:if>		

					
					
							
			
			
			
		
	</mod:documento>
</mod:modelo>
