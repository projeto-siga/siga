<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8" buffer="64kb"%>
<script language="javascript">
var newwin = null;
</script>

<mod:modelo>
	<mod:entrevista>
		<mod:selecao titulo="Autorizante" var="autoridade" opcoes="Juiz Federal - Diretor do Foro;Presidente do Tribunal Regional Federal da 2Âª RegiÃ£o" reler="sim" />
			<br/><br/>
        <c:set var="intervaloMsg">
&nbsp;&nbsp;&nbsp;O servidor submetido Ã  jornada ininterrupta poderÃ¡ prestar serviÃ§o extraordinÃ¡rio desde que, no dia da prestaÃ§Ã£o do serviÃ§o, cumpra jornada de oito horas de trabalho com intervalo de, no mÃ­nimo, uma hora (Â§1Âº do Art. 45, ResoluÃ§Ã£o nÂº 4/2008 - CJF, alterado pela ResoluÃ§Ã£o nÂº 173/2011 - CJF).</br>&nbsp;&nbsp;&nbsp;Na hipÃ³tese de prestaÃ§Ã£o de serviÃ§o extraordinÃ¡rio em fins de semana/feriados, somente a sobrejornada igual ou superior a 8 (oito) horas poderÃ¡ conter intervalo para almoÃ§o, a teor do artigo 1Âº, caput, da ResoluÃ§Ã£o nÂº 88/2009, do Conselho Nacional de JustiÃ§a. </c:set>
	<mod:grupo >    
            <mod:radio titulo="A presente solicitaÃ§Ã£o observa a antecedÃªncia mÃ­nima de 5 (cinco) dias Ãºteis, prevista no artigo 42, parÃ¡grafo 2Âº, da ResoluÃ§Ã£o nÂº 4/2008-CJF ." var="Antecedencia" valor="1" reler="nÃ£o" marcado="Sim"/> </br>
            <mod:radio titulo="NÃ£o foi possÃ­vel observar a antecedÃªncia mÃ­nima de 5 (cinco) dias Ãºteis, prevista no artigo 42, parÃ¡grafo 2Âº, da ResoluÃ§Ã£o nÂº 4/2008-CJF, " var="Antecedencia" valor="2" reler="nÃ£o" gerarHidden="NÃ£o"/>	
            haja vista&nbsp;&nbsp;<mod:memo colunas="80" linhas="5" var="antecedenciaNao" titulo="" />
            
    </mod:grupo>	
		<mod:grupo>		
			</br><mod:texto titulo="NÃºmero de servidores a serem incluÃ­dos"
				var="numIncluidos"	reler="ajax" idAjax="numIncluidosAjax" largura="2"/>
			<mod:grupo depende="numIncluidosAjax">
				<c:forEach var="i" begin="1" end="${numIncluidos}">
					<mod:grupo>
					</br>
					_____________________________________________________________________________________________________________________________________________________________________</br></br>
						<b>Dados do ${i}Âº servidor que prestarÃ¡ hora extra:</b>
						<mod:grupo>
							<mod:pessoa titulo="MatrÃ­cula" var="servidor${i}" />							
						</mod:grupo>
						<mod:grupo>
							<mod:selecao
								titulo="NÃºmero de dias a serem trabalhados pelo servidor"
								var="numDatas${i}" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15"
								reler="ajax" idAjax="numDatasAjax${i}" /> </br></br>
						</mod:grupo>
						<mod:grupo depende="numDatasAjax${i}">
							<c:forEach var="j" begin="1" end="${requestScope[f:concat('numDatas',i)]}">
								<mod:grupo titulo="ServiÃ§o ExtraordinÃ¡rio">
								<mod:grupo depende="Func${i}${j}">
								    <mod:data titulo="Data do ServiÃ§o ExtraordinÃ¡rio" var="dataServico${i}${j}"></mod:data> &nbsp;&nbsp;&nbsp;&nbsp;
									<mod:selecao titulo="Dia da Semana" var="dataSemana${i}${j}" opcoes="SÃ¡bado;Domingo;Segunda;TerÃ§a;Quarta;Quinta;Sexta"/> &nbsp;&nbsp;&nbsp;&nbsp;
									<mod:selecao titulo="Feriado?" var="feriado${i}${j}" opcoes="NÃ£o;Sim"/>&nbsp;&nbsp;&nbsp;&nbsp;
									<mod:selecao titulo="Adicional (AD) ou Banco de horas?" var="adicionalBanco${i}${j}" opcoes="BH;AD"/></br>
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
						    		largura="2" maxcaracteres="2" titulo="HorÃ¡rio inicial" var="horaIni${i}${j}" reler="ajax" idAjax="Func${i}${j}" />h 
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }" titulo="" largura="2" maxcaracteres="2" var="minutoIni${i}${j}" reler="ajax" idAjax="Func${i}${j}" />m &nbsp;&nbsp;&nbsp;&nbsp;
						    		<span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=225,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${intervaloMsg}');"><u>
                                    Intervalo?</u></span>
						    		<mod:selecao titulo="" var="intervalo${i}${j}" opcoes="NÃ£o;Sim" reler="ajax" idAjax="Func${i}${j}" />&nbsp;&nbsp;&nbsp;&nbsp;
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"largura="2" maxcaracteres="2" titulo="HorÃ¡rio final" var="horaFim${i}${j}" reler="ajax" idAjax="Func${i}${j}" />h
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"titulo="" largura="2" maxcaracteres="2" var="minutoFim${i}${j}" reler="ajax" idAjax="Func${i}${j}" />m&nbsp;&nbsp;&nbsp;&nbsp;

                            
                                 <c:set var="xintervalo" value="${requestScope[f:concat(f:concat('intervalo',i),j)]}" />
                                 <fmt:formatNumber var="xhoraIni" value="${requestScope[f:concat(f:concat('horaIni',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 <fmt:formatNumber var="xhoraFim" value="${requestScope[f:concat(f:concat('horaFim',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 <fmt:formatNumber var="xminutoIni" value="${requestScope[f:concat(f:concat('minutoIni',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 <fmt:formatNumber var="xminutoFim" value="${requestScope[f:concat(f:concat('minutoFim',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 

                                <c:if test="${(xhoraIni > 24)}"> 
								 <p style="color:red">Hora de inÃ­cio deve ser menor ou igual a 24</p>
								 <c:set var="condicional" value="nao"/>
								</c:if>
								
								<c:if test="${(xminutoIni > 59)}"> 
                                 <p style="color:red">Minuto de inÃ­cio deve ser menor ou igual a 59</p>
                                 <c:set var="condicional" value="nao"/>
                                </c:if>
                                
								<c:if test="${(xhoraFim > 24)}"> 
								 <p style="color:red">Hora de fim deve ser menor ou igual a 24.</p>
							 	<c:set var="condicional" value="nao"/>
								</c:if>
								
								<c:if test="${(xminutoFim > 59)}"> 
								 <p style="color:red">Minuto de fim deve ser menor ou igual a 59.</p>
								 <c:set var="condicional" value="nao"/>
								</c:if>

                                <c:if test="${(xhoraIni > xhoraFim)}">
                                 <p style="color:red">HorÃ¡rio inicial maior que HorÃ¡rio final.</p>
                                 <c:set var="condicional" value="nao"/>
                                </c:if>
                                <c:if test="${(xhoraIni == xhoraFim)&&(xminutoIni > xminutoFim) }">
                                 <p style="color:red">HorÃ¡rio inicial maior que HorÃ¡rio final.</p>
                                 <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                               
                                 <c:if test="${(not empty xhoraIni)&& (not empty xhoraFim) && (not empty xminutoFim) && (not empty xminutoIni)&& (condicional != 'nao')&& (xintervalo =='NÃ£o')}">
                                     <fmt:formatNumber var="xhoraTotalC" value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))/60)}" type="number" pattern="##.##" />
                                     <fmt:parseNumber var="xhoraTotal" value="${xhoraTotalC}" type="number" integerOnly="true"/>
                                     <fmt:formatNumber var="xminutoTotal" value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))%60)}" maxFractionDigits="0" minIntegerDigits="2" />
                                     <mod:oculto var="totalHoras${i}${j}" valor="${xhoraTotal}" />
                                     <mod:oculto var="totalMinutos${i}${j}" valor="${xminutoTotal}"/>                                           
                                Total de horas solicitadas:<input type="text" name="totalHoras${i}${j}" value="${xhoraTotal}" size="2" readonly/>h <input type="text" name="totalMinutos${i}${j}" value="${xminutoTotal}" size="2" readonly/>min  
                                 </c:if>
                                 
                                 <c:if test="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))<= 60) && (xintervalo =='Sim') }">
                                    <c:set var="condicional" value="nao"/>                                
                                    <p style="color:red"> Para preencher esta opÃ§Ã£o, clicar em â€œ<u>Intervalo ?</u>â€ e ler o conteÃºdo. </p>
                                 </c:if>  
                                 
                                 <c:if test="${(not empty xhoraIni)&& (not empty xhoraFim) && (not empty xminutoFim) && (not empty xminutoIni)&& (condicional != 'nao') && (xintervalo =='Sim')}">
                                     <fmt:formatNumber var="xhoraTotalC" value="${((((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))/60)-1)}" type="number" pattern="##.##" />
                                     <fmt:parseNumber var="xhoraTotal" value="${xhoraTotalC}" type="number" integerOnly="true"/>
                                     <fmt:formatNumber var="xminutoTotal"  value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))%60)}" maxFractionDigits="0" minIntegerDigits="2"  />
                                     <mod:oculto var="totalHoras${i}${j}" valor="${xhoraTotal}" />
                                     <mod:oculto var="totalMinutos${i}${j}" valor="${xminutoTotal}"/>
                                Total de horas solicitadas:<input type="text" name="totalHoras${i}${j}" value="${xhoraTotal}" size="2" readonly/>h <input type="text" name="totalMinutos${i}${j}" value="${xminutoTotal}" size="2" readonly/>min
                                </c:if>
                                
                                <c:if test="${(xhoraTotal > 10) or ((xhoraTotal== 10)and(xminutoTotal > 0))}">
                                 <p style="color:red">Total de Hora Extra nÃ£o pode ser maior que 10h00m.</p>
                                 </c:if>
                                
                                <c:if test="${((xhoraTotal > 7)||((xhoraTotal==7)&&(xminutoTotal>0))) && (xintervalo == 'NÃ£o')}"> 
                                 <p style="color:red">Para total de horas maior do que 7 horas deve-se obrigatoriamente informar um intervalo de 1 hora</p>
                                 <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                                
                                </mod:grupo>

								
								</mod:grupo>
							</c:forEach>
						</mod:grupo>
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
		</mod:grupo>
		<mod:grupo>
		<c:set var="artigo47">
