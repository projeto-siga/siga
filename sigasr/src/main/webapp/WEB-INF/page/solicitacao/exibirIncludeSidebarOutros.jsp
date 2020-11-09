<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>	
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
	
<c:if test="${vinculadas != null && !vinculadas.isEmpty()}">
	<div class="card-sidebar card bg-light mb-3">
		<div class="card-header">Veja Tamb&eacute;m</div>
		<div class="card-body">
			<p>
				<c:forEach items="${vinculadas}" var="vinculada">
					<a
						href="${linkTo[SolicitacaoController].exibir(vinculada.siglaCompacta)}">
						${vinculada.codigo} </a>
					<br />
				</c:forEach>
			</p>
		</div>
	</div>
</c:if>
<c:if test="${solicitacao.parteDeArvore}">
	<div class="card-sidebar card bg-light mb-3">
		<div class="card-header">Contexto</div>
		<div class="card-body">
			<p>
				<sigasr:listaArvore solicitacao="${solicitacao.paiDaArvore}"
					visualizando="${solicitacao}" />
			</p>
		</div>
	</div>
</c:if>
<c:if test="${not empty arqs}">
	<div class="card-sidebar card bg-light mb-3">
		<div class="card-header">Arquivos Anexos</div>
		<div class="card-body">
			<p>
				<c:forEach items="${arqs}" var="anexacao">
					<sigasr:arquivo arquivo="${anexacao}" descricao="sim"/> 
					<br />
	                 	</c:forEach>
	       	</p>
       </div>
	</div>
</c:if>

<c:if test="${juntadas != null && !juntadas.isEmpty()}">
	<div class="card-sidebar card bg-light mb-3">
	    <div class="card-header">Solicita&ccedil;&otilde;es juntadas</div>
	    <div class="card-body">
		    <p>
		        <c:forEach items="${juntadas}" var="juntada">
		            <a href="${linkTo[SolicitacaoController].exibir(juntada.siglaCompacta)}">
		            ${juntada.codigo} </a> <br/> 
		        </c:forEach>
		    </p>
		   </div>
	</div>
</c:if>

<c:if test="${not empty listas}">
	<div class="card-sidebar card bg-light mb-3">
	    <div class="card-header">Listas de Prioridade</div>
	    <div class="card-body">
	        <c:forEach items="${listas}" var="listas">
	            <p>
	                <input type="hidden" name="idlista"
	                value="${listas.idLista}" /> 
	                <a style="color: #365b6d; text-decoration: none"
	                href="${linkTo[SolicitacaoController].exibirLista(listas.idLista)}">
	                    ${listas.listaAtual.nomeLista} </a>
	            </p>
	        </c:forEach>
       </div>
	</div>
</c:if>
  
 <div id="divConhecimentosRelacionados">
     <jsp:include page="exibirConhecimentosRelacionados.jsp"/>
 </div>