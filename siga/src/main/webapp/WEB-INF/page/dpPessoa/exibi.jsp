<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Dados do Servidor">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados do <fmt:message key="usuario.servidor"/></h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-3">
						<div class="form-group">
							<label>Nome</label>
							<span>${pessoa.nomePessoa}</span>							
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
						<div class="form-group">
							<label><fmt:message key="usuario.lotacao"/></label>
							<siga:selecionado 
									sigla="${pessoa.lotacao.sigla} - ${pessoa.lotacao.descricaoAmpliada}"
									descricao="${pessoa.lotacao.descricaoAmpliada}"
									lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" />
						</div>
					</div>
					<div class="col-sm-3">
						<div class="form-group">
							<label >Matrícula</label>
							<span>${pessoa.sigla}</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
						<div class="form-group">
							<label>Cargo</label>
							<span>${pessoa.cargo.descricao}</span>
						</div>
					</div>
					<div class="col-sm-3">
						<div class="form-group">
							<label>Sigla</label>
							<span>${pessoa.siglaPessoa}</span>
						</div>
					</div>
				</div>			
				<div class="row">
					<div class="col-sm-3">
						<div class="form-group">
							<label>Função</label>
							<span>${pessoa.funcaoConfianca.descricao}</span>
						</div>
					</div>
					<div class="col-sm-3">
						<div class="form-group">
							<label>Email</label>
							<span>${pessoa.emailPessoa}</span>
						</div>
					</div>
				</div>			
			</div>
		</div>
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
				<tr>
					<th align="right">Data</th>
					<th align="right"><fmt:message key="usuario.lotacao"/></th>
					<th align="right">Função</th>
					<th align="right">Padrão</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach var="pessoa" items="${pessoa.historicoInfoCorporativas}">
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
</siga:pagina>