package br.gov.jfrj.siga.ex.api.v1.module;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.api.v1.unit.Criar;
import br.gov.jfrj.siga.ex.api.v1.unit.Desentranhar;
import br.gov.jfrj.siga.ex.api.v1.unit.Juntar;
import br.gov.jfrj.siga.ex.api.v1.unit.Receber;
import br.gov.jfrj.siga.ex.api.v1.unit.Tramitar;

public class JuntarTramitarReceberEDesentranhar extends DocTest {

    @Test
    public void test_JuntarTramitarOPaiReceberEDesentranhar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);

        Juntar.juntar(Pessoa.ZZ99999, sigla, siglaPai);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, siglaPai, Lotacao.ZZLTEST2);

        Receber.receber(Pessoa.ZZ99998, siglaPai);

        Desentranhar.desentranhar(Pessoa.ZZ99998, sigla, "Juntado por engano.");
        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        naoContemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99999, Lotacao.ZZLTEST);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99998, sigla, Lotacao.ZZLTEST);

    }

    @Test
    public void test_TramitarReceberJuntarTramitarOPaiReceberEDesentranhar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);
        Receber.receber(Pessoa.ZZ99998, sigla);

        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99998);
        Juntar.juntar(Pessoa.ZZ99998, sigla, siglaPai);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99998, siglaPai, Lotacao.ZZLTEST);

        Receber.receber(Pessoa.ZZ99999, siglaPai);

        Desentranhar.desentranhar(Pessoa.ZZ99999, sigla, "Juntado por engano.");
        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        naoContemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        contemAcao("concluir_gravar", false);
    }

}
