package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Criar extends AuthTest {

    public static String criarMemorandoTemporario(Pessoa pessoa) {
        return givenFor(pessoa)

                .param("modelo", "Memorando")
                .param("subscritor", pessoa.name())
                .param("classificacao", "00.01.01.01")
                .param("texto_memorando", "Testando Rest Assured.")
                .param("fecho", "Testando fecho")

                .when()
                .post("/sigaex/api/v1/documentos")

                .then()
                .statusCode(200)
                .body("sigladoc", notNullValue())

                .extract()
                .path("sigladoc");
    }

    public static String criarMemorando(Pessoa pessoa) {
        String siglaTmp = Criar.criarMemorandoTemporario(pessoa);
        String sigla = AssinarComSenha.assinarComSenha(pessoa, siglaTmp);
        sigla += "A";
        return sigla;
    }

    public static String compactarSigla(String sigla) {
        if (sigla == null)
            return null;
        return sigla.replace("-", "").replace("/", "");
    }

    @Test
    public void test_CriarDocumento_OK() {
        String siglaTmp = criarMemorandoTemporario(Pessoa.ZZ99999);
    }
}
