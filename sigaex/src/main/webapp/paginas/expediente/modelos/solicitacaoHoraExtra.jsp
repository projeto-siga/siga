<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8" buffer="64kb"%>
<script language="javascript">
var newwin = null;
</script>

<mod:modelo>
	<mod:entrevista>
		<mod:selecao titulo="Autorizante" var="autoridade" opcoes="Juiz Federal - Diretor do Foro;Presidente do Tribunal Regional Federal da 2ª Região" reler="sim" />
			<br/><br/>
        <c:set var="intervaloMsg">
&nbsp;&nbsp;&nbsp;O servidor submetido à jornada ininterrupta poderá prestar serviço extraordinário desde que, no dia da prestação do serviço, cumpra jornada de oito horas de trabalho com intervalo de, no mínimo, uma hora (§1º do Art. 45, Resolução nº 4/2008 - CJF, alterado pela Resolução nº 173/2011 - CJF).</br>&nbsp;&nbsp;&nbsp;Na hipótese de prestação de serviço extraordinário em fins de semana/feriados, somente a sobrejornada igual ou superior a 8 (oito) horas poderá conter intervalo para almoço, a teor do artigo 1º, caput, da Resolução nº 88/2009, do Conselho Nacional de Justiça. </c:set>
	<mod:grupo >    
            <mod:radio titulo="A presente solicitação observa a antecedência mínima de 5 (cinco) dias úteis, prevista no artigo 42, parágrafo 2º, da Resolução nº 4/2008-CJF ." var="Antecedencia" valor="1" reler="não" marcado="Sim"/> </br>
            <mod:radio titulo="Não foi possível observar a antecedência mínima de 5 (cinco) dias úteis, prevista no artigo 42, parágrafo 2º, da Resolução nº 4/2008-CJF, " var="Antecedencia" valor="2" reler="não" gerarHidden="Não"/>	
            haja vista&nbsp;&nbsp;<mod:memo colunas="80" linhas="5" var="antecedenciaNao" titulo="" />
            
    </mod:grupo>	
		<mod:grupo>		
			</br><mod:texto titulo="Número de servidores a serem incluídos"
				var="numIncluidos"	reler="ajax" idAjax="numIncluidosAjax" largura="2"/>
			<mod:grupo depende="numIncluidosAjax">
				<c:forEach var="i" begin="1" end="${numIncluidos}">
					<mod:grupo>
					</br>
					_____________________________________________________________________________________________________________________________________________________________________</br></br>
						<b>Dados do ${i}º servidor que prestará hora extra:</b>
						<mod:grupo>
							<mod:pessoa titulo="Matrícula" var="servidor${i}" />							
						</mod:grupo>
						<mod:grupo>
							<mod:selecao
								titulo="Número de dias a serem trabalhados pelo servidor"
								var="numDatas${i}" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15"
								reler="ajax" idAjax="numDatasAjax${i}" /> </br></br>
						</mod:grupo>
						<mod:grupo depende="numDatasAjax${i}">
							<c:forEach var="j" begin="1" end="${requestScope[f:concat('numDatas',i)]}">
								<mod:grupo titulo="Serviço Extraordinário">
								<mod:grupo depende="Func${i}${j}">
								    <mod:data titulo="Data do Serviço Extraordinário" var="dataServico${i}${j}"></mod:data> &nbsp;&nbsp;&nbsp;&nbsp;
									<mod:selecao titulo="Dia da Semana" var="dataSemana${i}${j}" opcoes="Sábado;Domingo;Segunda;Terça;Quarta;Quinta;Sexta"/> &nbsp;&nbsp;&nbsp;&nbsp;
									<mod:selecao titulo="Feriado?" var="feriado${i}${j}" opcoes="Não;Sim"/>&nbsp;&nbsp;&nbsp;&nbsp;
									<mod:selecao titulo="Adicional (AD) ou Banco de horas?" var="adicionalBanco${i}${j}" opcoes="BH;AD"/></br>
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
						    		largura="2" maxcaracteres="2" titulo="Horário inicial" var="horaIni${i}${j}" reler="ajax" idAjax="Func${i}${j}" />h 
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }" titulo="" largura="2" maxcaracteres="2" var="minutoIni${i}${j}" reler="ajax" idAjax="Func${i}${j}" />m &nbsp;&nbsp;&nbsp;&nbsp;
						    		<span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=225,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${intervaloMsg}');"><u>
                                    Intervalo?</u></span>
						    		<mod:selecao titulo="" var="intervalo${i}${j}" opcoes="Não;Sim" reler="ajax" idAjax="Func${i}${j}" />&nbsp;&nbsp;&nbsp;&nbsp;
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }" largura="2" maxcaracteres="2" titulo="Horário final" var="horaFim${i}${j}" reler="ajax" idAjax="Func${i}${j}" />h
						    		<mod:texto valor="00" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;
                                    if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }" titulo="" largura="2" maxcaracteres="2" var="minutoFim${i}${j}" reler="ajax" idAjax="Func${i}${j}" />m&nbsp;&nbsp;&nbsp;&nbsp;

                            
                                 <c:set var="xintervalo" value="${requestScope[f:concat(f:concat('intervalo',i),j)]}" />
                                 <fmt:formatNumber var="xhoraIni" value="${requestScope[f:concat(f:concat('horaIni',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 <fmt:formatNumber var="xhoraFim" value="${requestScope[f:concat(f:concat('horaFim',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 <fmt:formatNumber var="xminutoIni" value="${requestScope[f:concat(f:concat('minutoIni',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 <fmt:formatNumber var="xminutoFim" value="${requestScope[f:concat(f:concat('minutoFim',i),j)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                 

                                <c:if test="${(xhoraIni > 24)}"> 
								 <p style="color:red">Hora de início deve ser menor ou igual a 24</p>
								 <c:set var="condicional" value="nao"/>
								</c:if>
								
								<c:if test="${(xminutoIni > 59)}"> 
                                 <p style="color:red">Minuto de início deve ser menor ou igual a 59</p>
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
                                 <p style="color:red">Horário inicial maior que Horário final.</p>
                                 <c:set var="condicional" value="nao"/>
                                </c:if>
                                <c:if test="${(xhoraIni == xhoraFim)&&(xminutoIni > xminutoFim) }">
                                 <p style="color:red">Horário inicial maior que Horário final.</p>
                                 <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                               
                                 <c:if test="${(not empty xhoraIni)&& (not empty xhoraFim) && (not empty xminutoFim) && (not empty xminutoIni)&& (condicional != 'nao')&& (xintervalo =='Não')}">
                                     <fmt:formatNumber var="xhoraTotalC" value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))/60)}" type="number" pattern="##.##" />
                                     <fmt:parseNumber var="xhoraTotal" value="${xhoraTotalC}" type="number" integerOnly="true"/>
                                     <fmt:formatNumber var="xminutoTotal" value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))%60)}" maxFractionDigits="0" minIntegerDigits="2" />
                                     <mod:oculto var="totalHoras${i}${j}" valor="${xhoraTotal}" />
                                     <mod:oculto var="totalMinutos${i}${j}" valor="${xminutoTotal}"/>                                           
                                Total de horas solicitadas:<input type="text" name="totalHoras${i}${j}" value="${xhoraTotal}" size="2" readonly/>h <input type="text" name="totalMinutos${i}${j}" value="${xminutoTotal}" size="2" readonly/>min  
                                 </c:if>
                                 
                                 <c:if test="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))<= 60) && (xintervalo =='Sim') }">
                                    <c:set var="condicional" value="nao"/>                                
                                    <p style="color:red"> Para preencher esta opção, clicar em “<u>Intervalo ?</u>” e ler o conteúdo. </p>
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
                                 <p style="color:red">Total de Hora Extra não pode ser maior que 10h00m.</p>
                                 </c:if>
                                
                                <c:if test="${((xhoraTotal > 7)||((xhoraTotal==7)&&(xminutoTotal>0))) && (xintervalo == 'Não')}"> 
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
A prestação remunerada de serviço extraordinário aos sábados, domingos e feriados somente será admitida nos seguintes casos:</br>&nbsp;&nbsp;- Para realização de atividades essenciais que não possam ser exercidas em dias úteis;</br>&nbsp;&nbsp; - Para eventos que ocorram nesses dias, desde que seja impossível adotar escala de revezamento ou realizar a devida compensação;</br>&nbsp;&nbsp;- Quando ocorrerem situações que requeiram reparos inadiáveis e imediato atendimento e sejam decorrentes de fatos supervenientes;</br>&nbsp;&nbsp;- Para colocação em dia de tarefas específicas mediante plano de esforço concentrado aprovado pelo (...) Diretor da Secretaria Geral, nas Seções Judiciárias.
        </c:set>
