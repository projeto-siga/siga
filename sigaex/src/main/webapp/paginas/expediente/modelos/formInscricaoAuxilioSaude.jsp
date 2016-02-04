<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<!-- este modelo trata de
FORMULARIO PARA INSCRICAO NO AUXILIO SAUDE-->

<mod:modelo>

	<mod:entrevista>
		</br>
		<mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Matrícula" var="matricula" reler="sim"
					buscarFechadas="true" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Ramal" var="ramal" largura="10" />
				<mod:texto titulo="Plano de saúde a qual está vinculado" var="plano"
					largura="30" />
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Data de vigência" var="mes"
					opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
			</mod:grupo>
			</br>
		</mod:grupo>


		<mod:selecao titulo="Total de dependentes" var="totalDePessoal"
			opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
		<mod:grupo titulo="Relação de Dependentes">


			<mod:grupo>
				<mod:selecao titulo="Todos os dependentes pertencem ao mesmo plano?"
					var="pertence" opcoes="Sim;Não" reler="sim" />
			</mod:grupo>
			<br />
			<c:forEach var="i" begin="1" end="${totalDePessoal}">
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome${i}" largura="59" />
					<mod:data titulo="Data de Nascimento" var="data_nasc${i}" />
				</mod:grupo>
				<mod:grupo>
				<mod:selecao titulo="Parentesco" var="parentesco${i}" 
					opcoes="Cônjuge;Filhos menores de 21 anos;Filhos entre 21 e 24 anos;Filho maior, inválido;Companheiro(a);Enteado(a) - Filho de Cônjuge, menor de 21 anos;Enteado(a) - Filho de Cônjuge, entre 21 e 24 anos;Enteado(a) - Filho de Cônjuge, maior inválido;Enteado(a) - Filho de Companheiro, menor de 21 anos;Enteado(a) - Filho de Companheiro, entre 21 e 24 anos;Enteado(a) - Filho de Companheiro, maior inválido;Menor sob guarda ou tutela" />
			</mod:grupo>
						<mod:grupo>	
					<mod:texto titulo="Estado civil" var="estado${i}" largura="19" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nº de identidade" var="identidade${i}"
						largura="15" />
					<mod:texto titulo="Órgão expedidor" var="orgao${i}" largura="15" />
					<mod:data titulo="Data de Expedição" var="data_expedicao${i}" />
				</mod:grupo>
				<mod:grupo>
					<c:if test="${pertence == 'Não'}">
						<mod:grupo>
							<mod:texto titulo="Plano de Saúde" var="planoDep${i}"
								largura="15" />
						</mod:grupo>
					</c:if>
				</mod:grupo>
				<br />


			</c:forEach>
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >INSCRI&Ccedil;&Atilde;O NO AUX&Iacute;LIO SA&Uacute;DE </p></td>
						</tr>
				<tr><td></td>
				</tr>
				<tr><td></td>
				</tr>
				<tr><td></td>
				</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->

		<br />

		<table width="100%" height="90px" border="1" cellpadding="2"
			cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" height="30px" align="center"><b>RELAÇÃO
				DE BENEFICIÁRIOS</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<c:forEach varStatus="indice" var="i" begin="1"
				end="${totalDePessoal}">
				<tr>
					<td width="65%" bgcolor="#FFFFFF" colspan="2" align="center"><b>Nome</b></td>
					<td width="15%" bgcolor="#FFFFFF" align="center"><b>Parentesco</b></td>
					<td width="20%" bgcolor="#FFFFFF" colspan="2" align="center"><b>Plano
					de Saúde</b></td>

				</tr>

				<tr>
					<td width="65%" bgcolor="#FFFFFF" colspan="2" align="center">${requestScope[f:concat('nome',i)]}</td>
					<td width="15%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('parentesco',i)]}</td>

					<c:if test="${pertence == 'Não'}">
						<td width="20%" bgcolor="#FFFFFF" colspan="2" align="center">${requestScope[f:concat('planoDep',i)]}</td>
					</c:if>
					<c:if test="${pertence == 'Sim'}">
						<td width="20%" bgcolor="#FFFFFF" colspan="2" align="center">${plano}</td>
					</c:if>
				</tr>


				<tr>
					<td width="30%" bgcolor="#FFFFFF" align="center"><b>Nº de
					identidade</b></td>
					<td width="20%" bgcolor="#FFFFFF" align="center"><b>Órgão
					expedidor</b></td>
					<td width="15%" bgcolor="#FFFFFF" align="center"><b>Data
					expedição</b></td>
					<td width="15%" bgcolor="#FFFFFF" align="center"><b>Data
					nascimento</b></td>
					<td width="20%" bgcolor="#FFFFFF" align="center"><b>Estado
					Civil</b></td>
				</tr>

				<tr>
					<td width="30%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('identidade',i)]}</td>
					<td width="20%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('orgao',i)]}</td>
					<td width="15%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('data_expedicao',i)]}</td>
					<td width="15%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('data_nasc',i)]}
					<c:set var="idade" value="${requestScope[f:concat('data_nasc',i)]}" scope="request" />
					<c:set var="parentesco" value="${requestScope[f:concat('parentesco',i)]}" scope="request" />
					<c:if test="${requestScope[f:idadeEmAnos(idade)] > 21 && parentesco == 'Filhos entre 21 e 24 anos'}">
					*
					</c:if>
					</td>
					<td width="20%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('estado',i)]}</td>
				</tr>
				
				<c:if test="${requestScope[f:idadeEmAnos(idade)] > 21 && parentesco == 'Filhos entre 21 e 24 anos'}">
					
				<tr>
				<td width="100%" colspan="5" bgcolor="#FFFFFF">
				* Declaro, a fim de fazer prova junto ao plano de saúde, que ${requestScope[f:concat('nome',i)]} permanece na condição de solteiro(a), estudante e vive sob minha dependência econômica.
				</td>
				</tr>
							</c:if>
			</c:forEach>
		</table>

		<p style="TEXT-INDENT: 2cm" align="justify"><br />Eu, <mod:identificacao
			pessoa="${requestScope['matricula_pessoaSel.id']}" negrito="sim"
			nivelHierarquicoMaximoDaLotacao="4" /> CPF:
		${f:pessoa(requestScope['matricula_pessoaSel.id']).cpfPessoa}, ramal
		${ramal}, vinculado ao plano de saúde ${plano}, declaro estar ciente
		dos termos da Resolução nº 587, que regulamenta à assistência à saúde
		prevista no art. 230 da Lei nº 8.112, de 1990, com a redação dada pela
		Lei nº 11.032, de 2006, e de que o respectivo reembolso será realizado
		em Folha de Pagamento, conforme apresentado abaixo: <br />
		<br />
		Data de Vigência: ${mes} <br />
		<br />
		&bull;O beneficiário receberá o valor integral da mensalidade do Plano
		de Saúde externo, quando este for igual ou inferior ao limite máximo
		de reembolso; <br /><br />

		&bull;Quando o valor da mensalidade cobrada exceder o limite máximo do
		reembolso, o beneficiário receberá o valor deste limite; <br /><br />
		&bull;Declaro, ainda, que o(s) beneficiário(s) acima relacionado(s)
		não recebe(m) auxílio semelhante e nem participa(m) de outro programa
		de assistência à saúde, custeado pelos cofres públicos, ainda que em
		parte.</p>

		<b>OBSERVAÇÃO<b />
		<p align="justify">&bull; A inscrição de dependentes somente
		poderá ser feita se o servidor for inscrito como titular no referido
		benefício e somente ele poderá efetivá-la.</p>

		<p align="justify"><b>Esclarecimentos quanto à documentação a
		ser encaminhada serão prestados após a regulamentaçãopelo TRF - 2ª
		Região.<b /></p>
		<br />
		<p align="center">${doc.dtExtenso}</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
	</mod:documento>
</mod:modelo>

