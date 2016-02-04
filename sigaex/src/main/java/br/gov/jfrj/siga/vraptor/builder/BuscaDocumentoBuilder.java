package br.gov.jfrj.siga.vraptor.builder;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.ExMobilSelecao;

public final class BuscaDocumentoBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(BuscaDocumentoBuilder.class);
	
	private Long id;
	private String sigla;
	private ExMobilSelecao documentoViaSel;
	private Long idMob;
	private ExMobil mob; 
	private ExMovimentacao mov;
	
	private BuscaDocumentoBuilder() {
	}
	
	public static BuscaDocumentoBuilder novaInstancia() {
		return new BuscaDocumentoBuilder();
	}
	
	public ExDocumento buscarDocumento(final ExDao dao) {		
		ExDocumento doc = null;

		if (id != null) {
			mov = dao.consultar(id, ExMovimentacao.class, false);
			mob = getMov(mov);
		}

		if (mob == null && sigla != null) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			mob = (ExMobil) dao.consultarPorSigla(filter);
			doc = mob.getExDocumento();
		} else if (mob == null && documentoViaSel != null && documentoViaSel.getId() != null) {
			mob = dao.consultar(documentoViaSel.getId(), ExMobil.class, false);
		} else if (mob == null && idMob != null && idMob != 0) {
			mob = dao.consultar(idMob, ExMobil.class, false);
		}

		if (mob != null) {
			doc = mob.doc();
		}
		if (doc != null && mob == null) {
			mob = doc.getMobilGeral();
		}
		
		if (doc == null) {
			throw new AplicacaoException("Documento não informado");
		}

		return doc;
	}
	
	private ExMobil getMov(final ExMovimentacao movimentacao) throws AplicacaoException {
		ExMobil mobil = null;
		if (movimentacao != null) {
			try {
				mobil = movimentacao.getExMobil();
			} catch (Exception e) {
				LOGGER.warn("[getMov] - Não foi possível recuperar o mobil da movimentação");
				throw new AplicacaoException("Ocorreu um erro ao recuperar o mobil da movimentação.", 0, e);
			}
		}

		return mobil;
	}

	public BuscaDocumentoBuilder setId(final Long id) {
		this.id = id;
		return this;
	}

	public BuscaDocumentoBuilder setSigla(final String sigla) {
		this.sigla = sigla;
		return this;
	}

	public BuscaDocumentoBuilder setDocumentoViaSel(final ExMobilSelecao documentoViaSel) {
		this.documentoViaSel = documentoViaSel;
		return this;
	}

	public BuscaDocumentoBuilder setIdMob(final Long idMob) {
		this.idMob = idMob;
		return this;
	}

	public BuscaDocumentoBuilder setMob(final ExMobil mob) {
		this.mob = mob;
		return this;
	}

	public Long getId() {
		return id;
	}

	public String getSigla() {
		return sigla;
	}

	public ExMobilSelecao getDocumentoViaSel() {
		return documentoViaSel;
	}

	public Long getIdMob() {
		return idMob;
	}

	public ExMobil getMob() {
		return mob;
	}

	public ExMovimentacao getMov() {
		return mov;
	}
}
