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
			<p>
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
			</p>
		</div>
	</div>		
</siga:pagina>