<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	
	<mod:entrevista>
		</br>
		<mod:grupo>
			<mod:texto titulo="Ramal" var="ramal" largura="10" />
		</mod:grupo>
		</br>
		<mod:grupo titulo="Relação de Beneficiários">
			<br/>
			<mod:selecao titulo="Total de pessoas"
				var="totalDePessoal" opcoes="1;2;3;4;5;6;7;8;9;10" reler="sim" />
			<br/>
			<br/>
			<c:forEach var="i" begin="1" end="${totalDePessoal}">
				<mod:grupo>
					<mod:texto titulo="Nome" var="nome${i}" largura="60" />
				</mod:grupo>
				<mod:data titulo="Data de Nascimento" var="data_nasc${i}" obrigatorio="Sim"/>
				<mod:grupo>
					<mod:texto titulo="Parentesco" var="parentesco${i}" largura="60" />
				</mod:grupo>
				<br/>
			</c:forEach>				
		</mod:grupo>
	</mod:entrevista>
	
	<mod:documento>
	
	<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >DESIGNA&Ccedil;&Atilde;O PARA PENS&Atilde;O ESTATUT&Aacute;RIA </p></td>
						</tr>
				<tr><td><br/><br/></td>
				</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
	
			<mod:valor var="texto_requerimento"><p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
			${doc.subscritor.padraoReferenciaInvertido},
			matricula nº ${doc.subscritor.matricula}, servidor(a) do Quadro de Pessoal Permanente da Justiça Federal &#8212; 
			Seção Judiciária do Rio de Janeiro, lotado(a) no(a) ${doc.subscritor.lotacao.nomeLotacao}, 
			ramal ${ramal}, designa, para fins de Pensão Estatutária, conforme o art. 217, da Lei nº 8.112/90, 
			os beneficiários abaixo discriminados e anexa os documentos necessários.
			</p>
			<br/>
				
		<table width="100%" height="90px" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" height="20px" align="center"><b>RELAÇÃO DE BENEFICIÁRIOS</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
				<td width="40%" bgcolor="#FFFFFF" align="center"><b>Nome</b></td>
				<td width="20%" bgcolor="#FFFFFF" align="center"><b>Data de Nascimento</b></td>
				<td width="20%" bgcolor="#FFFFFF" align="center"><b>Parentesco</b></td>
			</tr>
			<c:forEach varStatus="indice" var="i" begin="1" end="${totalDePessoal}">
				<tr> 
					<td width="40%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('nome',i)]}</td>
					<td width="40%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('data_nasc',i)]}</td>
					<td width="20%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('parentesco',i)]}</td>
				</tr>
			</c:forEach>
		</table>
		</mod:valor>		
	</mod:documento>
</mod:modelo>
	
