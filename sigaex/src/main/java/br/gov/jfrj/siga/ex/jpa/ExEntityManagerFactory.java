package br.gov.jfrj.siga.ex.jpa;

import javax.enterprise.inject.Specializes;

import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExDocumentoNumeracao;
import br.gov.jfrj.siga.ex.ExEmailNotificacao;
import br.gov.jfrj.siga.ex.ExEstadoDoc;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.ExSequencia;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoSequencia;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;
import br.gov.jfrj.siga.jpa.SigaEntityManagerFactory;

@Specializes
public class ExEntityManagerFactory extends SigaEntityManagerFactory {

    public void addProperties() {
        super.addProperties();

        // Cache Region CACHE_EX
        addCache("ex", "LRU", 2500, 10000, 300000, 300000);
    }

    public void addClasses() {
        super.addClasses();

        addClass(ExTipoDocumento.class);
        addClass(ExFormaDocumento.class);
        addClass(ExClassificacao.class);
        addClass(ExTemporalidade.class);
        addClass(ExTipoDestinacao.class);
        addClass(ExTipoFormaDoc.class);
        addClass(ExVia.class);
        addClass(ExNivelAcesso.class);
        addClass(ExEstadoDoc.class);
        addClass(ExTipoDespacho.class);
        addClass(ExEmailNotificacao.class);
        addClass(ExPreenchimento.class);
        addClass(ExTpDocPublicacao.class);
        addClass(ExPapel.class);
        addClass(ExBoletimDoc.class);
        addClass(ExModelo.class);
        addClass(ExMovimentacao.class);
        addClass(ExDocumento.class);
        addClass(ExMobil.class);
        addClass(ExTipoMobil.class);
        addClass(ExMarca.class);
        addClass(ExConfiguracao.class);
        addClass(ExConfiguracaoCache.class);
        addClass(ExDocumentoNumeracao.class);
        addClass(ExSequencia.class);
        addClass(ExTipoSequencia.class);
        addClass(ExProtocolo.class);
    }
}