package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.AuthTest;
import io.restassured.response.ValidatableResponse;

public class Consultar extends AuthTest {

    public static ValidatableResponse consultar(Pessoa pessoa, String sigla) {
        return consultar(pessoa, sigla, true, true);
    }

    public static ValidatableResponse consultar(Pessoa pessoa, String sigla, boolean completo, boolean exibe) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("completo", completo)
                .param("exibe", exibe)
                .param("desabilitarRecebimentoAutomatico", true)

                .when()
                .get("/sigaex/api/v1/documentos/{sigla}")

                .then()
                .statusCode(200);
        return resp;
    }

    public static void contemMarcas(ValidatableResponse resp, CpMarcadorEnum... idMarcador) {
        int[] a = new int[idMarcador.length];

        for (CpMarcadorEnum m : idMarcador) {
            resp.body("marcas.idMarcador", hasItem((int) m.getId()));
        }
    }

    public static void contemMarca(ValidatableResponse resp, CpMarcadorEnum idMarcador, Pessoa pessoa,
            Lotacao lotacao) {
        try {
            if (pessoa == null)
                if (lotacao == null)
                    resp.body("marcas.findAll {it.idMarcador == " + idMarcador.getId()
                            + " && it.idPessoaIni == null && it.idLotacaoIni == null}.id", not(empty()));
                else
                    resp.body("marcas.findAll {it.idMarcador == " + idMarcador.getId()
                            + " && it.idPessoaIni == null && it.idLotacaoIni == "
                            + lotacao.getId() + "}.id", not(empty()));
            else if (lotacao == null)
                resp.body("marcas.findAll {it.idMarcador == " + idMarcador.getId() + " && it.idPessoaIni == "
                        + pessoa.getId() + " && it.idLotacaoIni == null}.id", not(empty()));

            else
                resp.body("marcas.findAll {it.idMarcador == " + idMarcador.getId() + " && it.idPessoaIni == "
                        + pessoa.getId() + " && it.idLotacaoIni == "
                        + lotacao.getId() + "}.id", not(empty()));
        } catch (AssertionError e) {
            throw new AssertionError(
                    "Não encontrei marca " + idMarcador.name() + " com pessoa "
                            + (pessoa == null ? "nula" : pessoa.name()) + " e lotação "
                            + (lotacao == null ? "nula" : lotacao.name()));
        }
    }

    public static void contemMarca(ValidatableResponse resp, CpMarcadorEnum idMarcador, Lotacao lotacao) {
        contemMarca(resp, idMarcador, null, lotacao);
    }

    public static void contemMarca(ValidatableResponse resp, CpMarcadorEnum idMarcador, Pessoa pessoa) {
        contemMarca(resp, idMarcador, pessoa, null);
    }

    public static void contemAcao(ValidatableResponse resp, String acao, Boolean pode) {
        try {
            resp.body("mobs[0].acoes.findAll {it.acao == '" + acao
                    + "' && it.pode == " + pode + "}.id", not(empty()));
        } catch (AssertionError e) {
            throw new AssertionError(
                    "Não encontrei ação " + acao + " com pode == " + pode);
        }
    }

    public static void contemVizNode(ValidatableResponse resp, String graphPath, String id, String shape,
            String color) {
        String viz = resp.extract().path("vizTramitacao");
        viz = viz.replaceAll("\\]\\[", ", ");
        String[] lines = viz.split(";");
        for (int i = 0; i < lines.length; i++)
            lines[i] = lines[i].trim();

        String line = null;
        for (int i = 0; i < lines.length; i++)
            if (lines[i].startsWith("\"" + id + "\"[")) {
                line = lines[i];
                break;
            }

        if (line == null)
            throw new RuntimeException("Nó '" + id + "' não encontrado no gráfico " + graphPath);

        String[] attrs = line.substring(line.indexOf("[") + 1, line.length() - 1).replace("\", ", "\"|").split("\\|");

        assertVizAttr(attrs, graphPath, id, "shape", shape);
        assertVizAttr(attrs, graphPath, id, "color", color);
    }

    private static void assertVizAttr(String[] attrs, String graphPath, String id, String key, String value) {
        String prefix = key + "=\"";
        String desired = prefix + value + "\"";
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].startsWith(prefix)) {
                if (attrs[i].equals(desired))
                    return;
                else if (value != null)
                    throw new RuntimeException("Nó '" + id + "' do gráfico " + graphPath + " deveria ter " + desired
                            + " mas tem " + attrs[i]);
                else
                    throw new RuntimeException("Nó '" + id + "' do gráfico " + graphPath + " não deveria ter " + key
                            + " mas tem " + attrs[i]);
            }
        }
        if (value != null)
            throw new RuntimeException("Nó '" + id + "' do gráfico " + graphPath + " deveria ter " + desired
                    + " mas não tem esta propriedade");
    }

    @Test
    public void test_Consultar_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);

        ValidatableResponse resp = consultar(Pessoa.ZZ99999, siglaTmp);

        contemMarca(resp, CpMarcadorEnum.EM_ELABORACAO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(resp, CpMarcadorEnum.PENDENTE_DE_ASSINATURA, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(resp, CpMarcadorEnum.COMO_SUBSCRITOR, Pessoa.ZZ99999);

        contemAcao(resp, "assinar", true);
        contemAcao(resp, "transferir", false);

        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        resp = consultar(Pessoa.ZZ99999, sigla);

        contemMarca(resp, CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);

        contemAcao(resp, "assinar", false);
        contemAcao(resp, "transferir", true);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        resp = consultar(Pessoa.ZZ99998, sigla);

        contemMarca(resp, CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemMarca(resp, CpMarcadorEnum.EM_TRANSITO_ELETRONICO, Pessoa.ZZ99999, Lotacao.ZZLTEST);

        contemAcao(resp, "receber", true);
    }

}
