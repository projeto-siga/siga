<%@ include file="/WEB-INF/page/include.jsp"%>

<div>
	<c:set var="msgAviso" value="${pi.getMsgAviso(titular, lotaTitular)}" />
	<c:if test="${not pi.isDesabilitarFormulario(titular, lotaTitular) and empty msgAviso}">
		<div class="gt-form-row gt-width-100">
			<input type="hidden" value="${pi.id}" name="tiId" />

			<c:set var="fieldIndex" value="0" />
			<c:forEach var="variavel"
				items="${pi.definicaoDeTarefaCorrente.definicaoDeVariavel}">
				<c:set var="valor" value="${pi.obterValorDeVariavel(variavel)}" />
				<div class="form-group">
					<c:choose>
						<c:when test="${variavel.tipo eq 'SELECAO'}">
							<label>${fn:substring(variavel.nome,0,fn:indexOf(variavel.nome,'('))}</label>
						</c:when>
						<c:otherwise>
							<label>${variavel.nome}</label>
						</c:otherwise>
					</c:choose>

					<c:set var="editable"
						value="${variavel.acesso eq 'READ_WRITE_REQUIRED' or variavel.acesso eq 'READ_WRITE'}" />
					<c:if test="${editable}">
						<input name="campoIdentificador[${fieldIndex}]" type="hidden"
							value="${variavel.identificador}" />
					</c:if>
					<c:choose>
						<c:when test="${variavel.tipo eq 'DOC_MOBIL'}">
							<c:choose>
								<c:when test="${editable}">
									<siga:selecao propriedade="${variavel.identificador}"
										modulo="sigaex" tipo="expediente" tema="simple"
										ocultardescricao="sim" siglaInicial="${valor}"
										requiredValue="${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}" />
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>
										<a href="/sigaex/app/expediente/doc/exibir?sigla=${valor}">${valor}</a>&nbsp;
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${variavel.tipo eq 'PESSOA'}">
							<c:choose>
								<c:when test="${editable}">
									<siga:selecao propriedade="${variavel.identificador}"
										modulo="siga" tipo="pessoa" tema="simple"
										ocultardescricao="sim" siglaInicial="${valor}"
										requiredValue="${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}" />
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>
										<a href="/siga/app/pessoa/exibir?sigla=${valor}">${valor}</a>&nbsp;
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${variavel.tipo eq 'LOTACAO'}">
							<c:choose>
								<c:when test="${editable}">
									<siga:selecao propriedade="${variavel.identificador}"
										modulo="siga" tipo="lotacao" tema="simple"
										ocultardescricao="sim" siglaInicial="${valor}"
										requiredValue="${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}" />
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>
										<a href="/siga/app/lotacao/exibir?sigla=${valor}">${valor}</a>&nbsp;
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${variavel.tipo eq 'DOUBLE'}">
							<c:choose>
								<c:when test="${editable}">
									<input name="campoValor[${fieldIndex}]" type="text"
										value="<fmt:formatNumber value="${valor}" />"
										${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}
										class="form-control" />
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>
										<fmt:formatNumber value="${valor}" />
										&nbsp;
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${variavel.tipo eq 'DATE'}">
							<c:choose>
								<c:when test="${editable}">
									<input name="campoValor[${fieldIndex}]" type="text"
										value="<fmt:formatDate pattern="dd/MM/yyyy"	value="${valor}" />"
										${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}
										onblur="javascript:verifica_data(this, true);"
										class="form-control" />
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>
										<fmt:formatDate pattern="dd/MM/yyyy" value="${valor}" />
										&nbsp;
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${variavel.tipo eq 'BOOLEAN'}">
							<c:choose>
								<c:when test="${editable}">
									<select name="campoValor[${fieldIndex}]" class="form-control"
										${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}>
										<option value=""></option>
										<option value="1"
											${valor eq '1' or valor eq 'true' ? 'selected' : ''}>Sim</option>
										<option value="0"
											${valor eq '0' or valor eq 'false' ? 'selected' : ''}>Não</option>
									</select>
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>${valor eq '1' or valor eq 'true' ? 'Sim' : valor eq '0' or valor eq 'false' ? 'Não' : ''}&nbsp;</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${variavel.tipo eq 'SELECAO'}">
							<c:choose>
								<c:when test="${editable}">
									<select name="campoValor[${fieldIndex}]" class="form-control"
										${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''}>
										<c:forEach var="opcao"
											items="${wf:listarOpcoes(variavel.nome)}">
											<option value="${opcao}" ${valor eq opcao ? 'selected' : ''}>${opcao}</option>
										</c:forEach>
									</select>
								</c:when>
								<c:otherwise>
									<div class="form-control" readonly>${valor}&nbsp;</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${editable}">
									<input name="campoValor[${fieldIndex}]" type="text"
										value="${valor}" class="form-control"
										${variavel.acesso eq 'READ_WRITE_REQUIRED' ? 'required' : ''} />
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
			<c:forEach var="desvio"
				items="${pi.definicaoDeTarefaCorrente.definicaoDeDesvio}"
				varStatus="loop">
				<c:set var="resp" value="${desvio.obterProximoResponsavel(pi)}" />
				<c:if test="${not empty resp}">
					<c:set var="resp" value=" &raquo; ${resp}" />
				</c:if>
				<button type="submit" name="indiceDoDesvio" value="${loop.index}"
					class="btn btn-info mr-3">${empty desvio.nome ? 'Prosseguir' : desvio.nome}${resp}</button>
			</c:forEach>
		</div>
		<c:if test="${empty pi.definicaoDeTarefaCorrente.definicaoDeDesvio}">
				<c:set var="resp" value="${pi.obterProximoResponsavel()}" />
				<c:if test="${not empty resp}">
					<c:set var="resp" value=" &raquo; ${resp}" />
				</c:if>
			<button type="submit" name="indiceDoDesvio" value=""
				class="btn btn-info mr-3">Prosseguir${resp}</button>
		</c:if>
		<%--</c:if> --%>
	</c:if>
	<span style="color: red; font-weight: bold;"> ${msgAviso}</span>
</div>
