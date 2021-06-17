<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Protocolo de Transferência" popup="true">
	<style>
	   @media print { 
	       #btn-form { display:none; } 
	   }
	</style>
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5><fmt:message key='protocolo.transferencia' /></h5>
			</div>

			<div class="card-body">

				<table class="table table-bordered table-light">
					<tr>
						<tr>
							<td>De</td>
							<td>${lotaTitular.descricao} - ${cadastrante.descricao}</td>
						</tr>
						<tr>
							<td>Para</td>
							<td>${mov.respString}</td>
						</tr>
						<tr>
							<td>Data</td>
							<td colspan="2">${mov.dtRegMovDDMMYYHHMMSS}</td>
						</tr>
				</table>
			</div>
		</div>
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Documento(s)</h5>
			</div>

			<div class="card-body">
				<table class="table table-striped" id="tbl-docs">
					<col width="22%" />
					<col width="5%" />
					<col width="4%" />
					<col width="4%" />
					<col width="5%" />
					<col width="4%" />
					<col width="4%" />
					<col width="4%" />
					<col width="4%" />
					<col width="44%" />
					<thead class="${thead_color} align-middle text-center">
						<tr>
							<th rowspan="2" class="text-right">Número</th>
							<th colspan="3">Documento</th>
							<th colspan="3">Última Movimentação</th>
							<th colspan="2">Atendente</th>
							<th rowspan="2" class="text-left">Descrição</th>
						</tr>
						<tr>
							<th>Data</th>
							<th><fmt:message key="usuario.lotacao"/></th>
							<th><fmt:message key="usuario.pessoa2"/></th>
							<th>Data</th>
							<th><fmt:message key="usuario.lotacao"/></th>
							<th><fmt:message key="usuario.pessoa2"/></th>
							<th><fmt:message key="usuario.lotacao"/></th>
							<th><fmt:message key="usuario.pessoa2"/></th>
						</tr>

					<c:forEach var="i" items="${itens}">
						<tr>
							<td>
								<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${i.mob.sigla}">${i.mob.codigo}</a>
								<c:if test="${not i.mob.geral}">
								<td>${i.doc.dtDocDDMMYY}</td>
								<td><siga:selecionado
											sigla="${i.doc.lotaSubscritor.sigla}"
											descricao="${i.doc.lotaSubscritor.descricao}" /></td>
									<td><siga:selecionado
											sigla="${i.doc.subscritor.iniciais}"
											descricao="${i.doc.subscritor.descricao}" /></td>
									<td>${i.dtDDMMYY}</td>
									<td><siga:selecionado
											sigla="${i.lotaSubscritor.sigla}"
											descricao="${i.lotaSubscritor.descricao}" /></td>
									<td><siga:selecionado
											sigla="${i.subscritor.iniciais}"
											descricao="${i.subscritor.descricao}" /></td>
									<td><siga:selecionado
											sigla="${i.lotaAtendente.sigla}"
											descricao="${i.lotaAtendente.descricao}" /></td>
									<td><siga:selecionado
											sigla="${i.atendente.iniciais}"
											descricao="${i.atendente.descricao}" /></td>
								</c:if> <c:if test="${i.mob.geral}">
									<td>${i.doc.dtDocDDMMYY}</td>
									<td><siga:selecionado
											sigla="${i.doc.subscritor.iniciais}"
											descricao="${i.doc.subscritor.descricao}" /></td>
									<td><siga:selecionado
											sigla="${i.doc.lotaSubscritor.sigla}"
											descricao="${i.doc.lotaSubscritor.descricao}" /></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</c:if>
								<td class="text-left">
									<c:choose>
										<c:when test="${siga_cliente!='GOVSP'}">
											${f:descricaoConfidencial(i.doc, lotaTitular)}
										</c:when>
										<c:otherwise>
											${i.doc.descrDocumento}
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
					</c:forEach>


				</table>
			</div>
		</div>

		<br />
		<div id="btn-form">
			<form name="frm" action="principal" namespace="/" method="get"
				theme="simple">
				<button type="button" class="btn btn-primary" onclick="javascript: document.body.offsetHeight; window.print();" >Imprimir</button>
				<c:if test="${popup != true}">
					<button type="button" class="btn btn-primary" onclick="javascript:history.back();" >Voltar</button>
				</c:if>
			</form>
		</div>
		<br /> <br />
		<div>
			<br /> <br />
			<p align="center">Recebido em: _____/_____/_____ às _____:_____</p>
			<br /> <br /> <br />
			<p align="center">________________________________________________</p>
			<p align="center">Assinatura do Servidor</p>
		</div>
	</div>
</siga:pagina>