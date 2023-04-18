package br.gov.jfrj.siga.wf.jpa;

import javax.enterprise.inject.Specializes;

import br.gov.jfrj.siga.jpa.SigaEntityManagerFactory;
import br.gov.jfrj.siga.wf.model.WfConfiguracao;
import br.gov.jfrj.siga.wf.model.WfConfiguracaoCache;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeResponsavel;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfMarca;
import br.gov.jfrj.siga.wf.model.WfMov;
import br.gov.jfrj.siga.wf.model.WfMovAnotacao;
import br.gov.jfrj.siga.wf.model.WfMovCancelamento;
import br.gov.jfrj.siga.wf.model.WfMovDesignacao;
import br.gov.jfrj.siga.wf.model.WfMovPriorizacao;
import br.gov.jfrj.siga.wf.model.WfMovRedirecionamento;
import br.gov.jfrj.siga.wf.model.WfMovTermino;
import br.gov.jfrj.siga.wf.model.WfMovTransicao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.WfResponsavel;
import br.gov.jfrj.siga.wf.model.WfVariavel;

@Specializes
public class WfEntityManagerFactory extends SigaEntityManagerFactory {

    public void addClasses() {
        super.addClasses();

        addClass(WfDefinicaoDeDesvio.class);
        addClass(WfDefinicaoDeProcedimento.class);
        addClass(WfDefinicaoDeResponsavel.class);
        addClass(WfDefinicaoDeTarefa.class);
        addClass(WfDefinicaoDeVariavel.class);
        addClass(WfMov.class);
        addClass(WfMovAnotacao.class);
        addClass(WfMovCancelamento.class);
        addClass(WfMovDesignacao.class);
        addClass(WfMovPriorizacao.class);
        addClass(WfMovRedirecionamento.class);
        addClass(WfMovTermino.class);
        addClass(WfMovTransicao.class);
        addClass(WfProcedimento.class);
        addClass(WfResponsavel.class);
        addClass(WfVariavel.class);
        addClass(WfConfiguracao.class);
        addClass(WfConfiguracaoCache.class);
        addClass(WfMarca.class);
    }
}