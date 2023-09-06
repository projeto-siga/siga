package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Notificar extends DocTest {

    public static void notificar(
            Pessoa pessoa, String sigla, String pessoaDest, String lotacaoDest, String orgao,
            String observacao, String dataDevolucao) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("lotacao", lotacaoDest)
                .param("matricula", pessoaDest)
                .param("orgao", orgao)
                .param("observacao", observacao)
                .param("dataDevolucao", dataDevolucao)

                .when().post("/sigaex/api/v1/documentos/{sigla}/notificar").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    public static void notificar(Pessoa pessoa, String sigla, Pessoa pessoaDest) {
        notificar(pessoa, sigla, pessoaDest.name(), null, null, null, null);
    }

    public static void notificar(Pessoa pessoa, String sigla, Lotacao lotacaoDest) {
        notificar(pessoa, sigla, null, lotacaoDest.name(), null, null, null);
    }

    @Test
    public void test_NotificarLotacao_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);

    }

    @Test
    public void test_NotificarLotacaoEArquivar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

//        notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);
        notificar(Pessoa.ZZ99999, sigla, Pessoa.ZZ99998);
        notificar(Pessoa.ZZ99999, sigla, Pessoa.ZZ99997);

        ArquivarCorrente.arquivar(Pessoa.ZZ99999, sigla);

        consultar(Pessoa.ZZ99999, sigla);
//        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Pessoa.ZZ99998);
        contemAcao("arquivar_corrente_gravar", false);
        contemAcao("reabrir_gravar", true);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
//        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");
        contemVizNode("vizTramitacao", Pessoa.ZZ99998.name(), "rectangle", "red");

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);

        consultar(Pessoa.ZZ99997, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);

        Receber.receber(Pessoa.ZZ99998, sigla);
        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", true);

        Receber.receber(Pessoa.ZZ99997, sigla);
        consultar(Pessoa.ZZ99997, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", true);

    }

    @Test
    public void test_TramitarDevolverNotificarArquivarReceber_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST3);
        Receber.receber(Pessoa.ZZ99997, sigla);
        Tramitar.tramitarParaLotacao(Pessoa.ZZ99997, sigla, Lotacao.ZZLTEST);
        Receber.receber(Pessoa.ZZ99999, sigla);

        notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        ArquivarCorrente.arquivar(Pessoa.ZZ99999, sigla);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemAcao("arquivar_corrente_gravar", false);
        contemAcao("reabrir_gravar", true);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);

        Receber.receber(Pessoa.ZZ99998, sigla);
        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", true);
        
        Concluir.concluir(Pessoa.ZZ99998, sigla);
        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
    }

    @Test
    public void test_NotificarJuntarReceberConcluir_OK() {
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        Juntar.juntar(Pessoa.ZZ99999, sigla, siglaPai);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemAcao("arquivar_corrente_gravar", false);
        contemAcao("reabrir_gravar", false);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);

        Receber.receber(Pessoa.ZZ99998, sigla);
        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", true);
        
        Concluir.concluir(Pessoa.ZZ99998, sigla);
        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        
    }

}
