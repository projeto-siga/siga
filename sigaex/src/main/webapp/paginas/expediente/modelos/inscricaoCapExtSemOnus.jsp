<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- Modelo de solicitação de inscrição para capacitação sem onus -->

<mod:modelo>
	<mod:entrevista>
		 <mod:grupo titulo="Todos os campos são de preenchimento obrigatório">
			<mod:texto titulo="Ação de capacitação" largura="40" var="acao" />
			<mod:texto titulo="Instituição/Consultor" largura="40" var="ic" />
			<mod:grupo titulo="Período Solicitado">
				<mod:data titulo="De" var="dataInicio" />
				<mod:data titulo="a" var="dataFim" />
				<mod:texto titulo="Cidade/Uf" largura="40" var="cidade" />
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Prospecto anexo" var="prospecto"
					opcoes="sim;não" />
			</mod:grupo>
			<mod:grupo>
				<mod:memo colunas="55" linhas="2"
					titulo="Qual a importância que a ação de capacitação terá para o 
					desenvolvimento das atividades da unidade organizacional?
					(Defina de forma que os objetivos atingidos possam ser mensurados ou observados)"
					var="imp" />
			</mod:grupo>
			<mod:grupo
				titulo="IDENTIFIQUE AS ATIVIDADES/TAREFAS DA UNIDADE QUE SERÃO AFETADAS PELA CAPACITAÇÃO">
				<mod:selecao titulo="Quantidade de tarefas" var="qtd_tar"
					opcoes="1;2;3;4;5;6;7;8;9;10" reler="ajax" idAjax="qtd_tarefaAjax" />
				<mod:grupo depende="qtd_tarefaAjax">
				<c:forEach var="i" begin="1" end="${qtd_tar}">
					<mod:grupo titulo="Atividade ${i}">
						<mod:grupo>
							<mod:texto largura="30"
								titulo="Atividade/tarefa que sera afetada pelo treinamento"
								var="atividadeTarefa${i}" />
						</mod:grupo>
						<mod:grupo>
							<mod:memo  colunas="25" linhas="3" titulo="Como é desenvolvida atualmente"
								var="des${i}" />
						</mod:grupo>
						<mod:grupo>
							<mod:memo colunas="25" linhas="3"
								titulo="Como voce prevê que ficará após a capacitação"
								var="capact${i}" />
						</mod:grupo>
					</mod:grupo>
				</c:forEach>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>

	<mod:grupo titulo="Servidor(es)">
	

		<mod:mensagem
			texto="SERVIDOR(ES) INDICADO(S), CADA SERVIDOR INDICADO ESTÁ DEVIDAMENTE CIENTE DO INTEIRO TEOR DA PORTARIA Nº 042-GDF,DE 10.9.2002, ASSUMINDO OS COMPROMISSOS DETERMINADOS NESSE ATO NORMATIVO,
	       	 		SOBRE TUDO NO QUE SE REFERE À MULTIPLICAÇÃO DO CONHECIMENTO ADQUIRIDO."/>
		
		
			 <mod:selecao titulo="Mais que 10 servidores a indicar" var="quantos"
				opcoes="não;sim" reler="ajax" idAjax="quantosAjax"/>
			<mod:grupo depende="quantosAjax">
					<c:choose>
						<c:when test="${quantos=='sim'}">
							<mod:mensagem texto="A lista de servidores deverá ser anexada ao documento" vermelho="sim"/>
						</c:when> 
						<c:otherwise>
							<mod:selecao titulo="Quantos servidores deseja indicar" 
								var="serv_indic" reler="ajax" idAjax="serv_indicAjax" opcoes="1;2;3;4;5;6;7;8;9;10" />
						</c:otherwise>
					</c:choose>
		
				<mod:grupo depende="serv_indicAjax"> 	
					<c:forEach var="i" begin="1" end="${serv_indic}">
						<mod:grupo titulo="Servidor ${i}">			
								<mod:pessoa titulo="Nome" var="nome${i}" />						
						</mod:grupo>
					</c:forEach>
				</mod:grupo>
		</mod:grupo>
	</mod:grupo>
	
	
	<mod:grupo>				
		<mod:memo colunas="25" linhas="2" titulo="Critério(s) Utilizado(s) para indicação" var="criterios" />
	</mod:grupo>
		<mod:grupo>
			<mod:memo colunas="25" linhas="2"
				titulo="Observação (justifique o numero de servidores indicados)"
				var="obs_serv" />

	</mod:grupo>
	<mod:grupo>
			<mod:data titulo="Data" var="data" />
	</mod:grupo>	
		<!-- 
		<mod:grupo titulo="SEÇÃO DE TREINAMENTO-SETRE">
			<mod:selecao
				titulo="Conteúdo programático (aprovado pela area competente)"
				var="aprovado" opcoes="sim;não" />
		</mod:grupo>
		<mod:grupo>
			<mod:selecao
				titulo="Solicitação constante do levantamento de necessidades de treinamento"
				var="solicit" opcoes="sim;não" />
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Reserva de vagas(s)" var="reserv"
				opcoes="sim;não" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Contato(Nome, telefone, E-mail)" largura="40"
				var="contato" />
		</mod:grupo>
		<mod:grupo>
			<mod:memo colunas="25" linhas="2" titulo="Observação" var="obs" />

		</mod:grupo>
		
	-->	
	</mod:entrevista>
	<mod:documento>



		<c:import
			url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />

		<table>
			<tr>
				<td>SOLICITAÇÃO DE INSCRIÇÃO EM AÇÃO DE CAPACITAÇÃO EXTERNA-SEM
				ONUS</td>
			</tr>
		</table>
		<table>
			<tr>
				<td>Nº/Ano:</td>
				<td>2001-04-SRH</td>
			</tr>
		</table>

		<br />
		<br />

		<table width="100%">
			<tr>
				<td colspan="2"><font size="2">Ação de Capacitação: <b>${acao}</b></font>
				</td>
			</tr>
			<tr>
				<td colspan="2"><font size="2">Instituição/Consultor: <b>${ic}</b></font>
				</td>
			</tr>
			<tr>
				<td width="70%"><font size="2">Período Solicitado:<br>
				De: <b>${dataInicio}</b> a: <b>${dataFim}</b></font></td>
				<td width="30%"><font size="2">Cidade/UF: <b>${cidade}</b></font>
				</td>
			</tr>
			<tr>
				<td colspan="2"><font size="2">Prospecto: <b>${prospecto}</b></font>
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td>
				<p><font size="2">Importância que a ação de capacitação
				terá para o desenvolvimento das atividades da unidade
				organizacional:&nbsp; <b>${imp}</b></font></p>
				</td>
			</tr>
		</table>
		<br>
		<table width="100%">
			<tr>
				<td>ATIVIDADES/TAREFAS DA UNIDADE QUE SERÃO AFETADAS PELA
				CAPACITAÇÃO</td>
			</tr>
		</table>
		<table width="100%">
			<tr>

				<td>Atividade/tarefa que será afetada pelo treinamento:</td>
				<td>Como será desenvolvida atualmente?</td>
				<td>Como você prevê que ficará após a capacitação?
				</td>
			</tr>
		</table>

		<table width="100%">

			<c:forEach var="i" begin="1" end="${qtd_tar}">
				<tr>


					<td><font size="2"><b>${requestScope[f:concat('atividadeTarefa',i)]}</b></font></td>
					<td><font size="2"><b>${requestScope[f:concat('des',i)]}</b></font></td>
					<td><font size="2"><b>${requestScope[f:concat('capact',i)]}</b></font></td>
					

				</tr>

			</c:forEach>

		</table>
		<br>
		<table width="100%">
			<tr>
				<td><font size="3">SERVIDOR(ES) INDICADO(S)</font></td>
			</tr>
			<tr>
				<td><font size="3">Cada servidor indicado está
				devidamente ciente do inteiro teor da portaria Nº 042-GDF,DE
				10.9.2002, assumindo os compromissos determinados nesse ato
				normativo, sobre tudo no que se refere à multiplicação do
				conhecimento adquirido.</font></td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td><font size="2">Nome</font></td>
				<td><font size="2">Função</font></td>
			</tr>
		</table>

		<table width="100%">
			<c:forEach var="i" begin="1" end="${serv_indic}">
			
			
				<tr>					
					<td><font size="2"><b>${requestScope[f:concat(f:concat('nome',i),'_pessoaSel.descricao')]}</b></font></td>
					<td><font size="2"><b> <c:set var="cargo" value="${f:pessoa(requestScope[f:concat(f:concat('nome',i),'_pessoaSel.id')]).funcaoConfianca.descricao}"/>${cargo}</b></font></td>
				</tr>
			</c:forEach>
		</table>
		
		<table width="100%">
			<tr>
				<td colspan="2"><font size="2">Observação(justificação
				do numero de servidores indicados):<br>
				<b>${obs_serv}</b></font> </td>
			</tr>
			<tr>
				<td colspan="2"><font size="2">Critérios utilizados para as indicações<br>
				<b>${criterios}</b></font> </td>
			</tr>
			<tr>
				<td width="30%"><font size="2">Data: <b>${data}</b></font></td>
				<td width="70%"><font size="2">Responsabilizo-me pelas
				informações prestadas e pela participação dos indicados, assim como
				pelo cumprimento do estabelecido na portaria N.042/02-GDF</font>
				
				<p align="center">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
				_________________________________________<br>
				</p>
				<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				Assinatura e carimbo do diretor.</font> <br>
				</td>
			</tr>
		</table>
		<br>
		<!--  
		<table width="100%">
			<tr>
				<td>SEÇÃO DE TREINAMENTO - SETRE</td>
			</tr>
			<tr>
				<td><font size="2">Solicitação constante do levantamento
				de necessidades de treinamento: <b>${solicit}</b></font></td>
			</tr>
			<tr>
				<td><font size="2">Reserva de vagas: <b>${reserv}</b></font></td>
			</tr>
			<tr>
				<td><font size="2">Contato: <b>${contato}</b></font></td>
			</tr>
			<tr>
				<td><font size="2">Conteúdo programático(aprovado): <b>${aprovado}</b></font>
				</td>
			</tr>
			<tr>
				<td><font size="2">Observação: <b>${obs}</b></font></td>
			</tr>
		</table>
		-->
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />		
	</mod:documento>
</mod:modelo>
