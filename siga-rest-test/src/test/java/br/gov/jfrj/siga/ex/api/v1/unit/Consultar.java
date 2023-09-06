package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import io.restassured.response.ValidatableResponse;

public class Consultar extends DocTest {

    public static ValidatableResponse consultar(Pessoa pessoa, String sigla, boolean completo, boolean exibe) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("completo", completo)
                .param("exibe", exibe)
                .param("desabilitarRecebimentoAutomatico", true)

                .when()
                .get("/sigaex/api/v1/documentos/{sigla}")

                .then();

        assertStatusCode200(resp);

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
            resp.body(queryMarca(idMarcador, pessoa, lotacao), not(empty()));
        } catch (AssertionError e) {
            throw new AssertionError(
                    "Não encontrei marca " + idMarcador.name() + " com pessoa "
                            + (pessoa == null ? "nula" : pessoa.name()) + " e lotação "
                            + (lotacao == null ? "nula" : lotacao.name()));
        }
    }

    public static void naoContemMarca(ValidatableResponse resp, CpMarcadorEnum idMarcador, Pessoa pessoa,
            Lotacao lotacao) {
        try {
            resp.body(queryMarca(idMarcador, pessoa, lotacao), empty());
        } catch (AssertionError e) {
            throw new AssertionError(
                    "Encontrei marca " + idMarcador.name() + " com pessoa "
                            + (pessoa == null ? "nula" : pessoa.name()) + " e lotação "
                            + (lotacao == null ? "nula" : lotacao.name()));
        }
    }

    private static String queryMarca(CpMarcadorEnum idMarcador, Pessoa pessoa, Lotacao lotacao) {
        String s = "marcas.findAll {it.idMarcador == " + idMarcador.getId();

        if (pessoa == null)
            s += " && it.idPessoaIni == null";
        else
            s += " && it.idPessoaIni == " + pessoa.getId();

        if (lotacao == null)
            s += " && it.idLotacaoIni == null";
        else
            s += " && it.idLotacaoIni == " + lotacao.getId();

        s += "}.id";
        return s;
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

    public static void contemMovimentacao(ValidatableResponse resp, ExTipoDeMovimentacao idTipoDeMovimentacao,
            Pessoa pessoaCadastrante,
            Lotacao lotacaoCadastrante) {

        String s = "mobs[0].movs.findAll {it.exTipoMovimentacao == '" + idTipoDeMovimentacao.name() + "'";

        if (pessoaCadastrante == null)
            s += " && it.parte.cadastrante.siglaAmpliada == null";
        else
            s += " && it.parte.cadastrante.siglaAmpliada == '" + pessoaCadastrante.name() + "'";

        if (lotacaoCadastrante == null)
            s += " && it.parte.lotaCadastrante.siglaAmpliada == null";
        else
            s += " && it.parte.lotaCadastrante.siglaAmpliada == '" + lotacaoCadastrante.name() + "'";

        s += "}.idMov";

        try {
            resp.body(s, not(empty()));
        } catch (AssertionError e) {
            throw new AssertionError(
                    "Não encontrei movimentação " + idTipoDeMovimentacao.name() + " com cadastrante "
                            + (pessoaCadastrante == null ? "nula" : pessoaCadastrante.name())
                            + " e lotação cadastrante "
                            + (lotacaoCadastrante == null ? "nula" : lotacaoCadastrante.name()));
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

        consultar(Pessoa.ZZ99999, siglaTmp);

        contemMarca(CpMarcadorEnum.EM_ELABORACAO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.PENDENTE_DE_ASSINATURA, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.COMO_SUBSCRITOR, Pessoa.ZZ99999);
        contemAcao("assinar", true);
        contemAcao("transferir", false);
        contemAcao("arquivar_corrente_gravar", false);
    }

}
