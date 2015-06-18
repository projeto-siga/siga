<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de SOLICITAÇÃO DE LICENÇA MÉDICA
    Ultima Alteração 08/03/2007 
 -->

<mod:modelo>
    <mod:entrevista>
        <mod:grupo titulo="DETALHES DA LICENÇA">
            <mod:selecao titulo="Atestado em anexo de" var="tipoAtestado"
                opcoes="médico da SJRJ;médico externo" />
        </mod:grupo>
        <mod:grupo>
            <mod:texto titulo="Endereço" largura="55" maxcaracteres="80"
                var="endereco" />
        </mod:grupo>
        <mod:grupo>
            <mod:texto titulo="Tel Residencial" largura="15" maxcaracteres="13"
                var="telResidencial" />
            <mod:texto titulo="Celular" largura="15" maxcaracteres="13"
                var="telCelular" />
            <mod:texto titulo="Ramal" largura="10" maxcaracteres="10"
                var="ramalLotacao" />
        </mod:grupo>
        <mod:selecao titulo="Tipo" var="tipolicenca"
            opcoes="Licença para Tratamento da Própria Saúde;Licença à Gestante;Licença por Motivo de Doença de Pessoa da Família;"
            reler="sim" />
        <c:if test="${tipolicenca eq 'Licença à Gestante'}">
            <mod:grupo>
                <mod:caixaverif titulo="Prorrogação da Licença à Gestante" var="plg"
                    reler="ajax" idAjax="plgAjax" />
            </mod:grupo>
        </c:if>
        <mod:grupo depende="plgAjax">
            <c:if test="${plg == 'Sim'}">
                <mod:grupo>
                    <mod:mensagem
                        texto="(Declaro estar ciente de que, caso haja falecimento da criança, cessará, imediatamente, o meu direito à prorrogação da Licença à Gestante, conforme disposto no art.7º da Res-CJF n.30/2008)"></mod:mensagem>
                </mod:grupo>
            </c:if>
        </mod:grupo>
        <c:if
            test="${tipolicenca eq 'Licença por Motivo de Doença de Pessoa da Família'}">
            <mod:grupo>
                <mod:texto titulo="Nome do Familiar" largura="30" maxcaracteres="30"
                    var="nomeFamiliar" />
                <mod:texto titulo="Grau de Parentesco" largura="15"
                    maxcaracteres="15" var="grauParentesco" />
            </mod:grupo>
        </c:if>
        <mod:grupo titulo="Período Solicitado">
            <mod:data titulo="Data Inicial" var="dataInicio" />
            <mod:texto titulo="Quant. de dias" var="quantDias" largura="3"
                maxcaracteres="3" obrigatorio="Sim" />
        </mod:grupo>
    </mod:entrevista>

    <mod:documento>
        <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
        <head>
        <style type="text/css">
