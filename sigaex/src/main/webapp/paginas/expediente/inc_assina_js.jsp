<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<c:choose>
	<c:when test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;ADRB:Política - Referência Básica')}">
		<input type="hidden" id="gPolitica" name="gPolitica" value="true" />
	</c:when>
	<c:otherwise>
		<input type="hidden" id="gPolitica" name="gPolitica" value="false" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;REP:Acesso ao Repositório de Certificados Digitais')}">
		<input type="hidden" id="acessoCertificadoDigital" name="acessoCertificadoDigital" value="true" />
	</c:when>
	<c:otherwise>
		<input type="hidden" id="acessoCertificadoDigital" name="acessoCertificadoDigital" value="false" />
	</c:otherwise>
</c:choose>