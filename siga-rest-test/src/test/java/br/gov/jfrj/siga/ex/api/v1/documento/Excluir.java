package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Excluir extends AuthTest {

    public static void excluir(Pessoa pessoa, String sigla) {
        givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/excluir")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    @Test
    public void test_Excluir_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);
        excluir(Pessoa.ZZ99999, siglaTmp);
    }

}
