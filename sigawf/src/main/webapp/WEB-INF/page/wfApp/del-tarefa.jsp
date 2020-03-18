<%@ include file="/WEB-INF/page/include.jsp"%>
<c:choose>
	<c:when test="${not empty ajax}">
		<div class="gt-content-box"
			style="padding: 0px; background-color: #f9fafc; margin-bottom: 3px;">
			<table class="gt-form-table">
				<tr class="header">
					<td colspan="2">Procedimento: <span
						style="font-weight: normal;">${taskInstance.task.processDefinition.name}</span><span
						style="text-align: right;">&nbsp(${f:espera(taskInstance.create)})</span><br />
						Tarefa: <a
						href="${linkTo[WfAppController].task[task.taskInstance.id]}"><span
							style="font-weight: normal;">${task.taskInstance.task.name}</span>
					</a> <c:if test="${not empty taskInstance.actorId}">
						sendo atendida por <span style="font-weight: normal;">${taskInstance.actorId}</span>
						</c:if> <c:if test="${empty taskInstance.actorId}">
						aguardando atendimento
						<c:if
								test="${wf:podePegarTarefa(cadastrante, titular,lotaCadastrante,lotaTitular,taskInstance) == true}">
								<a
									href="${linkTo[WorkflowController].pegar}?tiId=${taskInstance.id}">(pegar)</a>
							</c:if>
						</c:if></td>
				</tr>
				</c:when>
				<c:otherwise>
					<table class="form" width="100%">
						<tr class="header">
							<td colspan="2">Execução da Tarefa</td>
						</tr>
						</c:otherwise>
						</c:choose>

						<input type="hidden" value="${task.taskInstance.id}" name="tiId" />
						<tr>
							<td <c:if test="${task.conhecimentoEditavel}">rowspan="2"</c:if>>Descrição</td>
							<td><span id="desc_ver">${task.descricao}</span> <c:if
									test="${task.conhecimentoEditavel}">
									<span id="desc_editar" style="display: none"><textarea
											cols="80" rows="15" name="conhecimento">${task.conhecimento}</textarea>
									</span>
								</c:if></td>
						</tr>
						<c:if test="${task.conhecimentoEditavel}">
							<tr>
								<td><input name="editar_conhecimento" type="button"
									value="Editar a descrição" id="desc_but_editar"
									onclick="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display=''; " />
									<input name="salvar_conhecimento" type="submit"
									id="desc_but_gravar" value="Salvar" style="display: none"
									onclick="javascript: this.form.action='${linkTo[WorkflowController].saveKnowledge}'; " /></td>
							</tr>
						</c:if>

						<c:set var="fieldIndex" value="0" />
						<c:set var="editable" value="${false}" />
						<c:forEach var="variable" items="${task.variableList}">
							<c:if test="${not variable.aviso}">
								<tr>
									<!-- ${fieldIndex} -->
									<c:if
										test="${(empty doc_ref) or (not (doc_ref eq taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]))}">
										<c:choose>
											<c:when test="${fn:startsWith(variable.mappedName,'sel_')}">
												<td width="">${fn:substring(variable.variableName,0,fn:indexOf(variable.variableName,'('))}</td>
											</c:when>
											<c:otherwise>
												<td width="">${variable.variableName}</td>
											</c:otherwise>
										</c:choose>

										<td width=""><c:set var="editable"
												value="${variable.writable and (variable.readable or empty taskInstance.token.processInstance.contextInstance.variables[variable.mappedName])}" />
											<c:if test="${editable}">
												<input name="fieldNames[${fieldIndex}]" type="hidden"
													value="${variable.mappedName}" />
											</c:if> <c:choose>
												<c:when test="${fn:startsWith(variable.mappedName,'doc_')}">
													<c:choose>
														<c:when test="${editable}">
															<siga:selecao propriedade="${variable.mappedName}"
																modulo="sigaex" tipo="expediente" tema="simple"
																ocultardescricao="sim"
																siglaInicial="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
														</c:when>
														<c:otherwise>
															<a
																href="/sigaex/app/expediente/doc/exibir?sigla=${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}">${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}</a>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${fn:startsWith(variable.mappedName,'pes_')}">
													<c:choose>
														<c:when test="${editable}">
															<siga:selecao propriedade="${variable.mappedName}"
																modulo="siga" tipo="pessoa" tema="simple"
																ocultardescricao="sim"
																siglaInicial="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
														</c:when>
														<c:otherwise>
									${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
								</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${fn:startsWith(variable.mappedName,'lot_')}">
													<c:choose>
														<c:when test="${editable}">
															<siga:selecao propriedade="${variable.mappedName}"
																modulo="siga" tipo="lotacao" tema="simple"
																ocultardescricao="sim"
																siglaInicial="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
														</c:when>
														<c:otherwise>
									${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
								</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${fn:startsWith(variable.mappedName,'dt_')}">
													<c:choose>
														<c:when test="${editable}">
															<input name="fieldValues[${fieldIndex}]" type="text"
																value="<fmt:formatDate pattern="dd/MM/yyyy"	value="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />"
																onblur="javascript:verifica_data(this, true);" />
														</c:when>
														<c:otherwise>
															<fmt:formatDate pattern="dd/MM/yyyy"
																value="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${fn:startsWith(variable.mappedName,'sel_')}">
													<c:choose>
														<c:when test="${editable}">
															<select name="fieldValues[${fieldIndex}]">
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
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${editable}">
															<input name="fieldValues[${fieldIndex}]" type="text"
																value="${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}" />
														</c:when>
														<c:otherwise>
									${taskInstance.token.processInstance.contextInstance.variables[variable.mappedName]}
									</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose></td>
								</tr>
							</c:if>
							</c:if>
							<c:if test="${editable}">
								<c:set var="fieldIndex" value="${fieldIndex + 1}" />
							</c:if>
						</c:forEach>
						<c:if
							test="${(titular.sigla eq taskInstance.actorId) or (wf:podePegarTarefa(cadastrante, titular,lotaCadastrante,lotaTitular,taskInstance) == true)}">
							<tr>
								<td colspan="2"><siga:links>
										<c:forEach var="transition" items="${task.transitions}">
											<siga:link icon="${transition.icon}"
												title="${empty transition.name ? 'Prosseguir' : transition.name}${transition.resp}"
												url="javascript: var h = document.getElementById('transitionName${tiId}'); h.value='${empty transition.name ? 'Prosseguir' : transition.name}${transition.resp}'; var form = document.getElementById('form${tiId}'); form.submit();"
												test="${true}" />
										</c:forEach>
									</siga:links></td>
							</tr>
						</c:if>
						</td>
						</tr>
						<c:forEach var="variable" items="${task.variableList}">

							<c:if test="${fn:startsWith(variable.mappedName,'doc_')}">
								<!-- 							<c:if test="${variable.aviso}">  -->
								<!-- 							</c:if>	-->
								<c:if test="${not empty task.msgAviso}">
									<tr>
										<td colspan="2" style="color: red; font-weight: bold;">
											${task.msgAviso}</td>
									</tr>
								</c:if>
							</c:if>
						</c:forEach>
					</table>
					</div>