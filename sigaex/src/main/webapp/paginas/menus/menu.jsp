<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<li><a href="#">Documentos</a>
	<ul>
		<li>
			<a href="/sigaex/app/expediente/doc/editar">Novo</a>
		</li>
		<li>
			<a href="/sigaex/app/expediente/doc/listar?primeiraVez=sim">Pesquisar</a>
		</li>

		<c:if test="${f:resource('siga.lucene.ativo')}">
			<li><ww:url id="url" action="full_search"
					namespace="/expediente/doc">
				</ww:url> <ww:a href="%{url}">Pesquisar por texto</ww:a>
			</li>
		</c:if>

		<li>
			<siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/transferir_lote"
				texto="Transferir em lote" />
		</li>
		<li> <siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/receber_lote"
				texto="Receber em lote" />
		</li>
		<li><siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/anotar_lote"
				texto="Anotar em lote" />
		</li>
		<c:catch>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
				<li><ww:url id="url" action="assinar_lote"
						namespace="/expediente/mov" /> <siga:monolink href="%{url}"
						texto="Assinar em lote" />
				</li>
			</c:if>
		</c:catch>
		<c:catch>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
				<li> <siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/assinar_despacho_lote"
						texto="Assinar Despacho em lote" />
				</li>
			</c:if>
		</c:catch>
		<li> <siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/arquivar_corrente_lote"
				texto="Arquivar em lote" />
		</li>
		<c:catch>
			<c:if
				test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
				<li> <siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/arquivar_intermediario_lote"
					texto="Arquivar Intermediário em lote" />
				</li>
			</c:if>
		</c:catch>
		<c:catch>
			<c:if
				test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
				<li> <siga:monolink href="${pageContext.request.contextPath}/app/expediente/mov/arquivar_permanente_lote"
					texto="Arquivar Permanente em lote" />
				</li>
			</c:if>
		</c:catch>
		<c:catch>
			<c:if
				test="${f:testaCompetencia('atenderPedidoPublicacao',titular,lotaTitular,null)}">
				<li><ww:url id="url" action="atender_pedido_publicacao"
						namespace="/expediente/mov" /> <ww:a href="%{url}">Gerenciar Publicação DJE</ww:a>
				</li>
			</c:if>
		</c:catch>
		<%--<c:catch>
			<c:if
				test="${f:testaCompetencia('definirPublicadoresPorConfiguracao',titular,lotaTitular,null)}">
				<li><ww:url id="url" action="definir_publicadores"
					namespace="/expediente/configuracao" /><ww:a href="%{url}">Definir Publicadores DJE</ww:a></li>
			</c:if>
		</c:catch>--%>
		<c:catch>
			<c:if
				test="${f:testaCompetencia('gerenciarPublicacaoBoletimPorConfiguracao',titular,lotaTitular,null)}">
				<li><ww:url id="url" action="gerenciar_publicacao_boletim"
						namespace="/expediente/configuracao" /> <ww:a href="%{url}">Definir Publicadores Boletim</ww:a>
				</li>
			</c:if>
		</c:catch>
	</ul>
</li>

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas')}">
	<li><a href="#">Ferramentas</a>
		<ul>
		    <li><siga:monolink href="${pageContext.request.contextPath}/app/forma/listar"
					texto="Cadastro de Formas" />
			</li>
			<li><ww:url id="url" action="listar" namespace="/modelo" /> <ww:a
					href="%{url}">Cadastro de modelos</ww:a>
			</li>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;DESP:Tipos de despacho')}">
				<li><ww:url id="url" action="listar"
						namespace="/despacho/tipodespacho" /> <ww:a href="%{url}">Cadastro de tipos de despacho</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;CFG:Configurações')}">
				<li><ww:url id="url" action="listar"
						namespace="/expediente/configuracao" /> <ww:a href="%{url}">Cadastro de configurações</ww:a>
				</li>
			</c:if>
			
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;EMAIL:Email de Notificação')}">
				<li><ww:url id="url" action="listar"
						namespace="/expediente/emailNotificacao" /> <ww:a href="%{url}">Cadastro de email de notificação</ww:a>
				</li>
			</c:if>
			
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação')}">
				<li><ww:url id="url" action="listar"
						namespace="/expediente/classificacao" /> <ww:a href="%{url}">Classificação Documental</ww:a>
				</li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade')}">
				<li><ww:url id="url" action="listar"
						namespace="/expediente/temporalidade" /> <ww:a href="%{url}">Temporalidade Documental</ww:a>
				</li>
			</c:if>
			
		</ul>
	</li>
