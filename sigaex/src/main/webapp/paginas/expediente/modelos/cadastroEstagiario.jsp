<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>

<mod:modelo>
	<mod:entrevista>
	
		<mod:grupo titulo="DADOS PESSOAIS">
				<mod:texto titulo="Nome" largura="60" var="nomeEstagiario" />
				<mod:selecao titulo="Sexo" var="sexo" opcoes="Masculino;Feminino" />
			<mod:grupo>
				<mod:data titulo="Data de Nascimento" var="dataNascimento" />
				<mod:texto titulo="Naturalidade/UF" largura="40" var="naturalidade" />
			</mod:grupo>
			<mod:grupo>
				<mod:monetario titulo="Nº Identidade" largura="13" maxcaracteres="9" var="docIdentidade" verificaNum="sim" />
				<mod:texto titulo="Orgão Expedidor" largura="10" var="orgExpedidor" />
				<mod:data titulo="Data de Emissão" var="dataEmissao" />
			</mod:grupo>
			<mod:grupo>
				<mod:monetario titulo="Nº CPF" var="docCpf" largura="15" maxcaracteres="11" verificaNum="sim" />
				<mod:selecao titulo="Estado Civil" var="estadoCivil" opcoes="Solteiro(a);Casado(a);Divorciado(a);Viúva(o);Desquitado" reler="sim"/>
				<mod:texto titulo="Tipo Sangüíneo" var="tipoSanguineo" largura="5" />
				<mod:texto titulo="Fator RH" var="fatorRh" largura="5" />
			</mod:grupo>
			<mod:grupo titulo="Filiação">
					<mod:texto titulo="Pai" var="pai" largura="60" />
				<mod:grupo>
					<mod:texto titulo="Mãe" var="mae" largura="60" />
				</mod:grupo>
			</mod:grupo>
			<mod:grupo>
				<c:if test="${estadoCivil == 'Casado(a)'}">
					<mod:texto titulo="Cônjuge" var="conjuge" largura="60" />
				</c:if>
			</mod:grupo>
		</mod:grupo>
		
		<mod:grupo titulo="DADOS RESIDÊNCIAIS">
				<mod:texto titulo="Endereço" var="endereco" largura="60" />
			<mod:grupo>
				<mod:texto titulo="Telefone" var="tel" largura="15" maxcaracteres="11" />
				<mod:texto titulo="Celular" var="cel" largura="15" maxcaracteres="11" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Bairro" var="bairro" largura="30"/>
				<mod:texto titulo="Cidade" var="cidade" largura="30" />
				<mod:texto titulo="CEP" var="cep" largura="15" maxcaracteres="8" />
			</mod:grupo>
		</mod:grupo>
		
		<mod:grupo titulo="DADOS DO CURSO" >
			<mod:grupo>
				<mod:selecao titulo="Período" var="periodo" opcoes="1;2;3;4;5;6;7;8;9;10" reler="não" />
				<mod:texto titulo="Ano" var="ano" largura="5" /> 
				<mod:texto titulo="Matrícula" var="matricula" largura="20" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Instituição de Ensino" var="instEnsino" largura="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Curso" opcoes="Ciências Contábeis;Direito;Nível Médio" var="curso" />
				<mod:selecao titulo="Turno" opcoes="Manhã;Tarde;Noite" var="turno" />
				<mod:selecao titulo="Disponibilidade para Estágio" opcoes="Manhã;Tarde" var="dispEstagio" />
			</mod:grupo>
		</mod:grupo>
		
		<mod:grupo titulo="DADOS DO ESTÁGIO">
		        <mod:lotacao titulo="Unidade de Lotação" var="unidade" obrigatorio="Sim" /> <br>				
				<mod:texto titulo="Telefone1" var="telEstagio" largura="15" maxcaracteres="11" />
				<mod:texto titulo="Telefone2" var="telEstagio2" largura="15" maxcaracteres="11" />
			<mod:grupo>
				<mod:data titulo="Data início do Estágio" var="dataInicio" />
				<mod:data titulo="Previsão de Término" var="previsaoTermino" />				
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Supervisor" var="supervisor" />
			</mod:grupo>
		</mod:grupo>
		
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
		     @page {
				margin-left: 2cm;
				margin-right: 2cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
		 	 }
		</style>
		</head>
		<body>
		<p align="right">
		SIGLA:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MATRÍCULA:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Avisado em_____/_____/____<br>
		</p>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td width="85%"><c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp"/></td>
				<td width="15%" align="center"><b>Fotografia</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
					<tr>
						<td align="center"><b>CADASTRO DE ESTAGIÁRIO - ÁREA JUDICIÁRIA -</b></td>
					</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="100%" colspan="2"><b>1. DADOS PESSOAIS</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="70%">Nome: <b>${nomeEstagiario}</b></td>
				<td bgcolor="#FFFFFF" width="30%">Sexo: <b>${sexo}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="30%">Data de Nascimento: <b>${dataNascimento}</b></td>
				<td bgcolor="#FFFFFF" width="70%">Naturalidade/UF: <b>${naturalidade}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="30%">Nº Identidade: <b>${docIdentidade}</b></td>
				<td bgcolor="#FFFFFF" width="35%">Orgão Expedidor: <b>${orgExpedidor}</b></td>
				<td bgcolor="#FFFFFF" width="35%">Data Emissão: <b>${dataEmissao }</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="30%">Nº CPF: <b>${docCpf}</b></td>
				<td bgcolor="#FFFFFF" width="30%">Estado Civil: <b>${estadoCivil}</b></td>
				<td bgcolor="#FFFFFF" width="23%">Tipo Sangüíneo: <b>${tipoSanguineo}</b></td>
				<td bgcolor="#FFFFFF" width="17%">Fator RH: <b>${fatorRh}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" >Pai: <b>${pai}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" >Mãe: <b>${mae}</b></td>
			</tr>
		</table>
		<c:if test="${estadoCivil == 'Casado(a)'}">
			<table width="100%" border="1" cellpadding="3">
				<tr>
					<td bgcolor="#FFFFFF" >Cônjuge: <b>${conjuge}</b></td>
				</tr>
			</table>
		</c:if>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td width="60%">Endereço: <b>${endereco}</b></td>
				<td width="20%">Telefone: <b>${tel}</b></td>
				<td width="20%">Celular: <b>${cel}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td width="50%">Bairro: <b>${bairro}</b></td>
				<td width="30%">Cidade: <b>${cidade}</b></td>
				<td width="20%">Cep: <b>${cep}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="100%" colspan="3"><b>2. CURSO: ${curso}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
				<tr>
					<td width="30%">Período/Ano: <b>${periodo}&ordm;</b> - <b>${ano}</b></td>
					<td width="30%">Matrícula: <b>${matricula}</b></td>
				</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td width="40%">Instituição de Ensino: <b>${instEnsino}</b></td>
				<td width="20%">Turno: <b>${turno}</b></td>
				<td width="40%">Disponibilidade para Estágio: <b>${dispEstagio }</b>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="100%" colspan="3"><b>3. DADOS DO ESTÁGIO</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
				<tr>
					<td width="50%">Unidade de Lotação: <b>${f:lotacao(requestScope['unidade_lotacaoSel.id']).descricao}</b></td>
					<td width="25%">Telefone1: <b>${telEstagio}</b></td>
					<td width="25%">Telefone2: <b>${telEstagio2}</b></td>
				</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td width="50%">Data de Início do Estágio: <b>${dataInicio}</b></td>
				<td width="50%">Previsão de Término do Estágio: <b>${previsaoTermino}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td width="60%">Supervisor: <b>${requestScope['supervisor_pessoaSel.descricao']}</b></td>
				<c:if test="${not empty requestScope['supervisor_pessoaSel.id']}">
					<c:set var="cargo" value="${f:cargoPessoa(requestScope['supervisor_pessoaSel.id'])}"/>
				</c:if>
				<td width="40%">Cargo/Função: <b>${cargo}</b></td>
			</tr>
		</table>	
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td bgcolor="#FFFFFF" width="100%"><b>4. DOCUMENTOS NECESSÁRIOS</b></td>
			</tr>
				<tr>
						<td>
								<b> - Documento de apresentação à Vara Federal/ao Juizado Especial Federal (Ofício da EMARF ou Encaminhamento da DICRE);</b><br><br>
								<b> - 1 foto 3 X 4.</b>
							
						</td>
				</tr>
		</table>
		<table width="100%" border="1" cellpadding="15">
		<tr>
			<td align="center"> Afirmamos a veracidade das informações acima descritas.<br><br><br><br>
			
									______________________________________<br>
									(Assinatura do Estagiário)<br><br><br><br><br>
									
			Solicito a EMISSÃO do crachá de identificação do(a) estagiári(a).<br><br><br><br>
			
			<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
			
			</td>
		</tr>
		</table>
		<font size="2">SIRH [&nbsp;] SHF [&nbsp;] Atualiza LOT [&nbsp;] CRAC [&nbsp;] CAD em_____/_____/____ </font>
		</body>	
		</html>
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
	</mod:documento>
</mod:modelo>
