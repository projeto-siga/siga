package br.gov.jfrj.siga.gc.jpa;

import javax.enterprise.inject.Specializes;

import br.gov.jfrj.siga.gc.model.GcAcesso;
import br.gov.jfrj.siga.gc.model.GcArquivo;
import br.gov.jfrj.siga.gc.model.GcConfiguracao;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcMarca;
import br.gov.jfrj.siga.gc.model.GcMovimentacao;
import br.gov.jfrj.siga.gc.model.GcPapel;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoInformacao;
import br.gov.jfrj.siga.gc.model.GcTipoMovimentacao;
import br.gov.jfrj.siga.gc.model.GcTipoTag;
import br.gov.jfrj.siga.jpa.SigaEntityManagerFactory;

@Specializes
public class GcEntityManagerFactory extends SigaEntityManagerFactory {

    public void addClasses() {
        super.addClasses();

        addClass(GcAcesso.class);
        addClass(GcArquivo.class);
        addClass(GcConfiguracao.class);
        addClass(GcInformacao.class);
        addClass(GcMarca.class);
        addClass(GcMovimentacao.class);
        addClass(GcTag.class);
        addClass(GcTipoInformacao.class);
        addClass(GcTipoMovimentacao.class);
        addClass(GcTipoTag.class);
        addClass(GcPapel.class);
    }
}