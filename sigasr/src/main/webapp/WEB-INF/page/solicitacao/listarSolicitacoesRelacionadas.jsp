<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="solicitacoesRelacionadas" required="true"%>


<c:forEach items="${solicitacoesRelacionadas}" var="sol">
	<tr>
		<td>
			<a href="#" onclick="exibir(${sol[0]})">
				${sol[2]}
			</a>
		</td> 
		<td>
			<b style="color: #333">
				${sol[3]}:
			</b>
			&nbsp; <siga:selecionado sigla="${sol[1]}" descricao="${sol[1]}"/>
		</td> 
	</tr>
</c:forEach>

<c:if test="${solicitacoesRelacionadas == null || solicitacoesRelacionadas.size() == 0}">
	<tr><td colspan="2" style="text-align: center;">N&atilde;o h&aacute; solicita&ccedil;&otilde;es relacionadas</td></tr>
</c:if>