A prestaÃ§Ã£o remunerada de serviÃ§o extraordinÃ¡rio aos sÃ¡bados, domingos e feriados somente serÃ¡ admitida nos seguintes casos:</br>&nbsp;&nbsp;- Para realizaÃ§Ã£o de atividades essenciais que nÃ£o possam ser exercidas em dias Ãºteis;</br>&nbsp;&nbsp; - Para eventos que ocorram nesses dias, desde que seja impossÃ­vel adotar escala de revezamento ou realizar a devida compensaÃ§Ã£o;</br>&nbsp;&nbsp;- Quando ocorrerem situaÃ§Ãµes que requeiram reparos inadiÃ¡veis e imediato atendimento e sejam decorrentes de fatos supervenientes;</br>&nbsp;&nbsp;- Para colocaÃ§Ã£o em dia de tarefas especÃ­ficas mediante plano de esforÃ§o concentrado aprovado pelo (...) Diretor da Secretaria Geral, nas SeÃ§Ãµes JudiciÃ¡rias.
        </c:set>
<c:set var="artigo">
            <b>foram observados os requisitos previstos no  
            <span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=325,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${artigo47}');"><u>
            artigo 47</u></span>,caput, da ResoluÃ§Ã£o nÂº 4/2008-CJF, com a redaÃ§Ã£o dada pela ResoluÃ§Ã£o nÂº 173/2011-CJF. </b>
