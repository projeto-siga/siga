<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>


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
					<td><siga:selecionado 
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
			<%-- 			<p>
				<b>Nome:</b> ${pessoa.nomePessoa}
			</p>
			<p>
				<b>Lotação:</b> <siga:selecionado 
									sigla="${pessoa.lotacao.sigla} - ${pessoa.lotacao.descricaoAmpliada}"
									descricao="${pessoa.lotacao.descricaoAmpliada}"
									lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" />
			</p>
			<p>
				<b>Cargo:</b> ${pessoa.cargo.descricao}
			</p>
			<p>
				<b>Função:</b> ${pessoa.funcaoConfianca.descricao}
			</p>
			<p>
				<b>Email:</b> ${pessoa.emailPessoa}
			</p> --%>
		</div>
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2 class="gt-table-head">Histórico</h2>
				<div class="gt-content-box gt-for-table">
					<table border="0" class="gt-table">
						<thead>
							<tr>
								<th align="right">Data</th>
								<!-- <th align="right">Data Fim</th> -->
								<!-- <th align="right">Nome</th>
								<th align="right">Matricula</th> -->
								<th align="right">Lotacao</th>
								<!-- <th align="right">Cargo</th> -->
								<th align="right">Função</th>
								<th align="right">Padrão</th>
								<!-- <th align="right">Email</th> -->
							</tr>
						</thead>

						<tbody>
							<c:forEach var="pessoa"
								items="${pessoa.historicoInfoCorporativas}">
								<%-- items="${pessoa.pessoaInicial.pessoasPosteriores}"> --%>
								<tr>
									<td>${pessoa.dtInicioPessoaDDMMYYHHMMSS}</td>
									<%-- <td>${pessoa.dtFimPessoaDDMMYYHHMMSS}</td> --%>
									<%-- <td>${pessoa.nomePessoa}</td> --%>
									<%-- <td>${pessoa.matricula}</td> --%>
									<td><siga:selecionado 
											sigla="${pessoa.lotacao.sigla}"
											descricao="${pessoa.lotacao.descricaoAmpliada}"
											lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" />
									</td>
									<%-- <td>${pessoa.cargo.descricao}</td> --%>
									<td>${pessoa.funcaoConfianca.descricao}</td>
									<td>${pessoa.padraoReferencia}</td>
									<%-- <td>${pessoa.emailPessoa}</td> --%>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>		
</siga:pagina>