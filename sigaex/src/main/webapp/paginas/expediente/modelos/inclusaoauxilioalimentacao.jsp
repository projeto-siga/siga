<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
	<mod:grupo titulo="1 - Dados do beneficiário">
			<mod:pessoa var="nome" titulo="Nome"/>
		<mod:grupo>
			<mod:lotacao var="lotacao" titulo="Lotação"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto var="ramal" titulo="Ramal" largura="5"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto var="dtposse" titulo="Data da posse" largura="5"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto var="dtexercicio" titulo="Data do exercício" largura="5"/>
		</mod:grupo>
		<mod:grupo>
			<mod:mensagem titulo="Deseja usufruir do benefício Auxílio Alimentação ? " />
			<mod:selecao titulo="" var="resp1" opcoes="  ;desejo;não desejo" />		
		</mod:grupo>
		<mod:grupo>
			<mod:mensagem titulo="Acumula cargo ou emprego publico ? " />
			<mod:selecao titulo="" var="resp2" opcoes="  ;NÃO ACUMULO CARGO OU EMPREGO PÚBLICO;ACUMULO CARGO OU EMPREGO PÚBLICO" />		
		</mod:grupo>
	</mod:grupo>
	</mod:entrevista>
	
	<mod:documento>		
			<c:set var="tl" value="6pt" />		
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
	<body>
	<mod:letra tamanho="${tl}">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" width="100%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">INCLUSÃO NO PROGRAMA DE APOIO À PSIQUIATRIA E PSICOLOGIA (PAPSI)</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>       
			
			<table width="100%" border="0" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="60%">Nome: ${requestScope['nome_pessoaSel.descricao']}</td> 
					<td bgcolor="#FFFFFF" width="40%">Matrícula: ${requestScope['nome_pessoaSel.sigla']}</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">Cargo: ${f:cargoPessoa(requestScope['nome_pessoaSel.id']) }</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="60%">Lotação: ${requestScope['lotacao_lotacaoSel.descricao']}</td>
					<td bgcolor="#FFFFFF" width="40%">Ramal: ${ramal }</td>
				</tr>
				
				<tr>
					<td bgcolor="#FFFFFF" width="60%">Data da posse: ${dtposse}</td>
					<td bgcolor="#FFFFFF" width="40%">Data do exercício: ${dtexercicio}</td>			
				</tr>
				
			</table>
			<table width="100%" border="1" cellpading="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td>Acúmulo de cargo ou emprego público: ${resp2}</td>
				</tr>
			</table>
			<br>
	 		<table width="100%" border="1" bgcolor="#FFFFFF">
			  <tr>	
				  <td>  
				      <p style="margin-left: 5%"> Nos termos da resolução nº 323, 10/07/2003, publicada no DOU, que regulamentou a <br> concessão do Auxílio-alimentação, no âmbito do Conselho e da Justiça Federal de primeiro e segundo <br>graus, DECLARO que <b><u>${resp1} </u></b> usufruir do benefício Auxílio-alimentação.</p>
					  <br>                                                
				      <p style="margin-left: 5%"> Declaro ter ciência que no caso de acumulação lícita de cargo ou emprego público  farei jus à <br> percepção de apenas um Auxílio-alimentação, mediante opção e que de acordo com o Art. 2º, ítem I, da referida <br> Resolução, os servidores CEDIDOS ou REQUISITADOS deverão apresentar declaração fornecida pelo <br> órgão de origem ou por aquele onde presta serviço, de que não percebe auxílio da natureza idêntica. </p>
				      <br>
				  </td>                                                   
         																																																																					
         	   </tr>
         	</table>
         	<br>
         			
         	<table width="100%" border="1" cellpadding="2" cellspacing="1">
				<tr>
					<td width="100%">OBSERVAÇÕES</td>
				</tr>
			</table>
			
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>  <td>
					<p> Art.  9º O servidor que acumule licitamente cargos ou empregos fara jus à percepção de apenas um auxílio-<br>alimentação mediante opção<br>
					</p>
				    </b>
					<p> Art. 10º Para habilitar-se a receber o auxílio-alimentação, o servidor deverá preencher formulário próprio de <br>cadastramento e, se for o caso, apresentar:</p>
					</br>
					<p> I - em se tratando de requisitado ou cedido, declaração de outro órgão informando que não percebe o<br> benefício; e </p>
					</br>
					<p> II - na hipótese de acumulação lícita de cargo público, declaração do outro órgao informando que o servidor <br> não percebe auxílio de natureza idêntica. </p>
					</br>
					<p> § 1º Na hipótese do inciso I deste artigo, no caso de optar o servidor por receber o auxílio alimentação de <br> órgão diverso do que paga a sua remuneração, o valor do benefício será creditado em sua conta corrente. </p>
					</br>
					<p> § 2º A desistência da percepção do auxílio-alimentação, a solicitação de reinclusão e qualquer alteração na <br> situação de optante e não optante deverão ser formalizadasjunto à área competente. </p>
					</br>
			        <p> Art. 11 O auxílio-alimentação a ser concedido ao servidor cuja jornadas de trabalho <br> seja inferior a trinta horas <br> semanais corrsponderá a cinqüenta por cento do valor fixado para o benefício. </p>
			        </br>
			        <p> § 1º Ocorrendo a acumulação de cargos a que alude o art, 9º e sendo a soma das jornadas de trabalho superior a trinta horas semanais, o servidor perceberá o auxílio pelo seu valor integral, a ser pago pelo órgão <br> ou entidade de sua opção.
			        </p>
			        <br> 				
				  </td>
			</tr>
			</table>
			<br>
			
         	<table width="100%" border="0" cellpadding="2" cellspacing="1" align="center" bgcolor="#FFFFFF">
         		<tr>
         			<td>
         			<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp"/>
         			</td>         		
				</tr>
			</table>
		    <br>
		    <br>
					<table width="100%" align="left">		
						<tr>
							<td>5 - SEÇÃO DE BENEFÍCIOS</td>
						</tr>
						<tr>
							<td>
							<br/>
								Incluído por:___________________________________________  em:____/____/____ 
							</td>												
						</tr>
					</table>
					</td>
				</tr>			
			</table>
			
				<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />				
	</mod:letra>
	</body>
	</html>
	</mod:documento>
</mod:modelo>