</c:if>

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios')}">

	<li><a href="#">Relatórios</a>
		<ul id="relatorios" class="navmenu-large">
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;FORMS:Relação de formulários')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relFormularios.jsp">
						Relação de formulários
					</ww:a>
				</li>
			</c:if>

			<%-- Substituído pelo pelo "relConsultaDocEntreDatas"
		<li><ww:url id="url" action="relRelatorios"
				namespace="/expediente/rel">
				<ww:param name="nomeArquivoRel">relExpedientes.jsp</ww:param>
			</ww:url> <ww:a href="%{url}">Relatório de Expedientes</ww:a></li>  --%>


			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DATAS:Relação de documentos entre datas')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relConsultaDocEntreDatas.jsp">
						Relação de documentos entre datas
					</ww:a>
				</li>
			</c:if>
			<!-- 
			<li><ww:url id="url" action="relRelatorios"
				namespace="/expediente/rel">
				<ww:param name="nomeArquivoRel">relModelos.jsp</ww:param>
			</ww:url> <ww:a href="%{url}">Relatório de Modelos</ww:a></li>
	-->
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;SUBORD:Relatório de documentos em setores subordinados')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relDocumentosSubordinados.jsp">
						Relatório de Documentos em Setores Subordinados
					</ww:a>
				</li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MVSUB:Relatório de movimentação de documentos em setores subordinados')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovimentacaoDocSubordinados.jsp">
						Relatório de Movimentação de Documentos em Setores Subordinados
					</ww:a>
				</li>
			</c:if>
			
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELMVP:Relatório de movimentações de processos')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovProcesso.jsp">
						Relatório de Movimentações de Processos
					</ww:a>
				</li>
			</c:if>
			
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CRSUB:Relatório de documentos criados em setores subordinados')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relCrDocSubordinados.jsp">
						Relatório de Criação de Documentos em Setores Subordinados
					</ww:a>
				</li>
			</c:if>
			

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MOVLOT:Relação de movimentações')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovimentacao.jsp">
						Relatório de Movimentações
					</ww:a>
				</li>
			</c:if>
			
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MOVCAD:Relação de movimentações por cadastrante')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovCad.jsp">
						Relatório de Movimentações por Cadastrante
					</ww:a>
				</li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DSPEXP:Relação de despachos e transferências')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relOrgao.jsp">
						Relatório de Despachos e Transferências
					</ww:a>
				</li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DOCCRD:Relação de documentos criados')}">
				<li>
					<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relTipoDoc.jsp">
						Relação de Documentos Criados
					</ww:a>
				</li>
			</c:if>
			
			
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental')}">
				<li><a href="#">Classificação Documental</a>
					<ul>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental;CLASS:Relação de classificações')}">
							<li>
								<ww:a href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relClassificacao.jsp">
									Relação de Classificações
								</ww:a>
							</li>
						</c:if>
						
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental;DOCS:Relação de documentos classificados')}">
							<li> 
								<ww:a id="relclassificados" href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relDocsClassificados.jsp">
									Relação de Documentos Classificados
								</ww:a>
							</li>
						</c:if>
						
					</ul>		
				</li>
			</c:if>
			

		</ul></li>
</c:if>
