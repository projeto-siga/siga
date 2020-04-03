<%@ include file="/WEB-INF/page/include.jsp"%>

<div>
	<div>
		<c:if
			test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
			<span id="gc-ancora"></span>
		</c:if>


		<c:if
			test="${not f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
			<span id="desc_ver"> <c:choose>
					<c:when test="${not empty task.descricao}">
						<c:if test="${task.conhecimentoEditavel}">
							<a style="float: right; margin-left: 15px; margin-bottom: 15px;"
								title="Editar a descrição"
								href="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display='';"><img
								src="/siga/css/famfamfam/icons/pencil.png"> </a>
						</c:if>${task.descricao}</c:when>
					<c:otherwise>Ainda não existe uma descrição de como esta tarefa deve ser executada. Por favor, clique <a
							href="javascript: document.getElementById('desc_ver').style.display='none'; document.getElementById('desc_editar').style.display=''; document.getElementById('desc_but_editar').style.display='none'; document.getElementById('desc_but_gravar').style.display='';">aqui</a> para contribuir.</c:otherwise>
				</c:choose>
			</span>
		</c:if>
	</div>
	<c:if test="${task.desabilitarForm eq false}">
		<div class="gt-form-row gt-width-100">
			<input type="hidden" value="${task.id}" name="tiId" />

			<c:set var="fieldIndex" value="0" />
			<c:forEach var="variable" items="${task.definicaoDeVariavel}">
				<c:set var="valor" value="${task.obterValorDeVariavel(variable)}" />
				<div class="form-group">
					<c:choose>
						<c:when test="${fn:startsWith(variable.identificador,'sel_')}">
							<label>${fn:substring(variable.nome,0,fn:indexOf(variable.nome,'('))}</label>
						</c:when>
						<c:otherwise>
							<label>${variable.nome}</label>
						</c:otherwise>
					</c:choose>

					<c:set var="editable" value="${variable.acesso eq 'READ_WRITE'}" />
					<c:if test="${editable}">
						<input name="fieldNames[${fieldIndex}]" type="hidden"
							value="${variable.identificador}" />
					</c:if>
					<c:choose>
						<c:when test="${fn:startsWith(variable.identificador,'doc_')}">
							<c:choose>
								<c:when test="${editable}">
									<siga:selecao propriedade="${variable.identificador}"
										modulo="sigaex" tipo="expediente" tema="simple"
										ocultardescricao="sim" siglaInicial="${valor}" />
								</c:when>
								<c:otherwise>
									<a href="/sigaex/app/expediente/doc/exibir?sigla=${valor}">${valor}</a>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${fn:startsWith(variable.identificador,'pes_')}">
							<c:choose>
								<c:when test="${editable}">
									<siga:selecao propriedade="${variable.identificador}"
										modulo="siga" tipo="pessoa" tema="simple"
										ocultardescricao="sim" siglaInicial="${valor}" />
								</c:when>
								<c:otherwise>
									${valor}
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${fn:startsWith(variable.identificador,'lot_')}">
							<c:choose>
								<c:when test="${editable}">
									<siga:selecao propriedade="${variable.identificador}"
										modulo="siga" tipo="lotacao" tema="simple"
										ocultardescricao="sim" siglaInicial="${valor}" />
								</c:when>
								<c:otherwise>
									${valor}
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${fn:startsWith(variable.identificador,'dt_')}">
							<c:choose>
								<c:when test="${editable}">
									<input name="fieldValues[${fieldIndex}]" type="text"
										value="<fmt:formatDate pattern="dd/MM/yyyy"	value="${valor}" />"
										onblur="javascript:verifica_data(this, true);"
										class="form-control" />
								</c:when>
								<c:otherwise>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${valor}" />
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${fn:startsWith(variable.identificador,'sel_')}">
							<c:choose>
								<c:when test="${editable}">
									<select name="fieldValues[${fieldIndex}]" class="form-control">
										<c:forEach var="opcao"
											items="${wf:listarOpcoes(variable.nome)}">
											<option value="${opcao}">${opcao}</option>
										</c:forEach>
									</select>
								</c:when>
								<c:otherwise>
										${valor}
									</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${editable}">
									<input name="fieldValues[${fieldIndex}]" type="text"
										value="${valor}" class="form-control" />
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>${valor}&nbsp;</div>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
				<c:if test="${editable}">
					<c:set var="fieldIndex" value="${fieldIndex + 1}" />
				</c:if>
			</c:forEach>
		</div>
		<%--<c:if test="${task.podePegarTarefa}"> --%>
		<div class="gt-form-row gt-width-100">
			<c:forEach var="transition" items="${task.definicaoDeDesvio}"
				varStatus="loop">
				<c:set var="resp"
					value="${transition.obterProximoResponsavel(task.instanciaDeProcedimento)}" />
				<c:if test="${not empty resp}">
					<c:set var="resp" value=" &raquo; ${resp}" />
				</c:if>
				<button type="submit" name="detourIndex" value="${loop.index}"
					class="btn btn-info mr-3">${empty transition.nome ? 'Prosseguir' : transition.nome}${resp}</button>
			</c:forEach>
		</div>
		<c:if test="${empty task.definicaoDeDesvio}">
			<button type="submit" name="detourIndex" value=""
				class="btn btn-info mr-3">Prosseguir${resp}</button>
		</c:if>
		<%--</c:if> --%>
	</c:if>
	<span style="color: red; font-weight: bold;"> ${task.msgAviso}</span>
</div>
