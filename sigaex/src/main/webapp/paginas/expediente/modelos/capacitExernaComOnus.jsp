<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
SOLICITAÇÃO DE INSCRIÇÃO EM AÇÃO DE CAPACITAÇÃO EXTERNA - COM ÔNUS -->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Solicitação">
			<mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Ação de capacitação" var="capacit_tit" />
				</mod:grupo>
				<mod:grupo>
					<mod:data titulo="Data de início" var="data_ini" />
					<mod:data titulo="Data de fim" var="data_fim" />
				</mod:grupo>
				<mod:texto titulo="Cidade/UF" var="cidade_uf" />
				<mod:selecao titulo="Prospecto anexo?" var="prospecto_anexo"
					opcoes="sim;não" />
				<mod:texto titulo="Valor" var="valor" />
				<mod:selecao titulo="Passagens?" var="passagens" opcoes="sim;não" />
				<mod:selecao titulo="Diárias?" var="diarias" opcoes="sim;não" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Instituição/Consultor TESTANDO"
					var="instituicao_consultor" />
				<mod:texto titulo="CNPJ ou CPF" var="cnpj_cpf" />
				<mod:texto titulo="NIT/PIS/PASEP (Pessoa física)"
					var="nit_pis_pasep" />
			</mod:grupo>
			<mod:grupo titulo="Indicação de Instituição / Consultor">
				<mod:selecao
					titulo="Número de indicações a serem preenchidas para esta ação de capacitação"
					var="indicacoes" opcoes="1;2;3;4;5" reler="sim" />
				<c:forEach var="i" begin="1" end="${indicacoes}">
					<mod:grupo>
						<mod:texto titulo="${i}) Ação de capacitação / Título"
							var="capacit_tit_indic${i}" />
					</mod:grupo>
					<mod:grupo>
						<mod:texto titulo="Data/Período da Capacitação"
							var="data_per_indic${i}" />
						<mod:texto titulo="Instituição/Consultor"
							var="institucao_consultor_indic${i}" />
						<mod:texto titulo="Valor" var="valor_indic${i}" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
			<mod:grupo titulo="Observações">
			<mod:memo colunas="80" linhas="5" 
				titulo="Justifique a indicação que melhor 
						atenderia às necessidades de capacitação" 
				var="obs" />

			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Solicitação dentro do prazo?" var="prazo_ok"
					opcoes="sim;não" reler="sim"/>
					
				<c:if test="${prazo_ok == 'não'}">	
					<mod:memo colunas="80" linhas="5" 
						titulo="Justifique"
						var="justificativa_prazo" />
				</c:if>
					
					
				<mod:grupo>
				<mod:memo colunas="80" linhas="5" 
						titulo="Qual a importância que a ação de 
						capacitação terá para o desenvolvimento das 
						atividades da unidade organizacional?
						(DEFINA, DE FORMA QUE OS OBJETIVOS 
						A SEREM ATINGIDOS POSSAM SER MENSURADOS OU OBSERVADOS)"
						var="importancia" />
				</mod:grupo>
			</mod:grupo>


			<mod:grupo
				titulo="Atividades/tarefas da unidade que serão afetadas pela capacitação">
				<mod:selecao titulo="Quantas atividades/tarefas deseja elencar?"
					var="ativ_tarefa" opcoes="1;2;3;4;5" reler="sim" />
				<c:forEach var="i" begin="1" end="${ativ_tarefa}">
					<mod:grupo>
						<mod:texto
							titulo="${i}) Atividade/tarefa que será afetada pelo treinamento"
							var="ativ_tarefa${i}" />
					</mod:grupo>
					<mod:grupo>
						<mod:memo colunas="80" linhas="5"
						    titulo="Como é desenvolvida atualmente?"
							var="ativ_hoje${i}" />
					</mod:grupo>
					<mod:grupo>
						<mod:memo colunas="80" linhas="5"
						    titulo="Como prevê que ficará após a capacitação?"
							var="ativ_pos_capacit${i}" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
			<mod:grupo titulo="Servidores Indicados">
				<mod:selecao
					titulo="Quantos servidores serão indicados para esta ação de capacitação?"
					var="servidores_indic" opcoes="1;2;3;4;5" reler="sim" />
				<c:forEach var="i" begin="1" end="${servidores_indic}">
					<mod:grupo>
						<mod:texto titulo="${i}) Nome/cargo do servidor/função/seção"
							var="serv_indic${i}" />
					</mod:grupo>
					<mod:grupo>
						<mod:memo colunas="80" linhas="5"
						    titulo="Critérios usados para a indicação?"
							var="criterios${i}" />
					</mod:grupo>
				</c:forEach>
				<mod:grupo>
					<mod:memo colunas="80" linhas="5"
					    titulo="Justifique o número de servidores indicados"
						var="justif_n_serv" />
				</mod:grupo>
				<mod:grupo>
					<mod:data titulo="Data:" var="data_form" />
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head></head>
		<body>

		<h1>SOLICITAÇÃO DE INSCRIÇÃO EM AÇÃO DE CAPACITAÇÃO EXTERNA - COM ÔNUS
		</h1>

		<h3>1- SOLICITAÇÃO - ENCAMINHAR COM ANTECEDÊNCIA MÍNIMA DE 20 (VINTE)
		DIAS</h3>

		<p>&nbsp;</p>
		<table width="100%" border="1" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="4">AÇÃO DE CAPACITAÇÃO (TÍTULO): ${capacit_tit}</td>
			</tr>
			<tr>
				<td colspan="2">DATA / PER&Iacute;ODO: ${data_ini} a ${data_fim}</td>
				<td colspan="2">Cidade/UF: ${cidade_uf}</td>
			</tr>
			<tr>
				<td width="319">PROSPECTO ANEXO: ${prospecto_anexo} <br>
				<br>
				<br>
				<br>
				</td>
				<td width="60">VALOR: ${valor}</td>
				<td width="244">PASSAGEM: ${passagens}</td>
				<td width="268">DI&Aacute;RIAS: ${diarias}</td>
			</tr>
			<tr>
				<td colspan="2">INSTITUI&Ccedil;&Atilde;O/CONSULTOR:
				${institucao_consultor}</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">CNPJ ou CPF : ${cnpj_cpf}</td>
				<td colspan="2">PESSOA F&Iacute;SICA: NIT OU PIS/PASEP
				${nit_pis_pasep}</td>
			</tr>
		</table>
		<h3>2- INDICA&Ccedil;&Atilde;O DE INSTITUI&Ccedil;&Atilde;O/CONSULTOR</h3>
		<table width="85%" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td width="29%">
				<div align="center">A&Ccedil;&Atilde;O DE CAPACITA&Ccedil;&Atilde;O
				(T&Iacute;TULO)</div>
				</td>
				<td width="31%">
				<div align="center">DATA/PER&Iacute;ODO DA CAPACITA&Ccedil;&Atilde;O</div>
				</td>
				<td width="25%">
				<div align="center">INSTITUI&Ccedil;&Atilde;O/CONSULTOR</div>
				</td>
				<td width="15%">
				<div align="center">VALOR</div>
				</td>
			</tr>
			<c:forEach var="i" begin="1" end="${indicacoes}">
				<tr>
					<td width="29%">${requestScope[f:concat('capacit_tit_indic',i)]}</td>
					<td width="31%">${requestScope[f:concat('data_per_indic',i)]}</td>
					<td width="25%">${requestScope[f:concat('institucao_consultor_indic',i)]}</td>
					<td width="15%">${requestScope[f:concat('valor_indic',i)]}</td>
				</tr>
			</c:forEach>
		</table>


		<p><b>Observa&ccedil;&atilde;o:</b> Justifique a
		indica&ccedil;&atilde;o que melhor atenderia &agrave;s necessidades de
		capacita&ccedil;&atilde;o: ${obs}</p>
		<p>Solicita&ccedil;&atilde;o dentro do prazo? ${prazo_ok}</p>
		<p>Caso negativo, justifique: ${justificativa_prazo}</p>
		<p>Qual a import&acirc;ncia que a a&ccedil;&atilde;o de
		capacita&ccedil;&atilde;o ter&aacute; para o desenvolvimento das
		atividades da unidade organizacional?: ${importancia}</p>
		<p>&nbsp;</p>
		<h3>3- SOBRE A CAPACITA&Ccedil;&Atilde;O</h3>
		<p>Atividades/tarefas da unidade que ser&atilde;o afetadas pela
		capacita&ccedil;&atilde;o</p>
		<table width="85%" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td width="29%">
				<div align="center">ATIVIDADE/TAREFA QUE SER&Aacute; AFETADA PELO
				TREINAMENTO</div>
				</td>
				<td width="31%">
				<div align="center">COMO &Eacute; DESENVOLVIDA ATUALMENTE?</div>
				</td>
				<td width="25%">
				<div align="center">COMO VOC&Ecirc; PREV&Ecirc; QUE FICAR&Aacute;
				AP&Oacute;S A CAPACITA&Ccedil;&Atilde;O?</div>
				</td>
			</tr>
			<c:forEach var="i" begin="1" end="${ativ_tarefa}">
				<tr>
					<td width="29%">${requestScope[f:concat('ativ_tarefa',i)]}</td>
					<td width="31%">${requestScope[f:concat('ativ_hoje',i)]}</td>
					<td width="25%">${requestScope[f:concat('ativ_pos_capacit',i)]}</td>
				</tr>
			</c:forEach>
		</table>
		<h3>4- SERVIDORES INDICADOS</h3>
		<table width="85%" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td width="29%">
				<div align="center">NOME/CARGO DO
				SERVIDOR/FUN&Ccedil;&Atilde;O/SE&Ccedil;&Atilde;O</div>
				</td>
				<td width="31%">
				<div align="center">CRIT&Eacute;RIO(S) UTILIZADO(S) PARA A
				INDICA&Ccedil;&Atilde;O</div>
				</td>
			</tr>
			<c:forEach var="i" begin="1" end="${servidores_indic}">
				<tr>
					<td width="29%">${requestScope[f:concat('serv_indic',i)]}</td>
					<td width="31%">${requestScope[f:concat('criterios',i)]}</td>
				</tr>
			</c:forEach>
		</table>
		<p><b>Observa&ccedil;&atilde;o:</b> Justifique o n&uacute;mero de
		servidores indicados: ${justif_n_serv}</p>
		<table width="100%" border="1" cellspacing="0" cellpadding="0">
			<tr>
				<td width="23%">Data: ${data_form}</td>
				<td width="77%">
				<div align="center">RESPONSABILIZO-ME PELAS
				INFORMA&Ccedil;&Otilde;ES PRESTADAS E PELA
				MULTIPLICA&Ccedil;&Atilde;O DO CONHECIMENTO ADQUIRIDO PELOS
				INDICADOS, ASSIM COMO PELO ATESTO AO FINAL DA A&Ccedil;&Atilde;O DE
				CAPACITA&Ccedil;&Atilde;O.</div>
				</td>
			</tr>
			<tr>
				<td width="23%">&nbsp;</td>
				<td width="77%">
				<div align="center">_________________________________________________</div>
				</td>
			</tr>
			<tr>
				<td width="23%">&nbsp;</td>
				<td width="77%">
				<div align="center"><font size="1">ASSINATURA E CARIMBO DO DIRETOR</font></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				<div align="center"><b>NO CASO DE &Ocirc;NUS APENAS COM
				DI&Aacute;RIAS E/OU PASSAGENS, ESTE FORMUL&Aacute;RIO DEVER&Aacute;
				SER ENCAMINHADO DIRETAMENTE &Agrave; SECRETARIA GERAL COM A PROPOSTA
				DE CONCESS&Atilde;O DE DI&Aacute;RIAS.<br>
				CASO ENVOLVA CONTRATA&Ccedil;&Atilde;O, DEVER&Aacute; SER
				ENCAMINHADO, PRIMEIRAMENTE, &Agrave; SE&Ccedil;&Atilde;O DE
				TREINAMENTO.</b><br>
				</div>
				</td>
			</tr>
		</table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>

		<p>&nbsp;</p>
		</body>
		</html>
	</mod:documento>
</mod:modelo>
