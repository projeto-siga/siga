<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Dados da Lotação">
	<!-- main content -->
	<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Dados da Lotação</h2>
		</div>
		<div class="gt-form gt-content-box">
			<p>
				<b>Nome: ${lotacao.nomeLotacao} </b>
			</p>
			<p>
				<b>Sigla:</b> ${lotacao.sigla}
			</p>
			<p>
				<b>Lotação Pai:</b> <siga:selecionado 
									sigla="${lotacao.lotacaoPai.sigla} - ${lotacao.lotacaoPai.descricaoAmpliada}"
									descricao="${lotacao.lotacaoPai.descricaoAmpliada}"
									lotacaoParam="${lotacao.lotacaoPai.orgaoUsuario.sigla}${lotacao.lotacaoPai.sigla}" />
			</p>
		</div>
	
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Lotações Subordinadas</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>							
							<th align="right">Sigla</th>														
							<th align="right">Nome</th>														
						</tr>
					</thead>
					<tbody>
						<c:forEach var="lotacaoSubordinada" items="${lotacao.dpLotacaoSubordinadosSet}">
							<c:if test="${empty lotacaoSubordinada.dataFimLotacao}">
								<tr>
									<td><siga:selecionado 
											sigla="${lotacaoSubordinada.sigla}"
											descricao="${lotacaoSubordinada.descricaoAmpliada}"
											lotacaoParam="${lotacaoSubordinada.orgaoUsuario.sigla}${lotacaoSubordinada.sigla}" />
									</td>
									<td>${lotacaoSubordinada.nomeLotacao}</td>							
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>				
			</div>				
		</div>
		<br/><br/>
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Magistrados/ Servidores</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>							
							<th align="right">Nome</th>														
							<th align="right">Matricula</th>														
							<!-- <th align="right">Lotacao</th> -->														
							<th align="right">Cargo</th>														
							<th align="right">Função</th>														
							<th align="right">Email</th>														
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="pessoa" items="${lotacao.dpPessoaLotadosSet}">
							<tr>
								<td>${pessoa.nomePessoa}</td>							
								<td><siga:selecionado
									sigla="${pessoa.sigla}"
									descricao="${pessoa.descricao} - ${pessoa.sigla}"
									pessoaParam="${pessoa.sigla}" />
								</td>							
								<%-- <td><siga:selecionado 
									sigla="${pessoa.lotacao.sigla}"
									descricao="${pessoa.lotacao.descricaoAmpliada}"
									lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" /></td> --%>	
								<td>${pessoa.cargo.descricao}</td>							
								<td>${pessoa.funcaoConfianca.descricao}</td>							
								<td>${pessoa.emailPessoa}</td>							
							</tr>
						</c:forEach>
					</tbody>
				</table>				
			</div>				
		</div>
	</div>		
</siga:pagina>