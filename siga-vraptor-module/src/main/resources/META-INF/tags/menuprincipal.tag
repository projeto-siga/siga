<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
	function getServidorSiga(cname) {
		try {
			var name = cname + "=";
			var ca = document.cookie.split(';');
			var str = "";

			for (var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) == ' ') {
					c = c.substring(1);
				}

				if (c.indexOf(name) == 0) {
					str = c.substring(name.length, c.length);
				}
			}

			if (str != "") {
				var res = str.replace(/"/g, "");
				if (res != null && res != "undefined") {
					return res.split(';')[0].substring(25);
				}
			}

			return "";
		} catch (err) {
			return "";
		}
	}
</script>

<li class="nav-item dropdown"><a href="javascript:void(0);"
	class="navbar-brand dropdown-toggle" data-toggle="dropdown"> <fmt:message
			key="menu.titulo" /></a>
	<ul class="dropdown-menu">
		<c:if test="${not empty f:resource('/siga.pagina.inicial.url') && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;QUADRO:Quadros Quantitativos')}">
			<li><a class="dropdown-item"
				href="/siga/app/principal?redirecionar=false">Quadros
					Quantitativos</a></li>
		</c:if>
		<c:if test="${f:resource('/siga.local') != 'GOVSP'}">
			<li><a class="dropdown-item" href="/siga/app/principal">Página
					Inicial</a></li>
		</c:if>
		<c:if
			test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF:Módulo de Workflow') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SE:Módulo de Servicos Externos') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GO:Gestão Orçamentária') or	
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar') or	
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BDP: Módulo de Banco de Permutas') or 
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios') or 
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro') or 
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas') or 
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DCN: Módulo de Docência de Magistrados') or 
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação') or 
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento') or
					f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TERC: Módulo de Terceirizados')}">
		<li class="dropdown-submenu"><a href="javascript:void(0);"
			class="dropdown-item dropdown-toggle">Módulos</a>
			<ul class="dropdown-menu">
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/doc/listar?primeiraVez=sim">Documentos</a>
					</li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF:Módulo de Workflow')}">
					<li><a class="dropdown-item" href="/sigawf/app/ativos">Workflow</a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
					<li><a class="dropdown-item" href="/sigasr/">Serviços</a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
					<li><a class="dropdown-item"
						href="/sigagc/app/estatisticaGeral">Gestão de Conhecimento</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
					<li><a class="dropdown-item" href="/sigatp/">Transportes</a></li>
				</c:if>

				<c:if test="${'ZZ' != titular.orgaoUsuario.sigla}">

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SE:Módulo de Servicos Externos')}">
						<li><a class="dropdown-item" href="/sigade/">Dados
								Externos</a></li>
					</c:if>

					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GO:Gestão Orçamentária')}">
						<li><a class="dropdown-item" href="/sigago/">Gestão
								Orçamentária</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação') or
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BDP: Módulo de Banco de Permutas') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DCN: Módulo de Docência de Magistrados') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento') or
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TERC: Módulo de Terceirizados')}">
						<li class="dropdown-submenu"><a href="javascript:void(0);"
							class="dropdown-item dropdown-toggle">Pessoas</a>
							<ul class="dropdown-menu">
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.aq')}">AQ</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BDP: Módulo de Banco de Permutas')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.bdp')}">Banco de
											Permutas</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.bnf')}">Benefícios</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.cad')}">Cadastro</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.cst')}">Consultas</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DCN: Módulo de Docência de Magistrados')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.dcn')}">Docência de
											Magistrados</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.lot')}">Lotação</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.trn')}">Treinamento</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TERC: Módulo de Terceirizados')}">
									<li><a class="dropdown-item"
										href="${f:getURLSistema('/siga.sgp.terc')}">Terceirizados</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;PP:Agendamento de perícias do INSS')}">
						<li class="dropdown-submenu"><a href="javascript:void(0);"
							class="dropdown-item dropdown-toggle">Agendas</a>
							<ul class="dropdown-menu">
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;PP')}">
									<li><a class="dropdown-item" href="/sigapp/">Perícias
											Médicas</a></li>
								</c:if>
							</ul></li>
					</c:if>
				</c:if>

			</ul></li>
		</c:if>
		<c:if test="${f:resource('/siga.local') eq 'GOVSP'}">
			<c:if test="${not empty f:resource('gsa.url')}">
				<li><a class="dropdown-item" href="/siga/app/busca">Busca
						Textual</a></li>
			</c:if>
		</c:if>

		<li class="dropdown-submenu"><a href="javascript:void(0);" class="dropdown-item dropdown-toggle">Administração</a>
			<ul class="dropdown-menu">
				<c:if test="${(!f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;ADM:Administração;TSEN:Ocultar Trocar Senha') || titular.orgaoUsuario.sigla == 'ZZ')}">
					<li><a class="dropdown-item"
						href="/siga/app/usuario/trocar_senha">Trocar senha</a></li>
				</c:if>
				<c:if test="${(!f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;ADM;DEMAIL:Definir Email do Usuário') || titular.orgaoUsuario.sigla == 'ZZ')}">
					<li><a class="dropdown-item"
						href="/siga/app/usuario/trocar_email">Trocar Email do Usuário</a></li>
				</c:if>
				<c:if test="${f:podeUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao) }">
					<li class="dropdown-submenu"><a href="javascript:void(0);" class="dropdown-item dropdown-toggle">Gerenciar PIN</a>
						<ul class="dropdown-menu">
						<c:choose>
							<c:when test="${(empty identidadeCadastrante.pinIdentidade)}">
								<li><a class="dropdown-item" href="/siga/app/pin/cadastro">Cadastrar novo PIN</a>
							</c:when>
							<c:otherwise>	
								<li><a class="dropdown-item" href="/siga/app/pin/troca">Alterar PIN</a>
								<li><a class="dropdown-item" href="/siga/app/pin/reset">Esqueci meu PIN</a>
							</c:otherwise>	
						</c:choose>
						</ul>
					</li>
				</c:if>

				
				
				<%--
					<li><a href="/siga/substituicao/substituir.action">Entrar
							como substituto</a>
					</li>
					<c:if
						test="${(not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao) ||(not empty titular && titular.idPessoa!=cadastrante.idPessoa)}">
						<li><a href="/siga/substituicao/finalizar.action">Finalizar substituição de 
					<c:choose>
									<c:when
										test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">${titular.nomePessoa}</c:when>
									<c:otherwise>${lotaTitular.sigla}</c:otherwise>
								</c:choose>
							</a>
						</li>
					</c:if>
					 --%>
				<c:choose>
					<c:when
						test="${(cadastrante.idPessoa != titular.idPessoa) || (cadastrante.lotacao.idLotacao != lotaTitular.idLotacao)}">
						<%-- é uma substituição --%>
						<c:if
							test="${f:podeCadastrarQqSubstituicaoPorConfiguracao(cadastrante, cadastrante.lotacao)}">
							<li><a class="dropdown-item"
								href="${serverAndPort}/siga/app/substituicao/listar">Gerenciar
									possíveis substitutos</a></li>
						</c:if>
					</c:when>
					<c:otherwise>
						<c:catch>
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;ADM:Administração')}">
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;ADM;GER:Gerenciar Substitutos')}">
									<li><a class="dropdown-item"
										href="${serverAndPort}/siga/app/substituicao/listar">Gerenciar
											possíveis substitutos</a></li>
								</c:if>
							</c:if>
						</c:catch>
					</c:otherwise>
				</c:choose>
				<!-- f:podeDelegarVisualizacao(cadastrante, cadastrante.lotacao) -->
				<c:if test="${false}">
					<li><a class="dropdown-item"
									href="${serverAndPort}/siga/app/visualizacao/listar">Delegar visualização</a></li>
				</c:if>
			</ul></li>



		<c:if
			test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade')}">
			<li class="dropdown-submenu"><a href="javascript:void(0);"
				class="dropdown-item dropdown-toggle">Gestão de Identidade</a>
				<ul class="dropdown-menu">
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;ID:Gerenciar identidades')}">
						<li><a class="dropdown-item"
							href="/siga/app/gi/identidade/listar">Identidade</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERMISSAO:Gerenciar permissões')}">
						<li><a class="dropdown-item"
							href="/siga/app/gi/acesso/listar">Configurar Permissões</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFIL:Gerenciar perfis de acesso')}">
						<li><a class="dropdown-item"
							href="/siga/app/gi/perfil/listar">Perfil de Acesso</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;CAD_ORGAO_USUARIO:Cadastrar Orgãos Usuário')}">
						<li><a class="dropdown-item"
							href="/siga/app/orgaoUsuario/listar">Cadastro de Orgãos</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;CAD_CARGO:Cadastrar Cargo')}">
						<li><a class="dropdown-item" href="/siga/app/cargo/listar">Cadastro
								de Cargo</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;CAD_LOTACAO:Cadastrar Lotação')}">
						<li><a class="dropdown-item" href="/siga/app/lotacao/listar">Cadastro
								de <fmt:message key="usuario.lotacao" />
						</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;CAD_FUNCAO:Cadastrar Função de Confiança')}">
						<li><a class="dropdown-item" href="/siga/app/funcao/listar">Cadastro
								de Função de Confiança</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;CAD_PESSOA:Cadastrar Pessoa')}">
						<li><a class="dropdown-item" href="/siga/app/pessoa/listar">Cadastro
								de Pessoa</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;ENV:Envio de E-mail para Novos Usuários')}">
						<li><a class="dropdown-item"
							href="/siga/app/pessoa/enviarEmail">Envio de E-mail para
								Novos Usuários</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
						<li><a class="dropdown-item"
							href="/sigawf/app/admin/administrar">Administrar SIGA WF</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFILJEE:Gerenciar perfis do JEE')}">
						<li><a class="dropdown-item"
							href="/siga/app/gi/perfilJEE/listar">Perfil de Acesso do JEE</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR:Gerenciar grupos de distribuição')
							       || (f:podeGerirAlgumGrupo(titular,lotaTitular,2) && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;DELEG:Gerenciar grupos de distribuição delegados'))}">
						<li><a class="dropdown-item"
							href="${serverAndPort}/siga/app/gi/grupoDeEmail/listar">Grupo
								de Distribuição</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;SELFSERVICE:Gerenciar serviços da própria lotação')}">
						<li><a class="dropdown-item"
							href="/siga/app/gi/servico/editar">Acesso a Serviços</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;REL:Gerar relatórios')}">
						<li class="dropdown-submenu"><a href="javascript:void(0);"
							class="dropdown-item dropdown-toggle">Relatórios</a>
							<ul class="dropdown-menu">
								<li><a class="dropdown-item"
									href="/siga/app/gi/relatorio/selecionar_acesso_servico">Acesso
										aos Serviços</a></li>
								<li><a class="dropdown-item"
									href="/siga/app/gi/relatorio/selecionar_permissao_usuario">Permissões
										de Usuário</a></li>
								<li><a class="dropdown-item"
									href="/siga/app/gi/relatorio/selecionar_alteracao_direitos">Alteração
										de Direitos</a></li>
								<li><a class="dropdown-item"
									href="/siga/app/gi/relatorio/selecionar_historico_usuario">Histórico
										de Usuário</a></li>
							</ul></li>
					</c:if>
				</ul></li>
		</c:if>


		<c:if
			test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;FE:Ferramentas')}">
			<li class="dropdown-submenu"><a href="javascript:void(0);"
				class="dropdown-item dropdown-toggle">Ferramentas</a>
				<ul class="dropdown-menu">
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;MODVER:Visualizar modelos')}">
						<li><a class="dropdown-item" href="/siga/app/modelo/listar">Cadastro
								de Modelos</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_ORGAO:Cadastrar Orgãos')}">
						<li><a class="dropdown-item" href="/siga/app/orgao/listar">Cadastro
								de Orgãos Externos</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
						<li><a class="dropdown-item"
							href="/sigawf/app/admin/administrar">Administrar SIGA WF</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;LDAP_ADMIN:Administrar Integracao LDAP')}">
						<li><a class="dropdown-item"
							href="/siga/app/admin/ldap/administrar">Administrar
								Integração LDAP</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_FERIADO:Cadastrar Feriados')}">
						<li><a class="dropdown-item" href="/siga/app/feriado/listar">Cadastro
								de Feriados</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CFG:Cadastrar Configurações')}">
						<li><a class="dropdown-item"
							href="/siga/app/configuracao/listar">Cadastro de
								Configurações</a></li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;FE: Ferramentas;CAD_MARCADOR: Cadastro de Marcadores')}">
						<li><a class="dropdown-item"
							href="/siga/app/marcador/listar">Cadastro de 
								Marcadores</a></li>
					</c:if>
				</ul></li>
		</c:if>

		<c:if test="${not empty meusTitulares}">
			<li class="dropdown-submenu"><a href="javascript:void(0);"
				class="dropdown-item dropdown-toggle">Substituir</a>
				<div class="dropdown-menu">
					<c:forEach var="substituicao" items="${meusTitulares}">
						<div class="dropdown-item" href="#">
						<a class=""
							href="javascript:delSession();location.href='/siga/app/substituicao/substituirGravar?id=${substituicao.idSubstituicao}'">
								<c:choose>
									<c:when test="${not empty substituicao.titular}">
										${f:maiusculasEMinusculas(substituicao.titular.pessoaAtual.nomePessoa)}
									</c:when>
									<c:otherwise>
										${f:maiusculasEMinusculas(substituicao.lotaTitular.lotacaoAtual.nomeLotacao)}
									</c:otherwise>
								</c:choose>
						</a>
						<a class="text-light"
							style="border-left: 0px; float: right; padding-left: 0.1em; padding-right: 0.1em;"
							href="javascript:if (confirm('Deseja excluir substituição?')) location.href='/siga/app/substituicao/exclui?id=${substituicao.idSubstituicao}&porMenu=true';">
								<img style="display: inline;"
								src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir"
								onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';"
								onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';">
						</a> 
						</div>
						
					</c:forEach>
				</div></li>
		</c:if>

		<li><c:choose>
				<c:when test="${f:resource('/siga.local') eq 'GOVSP'}">
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;MA:Manual')}">
							<a class="dropdown-item" id="apostilaSiga"
								href="/siga/arquivos/Manual-Basico-de-Operacoes-Sistema-SP-Sem-Papel-Documentos-Digitais.pdf"
								target="_blank">Manual</a>
					</c:if>
				</c:when>
				<c:otherwise>
					<a class="dropdown-item" id="apostilaSiga"
						href="/siga/arquivos/apostila_sigaex.pdf" target="_blank">Apostila
						SIGA-Doc</a>

				</c:otherwise>
			</c:choose></li>

		<li><a class="dropdown-item" href="/siga/public/app/logout">Logoff</a></li>
	</ul></li>

<!-- insert menu -->
<c:import url="/paginas/menus/menu.jsp"></c:import>
<c:if test="${false}">
</c:if>




