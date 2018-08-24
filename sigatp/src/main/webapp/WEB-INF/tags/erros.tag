<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sigatpTags" uri="/WEB-INF/tpTags.tld"%>

<script type="text/javascript" src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js">
</script>

<style>
	#divErros {display: none;}
</style>

<div id="divErros" class="gt-error">
	<c:if test="${errors.size() > 0}">
		<c:set var="contadorRequired" value="0" />
		<c:forEach items="${errors}" var="error">
			<c:choose>
			    <c:when test="${error.message == 'Required'}">
					<c:set var="contadorRequired" value="${contadorRequired + 1}" />

				    <c:if test="${contadorRequired == 1}">
				    	<li>${error.message}</li>
				    </c:if>
			    </c:when>
			    
			    <c:when test="${error.category.contains('LinkErro')}">
			    	<sigatpTags:erroLink  
			    		message="${error.message}" 
			    		classe="${error.category.replace('LinkErro','')}" 
			    		comando='${linkTo[MissoesController].buscarPelaSequenceAposErro}'
		    		/>
			    </c:when>
			    
			    <c:when test="${error.category.contains('LinkGenericoErro')}">
			    	<sigatpTags:erroLink  
			    		message="${error.message}" 
			    		comando='<a href="#" onclick="javascript:window.history.back();">Voltar</a>'
		    		/>
			    </c:when>
			    <c:otherwise>
			     	<li>${error.message.replace('???','')}</li>
			    </c:otherwise>
			</c:choose>
		</c:forEach>
	</c:if>
</div>

<script language="javascript">
	if ($('#divErros li').length > 0) {
		$("#divErros").show();
	}
</script>