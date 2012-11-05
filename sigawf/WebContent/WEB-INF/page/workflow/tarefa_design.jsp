<%@ include file="/WEB-INF/page/include.jsp"%>
<c:choose>
	<c:when test="${not empty ajax}">
		<%--
		<table class="form100wf" width="100%">
			<tr class="header">
				<td colspan="2">Procedimento: <span
					style="font-weight: normal;">${taskInstance.task.processDefinition.name}</span><span
					style="text-align: right;">&nbsp(${f:espera(taskInstance.create)})</span><br />
					Tarefa: <a href="/sigawf/task.action?tiId=${task.taskInstance.id}"><span
						style="font-weight: normal;">${task.taskInstance.task.name}</span>
				</a> <ww:if test="${not empty taskInstance.actorId}">
						sendo atendida por <span style="font-weight: normal;">${taskInstance.actorId}</span>
					</ww:if> <ww:else>
						aguardando atendimento
						<c:if
							test="${wf:podePegarTarefa(cadastrante, titular,lotaCadastrante,lotaTitular,taskInstance) == true}">
							<ww:url id="url" action="pegar">
								<ww:param name="tiId">${taskInstance.id}</ww:param>
							</ww:url>
							<a href="${url}">(pegar)</a>
						</c:if>
					</ww:else>
				</td>
			</tr>
			 --%>
	</c:when>
	<c:otherwise>
		<h3>Execução da Tarefa</h3>
		<div class="gt-form gt-content-box">
	</c:otherwise>
</c:choose>


<div class="gt-form-row gt-width-100">
	<span id="desc_ver">${task.descricao}</span>
	<c:if test="${task.conhecimentoEditavel}">
		<span id="desc_editar" style="display: none"><label>Descrição
				da tarefa</label>
		<textarea cols="80" rows="15" name="conhecimento"
				class="gt-form-textarea">${task.conhecimento}</textarea></span>
	</c:if>
	<c:if test="${task.conhecimentoEditavel}">
		<tr>
			<td><input name="editar_conhecimento" type="button"
				value="Editar a descrição" id="desc_but_editar"
				onclick="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display=''; " />
				<input name="salvar_conhecimento" type="submit" id="desc_but_gravar"
				value="Salvar" style="display: none"
				onclick="javascript: this.form.action='${url}'; " />
			</td>
		</tr>
	</c:if>
</div>
<div class="gt-form-row gt-width-100">
	<table class="gt-form-table">
		<ww:url action="saveKnowledge" id="url"></ww:url>
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
						</c:if> <ww:if test="%{#attr.variable.mappedName.startsWith('doc_')}">
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
						</ww:if> <ww:elseif test="%{#attr.variable.mappedName.startsWith('pes_')}">
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
						</ww:elseif> <ww:elseif test="%{#attr.variable.mappedName.startsWith('lot_')}">
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
						</ww:elseif> <ww:elseif test="%{#attr.variable.mappedName.startsWith('dt_')}">
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
						</ww:elseif> <ww:elseif test="%{#attr.variable.mappedName.startsWith('sel_')}">
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
						</ww:else>
					</td>
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
		<span style="color: red; font-weight: bold;"> ${task.msgAviso}</span>
	</ww:if>
</c:forEach>
</div>