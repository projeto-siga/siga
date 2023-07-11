package br.gov.jfrj.siga.ex.api.v1.module;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.api.v1.unit.Concluir;
import br.gov.jfrj.siga.ex.api.v1.unit.Criar;
import br.gov.jfrj.siga.ex.api.v1.unit.Receber;
import br.gov.jfrj.siga.ex.api.v1.unit.TramitarEmParalelo;

public class TramitarEmParaleloReceberEConcluir extends DocTest {

    @Test
    public void test_TramitarEmParaleloParaLotacao_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        TramitarEmParalelo.tramitarEmParalelo(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");

        consultar(Pessoa.ZZ99999, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);

        Receber.receber(Pessoa.ZZ99998, sigla);

        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", true);
        contemAcao("arquivar_corrente_gravar", false);

        Concluir.concluir(Pessoa.ZZ99998, sigla);

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", null);
    }

}
