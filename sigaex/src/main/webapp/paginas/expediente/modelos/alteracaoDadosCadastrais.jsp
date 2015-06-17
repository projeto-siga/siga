<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
ALTERAÇÃO DE DADOS CADASTRAIS -->

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">  
    <mod:entrevista>    
        <mod:grupo titulo="O QUE SERÁ ATUALIZADO NO CADASTRO FUNCIONAL?"></mod:grupo>
        <mod:selecao titulo="Escolaridade" var="alterarEscolaridade"
            opcoes="Não;Sim" reler="sim" />

        <mod:selecao titulo="Estado Civil" var="alterarEstadoCivil"
            opcoes="Não;Sim" reler="sim" />

        <mod:selecao titulo="Endereço" var="alterarEndereco" 
            opcoes="Não;Sim" reler="sim" />

        <mod:selecao titulo="Telefone" var="alterarTelefone" 
            opcoes="Não;Sim" reler="sim" />
        
        <hr>

        <c:if test="${alterarEscolaridade == 'Sim'}">
            <mod:grupo>
                <mod:selecao titulo="Escolaridade" var="escolaridade"
                    opcoes="Antigo Primário;Antigo Ginásio;Antigo Científico;
                Ensino Fundamental ou 1º Grau; Ensino Médio ou 2º grau;
                Superior ou Graduação; Pós-Graduação, Mestrado ou Doutorado"
                    reler="sim" />
                    <c:if test="${escolaridade == ' Pós-Graduação, Mestrado ou Doutorado'}">
                    <mod:grupo>
                    <mod:selecao titulo="Requer Adicional de Qualificação por curso de Pós-Graduação" var="adicionalopg"
                    opcoes=" ;Sim;Não"
                    reler="não" />
                    </mod:grupo>
                    </c:if>
            </mod:grupo>
        </c:if>

        <c:if test="${alterarEstadoCivil == 'Sim'}">
            <mod:grupo>
                <mod:selecao titulo="Estado Civil" var="estadocivil"
                    opcoes="Solteiro(a);Casado(a);Divorciado(a);Desquitado(a);Viúvo(a)"
                    reler="não" />
            </mod:grupo>
        </c:if>

        <c:if test="${alterarEndereco == 'Sim'}">
            <mod:grupo>
                <mod:texto largura="80" titulo="Endereço" var="endereco"/>
            </mod:grupo>
            <mod:grupo>
                <mod:texto largura="40" titulo="Bairro" var="bairro" />
                <mod:texto largura="30" titulo="Cidade" var="cidade" />
                <mod:texto largura="2" titulo="UF" var="uf" />
            </mod:grupo>
            <mod:grupo>
                <mod:texto largura="9" titulo="CEP" var="cep" />
            </mod:grupo>
            <mod:mensagem titulo="Atenção" texto="&quot;Se comprovadamente falsa a declaração, sujeitar-se-á o declarante às sanções civis, administrativas e criminais previstas na legislação aplicável.&quot; (Lei 7115/1983, art. 2º)" vermelho="Sim"></mod:mensagem>
        </c:if>

        <c:if test="${alterarTelefone == 'Sim'}">
            <mod:grupo>
                <mod:texto largura="18" titulo="Telefone 1" var="tel1" />
            </mod:grupo>
            <mod:grupo>
                <mod:texto largura="18" titulo="Telefone 2" var="tel2" />
            </mod:grupo>
            <mod:grupo>
                <mod:texto largura="18" titulo="Telefone 3" var="tel3" />
            </mod:grupo>
        </c:if>

    </mod:entrevista>


    <mod:documento>
        <mod:valor var="texto_requerimento">
            
        <p style="TEXT-INDENT: 2cm" align="justify" >       
        ${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a)
        no(a) ${doc.subscritor.lotacao.descricao}, solicita a Vossa Senhoria
        que sejam efetuadas as seguintes <b>ALTERAÇÕES EM SEUS ASSENTAMENTOS FUNCIONAIS</b>:
        </p>    
        
        <ul>
        <c:if test="${alterarEscolaridade == 'Sim'}">
        <li>
        Escolaridade: (Com documento autenticado em anexo)<br/>${escolaridade}<br />
        <c:if test="${escolaridade == ' Pós-Graduação, Mestrado ou Doutorado'}"><br />
        -   Requer Adicional de Qualificação por curso de Pós-Graduação? ${adicionalopg}</li>
        </c:if>
        </c:if>
        
        <c:if test="${alterarEstadoCivil == 'Sim'}">
            <li>Estado Civil: (Com documento autenticado em anexo)<br/>
            ${estadocivil}</li> 
        </c:if>

            
        <c:if test="${alterarEndereco == 'Sim'}">
            <li>Endereco:<br />
                        ${endereco}</li>
                Bairro:  ${bairro}<br />
                Cidade: ${cidade}<br />
                UF: ${uf}<br />
                CEP:  ${cep}<br />
                <b>Atenção: &quot;Se comprovadamente falsa a declaração, 
                sujeitar-se-á o declarante às sanções civis, administrativas e criminais 
                previstas na legislação aplicável.&quot; (Lei 7115/1983, art. 2º)</b> 
                
        </c:if>
            
        
        <c:if test="${tel1 != null || tel2 != null || tel3 != null }">
            <li>TELEFONE(S)<br/>
            
            
                    <c:if test="${tel1 != ''}"> 
                    ${tel1}<br/>
                    </c:if> 
                
                    <c:if test="${tel2 != ''}"> 
                    ${tel2}<br/>
                    </c:if> 
                    
                    <c:if test="${tel3 != ''}"> 
                    ${tel3}<br/>
                    </c:if> 
        </c:if>
        </ul>
        </mod:valor>        
    </mod:documento>
</mod:modelo>
