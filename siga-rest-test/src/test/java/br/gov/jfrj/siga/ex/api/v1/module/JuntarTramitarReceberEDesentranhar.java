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
    public void test_JuntarTramitarReceberEDesentranhar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);

        Juntar.juntar(Pessoa.ZZ99999, sigla, siglaPai);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, siglaPai, Lotacao.ZZLTEST2);

        Receber.receber(Pessoa.ZZ99998, siglaPai);

        Desentranhar.desentranhar(Pessoa.ZZ99998, sigla, "Juntado por engano.");
        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
    }

}
