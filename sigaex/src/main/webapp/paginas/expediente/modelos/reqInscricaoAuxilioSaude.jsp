<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<!-- este modelo trata de
REQUERIMENTO PARA INSCRICAO NO AUXILIO SAUDE-->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="seben" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		</br>
		<mod:selecao titulo="Tipos de Inclusão" var="tipoDeInclusao"
			opcoes="Exclusivamente de Titular;De Titular e de seus Dependentes Diretos;Exclusivamente de Dependentes Diretos"
			reler="sim" />
		<c:if
			test="${tipoDeInclusao == 'De Titular e de seus Dependentes Diretos' || tipoDeInclusao == 'Exclusivamente de Dependentes Diretos'}">
			<span><b>(A inscrição de dependentes somente poderá ser
			feita se o servidor for inscrito como titular no referido benefício e
			somente ele poderá efetivá-la)</b></span>
		</c:if>
		<%--<mod:grupo titulo="Tipos de Inclusão">
			<mod:radio var="tipoDeInclusao" valor="1" reler="sim" marcado="sim" titulo="Exclusivamente de Titular"/>
			<mod:radio var="tipoDeInclusao" valor="2" reler="sim" titulo="De Titular e de seus Dependentes Diretos (A inscrição de dependentes somente poderá ser feita se o servidor for inscrito como titular no referido benefício e somente ele poderá efetivá-la)"/>
			<mod:radio var="tipoDeInclusao" valor="3" reler="sim" titulo="Exclusivamente de Dependentes Diretos (A inscrição de dependentes somente poderá ser feita se o servidor for inscrito como titular no referido benefício e somente ele poderá efetivá-la)"/>
		</mod:grupo>--%>
		<c:if
			test="${tipoDeInclusao == 'Exclusivamente de Titular' || tipoDeInclusao == 'De Titular e de seus Dependentes Diretos'}">
			<br />
			<mod:grupo titulo="Dados do Titular">
				<mod:grupo>
					<mod:pessoa titulo="Matrícula" var="matriculaTitular" reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<mod:data titulo="Data do exercício" var="dataExercicio" />
					<mod:data titulo="Data de nascimento" var="dataNascimentoTitular" />
					<mod:selecao titulo="Sexo" opcoes="M;F" var="sexoTitular" />
					<mod:selecao titulo="Estado Civil" var="estadoCivilTitular"
						opcoes="Solteiro(a);Casado(a);Divorciado(a);Viúva(o);Desquitado"
						reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Plano de saúde a qual está vinculado"
						var="plano" largura="30" />
					<mod:selecao
						titulo="Todos os dependentes pertencem ao mesmo plano?"
						var="pertence" opcoes="Sim;Não" reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<mod:selecao titulo="Data de vigência" var="mesVigencia"
						opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
					<mod:texto titulo="Ano" var="anoVigencia" largura="6"
						maxcaracteres="4" reler="ajax" idAjax="anoAjax" />
					<mod:grupo depende="anoAjax">
						<c:if test="${empty anoVigencia}">
							<mod:mensagem titulo="Alerta"
								texto="o ano de Vigência deve ser preenchido." vermelho="sim" />
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>
		</c:if>
		<c:if
			test="${tipoDeInclusao == 'De Titular e de seus Dependentes Diretos' || tipoDeInclusao == 'Exclusivamente de Dependentes Diretos'}">
			<br />
			<mod:grupo titulo="Dados Dos Dependentes Diretos">
				<mod:selecao titulo="Número total a incluir no Auxílilo-Saúde"
					var="totalDeDependentes" opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
				<c:forEach var="i" begin="1" end="${totalDeDependentes}">
					<mod:grupo>
						<mod:selecao titulo="Parentesco" var="parentesco${i}"
							opcoes="Cônjuge;Filhos Menores De 21 Anos;Filhos Entre 21 e 24 Anos;Filho Maior, Inválido;Companheiro(A);Enteado(A) - Filho De Cônjuge, Menor De 21 Anos;Enteado(A) - Filho De Cônjuge, Entre 21 e 24 Anos;Enteado(A) - Filho de cônjuge, maior inválido;Enteado(A) - Filho de Companheiro, menor de 21 anos;Enteado(A) - Filho de Companheiro, entre 21 e 24 anos;Enteado(A) - Filho de Companheiro, maior inválido;Menor Sob Guarda Ou Tutela"
							reler="sim" />
					</mod:grupo>
					<mod:grupo>
						<mod:texto titulo="Nome" var="nomeDependente${i}" largura="59" />
						<mod:selecao titulo="Sexo" opcoes="M;F" var="sexoDependente${i}" />
						<mod:data titulo="Data de Nascimento"
							var="dataNascimentoDependente${i}" />
					</mod:grupo>
					<mod:grupo>
						<c:if test="${pertence == 'Não'}">
							<mod:grupo>
								<mod:texto titulo="Plano de Saúde" var="planoDep${i}"
									largura="30" />
							</mod:grupo>
						</c:if>
					</mod:grupo>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Filhos Menores De 21 Anos'}">
						<mod:grupo>
							<mod:selecao titulo="Dependente econômico"
								var="dependenteEconomico${i}" opcoes="Sim;Não" />
							<mod:selecao titulo="Solteiro" var="solteiro${i}"
								opcoes="Sim;Não" />
							<mod:selecao titulo="Estudante" var="estudante${i}"
								opcoes="Sim;Não" />
							<mod:selecao titulo="Reside em sua companhia"
								var="resideCompanhia${i}" opcoes="Sim;Não" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Filhos Entre 21 e 24 Anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Filho Maior, Inválido'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio titulo="Não" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Companheiro(A)'}">
						<mod:selecao titulo="Estado Civil" var="estadoCivilDep${i}"
							opcoes="Solteiro(a);Casado(a);Divorciado(a);Viúva(o);Desquitado"
							reler="sim" />
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho De Cônjuge, Menor De 21 Anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho De Cônjuge, Entre 21 e 24 Anos'}">
						<mod:texto titulo="Nº do Cartão do Auxílio-Saúde"
							var="numeroCartaoAuxSau${i}" largura="59" />
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de cônjuge, maior inválido'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de Companheiro, menor de 21 anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de Companheiro, entre 21 e 24 anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de Companheiro, maior inválido'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"
								marcado="Sim" valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="solteiro${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}" marcado="Sim"
								valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}" marcado="Sim"
								valor="1" />
							<mod:radio
								titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)"
								var="resideCompanhia${i}" valor="2" />
						</mod:grupo>
					</c:if>
					<c:if
						test="${requestScope[f:concat('parentesco',i)] == 'Menor Sob Guarda Ou Tutela'}">
					</c:if>
				</c:forEach>
			</mod:grupo>
		</c:if>
		<mod:grupo>
			<mod:caixaverif
				titulo="Estou ciente de que devo enviar a documentação exigida"
				var="documentacaoExigida" reler="Não" />
			<a
				href="${pageContext.request.contextPath}/arquivos_estaticos/TabelaDocumentacaoAuxilioSaude.htm"
				target="_blank"> <b> &nbsp; &rarr; Visualizar Documentação
			Exigida </b></a>
		</mod:grupo>
		<mod:grupo>
			<mod:caixaverif titulo="Declaro que estou ciente:"
				var="declaracaoCiente" reler="Não" />
			<p>- dos termos da Resolução nº 02/2008 do CJF e do despacho
			exarado pelo Exmo. Presidente do TRF em janeiro de 2009 e divulgado
			por correio eletrônico institucional em 04/03/2009.</p>
			<p>- de que a inclusão solicitada somente será
			processada/efetivada mediante o envio imediato à SEBEN da
			documentação requerida. Caso a documentação não chegue até à referida
			Seção até o último dia do mês pretendido, deverá ser feita outra
			solicitação com nova data de vigência.</p>
			<p>- de que o(s) beneficiário(s) relacionado(s) não recebe(m)
			auxílio semelhante e nem participa(m) de outro programa de
			assistência à saúde, custeado pelos cofres públicos, ainda que em
			parte;</p>
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
	

		<p style="TEXT-INDENT: 2cm" align="justify">
		${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).nomePessoa},
		matrícula nº
		${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).matricula},
		lotado no(a)
		${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).lotacao.descricao},
		vem requerer <c:choose>
			<c:when test="${tipoDeInclusao == 'Exclusivamente de Titular'}">					
					sua inclusão no Auxílio-Saúde, a partir de mês de ano.
				</c:when>
			<c:when
				test="${tipoDeInclusao == 'De Titular e de seus Dependentes Diretos'}">					
					sua inclusão e a inclusão dos dependentes diretos abaixo relacionados:
				</c:when>
			<c:otherwise>
					a inclusão dos dependentes diretos abaixo relacionados: 
				</c:otherwise>
		</c:choose> no Auxílio-Saúde, a partir de ${mesVigencia} de ${anoVigencia}.</p>
		<br />
				<br />

		<c:if
			test="${tipoDeInclusao == 'De Titular e de seus Dependentes Diretos' || tipoDeInclusao == 'Exclusivamente de Titular'}">
			<table width="100%" height="90px" border="1" cellpadding="2"
				cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" height="30px" colspan="2" align="left"><b>1.
					Dados do Titular</b></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="50%" height="30px" align="left"><b>Data
					do exercício:</b> ${dataExercicio}</td>
					<td bgcolor="#FFFFFF" width="50%" height="30px" align="left"><b>Data
					de Nascimento:</b> ${dataNascimentoTitular}</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" height="30px" align="left"><b>Estado
					Civil:</b> ${estadoCivilTitular}</td>
					<td width="20%" bgcolor="#FFFFFF" colspan="2" align="left"><b>Plano:</b>
					${plano}</td>
				</tr>
			</table>
			<br />
		</c:if>


		<c:if
			test="${tipoDeInclusao == 'De Titular e de seus Dependentes Diretos' || tipoDeInclusao == 'Exclusivamente de Dependentes Diretos' }">
			<table width="100%" height="90px" border="1" cellpadding="2"
				cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" height="30px" align="left" colspan="2"><b>2.
					Dados dos Dependentes Diretos</b></td>
				</tr>
				<c:forEach var="i" begin="1" end="${totalDeDependentes}">
					<tr>
						<td bgcolor="#FFFFFF" height="30px" colspan="2" align="left"
							style="background-color: #D3D3D3"><b>2.${i}. Nome:
						${requestScope[f:concat('nomeDependente',i)]}</b></td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="50%" height="30px" align="left"><b>Data
						de Nascimento:</b>
						${requestScope[f:concat('dataNascimentoDependente',i)]}</td>
						<td bgcolor="#FFFFFF" width="50%" height="30px" align="left"><b>Parentesco/Condição:</b>
						${requestScope[f:concat('parentesco',i)]}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" height="30px" align="left"><b>Estado
						Civil:</b> ${requestScope[f:concat('estadoCivilDep',i)]}</td>
						<c:if test="${pertence == 'Não'}">
							<td width="20%" bgcolor="#FFFFFF" colspan="2" align="left"><b>Plano:</b>
							${requestScope[f:concat('planoDep',i)]}</td>
						</c:if>
						<c:if test="${pertence == 'Sim'}">
							<td width="20%" bgcolor="#FFFFFF" colspan="2" align="left"><b>Plano:</b>
							${plano}</td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</c:if>

		<c:if test="${declaracaoCiente == 'Sim'}">
			<p>Declara que:</p>

			<p align="justify">- dos termos da Resolução nº 02/2008 do CJF e
			do despacho exarado pelo Exmo. Presidente do TRF em janeiro de 2009 e
			divulgado por correio eletrônico institucional em 04/03/2009.</p>

			<p align="justify">- de que a inclusão solicitada somente será
			processada/efetivada mediante o envio imediato à SEBEN da
			documentação requerida. Caso a documentação não chegue até à referida
			Seção até o último dia do mês pretendido, deverá ser feita outra
			solicitação com nova data de vigência.</p>

			<p align="justify">- de que o(s) beneficiário(s) relacionado(s)
			não recebe(m) auxílio semelhante e nem participa(m) de outro programa
			de assistência à saúde, custeado pelos cofres públicos, ainda que em
			parte;</p>
		</c:if>

		<c:if test="${documentacaoExigida == 'Sim'}">
			<p align="justify">firma o seguinte compromisso: <b>Declara
			estar ciente de que tais documentos devem ser apresentados,
			impreterivelmente, até o pedido de vacância.</b></p>
		</c:if>

		<c:forEach var="i" begin="1" end="${totalDeDependentes}">
			<c:if
				test="${tipoDeInclusao == 'De Titular e de seus Dependentes Diretos' || tipoDeInclusao == 'Exclusivamente de Dependentes Diretos' }">

				<c:if
					test="${requestScope[f:concat('parentesco',i)] != 'Cônjuge' && requestScope[f:concat('parentesco',i)] != 'Filhos Menores De 21 Anos'}">

					<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />

					<p style="font-family: Arial; font-size: 11pt; font-weight: bold;"
						align="center">DECLARA&Ccedil;&Atilde;O</p>
				<br />
					<p align="justify" style="TEXT-INDENT: 2cm">
					${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).nomePessoa},
					matrícula nº
					${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).matricula},
					lotado no(a)
					${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).lotacao.descricao},
					declara, sob as penas da lei, a fim de fazer prova perante o
					Auxílio-Saúde da SJRJ, que
					${requestScope[f:concat('nomeDependente',i)]} permanece na condição
					de solteiro(a), estudante, reside em sua companhia e que vive sob
					sua dependência econômica.</p>
				<br />
				<br />
					<p align="center">${doc.dtExtenso}</p>
					<c:import
						url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
					<p align="center">
					RJ${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).matricula}
					</p>

				</c:if>
			</c:if>
		</c:forEach>


	</mod:documento>
</mod:modelo>

