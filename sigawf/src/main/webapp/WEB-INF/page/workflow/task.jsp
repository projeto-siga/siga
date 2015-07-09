<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="Tarefa">
	<div class="gt-bd gt-cols clearfix" id="page">
		<div class="gt-content clearfix">

			<c:if
				test="${not f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
				<div id="desc_editar" style="display: none;">
					<h3>Descrição da Tarefa</h3>
					<div class="gt-form gt-content-box">
						<ww:url action="saveKnowledge" id="url"></ww:url>
						<form method="POST" action="${url}">
							<input name="tiId" type="hidden" value="${tiId}" />
							<div class="gt-form-row gt-width-100">
								<label>Descrição</label>
								<textarea cols="80" rows="15" name="conhecimento"
									class="gt-form-textarea">${task.conhecimento}</textarea>
							</div>
							<div class="gt-form-row gt-width-100">
								<input name="salvar_conhecimento" type="submit" value="Salvar"
									class="gt-btn-medium gt-btn-left" /> <a
									href="javascript: window.location.reload()"
									class="gt-btn-medium gt-btn-left">Cancelar</a>
							</div>
						</form>
					</div>
				</div>
			</c:if>

			<!-- Adicionando a lista de Tarefas -->
			<ww:url action="executeTask" id="url"></ww:url>
			<form method="POST" action="${url}">
				<h3>Execução da Tarefa</h3>
				<div class="gt-form gt-content-box">
					<div style="margin:10px;" >
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
							<span id="gc-ancora"></span>
						</c:if>


						<c:if
							test="${not f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
							<span id="desc_ver"> <c:choose>
									<c:when test="${not empty task.descricao}">
										<c:if test="${task.conhecimentoEditavel}">
											<a
												style="float: right; margin-left: 15px; margin-bottom: 15px;"
												title="Editar a descrição"
												href="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display='';"><img
												src="/siga/css/famfamfam/icons/pencil.png"> </a>
										</c:if>${task.descricao}</c:when>
									<c:otherwise>Ainda não existe uma descrição de como esta tarefa deve ser executada. Por favor, clique <a
											href="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display='';">aqui</a> para contribuir.</c:otherwise>
								</c:choose> </span>
						</c:if>
					</div>
					<div class="gt-form-row gt-width-100">
						<table class="gt-form-table">
							<input type="hidden" value="${task.taskInstance.id}" name="tiId" />

							<c:forEach var="variable" items="${task.variableList}">
								<c:if test="${not variable.aviso}">
									<tr>
										<ww:if test="%{#attr.variable.mappedName.startsWith('sel_')}">
											<td width="">${fn:substring(variable.variableName,0,fn:indexOf(variable.variableName,'('))}</td>
										</ww:if>
										<ww:else>
											<td width="">${variable.variableName}</td>
										</ww:else>

										<td width=""><c:set var="editable"
												value="${variable.writable and (variable.readable or empty taskInstance.token.processInstance.contextInstance.variables[variable.mappedName])}" />
											<c:if test="${editable}">
												<input name="fieldNames" type="hidden"
													value="${variable.mappedName}" />
											</c:if> <ww:if
												test="%{#attr.variable.mappedName.startsWith('doc_') or #attr.variable.mappedName.startsWith('(doc_')}">
												<c:choose>
													<c:when test="${editable}">
														<siga:selecao propriedade="${variable.mappedName}"
															modulo="../sigaex" tipo="expediente" tema="simple"
															ocultardescricao="sim"
															siglaInicial="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
													</c:when>
													<c:otherwise>
														<a
															href="/sigaex/expediente/doc/exibir.action?sigla=${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}">${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}</a>
													</c:otherwise>
												</c:choose>
											</ww:if> <ww:elseif
												test="%{#attr.variable.mappedName.startsWith('pes_')}">
												<c:choose>
													<c:when test="${editable}">
														<siga:selecao propriedade="${variable.mappedName}"
															modulo="../sigaex" tipo="pessoa" tema="simple"
															ocultardescricao="sim"
															siglaInicial="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
													</c:when>
													<c:otherwise>
									${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
								</c:otherwise>
												</c:choose>
											</ww:elseif> <ww:elseif
												test="%{#attr.variable.mappedName.startsWith('lot_')}">
												<c:choose>
													<c:when test="${editable}">
														<siga:selecao propriedade="${variable.mappedName}"
															modulo="../sigaex" tipo="lotacao" tema="simple"
															ocultardescricao="sim"
															siglaInicial="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
													</c:when>
													<c:otherwise>
									${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
								</c:otherwise>
												</c:choose>
											</ww:elseif> <ww:elseif
												test="%{#attr.variable.mappedName.startsWith('dt_')}">
												<c:choose>
													<c:when test="${editable}">
														<input name="fieldValues" type="text"
															value="<fmt:formatDate pattern="dd/MM/yyyy"	value="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />"
															onblur="javascript:verifica_data(this, true);" />
													</c:when>
													<c:otherwise>
														<fmt:formatDate pattern="dd/MM/yyyy"
															value="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
													</c:otherwise>
												</c:choose>
											</ww:elseif> <ww:elseif
												test="%{#attr.variable.mappedName.startsWith('sel_')}">
												<c:choose>
													<c:when test="${editable}">
														<select name="fieldValues">
															<c:forEach var="opcao"
																items="${wf:listarOpcoes(variable.variableName)}">
																<option value="${opcao}">${opcao}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
										${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
									</c:otherwise>
												</c:choose>
											</ww:elseif> <ww:else>
												<c:choose>
													<c:when test="${editable}">
														<input name="fieldValues" type="text"
															value="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
													</c:when>
													<c:otherwise>
									${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
									</c:otherwise>
												</c:choose>
											</ww:else></td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</div>
					<c:if
						test="${(titular.sigla eq taskInstance.actorId) or (wf:podePegarTarefa(cadastrante, titular,lotaCadastrante,lotaTitular,taskInstance) == true)}">
						<div class="gt-form-row gt-width-100">
							<c:forEach var="transition" items="${task.transitions}">
								<input name="transitionName" type="submit"
									value="${empty transition.name ? 'Prosseguir' : transition.name}${transition.resp}"
									class="gt-btn-large gt-btn-left" />
							</c:forEach>
						</div>
					</c:if>
					<c:forEach var="variable" items="${task.variableList}">
						<ww:if test="%{#attr.variable.mappedName.startsWith('doc_')}">
							<!-- 							<c:if test="${variable.aviso}">  -->
							<!-- 							</c:if>	-->
							<span style="color: red; font-weight: bold;">
								${task.msgAviso}</span>
						</ww:if>
					</c:forEach>
				</div>
			</form>
			<c:if
				test="${(titular.sigla eq taskInstance.actorId) or (wf:podePegarTarefa(cadastrante, titular,lotaCadastrante,lotaTitular,taskInstance))}">
				<h3 class="gt-form-head">Designaçao da Tarefa</h3>
				<div class="gt-form gt-content-box">
					<ww:url id="url" action="assignTask" />
					<form method="POST" action="${url}">
						<input name="tiId" type="hidden" value="${tiId}" />
						<div class="gt-form-row gt-width-100">
							<label>Pessoa</label>
							<siga:selecao propriedade="ator" modulo="../sigaex" tema="simple" />
						</div>
						<div class="gt-form-row gt-width-100">

							<label>Lotação</label>
							<siga:selecao propriedade="lotaAtor" modulo="../sigaex"
								tema="simple" />
						</div>
						<div class="gt-form-row gt-width-33" style="float: left">
							<label>Prioridade</label>
							<ww:select name="prioridade"
								list="#{1:'Muito Alta', 2:'Alta', 3:'Média', 4:'Baixa', 5:'Muito Baixa'}"
								theme="simple" />
						</div>
						<div class="gt-form-row gt-width-66" style="float: right">
							<label>Justificativa (opcional)</label> <input type="text"
								name="justificativa" style="width: 100%" />
						</div>
						<div class="gt-form-row gt-width-100" style="clear: both">
							<input name="designar" type="submit" value="Designar"
								class="gt-btn-medium gt-btn-left" />
							<c:if test="${empty taskInstance.actorId}">
								<ww:url id="url" action="pegar">
									<ww:param name="tiId">${taskInstance.id}</ww:param>
								</ww:url>
								<input type="button" value="Pegar tarefa para mim"
									onclick="javascript:window.location.href='${url}'"
									class="gt-btn-large gt-btn-left">
							</c:if>
						</div>
					</form>
				</div>

				<h3>Comentários</h3>
				<div class="gt-content-box">
					<table class="gt-table">
						<thead>
							<th>Data/Hora</th>
							<th>Atendente</th>
							<th>Descrição</th>
						</thead>
						<c:forEach var="ti" items="${wf:ordenarTarefas(taskInstance)}">
							<c:forEach var="c" items="${wf:ordenarComentarios(ti)}">
								<tr>
									<td>${f:espera(c.time)}</td>
									<td><ww:property value="%{#attr.c.actorId}" /></td>
									<td><ww:property value="%{#attr.c.message}" /></td>
								</tr>
							</c:forEach>
							<tr>
								<td>${f:espera(ti.create)}</td>
								<td><ww:property value="%{#attr.ti.actorId}" /></td>
								<td><b><ww:property value="%{#attr.ti.name}" /> </b></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="gt-form gt-content-box">
					<ww:url id="url" action="commentTask" />
					<form method="POST" action="${url}">
						<input name="tiId" type="hidden" value="${tiId}" /> <label>Comentário</label>
						<div class="gt-form-row gt-width-100">
							<input type="text" size="80" name="comentario"
								class="gt-form-text" />
						</div>
						<div class="gt-form-row gt-width-100">
							<input name="butc" type="submit" value="Adicionar"
								class="gt-btn-medium gt-btn-left" />
						</div>
					</form>
				</div>
			</c:if>
		</div>

		<div class="gt-sidebar">
			<!-- Sidebar Content -->
			<div class="gt-sidebar-content">
				<h3>Dados da Tarefa</h3>
				<p>
					<b>Procedimento:</b> ${taskInstance.task.processDefinition.name}
				</p>
				<p>
					<b>Tarefa:</b> ${taskInstance.task.name}
				</p>
				<p>
					<b>Prioridade:</b>
					<c:choose>
						<c:when test="${taskInstance.priority == 1}">Muito Alta</c:when>
						<c:when test="${taskInstance.priority == 2}">Alta</c:when>
						<c:when test="${taskInstance.priority == 3}">Média</c:when>
						<c:when test="${taskInstance.priority == 4}">Baixa</c:when>
						<c:when test="${taskInstance.priority == 51}">Muito Baixa</c:when>
					</c:choose>
				</p>
				<p>
					<b>Cadastrante:</b>
					<ww:property
						value="%{#attr.taskInstance.getVariable('wf_cadastrante')}" />
					(
					<ww:property
						value="%{#attr.taskInstance.getVariable('wf_lota_cadastrante')}" />
					)
				</p>
				<p>
					<b>Titular:</b>
					<ww:property
						value="%{#attr.taskInstance.getVariable('wf_titular')}" />
					(
					<ww:property
						value="%{#attr.taskInstance.getVariable('wf_lota_titular')}" />
					)
				</p>
				<p>
					<b>Início:</b> ${f:espera(taskInstance.create)}
				</p>
			</div>
			<!-- /Sidebar Content -->
			<%--
			<!-- Sidebar Navigation -->
			<div class="gt-sidebar-nav gt-sidebar-nav-brown">
				<h3>Quick Links</h3>
				<ul>
					<li><a href="http://www.gooeytemplates.com" target="_blank">GooeyTemplates.com</a>
					</li>
				</ul>
			</div>
			<!-- /Sidebar Navigation -->

			<!-- Sidebar Navigation -->
			<div class="gt-sidebar-nav gt-sidebar-nav-green">
				<h3>Contact Us</h3>
				<ul>
					<li><a href="mailto:help@gooeytemplates.com">Email Gooey
							Templates</a></li>
				</ul>
			</div>
			<!-- /Sidebar Navigation -->

			<!-- Sidebar Box -->
			<div class="gt-sidebar-box gt-sidebar-box-green">
				<!-- search -->
				<div class="gt-search">
					<div class="gt-search-inner">
						<input type="text" class="gt-search-text" value="Find an Article"
							onfocus="javascript:if(this.value=='Find an Article')this.value='';"
							onblur="javascript:if(this.value=='')this.value='Find an Article';" />
					</div>
				</div>
				<!-- /search -->
			</div>
			<!-- /Sidebar Box -->
--%>
			
			<!-- Sidebar List -->
			<div class="gt-sidebar-list">
				<h3>Mapa do Procedimento</h3>
				<div style="display: none" id="input">
					digraph G {
						graph[size="3,3", rankdir="LR"];
						<ww:property value="dot" escape="false"/>
					}
				</div>
				<a
						onclick="javascript: document.getElementById('page').style.display='none'; document.getElementById('svg').style.display='block'; bigmap();"
						title="Zoom" href="#">
				<div class="gt-sidebar-list-content" id="output">
				</div>
				</a>
			</div>
			
			<!-- /Sidebar List -->

			<div class="gt-sidebar-content" id="gc"></div>

		</div>
		<!-- / sidebar -->
	</div>

	<div class="gt-bd clearfix" style="display: none" id="map">
		<div class="gt-content clearfix">
			<div id="desc_editar">
				<h3>Mapa do Procedimento</h3>
				<div class="gt-form gt-content-box" style="padding-bottom: 15px;">
					<img style="width: 100%" src="loadPhoto?tId=${taskInstance.id}" />
				</div>
			</div>
		</div>
		<a href="task.action?tiId=${taskInstance.id}"
			class="gt-btn-large gt-btn-left">Voltar</a>

	</div>
	
	<div class="gt-bd clearfix" style="display: none" id="svg">
		<div class="gt-content clearfix">
			<div id="desc_editar">
				<h3>Mapa do Procedimento</h3>
				<div style="display: none" id="input2">
					digraph G {
						graph[size="100,100", rankdir="LR"];
						<ww:property value="dot" escape="false"/>
					}
				</div>
				
				<div class="gt-form gt-content-box" style="padding-bottom: 15px;" id="output2">
				</div>
			</div>
		</div>
		<a href="task.action?tiId=${taskInstance.id}"
			class="gt-btn-large gt-btn-left">Voltar</a>

	</div>

	

	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">

		<c:url var="url" value="/../sigagc/app/knowledge">
			<c:param name="tags">${task.ancora}</c:param>
			<c:param name="estilo">inplace</c:param>
			<c:param name="msgvazio">Ainda não existe uma descrição de como esta tarefa deve ser executada. Por favor, clique <a href="$1">aqui</a> para contribuir.</c:param>
			<c:param name="titulo">${taskInstance.task.processDefinition.name} - ${taskInstance.task.name}</c:param>
			<c:param name="ts">${currentTimeMillis}</c:param>
		</c:url>
		<script type="text/javascript">
	SetInnerHTMLFromAjaxResponse("${url}",document.getElementById('gc-ancora'));
	</script>

		<c:url var="url" value="/../sigagc/app/knowledge">
			<c:param name="tags">@workflow</c:param>
			<c:forEach var="tag" items="${task.tags}">
				<c:param name="tags">${tag}</c:param>
			</c:forEach>
			<c:param name="estilo">sidebar</c:param>
			<c:param name="ts">${currentTimeMillis}</c:param>
		</c:url>
		<script type="text/javascript">
	SetInnerHTMLFromAjaxResponse("${url}",document.getElementById('gc'));
	</script>
	</c:if>
	
	<!-- <script src="/siga/javascript/viz.js" language="JavaScript1.1" type="text/javascript"></script>  -->
	<!-- <script src="http://mdaines.github.io/viz.js/viz.js"></script>-->
	<script src="https://rawgit.com/mdaines/viz.js/gh-pages/viz.js"></script>
	
	<script>
		function bigmap() {
			var input = 'digraph G { graph[size="100,100"]; <ww:property value="dot" escape="false"/> }';
			var format = "svg";
			var engine = "dot";
			
			var result = Viz(input, format, engine);
			
			if (format == "svg") {
			  document.getElementById("output2").innerHTML = result;
			} else {
			  document.getElementById("output2").innerHTML = "";
			}
			$(document).ready(function () {
			    updateContainer();
			});
		}
		
		function smallmap() {
			var input = 'digraph G { graph[size="3,3"]; <ww:property value="dot" escape="false"/> }';
			var format = "svg";
			var engine = "dot";
			
			var result = Viz(input, format, engine);
			
			if (format == "svg") {
			  document.getElementById("output").innerHTML = result;
			} else {
			  document.getElementById("output").innerHTML = "";
			}
		}

		smallmap();

		$(document).ready(function () {
		    updateContainer();
		    $(window).resize(function() {
		        updateContainer();
		    });
		});
		function updateContainer() {
		    var smallwidth = $('#output').width(); 
		    var smallsvg = $('#output :first-child').first(); 
		    var smallviewbox = smallsvg.attr('viewBox');
		      
		    if(typeof smallviewbox != 'undefined') {
			    var a = smallviewbox.split(' ');  
	
			    // set attrs and 'resume' force 
			    smallsvg.attr('width', smallwidth);
			    smallsvg.attr('height', smallwidth * a[3] / a[2]);
		    }
		    
		    var bigwidth = $('#output2').width(); 
		    var bigsvg = $('#output2 :first-child').first(); 
		    var bigviewbox = bigsvg.attr('viewBox');
		      
		    if(typeof bigviewbox != 'undefined') {
			    var a = bigviewbox.split(' ');  
	
			    // set attrs and 'resume' force 
			    bigsvg.attr('width', bigwidth);
			    bigsvg.attr('height', bigwidth * a[3] / a[2]);
		    }
		}
    </script>
	
</siga:pagina>