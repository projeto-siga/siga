<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:if test="${pessoa != null}">
	<input type="hidden" name="dataNascimento" id="pessoa.dataNascimento" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${pessoa.getDataNascimento()}" />" />
	<input type="hidden" name="lotacao" id="pessoa.lotacao" value="${pessoa.getLotacao().getDescricao()}" />
	<input type="hidden" name="emailInstitucional" id="pessoa.emailInstitucional" value="${pessoa.getEmailPessoa()}" />
</c:if>