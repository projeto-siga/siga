package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Criar extends DocTest {

    public static String criarMemorandoTemporario(Pessoa pessoa) {
        ValidatableResponse resp = givenFor(pessoa)

                .param("modelo", "Memorando")
                .param("subscritor", pessoa.name())
                .param("classificacao", "00.01.01.01")
                .param("texto_memorando", "Testando Rest Assured.")
                .param("fecho", "Testando fecho")

                .when().post("/sigaex/api/v1/documentos").then();

        assertStatusCode200(resp);

        return resp.body("sigladoc", notNullValue())

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
    public void test_CriarMemorandoTemporario_OK() {
        String siglaTmp = criarMemorandoTemporario(Pessoa.ZZ99999);

        consultar(Pessoa.ZZ99999, siglaTmp);
        contemMarca(CpMarcadorEnum.EM_ELABORACAO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.PENDENTE_DE_ASSINATURA, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.COMO_SUBSCRITOR, Pessoa.ZZ99999);
        contemAcao("assinar", true);
        contemAcao("transferir", false);
    }

    @Test
    public void test_CriarMemorandoAssinado_OK() {
        String sigla = criarMemorando(Pessoa.ZZ99999);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("assinar", false);
        contemAcao("transferir", true);
    }
}
