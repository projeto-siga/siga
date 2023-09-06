package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Excluir extends DocTest {

    public static void excluir(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/excluir")

                .then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_Excluir_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);
        excluir(Pessoa.ZZ99999, siglaTmp);
    }

}
