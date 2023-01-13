<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Histórico de Orgão">

<div class="container-fluid">	
	<form name="frm" action="" method="POST">
	<h5>Histórico de Alteração do Cadastro de Órgão.</h5>
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color} align-middle text-center">
				<tr>
					<th style="width: 10%" class="text-center" rowspan="2">Data</th>
					<th style="width: 20%" class="text-left" rowspan="2">Evento</th>
					<th style="width: 20%" colspan="2" align="left">Responsável Pelo Evento</th>
					<th style="width: 35%" class="text-center" rowspan="2">Marco Regulatório</th>
					<th style="width: 5%" class="text-center" rowspan="2">Data de Alteração</th>
				</tr>
				<tr>
					<th class="text-left">
						<fmt:message key="usuario.lotacao"/>
					</th>
					<th class="text-left">
						<fmt:message key="usuario.pessoa"/>
					</th>
				</tr>
			</thead>
			<c:forEach var="hist" items="${listaHistorico}">
				<tr>
					<td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${hist.hisDtIni}"/></td>
					<td class="text-left">Alteração de Cadastro do órgão</td>
					<td class="text-left">
						<siga:selecionado isVraptor="true" sigla="${hist.hisIdcIni.dpPessoa.lotacao.orgaoUsuario.siglaOrgaoUsu}${hist.hisIdcIni.dpPessoa.lotacao.sigla}"
										descricao="${hist.hisIdcIni.dpPessoa.lotacao.descricaoAmpliada}" 
										lotacaoParam="${hist.hisIdcIni.dpPessoa.lotacao.orgaoUsuario.siglaOrgaoUsu}${hist.hisIdcIni.dpPessoa.lotacao.sigla}" />
					</td>
					<td class="text-left">
						<siga:selecionado isVraptor="true" sigla="${hist.hisIdcIni.dpPessoa.nomePessoa}"
										descricao="${hist.hisIdcIni.dpPessoa.descricao} - ${hist.hisIdcIni.dpPessoa.sigla}" 
										pessoaParam="${hist.hisIdcIni.dpPessoa.sigla}"/>
					</td>
					<td class="text-center">${hist.marcoRegulatorio}</td>
					<td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy" value="${hist.dataAlteracao}"/></td>
				</tr>
			</c:forEach>
		
		</table>
		<br />
	</form>
	
</div>
</siga:pagina>