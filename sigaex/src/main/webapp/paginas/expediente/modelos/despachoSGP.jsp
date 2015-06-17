
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<mod:modelo>
	<mod:entrevista>
	
	
		<mod:grupo>
		<mod:selecao titulo="Seção" var="secao"
				opcoes="SELEG;SGP;SECAD;SASGP" reler="ajax" idAjax="secaoAjax" />
		</mod:grupo>
		<mod:grupo depende="secaoAjax">
			<ww:if test="${secao != 'SECAD' and secao != 'SASGP'}">
				<mod:selecao titulo="Assunto" var="assunto"
					opcoes="Ausência ao serviço;Licença-paternidade" reler="ajax"
					idAjax="assuntoAjax" />
				<mod:grupo depende="assuntoAjax">
					<c:choose>
						<c:when test="${assunto == 'Ausência ao serviço'}">
							<mod:selecao titulo="Razão" var="razao"
								opcoes="Casamento;Doação de Sangue;Falecimento em família"
								reler="ajax" idAjax="razaoAjax" />
							<mod:grupo depende="razaoAjax">
								<c:if test="${razao == 'Casamento'}">
									<mod:grupo>
										<mod:radio titulo="Sem concomitância com outro afastamento"
											var="concomitancia" valor="1" reler="ajax"
											idAjax="concomitanciaAjax" marcado="Sim" />
										<mod:radio titulo="Concomitância com férias"
											var="concomitancia" valor="2" reler="ajax"
											idAjax="concomitanciaAjax" />
										<mod:radio titulo="Indeferimento" var="concomitancia"
											valor="3" reler="ajax" idAjax="concomitanciaAjax" />
									</mod:grupo>
									<c:set var="valConcomitancia" value="${concomitancia}" />
									<c:if test="${empty valConcomitancia}">
										<c:set var="valConcomitancia"
											value="${param['concomitancia']}" />
									</c:if>
									<c:if test="${empty valConcomitancia}">
										<c:set var="valConcomitancia" value="1" />
									</c:if>
									<mod:grupo depende="concomitanciaAjax">
										<c:choose>
											<c:when test="${valConcomitancia == '1' or valConcomitancia == '2'}">
												<%-- Ausência ao serviço em razão de casamento(sem concomitância com outro afastamento ou concomitância com ferias --%>
												<c:if test="${secao == 'SELEG' or secao == 'SGP'}">													
													<mod:data titulo="Período de Ausência" var="dataIni" /> a <mod:data
														var="dataFim" titulo="" />
													<c:if test="${valConcomitancia == '2' and secao == 'SELEG'}">
														<mod:data titulo="Período de Férias" var="dataFeriasIni" /> a <mod:data
															var="dataFeriasFim" titulo="" />
													</c:if>
												</c:if> 												
											</c:when>
											<c:when test="${valConcomitancia == '3'}"> <%-- Indeferimento --%>
												<c:if test="${secao == 'SELEG' or secao == 'SGP'}">
													<mod:grupo titulo="Causa">
														<mod:grupo largura="30">
															<mod:radio titulo="a ausência de previsão legal"
																var="causaIndef" valor="1" marcado="Sim" />
														</mod:grupo>
														<mod:grupo largura="70">
															<mod:radio titulo="a irregularidade da documentação"
																var="causaIndef" valor="2" />
														</mod:grupo>
													</mod:grupo>
												</c:if>												
												<c:if test="${secao == 'SECAD'}">
													NÃO DEFINIDO ??????
												</c:if>
											</c:when>
										</c:choose>
									</mod:grupo>
								</c:if>
								<c:if test="${razao == 'Doação de Sangue'}">
									<mod:grupo>
										<c:if test="${secao == 'SELEG' or secao == 'SGP'}">
											<mod:data titulo="Data de doação" var="dataDoacao" />
										</c:if>										
									</mod:grupo>
								</c:if>
								<c:if test="${razao == 'Falecimento em família'}">
									<mod:grupo>
										<mod:grupo largura="50">
											<mod:radio titulo="Sem concomitância com outro afastamento"
												var="concomitancia" valor="1" reler="ajax"
												idAjax="concomitanciaAjax" marcado="Sim" />
											<mod:radio
												titulo="Concomitância com licença para tratar da própria saúde"
												var="concomitancia" valor="2" reler="ajax"
												idAjax="concomitanciaAjax" />
											<mod:radio
												titulo="Concomitância com licença por motivo de doença em pessoa da família"
												var="concomitancia" valor="3" reler="ajax"
												idAjax="concomitanciaAjax" />
											<mod:radio titulo="Indeferimento" var="concomitancia"
												valor="4" reler="ajax" idAjax="concomitanciaAjax" />
										</mod:grupo>
										<c:set var="valConcomitancia" value="${concomitancia}" />
										<c:if test="${empty valConcomitancia}">
											<c:set var="valConcomitancia"
												value="${param['concomitancia']}" />
										</c:if>
										<c:if test="${empty valConcomitancia}">
											<c:set var="valConcomitancia" value="1" />
										</c:if>
										<mod:grupo largura="50">
											<mod:grupo depende="concomitanciaAjax">
												<ww:if test="${valConcomitancia != '4'}">
													<mod:data titulo="Período de Ausência" var="dataIni" /> a <mod:data
																		var="dataFim" titulo="" />	
													<c:if test="${secao == 'SELEG'}">					
														<c:if test="${valConcomitancia == '2' or valConcomitancia == '3'}">					
															<mod:data titulo="Período de Licença" var="dataLicencaIni" /> a <mod:data
																			var="dataLicencaFim" titulo="" />	
														</c:if>
														<mod:radio
															titulo="Sem exclusão do parente falecido como dependente do I.R."
															var="exclusaoIR" valor="1" marcado="Sim" reler="ajax" idAjax="exclusaoIRajax" />
														<mod:radio
															titulo="Com exclusão do parente falecido como dependente do I.R."
															var="exclusaoIR" valor="2" reler="ajax" idAjax="exclusaoIRajax"/>
														<c:set var="valExclusaoIR" value="${exclusaoIR}" />
														<c:if test="${empty valExclusaoIR}">
															<c:set var="valExclusaoIR" value="${param['exclusaoIR']}" />
														</c:if>
														<c:if test="${empty valExclusaoIR}">
														
															<c:set var="valExclusaoIR" value="1" />
														</c:if>	
														<mod:grupo depende="exclusaoIRajax">
															<c:if test="${valExclusaoIR == '2'}"> <%-- com exclusão do parente falecido do IR --%>
																<mod:texto titulo="Número do Processo Administrativo" var="numPA" largura="10"/>
															</c:if>
														</mod:grupo>
													</c:if>
												</ww:if>
												<ww:else> <%-- ${valConcomitancia == '4' --%>
													<c:if test="${secao == 'SELEG' or secao == 'SGP'}">
														<mod:grupo titulo="Causa">
															<mod:grupo largura="30">
																<mod:radio titulo="a ausência de previsão legal"
																	var="causaIndef" valor="1" marcado="Sim" />
															</mod:grupo>
															<mod:grupo largura="70">
																<mod:radio titulo="a irregularidade da documentação"
																	var="causaIndef" valor="2" />
															</mod:grupo>
														</mod:grupo>
													</c:if>												
												</ww:else>
											</mod:grupo>
										</mod:grupo>
									</mod:grupo>
								</c:if>
							</mod:grupo>						  
						</c:when>
						<c:when test="${assunto == 'Licença-paternidade'}">
							<mod:grupo>
								<mod:radio titulo="Sem concomitância com outro afastamento"
									var="concomitancia" valor="1" reler="ajax"
									idAjax="concomitanciaAjax" marcado="Sim" />									
								<mod:radio titulo="Indeferimento" var="concomitancia"
									valor="2" reler="ajax" idAjax="concomitanciaAjax" />
							</mod:grupo>
							<c:set var="valConcomitancia" value="${concomitancia}" />
							<c:if test="${empty valConcomitancia}">
								<c:set var="valConcomitancia" value="${param['concomitancia']}" />
							</c:if>
							<c:if test="${empty valConcomitancia}">
								<c:set var="valConcomitancia" value="1" />
							</c:if>
							<mod:grupo depende="concomitanciaAjax">
								<ww:if test="${valConcomitancia == '1'}">
									<c:if test="${secao == 'SELEG' or secao == 'SGP'}">													
										<mod:data titulo="Período de Ausência" var="dataIni" /> a <mod:data	var="dataFim" titulo="" />
									</c:if>	
								</ww:if>
								<ww:else> <%-- ${valConcomitancia == '2'}  Indeferimento --%>
									<c:if test="${secao == 'SELEG' or secao == 'SGP'}">
								<mod:grupo titulo="Causa">
											<mod:grupo largura="30">
												<mod:radio titulo="a ausência de previsão legal"
													var="causaIndef" valor="1" marcado="Sim" />
											</mod:grupo>
											<mod:grupo largura="70">
												<mod:radio titulo="a irregularidade da documentação"
													var="causaIndef" valor="2" />
											</mod:grupo>
										</mod:grupo>
									</c:if>												
									<c:if test="${secao == 'SECAD'}">
										NÃO DEFINIDO ??????
									</c:if>
								</ww:else>
							</mod:grupo>	
						</c:when>  
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</mod:grupo>
			</ww:if>
			<ww:else>
				<%-- secao == 'SECAD'  ou == 'SASGP'--%>
				<mod:grupo>
					<c:choose>
						<c:when test="${secao == 'SECAD'}">
							<mod:data titulo="Data de Publicação" var="dataPubl" />
						</c:when>
						<c:when test="${secao == 'SASGP'}">
							<mod:data titulo="Data de Ciência do Servidor " var="dataCiencia" />
						</c:when>
					</c:choose>					
				</mod:grupo>
			</ww:else>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
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
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br />
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight: bold;">DESPACHO N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight: bold;"><br />REF. ${tipoDeDocumento} N&ordm; ${numero}, ${data} - ${orgao}.</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<mod:letra tamanho="${tl}">


			<ww:if test="${secao != 'SECAD' && secao != 'SASGP'}">
				<c:choose>
					<c:when test="${assunto == 'Ausência ao serviço'}">
						<c:choose>
							<c:when test="${razao == 'Casamento'}">
								<c:choose>
									<c:when test="${concomitancia == '1' or concomitancia == '2'}">
										<%-- Ausência ao serviço em razão de casamento --%>
										<c:choose>
											<c:when test="${secao == 'SELEG'}">											
												<ww:if test="${concomitancia == '1'}">
													<%-- concomitância com férias --%>
													<mod:oculto var="textoDespacho"
														valor="Sugiro o deferimento da ausência ao serviço no período de ${dataIni} a
																		${dataFim}, tendo em vista a regularidade da documentação." />

												</ww:if>
												<ww:else>
													<%--${concomitancia == '2' sem concomitância com outro afastamento--%>
													<mod:oculto var="textoDespacho"
														valor="Não obstante a regularidade da documentação, sugiro o deferimento
															da ausência ao serviço no período de ${dataIni} a ${dataIni}, tendo
															em vista que o(a) servidor(a) esteve em fruição de férias de 
															${dataFeriasIni}a ${dataFeriasFim},não havendo previsão legal para 
															que o período de ausência seja postergado." />
												</ww:else>
											</c:when> 											
											<c:when test="${secao == 'SGP'}"> <%-- o mesmo texto para concomitância = 1 ou = 2 --%> 
												<mod:oculto var="textoDespacho"
														valor="Defiro a ausência no período de ${dataIni} a {dataIni}, nos termos 
														do art. 97, III, “a”, da Lei nº 8.112/90.
														
														À SECAD para as providências cabíveis. " />
											</c:when>											
										</c:choose>
									</c:when>
									<c:when test="${concomitancia == '3'}"> <%-- Indeferimento --%>
										<c:choose>
											<c:when test="${secao == 'SELEG'}">
												<mod:oculto var="textoDespacho"
														valor="Sugiro o indeferimento da ausência ao serviço tendo em vista
														${causaIndef}. "/> 
										
											</c:when>
											<c:when test="${secao == 'SGP'}">
												<mod:oculto var="textoDespacho"
														valor="Indefiro tendo em vista ${causaIndef}. 

														À SASGP para cientificar o(a) servidor(a). Após, à SECAD para as providências cabíveis. "/> 									
											</c:when>
											<c:when test="${secao == 'SECAD'}">
												<%-- Verificar ???? --%>
										
											</c:when>
										</c:choose>
									</c:when>
								</c:choose>	
							</c:when>
							<c:when test="${razao == 'Doação de Sangue'}">
								<c:choose>
									<c:when test="${secao == 'SELEG'}">
										<mod:oculto var="textoDespacho"
													valor="Sugiro o deferimento da ausência ao serviço no dia ${dataDoacao}, tendo em vista a 
													comprovação da doação de sangue, bem como a anuência do superior hierárquico."/> 
									</c:when>
									<c:when test="${secao == 'SGP'}">
										<mod:oculto var="textoDespacho"
													valor="Defiro a ausência no dia ${dataDoacao}, nos termos do art. 97, I, da Lei nº 8.112/90.

													À SECAD para as providências cabíveis. "/> 
									</c:when>
								</c:choose>
							</c:when>
							<c:when test="${razao == 'Falecimento em família'}">
								<ww:if test="${concomitancia != '4'}">
									<c:if test="${concomitancia == '1'}">
										<c:choose>  <%-- PAREI AQUI --%>										
											<c:when test="${secao == 'SELEG'}">	
												<mod:oculto var="textoDespacho"
													valor="Sugiro o deferimento da ausência ao serviço no período de ___/___/___ a ___/___/___, 
													tendo em vista a regularidade da documentação. O(A) falecido(a) não era dependente no Imposto 
													de Renda na fonte."/> 
											</c:when>
										</c:choose>
										
									</c:if>
									<c:if test="${secao == 'SELEG'}">					
										<c:if test="${valConcomitancia == '2' or valConcomitancia == '3'}">					
											<mod:data titulo="Período de Licença" var="dataLicencaIni" /> a <mod:data
															var="dataLicencaFim" titulo="" />	
										</c:if>
										<mod:radio
											titulo="Sem exclusão do parente falecido como dependente do I.R."
											var="exclusaoIR" valor="1" marcado="Sim" reler="ajax" idAjax="exclusaoIRajax" />
										<mod:radio
											titulo="Com exclusão do parente falecido como dependente do I.R."
											var="exclusaoIR" valor="2" reler="ajax" idAjax="exclusaoIRajax"/>
										<c:set var="valExclusaoIR" value="${exclusaoIR}" />
										<c:if test="${empty valExclusaoIR}">
											<c:set var="valExclusaoIR" value="${param['exclusaoIR']}" />
										</c:if>
										<c:if test="${empty valExclusaoIR}">
										
											<c:set var="valExclusaoIR" value="1" />
										</c:if>	
										<mod:grupo depende="exclusaoIRajax">
											<c:if test="${valExclusaoIR == '2'}"> <%-- com exclusão do parente falecido do IR --%>
													<mod:texto titulo="Número do Processo Administrativo" var="numPA" largura="10" />
										</c:if> 
										</mod:grupo>
									</c:if> 
								 
								</ww:if>
							</c:when>
								
						</c:choose>
					</c:when>					
				</c:choose>
			</ww:if>
			<ww:else>
				<%-- secao == 'SECAD'  ou == 'SASGP'--%>
				<c:choose>
					<c:when test="${secao == 'SECAD'}">
						<mod:oculto var="textoDespacho" 
									valor="Publicado no Boletim Interno de ${dataPubl}.

									Providenciados os devidos registros. " />
					</c:when>
					<c:when test="${secao == 'SASGP'}">
						<mod:oculto var="textoDespacho" valor="(a) cientificado(a) em ${dataCiencia}."/> 
					</c:when>
				</c:choose>
				
			</ww:else>


				

				
				
				
				
			



			<span style="font-size: ${tl};">${texto}</span>
			<center>${doc.dtExtenso}</center>
			<p>&nbsp;</p>
			<c:import
				url="/paginas/expediente/modelos/inc_assinatura.jsp?textoFinal=${portDelegacao}" />

			<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

			<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		</mod:letra>
		</body>
		</html>
	</mod:documento>
</mod:modelo>

