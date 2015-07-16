<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
<li><a href="#">Documentos</a>
	<ul>
		<li><a href="/sigaex/app/expediente/doc/editar">Novo</a></li>
		<li><a href="/sigaex/app/expediente/doc/listar?primeiraVez=sim">Pesquisar</a>
		</li>

		<li><siga:monolink
				href="${pageContext.request.contextPath}/app/expediente/mov/transferir_lote"
				texto="Transferir em lote" /></li>
		<li><siga:monolink
				href="${pageContext.request.contextPath}/app/expediente/mov/receber_lote"
				texto="Receber em lote" /></li>
		<li><siga:monolink
				href="${pageContext.request.contextPath}/app/expediente/mov/anotar_lote"
				texto="Anotar em lote" /></li>
			<li><siga:monolink
					href="${pageContext.request.contextPath}/app/expediente/mov/assinar_tudo"
					texto="Assinar Documentos, Despachos e Anexos" /></li>
		<c:catch>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
				<li><siga:monolink
						href="${pageContext.request.contextPath}/app/expediente/mov/assinar_lote"
						texto="Assinar em lote" /></li>
			</c:if>
		</c:catch>
		<c:catch>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
				<li><siga:monolink
						href="${pageContext.request.contextPath}/app/expediente/mov/assinar_despacho_lote"
						texto="Assinar Despacho em lote" /></li>
			</c:if>
		</c:catch>
		<li><siga:monolink
				href="${pageContext.request.contextPath}/app/expediente/mov/arquivar_corrente_lote"
				texto="Arquivar em lote" /></li>
		<c:catch>
			<c:if
				test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
				<li><siga:monolink
						href="${pageContext.request.contextPath}/app/expediente/mov/arquivar_intermediario_lote"
						texto="Arquivar Intermediário em lote" /></li>
			</c:if>
		</c:catch>
		<c:catch>
			<c:if
				test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
				<li><siga:monolink
						href="${pageContext.request.contextPath}/app/expediente/mov/arquivar_permanente_lote"
						texto="Arquivar Permanente em lote" /></li>
			</c:if>
		</c:catch>
		<c:catch>
			<c:if
				test="${f:testaCompetencia('atenderPedidoPublicacao',titular,lotaTitular,null)}">
				<li><siga:monolink
						href="${pageContext.request.contextPath}/app/expediente/mov/atender_pedido_publicacao"
						texto="Gerenciar Publicação DJE" /></li>
			</c:if>
		</c:catch>

		<c:catch>
			<c:if
				test="${f:testaCompetencia('gerenciarPublicacaoBoletimPorConfiguracao',titular,lotaTitular,null)}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/configuracao/gerenciar_publicacao_boletim">Definir
						Publicadores Boletim</a></li>
			</c:if>
		</c:catch>
	</ul></li>

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas')}">
	<li><a href="#">Ferramentas</a>
		<ul>
			<li><siga:monolink
					href="${pageContext.request.contextPath}/app/forma/listar"
					texto="Cadastro de Espécies" /></li>
			<li><a href="/sigaex/app/modelo/listar">Cadastro de modelos</a>
			</li>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;DESP:Tipos de despacho')}">
				<li><a href="/sigaex/app/despacho/tipodespacho/listar">Cadastro
						de tipos de despacho</a></li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;CFG:Configurações')}">
				<li><a href="/sigaex/app/expediente/configuracao/listar">Cadastro
						de configurações</a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;EMAIL:Email de Notificação')}">
				<li><a href="/sigaex/app/expediente/emailNotificacao/listar">Cadastro
						de email de notificação</a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação')}">
				<li><a href="/sigaex/app/expediente/classificacao/listar">Classificação
						Documental</a></li>
			</c:if>
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/temporalidade/listar">Temporalidade
						Documental</a></li>
			</c:if>

		</ul></li>
</c:if>

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios')}">

	<li><a href="#">Relatórios</a>
		<ul id="relatorios" class="navmenu-large">
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;FORMS:Relação de formulários')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relFormularios.jsp">
						Relação de formulários </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DATAS:Relação de documentos entre datas')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relConsultaDocEntreDatas.jsp">
						Relação de documentos entre datas </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;SUBORD:Relatório de documentos em setores subordinados')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relDocumentosSubordinados.jsp">
						Relatório de Documentos em Setores Subordinados </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MVSUB:Relatório de movimentação de documentos em setores subordinados')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovimentacaoDocSubordinados.jsp">
						Relatório de Movimentação de Documentos em Setores Subordinados </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELMVP:Relatório de movimentações de processos')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovProcesso.jsp">
						Relatório de Movimentações de Processos </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CRSUB:Relatório de documentos criados em setores subordinados')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relCrDocSubordinados.jsp">
						Relatório de Criação de Documentos em Setores Subordinados </a></li>
			</c:if>


			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MOVLOT:Relação de movimentações')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovimentacao.jsp">
						Relatório de Movimentações </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MOVCAD:Relação de movimentações por cadastrante')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovCad.jsp">
						Relatório de Movimentações por Cadastrante </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DSPEXP:Relação de despachos e transferências')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relOrgao.jsp">
						Relatório de Despachos e Transferências </a></li>
			</c:if>

			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DOCCRD:Relação de documentos criados')}">
				<li><a
					href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relTipoDoc.jsp">
						Relação de Documentos Criados </a></li>
			</c:if>


			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental')}">
				<li><a href="#">Classificação Documental</a>
					<ul>
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental;CLASS:Relação de classificações')}">
							<li><a
								href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relClassificacao.jsp">
									Relação de Classificações </a></li>
						</c:if>

						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental;DOCS:Relação de documentos classificados')}">
							<li><a id="relclassificados"
								href="${pageContext.request.contextPath}/app/expediente/rel/relRelatorios?nomeArquivoRel=relDocsClassificados.jsp">
									Relação de Documentos Classificados </a></li>
						</c:if>

					</ul></li>
			</c:if>


		</ul></li>
</c:if>
</c:if>