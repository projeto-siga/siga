<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista> 
	<mod:grupo titulo="Período de Licença para Capacitação">
			<mod:data titulo="De" var="dataInicio" />
			<mod:data titulo="a" var="dataFim" />
		</mod:grupo>
		    
			<mod:selecao titulo="Participação em:" var="cursos" opcoes="Curso de Capacitação;Pesquisa e Levantamento de Dados" reler="ajax" idAjax="cursosAjax" />
				<mod:grupo depende="cursosAjax">
					<c:if test="${cursos eq 'Pesquisa e Levantamento de Dados'}">
						<mod:texto titulo="Tema do curso:" var="temaCurso" largura="60" />
					</c:if>
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
		<c:import url="/paginas/expediente/modelos/inc_tit_juizfedDirForo.jsp" />
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria, nos termos do art.87, da Lei n&ordm; 8.112/90, com a
		redação da Lei n&ordm; 9.527/97, c/c Resolução n&ordm; 5/2008, do Conselho da Justiça Federal, e Resolução n&ordm; 22/2008, do TRF da 2ª Região, <b>LICENÇA PARA CAPACITAÇÃO</b> a que faz jus, para fruição 
		<c:choose>
				<c:when test="${(dataInicio == dataFim) or (empty dataFim)}">
					no dia <b>${dataInicio}</b>,
				</c:when>
					<c:otherwise>
					no período de <b>${dataInicio}</b> a <b>${dataFim}</b>,
					</c:otherwise>
			</c:choose>
		
		destinada à 
			<c:choose>
				<c:when test="${cursos eq 'Curso de Capacitação'}">
					participação em Curso de Capacitação.
				</c:when>
				<c:otherwise>
					pesquisa e levantamento de dados necessários à elaboração de trabalho para conclusão de curso de pós-graduação, cujo tema é: ${temaCurso}.				
				</c:otherwise>
			</c:choose>
		</p>
		
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
		<br/>
		<br/>
		<p align="center">${doc.dtExtenso}</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?apenasCargo=sim" />
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
			
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<c:import url="/paginas/expediente/modelos/inc_tit_termoCompromisso.jsp" />
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, firma o compromisso de apresentar relatório semanal das atividades desenvolvidas, 
		devidamente endossado pelo orientador ou coordenador do respectivo curso, nos termos do art. 2º, da Resolução nº 22/2008, do TRF da 2ª Região, tendo em vista tratar-se de licença para capacitação com a finalidade de conclusão de curso de especialização, mestrado ou doutorado. 
		</p>
		<br/>
		<br/>
		<p align="center">${doc.dtExtenso}</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?apenasCargo=sim" />
		
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<br/>
        <br/>
        <c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" />
		<p style="TEXT-INDENT: 2cm" align="justify">
        
        ${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao},declara, nos termos do § 7º do art. 23 da Resolução nº 5/2008, do CJF, estar ciente de que:</br>
        <br/>
        1 -  Ao final da atividade deverá apresentar à Subsecretaria de Gestão de Pessoas, no prazo máximo de 30 dias, os seguintes documentos comprobatórios, conforme natureza da ação de capacitação: comprovante de frequência, participação e aproveitamento no evento objeto da licença, nas hipóteses de participação em evento com carga horária mínima de 12 horas; comprovante de entrega de monografia, dissertação, tese ou trabalho de conclusão de curso de graduação ou pós-graduação, bem como a entrega de cópia do trabalho final de curso, preferencialmente por meio eletrônico, à unidade de recursos humanos do órgão; comprovante de participação em atividade de orientação para elaboração de monografia, dissertação, tese ou trabalho de conclusão de curso de graduação ou pós-graduação; declaração de aprovação ou certificado de conclusão do curso; declaração de participação em processo seletivo para ingresso em curso de pós-graduação stricto sensu ou de obtenção de certificação de competências profissionais.<br>
        <br/>
        <br/>
        2 -  Na hipótese de não participação integral no evento objeto da licença, deverá requerer, mediante justificativa pertinente, a interrupção da licença, com o retorno imediato ao trabalho. <br>
        <br/>
        <br/>
       3 -  A ausência de comprovação de que trata o item 1 ou o não acatamento da justificativa de que trata o item 2 ensejará a cassação da licença com efeito retroativo, sendo computados como faltas ao serviço e descontados em folha de pagamento os dias referentes à licença cassada, garantidos o contraditório e a ampla defesa. Nos termos da legislação vigente, será instraurada sindicância para apuração de infração disciplinar.<br>
      </p>
        <br/>
        <br/>
        <c:import
            url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
		</body>
		</html>
	</mod:documento>
</mod:modelo>
