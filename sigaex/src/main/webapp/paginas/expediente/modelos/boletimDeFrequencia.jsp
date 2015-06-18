<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Boletim de Frequência">
			<mod:selecao titulo="Mês de referência" var="mes"
				opcoes="Jan;Fev;Mar;Abr;Maio;Jun;Jul;Ago;Set;Out;Nov;Dez" />
			<c:if test="${mes=='Jan'}">
				<c:set var="numMes" value="01" />
			</c:if>
			<c:if test="${mes=='Fev'}">
				<c:set var="numMes" value="02" />
			</c:if>
			<c:if test="${mes=='Mar'}">
				<c:set var="numMes" value="03" />
			</c:if>
			<c:if test="${mes=='Abr'}">
				<c:set var="numMes" value="04" />
			</c:if>
			<c:if test="${mes=='Maio'}">
				<c:set var="numMes" value="05" />
			</c:if>
			<c:if test="${mes=='Jun'}">
				<c:set var="numMes" value="06" />
			</c:if>
			<c:if test="${mes=='Jul'}">
				<c:set var="numMes" value="07" />
			</c:if>
			<c:if test="${mes=='Ago'}">
				<c:set var="numMes" value="08" />
			</c:if>
			<c:if test="${mes=='Set'}">
				<c:set var="numMes" value="09" />
			</c:if>
			<c:if test="${mes=='Out'}">
				<c:set var="numMes" value="10" />
			</c:if>
			<c:if test="${mes=='Nov'}">
				<c:set var="numMes" value="11" />
			</c:if>
			<c:if test="${mes=='Dez'}">
				<c:set var="numMes" value="12" />
			</c:if>
			<mod:texto titulo="Ano" var="ano" largura="6" maxcaracteres="4"
				reler="ajax" idAjax="anoAjax" />
			<mod:grupo depende="anoAjax">
				<c:if test="${empty ano}">
					<mod:mensagem titulo="Alerta"
						texto="o ano do Boletim deve ser preenchido." vermelho="Sim" />
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:lotacao titulo="Lotação" var="lotacao" reler="sim" />
			</mod:grupo>

			<input type="hidden" id="atualizandoCaixaVerif"
				name="atualizandoCaixaVerif" />
			<mod:caixaverif var="subLotacoes" reler="sim" marcado="Sim"
				titulo="Incluir Sublotações"
				onclique="javascript:document.getElementById('atualizandoCaixaVerif').value=1;" />
			<c:if test="${requestScope['subLotacoes'] eq 'Sim'}">
				<c:set var="testarSubLotacoes" value="true" />
			</c:if>
			<c:if test="${requestScope['subLotacoes'] != 'Sim'}">
				<c:set var="testarSubLotacoes" value="false" />
			</c:if>

		</mod:grupo>
		<mod:grupo>
			<b> <mod:mensagem titulo="Atenção"
				texto="preencha o destinatário com SECAD e, após finalizar, transfira para a SECAD." />
			</b>
		</mod:grupo>

		<%-- O bloco abaixo carrega do BD as pessoas de um lotação, apenas quando a lotação muda --%>
		<mod:ler var="siglaLotacaoAnterior" />
		<c:choose>
			<c:when
				test="${(requestScope['lotacao_lotacaoSel.sigla'] != siglaLotacaoAnterior) or (param['atualizandoCaixaVerif']) == 1}">
				<c:set var="pessoas"
					value="${f:pessoasPorLotacao(param['lotacao_lotacaoSel.id'], testarSubLotacoes)}" />
				<c:set var="i" value="${0}" />
				<c:forEach var="pes" items="${pessoas}">
					<c:set var="i" value="${i+1}" />
					<mod:oculto var="sigla${i}" valor="${pes.sigla}" />
					<mod:oculto var="nome${i}" valor="${pes.descricao}" />
				</c:forEach>
				<mod:oculto var="contadorDePessoas" valor="${i}" />
				<mod:oculto var="siglaLotacaoAnterior"
					valor="${requestScope['lotacao_lotacaoSel.sigla']}" />
			</c:when>
			<c:otherwise>
				<mod:oculto var="contadorDePessoas" />
				<mod:oculto var="siglaLotacaoAnterior"
					valor="${siglaLotacaoAnterior}" />
				<c:forEach var="i" begin="1" end="${contadorDePessoas}">
					<mod:oculto var="sigla${i}" />
					<mod:oculto var="nome${i}" />
				</c:forEach>
			</c:otherwise>
		</c:choose>

		<c:forEach var="i" begin="1" end="${contadorDePessoas}">
			<c:set var="pes_descricao"
				value="${requestScope[f:concat('nome',i)]}" />
			<c:set var="pes_sigla" value="${requestScope[f:concat('sigla',i)]}" />

			<mod:grupo largura="30">${pes_descricao}</mod:grupo>
			<mod:grupo largura="70">
				<mod:grupo>
					<mod:selecao titulo="Frequência" var="freq${pes_sigla}"
						opcoes="Sem lançamentos;Com lançamentos" reler="ajax"
						idAjax="ajax${pes_sigla}" />
				</mod:grupo>
				<mod:grupo depende="ajax${pes_sigla}">
					<c:if
						test="${(requestScope[f:concat('freq',pes_sigla)] == 'Com lançamentos')}">
						<mod:selecao titulo="Ausência(s)" var="faltas${pes_sigla}"
							opcoes="1;2;3;4;5" reler="ajax" idAjax="ajax${pes_sigla}" />
						<c:forEach var="i" begin="1"
							end="${requestScope[f:concat('faltas',pes_sigla)]}">
							<mod:grupo>
								<mod:selecao
									titulo="Motivo (As tabelas da Base Legal encontram-se na página da SECAD na Intranet)"
									var="motivo${i}${pes_sigla}"
									opcoes="[Selecione o tipo de Ausência];Afastamento em Virtude de Participação em Programa de Treinamento;Afastamento para Participar de Atividade Instrutória;Afastamento por júri e outros serviços obrigatórios por lei;Atraso/Saída Antecipada;Ausência/Falta Justificada com Compensação;Ausência/Falta Justificada sem Compensação;Ausência por motivo de greve com compensação;Ausência por motivo de greve sem compensação;Compensação de Trabalho no Recesso;Compensação Decorrente de Trabalho nas Eleições;Falta Injustificada;Recesso;Trabalho Prestado nas Eleições;"
									reler="ajax" idAjax="ajax${pes_sigla}" />
							</mod:grupo>
							<c:choose>
								<c:when
									test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == '[Selecione o tipo de Ausência]'}">								</c:when>
								<c:when
									test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Compensação de Trabalho no Recesso'}">
									<mod:texto titulo="Datas" var="datas${i}${pes_sigla}"
										largura="60" />
								</c:when>
								<c:when
									test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Atraso/Saída Antecipada'}">
									<mod:data titulo="Data" var="data${i}${pes_sigla}"
										reler="ajax" idAjax="dataAjax${i}${pes_sigla}" />
									<mod:hora titulo="das" var="horaIni${i}${pes_sigla}"
										reler="ajax" idAjax="horaIniAjax${i}${pes_sigla}" />
									<mod:hora titulo="às" var="horaFim${i}${pes_sigla}"
										reler="ajax" idAjax="horaFimAjax${i}${pes_sigla}" />	
								</c:when>
								<c:otherwise>
									<mod:grupo>
										<mod:data titulo="De" var="de${i}${pes_sigla}" reler="ajax"
											idAjax="deAjax${i}${pes_sigla}" />
									</mod:grupo>
									<mod:grupo depende="deAjax${i}${pes_sigla}">
										<c:if
											test="${not empty requestScope[f:concat(f:concat('de',i),pes_sigla)] and not empty ano}">
											<c:choose>
												<c:when
													test="${f:mesModData(requestScope[f:concat(f:concat('de',i),pes_sigla)])!= f:concat(f:concat(numMes,'/'),ano)}">
													<mod:grupo>
														<mod:mensagem titulo="Alerta"
															texto="A data inicial informada deve ser igual ao mês ou ano referido"
															vermelho="Sim" />
													</mod:grupo>
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
										</c:if>
									</mod:grupo>
									<mod:data titulo="Até" var="ate${i}${pes_sigla}" reler="ajax"
										idAjax="ateAjax${i}${pes_sigla}" />
									<mod:grupo depende="ateAjax${i}${pes_sigla}">
										<c:if
											test="${not empty requestScope[f:concat(f:concat('ate',i),pes_sigla)] and not empty ano}">
											<c:choose>
												<c:when
													test="${f:mesModData(requestScope[f:concat(f:concat('ate',i),pes_sigla)])!= f:concat(f:concat(numMes,'/'),ano)}">
													<mod:grupo>
														<mod:mensagem titulo="Alerta"
															texto="A data final informada deve ser igual ao mês ou ano referido"
															vermelho="Sim" />
													</mod:grupo>
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
										</c:if>
									</mod:grupo>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</c:forEach>
		<c:if test="${not empty requestScope['lotacao_lotacaoSel.sigla']}">
			<mod:grupo>
				<mod:mensagem titulo="Total de pessoas por Lotação :" />
				<b>${contadorDePessoas}</b>
			</mod:grupo>
		</c:if>
		<c:if
			test="${(not empty requestScope['lotacao_lotacaoSel.descricao'])}">
			<mod:grupo>
				<mod:selecao titulo="Informações Adicionais ?"
					var="informacoesAdicionais" opcoes="Não;Sim" reler="ajax"
					idAjax="infoAdicionais" />
				<mod:grupo depende="infoAdicionais">
					<c:if test="${informacoesAdicionais eq 'Sim'}">
						<mod:memo colunas="60" linhas="3" titulo="Informações"
							var="informacoes" />
					</c:if>
				</mod:grupo>
			</mod:grupo>

			<mod:grupo>
				<mod:selecao titulo="Houve servidores incluidos na lotação no mês ?"
					var="servIncluidos" opcoes="Não;Sim" reler="ajax" idAjax="servIncl" />
			</mod:grupo>
			<mod:grupo depende="servIncl">
				<c:if test="${servIncluidos eq 'Sim'}">
					<mod:grupo>
						<mod:selecao titulo="Nº de Servidor(es) Incluido(s) :"
							var="numServIncluidos"
							opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30"
							reler="ajax" idAjax="numServIncl" />
					</mod:grupo>
					<mod:grupo depende="numServIncl">
						<c:forEach var="i" begin="1" end="${numServIncluidos}">
							<mod:grupo>
								<mod:texto titulo="${i}) Nome :" var="nomeServIncluido${i}"
									largura="55" />
								<mod:texto titulo="Matr&iacute;cula :" var="matServIncluido${i}"
									largura="15" />
								<mod:data titulo="A partir de" var="dataIncluso${i }" />
								<mod:grupo>
									<mod:caixaverif titulo="Houve ausência deste servidor ?"
										var="periodoAusencia${i}" reler="ajax"
										idAjax="pedAusencia${i}" />
									<mod:grupo depende="pedAusencia${i}">
										<c:if
											test="${requestScope[f:concat('periodoAusencia',i)] == 'Sim'}">
											<mod:grupo>
												<mod:texto titulo="Ausência :" var="ausencia${i}"
													largura="40" />
												<mod:data titulo="De" var="dataInicio${i}" />
												<mod:data titulo="a" var="dataFim${i}" />
											</mod:grupo>
										</c:if>
									</mod:grupo>
								</mod:grupo>
							</mod:grupo>
						</c:forEach>
					</mod:grupo>
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Houve servidores excluídos da lotação no mês ?"
					var="servExcluidos" opcoes="Não;Sim" reler="ajax" idAjax="servExcl" />
			</mod:grupo>
			<mod:grupo depende="servExcl">
				<c:if test="${servExcluidos == 'Sim'}">
					<mod:grupo>
						<mod:selecao titulo="Nº de Servidor(es) Excluído(s) :"
							var="numServExcluidos"
							opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30"
							reler="ajax" idAjax="numServExcl" />
					</mod:grupo>
					<mod:grupo depende="numServExcl">
						<c:forEach var="j" begin="1" end="${numServExcluidos}">
							<mod:grupo>
								<mod:texto titulo="${j}) Nome :" var="nomeServExcluido${j}"
									largura="55" />
								<mod:texto titulo="Matr&iacute;cula :" var="matServExcluido${j}"
									largura="15" />
								<mod:data titulo="A partir de" var="dataExcluido${j}" />
								<mod:grupo>
									<mod:caixaverif
										titulo="Houve ausência deste servidor até a data em questão ?"
										var="periodoAusenciaExcluido${j}" reler="ajax"
										idAjax="pedAusExcl${j}" />
									<mod:grupo depende="pedAusExcl${j}">
										<c:if
											test="${requestScope[f:concat('periodoAusenciaExcluido',j)] == 'Sim'}">
											<mod:grupo>
												<mod:texto titulo="Ausência :" var="ausenciaExcluido${j}"
													largura="40" />
												<mod:data titulo="De" var="dataInicioExclusao${j}" />
												<mod:data titulo="a" var="dataFimExclusao${j}" />
											</mod:grupo>
										</c:if>
									</mod:grupo>
								</mod:grupo>
							</mod:grupo>
						</c:forEach>
					</mod:grupo>
				</c:if>
			</mod:grupo>
		</c:if>
	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 3cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<c:set var="tl" value="9pt" />
		<c:set var="t2" value="7pt" />
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<p style="text-align: center;"><br />
		<b>BOLETIM DE FREQUÊNCIA</b><br />
		<br />
		</p>

		<table width="100%" border="0" cellpadding="2" cellspacing="1"
			bgcolor="#ffffff">
			<tr>
				<td width="30%">Unidade Organizacional:</td>
				<td width="70%">${f:lotacao(requestScope['lotacao_lotacaoSel.id']).descricao}</td>
			</tr>
			<tr>
				<td>Mês/Ano:</td>
				<td>${mes}/${ano}</td>
			</tr>
			<tr>
				<td>Data da emissão:</td>
				<td>${doc.dtDocDDMMYY}</td>
			</tr>
		</table>
		<br />

		<mod:letra tamanho="${t2}">
			<table width="100%" border="1" cellpadding="2" cellspacing="1"
				bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="15%" align="center"><b>Matrícula</b></td>
					<td bgcolor="#FFFFFF" width="35%" align="center"><b>Nome</b></td>
					<td bgcolor="#FFFFFF" width="15%" align="center"><b>Frequência</b></td>
					<td bgcolor="#FFFFFF" width="35%" align="center"><b>Período/Motivo</b></td>
				</tr>

				<c:forEach var="i" begin="1" end="${contadorDePessoas}">
					<c:set var="pes_descricao"
						value="${requestScope[f:concat('nome',i)]}" />
					<c:set var="pes_sigla" value="${requestScope[f:concat('sigla',i)]}" />
					<c:if
						test="${(requestScope[f:concat('freq',pes_sigla)] == 'Com lançamentos')}">
						<tr>
							<td bgcolor="#FFFFFF" width="15%" align="center">${pes_sigla}</td>
							<td bgcolor="#FFFFFF" width="35%" align="center">${pes_descricao}</td>
							<td bgcolor="#FFFFFF" width="15%" align="center">${requestScope[f:concat('freq',pes_sigla)]}</td>
							<td bgcolor="#FFFFFF" width="35%" align="center"><c:forEach
								var="i" begin="1"
								end="${requestScope[f:concat('faltas',pes_sigla)]}">
								<c:choose>
									<c:when
										test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == '[Selecione o tipo de Ausência]'}"> Não foi selecionado um tipo de ausência </c:when>
									<c:when
										test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Compensação de Trabalho no Recesso'}"> ${requestScope[f:concat(f:concat('datas',i),pes_sigla)]} - ${requestScope[f:concat(f:concat('motivo',i),pes_sigla)]}
											<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Compensação de Trabalho no Recesso'}">
												 	(PORTARIA DIRFO)	
											</c:if>
									</c:when>
									<c:when
										test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Atraso/Saída Antecipada'}"> ${requestScope[f:concat(f:concat('data',i),pes_sigla)]} - das ${requestScope[f:concat(f:concat('horaIni',i),pes_sigla)]} às ${requestScope[f:concat(f:concat('horaFim',i),pes_sigla)]} - ${requestScope[f:concat(f:concat('motivo',i),pes_sigla)]}
											<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Atraso/Saída Antecipada'}">
												 	(art.44, INCISO II LEI 8112/90)	
											</c:if>
									</c:when>
									<c:otherwise>
													De ${requestScope[f:concat(f:concat('de',i),pes_sigla)]}
													a ${requestScope[f:concat(f:concat('ate',i),pes_sigla)]}
													- ${requestScope[f:concat(f:concat('motivo',i),pes_sigla)]}
											
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Falta Injustificada'}">
													(art.44, INCISO I LEI 8112/90)
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Afastamento por Júri e Outros Serv. Obrigat. por Lei'}">
													(art.102, INCISO VI, LEI 8112/90)
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Afastamento em Virtude de Particip. em Programa de Treinamento'}">
													(art.102, INCISO IV LEI 8112/90)
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Trabalho Prestado nas Eleições'}">
												 	(art.120, &sect;2º LEI 4737/65 C/C art.98 LEI 9504/97)	
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Compensação Decorrente de Trabalho nas Eleições'}">
												 	(arts.218 e 221/CPP e 412, &sect;2º /CPC)	
											</c:if>

										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Recesso'}">
												 	(PORTARIA DIRFO)	
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Afastamento para Participar de Atividade Instrutória'}">
													(art.76-A, LEI 8112/90 C/C art.8º DECRETO 6114/07)	 		
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Ausência/Falta Justificada com Compensação'}">
													(art. 44, II da Lei 8112/90)
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Ausência/Falta Justificada sem Compensação'}">
													(art. 44, II da Lei 8112/90)
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Ausência por motivo de greve com compensação'}">
													(art. 2º, II da Res. 419/2005 do CJF)
											</c:if>
										<c:if
											test="${requestScope[f:concat(f:concat('motivo',i),pes_sigla)] == 'Ausência por motivo de greve sem compensação'}">
													(art. 2º, I da Res. 419/2005 do CJF)
											</c:if>
									</c:otherwise>
								</c:choose>
								<br>
							</c:forEach></td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
			<c:if test="${servIncluidos eq 'Sim'}">
				<table width="100%" border="1" cellpadding="2" cellspacing="1"
					bgcolor="#000000">
					<tr>
						<td colspan="2" bgcolor="#FFFFFF" align="center"><b>SERVIDOR(ES)
						INCLUÍDO(S) DA LOTAÇÃO</b></td>
					</tr>
				</table>
				<table width="100%" border="1" cellpadding="2" cellspacing="1"
					bgcolor="#000000">
					<tr>
						<td bgcolor="#FFFFFF" width="50%" align="center"><b>Nome</b></td>
						<td bgcolor="#FFFFFF" width="25%" align="center"><b>Matr&iacute;cula</b></td>
						<td bgcolor="#FFFFFF" width="25%" align="center"><b>Período/Motivo</b></td>
					</tr>
					<c:forEach var="i" begin="1" end="${numServIncluidos}">
						<tr>
							<td bgcolor="#FFFFFF" width="50%" align="center">${requestScope[f:concat('nomeServIncluido',i)]}
							</td>
							<td bgcolor="#FFFFFF" width="25%" align="center">${requestScope[f:concat('matServIncluido',i)]}
							</td>
							<td bgcolor="#FFFFFF" width="25%" align="center"><c:choose>
								<c:when
									test="${(not empty requestScope[f:concat('dataIncluso',i)])}">
								a partir de ${requestScope[f:concat('dataIncluso',i)]} 
							</c:when>
								<c:otherwise></c:otherwise>
							</c:choose> <c:if
								test="${requestScope[f:concat('periodoAusencia',i)] eq 'Sim'}">: obteve <b>${requestScope[f:concat('ausencia',i)]}</b>
								<c:choose>
									<c:when
										test="${(empty requestScope[f:concat('dataInicio',i)]) and (empty requestScope[f:concat('dataFim',i)])}">
									</c:when>
									<c:when
										test="${requestScope[f:concat('dataInicio',i)] == requestScope[f:concat('dataFim',i)] || empty requestScope[f:concat('dataFim',i)]}">
											no dia <b>${requestScope[f:concat('dataInicio',i)]}</b>.
									</c:when>
									<c:otherwise>
									no período de <b>${requestScope[f:concat('dataInicio',i)]}</b> a <b>${requestScope[f:concat('dataFim',i)]}</b>.
								</c:otherwise>
								</c:choose>
							</c:if></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<c:if test="${servExcluidos eq 'Sim'}">
				<table width="100%" border="1" cellpadding="2" cellspacing="1"
					bgcolor="#000000">
					<tr>
						<td colspan="2" bgcolor="#FFFFFF" align="center"><b>SERVIDOR(ES)
						EXCLUÍDO(S) DA LOTAÇÃO</b></td>
					</tr>
				</table>
				<table width="100%" border="1" cellpadding="2" cellspacing="1"
					bgcolor="#000000">
					<tr>
						<td bgcolor="#FFFFFF" width="50%" align="center"><b>Nome</b></td>
						<td bgcolor="#FFFFFF" width="25%" align="center"><b>Matr&iacute;cula</b></td>
						<td bgcolor="#FFFFFF" width="25%" align="center"><b>Período/Motivo</b></td>
					</tr>
					<c:forEach var="j" begin="1" end="${numServExcluidos}">
						<tr>
							<td bgcolor="#FFFFFF" width="50%" align="center">${requestScope[f:concat('nomeServExcluido',j)]}
							</td>
							<td bgcolor="#FFFFFF" width="25%" align="center">${requestScope[f:concat('matServExcluido',j)]}
							</td>
							<td bgcolor="#FFFFFF" width="25%" align="center"><c:choose>
								<c:when
									test="${(not empty requestScope[f:concat('dataExcluido',j)])}">
								a partir de ${requestScope[f:concat('dataExcluido',j)]} 
							</c:when>
								<c:otherwise></c:otherwise>
							</c:choose> <c:if
								test="${requestScope[f:concat('periodoAusenciaExcluido',j)] eq 'Sim'}">: obteve <b>${requestScope[f:concat('ausenciaExcluido',j)]}</b>
								<c:choose>
									<c:when
										test="${(empty requestScope[f:concat('dataInicioExclusao',j)]) and (empty requestScope[f:concat('dataFimExclusao',j)])}">
									</c:when>
									<c:when
										test="${requestScope[f:concat('dataInicioExclusao',j)] == requestScope[f:concat('dataFimExclusao',j)] || empty requestScope[f:concat('dataFimExclusao',j)]}">
											no dia <b>${requestScope[f:concat('dataInicio',j)]}</b>.
									</c:when>
									<c:otherwise>
									no período de <b>${requestScope[f:concat('dataInicioExclusao',j)]}</b> a <b>${requestScope[f:concat('dataFimExclusao',j)]}</b>.
								</c:otherwise>
								</c:choose>
							</c:if></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<c:if test="${informacoesAdicionais eq 'Sim'}">
				<table width="100%" border="1" cellpadding="2" cellspacing="1"
					bgcolor="#000000">
					<tr>
						<td colspan="2" bgcolor="#FFFFFF" align="center"><b>INFORMAÇÕES
						ADICIONAIS</b></td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="100%">${informacoes}</td>
					</tr>
				</table>
			</c:if>
		</mod:letra>
		<br />
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

		</body>

		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</html>
	</mod:documento>
</mod:modelo>
