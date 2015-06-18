<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
Recebimento da Segunda via de crachá de indentificação funcional 
Ultima atualização 14/03/2007
-->

<mod:modelo>

	<mod:entrevista>
		
		<mod:grupo titulo="DADOS CADASTRAIS PARA CONFECÇÃO DA CARTEIRA FUNCIONAL" >
		<mod:grupo>
			<mod:texto titulo="Estado Civil" var="estadoCivil" largura="20" />
			<mod:texto titulo="Tipo Sanguínio/Fator RH" var="sangFatorRh" largura="5" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Ramal" var="ramal" largura="3" />
			<mod:texto titulo="Telefone" var="telefone" largura="12" maxcaracteres="11" />
			<mod:texto titulo="Naturalidade" var="naturalidade" largura="30" />	
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="Data Ingresso" var="dataIngresso" />
		</mod:grupo>
		<mod:grupo>
			<mod:numero titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identidade" />
			<mod:texto titulo="Orgão Emissor" var="orgaoEmissor" largura="8" />
			<mod:data titulo="Data Expedição" var="dataExpedicao" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Nome do Pai" var="nomePai" largura="60" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Nome da Mãe" var="nomeMae" largura="60" />
		</mod:grupo>
			<mod:memo colunas="70" linhas="3" titulo="Motivo" var="motivo" />	
		</mod:grupo> 
	
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
    	<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp"/>						
    </head>
	<body>		
		<c:import url="/paginas/expediente/modelos/inc_tit_juizfedDirForo.jsp" />
			
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência a expedição da 2&ordm; via carteira funcional pelo 
		seguinte motivo: ${motivo }
		</p>	
		
		<br>
		<p style="TEXT-INDENT: 1.5cm" align="center">
		<u><b> DADOS CADASTRAIS PARA CONFECÇÃO DA CARTEIRA FUNCIONAL </b></u>
		</p>
		
		<br>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Nome:</td>
				<td bgcolor="#FFFFFF">${doc.subscritor.descricao}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Matrícula:</td>
				<td bgcolor="#FFFFFF">${doc.subscritor.sigla}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Data Ingresso:</td>
				<td bgcolor="#FFFFFF">${dataIngresso}</td>
			</tr>
			<tr>	
				<td bgcolor="#FFFFFF" width="28%">Cargo:</td>
				<td bgcolor="#FFFFFF">${doc.subscritor.cargo.nomeCargo}</td>
			</tr>
			<tr>	
				<td bgcolor="#FFFFFF" width="28%">Lotação:</td>
				<td bgcolor="#FFFFFF">${doc.subscritor.lotacao.descricao}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">CPF:</td>
				<td bgcolor="#FFFFFF">${doc.subscritor.cpfPessoa}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Identidade/órgão:</td>
				<td bgcolor="#FFFFFF">${identidade} ${orgaoEmissor }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Emissão da Identidade:</td>
				<td bgcolor="#FFFFFF">${dataExpedicao }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Nome do Pai:</td>
				<td bgcolor="#FFFFFF">${nomePai }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Nome da Mãe:</td>
				<td bgcolor="#FFFFFF">${nomeMae }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Estado civil:</td>
				<td bgcolor="#FFFFFF">${estadoCivil }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Naturalidade:</td>
				<td bgcolor="#FFFFFF">${naturalidade }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Tipo sang./Fator RH:</td>
				<td bgcolor="#FFFFFF">${sangFatorRh }</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Data de Nascimento:</td>
				<td bgcolor="#FFFFFF">${doc.subscritor.dataNascimento}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="28%">Telefone/Ramal:</td>
				<td bgcolor="#FFFFFF">${telefone } ${ramal }</td>
			</tr>
		</table> 
	
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
			<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		<br>
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
	</body>
	</html>
	</mod:documento>
</mod:modelo>
