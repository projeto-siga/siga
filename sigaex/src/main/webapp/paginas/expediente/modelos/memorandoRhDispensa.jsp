<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao var="qtdServidores"
			titulo="Quantidade de servidores dispensados" reler="ajax"
			idAjax="qtdServidoresAjax" opcoes="1;2;3;4;5;6;7;8;9;10" />
		</mod:grupo>	
		<mod:grupo depende="qtdServidoresAjax">
			<c:forEach var="i" begin="1" end="${qtdServidores}">
				<mod:grupo>
					<mod:pessoa titulo="Servidor" var="servidor${i}" reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<c:set var="funcao"
								value="${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')]).funcaoConfianca.descricao}" />
										
					<ww:if test="${empty funcao}">
						Função: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;S/ FUNÇÃO						
		  		  	</ww:if>
					<ww:else> 
						Função:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${funcao}
					</ww:else>							
					<c:if test="${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')]).cargo.descricao == 'TECNICO JUDICIARIO/SEGURANCA E TRANSPORTE'}">
						<mod:grupo>
							Exercerá as funções de segurança, 						
							<mod:selecao titulo="conforme disposto na Portaria RJ-PGD-2007/46"
							var="seguranca${i}" opcoes="Sim;Não" />
						</mod:grupo>
					</c:if> 
				</mod:grupo>				
				<hr color="#FFFFFF" />
			</c:forEach>
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="A partir de (opcional)" var="dataInicio" />
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Documento feito de ordem de Magistrado?"
				var="autoridade" opcoes="NÃO;SIM" reler="ajax"
				idAjax="autoridadeAjax" />
			<mod:grupo depende="autoridadeAjax">
				<c:if test="${autoridade eq 'SIM'}">
					<mod:pessoa titulo="Matrícula da Autoridade competente"
						var="autoridade" />
					<mod:grupo>
						<mod:radio marcado="Sim" titulo="Titular" var="botao"
							valor="Titular" />
						<mod:radio titulo="Substituto(a)" var="botao"
							valor="Substituto(a)" />
					</mod:grupo>
				</c:if>
			</mod:grupo>
		</mod:grupo>
		<mod:grupo>
			<b> <mod:mensagem titulo="Atenção"
				texto="preencha o destinatário com SELOT e, após finalizar, transfira para a SELOT." />
			</b>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_memorando">
			<p style="TEXT-INDENT: 2cm" align="justify">
			<c:if test="${autoridade eq 'SIM'}">
				De ordem do(a) Exmo(a). Juiz(a) Federal ${requestScope['botao']} do(a) <b>${f:lotacaoPessoa(requestScope['autoridade_pessoaSel.id'])}</b>,
				Dr(a). <b>${requestScope['autoridade_pessoaSel.descricao']}</b>, solicito  	
			</c:if> 
			<c:if test="${autoridade eq 'NÃO'}">
				Solicito 				
			</c:if> a dispensa do(s) servidor(es) abaixo relacionado(s), da(s) função(ões) 
			comissionada(s) discriminada(s), 
			<c:if test="${not empty dataInicio}">
						a partir de <b>${dataInicio}</b>.
			</c:if> 
			<c:if test="${empty dataInicio}">
						a partir da <b>publicação da(s) respectiva(s) Portaria(s)</b>.
			</c:if></p>
			<br/> <br/>			
			<c:forEach var="i" begin="1" end="${qtdServidores}">
				<c:set var="pes"
					value="${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')])}" />

				<table bgcolor="#000000" border="1" cellpadding="5" width="100%"
					align="center">

					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">MATRÍCULA: ${pes.matricula}</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">NOME: <b>${pes.descricao}</b></td>
					</tr>

					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">LOTAÇÃO: ${f:removeAcentoMaiusculas(pes.lotacao.descricao) }</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">FUNÇÃO COMISSIONADA:
						<b>${pes.funcaoConfianca.descricao}</b></td>
					</tr>	
					<c:if test="${not empty requestScope[f:concat('seguranca',i)]}">
						<tr>
							<td align="left"> O servidor <b> 
								<c:if test="${requestScope[f:concat('seguranca',i)] == 'Não'}"> não </c:if> 
				 					exercerá</b> funções de segurança, conforme a Portaria RJ-PGD-2007/46.
				 			</td>
				 		</tr>
					</c:if>					
				</table>
				<br/>
				<c:if test="${i mod 3 == '0' and i < qtdServidores}">
					<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
				</c:if>
				<br />
			</c:forEach>
		</mod:valor>
	</mod:documento>
</mod:modelo>