package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.api.v1.AuthTest;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.unit.Consultar;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import io.restassured.response.ValidatableResponse;

public class DocTest extends AuthTest {

    private static ThreadLocal<ValidatableResponse> tlResp = new ThreadLocal<>();

    public static void assertStatusCode200(ValidatableResponse resp) {
        try {
            resp.statusCode(200);
        } catch (AssertionError e) {
            String errormsg = resp.extract().path("errormsg");
            throw new RuntimeException(errormsg);
        }
    }

    public static ValidatableResponse consultar(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = Consultar.consultar(pessoa, sigla, true, true);
        tlResp.set(resp);
        return resp;
    }

    public static void contemMarca(CpMarcadorEnum idMarcador, Pessoa pessoa,
            Lotacao lotacao) {
        Consultar.contemMarca(tlResp.get(), idMarcador, pessoa, lotacao);
    }

    public static void contemMarca(CpMarcadorEnum idMarcador, Pessoa pessoa) {
        contemMarca(idMarcador, pessoa, null);
    }

    public static void contemMarca(CpMarcadorEnum idMarcador,
            Lotacao lotacao) {
        contemMarca(idMarcador, null, lotacao);
    }

    public static void contemMarca(CpMarcadorEnum idMarcador) {
        contemMarca(idMarcador, null, null);
    }

    public static void naoContemMarca(CpMarcadorEnum idMarcador, Pessoa pessoa,
            Lotacao lotacao) {
        Consultar.naoContemMarca(tlResp.get(), idMarcador, pessoa, lotacao);
    }

    public static void naoContemMarca(CpMarcadorEnum idMarcador, Pessoa pessoa) {
        naoContemMarca(idMarcador, pessoa, null);
    }

    public static void naoContemMarca(CpMarcadorEnum idMarcador,
            Lotacao lotacao) {
        naoContemMarca(idMarcador, null, lotacao);
    }

    public static void naoContemMarca(CpMarcadorEnum idMarcador) {
        naoContemMarca(idMarcador, null, null);
    }

    public static void contemAcao(String acao, Boolean pode) {
        Consultar.contemAcao(tlResp.get(), acao, pode);
    }

    public static void contemMovimentacao(ExTipoDeMovimentacao idTipoDeMovimentacao,
            Pessoa pessoaCadastrante, Lotacao lotacaoCadastrante) {
        Consultar.contemMovimentacao(tlResp.get(), idTipoDeMovimentacao, pessoaCadastrante, lotacaoCadastrante);
    }

    public static void contemVizNode(String graphPath, String id, String shape,
            String color) {
        Consultar.contemVizNode(tlResp.get(), graphPath, id, shape, color);
    }
}
