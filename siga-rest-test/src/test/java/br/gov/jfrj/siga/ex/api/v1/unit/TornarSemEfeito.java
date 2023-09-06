package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class TornarSemEfeito extends DocTest {

    public static void tornarSemEfeito(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/tornar-sem-efeito")

                .then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_TornarSemEfeito_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        tornarSemEfeito(Pessoa.ZZ99999, sigla);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.SEM_EFEITO, Pessoa.ZZ99999);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);
    }

}
