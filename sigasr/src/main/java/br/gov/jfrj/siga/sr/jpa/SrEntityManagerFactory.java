package br.gov.jfrj.siga.sr.jpa;

import javax.enterprise.inject.Specializes;

import br.gov.jfrj.siga.jpa.SigaEntityManagerFactory;
import br.gov.jfrj.siga.sr.model.DadosRH;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrArquivo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoCache;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoIgnorada;
import br.gov.jfrj.siga.sr.model.SrDisponibilidade;
import br.gov.jfrj.siga.sr.model.SrEquipe;
import br.gov.jfrj.siga.sr.model.SrExcecaoHorario;
import br.gov.jfrj.siga.sr.model.SrFatorMultiplicacao;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMarca;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.sr.model.SrParametroAcordo;
import br.gov.jfrj.siga.sr.model.SrPergunta;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrPrioridadeSolicitacao;
import br.gov.jfrj.siga.sr.model.SrResposta;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoAcao;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.model.SrTipoPergunta;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;

@Specializes
public class SrEntityManagerFactory extends SigaEntityManagerFactory {

    public void addClasses() {
        super.addClasses();

        addClass(DadosRH.class);
        addClass(SrAcao.class);
        addClass(SrAcordo.class);
        addClass(SrArquivo.class);
        addClass(SrAtributo.class);
        addClass(SrParametroAcordo.class);
        addClass(SrAtributoSolicitacao.class);
        addClass(SrConfiguracao.class);
        addClass(SrConfiguracaoCache.class);
        addClass(SrConfiguracaoIgnorada.class);
        addClass(SrDisponibilidade.class);
        addClass(SrEquipe.class);
        addClass(SrExcecaoHorario.class);
        addClass(SrFatorMultiplicacao.class);
        addClass(SrGestorItem.class);
        addClass(SrItemConfiguracao.class);
        addClass(SrLista.class);
        addClass(SrMarca.class);
        addClass(SrMovimentacao.class);
        addClass(SrObjetivoAtributo.class);
        addClass(SrPergunta.class);
        addClass(SrPesquisa.class);
        addClass(SrPrioridadeSolicitacao.class);
        addClass(SrResposta.class);
        addClass(SrSolicitacao.class);
        addClass(SrTipoAcao.class);
        addClass(SrTipoMovimentacao.class);
        addClass(SrTipoPergunta.class);
        addClass(SrTipoPermissaoLista.class);
    }
}