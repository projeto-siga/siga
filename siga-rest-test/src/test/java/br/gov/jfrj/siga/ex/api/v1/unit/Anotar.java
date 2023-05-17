package br.gov.jfrj.siga.ex.api.v1.unit;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Anotar extends DocTest {

    public static void anotar(Pessoa pessoa, String siglaTmp, String anotacao) {
        ValidatableResponse resp = givenFor(pessoa)

                .param("anotacao", anotacao)
                .pathParam("sigla", siglaTmp)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/anotar")

                .then();

        assertStatusCode200(resp);
    }

    @Test
    public void test_Anotar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("anotar", true);

        anotar(Pessoa.ZZ99999, sigla, "Teste de anotação");
        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
    }
}
