<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Rela&ccedil;&atilde;o de Fornecedores</h2>

			<c:choose>
				<c:when test="${fornecedores.size() > 0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table talesorter">
							<thead>
								<tr>
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
									<th>CNPJ</th>
									<th>Raz&atilde;o Social</th>
									<th>Telefone</th>
									<th>Ramo de Atividade</th>
									<th>Contato</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${fornecedores}" var="item">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[FornecedorController].editar(item.id)}" titulo="fornecedor" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[FornecedorController].excluir(item.id)}" titulo="fornecedor"
											 onclick="javascript:return confirm('Tem certeza de que deseja excluir este fornecedor?');"/>
										</td>
										<td><c:if test="${not empty item.cnpj}">${item.cnpj}</c:if></td>
										<td><c:if test="${not empty item.razaoSocial}">${item.razaoSocial}</c:if></td>
										<td><c:if test="${not empty item.telefone}">${item.telefone}</c:if></td>
										<td><c:if test="${not empty item.ramoAtividadeDescricao}">${item.ramoAtividadeDescricao}</c:if></td>
										<td><c:if test="${not empty item.nomeContato}">${item.nomeContato}</c:if></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem fornecedores cadastrados.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[FornecedorController].incluir}" id="botaoIncluirFornecedor"
					class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>					
			</div>
		</div>
	</div>
</siga:pagina>