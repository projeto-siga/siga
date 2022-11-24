package br.gov.jfrj.siga.ex.api.v1.module;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.api.v1.unit.Criar;
import br.gov.jfrj.siga.ex.api.v1.unit.Receber;
import br.gov.jfrj.siga.ex.api.v1.unit.Tramitar;

public class TramitarReceberEArquivar extends DocTest {

    @Test
    public void test_TramitarReceberEArquivar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);
        Receber.receber(Pessoa.ZZ99998, sigla);

        // TODO arquivar
    }

}
