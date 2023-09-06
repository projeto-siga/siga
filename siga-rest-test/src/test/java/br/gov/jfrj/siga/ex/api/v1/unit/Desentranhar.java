package br.gov.jfrj.siga.ex.api.v1.unit;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Desentranhar extends DocTest {

    public static void desentranhar(Pessoa pessoa, String sigla, String motivo) {
        ValidatableResponse resp = givenFor(pessoa)

                .queryParam("motivo", motivo)
                .pathParam("sigla", sigla)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/desentranhar")

                .then();

        assertStatusCode200(resp);
    }

    @Test
    public void test_Desentranhar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);

        Juntar.juntar(Pessoa.ZZ99999, sigla, siglaPai);

        desentranhar(Pessoa.ZZ99999, sigla, "Juntado por engano.");
        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
    }
}
