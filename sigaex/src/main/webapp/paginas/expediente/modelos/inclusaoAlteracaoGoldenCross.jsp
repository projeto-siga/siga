<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		
	<mod:grupo titulo="1 - Dados do titular">
			<mod:pessoa var="nometit" titulo="IdentificaÃ§Ã£o do titular"/>
		
		<mod:grupo>	
			<mod:texto var="nrocartaogolden" titulo="No CartÃ£o Golden Cross:" largura="15"/>
		</mod:grupo>
		
	</mod:grupo>
	
	<mod:grupo titulo="2 - InformaÃ§Ãµes iniciais">
		<mod:grupo>
			<mod:selecao titulo="OperaÃ§Ã£o" var="operacao" opcoes="[Selecione];InclusÃ£o;AtualizaÃ§Ã£o" />
			<mod:selecao titulo="Tipo de cliente" var="tcliente" opcoes="[Selecione];Titular;Dependente;Agregado" reler="ajax" idAjax="tclienteAjax" />
		</mod:grupo>
		<mod:grupo>
			<mod:mensagem titulo="A partir de :" />
			<mod:selecao titulo="Dia" var="diavalidadet" opcoes="01;11;21" />
			<mod:selecao titulo="Mes" var="mesvalidadet" opcoes="01;02;03;04;05;06;07;08;09;10;11;12" />
			<mod:selecao titulo="Ano" var="anovalidadet" opcoes="2007;2008;2009;2010;2011;2012;2013;2014;2015;2016;2017;2018;2019;2020" />
		</mod:grupo>					    
	</mod:grupo>
	
	<mod:grupo depende="tclienteAjax">
	<c:choose>
    <c:when test="${tcliente == 'Titular'}">
	<mod:grupo titulo="3 - InclusÃ£o/AlteraÃ§Ã£o do Titular" >					  
						
						<mod:selecao var="carenciat"
									 titulo="CARÃŠNCIA (Isento se protocolado na SEBEN atÃ© 20 dias do evento)"
									 opcoes = "NÃ£o;Sim" />
						<mod:grupo>
						<mod:selecao var="tipoplanot"
									titulo="Tipo de plano"
									opcoes="[Selecione];DAME1;DAME2;SUPERIOR" />
						<mod:data titulo="Data de nascimento" var="dataNascimentoTitular" />
						
						</mod:grupo>
						
						<mod:texto titulo="C.P.F:" var="cpftitular"/>
						<mod:grupo>
							<mod:data titulo="Data do exercÃ­cio" var="dataExercicioTitular"/>
						</mod:grupo>
							<mod:texto titulo="NÂº da Identidade" var="identidadetitular"/>							
							<mod:texto titulo="Ã“rgÃ£o expedidor" var="orgaoexpedidort"/>
						<mod:grupo>
							<mod:data var="dataExpedicaoTitular" titulo="Data da ExpediÃ§Ã£o" />
						</mod:grupo>						
						<mod:texto titulo="Nome da mÃ£e do titular" var="maetitular" largura="70"/>	
			
	</mod:grupo>
	
	
	</c:when>
	<c:when test="${tcliente == 'Dependente'}">
	
	<mod:grupo titulo="4 - InclusÃ£o/AlteraÃ§aÃµ de Dependentes Diretos"/>
			<mod:selecao var="quantidadeDependentes"
				titulo="Quantidade de dependentes diretos"
				opcoes="0;1;2;3" reler="ajax"
				idAjax="quantidadeDependenteAjax" />
				<mod:grupo depende="quantidadeDependenteAjax">
				<c:if test="${quantidadeDependentes != 0}">
						<c:forEach var="i" begin="1" end="${quantidadeDependentes}">
								<mod:grupo largura="70">
									
									<mod:texto titulo="Nome Dependente" var="nomedep${i}" largura="70"/>
								</mod:grupo>
								<mod:grupo largura="30">
									
									<mod:selecao titulo="Tipo Dependente" var="tipodep${i}" opcoes="[Selecione];Conjuge/Companheiro(a);Filho menor de 18 anos;Filho enter 18 e 24 anos;enteado;Menor sob guarda;Filho maior invÃ¡lido;Outros" reler="ajax" idAjax="tipodepAjax${i}" />
								       <c:if test="${requestScope[f:concat('tipodep',i)] == 'Outros'}">
									         <mod:texto var="outroDependente${i}" titulo="" largura="30" />
								       </c:if>    
								</mod:grupo>
									<mod:selecao var="carenciadep${i}"
												 titulo="CARÃŠNCIA (Isento se protocolado na SEBEN atÃ© 20 dias do evento)"
												 opcoes = "NÃ£o;Sim" />
									 <mod:selecao var="tipoplanodep${i}"
												 titulo="Tipo de plano"
												 opcoes="[Selecione];DAME1;DAME2;SUPERIOR" />	
									<mod:grupo>
										<mod:data titulo="Data de nascimento" var="dataNascimentoDependente${i}"/>										
									</mod:grupo>									
									<mod:grupo depende="tipodepAjax${i}">										
										 <c:choose>
											 <c:when test="${requestScope[f:concat('tipodep',i)] == 'Conjuge/Companheiro(a)'}">
												
												<mod:grupo>												     
											         <mod:texto titulo="C.P.F" var="cpfdependente${i}"/>
											         <mod:selecao var="escolhepispasepDep${i}" titulo="PIS/PASEP" opcoes="[ESCOLHER];PIS;PASEP" />
											         <mod:texto titulo="Numero" var="pispasepdep${i}"/>
											    </mod:grupo>
										    </c:when>
										    <c:otherwise>
										    	<mod:grupo>
										    	     <mod:mensagem titulo="C.P.F" />
											         <mod:texto var="cpfdependente${i}"/>
											         <mod:selecao var="escolhecpfdep${i}" opcoes="[Selecione];do prÃ³prio;do responsÃ¡vel" />
											    </mod:grupo>
										    </c:otherwise>
										</c:choose>
									</mod:grupo>
									<mod:grupo>				
									<mod:texto titulo="NÂº da Identidade" var="identidadedependende${i}"/>
									<mod:mensagem titulo="Ã“rgÃ£o expedidor" />
									<mod:texto var="orgaoexpedidordep${i}"/>
									</mod:grupo>
									
									<mod:grupo>
										<mod:data var="dataExpedicaoDependente${i}" titulo="Data da ExpediÃ§Ã£o" />									
									</mod:grupo>								
								    	     
									 <mod:selecao reler="ajax" idAjax="estrangeiroAjax${i}" var="estrangeiro${i}" titulo="Estrangeiro?" opcoes="NÃ£o;Sim" />
									     <mod:grupo depende="estrangeiroAjax${i}">
										   	<c:if test="${requestScope[f:concat('estrangeiro',i)] == 'Sim'}">
									      		<mod:selecao var="tipoDocumento${i}" titulo="Tipo de documento" opcoes="[ESCOHER];Passaporte;Identidade"/>
										         		<mod:texto titulo="Numero" var="docestrangeirodep${i}" largura="70"/>
											</c:if>
										</mod:grupo>					   
													
									<mod:texto titulo="Nome da mÃ£e do dependente"  var="maedependente${i}" largura="70"/>
						</c:forEach>
				</c:if>	
			</mod:grupo>
			
		</c:when>
		<c:otherwise>
			
		 <c:if test="${ tcliente =='Agregado'}">			
			<mod:grupo titulo="5 - InclusÃ£o/AlteraÃ§Ã£o de Agregados e Dependentes econÃ´micos" />
			<mod:selecao var="quantidadeAgregados"
				titulo="Quantidade de agregados e dependentes"
				opcoes="0;1;2" reler="ajax" idAjax="quantidadeAgregados" />
			<mod:grupo depende="quantidadeAgregados">
				<c:forEach var="j" begin="1" end="${quantidadeAgregados}">
						<mod:grupo largura="70">
		                    				
							<mod:texto titulo="Nome do agregado" var="nomeagr${j}" largura="70" />
						</mod:grupo>
						<mod:grupo largura="30">
						    <mod:mensagem titulo="Tipo de agregado" />
							<mod:selecao var="tipoagr${j}" opcoes="[Selecione];Pai;MÃ£e;Outros" reler="ajax" idAjax="tipoagrAjax${j}"/>
								<mod:grupo depende="tipoagrAjax${j}">
									 <c:if test="${requestScope[f:concat('tipoagr',j)] == 'Outros'}">
									         <mod:texto var="outroAgregado${j}" titulo="DescriÃ§Ã£o do agregado:" largura="30" />
								     </c:if>
								</mod:grupo>
						</mod:grupo>
						<mod:grupo>
						<mod:mensagem titulo="Estado Civil" />
							<mod:selecao var="ecivilagr${j}" opcoes="[Selecione];Solteiro(a);Casado(a);Separado(a);Divorciado(a);Viuvo(a);Outros"/>
						</mod:grupo>
						<mod:selecao var="carenciaagr${j}"
										 titulo="CARÃŠNCIA (Isento se protocolado na SEBEN atÃ© 20 dias do evento"
										 opcoes = "[SELECIONE];NÃ£o;Sim" />
						<mod:grupo> 
						<mod:selecao var="tipoplanoagr${j}"
										 titulo="Tipo de plano"
										 opcoes="[Selecione];DAME1;DAME2;SUPERIOR" />
										 
						</mod:grupo>
						<mod:selecao var="depeconomicotitular${j}"
										 titulo="Depende economicamente do titular"
										 opcoes = "NÃ£o;Sim" />
						<mod:grupo>
							<mod:data var="dataNascimentoAgregado${j}" titulo="Data de nascimento" />	
						</mod:grupo>
						<mod:grupo>							
								<mod:texto titulo="CPF" var="cpfagregado${j}"/>
								<mod:selecao var="titularCPF${i}" opcoes="[Selecione];do prÃ³prio;do responsÃ¡vel" />
						</mod:grupo>
							<mod:mensagem titulo="NÂº da Identidade" />
							<mod:texto var="identidadeagr${j}"/>
							<mod:mensagem titulo="Ã“rgÃ£o expedidor" />
							<mod:texto var="orgaoexpedidoragr${j}"/>
							<mod:grupo>
								<mod:data var="dataExpedicaoAgregado${j}" titulo="Data da ExpediÃ§Ã£o"/>							
								
								</mod:grupo>
								 <mod:selecao reler="ajax" idAjax="estrangeiroagrAjax${i}" var="estrangeiroagr${i}" titulo="Estrangeiro?" opcoes="NÃ£o;Sim" />
									     <mod:grupo depende="estrangeiroagrAjax${i}">
										   	<c:if test="${requestScope[f:concat('estrangeiroagr',i)] == 'Sim'}">
									      		<mod:selecao var="tipoDocumentoagr${i}" titulo="Tipo de documento" opcoes="[ESCOHER];Passaporte;Identidade"/>
										         		<mod:texto titulo="Numero" var="docestrangeiroagr${i}" />
											</c:if>
										</mod:grupo>						
							
							<mod:texto titulo="Nome da mÃ£e do agregado" largura="70" var="maeagregado"/>
							
				</c:forEach>
			</mod:grupo>
			</c:if>
		</c:otherwise>
	</c:choose>
	</mod:grupo>
				
