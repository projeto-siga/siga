package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;
import io.restassured.path.json.JsonPath;

public class Consultar extends AuthTest {

    public static String consultar(User user, String sigla, boolean completo, boolean exibe) {
        String body = givenFor(user)

                .pathParam("sigla", sigla)
                .param("completo", completo)
                .param("exibe", exibe)

                .when()
                .get("/sigaex/api/v1/documentos/{sigla}")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"))

                .extract()
                .body().asString();
        return body;
    }

    @Test
    public void test_Consultar_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario();
//        String sigla = AssinarComSenha.assinarComSenha(siglaTmp);
//        sigla += "A";

        String body = consultar(User.ZZ99999, siglaTmp, true, true);

//        JsonPath.from(body).assertThat
//        
//        body("store.book.findAll { it.price < 10 }.title", hasItems("Sayings of the Century", "Moby Dick"))
    }

}
