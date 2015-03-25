<c:if test="${!locOrgaos.empty}">
<td>Local (Solicitante)</td>
<td><sigasr:select name="locOrgao" id="locOrgao" items="${locOrgaos}" valueProperty="idComplexo" labelProperty="nomeComplexo" 
				value="${locOrgao?.idComplexo}"> 
	<sigasr:opcao valor="0">Todas</sigasr:opcao>
	</sigasr:select>
	
</td>
</c:if>