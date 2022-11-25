# SIGA-REST-TEST

Este mótudo utiliza o "rest assured" para realizar testes unitário, de módulo e de sistema nos webservices REST do Siga.

Por enquanto foram escritos apenas alguns testes para o módulo Siga-Doc.

O ambiente no qual os testes é executado e os tipos de testes previstos são detalhados a seguir.

## Ambiente de Testes

Os testes são realizados a partir de informações contidas no banco de dados produzido pelas "migrations". Estão disponíveis portanto, 3 usuários e 3 lotações a saber:

- ZZ99999 - ZZLTEST
- ZZ99998 - ZZLTEST2
- ZZ99997 - ZZLTEST3

E existem modelos de:

- Memorando
- Despacho
- Informação
- Ofício
- Parecer
- Planta
- Processo de Pessoal
- Processo de Execução Orçamentária e Financeira
- Boletim Interno

## Testes Unitários

Todos os testes unitários devem ficar no package *br.gov.jfrj.siga.ex.api.v1.unit*

Os testes unitários deve contem um método estático que faz a chamada ao Rest Assured para chamar determinado webservice e verificar o retorno.

Além disso, devem conter um ou mais testes unitários para validar o funcionamento deste método estático.

Vamos analisar o exemplo do testes do método de receber:


```Java
public class Receber extends DocTest {

    public static void receber(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when().post("/sigaex/api/v1/documentos/{sigla}/receber").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_Receber_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        receber(Pessoa.ZZ99998, sigla);

        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", true);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", null);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");
    }

}
```

Temos o método estático "receber" que utilizando o método givenFor(Pessoa.ZZ99999) se autentica no webservice como o usuário ZZ9999.

Depois, chama o webservice "receber" e testa o resultado com "assertStatusCode200". 
Este método é importante, pois ele reportará corretamente o erro contido no parâmetro "errormsg" do resultado JSON.

Os outros métodos utilizados no "receber" estão no padrão do Rest Assured, e podem ser consultados na sua [documentação](https://rest-assured.io/).

Depois, temos o teste unitário "test_Receber_OK".

Neste artefato só será realizado o teste até o momento do recebimento. Não teria sentido, por exemplo, fazer um arquivamento depois do recebimento.

Mesmo assim, para testar o recebimento é necessário, antes, criar um documento e tramitá-lo para alguém. Isso pode ser feito chamando os metodos estáticos dos testes unitários de "Criar" e "Tramitar". Não é necessários testar detalhes sobre a criação ou o trâmite, pois isso já foi feito no "Criar" e no "Tramitar".

Depois de chamar o método "receber" são feitos testes minuciosos sobre o estado do documento. Para isso, foram preparados alguns métodos facilitadores:

#### consultar

Este método chama o método estático "Consultar.consultar" e armazena o resultado em uma propriedade ThreadLocal, de modo que não precisamos nos preocupar em guardar o 
resultado em uma variável e repassá-la para todos os métodos subsequentes.

Um detalhe é que quando chamamos o "consultar", precisamos informar quem é o usuário que está fazendo a consulta,
já que o resultado é diferente dependendo da pessoa que está consultando.

#### contemMarca

Verifica se o resultado da consulta contém determinada marca. Está marca pode estar relacionada a uma pessoa, a uma lotação ou a uma pessoa e uma lotação. Além do identificador do marcador, é necessário informar exatamente a pessoa e a lotação desejadas.

#### contemAcao

Verifica se existe determinada ação e se ela está ativa ou não.

#### contemVizNode

Verifica se um gráfico contém determinado nó. Além do identificador do nó, também é necessário informar qual é o shape e qual é a cor.

## Testes de Módulo

Todos os testes de módulo devem ficar no package *br.gov.jfrj.siga.ex.api.v1.module*

Os testes de módulo não contêm métodos estáticos e nunca chamam o Rest Assured.

O objetivo aqui é realizar diversas operações em sequência e reproduzir casos mais complexos de uso.

Vamos analisar o exemplo do testes da sequência de tramitar, receber e arquivar:


```Java
public class TramitarReceberEArquivar extends DocTest {

    @Test
    public void test_TramitarReceberEArquivar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        
        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);
        
        Receber.receber(Pessoa.ZZ99998, sigla);
        
        ArquivarCorrente.arquivar(Pessoa.ZZ99998, sigla);
        
        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.ARQUIVADO_CORRENTE, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        contemAcao("arquivar_corrente_gravar", false);
        contemAcao("reabrir_gravar", true);
    }

}
```

Neste caso, foi feita uma inspeção detalhada, utilizando os facilitadores "cosultar", "contemMarca" e "contemAcao", apenas para na última operação. 

Nada nos impediria de testar estados anteriores mas, considerando que "tramitarParaLotacao" já foi testado em detalhes em "Tramitar" e que "receber" já foi testado na classe "Receber",  apenas o arquivamento deveria receber este tratamento especial.

## Testes de Sistema

Todos os testes de sistema devem ficar no package *br.gov.jfrj.siga.ex.api.v1.system*

Os testes de sistema não contêm métodos estáticos e nunca chamam o Rest Assured.

O objetivo aqui é realizar operações que envolvem mais de um módulo como, por exemplo, testar as integrações entre o Siga-Doc e o Siga-WF.
