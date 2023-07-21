package br.gov.jfrj.siga.ex.api.v1.module;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.api.v1.unit.ArquivarCorrente;
import br.gov.jfrj.siga.ex.api.v1.unit.Concluir;
import br.gov.jfrj.siga.ex.api.v1.unit.Criar;
import br.gov.jfrj.siga.ex.api.v1.unit.Notificar;
import br.gov.jfrj.siga.ex.api.v1.unit.Receber;

public class NotificarPessoaDaMesmaLotacaoReceberConcluirEArquivar extends DocTest {

    @Test
    public void test_NotificarLotacao_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        Notificar.notificar(Pessoa.ZZ99999, sigla, Pessoa.ZZ99994);

        Receber.receber(Pessoa.ZZ99994, sigla);

        consultar(Pessoa.ZZ99994, sigla);
        contemMarca(CpMarcadorEnum.AGUARDANDO_CONCLUSAO, Pessoa.ZZ99994, Lotacao.ZZLTEST);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", true);
        contemAcao("arquivar_corrente_gravar", true);

        Concluir.concluir(Pessoa.ZZ99994, sigla);

        consultar(Pessoa.ZZ99994, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", true);
        
        ArquivarCorrente.arquivar(Pessoa.ZZ99994, sigla);
        consultar(Pessoa.ZZ99994, sigla);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);
    }

}