<c:set var="artigo">
            <b>foram observados os requisitos previstos no  
            <span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=325,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${artigo47}');"><u>
            artigo 47</u></span>,caput, da Resolução nº 4/2008-CJF, com a redação dada pela Resolução nº 173/2011-CJF. </b>
</c:set>
           
                                </br>
                                <mod:caixaverif var="normaRegul" titulo="Esta solicitação observa os limites de horas-extras estabelecidos na norma regulamentar: 2 (duas) horas nos dias úteis, 10 (dez) horas semanais (de domingo a sábado), 44 (quarenta e quatro) horas mensais e 134 (cento e trinta e quatro) horas anuais." reler="nao" marcado="Não" obrigatorio="Sim" />                                
                                </br></br>
                                No caso de a solicitação se referir ao adicional de serviço extraordinário realizado em fins de semana e feriados:   
                                  </br> <input type="radio" name="adicionalServico" id="aplica" value="aplica" checked><label for="artigo47">${artigo} </label>
                                <br>
                                  <input type="radio" name="adicionalServico" id="naoAplica" value="naoAplica">  <label for="naoAplica">não se aplica.</label><br>

                                 </br></br>		      
			<mod:memo colunas="70" linhas="3"
				titulo="As horas-extras acima se mostram necessárias, pois (MANIFESTAÇÃO FUNDAMENTADA)"
				var="motivoHora" /> 
		</mod:grupo>
		<mod:grupo>
			<mod:memo colunas="70" linhas="2" titulo="A(s) mencionada(s) tarefa(s) só pode (em) ser realizada(s) durante esse período, eis que (MANIFESTAÇÃO FUNDAMENTADA </br>quanto à impossibilidade de realização do serviço durante o expediente regulamentar ou em dias úteis, na hipótese de </br>prestação de serviço extraordinário em fins de semana e feriados)"
				var="justificativa" /></br></br>
		</mod:grupo>
		<mod:grupo><p style="color:red">
		Obs.1: Quem assina esta solicitação é o diretor da unidade da prestação do serviço extraordinário.</br></br>
        Obs.2: Na hipótese de convocação, para prestação de serviço extraordinário, de servidor de outra unidade de lotação, também é necessário que o titular da unidade de origem daquele servidor seja incluído como cossignatário na solicitação.
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
							SOLICITAÇÃO N&ordm; ${doc.codigo}</font></td>
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
		autorização para prestação de serviço extraordinário segundo a escala
		abaixo discriminada, de acordo com os artigos 42 e seguintes da 
		Resolução nº 4/2008, do Conselho da Justiça Federal.</p>
		<br />

		<table width="100%" align="center" border="1" cellpadding="2"
			cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="20%" align="center"><p style="font-size:11px">Servidor</p></td>
				<td bgcolor="#FFFFFF" width="07%" align="center"><p style="font-size:11px">Matrícula</p></td>
				<td bgcolor="#FFFFFF" width="5%" align="center"><p style="font-size:11px">Lotação</p></td>
				<td bgcolor="#FFFFFF" width="10%" align="center"><p style="font-size:9px">Data do Serviço Extraordinário</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:11px">Dia da semana</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:11px">Feriado?</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:10px">Adicional ou Banco de Horas</p></td>
				<td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">Horário Inicial</p></td>
				<td bgcolor="#FFFFFF" width="7%"><p style="font-size:11px">Intervalo?</p></td>
				<td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">Horário Final</p></td>
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
						<c:choose>
						<c:when test="${j == 1}">
							<td bgcolor="#FFFFFF" width="10%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('dataServico',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('dataSemana',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('feriado',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('adicionalBanco',i),j)]}</p></td>
							<td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('horaIni',i),j)]}:${requestScope[f:concat(f:concat('minutoIni',i),j)]}:00</p></td>
							<td bgcolor="#FFFFFF" width="7%" align="center"><p style="font-size:11px">${requestScope[f:concat(f:concat('intervalo',i),j)]}</p></td>
						    <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('horaFim',i),j)]}:${requestScope[f:concat(f:concat('minutoFim',i),j)]}:00</p></td>
                            <td bgcolor="#FFFFFF" width="10%"><p style="font-size:11px">${requestScope[f:concat(f:concat('totalHoras',i),j)]}:${requestScope[f:concat(f:concat('totalMinutos',i),j)]}:00</p></td>
						</c:when>
						<c:otherwise>
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
						</c:otherwise>
						</c:choose>
					</c:forEach>

				</tr>
			
		</c:forEach>
	</table>
		<p style="text-align: justify; text-indent: 3cm;">As horas-extras
		acima se mostram necessárias, pois &nbsp;${motivoHora}</p>
		<c:if test="${tipoHoraExtra eq 'em fins de semana/feriados'}">
			<c:set var="tampouco" value="tampouco em dias úteis," scope="request" />
		</c:if>
		<p style="text-align: justify; text-indent: 3cm;">A(s) mencionada(s) tarefa(s) só pode (em) ser realizada(s) durante esse período,
		${tampouco} eis que &nbsp;${justificativa}</p>
        
        <c:if test="${Antecedencia == '2'}">
        <p style="text-align: justify; text-indent: 3cm;">
         Não foi possível observar a antecedência mínima de 5 (cinco) 
        dias prevista no artigo 42, parágrafo 2º, da Resolução nº 4/2008-CJF, 
        haja vista &nbsp; ${antecedenciaNao}</p>
        </c:if>
        
		<p style="text-align: justify; text-indent: 3cm;">Na hipótese de
		deferimento, comprometo-me a encaminhar à Subsecretaria de Gestão de
		Pessoas a ficha individual de frequência de serviço extraordinário
		preenchida, bem como atestada por mim e pela chefia imediata do(s)
		respectivo(s) servidor(es) até o 2º dia útil do mês subsequente ao da
		prestação do serviço, nos termos do artigo 49 da Resolução
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
			{Solicitação Assinada! Iniciando procedimento "Serviço extraordinário".}
			<c:set var="f" value="${f:criarWorkflow('Serviço extraordinário', doc, cadastrante, titular, lotaCadastrante, lotaTitular)}" />
	    </c:if>
	</mod:assinatura>
</mod:modelo>