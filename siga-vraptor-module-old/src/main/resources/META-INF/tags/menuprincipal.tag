<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript">
	$(document).ready(function(){
		document.getElementById("apostilaSiga").title = getServidorSiga("JSESSIONID"); 
	});

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
		}
		catch(err) {
			return "";    
		}
	}			
</script>

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
						<li><a href="/sigawf/app/resumo">Workflow</a>
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
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BDP: Módulo de Banco de Permutas') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DCN: Módulo de Docência de Magistrados') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação') or 
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento') or
						f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TERC: Módulo de Terceirizados')}">
						<li><a href="#">Pessoas</a>
							<ul>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;AQ: Módulo de Adicional de Qualificação')}">
									<li><a href="${f:getURLSistema('/siga.sgp.aq')}">AQ</a></li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BDP: Módulo de Banco de Permutas')}">
									<li><a href="${f:getURLSistema('/siga.sgp.bdp')}">Banco de Permutas</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;BNF: Módulo de Benefícios')}">
									<li><a href="${f:getURLSistema('/siga.sgp.bnf')}">Benefícios</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CAD: Módulo de Cadastro')}">
									<li><a href="${f:getURLSistema('/siga.sgp.cad')}">Cadastro</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;CST: Módulo de Consultas')}">
									<li><a href="${f:getURLSistema('/siga.sgp.cst')}">Consultas</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DCN: Módulo de Docência de Magistrados')}">
									<li><a href="${f:getURLSistema('/siga.sgp.dcn')}">Docência de Magistrados</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;LOT: Módulo de Lotação')}">
									<li><a href="${f:getURLSistema('/siga.sgp.lot')}">Lotação</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TRN: Módulo de Treinamento')}">
									<li><a href="${f:getURLSistema('/siga.sgp.trn')}">Treinamento</a>
									</li>
								</c:if>
								<c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TERC: Módulo de Terceirizados')}">
									<li><a href="${f:getURLSistema('/siga.sgp.terc')}">Terceirizados</a>
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
                
                	<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SE:Módulo de Servicos Externos')}">
						<li><a href="/sigade/">Dados Externos</a>
						</li>
					</c:if>
					
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GO:Gestão Orçamentária')}">
						<li><a href="/sigago/">Gestão Orçamentária</a>
						</li>
					</c:if>
					
				</ul>
			</li>
			<c:if test="${not empty f:resource('gsa.url')}">
			<li><a href="/siga/app/busca">Busca Textual</a></li>
			</c:if>
			<li><a href="#">Administração</a>
				<ul>
					<li><a href="/siga/app/usuario/trocar_senha" >Trocar senha</a>
					</li>
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
					 <c:when test="${(cadastrante.idPessoa != titular.idPessoa) || (cadastrante.lotacao.idLotacao != lotaTitular.idLotacao)}">
						<%-- é uma substituição --%>
						<c:if
							test="${f:podeCadastrarQqSubstituicaoPorConfiguracao(cadastrante, cadastrante.lotacao)}">
							<li><a
									href="${serverAndPort}/siga/app/substituicao/listar">Gerenciar possíveis substitutos</a>
							</li>
						</c:if>
					</c:when>
					<c:otherwise>
						<li><a
								href="${serverAndPort}/siga/app/substituicao/listar">Gerenciar possíveis substitutos</a>
						</li>
					</c:otherwise>
					</c:choose>
				</ul>
			</li>


			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade')}">
				<li><a href="#">Gestão de Identidade</a>
					<ul>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;ID:Gerenciar identidades')}">
							<li><a href="/siga/app/gi/identidade/listar">Identidade</a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERMISSAO:Gerenciar permissões')}">
							<li><a href="/siga/app/gi/acesso/listar">Configurar Permissões</a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFIL:Gerenciar perfis de acesso')}">
							<li><a href="/siga/app/gi/perfil/listar">Perfil de Acesso</a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;PERFILJEE:Gerenciar perfis do JEE')}">
							<li><a href="/siga/app/gi/perfilJEE/listar">Perfil de Acesso do JEE</a>
							</li>
						</c:if>
						<c:if 						
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR:Gerenciar grupos de distribuição')
							       || (f:podeGerirAlgumGrupo(titular,lotaTitular,2) && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;DELEG:Gerenciar grupos de distribuição delegados'))}"> 	
	 						<li><a
									href="${serverAndPort}/siga/app/gi/grupoDeEmail/listar">Grupo de Distribuição</a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;SELFSERVICE:Gerenciar serviços da própria lotação')}">
							<li><a href="/siga/app/gi/servico/editar">Acesso a Serviços</a>
							</li>
						</c:if>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;REL:Gerar relatórios')}">
							<li><a href="#">Relatórios</a>
								<ul>
									<li><a
											href="/siga/app/gi/relatorio/selecionar_acesso_servico">Acesso aos Serviços</a>
									</li>
									<li><a
											href="/siga/app/gi/relatorio/selecionar_permissao_usuario">Permissões de Usuário</a>
									</li>
									<li><a
											href="/siga/app/gi/relatorio/selecionar_alteracao_direitos">Alteração de Direitos</a>
									</li>
									<li><a
											href="/siga/app/gi/relatorio/selecionar_historico_usuario">Histórico de Usuário</a>
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
						<li><a href="/siga/app/modelo/listar">Cadastro de modelos</a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_ORGAO:Cadastrar Orgãos')}">
						<li><a href="/siga/app/orgao/listar">Cadastro de Orgãos Externos</a>
						</li>
					</c:if>
					<c:if
                        test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_ORGAO_USUARIO:Cadastrar Orgãos Usuário')}">
                        <li><a href="/siga/app/orgaoUsuario/listar">Cadastro de Orgãos</a>
                        </li>
                    </c:if>
                    <c:if
                        test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_CARGO:Cadastrar Cargo')}">
                        <li><a href="/siga/app/cargo/listar">Cadastro de Cargo</a>
                        </li>
                    </c:if>
                    <c:if
                        test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_LOTACAO:Cadastrar Lotação')}">
                        <li><a href="/siga/app/lotacao/listar">Cadastro de Lotação</a>
                        </li>
                    </c:if>
                    <c:if
                        test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_FUNCAO:Cadastrar Função de Confiança')}">
                        <li><a href="/siga/app/funcao/listar">Cadastro de Função de Confiança</a>
                        </li>
                    </c:if>
                    <c:if
                        test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_PESSOA:Cadastrar Pessoa')}">
                        <li><a href="/siga/app/pessoa/listar">Cadastro de Pessoa</a>
                        </li>
                    </c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;WF_ADMIN:Administrar SIGAWF')}">
						<li><a href="/sigawf/app/admin/administrar">Administrar SIGA WF</a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;LDAP_ADMIN:Administrar Integracao LDAP')}">
						<li><a href="/siga/app/admin/ldap/administrar">Administrar Integração LDAP</a>
						</li>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;FE;CAD_FERIADO:Cadastrar Feriados')}">
						<li><a href="/siga/app/feriado/listar">Cadastro de Feriados</a></li>
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
						<a href="/siga/app/substituicao/substituirGravar?id=${substituicao.idSubstituicao}">
							<c:choose>
								<c:when test="${not empty substituicao.titular}">
									${f:maiusculasEMinusculas(substituicao.titular.pessoaAtual.nomePessoa)}
								</c:when>
								<c:otherwise>
									${f:maiusculasEMinusculas(substituicao.lotaTitular.lotacaoAtual.nomeLotacao)}
								</c:otherwise>
							</c:choose> 
						</a>
					</li>
				</c:forEach>

			</ul>
		</li>

		<li>
			<c:choose>
				<c:when test="${not empty f:resource('siga.ex.manual.url')}">
						<a id="apostilaSiga" href="${f:resource('siga.ex.manual.url')}" target="_blank">Apostila SIGA-Doc</a>
				</c:when>
				<c:otherwise>
					<a id="apostilaSiga" href="/siga/arquivos/apostila_sigaexantesbs4.pdf" target="_blank">Apostila SIGA-Doc</a>
	
				</c:otherwise>
			</c:choose>
		</li>
		
		<li><a href="/siga/?GLO=true">Logoff</a>
		</li>

	</ul>
</li>
<!-- insert menu -->
<c:import url="/paginas/menus/menu.jsp"></c:import>
