<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
MANUTENÇÃO VANTAGENS PESSOAIS  -->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DO FUNCIONÁRIO">
				<mod:texto titulo="Classe" var="classe"/>
				<mod:texto titulo="Padrão" var="padrao" />
		</mod:grupo>
		
		<mod:grupo titulo="DETALHES DA MANUTENÇÃO DAS VANTAGENS PESSOAIS">
		
		    <mod:texto largura="60" titulo="Cargo" var="cargoServ"/>
		    
			<mod:grupo titulo="REALIZAR A MANUTENÇÃO EM:"></mod:grupo>
				<mod:caixaverif titulo="Adicional por tempo de serviço?" var="adicionalTempoServiço" reler="Não"/>
			<mod:grupo></mod:grupo>		
				<mod:caixaverif titulo="Dependentes(dedução do IRPF)na fonte?" var="manutencaoDependentes" reler="Não"/>
			<mod:grupo></mod:grupo>							
				<mod:caixaverif titulo="Décimos/Quintos incorporados?" var="decimosQuintos" reler="Não" />
			<mod:grupo></mod:grupo>							
			<mod:memo colunas="50" linhas="3" titulo="Outra a relatar" var="outros"/>
				
		</mod:grupo>
			
	</mod:entrevista>
	
	<mod:documento>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head></head>
	<body>
				
		<c:import url="/paginas/expediente/modelos/inc_tit_SraDiretoraSubsecretariaRH.jsp" />
		
		<p style="TEXT-INDENT: 2cm" align="justify">
	${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		classe ${requestScope.classe} e padrão ${requestScope.padrao}, 
		lotado(a) no(a)${subscritor.lotacao.descricao}, 
		vem respeitosamente requerer a Vossa Excelência, em razão de mudança nesta 
		Seção Judiciária, a manutenção das seguintes vantagens pessoais vinculadas 
		ao cargo de ${cargoServ}, e matrícula ${doc.subscritor.sigla}.
		</p>
		
		
		<B>
			<c:if test="${adicionalTempoServiço== 'Sim'}">
			    Adiconal por Tempo de Serviço;
			    <br>	
			</c:if>
			
			<c:if test="${manutencaoDependentes== 'Sim'}">
			    Manutenção de dependentes para fins de dedução no Imposto de Renda na fonte;
			    <br>		
			</c:if>
			
			<c:if test="${decimosQuintos== 'Sim'}">
			    Décimos/Quintos incorporados; 
			    <br>
			</c:if>	
		</B>

		
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
					
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
	</body>
	</html>
</mod:documento>
</mod:modelo>
