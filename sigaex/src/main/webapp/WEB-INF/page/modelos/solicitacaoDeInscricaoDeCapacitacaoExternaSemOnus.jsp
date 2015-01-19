<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- este modelo trata de
solicitação de inscrição para capacitação sem onus -->

<mod:modelo>
	 <mod:entrevista>
			<mod:grupo titulo="Solicitação de inscrição em ação de capacitação externa sem ônus"> 
			    <mod:texto titulo="${i} Ação de capacitação" largura="40" var="Acao${i}"/>
			    <mod:texto titulo="${i} Instituição/Consultor" largura="40" var="IC${i}"/>
				<mod:grupo titulo="Período Solicitado"> 
			   <mod:data titulo="De" var="dataInicio${i}" />
			   <mod:data titulo="a" var="dataFim${i}"  />
			   <mod:texto titulo="${i} cidade/uf" largura="40" var="Cidade${i}"/>					
	        </mod:grupo>
	        <mod:grupo>
	        	<mod:selecao titulo="Prospecto anexo" var="prospecto${i}" opcoes="sim;não" reler="sim"/>
	        </mod:grupo>
	        <mod:grupo>
	        	Qual a importância que a ação de capacitação terá para o desenvolvimento das atividades da unidade organizacional ?<br>
	        	(defina de forma que os objetivos a serem atingidos possam ser mensurados ou observados)<br><br>
	        
	        </mod:grupo>
	        <mod:grupo titulo="IDENTIFIQUE AS ATIVIDADES/TAREFAS DA UNIDADE QUE SERÃO AFETADAS PELA CAPACITAÇÃO">
	        	<mod:grupo titulo="Atividade/tarefa que sera afetada pelo treinamento">
	        		<mod:texto titulo="${i} 1" largura="10" var="atividadeTarefa1${i}"   />
	        		<mod:texto titulo="${i} 2" largura="10" var="atividadeTarefa2${i}"   />
	        		<mod:texto titulo="${i} 3" largura="10" var="atividadeTarefa3${i}"   />
	        		<mod:texto titulo="${i} 4" largura="10" var="atividadeTarefa4${i}"   />
	        	</mod:grupo>
	        	
	        	<mod:grupo titulo="Como será desenvolvida atualmente?">
	        		<mod:texto titulo="${i} 1" largura="10" var="des1${i}"   />
	        		<mod:texto titulo="${i} 2" largura="10" var="des2${i}"   />
	        		<mod:texto titulo="${i} 3" largura="10" var="des3${i}"   />
	        		<mod:texto titulo="${i} 4" largura="10" var="des4${i}"   />
	        	</mod:grupo>
	        	<mod:grupo titulo="Como voce prevê que ficará após a capacitação?">
	        		<mod:texto titulo="${i} 1" largura="10" var="capact1${i}"   />
	        		<mod:texto titulo="${i} 2" largura="10" var="capact2${i}"   />
	        		<mod:texto titulo="${i} 3" largura="10" var="capact3${i}"   />
	        		<mod:texto titulo="${i} 4" largura="10" var="capact4${i}"   />
	        	</mod:grupo>
	        	
	        </mod:grupo>
	        
	        <mod:grupo titulo="SERVIDOR(ES) INDICADO(S)">
	        </mod:grupo>
	        <mod:grupo titulo="CADA SERVIDOR INDICADO ESTÁ DEVIDAMENTE CIENTE DO INTEIRO TEOR DA PORTARIA Nº 042-GDF,DE 10.9.2002, ASSUMINDO OS COMPROMISSOS DETERMINADOS NESSE ATO NORMATIVO,
	        SOBRE TUDO NO QUE SE REFERE À MULTIPLICAÇÃO DO CONHECIMENTO ADQUIRIDO.">
	        	<mod:grupo titulo="Nome/Cargo do Servidor/Função/Seção">
	        		<mod:texto titulo="${i} 1" largura="10" var="NC1${i}"   />
	        		<mod:texto titulo="${i} 2" largura="10" var="NC2${i}"   />
	        		<mod:texto titulo="${i} 3" largura="10" var="NC3${i}"   />
	        		<mod:texto titulo="${i} 4" largura="10" var="NC4${i}"   />
	        	</mod:grupo>
	        	
	        	<mod:grupo titulo="Critério(s) Utilizado(s) para indicação">
	        		<mod:texto titulo="${i} 1" largura="10" var="crit1${i}"   />
	        		<mod:texto titulo="${i} 2" largura="10" var="crit2${i}"   />
	        		<mod:texto titulo="${i} 3" largura="10" var="crit3${i}"   />
	        		<mod:texto titulo="${i} 4" largura="10" var="crit4${i}"   />
	        	</mod:grupo>
	               	
	        </mod:grupo>
	        Observações:(justifique o numero de servidores indicados).
	        
	        <mod:grupo>
	        
	        	<mod:data titulo="data" var="data${i}"/>
	        	
	        </mod:grupo>
	         
	        <mod:grupo titulo="SEÇÃO DE TREINAMENTO-SETRE">	           
	        	<br>Conteúdo programático:<ww:checkbox theme="simple" name="Aprovado"/> Aprovado pela area competente					       
	        </mod:grupo>	        
	        <mod:grupo>
				<mod:selecao titulo="Solicitação constante do levantamento de necessidades de treinamento" var="solict${i}" opcoes="sim;não" reler="sim"/>	        	
	        </mod:grupo>		        
	        <mod:grupo>
				<mod:selecao titulo="Reserva de vagas(s)" var="reserv${i}" opcoes="sim;não" reler="sim"/>	        	
	        </mod:grupo>	        
	        <mod:grupo>	        
	        	<mod:texto titulo="${i} Contato(Nome, telefone, E-mail)" largura="40" var="contato${i}"/>		        	
	        </mod:grupo>		        	
		</mod:grupo>	      
	</mod:entrevista>
	
	<mod:documento>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	    <head> 
	    </head> 
	       <c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp"/> 	       	   
	<body>	
	<h2>Solicitação de inscrição em ação de capacitação externa sem ônus</h2>
		nº/ano:________  20001-04-SRH<br>
		Ação de Capacitação:${Acao}&nbsp
		Instituição/Consultor:${IC}<br>
		Período Solicitado: De: ${dataInicio} a: ${dataFim}<br> 
	    Cidade/UF: ${Cidade}&nbsp
	    Prospecto: ${prospecto}<br>
	   		Qual a importância que a ação de capacitação terá para o desenvolvimento das atividades da unidade organizacional ?<br>
		(Defina, de forma que os objetivos a serem atingidos possam ser mensurados ou observados).<br><br>
		<h3>Identifique as atividades/tarefas da unidade que serão afetadas pela capacitação</h3>
		Atividade/tarefa que será afetada pelo treinamento:
			1-${atividadeTarefa1}&nbsp
			2-${atividadeTarefa2}&nbsp	
			3-${atividadeTarefa3}&nbsp
			4-${atividadeTarefa4}<br>
		Como será desenvolvida atualmente?
			1-${des1}&nbsp
			2-${des2}&nbsp
			3-${des3}&nbsp
			4-${des4}<br>
		Como você prevê que ficará após a capacitação?
			1-${capact1}&nbsp
			2-${capact2}&nbsp
			3-${capact3}&nbsp
			4-${capact4}<br>
		<h3>Servidor(es) indicado(s)<br>cada servidor indicado está devidamente	ciente do inteiro teor da portaria Nº 042-GDF,DE 10.9.2002, assumindo os compromissos determinados nesse ato normativo,     
	        sobre tudo no que se refere à multiplicação do conhecimento adquirido.</h3>
	        
	        Nome/Cargo do Servidor/Função/Seção:
	        1-${NC1}&nbsp
	        2-${NC2}&nbsp
	        3-${NC3}&nbsp
	        4-${NC4}<br>
	        Critério(s) Utilizado(s) para indicação:
	        1-${crit1}&nbsp	
	        2-${crit2}&nbsp
	        3-${crit3}&nbsp
	        4-${crit4}<br>
	        data:${data}<br> 
	        Responsabilizo-me pelas informações prestadas e pela participação dos indicados, assim como pelo cumprimento do estabelecido na portaria N.042/02-GDF
	        ___________________________________________________________________
	        <h3>Seção de treinamento SETRE</h3>
	        Solicitação constante do levantamento de necessidades de treinamento:${solict}&nbsp
	        Reserva de vagas:${reserv}&nbsp
	        Contato:${contato}<br>
	        <c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
	                         
	</body>					
	</html>
	</mod:documento>

</mod:modelo>
