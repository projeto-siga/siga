<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Serviços">

    <h1>Página criada para testes das migrações dos HTML que compoem a tela de Cadastro de Servicos.</h1>

    <div>
	    <form id="errosteste" action="${linkTo[SolicitacaoController].testeErro}">
	        <label>Teste de Erros</label><br/>
	        <c:forEach var="error" items="${errors}">
	          <span style="color: red">${error.message}</span><br/>
	        </c:forEach>        
	        <input type="submit" value="Teste Erros" />
	        
	        <br/><br/><br/>
	    

		    exibirLocalRamalEMeioContato.jsp    
		    <jsp:include page="exibirLocalRamalEMeioContato.jsp"></jsp:include>
		    
		    
        </form>
    </div>
</siga:pagina>