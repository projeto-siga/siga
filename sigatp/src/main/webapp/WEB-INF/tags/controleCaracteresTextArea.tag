<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="maxCaracteres"%>
<%@ attribute name="nomeTextArea"%>
<%@ attribute name="idTextArea"%>
<%@ attribute name="rows"%>
<%@ attribute name="cols"%>
<%@ attribute name="valorTextArea"%>

<textarea <c:if test="${not empty idTextArea}">id="${idTextArea}"</c:if> name="${nomeTextArea}" rows="${rows}" cols="${cols}" maxLength="${not empty maxCaracteres ? maxCaracteres : 255}">${valorTextArea}</textarea>