</mod:entrevista>
	
<mod:documento>	
	
	
		<h2>Golden Cross - movimentaÃ§Ã£o cadastral  | ${operacao}</h2>
	
	
	<table width="100%">
	<tr>
	<td> Finalidade: <c:if test="${operacao=='InclusÃ£o'}">InclusÃ£o no cadastro</c:if><c:if test="${operacao=='AtualizaÃ§Ã£o'}">AtualizaÃ§Ã£o do cadastro</c:if></td>
	<td> Tipo de cliente: <c:if test="${tcliente=='Titular'}">Titular</c:if><c:if test="${tcliente=='Dependente'}">Dependente</c:if><c:if test="${tcliente=='Agregado'}">Agregado</c:if></td>
	<td> partir de: ${diavalidadet}/${mesvalidadet}/${anovalidadet} </td>
	</tr>				
	</table>
	
	<table width="100%" border="1" cellpadding="2" cellspacing="1">
				<tr>
	                <td>IdentificaÃ§Ã£o do Titular: ${requestScope['nometit_pessoaSel.descricao']}<br>MatrÃ­cula: ${f:pessoa(requestScope['nometit_pessoaSel.id']).matricula}<br/>LotaÃ§Ã£o: ${f:pessoa(requestScope['nometit_pessoaSel.id']).lotacao.nomeLotacao}<br/><c:if test="${tcliente == 'Titular'}">CarÃªncia(Isento se protocolado atÃ© 20 dias do evento): ${carenciat} <br/>Tipo de plano: ${tipoplanot}<br/> Data de nascimento: ${dataNascimentoTitular} <br/> C.P.F: ${cpftitular}<br/> Data de exercicio:  ${dataExercicioTitular}<br/> Identidade: ${identidadetitular} Expedidor: ${orgaoexpedidort}<br/> Data da expediÃ§Ã£o: ${dataExpedicaoTitular}<br/> Nome da mÃ£e do titular: ${maetitular}</c:if></td>
				</tr>
	</table>
	
	<table width="100%">
	<tr>
	<td><b>* MovimentaÃ§Ãµes com efeito financeiro somente podem ser feitas com vigÃªncias 1Âº,11 e 21de cada mÃªs, sendo necessÃ¡rio que tais solicitaÃ§Ãµes cheguem a SEBEN/SRH com 4 dias Ãºteis de antecedÃªnciaem relaÃ§Ã£o a dat pretendida</b> </br></br> <b> * Caso altere seu tipo de planopara categoria hierarquicamente acima (ex: DAME! para DAME2 ou SUPERIOR, DAME@ para SUPERIOR) e haja internaÃ§Ã£oclÃ­nica ou cirÃºrgica serÃ¡ necessÃ¡rio permanecer por 12 (doze) meses apÃ³s alta hospitalar antes de retomar a categoria anterior</b> </td> 
	</tr>
	
	</table>
	<table width="100%">
	<tr>
	<td><b>Obs: 1 - Nos casos de inclusÃ£o e atualizaÃ§Ã£o, o preenchimento dp CPF Ã© pbrigatÃ³rio, seja do prÃ³prio ou do resposÃ¡vel legal. Em sua ausÃªncia, deverÃ¡ ser informado o nro do PIS/PASEP. Se estrangeiro, nÃºmero do passaporte ou carteira civil;</br> 2- Caso opte pelo plano SUPERIOR, todos os dependentes diretos  serÃ£o tambÃ©m nele cadastrados.</b></td>
    </tr>
    </table>
   
    <table width="100%">
    <tr>
    <td><b>A inclusÃ£o de companheiro(a), enteado(a), menor sob guarda, dependente economico, ou beneficiÃ¡rio designado, somente poderÃ¡ ser feita apÃ³s defereimento do Exmo Sr. Presidente do TRF-2</b></td>
    </tr>
    </table>
    </br>
    
    <c:choose>    
	    <c:when test="${tcliente == 'Dependente'}">
	
	      <c:if test="${quantidadeDependentes > '0'}">
	
			<table width="100%">
				<tr><td align="center">DEPENDENTES DIRETOS</td></tr>
			</table>
			
			<c:forEach var="i" begin="1" end="${quantidadeDependentes}">
				  <table width="100%">
						<tr>
							<c:choose>
							    <c:when test="${requestScope[f:concat('tipodep',i)] == 'Conjuge/Companheiro(a)'}">	
							    
							        <td>Nome do conjuge/Companheiro(a): ${requestScope[f:concat('nomedep',i)]}<br>CarÃªncia(Isento se protocolado atÃ© 20 dias do evento): ${requestScope[f:concat('carenciadep',i)]}<br/> Tipo de plano: ${requestScope[f:concat('tipoplanodep',i)]}</br> Data de nascimento: ${requestScope[f:concat('dataNascimentoDependente',i)]} <br/> C.P.F: ${requestScope[f:concat('cpfdependente',i)]}</br>${requestScope[f:concat('escolhepispasepDep',i)]}: ${requestScope[f:concat('pispasepdep',i)]} <br/> Identidade: ${requestScope[f:concat('identidadedependende',i)]}, Expedidor: ${requestScope[f:concat('orgaoexpedidordep',i)]} Data da expediÃ§Ã£o: ${requestScope[f:concat('dataExpedicaoDependente',i)]} <c:if test="${requestScope[f:concat('estrangeiro',i)]=='Sim'}"> </br>${requestScope[f:concat('tipoDocumento',i)]}- ${requestScope[f:concat('docestrangeirodep',i)]}</c:if></br>Nome da mÃ£e do Conjuge/Companheiro(a): ${requestScope[f:concat('maedependente',i)]}</td>						
								       
        				        </c:when>
        				        
							    <c:otherwise>
							            
							        <td>Nome do dependente: ${requestScope[f:concat('nomedep',i)]}<br>CarÃªncia(Isento se protocolado atÃ© 20 dias do evento): ${requestScope[f:concat('carenciadep',i)]} <br/>Tipo de plano: ${requestScope[f:concat('tipoplanodep',i)]}</br>Parentesco: ${requestScope[f:concat('tipodep',i)]} ${requestScope[f:concat('outroDependente',i)]}</br> Data de nascimento: ${requestScope[f:concat('dataNascimentoDependente',i)]}<br/> C.P.F ${requestScope[f:concat('cpfdependente',i)]} ${requestScope[f:concat('escolhecpfdep',i)]}</br> Identidade: ${requestScope[f:concat('identidadedependende',i)]}, Expedidor: ${requestScope[f:concat('orgaoexpedidordep',i)]}, Data da expediÃ§Ã£o: ${requestScope[f:concat('dataExpedicaoDependente',i)]}<c:if test="${requestScope[f:concat('estrangeiro',i)]=='Sim'}"> </br>${requestScope[f:concat('tipoDocumento',i)]}- ${requestScope[f:concat('docestrangeirodep',i)]}</c:if></br>Nome da mÃ£e do titular: ${requestScope[f:concat('maedependente',i)]}</td>
				                       
							    </c:otherwise>
							</c:choose>
						</tr>	
					</table>
			</c:forEach>
	      </c:if>
	   </c:when>
	
	   <c:otherwise>
	   
	     <c:if test="${ tcliente == 'Agregado' }">
	  
		   <c:if test="${quantidadeAgregados > '0'}">
	
			   <table width="100%">
				   <tr><td align="center">AGREGADOS E DEPENDENTES</td></tr>
			   </table>
			   <c:forEach var="j" begin="1" end="${quantidadeAgregados}">
				       	<table width="100%">
						    <tr>
							
							    <td><p>Nome do agregado: ${requestScope[f:concat('nomeagr',j)]}<br>CarÃªncia(Isento se protocolado atÃ© 20 dias do evento): ${requestScope[f:concat('carenciaagr',j)]}<br/> Tipo de plano: ${requestScope[f:concat('tipoplanoagr',j)]}</br>Parentesco: ${requestScope[f:concat('tipoagr',j)]} ${requestScope[f:concat('outroAgregado',j)]} <br/>Estado civil: ${requestScope[f:concat('ecivilagr',j)]}<br/>Data Nascimento: ${requestScope[f:concat('dataNascimentoAgregado',j)]} <br/> 
							    C.P.F: ${requestScope[f:concat('cpfagregado',j)]}<br/> Depende economicamente do titular? ${requestScope[f:concat('depeconomicotitular',j)]}</br> Identidade: ${requestScope[f:concat('identidadeagr',j)]}, Expedidor: ${requestScope[f:concat('orgaoexpedidoragr',j)]}<br/> Data da expediÃ§Ã£o: ${requestScope[f:concat('dataExpedicaoAgregado',j)]}<c:if test="${requestScope[f:concat('estrangeiroagr',i)]=='Sim'}"> </br>${requestScope[f:concat('tipoDocumentoagr',i)]}- ${requestScope[f:concat('docestrangeiroagr',i)]}</c:if></br>Nome da mÃ£e do agregado: ${maeagregado}</p></td>
							
						    </tr>
					    </table>
			   </c:forEach>	
		   </c:if>
	     </c:if>
	     
	   </c:otherwise>
	   
	</c:choose>
	</br>
	
	<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
	<br/>
	<p>Recebido pela SEBEN em:_____/_____/____ - por:_______________________</p>
	
	</br>
	<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
	
	</mod:documento>
</mod:modelo>

