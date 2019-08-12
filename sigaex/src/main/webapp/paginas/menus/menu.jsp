<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
	<li class="nav-item dropdown"><a href="javascript:void(0);"
		class="nav-link dropdown-toggle" data-toggle="dropdown">
			Documentos </a>
		<ul class="dropdown-menu">
			<li><a class="dropdown-item"
				href="/sigaex/app/expediente/doc/editar">Novo</a></li>
			<li><a class="dropdown-item"
				href="/sigaex/app/expediente/doc/listar?primeiraVez=sim">Pesquisar</a></li>

			<li><a class="dropdown-item" href="/sigaex/app/mesa">Mesa
					Virtual</a></li>

			<div class="dropdown-divider"></div>
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;TRAMITE:Trâmite;LOTE:Em Lote')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/transferir_lote">Transferir
							em lote</a></li>
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/receber_lote">Receber em lote</a></li>
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/anotar_lote">Anotar em lote</a></li>
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/assinar_tudo">Assinar
							Documentos, Despachos e Anexos</a></li>
				</c:if>
			</c:catch>


			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/assinar_lote">Assinar em lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
					<span class="${hide_only_GOVSP}"><li><a
							class="dropdown-item"
							href="/sigaex/app/expediente/mov/assinar_despacho_lote">Assinar
								Despacho em lote</a></li></span>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;TRAMITE:Trâmite;LOTE:Em Lote')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/arquivar_corrente_lote">Arquivar
							em lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/arquivar_intermediario_lote">Arquivar
							Intermediário em lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/arquivar_permanente_lote">Arquivar
							Permanente em lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:testaCompetencia('atenderPedidoPublicacao',titular,lotaTitular,null)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/atender_pedido_publicacao">Gerenciar
							Publicação DJE</a></li>
				</c:if>
			</c:catch>

			<c:catch>
				<c:if
					test="${f:testaCompetencia('gerenciarPublicacaoBoletimPorConfiguracao',titular,lotaTitular,null)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/configuracao/gerenciar_publicacao_boletim">Definir
							Publicadores Boletim</a></li>
				</c:if>
			</c:catch>
		</ul></li>

	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas')}">

		<li class="nav-item dropdown"><a href="javascript:void(0);"
			class="nav-link dropdown-toggle" data-toggle="dropdown">
				Ferramentas </a>
			<ul class="dropdown-menu">

				<li><a class="dropdown-item" href="/sigaex/app/forma/listar">Cadastro
						de Espécies</a></li>
				<li><a class="dropdown-item" href="/sigaex/app/modelo/listar">Cadastro
						de modelos</a></li>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;DESP:Tipos de despacho')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/despacho/tipodespacho/listar">Cadastro de
							tipos de despacho</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;CFG:Configurações')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/configuracao/listar">Cadastro de
							configurações</a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;EMAIL:Email de Notificação')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/emailNotificacao/listar">Cadastro
							de email de notificação</a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/classificacao/listar">Classificação
							Documental</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/temporalidade/listar">Temporalidade
							Documental</a></li>
				</c:if>

			</ul></li>
	</c:if>

	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios')}">

		<li class="nav-item dropdown"><a href="javascript:void(0);"
			class="nav-link dropdown-toggle" data-toggle="dropdown">
				Relatórios </a>
			<ul class="dropdown-menu">
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;FORMS:Relação de formulários')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relFormularios.jsp">
							Relação de formulários </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DATAS:Relação de documentos entre datas')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relConsultaDocEntreDatas.jsp">
							Relação de documentos entre datas </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;SUBORD:Relatório de documentos em setores subordinados')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relDocumentosSubordinados.jsp">
							Relatório de Documentos em Setores Subordinados </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MVSUB:Relatório de movimentação de documentos em setores subordinados')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovimentacaoDocSubordinados.jsp">
							Relatório de Movimentação de Documentos em Setores Subordinados </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELMVP:Relatório de movimentações de processos')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovProcesso.jsp">
							Relatório de Movimentações de Processos </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CRSUB:Relatório de documentos criados em setores subordinados')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relCrDocSubordinados.jsp">
							Relatório de Criação de Documentos em Setores Subordinados </a></li>
				</c:if>


				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MOVLOT:Relação de movimentações')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovimentacao.jsp">
							Relatório de Movimentações </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;MOVCAD:Relação de movimentações por cadastrante')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relMovCad.jsp">
							Relatório de Movimentações por Cadastrante </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DSPEXP:Relação de despachos e transferências')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relOrgao.jsp">
							Relatório de Despachos e Transferências </a></li>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;DOCCRD:Relação de documentos criados')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relTipoDoc.jsp">
							Relação de Documentos Criados </a></li>
				</c:if>


				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental')}">
					<li class="dropdown-submenu"><a href="javascript:void(0);"
						class="dropdown-item dropdown-toggle">Classificação Documental</a>
						<ul class="dropdown-menu">
							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental;CLASS:Relação de classificações')}">
								<li><a class="dropdown-item"
									href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relClassificacao.jsp">
										Relação de Classificações </a></li>
							</c:if>

							<c:if
								test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;CLSD:Classificação Documental;DOCS:Relação de documentos classificados')}">
								<li><a class="dropdown-item" id="relclassificados"
									href="/sigaex/app/expediente/rel/relRelatorios?nomeArquivoRel=relDocsClassificados.jsp">
										Relação de Documentos Classificados </a></li>
							</c:if>

						</ul></li>
				</c:if>


			</ul></li>
	</c:if>

	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios')}">
		<li class="nav-item dropdown"><a href="javascript:void(0);"
			class="nav-link dropdown-toggle" data-toggle="dropdown"> Gestão </a>
			<ul class="dropdown-menu">
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;IGESTAO: Relatório de Indicadores de Gestão')}">
					<li><a class="dropdown-item" 
						href="/sigaex/app/expediente/rel/relIndicadoresGestao?primeiraVez=true">
							Indicadores de Gestão</a></li>
				</c:if>
				<c:if
 					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELDOCVOL: Relatório de documentos por volume')}">
					<li><a class="dropdown-item"
 						href="/sigaex/app/expediente/rel/relDocumentosPorVolume?primeiraVez=true">
 							Documentos por Volume </a></li>
 				</c:if>
<!-- 				<li><a class="dropdown-item" href="#"> Documentos Fora do -->
<!-- 						Prazo </a></li> -->

<!-- 				<li><a class="dropdown-item" href="#"> Documentos por -->
<!-- 						Devolução Programada </a></li> -->

<!-- 				<li><a class="dropdown-item" href="#"> Tempo médio por -->
<!-- 						Situação </a></li> -->

<!-- 				<li><a class="dropdown-item" href="#"> Tempo médio -->
<!-- 						Tramitação por Espécie Documental </a></li> -->

<!-- 				<li><a class="dropdown-item" href="#"> Volume de Tramitação -->
<!-- 						por Nome de documento </a></li> -->
<!-- 				<li><a class="dropdown-item"  -->
<!-- 					href="/sigaex/app/expediente/rel/relDocsOrgaoInteressado?primeiraVez=true"> -->
<!-- 						Total de documentos por Órgão Interessado</a></li> -->
			</ul>
		</li>
	</c:if>

</c:if>