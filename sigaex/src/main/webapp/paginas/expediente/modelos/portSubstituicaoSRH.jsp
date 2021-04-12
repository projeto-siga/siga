<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>

<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/portaria.jsp?tipo=SRH">
	<mod:entrevista>
	
	
	<mod:grupo titulo="Período solicitado">			
		<mod:data var="data1" titulo="Data inicial" reler="ajax" idAjax="dataAjax"/>
		<mod:data var="data2" titulo="Data final" reler="ajax" idAjax="dataAjax"/>
	</mod:grupo>

	<c:if test="${(not empty data1 or not empty data2)}">		
		<c:set var="docs"
				value="${f:buscaDocumentosAssinados(data1,data2)}"/>			
	</c:if>	
	<mod:grupo depende="dataAjax">
		<c:choose>
			<c:when test="${not empty docs}">		
				<c:set var="i" value="0"/>
				<table>
					<tr><th>Lotação</th><th>Matricula</th><th>Outros Motivos</th><th>Motivo</th><th>Titular</th><th>Substituto</th><th>Data Inicial</th><th>Data Final</th><th>Documento<br/>de<br/>Indicação</th><th>Cargo</th></tr>
						<c:forEach var="doc" items="${docs}">   								 										    							    				
		    				<tr>				    								    				
		    					<c:if test="${(data1Ant != data1 or data2Ant != data2)}">				    								    				
			    					<mod:oculto var="matricula${i}" valor="${f:pessoa(doc.form['substituto_pessoaSel.id']).sigla}"/>
			    					<mod:oculto	var="titular${i}" valor="${f:pessoa(doc.form['titular_pessoaSel.id']).descricao}"/>
			    					
			    					<mod:oculto	var="lotacao${i}" valor="${f:lotacaoPorNivelMaximo(f:pessoa(doc.form['titular_pessoaSel.id']).lotacao, 4).sigla}"/>
			    					<mod:oculto var="substituto${i}" valor="${f:pessoa(doc.form['substituto_pessoaSel.id']).descricao}"/>
			   				 		<mod:oculto var="dataInicio${i}" valor="${doc.form.dataInicio}"/>
			   				 		<mod:oculto var="dataFim${i}" valor="${doc.form.dataFim}"/>
			   				 		<mod:oculto var="motivo${i}" valor="${doc.form.motivo}"/>
			   				 		<mod:oculto var="outrosMotivos${i}" valor="${doc.form.outrosMotivos}"/>
			   				 		<mod:oculto var="docIndicacao${i}" valor="${doc.codigo}"/>	
			   				 		<mod:oculto var="marcacao${i}" valor="oi"/>
			   				 		<mod:oculto var="cargo${i}" valor="${f:pessoa(doc.form['titular_pessoaSel.id']).cargo.nomeCargo}"/>
			   				 			
		   				 		</c:if>
		   				 		<c:if test="${data1Ant == data1 or data2Ant == data2}">
		   				 			<mod:oculto var="motivo${i}" valor="oi"/>
		   				 		</c:if>
		   				 	
		   				 		<td><mod:texto var="lotacao${i}" titulo="" largura="15"/></td>
		   				 		<td><mod:texto var="matricula${i}" titulo="" largura="15"/></td>
		   				 		<td><mod:texto var="outrosMotivos${i}" titulo="" largura="30"/></td>
		   				 		<td><mod:texto var="motivo${i}" titulo="" largura="30"/></td>
		   				 		<td><mod:texto var="titular${i}" titulo="" largura="30"/></td>   
		   				 		<td><mod:texto var="substituto${i}" titulo="" largura="30"/></td>
		   				 		<td><mod:texto var="dataInicio${i}" titulo="" largura="10"/></td>
		   				 		<td><mod:texto var="dataFim${i}" titulo="" largura="10"/></td>
		   				 		<td><mod:texto var="docIndicacao${i}" titulo="" largura="10"/></td>
		   				 		<td><mod:texto var="cargo${i}" titulo="" largura="30"/></td>   				 		
		    					
		    				</tr>   				
	   	 				<c:set var="i" value="${i+1}"/>				   	 			 			 
						</c:forEach>
					</table>					
					<mod:oculto var="end" valor="${i}"/>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>		
		</mod:grupo>
		<mod:grupo>
			<mod:selecao var="maisServidores" titulo="Deseja entrar com mais servidores?" reler="ajax" idAjax="maisServidoresAjax" opcoes="Não;Sim"/>
		</mod:grupo>
		
		<mod:grupo depende="maisServidoresAjax">
			<c:if test="${maisServidores == 'Sim'}">
				<mod:texto var="qtdServidores" titulo="Quantidade de servidores" largura="3" reler="ajax" idAjax="qtdServidoresAjax"/>
				<c:set var="x" value="0"/>
			</c:if>
		</mod:grupo>
		<mod:grupo depende="qtdServidoresAjax">	
			<c:if test="${(qtdServidores >= 20) and (not empty qtdServidores)}">
				<mod:mensagem texto="Digite um número menor que 20"/>
			</c:if>				
			<c:if test="${(qtdServidores < 20) and (not empty qtdServidores)}">
				<mod:grupo>
					<table>
					<tr><th>Lotação</th><th>Matricula</th><th>Outros Motivos</th><th>Motivo</th><th>Titular</th><th>Substituto</th><th>Data Inicial</th><th>Data Final</th><th>Documento<br/>de<br/>Indicação</th><th>Cargo</th></tr>
					<c:forEach var="x" begin="${end}" end="${qtdServidores + i - 1}">
						<tr>
							<mod:oculto var="marcacao${x}" valor="oi"/>
							
							<mod:oculto var="motivo${x}" valor="${motivo}"/>
							
							<td><mod:texto var="lotacao${x}" titulo="" largura="15"/></td>
			   				<td><mod:texto var="matricula${x}" titulo="" largura="15"/></td>
			   				<td><mod:texto var="outrosMotivos${x}" titulo="" largura="30"/></td>
			   				<td><mod:texto var="motivo${x}" titulo="" largura="30"/></td>
			   				<td><mod:texto var="titular${x}" titulo="" largura="30"/></td>			   																				
			   				<td><mod:texto var="substituto${x}" titulo="" largura="30"/></td>
			   				<td><mod:data var="dataInicio${x}" titulo=""/></td>
			   				<td><mod:data var="dataFim${x}" titulo=""/></td>
			   				<td><mod:texto var="docIndicacao${x}" titulo="" largura="10"/></td>
			   				<td><mod:texto var="cargo${x}" titulo="" largura="30"/></td>
			   				
			   			
						</tr>
						<c:set var="x" value="${x+1}"/>
					</c:forEach>
					</table>					
				</mod:grupo>
			</c:if>				
		</mod:grupo>
		<mod:oculto var="end" valor="${x}"/>
		
		
	</mod:entrevista>
	
	<mod:documento>
		<mod:valor var="texto_portaria">
		
		<!-- INICIO ABERTURA --><p><B>A DIRETORA DA SUBSECRETARIA DE GESTÃO DE PESSOAS DA JUSTIÇA FEDERAL DE 1º INSTÂNCIA - SEÇÃO JUDICIARIA DO RIO DE JANEIRO,</B> 
		usando a competência que lhe foi delegada pela portaria nº 011 - GDF, de 26 de março de 2003,
		<p><b>RESOLVE:</b></p><!-- FIM ABERTURA -->
		<!-- INICIO CORPO --><p><b>DESIGNAR</b> os servidores abaixo relacionados (organizados pelo respectivos motivos) para substituírem seus respectivos
		titulares, em virtude de compensação dos dias trabalhados no recesso judiciário:</p>
		<c:forEach var="i" begin="0" end="${end}">
			
			<c:if test="${requestScope[f:concat('marcacao',i)]=='oi'}">
					${requestScope[f:concat('motivo',i)]}:<br/>
				<table width="100%">
					<tr>
						<td width="17%">NOME</td><td width="11%">MAT.</td><td width="17%">TITULAR DO<br/>CARGO/<br/>FUNÇÂO</td><td width="10%">LOTA-<br/>ÇÃO</td><td width="16%">CARGO/<br/>FUNÇÃO</td><td width="14%">PERÍODO</td><td>DOCU-<br/>MENTO</td>
					</tr>				
				<c:forEach var="x" begin="${i}" end="${end}">
					<c:if test="${requestScope[f:concat('motivo',i)]== requestScope[f:concat('motivo',x)]}">
						<input type="hidden"  name="marcacao${x}" value="tchau"/>
						<tr>
				  			<td>${f:quebraLinhas(f:stringParaMinusculaNomes(requestScope[f:concat('substituto',x)]))}</td>
				  			<td>${requestScope[f:concat('matricula',x)]}</td>
				  			<td>${f:quebraLinhas(f:stringParaMinusculaNomes(requestScope[f:concat('titular',x)]))}</td>	
				  			<td>${requestScope[f:concat('lotacao',x)]}</td>	
				  			<td>${f:stringParaMinusculaNomes(requestScope[f:concat('cargo',x)])}</td>
				  			<td>${requestScope[f:concat('dataInicio',x)]}<br/> a <br/>${requestScope[f:concat('dataFim',x)]}</td>			  		
				  			<td>${requestScope[f:concat('docIndicacao',x)]}</td>
				  		</tr>			  		
			  		</c:if>		
			  	</c:forEach>		  	
			  	</table>			  	
			</c:if>
		</c:forEach><!-- FIM CORPO -->
		</mod:valor>
	</mod:documento>
</mod:modelo>

