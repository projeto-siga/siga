<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Tipo de declaração" var="tipoDeDeclaracao"
				opcoes="DESIGNAÇÃO;DISPENSA" reler="sim" />
		</mod:grupo>
		
		<c:if test="${tipoDeDeclaracao=='DISPENSA'}">
			<mod:grupo>
				<mod:grupo>
					<mod:pessoa titulo="Nome" var="servidor" />
				</mod:grupo>
				<mod:grupo>
					<mod:funcao titulo="Função Dispensada" 
						var="funcaoDispensada" />
				</mod:grupo>	
			</mod:grupo>
			
		</c:if>
		<c:if test="${tipoDeDeclaracao=='DESIGNAÇÃO'}">
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="servidor" />
			</mod:grupo>
			<mod:grupo>
				<mod:funcao titulo="Função Designada" 
						var="funcaoDesignada" />
			</mod:grupo>
		</c:if>
	 
		<mod:grupo titulo="Marque os itens que devem ser declarados">
			<mod:grupo>
				<mod:caixaverif titulo="Bens móveis" var="bensMoveis" reler="sim" />
				<c:if test="${bensMoveis=='Sim'}">					
						<mod:selecao titulo="Quantidade" var="quantidadeBensMoveis"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeBensMoveis}">
							<mod:grupo>
								<mod:grupo titulo="Bens Móveis ${i}">
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorBensMoveis${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição"  var="descricaoBensMoveis${i}" largura="30"/>
								</mod:grupo>
								
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
				
			<mod:grupo>
				<mod:caixaverif titulo="Bens imóveis" var="bensImoveis" reler="sim" />
				<c:if test="${bensImoveis=='Sim'}">
					<mod:selecao titulo="Quantidade" var="quantidadeBensImoveis"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeBensImoveis}">
							<mod:grupo>
								<mod:grupo titulo="Bens Imóveis ${i}">
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorBensImoveis${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição"  var="descricaoBensImoveis${i}" largura="30"/>
								</mod:grupo>	
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif titulo="Somoventes" var="somoventes" reler="sim" />
				<c:if test="${somoventes=='Sim'}">
					<mod:selecao titulo="Quantidade" var="quantidadeSomoventes"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeSomoventes}">
							<mod:grupo titulo="Somoventes ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorSomoventes${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoSomoventes${i}" largura="30"/>
								</mod:grupo>	
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif
					titulo="Dinheiros ou aplicações financeiras no pais ou no exterior"
					var="dinheiroAplicacoes" reler="sim" />
				<c:if test="${dinheiroAplicacoes=='Sim'}">
					<mod:selecao titulo="Quantidade" var="quantidadeDinheiroAplicacoes"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeDinheiroAplicacoes}">
							<mod:grupo titulo="Dinheiros ou aplicações ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorDinheiroAplicacoes${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoDinheiroAplicacoes${i}" largura="30"/>
								</mod:grupo>	
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif titulo="Bens no exterior" var="bensExterior"
					reler="sim" />
				<c:if test="${bensExterior=='Sim'}">
					<mod:selecao titulo="Quantidade" var="quantidadeBensExterior"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeBensExterior}">
							<mod:grupo titulo="Bem Exteriores ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorBensExterior${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoBensExterior${i}" largura="30"/>
								</mod:grupo>	
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
			
			<mod:grupo>
				<mod:caixaverif titulo="Títulos" var="titulos" reler="sim" />
				<c:if test="${titulos=='Sim'}">
					<mod:selecao titulo="Quantidade" var="quantidadeTitulos"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeTitulos}">
							<mod:grupo titulo="Títulos ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorTitulos${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoTitulos${i}" largura="30"/>
								</mod:grupo>	
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif titulo="Fontes de renda externa" var="renda" reler="sim" />
				<c:if test="${renda=='Sim'}">					
						<mod:selecao titulo="Quantidade" var="quantidadeRenda"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeRenda}">
							<mod:grupo titulo="Renda ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorRenda${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoRenda${i}" largura="30"/>
								</mod:grupo>
							</mod:grupo>
						</c:forEach>
				</c:if>
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif titulo="variação Patrimonial"
					var="variacaoPatrimonial" reler="sim" />
				<c:if test="${variacaoPatrimonial=='Sim'}">
					<mod:grupo>
						<mod:monetario titulo="Valor anterior" largura="12"
						maxcaracteres="10" var="valorPatrimonialAnterior" formataNum="sim" extensoNum="sim"
						reler="sim" />
					</mod:grupo>
					<mod:grupo>
						<mod:monetario titulo="Valor atual" largura="12" maxcaracteres="10"
						var="valorPatrimonialAtual" formataNum="sim" extensoNum="sim"
						reler="sim" />
					</mod:grupo>
						<c:if test="${valorPatrimonialAnterior != valorPatrimonialAtual }">
							<mod:grupo>
								<mod:memo colunas="60" linhas="3" titulo="Justificativa" var="justificativa" />
							</mod:grupo>
						</c:if>
				</c:if>
			</mod:grupo>
			
			<mod:grupo titulo="Direito sobre veículos">
				<mod:grupo>
					<mod:caixaverif titulo="Automóveis" var="automoveis" reler="sim" />
					<c:if test="${automoveis=='Sim'}">
						<mod:selecao titulo="Quantidade" var="quantidadeAutomoveis"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeAutomoveis}">
							<mod:grupo titulo="Automóveis ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorAutomoveis${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoAutomoveis${i}" largura="30"/>
								</mod:grupo>
							</mod:grupo>
						</c:forEach>
					</c:if>
				</mod:grupo>
				<mod:grupo>
					<mod:caixaverif titulo="Embarcações" var="embarcacoes" reler="sim" />
					<c:if test="${embarcacoes=='Sim'}">
						<mod:selecao titulo="Quantidade" var="quantidadeEmbarcacoes"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeEmbarcacoes}">
							<mod:grupo titulo="Embarcações ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorEmbarcacoes${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoEmbarcacoes${i}" largura="30"/>
								</mod:grupo>							
							</mod:grupo>
						</c:forEach>
					</c:if>
				</mod:grupo>
				<mod:grupo>
					<mod:caixaverif titulo="Aeronáveis" var="aeronaveis" reler="sim" />
					<c:if test="${aeronaveis=='Sim'}">
						<mod:selecao titulo="Quantidade" var="quantidadeAeronaveis"
									opcoes="1;2;3;4;5;6;7;8;9;10"
									reler="sim" />
						<c:forEach var="i" begin="1" end="${quantidadeAeronaveis}">
							<mod:grupo titulo="Aeronáveis ${i}">
								<mod:grupo>
									<mod:monetario titulo="Valor" largura="12" maxcaracteres="10" var="valorAeronaveis${i}" formataNum="sim" extensoNum="sim"	reler="sim" />
								</mod:grupo>
								<mod:grupo>
									<mod:texto titulo="Descrição" var="descricaoAeronaveis${i}" largura="30"/>
								</mod:grupo>							
							</mod:grupo>
						</c:forEach>
					</c:if>
				</mod:grupo>
			</mod:grupo>

			
			
		</mod:grupo>

	</mod:entrevista>
	<mod:documento>
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
		<c:import url="/paginas/expediente/modelos/inc_tit_juizfedDirForo.jsp" />
		<br>
		<br>
		<h2 align="center"><u><strong>DECLARAÇÃO DE BENS E
		VALORES</strong></u></h2>
		<h2 align="center"><u><strong>${tipoDeDeclaracao }</strong></u></h2>
		
		<br>
		<br>
		${valor}
		<p style="TEXT-INDENT: 2cm" align="justify">Eu, <c:if
			test="${tipoDePosse=='CARGO EFETIVO'}">
			<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" /> em virtude de posse de cargo efetivo</c:if>
			<c:if test="${tipoDePosse=='CARGO COMISSIONADO'}">
			<b>${requestScope['nomeComissionado_pessoaSel.id']}</b> em virtude de posse do cargo comissionado de <b>${requestScope['funcaoComissionada_funcaoSel.descricao']}</b>
		</c:if> 
		<c:if test="${tipoDeDeclaracao=='DESIGNAÇÃO'}">
			<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" /> em virtude de designação para a 
			função de <b>${requestScope['funcaoDesignada_funcaoSel.descricao']}</b></c:if>
		<c:if test="${tipoDeDeclaracao=='DISPENSA'}">
			<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" /> em virtude de dispensa das atividades de ${requestScope['funcaoDispensada_funcaoSel.descricao']}</c:if>, declaro, para fins do disposto nos arts. 1&ordm;, inciso VII, e
		2&ordm; da Lei n&ordm; 8.730/93, c/c art. 11, XI, da Resol.
		284/CJF/2002,
		que exerce/exerceu nesta Seção Judiciária, conforme Portaria n&ordm;,
		de(perguntar ao usuario), <b>publicada</b> no Boletim Interno em, que possui em (data de início da designação <b>ou</b>
		dispensa da referida função) os <b>bens</b> abaixo discriminados, seguidos dos respectivos <b>valores</b>:
		</p>
		<br>
		<p style="TEXT-INDENT: 2cm" align="justify">
		<ul>
			<c:if test="${bensMoveis=='Sim'}">
				<b>Bens Móveis:</b>
				<c:forEach var="i" begin="1" end="${quantidadeBensMoveis}">
					<li>${requestScope[f:concat('descricaoBensMoveis',i)]} - ${requestScope[f:concat('valorBensMoveis',i)]} <b>(${requestScope[f:concat(f:concat('valorBensMoveis',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
			</c:if>
			
			<c:if test="${bensImoveis=='Sim'}">
				<b>Bens Imóveis:</b>
				<c:forEach var="i" begin="1" end="${quantidadeBensImoveis}">
					<li>${requestScope[f:concat('descricaoBensImoveis',i)]} - ${requestScope[f:concat('valorBensImoveis',i)]} <b>(${requestScope[f:concat(f:concat('valorBensImoveis',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
			</c:if>
			
			<c:if test="${somoventes=='Sim'}">
				<b>Somoventes:</b>
				<c:forEach var="i" begin="1" end="${quantidadeSomoventes}">
					<li>${requestScope[f:concat('descricaoSomoventes',i)]} - ${requestScope[f:concat('valorSomoventes',i)]} <b>(${requestScope[f:concat(f:concat('valorSomoventes',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
			</c:if>
			
			<c:if test="${dinheiroAplicacoes=='Sim'}">
				<b>Dinheiro ou aplicações financeiras:</b>
				<c:forEach var="i" begin="1" end="${quantidadeDinheiroAplicacoes}">
					<li>${requestScope[f:concat('descricaoDinheiroAplicacoes',i)]} - ${requestScope[f:concat('valorDinheiroAplicacoes',i)]} <b>(${requestScope[f:concat(f:concat('valorDinheiroAplicacoes',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
			</c:if>
			
			<c:if test="${bensExterior=='Sim'}">
				<b>Bens no Exterior:</b>
				<c:forEach var="i" begin="1" end="${quantidadeBensExterior}">
					<li>${requestScope[f:concat('descricaoBensExterior',i)]} - ${requestScope[f:concat('valorBensExterior',i)]} <b>(${requestScope[f:concat(f:concat('valorBensExterior',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
				
			</c:if>
			
			
			<c:if test="${titulos=='Sim'}">
				<b>Títulos:</b>
				<c:forEach var="i" begin="1" end="${quantidadeTitulos}">
					<li>${requestScope[f:concat('descricaoTitulos',i)]} - ${requestScope[f:concat('valorTitulos',i)]} <b>(${requestScope[f:concat(f:concat('valorTitulos',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
				
			</c:if>
			<c:if test="${automoveis=='Sim'}">
				<b>Automóveis:</b>
				<c:forEach var="i" begin="1" end="${quantidadeAutomoveis}">
					<li>${requestScope[f:concat('descricaoAutomoveis',i)]} - ${requestScope[f:concat('valorAutomoveis',i)]} <b>(${requestScope[f:concat(f:concat('valorAutomoveis',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
				
			</c:if>
			<c:if test="${embarcacoes=='Sim'}">
				<b>Embarcações:</b>
				<c:forEach var="i" begin="1" end="${quantidadeEmbarcacoes}">
					<li>${requestScope[f:concat('descricaoEmbarcacoes',i)]} - ${requestScope[f:concat('valorEmbarcacoes',i)]} <b>(${requestScope[f:concat(f:concat('valorEmbarcacoes',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
			</c:if>
			<c:if test="${aeronaveis=='Sim'}">
				<b>Aeronávies:</b>
				<c:forEach var="i" begin="1" end="${quantidadeAeronaveis}">
					<li>${requestScope[f:concat('descricaoAeronaveis',i)]} - ${requestScope[f:concat('valorAeronaveis',i)]} <b>(${requestScope[f:concat(f:concat('valorAeronaveis',i),'vrextenso')]})</b></li><br><br>
				</c:forEach>
			</c:if>
			<c:if test="${variacaoPatrimonial=='Sim'}">
				<li>váriação Patrimonial
				<c:if test="${valorPatrimonialAnterior == valorPatrimonialAtual }">
					<b>${valorPatrimonialAtual} (${valorPatrimonialAtualvrextenso})</b>
				</c:if>
				<c:if test="${valorPatrimonialAnterior != valorPatrimonialAtual }">
										valor Anterior: <b>${valorPatrimonialAnterior} (${valorPatrimonialAnteriorvrextenso})</b> -
										valor Atual: <b>${valorPatrimonialAtual} (${valorPatrimonialAtualvrextenso})</b>,<br>
										<b>Justificativa:</b> ${justificativa}
				</c:if>					
				</li>
			</c:if>
		</ul>
		</p>
		<p align="center">
					____________________________________________________
		</p>
		<p align="center">
					${requestScope['servidor_pessoaSel.descricao']}
		</p>
					
		</table>
		<c:import
			url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />

		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />

		</body>
		</html>
	</mod:documento>
</mod:modelo>
