<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<li><a id="menu_siga" class="" href="#">SIGA</a>
	<ul>
		<li><a href="/siga/app/principal">Página Inicial</a>
		</li>
		<c:if test="${empty pagina_de_erro}">
			<li><a href="#">Módulos</a>
				<ul>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
						<li><a
							href="/sigaex/app/expediente/doc/listar?primeiraVez=sim">Documentos</a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF:Módulo de Workflow')}">
						<li><a href="/sigawf/resumo.action">Workflow</a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
						<li><a href="/sigasr/">Serviços</a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
						<li><a href="/sigagc/app/estatisticaGeral">Gestão de Conhecimento</a>
						</li>
					</c:if>
					

					<!-- <li><a href="/sigatr/">Treinamento</a>
					</li> -->
					<!-- <li><a href="/SigaServicos/">Serviços</a>
					</li> -->
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento')}">
						<li><a href="#">Pessoas</a>
							<ul>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação')}">
									<li><a href="${f:getURLSistema('siga.sgp.aq')}">AQ</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios')}">
									<li><a href="${f:getURLSistema('siga.sgp.bnf')}">Benefícios</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro')}">
									<li><a href="${f:getURLSistema('siga.sgp.cad')}">Cadastro</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas')}">
									<li><a href="${f:getURLSistema('siga.sgp.cst')}">Consultas</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação')}">
									<li><a href="${f:getURLSistema('siga.sgp.lot')}">Lotação</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento')}">
									<li><a href="${f:getURLSistema('siga.sgp.trn')}">Treinamento</a>
									</li>
								</c:if>
							</ul>
						</li>
					</c:if>
					
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;PP:Agendamento de perícias do INSS')}">
					<li><a href="#">Agendas</a>
						<ul>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;PP')}">
								<li><a href="/sigapp/">Perícias Médicas</a>
								</li>
							</c:if>
						</ul>
					</li>
					</c:if>
						
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
                    	<li><a href="/sigatp/">Transportes</a>
                    </li>
                </c:if>
					
				</ul>
			</li>
			<li><a href="#">Administração</a>
				<ul>
					<li><ww:a href="/siga/app/usuario/trocar_senha" >Trocar senha</ww:a>
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
					<ww:if
						test="${(cadastrante.idPessoa != titular.idPessoa) || (cadastrante.lotacao.idLotacao != lotaTitular.idLotacao)}">
						<%-- é uma substituição --%>
						<c:if
							test="${f:podeCadastrarQqSubstituicaoPorConfiguracao(cadastrante, cadastrante.lotacao)}">
							<li><ww:a
									href="${serverAndPort}/siga/app/substituicao/listar">Gerenciar possíveis substitutos</ww:a>
							</li>
						</c:if>
					</ww:if>
					<ww:else>
						<li><ww:a
								href="${serverAndPort}/siga/app/substituicao/listar">Gerenciar possíveis substitutos</ww:a>
						</li>
					</ww:else>
				</ul>
			</li>


			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade')}">
				<li><a href="#">Gestão de Identidade</a>
					<ul>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;ID:Gerenciar identidades')}">
							<li><ww:a href="/siga/app/gi/identidade/listar">Identidade</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERMISSAO:Gerenciar permissões')}">
							<li><ww:a href="/siga/app/gi/acesso/listar">Configurar Permissões</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFIL:Gerenciar perfis de acesso')}">
							<li><ww:a href="/siga/app/gi/perfil/listar">Perfil de Acesso</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFILJEE:Gerenciar perfis do JEE')}">
							<li><ww:a href="/siga/app/gi/perfilJEE/listar">Perfil de Acesso do JEE</ww:a>
							</li>
						</c:if>
						<c:if 						
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR:Gerenciar grupos de distribuição')
							       || (f:podeGerirAlgumGrupo(titular,lotaTitular,2) && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;DELEG:Gerenciar grupos de distribuição delegados'))}"> 	
	 						<li><ww:a
									href="${serverAndPort}/siga/app/gi/grupoDeEmail/listar">Grupo de Distribuição</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;SELFSERVICE:Gerenciar serviços da própria lotação')}">
							<li><ww:a href="/siga/app/gi/servico/editar">Acesso a Serviços</ww:a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;REL:Gerar relatórios')}">
							<li><a href="#">Relatórios</a>
								<ul>
									<li><ww:a
											href="/siga/app/gi/relatorio/selecionar_acesso_servico">Acesso aos Serviços</ww:a>
									</li>
									<li><ww:a
											href="/siga/app/gi/relatorio/selecionar_permissao_usuario">Permissões de Usuário</ww:a>
									</li>
									<li><ww:a
											href="/siga/app/gi/relatorio/selecionar_alteracao_direitos">Alteração de Direitos</ww:a>
									</li>
									<li><ww:a
											href="/siga/app/gi/relatorio/selecionar_historico_usuario">Histórico de Usuário</ww:a>
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
						<li><ww:a href="/siga/app/modelo/listar">Cadastro de modelos</ww:a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_ORGAO:Cadastrar Orgãos')}">
						<li><ww:a href="/siga/app/orgao/listar">Cadastro de Orgãos Externos</ww:a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
						<li><ww:a href="/sigawf/administrar.action">Administrar SIGA WF</ww:a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_FERIADO:Cadastrar Feriados')}">
						<li><ww:a href="/siga/app/feriado/listar">Cadastro de Feriados</ww:a></li>
					</c:if>	
				</ul></li>
		</c:if>

		<%--	<li><a target="_blank"
			href="/wiki/Wiki.jsp?page=${f:removeAcento(titulo)}">Ajuda</a></li>
 --%>
		<li><a href="#">Substituir</a>
			<ul class="navmenu-large">
				<c:forEach var="substituicao" items="${meusTitulares}">
					<li>
						<a style="border-left: 0px; float: right; padding-left: 0.5em; padding-right: 0.5em;"
						    href="javascript:if (confirm('Deseja excluir substituição?')) location.href='/siga/app/substituicao/exclui?id=${substituicao.idSubstituicao}&porMenu=true';">
							<img style="display: inline;"
							src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir"
							onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';"
							onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';">
						</a> 
						<a href="/siga/app/substituicao/substituirGravar?idTitular=${substituicao.titular.idPessoa}&idLotaTitular=${substituicao.lotaTitular.idLotacao}">
							<c:choose>
								<c:when test="${not empty substituicao.titular}">
									${f:maiusculasEMinusculas(substituicao.titular.nomePessoa)}
								</c:when>
								<c:otherwise>
									${f:maiusculasEMinusculas(substituicao.lotaTitular.nomeLotacao)}
								</c:otherwise>
							</c:choose> 
						</a>
					</li>
				</c:forEach>

			</ul>
		</li>


		<li><ww:a href="/siga/?GLO=true">Logoff</ww:a>
		</li>

	</ul>
</li>
<!-- insert menu -->
<c:import url="/paginas/menus/menu.jsp"></c:import>