@page {
    margin-left: 2cm;
    margin-right: 2cm;
    margin-bottom: 1.75cm;
}
</style>
        </head>
        <body>
    <!-- INICIO PRIMEIRO CABECALHO
        <table width="100%" border="0" bgcolor="#FFFFFF">
            <tr>
                <td>
                    <c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
                </td>
            </tr>
            <tr>
                <td></td>
            </tr>
        </table>
    
        FIM PRIMEIRO CABECALHO -->

    <p style="font-size: 15pt;"><b>SOLICITAÇÃO DE LICENÇA MÉDICA</b></p>
        Atestado em anexo:&nbsp;&nbsp;&nbsp;&nbsp;
        <c:choose>
            <c:when test="${tipoAtestado eq 'médico da SJRJ'}">
                [x] de médico da SJRJ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;] de médico externo
            </c:when>
            <c:when test="${tipoAtestado eq 'médico externo'}">
                [&nbsp;&nbsp;] de médico da SJRJ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[x] de médico externo
            </c:when>
            <c:otherwise>
                [&nbsp;&nbsp;] de médico da SJRJ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;] de médico externo
            </c:otherwise>
        </c:choose>
        <table width="100%" border="1" cellpadding="3" cellspacing="1">
            <tr>
                <td bgcolor="#C0C0C0" colspan="2"><b>SERVIDOR</b></td>
            </tr>
        </table>
        <table width="100%" border="1" cellpadding="3" cellspacing="1">
            <tr>
                <td width="70%" bgcolor="#FFFFFF">Nome: <b>${doc.subscritor.descricao}</b></td>
                <td width="30%" bgcolor="#FFFFFF">Matrícula: <b>${doc.subscritor.sigla}</b></td>
            </tr>
        </table>
        <table width="100%" border="1" cellpadding="3">
            <tr>
                <td width="30%">Lotação: <b>${doc.subscritor.lotacao.descricao}</b></td>
                <td width="70%">Cargo: <b>${doc.subscritor.cargo.nomeCargo}</b></td>
            </tr>
        </table>
        <table width="100%" border="1" cellpadding="3">
            <tr>
                <td>Endereço: <b>${endereco}</b></td>
            </tr>
        </table>
        <table width="100%" border="1" cellpadding="3">
            <tr>
                <td>Tel Residencial: <b>${telResidencial}</b></td>
                <td>Tel Celular: <b>${telCelular}</b></td>
                <td>Ramal da Lotação: <b>${ramalLotacao}</b></td>
            </tr>
        </table>
        <table width="100%" border="1" cellpadding="3">
            <tr>
                <td width="60%">Tipo de Licença:<br />
                <c:choose>
                    <c:when
                        test="${tipolicenca eq 'Licença para Tratamento da Própria Saúde'}">
                    <p style="font-size: 9pt;">LTS - Licença para Tratamento da Própria Saúde<br /> </p>
                    </c:when>
                    <c:when
                        test="${tipolicenca eq 'Licença à Gestante' && plg eq 'Nao'}">
                   <p style="font-size: 9pt;"> LG - Licença à Gestante<br /></p>
                    </c:when>
                    <c:when
                        test="${tipolicenca eq 'Licença à Gestante' && plg eq 'Sim'}">
                   <p style="font-size: 9pt;"> LG - Licença à Gestante<br />
                    PLG - Prorrogação da Licença à Gestante (Declaro estar ciente de que, 
                    caso haja falecimento da criança, cessará, imediatamente, o meu direito à prorrogação 
                    da Licença à Gestante, conforme disposto no art.7º da Res-CJF n.30/2008)</p>
                    </c:when>
                    <c:when
                        test="${tipolicenca eq 'Licença por Motivo de Doença de Pessoa da Família'}">
                   <p style="font-size: 9pt;"> LTPF - Licença por Motivo de Doença de Pessoa da Família<br /></p>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose></td>
                <td width="40%" align="center">Período Solicitado:<br /><br />
                De: <b>${dataInicio}</b> até <c:if test="${not empty quantDias}">
                    <b>${f:calculaData(requestScope['quantDias'],requestScope['dataInicio'])}</b>
                </c:if></td>
            </tr>
        </table>
        <c:if
            test="${tipolicenca eq 'Licença por Motivo de Doença de Pessoa da Família'}">
            <table width="100%" border="1" cellpadding="3">
                <tr>
                    <td width="60%">Nome do Familiar: <b>${nomeFamiliar}</b></td>
                    <td width="40%">Grau de Parentesco: <b>${grauParentesco}</b></td>
                </tr>
            </table>
        </c:if>
        <table width="100%" border="1">
            <tr>
                <td width="50%" valign="top">Local e Data:<br />
                ${doc.dtExtenso}<br />
                <br />
                </td>
                <td width="50%" valign="top">Assinatura do(a) Servidor(a)<br />
                <br />
                </td>
            </tr>
        </table>
        <table width="100%" border="1" width="100%">
            <tr>
                <td bgcolor="#C0C0C0"><b> OBSERVAÇÕES </b></td>
            </tr>
            <tr>
                <td>
                <p align="justify" style="font-size: 8pt;">- Deverá ser anexado
                ao presente formulário atestado médico dentro de envelope lacrado,
                com identificação de "confidencial", e encaminhado à SESAU/SRH ou às
                SEAPOs no prazo de 3 (três) dias úteis apartir do 1º dia do
                afastamento.<br />
                </p>
                <p align="justify" style="font-size: 8pt;">- Somente serão
                aceitos formulários com assinatura do servidor e de seu superior
                hierárquico, com carimbo de identificação.<br />
                </p>
                <p align="justify" style="font-size: 8pt;"><b> - O atestado
                firmado por médico externo</b>, de forma legível, sem rasuras e em
                receituário adequado, deverá conter: nome completo do servidor;
                diagnóstico definitivo ou provável, codificado (CID-10) ou por
                extenso; período de afastamento recomendado e nome completo do
                médico; assinatura, carimbo e nº de registro do CRM.<br />
                </p>
                <p align="justify" style="font-size: 8pt;">Caso sejam concedidos
                ao servidor <b>licenças ou afastamentos durante o periodo de
                férias</b>, estas serão suspensas e o período remanescente será
                remarcado pela SRH, iniciando-se no 1º dia imediatamente posterior
                ao término da licença ou afastamento.<br />
                </p>
                </td>
            </tr>
            <tr>
                <td bgcolor="#C0C0C0"><b> CHEFIA </b></td>
            </tr>
        </table>
        <table width="100%" border="1" width="100%">
            <tr>
                <td valign="top">
                <p align="center" style="font-size: 8pt;"><b>Observações da
                chefia para a perícia médica</b><br />
                (preenchimento obrigatório, mesmo que seja com "nada a declarar").<br />
                OBS:<br />
                </p>
                <p align="center" style="font-size: 8pt;">- Em caso de
                solicitação de Licença por Motivo de Doença em Pessoa da Família -
                LTPF, há possibilidade do servidor compensar os dias não
                trabalhados?(conforme §2º do art. 2º da Resolução nº447/2005 - CJF)</p>
                <p style="font-size: 10pt;"> [&nbsp;&nbsp;]sim [&nbsp;&nbsp;]não</p><br />
                </td>
                <td valign="top">
                <p align="center" style="font-size: 8pt;">Último dia de trabalho
                do servidor: <br />
                <br />
                _______/_______/_______<br />
                Carimbo e assinatura da chefia<br />
                <br />
                </p>
                </td>
            </tr>
        </table>
        <c:import
            url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
        </body>
        </html>
    </mod:documento>
</mod:modelo>
