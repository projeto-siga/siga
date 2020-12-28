<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Condutores</h2>
		
			<sigatp:erros />

			<c:choose>
				<c:when test="${condutores.size() > 0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
								<tr>
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>							
									<th align="center" width="7%">Matr.</th>
									<th width="25%">Nome</th>
									<th width="15%">Tel. Inst.</th>
									<th width="15%">Tel. Part.</th>
									<th width="20%">CNH</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${condutores}" var="item">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[CondutorController].editar(item.id)}" titulo="condutor"/>
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[CondutorController].excluir(item.id)}" titulo="condutor"
											onclick="javascript:return confirm('Tem certeza de que deseja excluir os dados deste condutor?');"/>
										</td>
										<td><c:if test="${not empty item.matricula}">${item.matricula}</c:if></td>
										<td><c:if test="${not empty item.nomePessoaAI}">${item.nomePessoaAI}</c:if></td>
										<td><c:if test="${not empty item.telefoneInstitucional}">${item.telefoneInstitucional}</c:if></td>
										<td><c:if test="${not empty item.telefonePessoal or not empty item.celularPessoal}">
												${item.telefonePessoal}<br />${item.celularPessoal}
										</c:if></td>
										<td><c:if test="${not empty item.categoriaCNH.toString()}">
			    								Cat. ${item.categoriaCNH.toString()}
		  									<br />
											<c:if test="${item.vencimentoCNHExpirado}">
												<span style="color: red; font-weight: bolder;"> </c:if><fmt:formatDate
														pattern="dd/MM/yyyy" value="${item.dataVencimentoCNH.time}" />
												</span>
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br/><h3>N&atilde;o existem condutores cadastrados.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[CondutorController].incluir}" id="botaoIncluirCondutor"
					class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>
			</div>
		</div>
	</div>
</siga:pagina>