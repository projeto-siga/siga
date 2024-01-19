<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="fx"%>

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos') && (!fx:ehPublicoExterno(titular) || (fx:ehPublicoExterno(titular) && f:podeCriarNovoExterno(titular, titular.lotacao)))}">
	<li class="nav-item dropdown"><a href="javascript:void(0);"
		class="nav-link dropdown-toggle" data-toggle="dropdown">
			Documentos </a>
		<ul class="dropdown-menu">
			<li><a class="dropdown-item"
				href="/sigaex/app/expediente/doc/editar">Novo</a></li>
			<c:if test="${!ehPublicoExterno && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar')}">
                <li><a class="dropdown-item"
                    href="/sigaex/app/expediente/doc/listar?primeiraVez=sim">Pesquisar</a></li>
            </c:if> 
			<c:choose>
 				<c:when test="${!exibirMesaVirtualComoPadrao}">
				<li><a class="dropdown-item" href="/sigaex/app/mesa${fx:getMesaVersao(titular,lotaTitular)}">Mesa
					Virtual </a></li>
				 </c:when>	
				<c:otherwise>
    				<li><a class="dropdown-item" href="/siga/app/principal?exibirAcessoAnterior=false&&redirecionar=false">Quadro
					Quantitativo </a></li>
				</c:otherwise>
			</c:choose>	
			
			<c:if test="${not empty meusDelegados && f:podeDelegarVisualizacao(cadastrante, cadastrante.lotacao)}">
				<li class="dropdown-submenu"><a href="javascript:void(0);"
					class="dropdown-item dropdown-toggle">Mesa Virtual Delegada</a>
					<ul class="dropdown-menu navmenu-large">
						<c:forEach var="visualizacao" items="${meusDelegados}">
							<li><a class="dropdown-item"
								href="/siga/app/visualizacao/visualizacaoGravar?idVisualizacao=${visualizacao.idVisualizacao}">
											${f:maiusculasEMinusculas(visualizacao.titular.nomePessoa)}
							</a></li>
						</c:forEach>
		
					</ul></li>
			</c:if>
			<div class="dropdown-divider"></div>
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;TRALOTE:Tramitar em Lote')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/tramitar_lote"><fmt:message key="documento.transferencia.lote" /></a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;RECLOTE:Receber em Lote')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/receber_lote">Receber em Lote</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ANOLOTE:Anotar em Lote')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/anotar_lote">Anotar em Lote</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASSLOTE:Assinar em Lote')}">
					<c:choose>
						<c:when test="${siga_cliente == 'GOVSP'}">
							<li><a class="dropdown-item"
								href="/sigaex/app/expediente/mov/assinar_lote">Assinar em Lote</a></li>
						</c:when>
						<c:otherwise>
							<li><a class="dropdown-item"
								href="/sigaex/app/expediente/mov/assinar_tudo">Assinar em Lote</a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;RECLALOTE:Reclassificar em Lote')}">
					<li><a class="dropdown-item"
						   href="/sigaex/app/expediente/mov/reclassificar_lote">Reclassificar em Lote</a></li>
				</c:if>
			</c:catch>


		<!--  Retirado pois já não funcionava desta forma -->
			<!--  
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
					<span class="${hide_only_GOVSP}"><li><a
							class="dropdown-item"
							href="/sigaex/app/expediente/mov/assinar_despacho_lote">Assinar
								Despacho em lote</a></li></span>
				</c:if>
			</c:catch> -->
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ARQLOTE:Arquivar em Lote')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/arquivar_corrente_lote">Arquivar
							em Lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:podeArquivarIntermediarioPorConfiguracao(titular,lotaTitular)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/arquivar_intermediario_lote">Arquivar
							Intermediário em Lote</a></li>
				</c:if>
			</c:catch>
			
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;TRARQ: Transferência de Documentos Arquivados')}">
						<li><a class="dropdown-item"
								href="/sigaex/app/expediente/mov/transferir_doc_arquivado_lote">Transferência de Docs. Arquivado</a>
						</li>
				</c:if>
			</c:catch>
			
			<c:catch>
				<c:if
					test="${f:podeArquivarPermanentePorConfiguracao(titular,lotaTitular)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/arquivar_permanente_lote">Arquivar
							Permanente em Lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;DEFLOTE:Definir Acompanhamento em Lote')}"> 
						<li><a class="dropdown-item" href="/sigaex/app/expediente/mov/vincularPapelLote">Definir Acompanhamento em Lote</a></li>
				</c:if>
			</c:catch>
			<c:catch>
				<c:if
					test="${f:testaCompetencia('atenderPedidoPublicacaoNoDiario',titular,lotaTitular,null)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/mov/atender_pedido_publicacao">Gerenciar
							Publicação DJE</a></li>
				</c:if>
			</c:catch>

			<c:catch>
				<c:if
					test="${f:testaCompetencia('gerenciarPublicacaoNoBoletimPorConfiguracao',titular,lotaTitular,null)}">
					<li><a class="dropdown-item"
						href="/sigaex/app/configuracao/gerenciar_publicacao_boletim">Definir
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
						de Modelos</a></li>
				<c:if
					test="${false and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;DESP:Tipos de despacho')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/despacho/tipodespacho/listar">Cadastro de
							Tipos de Despacho</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;CFG:Configurações')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/configuracao/listar">Cadastro de Configurações</a></li>
								
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;EMAIL:Email de Notificação')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/emailNotificacao/listar">Cadastro
							de Email de Notificação</a></li>
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
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;FE:Ferramentas;PAINEL:Painel Administrativo')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/painel/exibir">Painel 
							Administrativo</a></li>
				</c:if>

			</ul></li>
	</c:if>

	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios')}">

		<li id="menuRelatorios" class="nav-item dropdown"><a href="javascript:void(0);"
			class="nav-link dropdown-toggle" data-toggle="dropdown">
				Relatórios </a>
			<ul id="subMenuRelatorios" class="dropdown-menu">
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
					test="${false and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELMVP:Relatório de movimentações de processos')}">
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
		<li id="menuGestao" class="nav-item dropdown"><a href="javascript:void(0);"
			class="nav-link dropdown-toggle" data-toggle="dropdown"> Gestão </a>
			<ul id="subMenuGestao" class="dropdown-menu">
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
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELFORAPRAZO:Relatório de documentos fora do prazo')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relDocumentosForaPrazo?primeiraVez=true">
							Documentos Fora do Prazo </a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELDEVPROGRAMADA:Relatório de documentos devolção programada')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relDocumentosDevolucaoProgramada?primeiraVez=true">
							Documentos por Devolução Programada </a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;RELTEMPOMEDIOSITUACAO:Tempo médio por Situação')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relTempoMedioSituacao?primeiraVez=true">
							Tempo médio por Situação </a></li>
				</c:if>

				<!-- 				<li><a class="dropdown-item" href="#"> Tempo médio -->
				<!-- 						Tramitação por Espécie Documental </a></li> -->

				<!-- 				<li><a class="dropdown-item" href="#"> Volume de Tramitação -->
				<!-- 						por Nome de documento </a></li> -->
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;ORGAOINT: Relatório de Documentos Por Órgão Interessado')}">
					<li><a class="dropdown-item"
						href="/sigaex/app/expediente/rel/relDocsOrgaoInteressado?primeiraVez=true">
							Total de documentos por Órgão Interessado</a></li>
				</c:if>
				<c:if
 					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;TRAMESP: Tempo Médio de Tramitação Por Espécie Documental')}">
					<li><a class="dropdown-item" 
						href="/sigaex/app/expediente/rel/relTempoTramitacaoPorEspecie?primeiraVez=true">
							Tempo Médio de Tramitação Por Espécie Documental</a></li>
				</c:if>
				<c:if
 					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;REL:Gerar relatórios;VOLTRAMMOD: Volume de Tramitação Por Nome do Documento')}">
					<li><a class="dropdown-item" 
						href="/sigaex/app/expediente/rel/relVolumeTramitacaoPorModelo?primeiraVez=true">
							Volume de Tramitação Por Nome do Documento</a></li>
				</c:if>
			</ul>
		</li>
	</c:if>
	<script type="text/javascript" language="Javascript1.1">
		ulRel = document.getElementById('subMenuGestao');
		if (ulRel !== null && ulRel.children.length == 0) {
			$('#menuGestao').addClass('d-none');
		}
		ulRel = document.getElementById('subMenuRelatorios');
		if (ulRel !== null && ulRel.children.length == 0) { 
			$('#menuRelatorios').addClass('d-none');
		}
	</script>
</c:if>