<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://localhost/functiontag" prefix="fg"%>

<c:set var="urlDownload" scope="session" value="${f:resource('/siga.armazenamento.arquivo.formatolivre.url')}" />
<siga:pagina titulo="${titulo}" popup="somenteComLogo">
<div class="container-fluid">
	<div class="card bg-light mb-3">
		<div class="card-header"><h5>Download de Arquivos</h5></div>
		<div class="card-body">
			<div id="app" class="container content">
				<div class="row">
					<div id="divDownload">
						<p id="mensagemDownload">${token != null ? 'Para fazer o download do arquivo, clique no botão abaixo:'
						: 'Não é possível fazer o download deste arquivo.'}</p>
					</div>
				</div>
				<div class="row">
					<c:if test="${token != null}"> 
						<a class="btn btn-primary" download="${nomeArquivo}" href="${urlDownload}download?tokenArquivo=${token}">Download</a>
					</c:if>
					<a class="btn btn-secondary ${token != null ? 'ml-2' : '' }" href="javascript:window.close();">Voltar</a>
				</div>
			</div>
		</div>
	</div>
</div>
</siga:pagina>