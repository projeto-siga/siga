<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<!-- este modelo trata de
REQUERIMENTO PARA INSCRICAO NO PLANO DE SAÚDE-->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="seben" scope="request" />
<c:set var="exibirIncDeferimento" value="não" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		</br>
		<mod:grupo titulo="Tipos de Inclusão">
			<mod:radio var="tipoDeInclusao" valor="1" reler="sim" titulo="Exclusivamente de Titular"/>
			<mod:radio var="tipoDeInclusao" valor="2" reler="sim" gerarHidden='não' titulo="De Titular e de seus Dependentes Diretos (A inscrição de dependentes somente poderá ser feita se o servidor for inscrito como titular no referido benefício e somente ele poderá efetivá-la)"/>
			<mod:radio var="tipoDeInclusao" valor="3" reler="sim" gerarHidden='não' titulo="Exclusivamente de Dependentes Diretos (A inscrição de dependentes somente poderá ser feita se o servidor for inscrito como titular no referido benefício e somente ele poderá efetivá-la)"/>
			<mod:radio var="tipoDeInclusao" valor="4" reler="sim" gerarHidden='não' titulo="De Titular e de seus Agregados, Beneficiários Designados, Pais Dependentes Econômicos (A inscrição de dependentes somente poderá ser feita se o servidor for inscrito como titular no referido benefício e somente ele poderá efetivá-la)"/>
			<mod:radio var="tipoDeInclusao" valor="5" reler="sim" gerarHidden='não' titulo="Exclusivamente de Agregados, Beneficiários Designados, Pais Dependentes Econômicos (A inscrição de dependentes somente poderá ser feita se o servidor for inscrito como titular no referido benefício e somente ele poderá efetivá-la)"/>
		</mod:grupo>
		<c:set var="valorTipoDeInclusao" value="${tipoDeInclusao}" />
		<c:if test="${empty valorTipoDeInclusao}">
			<c:set var="valorTipoDeInclusao" value="${param['tipoDeInclusao']}" />
		</c:if>
		<c:if test="${valorTipoDeInclusao == 1 || valorTipoDeInclusao == 2 || valorTipoDeInclusao == 4}">
			<br/>
			<mod:grupo titulo="Dados do Titular">
				<mod:grupo>
					<mod:pessoa titulo="Matrícula" var="matriculaTitular" reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<mod:data titulo="Data do exercício" var="dataExercicio" />
					<mod:data titulo="Data de nascimento" var="dataNascimentoTitular"/>
					<mod:selecao titulo="Sexo" opcoes="M;F" var="sexoTitular" />
					<mod:selecao titulo="Estado Civil" var="estadoCivilTitular" opcoes="Solteiro(a);Casado(a);Divorciado(a);Viúva(o);Desquitado" reler="sim"/>
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nº de identidade" var="identidade" largura="15" />
				</mod:grupo>
				<mod:grupo>
					Se estrangeiro(a):
					<mod:texto titulo="Número do passaporte" var="passaporte" largura="15" />
					<mod:texto titulo="Número da Carteira Civil" var="carteiraCivil" largura="15" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Órgão Expedidor" var="orgaoExpedidor" largura="20" />
					<mod:data titulo="Data de expedição" var="dataExpedicao"/>
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="CPF" var="cpf" largura="15" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Número do banco" var="numBanco" largura="5" />
					<mod:texto titulo="Agência" var="agencia" largura="10" />
					<mod:texto titulo="Conta-corrente" var="contaCorrente" largura="10" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nome da mãe" var="nomeDaMae" largura="40" />
				</mod:grupo>
				<mod:grupo>
					<mod:selecao titulo="Data de vigência da Inclusão" var="mesVigencia"
						opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
					<mod:texto titulo="Ano" var="anoVigencia" largura="6" maxcaracteres="4"
						reler="ajax" idAjax="anoAjax" />
					<mod:grupo depende="anoAjax">
						<c:if test="${empty anoVigencia}">
							<mod:mensagem titulo="Alerta"
								texto="o ano de Vigência deve ser preenchido." vermelho="sim" />
						</c:if>
					</mod:grupo>
				</mod:grupo>
			</mod:grupo>
		</c:if>
		<c:if test="${valorTipoDeInclusao == 2 || valorTipoDeInclusao == 3}">
			<br/>
			<mod:grupo titulo="Dados Dos Dependentes Diretos">
				<mod:selecao titulo="Número total a incluir no Plano de Saúde " var="totalDeDependentesDiretos"
				opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
				<c:forEach var="i" begin="1" end="${totalDeDependentesDiretos}">
					<mod:grupo>
						<mod:selecao titulo="Parentesco" var="parentesco${i}" 
							opcoes="Cônjuge;Filhos Menores De 21 Anos;Filhos Entre 21 e 24 Anos;Filho Maior, Inválido;Companheiro(A);Enteado(A) - Filho De Cônjuge, Menor De 21 Anos;Enteado(A) - Filho De Cônjuge, Entre 21 e 24 Anos;Enteado(A) - Filho de cônjuge, maior inválido;Enteado(A) - Filho de Companheiro, menor de 21 anos;Enteado(A) - Filho de Companheiro, entre 21 e 24 anos;Enteado(A) - Filho de Companheiro, maior inválido;Menor Sob Guarda Ou Tutela" reler="sim"/>
					</mod:grupo>
					<mod:grupo>
						<mod:texto titulo="Nome" var="nomeDependente${i}" largura="59" />
						<mod:data titulo="Data de Nascimento" var="dataNascimentoDependente${i}"/>
					</mod:grupo>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Filhos Menores De 21 Anos'}">
						<mod:grupo>
							<mod:selecao titulo="Dependente econômico" var="dependenteEconomico${i}" 
								opcoes="Sim;Não"/>
							<mod:selecao titulo="Solteiro" var="solteiro${i}" 
								opcoes="Sim;Não"/>
							<mod:selecao titulo="Estudante" var="estudante${i}" 
								opcoes="Sim;Não"/>
							<mod:selecao titulo="Reside em sua companhia" var="resideCompanhia${i}" 
								opcoes="Sim;Não"/>
						</mod:grupo>					
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Filhos Entre 21 e 24 Anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>					
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Filho Maior, Inválido'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>					
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Companheiro(A)'}">
						<mod:selecao titulo="Estado Civil" var="estadoCivilDep${i}" opcoes="Solteiro(a);Casado(a);Divorciado(a);Viúva(o);Desquitado" reler="sim"/>
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho De Cônjuge, Menor De 21 Anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>					
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho De Cônjuge, Entre 21 e 24 Anos'}">
						<mod:texto titulo="Nº do Cartão do Auxílio-Saúde" var="numeroCartaoAuxSau${i}" largura="59" />
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>	
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de cônjuge, maior inválido'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>	
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de Companheiro, menor de 21 anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>	
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de Companheiro, entre 21 e 24 anos'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>	
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Enteado(A) - Filho de Companheiro, maior inválido'}">
						<mod:grupo titulo="Dependente econômico">
							<mod:radio titulo="Sim" var="dependenteEconomico${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="dependenteEconomico${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Solteiro">
							<mod:radio titulo="Sim" var="solteiro${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="solteiro${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Estudante">
							<mod:radio titulo="Sim" var="estudante${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não" var="estudante${i}" valor="2" />
						</mod:grupo>					
						<mod:grupo titulo="Reside em sua companhia">
							<mod:radio titulo="Sim" var="resideCompanhia${i}"  marcado="Sim" valor="1" />
							<mod:radio titulo="Não (Neste caso, não pode ser incluído no Auxílio-Saúde)" var="resideCompanhia${i}" valor="2" />
						</mod:grupo>	
					</c:if>
					<c:if test="${requestScope[f:concat('parentesco',i)] == 'Menor Sob Guarda Ou Tutela'}">
					</c:if>
				</c:forEach>
				</mod:grupo>
			</c:if>
			<mod:grupo>
				<mod:caixaverif titulo="Estou ciente de que devo enviar a documentação exigida" var="documentacaoExigida"
					reler="Não" />
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif titulo="Declaro que estou ciente:" var="declaracaoCiente" reler="Não" />
				<p align="justify">- dos termos da Resolução nº 19/99 do TRF e do Contrato firmado entre o Plano de Saúde e o TRF 2ª Região estendido à esta seccional.</p>
				<p align="justify">- de que a inclusão solicitada somente será processada/efetivada mediante o envio imediato à SEBEN da documentação requerida. Caso a documentação não chegue até à referida Seção com até 4 dias úteis de antecedência em relação à data pretendida, deverá ser feita outra solicitação com nova data de vigência.</p>	
				<p align="justify">- de que o(s) beneficiário(s) relacionados não recebe(m) auxílio semelhante e nem participa(m) de outro programa de assistência à saúde, custeado pelos cofres públicos, ainda que em parte;</p>
				<c:if test="${valorTipoDeInclusao == 2 || valorTipoDeInclusao == 3}">
					<p align="justify">- de que a solicitação de inclusão do(a) dependente (<b>filho(a) maior inválido(a), 
					companheiro(a), enteado(a)-filho(a) de cônjuge maior inválido(a), enteado(a)-filho(a) de 
					companheiro(a) menor de 21 anos, enteado(a)-filho(a) de companheiro(a) entre 21 e 24 anos e 
					enteado(a)-filho(a) de companheiro(a) maior inválido(a)</b>) no Plano de Saúde está sujeita à 
					apreciação superior, somente podendo ser efetuada após seu deferimento. Neste caso, a data 
					de vigência pretendida poderá vir a ser alterada em virtude da data da autorização. 
					Mais informações, entrar em contato com a SEBEN;</p>
				</c:if>
				<c:if test="${valorTipoDeInclusao == 4 || valorTipoDeInclusao == 5}">
					<p align="justify">- de que a solicitação de inclusão do(a) dependente (<b>beneficiário designado e 
					dependente econômico</b>) no Plano de Saúde está sujeita à apreciação superior, somente 
					podendo ser efetuada após seu deferimento. Neste caso, a data de vigência pretendida 
					poderá vir a ser alterada em virtude da data da autorização. Mais informações, entrar em 
					contato com a SEBEN</p>
				</c:if>
			</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).nomePessoa}, matrícula nº ${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).matricula}, 
			lotado no(a) ${f:pessoa(requestScope['matriculaTitular_pessoaSel.id']).lotacao.descricao},
			vem requerer 
			<c:choose>
				<c:when test="${valorTipoDeInclusao==1}">					
					sua inclusão
				</c:when>
				<c:when test="${valorTipoDeInclusao==2}">					
					sua inclusão e a inclusão dos dependentes diretos abaixo relacionados
				</c:when>
				<c:when test="${valorTipoDeInclusao==3}">
					a inclusão dos dependentes diretos abaixo relacionados 
				</c:when>
				<c:when test="${valorTipoDeInclusao==4}">
					sua inclusão e a inclusão dos dependentes abaixo relacionados 
				</c:when>
				<c:when test="${valorTipoDeInclusao==5}">
					inclusão dos dependentes abaixo relacionados 
				</c:when>
			</c:choose>
				no Plano de Saúde, a partir de 1º de ${mesVigencia} de ${anoVigencia}.
			</p>
			<br/>
			<table width="100%">
				<tr>
					<td colspan="3" align="left"><b>1. Dados do Titular:</b></td>
				</tr>
				<tr>
					<td  colspan="2" align="left">Data do exercício: ${dataExercicio}</td>
					<td  align="left">Data de Nascimento: ${dataNascimentoTitular}</td>
				</tr>
				<tr>
					<td colspan="2" align="left">Estado Civil: ${estadoCivilTitular}</td>
					<td align="left">CPF: ${cpf}</td>
				</tr>
				<tr>
					<td>Nº de Identidade: ${identidade}</td>
					<td>Órgão Expedidor: ${orgaoExpedidor}</td>
					<td>Data de Expedição: ${dataExpedicao}</td>
				</tr>
				<tr>
					<td colspan="2">Número do passaporte: ${passaporte}</td>
					<td>Número da Carteira Civil: ${carteiraCivil}</td>
				</tr>
				<tr>
					<td>Nº do banco: ${numBanco}</td>
					<td>Agência: ${agencia}</td>
					<td>Conta-corrente: ${contaCorrente}</td>
				</tr>
				<tr>
					<td colspan="3"> Nome da mãe: ${nomeDaMae}</td>
				</tr>
			</table>
			<br/>
			<table width="50%" height="90px" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" height="30px" align="left"><b>2. Dados dos Dependentes Diretos:</b></td>
				</tr>
				<c:forEach var="i" begin="1" end="${totalDeDependentesDiretos}">
					<tr>
						<td bgcolor="#FFFFFF" height="30px" align="left"><b>2.${i}. Nome: ${requestScope[f:concat('nomeDependente',i)]}</b></td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="50%" height="30px" align="left">Data de Nascimento: ${requestScope[f:concat('dataNascimentoDependente',i)]}</td>
						<td bgcolor="#FFFFFF" width="50%" height="30px" align="left">Parentesco/Condição: ${requestScope[f:concat('parentesco',i)]}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" height="30px" align="left">Estado Civil: ${requestScope[f:concat('estadoCivilDep',i)]}</td>
					</tr>
				</c:forEach>
			</table>
			
			<p>Declara que:</p>

			<p align="justify">- dos termos da Resolução nº 19/99 do TRF e do Contrato firmado entre o Plano de Saúde e o TRF 2ª Região estendido à esta seccional.</p> 

			<p align="justify">- de que a inclusão solicitada somente será processada/efetivada mediante o envio imediato à SEBEN da documentação requerida. Caso a documentação não chegue até à referida Seção com até 4 dias úteis de antecedência em relação à data pretendida, deverá ser feita outra solicitação com nova data de vigência.</p> 

			<p align="justify">- de que o(s) beneficiário(s) relacionados não recebe(m) auxílio semelhante e nem participa(m) de outro programa de assistência à saúde, custeado pelos cofres públicos, ainda que em parte;</p>
		</mod:valor>	
	
		<mod:valor var="texto_requerimento2">	
			firma o seguinte compromisso:
		 	<b>Declara estar ciente de que tais documentos devem ser apresentados, impreterivelmente, até o pedido de vacância.</b>
		</mod:valor>
	</mod:documento>
</mod:modelo>
