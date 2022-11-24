package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

//TODO: O método de cancelar ainda não está implementado na API
public class Cancelar extends DocTest {

    public static void cancelar(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/cancelar")

                .then();

        assertStatusCode200(resp);

        resp
                .body("sigla", notNullValue())
                .body("status", equalTo("OK"));
    }

    // @Test
    public void test_Cancelar_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = Finalizar.finalizar(Pessoa.ZZ99999, siglaTmp);
        cancelar(Pessoa.ZZ99999, sigla);
    }

}