</c:set>
           
                                </br>
                                <mod:caixaverif var="normaRegul" titulo="Esta solicitaÃ§Ã£o observa os limites de horas-extras estabelecidos na norma regulamentar: 2 (duas) horas nos dias Ãºteis, 10 (dez) horas semanais (de domingo a sÃ¡bado), 44 (quarenta e quatro) horas mensais e 134 (cento e trinta e quatro) horas anuais." reler="nao" marcado="NÃ£o" obrigatorio="Sim" />                                
                                </br></br>
                                No caso de a solicitaÃ§Ã£o se referir ao adicional de serviÃ§o extraordinÃ¡rio realizado em fins de semana e feriados:   
                                  </br> <input type="radio" name="adicionalServico" id="aplica" value="aplica" checked><label for="artigo47">${artigo} </label>
                                <br>
                                  <input type="radio" name="adicionalServico" id="naoAplica" value="naoAplica">  <label for="naoAplica">nÃ£o se aplica.</label><br>

                                 </br></br>		      
			<mod:memo colunas="70" linhas="3"
				titulo="As horas-extras acima se mostram necessÃ¡rias, pois (MANIFESTAÃ‡ÃƒO FUNDAMENTADA)"
				var="motivoHora" /> 
		</mod:grupo>
		<mod:grupo>
			<mod:memo colunas="70" linhas="2" titulo="A(s) mencionada(s) tarefa(s) sÃ³ pode (em) ser realizada(s) durante esse perÃ­odo, eis que (MANIFESTAÃ‡ÃƒO FUNDAMENTADA </br>quanto Ã  impossibilidade de realizaÃ§Ã£o do serviÃ§o durante o expediente regulamentar ou em dias Ãºteis, na hipÃ³tese de </br>prestaÃ§Ã£o de serviÃ§o extraordinÃ¡rio em fins de semana e feriados)"
				var="justificativa" /></br></br>
		</mod:grupo>
		<mod:grupo><p style="color:red">
		Obs.1: Quem assina esta solicitaÃ§Ã£o Ã© o diretor da unidade da prestaÃ§Ã£o do serviÃ§o extraordinÃ¡rio.</br></br>
        Obs.2: Na hipÃ³tese de convocaÃ§Ã£o, para prestaÃ§Ã£o de serviÃ§o extraordinÃ¡rio, de servidor de outra unidade de lotaÃ§Ã£o, tambÃ©m Ã© necessÃ¡rio que o titular da unidade de origem daquele servidor seja incluÃ­do como cossignatÃ¡rio na solicitaÃ§Ã£o.
		</p>
		</mod:grupo>
		

	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br /><br /><br /></tr>
						<tr>
							<td align="right"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							SOLICITAÃ‡ÃƒO N&ordm; ${doc.codigo}</font></td>
						</tr>	
						<tr>
							<td align="right"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							${doc.dtExtenso}</font></td>
						</tr>											
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		
		<p style="font-size: 11pt; text-indent: 3cm; font-weight: bold;">Exmo. Senhor ${autoridade},</p>
		<br />
		<p style="text-align: justify; text-indent: 3cm;">Solicito
		autorizaÃ§Ã£o para prestaÃ§Ã£o de serviÃ§o extraordinÃ¡rio segundo a escala
		abaixo discriminada, de acordo com os artigos 42 e seguintes da 
		ResoluÃ§Ã£o nÂº 4/2008, do Conselho da JustiÃ§a Federal.</p>
		<br />

		<table width="100%" align="center" border="1" cellpadding="2"
			cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="20%" align="center"><p style="font-size:11px">Servidor</p></td>
				<td bgcolor="#FFFFFF" width="07%" align="center"><p style="font-size:11px">MatrÃ­cula</p></td>
				<td bgcolor="#FFFFFF" width="5%" align="center"><p style="font-size:11px">LotaÃ§Ã£o</p></td>
				<td bgcolor="#FFFFFF" width="10%" align="center"><p style="font-size:9px">Data do ServiÃ§o ExtraordinÃ¡rio</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:11px">Dia da semana</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:11px">Feriado?</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:10px">Adicional ou Banco de Horas</p></td>
				<td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">HorÃ¡rio Inicial</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:11px">Intervalo?</p></td>
				<td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">HorÃ¡rio Final</p></td>
				<td bgcolor="#FFFFFF" width="10%"><p style="font-size:10px">Total de horas solicitadas</p></td>
				
			</tr>
		
		<c:forEach var="i" begin="1" end="${numIncluidos}">
			<c:set var="servidorX"
				value="${requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')]}" />

				<tr>
					<td bgcolor="#FFFFFF" width="20%" align="center"><p style="font-size:10px">${f:maiusculasEMinusculas(f:pessoa(servidorX).nomePessoa)}</p></td>
					<td bgcolor="#FFFFFF" width="07%" align="center"><p style="font-size:11px">${f:pessoa(servidorX).matricula}</p></td>
					<td bgcolor="#FFFFFF" width="5%" align="center"><p style="font-size:8px">${f:pessoa(servidorX).lotacao.descricao}</p></td>
					<c:forEach var="j" begin="1"
						end="${requestScope[f:concat('numDatas',i)]}">
						<ww:if test="${j == 1}">
							<td bgcolor="#FFFFFF" width="10%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('dataServico',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('dataSemana',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('feriado',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('adicionalBanco',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('horaIni',i),j)]}:${requestScope[f:concat(f:concat('minutoIni',i),j)]}:00</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('intervalo',i),j)]}</p></td>
						    <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('horaFim',i),j)]}:${requestScope[f:concat(f:concat('minutoFim',i),j)]}:00</p></td>
                            <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('totalHoras',i),j)]}:${requestScope[f:concat(f:concat('totalMinutos',i),j)]}:00</p></td>
						</ww:if>
						<ww:else>
							<tr>
							<td bgcolor="#FFFFFF" width="20%" align="center"><p style="font-size:11px">${f:maiusculasEMinusculas(f:pessoa(servidorX).nomePessoa)}</p></td>
                            <td bgcolor="#FFFFFF" width="07%" align="center"><p style="font-size:11px">${f:pessoa(servidorX).matricula}</p></td>
                            <td bgcolor="#FFFFFF" width="05%" align="center"><p style="font-size:11px">${f:pessoa(servidorX).lotacao.descricao}</p></td>
							<td bgcolor="#FFFFFF" width="10%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('dataServico',i),j)]}</p></td>
                            <td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('dataSemana',i),j)]}</p></td>
                            <td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('feriado',i),j)]}</p></td>
                            <td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('adicionalBanco',i),j)]}</p></td>
                            <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('horaIni',i),j)]}:${requestScope[f:concat(f:concat('minutoIni',i),j)]}:00</p></td>
                            <td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('intervalo',i),j)]}</p></td>
                            <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('horaFim',i),j)]}:${requestScope[f:concat(f:concat('minutoFim',i),j)]}:00</p></td>
                            <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('totalHoras',i),j)]}:${requestScope[f:concat(f:concat('totalMinutos',i),j)]}:00</p></td>
							</tr>
						</ww:else>
					</c:forEach>

				</tr>
			
		</c:forEach>
	</table>
		<p style="text-align: justify; text-indent: 3cm;">As horas-extras
		acima se mostram necessÃ¡rias, pois &nbsp;${motivoHora}</p>
		<c:if test="${tipoHoraExtra eq 'em fins de semana/feriados'}">
			<c:set var="tampouco" value="tampouco em dias Ãºteis," scope="request" />
		</c:if>
		<p style="text-align: justify; text-indent: 3cm;">A(s) mencionada(s) tarefa(s) sÃ³ pode (em) ser realizada(s) durante esse perÃ­odo,
		${tampouco} eis que &nbsp;${justificativa}</p>
        
        <c:if test="${Antecedencia == '2'}">
        <p style="text-align: justify; text-indent: 3cm;">
         NÃ£o foi possÃ­vel observar a antecedÃªncia mÃ­nima de 5 (cinco) 
        dias prevista no artigo 42, parÃ¡grafo 2Âº, da ResoluÃ§Ã£o nÂº 4/2008-CJF, 
        haja vista &nbsp; ${antecedenciaNao}</p>
        </c:if>
        
		<p style="text-align: justify; text-indent: 3cm;">Na hipÃ³tese de
		deferimento, comprometo-me a encaminhar Ã  Subsecretaria de GestÃ£o de
		Pessoas a ficha individual de frequÃªncia de serviÃ§o extraordinÃ¡rio
		preenchida, bem como atestada por mim e pela chefia imediata do(s)
		respectivo(s) servidor(es) atÃ© o 2Âº dia Ãºtil do mÃªs subsequente ao da
		prestaÃ§Ã£o do serviÃ§o, nos termos do artigo 49 da ResoluÃ§Ã£o
		supramencionada.</p>

		<p style="text-align: justify; text-indent: 3cm;">Atenciosamente,</p>


		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />




		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
	<mod:assinatura>
		<c:if test="${doc.orgaoUsuario.idOrgaoUsu == '1'}">
			{SolicitaÃ§Ã£o Assinada! Iniciando procedimento "ServiÃ§o extraordinÃ¡rio".}
			<c:set var="f" value="${f:criarWorkflow('ServiÃ§o extraordinÃ¡rio', doc, cadastrante, titular, lotaCadastrante, lotaTitular)}" />
	    </c:if>
	</mod:assinatura>
</mod:modelo>