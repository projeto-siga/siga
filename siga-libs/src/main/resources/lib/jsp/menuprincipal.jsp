<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

		<c:if test="${empty pagina_de_erro}">
			<li class="dropdown"><a href="#" class="dropdown-toggle"> <i class="fa fa-sitemap"></i> <span class="hidden-xs">Módulos</span></a>
				<ul class="dropdown-menu">
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
						<li><a
							href="/sigaex/app/expediente/doc/listar?primeiraVez=sim"><i class="fa fa-cube"></i> <span class="hidden-xs">Documentos</span></a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF:Módulo de Workflow')}">
						<li><a href="/sigawf/app/resumo"><i class="fa fa-cube"></i> <span class="hidden-xs">Workflow</span></a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
						<li><a href="/sigasr/"><i class="fa fa-cube"></i> <span class="hidden-xs">Serviços</span></a>
						</li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
						<li><a href="/sigagc/app/estatisticaGeral"><i class="fa fa-cube"></i> <span class="hidden-xs">Gestão de Conhecimento</span></a>
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
						<li class="dropdown"><a href="#" class="dropdown-toggle"> <i class="fa fa-plus-square"></i> <span class="hidden-xs">Pessoas</span></a>
			 				<ul class="dropdown-menu">
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
						<li><a href="/sigapp/"><i class="fa fa-cube"></i> <span class="hidden-xs">Agenda de Perícias Médicas</span></a></li>
					</c:if>
						
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
                    	<li><a href="/sigatp/"><i class="fa fa-cube"></i> <span class="hidden-xs">Transportes</span></a>
                    </li>
                </c:if>
					
				</ul>
			</li>
			
			
<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade')}">
	<li class="dropdown"><a href="#" class="dropdown-toggle"><i
			class="fa fa-user"></i> <span>Gestão de Identidade</span></a>
		<ul class="dropdown-menu">
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
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR:Gerenciar grupos de distribuição')
							       || (f:podeGerirAlgumGrupo(titular,lotaTitular,2) && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;DELEG:Gerenciar grupos de distribuição delegados'))}">
				<li><ww:a href="${serverAndPort}/siga/gi/email/listar.action">Grupo de Distribuição</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;SELFSERVICE:Gerenciar serviços da própria lotação')}">
				<li><ww:a href="/siga/gi/servico/acesso.action">Acesso a Serviços</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;REL:Gerar relatórios')}">
				<li class="dropdown"><a href="#" class="dropdown-toggle"> <i
						class="fa fa-dashboard"></i> <span class="hidden-xs">Relatórios</span></a>
					<ul class="dropdown-menu">
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
	<li class="dropdown"><a href="#" class="dropdown-toggle"><i
			class="fa fa-cog"></i> <span>Ferramentas</span></a>
		<ul class="dropdown-menu">
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;MODVER:Visualizar modelos')}">
				<li><ww:a href="/siga/modelo/listar.action">Cadastro de modelos</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_ORGAO:Cadastrar Órgãos')}">
				<li><ww:a href="/siga/orgao/listar.action">Cadastro de Órgãos Externos</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
				<li><ww:a href="/sigawf/administrar.action">Administrar SIGA WF</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_FERIADO:Cadastrar Feriados')}">
				<li><ww:a href="/siga/feriado/listar.action">Cadastro de Feriados</ww:a></li>
			</c:if>
		</ul></li>
</c:if>

<%--	<li><a target="_blank"
			href="/wiki/Wiki.jsp?page=${f:removeAcento(titulo)}">Ajuda</a></li>
 --%>
<!-- insert menu -->
<c:import url="/paginas/menus/menu.jsp"></c:import>

