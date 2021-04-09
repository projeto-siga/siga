<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:cfg-lista-cabecalho>
	<th align="center"
		style="${tipoDeConfiguracao.style('DEFINICAO_DE_PROCEDIMENTO')}">Diagrama</th>
</siga:cfg-lista-cabecalho>

<siga:cfg-lista-itens>
	<td style="${tipoDeConfiguracao.style('DEFINICAO_DE_PROCEDIMENTO')}">${cfg.definicaoDeProcedimento.nome}<c:if
			test="${not empty configuracao.definicaoDeProcedimento}">${configuracao.definicaoDeProcedimento.nome}</c:if></td>
</siga:cfg-lista-itens>

