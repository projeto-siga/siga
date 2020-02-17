<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css"	type="text/css" media="screen, projection, print" />

<siga:pagina titulo="Gerar Protocolo" popup="true">
	<style>
	   @media print { 
	       #btn-form { display:none; } 
	       #bg {-webkit-print-color-adjust: exact;}
	       
	   }
	</style>
	<!-- main content bootstrap -->
	<div class="card-body">
		<div class="row">	

			<div class="col-sm-12">
				<div class="text-center">
					<img src="${pageContext.request.contextPath}/imagens/brasao_sp.png" class="rounded float-left" width="80px"/>
					<h4><b>${f:resource('siga.cabecalho.titulo')}</b></h4>
					<h5>${doc.orgaoUsuario.descricao}</h5>
					<h5>${doc.lotacao.descricao }</h5>
				</div>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-sm-12">
				<div class="p-3 mb-2 bg-dark text-white text-center" id="bg"><h4><b>Protocolo de Acompanhamento de Documento</b><h4></div>
			</div>
		</div>
		<br>
		<br>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group text-center">
					<!-- <label>N&uacute;mero do Protocolo: <b>${protocolo.numero} / ${ano}</b></label> -->
					<label>N&uacute;mero do Protocolo: <b>${protocolo.codigo}</b></label>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group text-center">
					<label>Data/Hora: ${dataHora}</label>
				</div>
			</div>
		</div>
		<br>
		<br>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group text-center">
					<label><b>Aten&ccedil;&atilde;o: </b>Para consultar o andamento do seu documento acesse ${url} </label>
				</div>
			</div>
		</div>
		<br>
		<br>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group text-center">
					<label>${url} - ${doc.cadastrante.nomePessoa} - ${doc.cadastrante.cargo.descricao } - ${doc.cadastrante.lotacao.descricao } - ${doc.dtRegDocDDMMYYHHMMSS}</label>
				</div>
			</div>
		</div>
		<br />
		<div id="btn-form">
			<form name="frm" action="principal" namespace="/" method="get"
				theme="simple">
				<button type="button" class="btn btn-primary" onclick="javascript: document.body.offsetHeight; window.print();" >Imprimir</button>
			</form>
		</div>	
	</div>
		
	
</siga:pagina>