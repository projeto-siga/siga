<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%> 
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf-8" buffer="64kb"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script language="javascript">
var newwin = null;
</script>

<mod:modelo>
    <mod:entrevista>
<c:set var="intervaloMsg">
&nbsp;&nbsp;&nbsp;O servidor submetido à jornada ininterrupta poderá prestar serviço extraordinário desde que, no dia da prestação do serviço, cumpra jornada de oito horas de trabalho com intervalo de, no mínimo, uma hora (§1º do Art. 45, Resolução nº 4/2008 - CJF, alterado pela Resolução nº 173/2011 - CJF).</br>&nbsp;&nbsp;&nbsp; Na hipótese de prestação de serviço extraordinário em fins de semana/feriados, somente a sobrejornada igual ou superior a 8 (oito) horas poderá conter intervalo para almoço, a teor do artigo 1º, caput, da Resolução nº 88/2009, do Conselho Nacional de Justiça.        </c:set>
    
    <mod:grupo titulo="Referente">
        <mod:radio titulo="ao Plantão Judiciário." var="referente" valor="1"marcado="Sim"/>
        <mod:radio titulo="à Solicitação de Serviço Extraordinário" var="referente" valor="2" />
        <c:set var="valorreferente" value="${referente}" />
        <c:if test="${empty valorreferente}">
            <c:set var="valorreferente" value="${param['referente']}" />
        </c:if>
         <c:if test="${(valorreferente == '2')}">        
         <mod:texto titulo="Nº" var="numerosolicitacao"  largura="7" obrigatorio="Sim"/>         
         </c:if>
       
   </mod:grupo>
   </br>
        <mod:grupo>
            <mod:pessoa titulo="Matrícula" var="servidor" obrigatorio="Sim" reler="sim" /> 
        </mod:grupo>
        <mod:grupo> 
            <mod:lotacao titulo="Unidade Organizacional da prestação do serviço extraordinário/do Plantão Judiciário" var="unidade" />
        </mod:grupo>    
        <mod:grupo>
            <mod:texto titulo="Ano" var="ano" maxcaracteres="4" largura="4"/> &nbsp;
            <mod:selecao titulo="Mês" var="mes" 
            opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro"/> &nbsp;
            </br>
            <mod:selecao titulo="Número de dias incluídos"  var="numDiasHoraExtra"
              opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31"
              reler="ajax" idAjax="diasAjax" />
        </mod:grupo>            
        <mod:grupo depende="diasAjax">  

            <c:forEach var="i" begin="1" end="${numDiasHoraExtra}">
                <b>${i}º dia:</b>
                <mod:grupo depende="Func${i}">
                    <mod:data titulo="Data do Serviço Extraordinário" var="dataServico${i}"></mod:data> &nbsp;&nbsp;&nbsp;&nbsp;                        
                    <mod:selecao titulo="Dia da semana" var="diaSemana${i}"
                        opcoes="Domingo;Segunda-feira;Terça-feira;Quarta-feira;Quinta-feira;Sexta-feira;Sábado" reler="ajax" idAjax="Func${i}"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                    <mod:selecao titulo="Feriado" var="feriado${i}"
                        opcoes="Não;Sim" reler="ajax" idAjax="Func${i}"/></br>
                   <mod:texto largura="2" maxcaracteres="2" titulo="Horário inicial" var="horaIni${i}" reler="ajax" idAjax="Func${i}" />h 
                   <mod:texto titulo="" largura="2" maxcaracteres="2" var="minutoIni${i}" reler="ajax" idAjax="Func${i}" />m &nbsp;&nbsp;&nbsp;&nbsp;
                   <mod:texto largura="2" maxcaracteres="2" titulo="Horário final" var="horaFim${i}" reler="ajax" idAjax="Func${i}" />h
                   <mod:texto titulo="" largura="2" maxcaracteres="2" var="minutoFim${i}" reler="ajax" idAjax="Func${i}" />m&nbsp;&nbsp;&nbsp;&nbsp;
                    </br>
                    
                    <span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=255,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${intervaloMsg}');"><u>
                                    Intervalo?</u></span>
                    <mod:selecao titulo="" var="intervalo${i}" opcoes="Não;Sim" reler="ajax" idAjax="Func${i}" />&nbsp;&nbsp;&nbsp;&nbsp;
                    <c:set var="xintervalo" value="${requestScope[f:concat('intervalo',i)]}" />
                    <fmt:formatNumber var="xhoraIni" value="${requestScope[f:concat('horaIni',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                    <fmt:formatNumber var="xhoraFim" value="${requestScope[f:concat('horaFim',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                    <fmt:formatNumber var="xminutoIni" value="${requestScope[f:concat('minutoIni',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                    <fmt:formatNumber var="xminutoFim" value="${requestScope[f:concat('minutoFim',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                    <c:set var="xdiaSemana" value="${requestScope[f:concat('diaSemana',i)]}" />
                    <c:set var="xferiado" value="${requestScope[f:concat('feriado',i)]}" /> 
                    
                                <c:if test="${(xhoraIni > 24)}"> 
                                <p style="color:red">Hora de início deve ser menor ou igual a 24</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                                <c:if test="${(xintervalo == 'Não')}"> 
                                <input type="hidden" name="horaIniIntervaloNao${i}" value="00"/>
                                <input type="hidden" name="horaFimIntervaloNao${i}" value="00"/>
                                <input type="hidden" name="minutoIniIntervaloNao${i}" value="00"/>
                                <input type="hidden" name="minutoFimIntervaloNao${i}" value="00"/>
                              
                                </c:if>                                
                               
                                
                                <c:if test="${(xintervalo == 'Sim')}"> 
                                   <mod:texto largura="2" maxcaracteres="2" titulo="Início do Intervalo" var="horaIniIntervalo${i}" reler="ajax" idAjax="Func${i}" />h 
                                   <mod:texto titulo="" largura="2" maxcaracteres="2" var="minutoIniIntervalo${i}" reler="ajax" idAjax="Func${i}" />m &nbsp;&nbsp;&nbsp;&nbsp;
                                   <mod:texto largura="2" maxcaracteres="2" titulo="Fim do Intervalo" var="horaFimIntervalo${i}" reler="ajax" idAjax="Func${i}" />h
                                   <mod:texto titulo="" largura="2" maxcaracteres="2" var="minutoFimIntervalo${i}" reler="ajax" idAjax="Func${i}" />m&nbsp;&nbsp;&nbsp;&nbsp;
                                       </br>
                                        <fmt:formatNumber var="xhoraIniIntervalo" value="${requestScope[f:concat('horaIniIntervalo',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                        <fmt:formatNumber var="xhoraFimIntervalo" value="${requestScope[f:concat('horaFimIntervalo',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                        <fmt:formatNumber var="xminutoIniIntervalo" value="${requestScope[f:concat('minutoIniIntervalo',i)]}" maxFractionDigits="0" minIntegerDigits="2" />
                                        <fmt:formatNumber var="xminutoFimIntervalo" value="${requestScope[f:concat('minutoFimIntervalo',i)]}"maxFractionDigits="0" minIntegerDigits="2" />
                                        <fmt:formatNumber var="xhoraTotalIntervalo" value="${(((xhoraFimIntervalo * 60 + xminutoFimIntervalo)
                                        -(xhoraIniIntervalo * 60 + xminutoIniIntervalo))/60)}" maxFractionDigits="0" minIntegerDigits="2" />
                                        <fmt:formatNumber var="xminutoTotalIntevalo" value="${(((xhoraFimIntervalo * 60 + xminutoFimIntervalo)
                                        -(xhoraIniIntervalo * 60 + xminutoIniIntervalo))%60)}" maxFractionDigits="0" minIntegerDigits="2" />
                              
                                <p style="color:red">Para preencher esta opção, clicar em <u>“Intervalo?”</u> e ler o conteúdo.</p>
                                <c:if test="${(xhoraFimIntervalo > 24)}"> 
                                <p style="color:red">Hora de fim do intervalo deve ser menor ou igual a 24.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                                <c:if test="${(xminutoIniIntervalo > 59)}"> 
                                <p style="color:red">Minuto de início do intervalo deve ser menor ou igual a 59</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                                <c:if test="${(xminutoFimIntervalo > 59)}"> 
                                <p style="color:red">Minuto de fim do intervalo deve ser menor ou igual a 59.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>

                                <c:if test="${(xhoraIniIntervalo > xhoraFimIntervalo)}">
                                <p style="color:red">Início do intervalo maior que fim do intervalo.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>
                                <c:if test="${(xhoraIniIntervalo == xhoraFimIntervalo)&&(xminutoIniIntervalo > xminutoFimIntervalo) }">
                                <p style="color:red">Início do intervalo maior que fim do intervalo.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>                   
                                
                                <c:if test="${((xhoraTotalIntervalo < 1)||((xhoraTotalIntervalo == 1)&& (xminutoIniIntervalo > xminutoFimIntervalo ))) }"> 
                                <p style="color:red">O intervalo deve ser no mínimo de 1 hora.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>  
                                </c:if>
                                
                                <c:if test="${(xhoraFim > 24)}"> 
                                <p style="color:red">Hora de fim deve ser menor ou igual a 24.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                                <c:if test="${(xminutoIni > 59)}"> 
                                <p style="color:red">Minuto de início deve ser menor ou igual a 59</p>
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
                                <p style="color:red">Horário inicial  maior que Horário final.</p>
                                <c:set var="condicional" value="nao"/>
                                </c:if>
                                
                                <c:if test="${(not empty xhoraIni)&& (not empty xhoraFim) && (not empty xminutoFim) && (not empty xminutoIni)&& (condicional != 'nao')&& (xintervalo =='Não')}">
                                     <fmt:formatNumber var="xhoraTotalC" value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))/60)}" type="number" pattern="##.##" />
                                     <fmt:parseNumber var="xhoraTotal" value="${xhoraTotalC}" type="number" integerOnly="true"/>
                                     <fmt:formatNumber var="xminutoTotal" value="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))%60)}" maxFractionDigits="0" minIntegerDigits="2" />
                                     Total de horas:<input type="text" name="totalHoras${i}" value="${xhoraTotal}" size="2" readonly/>h <input type="text" name="totalMinutos${i}" value="${xminutoTotal}" size="2" readonly/>min
                                     <mod:oculto var="totalHorasf${i}" valor="${xhoraTotal}" />
                                     <mod:oculto var="totalMinutosf${i}" valor="${xminutoTotal}" />    
                                  </c:if>
                                 
                                 <c:if test="${(((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))<= 60) && (xintervalo =='Sim') }">
                                    <c:set var="condicional" value="nao"/>                                
                                    <p style="color:red"> Com intervalo, o total de Horas Extras deve ser superior a 60 minutos. </p>
                                 </c:if>  
                                 
                                <c:if test="${(not empty xhoraIni)&& (not empty xhoraFim) && (not empty xminutoFim) && (not empty xminutoIni)&& (condicional != 'nao') && (xintervalo =='Sim')}">
                                    <fmt:formatNumber var="xhoraTotalC" value="${((((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))-((xhoraFimIntervalo * 60 + xminutoFimIntervalo)-(xhoraIniIntervalo * 60 + xminutoIniIntervalo)))/60)}" type="number" pattern="##.##" />
                                    <fmt:parseNumber var="xhoraTotal" value="${xhoraTotalC}" type="number" integerOnly="true"/>
                                    <fmt:formatNumber var="xminutoTotal" value="${((((xhoraFim * 60 + xminutoFim)-(xhoraIni * 60 + xminutoIni))-((xhoraFimIntervalo * 60 + xminutoFimIntervalo)-(xhoraIniIntervalo * 60 + xminutoIniIntervalo)))%60)}" maxFractionDigits="0" minIntegerDigits="2" />
                                     Total de horas:<input type="text" name="totalHoras${i}" value="${xhoraTotal}" size="2" readonly/>h <input type="text" name="totalMinutos${i}" value="${xminutoTotal}" size="2" readonly/>min
                                     <mod:oculto var="totalHorasf${i}" valor="${xhoraTotal}" />
                                     <mod:oculto var="totalMinutosf${i}" valor="${xminutoTotal}" />    
                                </c:if>
                          
                 </mod:grupo>
                    
            </c:forEach>
            
        </mod:grupo>
        <br/>
        <mod:grupo>
             <mod:radio titulo="Afirmo que todas as tarefas/atividades descritas na Solicitação de Serviço Extraordinário foram cumpridas." var="solicitacaoCumprida" valor="1" marcado="Sim"/>
              <mod:radio titulo="Afirmo que as tarefas/atividades descritas na Solicitação de Serviço Extraordinário não foram todas cumpridas pois:" var="solicitacaoCumprida" valor="2" />
                            <mod:memo titulo="" var="solicitacaoCumpridaNaoPois" colunas="60" linhas="7" /><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Além da justificativa, incluir a tarefa alterada.)</b></br>
                 <mod:radio titulo="Não se aplica." var="solicitacaoCumprida" valor="3" /></br>
                </br>
        </mod:grupo>
        <mod:grupo >
                                <mod:caixaverif var="prestacao" titulo="A prestação do Serviço Extraordinário observou os limites de horas-extras estabelecidos na norma regulamentar: 2 (duas) horas nos dias úteis, 10 (dez) horas semanais (de domingo a sábado), 44 (quarenta e quatro) horas mensais e 134 (cento e trinta e quatro) horas anuais." reler="nao" marcado="Não" obrigatorio="Sim" />                                
                    
                
        </mod:grupo>
        <br>
        <b><mod:grupo>
            Obs.1: O próprio servidor deverá preencher o formulário.</br> </br> 

            Obs.2: Antes da finalização do documento, deverão ser cadastrados como cossignatários:</br> 
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a) o superior hierárquico e o titular da unidade de prestação do serviço extraordinário, exatamente nessa ordem;</br> 
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b) o diretor de secretaria, no caso de Plantão Judiciário.
            
        </mod:grupo></b>    
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
                            <td align="center"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
                            FICHA INDIVIDUAL DE FREQUENCIA DE SERVIÇO EXTRAORDINÁRIO</font></td>
                        </tr>   
                        <tr>
                            <td align="center"><font style="font-family:Arial;font-size:11pt;">
                            (Art.49 da Resolução n&ordm; 4/2008 - CJF)</font></td>
                        </tr>                                           
                    </table>
                </td>
            </tr>
        </table>
        FIM PRIMEIRO CABECALHO -->

        <!-- INICIO CABECALHO
        <c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
        FIM CABECALHO -->

        
        &nbsp;<br/> <%-- Solução by Edson --%>
        <b>Referente:</b>

        <c:if test="${(referente == '1')}">
        ao Plantão Judiciário.</c:if></br>
        <c:if test="${(referente == '2')}">
        à Solicitação de Serviço Extraordinário Nº:${numerosolicitacao}</c:if></br>
        
        <b>Unidade Organizacional:</b> ${requestScope['unidade_lotacaoSel.sigla']}-${requestScope['unidade_lotacaoSel.descricao']}<br/>
        <b>Mês/Ano:</b>  ${mes}/${ano} <br/>
       
         
        <table width="100%" align="center" border="1" cellpadding="2"
            cellspacing="1" bgcolor="#000000">
            <tr>
                <td bgcolor="#BAB9AF" width="45%" align="center">Nome</td>
                <td bgcolor="#BAB9AF" width="15%" align="center">Matrícula</td>
                <td bgcolor="#BAB9AF" width="40%" align="center">Lotação</td>
            </tr>
            <tr>
                <td bgcolor="#FFFFFF" width="45%" align="center">${requestScope['servidor_pessoaSel.descricao']}</td>
                <td bgcolor="#FFFFFF" width="15%" align="center">${requestScope['servidor_pessoaSel.sigla']}</td>
                <td bgcolor="#FFFFFF" width="40%" align="center">${f:pessoa(requestScope['servidor_pessoaSel.id']).lotacao.descricao}</td>
            </tr>         
       </table>
       
        </br>
        <table width="100%" align="center" border="1" cellpadding="2"
            cellspacing="1" bgcolor="#000000">
            <tr>
                <td bgcolor="#BAB9AF" width="05%" align="center">Data</td>
                <td bgcolor="#BAB9AF" width="20%" align="center">Dia da Semana</td>
                <td bgcolor="#BAB9AF" width="5%" align="center">Feriado?</td>
                <td bgcolor="#BAB9AF" width="10%" align="center">Início</td>
                <td bgcolor="#BAB9AF" width="10%" align="center">Término</td>
                <td bgcolor="#BAB9AF" width="10%" align="center">Início</td>
                <td bgcolor="#BAB9AF" width="10%" align="center">Término</td>
                <td bgcolor="#BAB9AF" width="10%" align="center">Total de horas</td>                
             </tr>
            
            <c:set var="horaDiaUtil" value="0"/> 
            <c:set var="minutoDiaUtil" value="0"/> 
            <c:set var="horaDomingo" value="0"/> 
            <c:set var="minutoDomingo" value="0"/> 
        
        <c:forEach var="i" begin="1" end="${numDiasHoraExtra}">         
            
            <tr>
                
                <td bgcolor="#FFFFFF" width="05%" align="center">${requestScope[f:concat('dataServico',i)]}</td>
                <td bgcolor="#FFFFFF" width="20%" align="center">${requestScope[f:concat('diaSemana',i)]}</td>
                <td bgcolor="#FFFFFF" width="5%" align="center">${requestScope[f:concat('feriado',i)]}</td>  
                <td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('horaIni',i)]}:${requestScope[f:concat('minutoIni',i)]}:00</td>                
                <td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('horaIniIntervalo',i)]}${requestScope[f:concat('horaIniIntervaloNao',i)]}:${requestScope[f:concat('minutoIniIntervalo',i)]}${requestScope[f:concat('minutoIniIntervaloNao',i)]}:00</td>
                <td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('horaFimIntervalo',i)]}${requestScope[f:concat('horaFimIntervaloNao',i)]}:${requestScope[f:concat('minutoFimIntervalo',i)]}${requestScope[f:concat('minutoFimIntervaloNao',i)]}:00</td>               
                <td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('horaFim',i)]}:${requestScope[f:concat('minutoFim',i)]}:00</td>
                <td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('totalHorasf',i)]}:${requestScope[f:concat('totalMinutosf',i)]}:00   </td>                
                    
            </tr>

                        <c:set var="xhoraTotal2" value="${requestScope[f:concat('totalHorasf',i)]}" />
                        <c:set var="xminutoTotal2" value="${requestScope[f:concat('totalMinutosf',i)]}" />
                        <c:set var="xdiaSemana" value="${requestScope[f:concat('diaSemana',i)]}" />
                        <c:set var="xferiado" value="${requestScope[f:concat('feriado',i)]}" /> 
                                       
             <c:if test="${(xdiaSemana == 'Domingo') or (xferiado != 'Não')}">  

                   <c:set var="horaDomingo" value="${horaDomingo + xhoraTotal2}" /> 
                   <c:set var="minutoDomingo" value="${minutoDomingo + xminutoTotal2}"/> 
             </c:if>

             <c:if test="${(xdiaSemana != 'Domingo') and (xferiado == 'Não')}">  
                                       
                   <c:set var="horaDiaUtil" value="${horaDiaUtil + xhoraTotal2}" /> 
                   <c:set var="minutoDiaUtil" value="${minutoDiaUtil + xminutoTotal2}"/> 

              </c:if>

             </c:forEach>
                   <c:set var="horaDomingo2" value="${(((horaDomingo*60)+minutoDomingo)/60)}"/>
                   <c:set var="minutoDomingo2" value="${((horaDomingo*60)+minutoDomingo)%60}"/>
                   
                   <fmt:formatNumber var="horaDomingoFC" value="${(horaDomingo2)}" type="number" pattern="##.##"  /> 
                   <fmt:parseNumber var="horaDomingoF" value="${horaDomingoFC}" type="number" integerOnly="true"/>
                   <fmt:formatNumber var="minutoDomingoF" value="${minutoDomingo2}" maxFractionDigits="0" minIntegerDigits="2" maxIntegerDigits="2"/> 
                   
                   
                   <c:set var="horaDiaUtilF2" value="${(((horaDiaUtil*60)+minutoDiaUtil)/60)}"/>
                   <c:set var="minutoDiaUtilF2" value="${((horaDiaUtil*60)+minutoDiaUtil)%60}"/>
                   
                   <fmt:formatNumber var="horaDiaUtilFC" value="${horaDiaUtilF2}" type="number" pattern="##.##" /> 
                   <fmt:parseNumber var="horaDiaUtilF" value="${horaDiaUtilFC}" type="number" integerOnly="true"/>
                   <fmt:formatNumber var="minutoDiaUtilF" value="${minutoDiaUtilF2}" maxFractionDigits="0" minIntegerDigits="2" maxIntegerDigits="2"/> 
                                  
                   <fmt:formatNumber var="horaExtraC" value="${((((horaDiaUtilF*60)+minutoDiaUtilF)+((horaDomingoF*60)+minutoDomingoF))/60)}" type="number" pattern="##.##" />
                   <fmt:parseNumber var="horaExtra" value="${horaExtraC}" type="number" integerOnly="true"/> 
                   <fmt:formatNumber var="minutoExtra" value="${((((horaDiaUtilF*60)+minutoDiaUtilF)+((horaDomingoF*60)+minutoDomingoF))%60)}" maxFractionDigits="0" minIntegerDigits="2" maxIntegerDigits="2"/> 
        </table>
        
        <br/><br/>
        <table width="100%" align="center" border="1" cellpadding="2"
            cellspacing="1" bgcolor="#000000">
            <tr>
                <td bgcolor="#FFFFFF" width="40%" align="left"><b>Total de Horas Extraordinárias em Dias Úteis e Sábados:</b> &nbsp; 
               <c:if test="${(empty minutoDiaUtilF) and (empty horaDiaUtilF)}">
                00:00:00
                </c:if>
                <c:if test="${(not empty minutoDiaUtilF) and (not empty horaDiaUtilF) }">
                ${horaDiaUtilF}:${minutoDiaUtilF}:00
              </c:if>
                </td>            
            </tr>
            <tr>
                <td bgcolor="#FFFFFF" width="30%" align="left"><b>Total de Horas Extraordinárias em Domingos e Feriados:</b> &nbsp;
                <c:if test="${(empty horaDomingoF) and (empty minutoDomingoF)}">
                00:00:00
                </c:if>
                <c:if test="${(not empty horaDomingoF) and (not empty minutoDomingoF)}">
                 ${horaDomingoF}:${minutoDomingoF}:00
              </c:if>
                </td>      
            </tr>
            <tr>
                <td bgcolor="#FFFFFF" width="30%" align="left"><b>Total de Horas Extraordinárias:</b> &nbsp; ${horaExtra}:${minutoExtra}:00</td>        
            </tr>           
        </table>        
        <br/>
        <c:if test="${(solicitacaoCumprida == '1')}">
        <p style="text-align: justify; text-indent: 2cm;">
         Informo que todas as tarefas/atividades descritas na Solicitação de Serviço Extraordinário foram cumpridas.</p>
        </c:if>
        </br>
        <c:if test="${(solicitacaoCumprida == '2')}">
        <p style="text-align: justify; text-indent: 2cm;">
         Informo que as tarefas/atividades descritas na Solicitação de Serviço Extraordinário não foram todas cumpridas pois:&nbsp; ${solicitacaoCumpridaNaoPois}</p>
        </c:if></br>
        <p style="text-align: center;">${doc.dtExtenso}</p> 
              
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
</mod:modelo>
