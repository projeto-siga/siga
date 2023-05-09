package br.gov.jfrj.siga.ex.api.v1.unit;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Juntar extends DocTest {

    public static void juntar(Pessoa pessoa, String sigla, String siglaPai) {
        ValidatableResponse resp = givenFor(pessoa)

                .queryParam("siglapai", siglaPai)
                .pathParam("sigla", sigla)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/juntar")

                .then();

        assertStatusCode200(resp);
    }

    @Test
    public void test_Juntar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("juntar", false);

        juntar(Pessoa.ZZ99999, sigla, siglaPai);
        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.JUNTADO);
    }
}
