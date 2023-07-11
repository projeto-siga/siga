package br.gov.jfrj.siga.ex.api.v1.module;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.api.v1.unit.ArquivarCorrente;
import br.gov.jfrj.siga.ex.api.v1.unit.Criar;
import br.gov.jfrj.siga.ex.api.v1.unit.Receber;
import br.gov.jfrj.siga.ex.api.v1.unit.Tramitar;

public class TramitarReceberEArquivar extends DocTest {

    @Test
    public void test_TramitarReceberEArquivar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        
        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);
        
        Receber.receber(Pessoa.ZZ99998, sigla);
        
        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", true);

        ArquivarCorrente.arquivar(Pessoa.ZZ99998, sigla);
        
        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.ARQUIVADO_CORRENTE, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);
        contemAcao("reabrir_gravar", true);
    }

}
