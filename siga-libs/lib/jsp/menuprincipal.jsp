<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<li><a class="" href="#">SIGA</a>
	<ul>
		<li><a href="/siga/principal.action">Página Inicial</a>
		</li>
		<c:if test="${empty pagina_de_erro}">
			<li><a href="#">Módulos</a>
				<ul>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
						<li><a
							href="/sigaex/expediente/doc/listar.action?primeiraVez=sim">Documentos</a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF:Módulo de Workflow')}">
						<li><a href="/sigawf/resumo.action">Workflow</a>
						</li>
					</c:if>
					
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
						<li><a
							href="/sigasr/" >Serviços</a>
						</li>
					</c:if>

					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
						<li><a
							href="/sigagc/" >Gestão de Conhecimento</a>
						</li>
					</c:if>

					<!-- <li><a href="/sigatr/">Treinamento</a>
					</li> -->
					<!-- <li><a href="/SigaServicos/">Serviços</a>
					</li> -->
					<li><a href="#">Pessoas</a>
						<ul>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro')}">
								<li><a href="${f:getURLSistema('siga.sgp.cad')}">Cadastro</a>
								</li>
							</c:if>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação')}">
								<li><a href="${f:getURLSistema('siga.sgp.lot')}">Lotação</a>
								</li>
							</c:if>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios')}">
								<li><a href="${f:getURLSistema('siga.sgp.bnf')}">Benefícios</a>
								</li>
							</c:if>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação')}">
								<li><a href="${f:getURLSistema('siga.sgp.aq')}">AQ</a></li>
							</c:if>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento')}">
								<li><a href="${f:getURLSistema('siga.sgp.trn')}">Treinamento</a>
								</li>
							</c:if>
						</ul>
					</li>
				</ul>
			</li>
			<li><a href="#">Administração</a>
				<ul>
					<li><ww:a href="/siga/trocar_senha.action">Trocar senha</ww:a>
					</li>
					<%--
					<li><a href="/siga/substituicao/substituir.action">Entrar
							como substituto</a>
					</li>
					<c:if
						test="${(not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao) ||(not empty titular && titular.idPessoa!=cadastrante.idPessoa)}">
						<li><ww:a href="/siga/substituicao/finalizar.action">Finalizar substituição de 
					<c:choose>
									<c:when
										test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">${titular.nomePessoa}</c:when>
									<c:otherwise>${lotaTitular.sigla}</c:otherwise>
								</c:choose>
							</ww:a>
						</li>
					</c:if>
					 --%>
					<ww:if test="${(cadastrante.idPessoa != titular.idPessoa) || (cadastrante.lotacao.idLotacao != lotaTitular.idLotacao)}">
					 <%-- é uma substituição --%> 
					 	<c:if test="${f:podeCadastrarQqSubstituicaoPorConfiguracao(cadastrante, cadastrante.lotacao)}">
					 		<li><ww:a href="${serverAndPort}/siga/substituicao/listar.action">Gerenciar possíveis substitutos</ww:a>
							</li>
					 	</c:if>
					 </ww:if>
					 <ww:else>
					 	<li><ww:a href="${serverAndPort}/siga/substituicao/listar.action">Gerenciar possíveis substitutos</ww:a>
						</li>
					 </ww:else>				
				</ul>
			</li>


			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade')}">
				<li><a href="#">Gestão de Identidade</a>
					<ul>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;ID:Gerenciar identidades')}">
							<li><ww:a href="/siga/gi/identidade/listar.action">Identidade</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERMISSAO:Gerenciar permissões')}">
							<li><ww:a href="/siga/gi/acesso/listar.action">Configurar Permissões</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFIL:Gerenciar perfis de acesso')}">
							<li><ww:a href="/siga/gi/perfil/listar.action">Perfil de Acesso</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFILJEE:Gerenciar perfis do JEE')}">
							<li><ww:a href="/siga/gi/perfiljee/listar.action">Perfil de Acesso do JEE</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GEMAIL:Gerenciar grupos de distribuição')}">
							<li><ww:a href="/siga/gi/email/listar.action">Grupo de Distribuição</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;SELFSERVICE:Gerenciar serviços da própria lotação')}">
							<li><ww:a href="/siga/gi/servico/acesso.action">Acesso a Serviços</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;REL:Gerar relatórios')}">
							<li><a href="#">Relatórios</a>
								<ul>
									<li><ww:a
											href="/siga/gi/relatorio/selecionar_acesso_servico.action">Acesso aos Serviços</ww:a>
									</li>
									<li><ww:a
											href="/siga/gi/relatorio/selecionar_permissao_usuario.action">Permissões de Usuário</ww:a>
									</li>
									<li><ww:a
											href="/siga/gi/relatorio/selecionar_alteracao_direitos.action">Alteração de Direitos</ww:a>
									</li>
									<li><ww:a
											href="/siga/gi/relatorio/selecionar_historico_usuario.action">Histórico de Usuário</ww:a>
									</li>
								</ul></li>
						</c:if>
					</ul></li>
			</c:if>
		</c:if>

		<c:if
			test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;FE:Ferramentas')}">
			<li><a href="#">Ferramentas</a>
				<ul>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;MODVER:Visualizar modelos')}">
						<li><ww:a href="/siga/modelo/listar.action">Cadastro de modelos</ww:a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
						<li><ww:a href="/sigawf/administrar.action">Administrar SIGA WF</ww:a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
						<li><ww:a href="/sigawf/administrar.action">Administrar SIGA WF</ww:a>
						</li>
					</c:if>
				</ul></li>
		</c:if>

		<%--	<li><a target="_blank"
			href="/wiki/Wiki.jsp?page=${f:removeAcento(titulo)}">Ajuda</a></li>
 --%>
		<li><a href="#">Substituir</a>
			<ul class="navmenu-large">
				<c:forEach var="substituicao" items="${meusTitulares}">
					<li><a
						href="/siga/substituicao/substituir_gravar.action?idTitular=${substituicao.titular.idPessoa}&idLotaTitular=${substituicao.lotaTitular.idLotacao}">
							<c:choose>
								<c:when test="${not empty substituicao.titular}">
						${f:maiusculasEMinusculas(substituicao.titular.nomePessoa)}
					</c:when>
								<c:otherwise>
						${f:maiusculasEMinusculas(substituicao.lotaTitular.nomeLotacao)}
					</c:otherwise>
							</c:choose> </a>
					</li>
				</c:forEach>
			</ul>
		</li>

		<li><ww:a href="/siga/logoff.action">Logoff</ww:a>
		</li>

	</ul>
</li>
<!-- insert menu -->
<c:import url="/paginas/menus/menu.jsp"></c:import>

