<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>	
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp">
	<mod:entrevista>
		<mod:grupo titulo="Dados do Servidor">
			<mod:pessoa titulo="Servidor" var="servidor" />
		</mod:grupo>
		
		<mod:grupo titulo="Cancelamentos">
			<mod:selecao titulo="Número de cancelamentos" var="cancelamentos"
				opcoes="0;1;2;3" reler="sim" />
			<c:forEach var="i" begin="1" end="${cancelamentos}">
				<mod:grupo largura="25">
					<mod:selecao titulo="${i}) Período Aquisitivo"
						var="periodoAquisitivoCancelamento${i}" reler="ajax" idAjax="periodoAquisitivoCancelamentoAjax${i}"
						opcoes="Selecione;2008/2009;2009/2010;2010/2011;2011/2012;2012/2013" />
				</mod:grupo>
				<mod:grupo largura="75">
					<mod:grupo depende="periodoAquisitivoCancelamentoAjax${i}">
						<c:if test="${requestScope[f:concat('periodoAquisitivoCancelamento',i)] != 'Selecione'}">				
					<mod:selecao titulo="Sequencial" var="sequencialCancelamento${i}"
						opcoes="1;2;3" />
							<mod:data titulo="Período de" var="dataInicialCancelamento${i}" />
							<mod:data titulo="a" var="dataFinalCancelamento${i}" />
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
		
		<mod:grupo titulo="Interrupções por Necessidade de Serviço">
			<mod:selecao titulo="Número de interrupções" var="interrupcoes"
				opcoes="0;1" reler="sim" />
			<c:forEach var="i" begin="1" end="${interrupcoes}">
				<mod:grupo largura="25">
					<mod:selecao titulo="${i}) Período Aquisitivo"
						var="periodoAquisitivoInterrupcao${i}" reler="ajax" idAjax="periodoAquisitivoInterrupcaoAjax${i}"
						opcoes="Selecione;2008/2009;2009/2010;2010/2011;2011/2012;2012/2013" />
				</mod:grupo>
				<mod:grupo largura="75">
					<mod:grupo depende="periodoAquisitivoInterrupcaoAjax${i}">
						<c:if test="${requestScope[f:concat('periodoAquisitivoInterrupcao',i)] != 'Selecione'}">
								<mod:selecao titulo="Sequencial" var="sequencialInterrupcao${i}"
								opcoes="1;2;3" />
							<mod:data titulo="Período de" var="dataInicialInterrupcao${i}" />
							<mod:data titulo="a" var="dataFinalInterrupcao${i}" />
								<mod:grupo>
									<mod:data titulo="Interromper a partir de"
										var="dataInterrupcao${i}" />
								</mod:grupo>
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
				<mod:grupo titulo="Alterações">
			<mod:selecao titulo="Número de alterações" var="alteracoes"
				opcoes="0;1;2;3" reler="sim" />
			<c:forEach var="i" begin="1" end="${alteracoes}">
					<mod:grupo largura="25">
						<mod:selecao titulo="${i}) Período Aquisitivo"
							var="periodoAquisitivoAlteracao${i}" reler="ajax" idAjax="periodoAquisitivoAlteracaoAjax${i}"
							opcoes="Selecione;2008/2009;2009/2010;2010/2011;2011/2012;2012/2013" />
					</mod:grupo>
					<mod:grupo largura="75">
							<mod:grupo depende="periodoAquisitivoAlteracaoAjax${i}">
								<c:if test="${requestScope[f:concat('periodoAquisitivoAlteracao',i)] != 'Selecione'}">
									<mod:selecao titulo="Sequencial" var="sequencialAlteracao${i}"
										opcoes="1;2;3" reler="sim" />
									<mod:data titulo="Período de" var="dataInicialAlteracao${i}" />
									<mod:data titulo="a" var="dataFinalAlteracao${i}" />
										<mod:grupo>
											<mod:data titulo="Alterar para de"
												var="dataInicialNovaAlteracao${i}" />
											<mod:data titulo="a" var="dataFinalNovaAlteracao${i}" />
										</mod:grupo>								
								</c:if>
								<c:if test="${requestScope[f:concat('sequencialAlteracao',i)] == 1}">
									<mod:grupo>
										<mod:selecao
											titulo="Deseja o adiantamento da remuneração de férias?"
											var="adiantamentoAlteracao${i}" opcoes="Sim;Não" />
									</mod:grupo>
								</c:if>
							</mod:grupo>
					</mod:grupo>
			</c:forEach>
		</mod:grupo>
			<mod:grupo titulo="Marcações">
			<mod:selecao titulo="Número de marcações" var="marcacoes"
				opcoes="0;1;2;3" reler="sim" />
			<c:forEach var="i" begin="1" end="${marcacoes}">
						<mod:grupo largura="25">
					<mod:selecao titulo="${i}) Período Aquisitivo"
						var="periodoAquisitivoMarcacao${i}" reler="ajax" idAjax="periodoAquisitivoMarcacaoAjax${i}"
						opcoes="Selecione;2008/2009;2009/2010;2010/2011;2011/2012;2012/2013" />
				</mod:grupo>
					<mod:grupo largura="75">
						<mod:grupo depende="periodoAquisitivoMarcacaoAjax${i}">
							<c:if test="${requestScope[f:concat('periodoAquisitivoMarcacao',i)] != 'Selecione'}">
								<mod:selecao titulo="Sequencial" var="sequencialMarcacao${i}"
									opcoes="1;2;3" reler="sim" />
								<mod:data titulo="Período de" var="dataInicialMarcacao${i}" />
								<mod:data titulo="a" var="dataFinalMarcacao${i}" />
							</c:if>
							<c:if test="${requestScope[f:concat('sequencialMarcacao',i)] == 1}">
									<mod:grupo>
										<mod:selecao
											titulo="Deseja o adiantamento da remuneração de férias?"
											var="adiantamentoMarcacao${i}" opcoes="Sim;Não" />
									</mod:grupo>
							</c:if>
						</mod:grupo>
				</mod:grupo>
				</c:forEach>
		</mod:grupo>
		<mod:grupo titulo="Demais Informações">
			<mod:grupo>
				<mod:selecao titulo="Deseja manter os demais períodos já marcados?"
					var="conjuge_servidor" opcoes="Sim;Não" />
			</mod:grupo>
			<c:if test="${marcacoes+alteracoes >0}">
				<mod:grupo>
					<mod:selecao titulo="Trata-se de fruição após o período normal?"
						var="foraPeriodoNormal"
						opcoes="Não;Sim, por necessidade de serviço" />
				</mod:grupo>
			</c:if>
			<mod:grupo>
				<mod:selecao titulo="Qual o motivo desta solicitação?" var="motivo"
					opcoes="Necessidade de serviço;Outros" reler="sim" />
			</mod:grupo>
			<c:if test="${motivo == 'Outros'}">
				<mod:grupo>
					<mod:texto titulo="Motivo" var="motivoDesc" largura="60" />  
				</mod:grupo>
			</c:if>
		</mod:grupo>
		<mod:grupo>
			<b> <mod:mensagem titulo="Atenção"
				texto="preencha o destinatário com SECAD e, após finalizar, transfira para a SECAD." />
			</b>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
	

	
	<mod:valor var="texto_memorando">
		<p style="align:justify;TEXT-INDENT:2cm">Solicito que seja(m) efetuada(s) a(s) seguinte(s)
		alteração(ões) no cadastro de férias do servidor(a) <b>${requestScope['servidor_pessoaSel.descricao']}</b>,
		matrícula <b>${requestScope['servidor_pessoaSel.sigla']}</b>, respeitando a
		data de fechamento da folha de pagamento para percepção das
		vantagens pecuniárias próprias.<br/>&nbsp;</p>

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="20%">Tipo</td>
				<td bgcolor="#FFFFFF" width="15%">Período Aquisitivo</td>
				<td bgcolor="#FFFFFF" width="15%">Sequencial</td>
				<td bgcolor="#FFFFFF" width="50%">Detalhes</td>
			</tr>
			<c:forEach var="i" begin="1" end="${cancelamentos}">
				<tr>
					<td bgcolor="#FFFFFF">Cancelamento</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('periodoAquisitivoCancelamento',i)]}</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('sequencialCancelamento',i)]}</td>
					<td bgcolor="#FFFFFF">Período:
					${requestScope[f:concat('dataInicialCancelamento',i)]} a
					${requestScope[f:concat('dataFinalCancelamento',i)]}</td>
				</tr>
			</c:forEach>
			<c:forEach var="i" begin="1" end="${interrupcoes}">
				<tr>
					<td bgcolor="#FFFFFF">Interrupção</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('periodoAquisitivoInterrupcao',i)]}</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('sequencialInterrupcao',i)]}</td>
					<td bgcolor="#FFFFFF">Período:
					${requestScope[f:concat('dataInicialInterrupcao',i)]} a
					${requestScope[f:concat('dataFinalInterrupcao',i)]}<br />
					Interromper a partir de:
					${requestScope[f:concat('dataInterrupcao',i)]}</td>
				</tr>
			</c:forEach>
			<c:forEach var="i" begin="1" end="${alteracoes}">
				<tr>
					<td bgcolor="#FFFFFF">Alteração</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('periodoAquisitivoAlteracao',i)]}</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('sequencialAlteracao',i)]}</td>
					<td bgcolor="#FFFFFF">Período:
					${requestScope[f:concat('dataInicialAlteracao',i)]} a
					${requestScope[f:concat('dataFinalAlteracao',i)]}<br />
					Alterar para:
					${requestScope[f:concat('dataInicialNovaAlteracao',i)]} a
					${requestScope[f:concat('dataFinalNovaAlteracao',i)]} <c:if
						test="${requestScope[f:concat('sequencialAlteracao',i)] == 1}">
						<br />Adiantamento: ${requestScope[f:concat('adiantamentoAlteracao',i)]}
					</c:if></td>
				</tr>
			</c:forEach>
			<c:forEach var="i" begin="1" end="${marcacoes}">
				<tr>
					<td bgcolor="#FFFFFF">Marcação</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('periodoAquisitivoMarcacao',i)]}</td>
					<td bgcolor="#FFFFFF">${requestScope[f:concat('sequencialMarcacao',i)]}</td>
					<td bgcolor="#FFFFFF">Período:
					${requestScope[f:concat('dataInicialMarcacao',i)]} a
					${requestScope[f:concat('dataFinalMarcacao',i)]}<c:if
						test="${requestScope[f:concat('sequencialMarcacao',i)] == 1}">
						<br />Adiantamento: ${requestScope[f:concat('adiantamentoMarcacao',i)]}
					</c:if></td>
				</tr>
			</c:forEach>
		</table>
		
		<p><br/>Deseja manter os demais períodos já marcados? <b>${conjuge_servidor}</b></p>
		<c:if test="${marcacoes+alteracoes >0}">
			<p>Trata-se de fruição após o período normal? <b>${foraPeriodoNormal}</b></p>
		</c:if>
		<p>Qual o motivo desta solicitação? <b>${motivo}</b></p>
		<c:if test="${motivo == 'Outros'}">
			<p>Motivo: <b>${motivoDesc}</b></p>
		</c:if>
	</mod:valor>		
	</mod:documento>
</mod:modelo>
