<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Dados do Servidor">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Dados do Servidor</h2>
		</div>
		<div class="gt-form gt-content-box">
			<table class="gt-for-table-custom">
				<tr>
					<th><b>Nome:</b></th>
					<td colspan="3"><b>${pessoa.nomePessoa}</b></td>
				</tr>
				<tr>
					<th><b>Lotação:</b></th>
					<td width="70%"><siga:selecionado 
									sigla="${pessoa.lotacao.sigla} - ${pessoa.lotacao.descricaoAmpliada}"
									descricao="${pessoa.lotacao.descricaoAmpliada}"
									lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" />
					</td>
					<th><b>Matrícula:</b></th>
					<td>${pessoa.sigla}</td>
				</tr>
				<tr>
					<th><b>Cargo:</b></th>
					<td>${pessoa.cargo.descricao}</td>
					<th><b>Sigla:</b></th>
					<td>${pessoa.siglaPessoa}</td>
				</tr>
				<tr>
					<th><b>Função:</b></th>
					<td>${pessoa.funcaoConfianca.descricao}</td>
					<th><b>Email:</b></th>
					<td>${pessoa.emailPessoa}</td>
				</tr>
			</table>
		</div>
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2 class="gt-table-head">Histórico</h2>
				<div class="gt-content-box gt-for-table">
					<table border="0" class="gt-table">
						<thead>
							<tr>
								<th align="right">Data</th>
								<th align="right">Lotacao</th>
								<th align="right">Função</th>
								<th align="right">Padrão</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="pessoa"
								items="${pessoa.historicoInfoCorporativas}">
								<tr>
									<td>${pessoa.dtInicioPessoaDDMMYYHHMMSS}</td>
									<td><siga:selecionado 
											sigla="${pessoa.lotacao.sigla}"
											descricao="${pessoa.lotacao.descricaoAmpliada}"
											lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" />
									</td>
									<td>${pessoa.funcaoConfianca.descricao}</td>
									<td>${pessoa.padraoReferencia}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>		
</siga:pagina>