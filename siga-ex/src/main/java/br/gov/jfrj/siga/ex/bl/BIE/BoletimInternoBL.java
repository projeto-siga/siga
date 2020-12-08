package br.gov.jfrj.siga.ex.bl.BIE;

import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;
import br.gov.jfrj.siga.ex.util.BIE.ManipuladorEntrevista;
import br.gov.jfrj.siga.hibernate.ExDao;

public class BoletimInternoBL {
	
	//Edson: aqui ficam todos os métodos de negócio que manipulam objetos ExBoletimDoc
	
	public void gravarBIE(ExDocumento docBIE) throws Exception {
		ManipuladorEntrevista meBIE = new ManipuladorEntrevista(docBIE);
		final List<ExDocumento> documentosPublicar = meBIE.obterDocsMarcados();
		final List<ExDocumento> documentosNaoPublicar = meBIE.obterDocsNaoMarcados();

		ExBoletimDoc boletim;
		ExDao dao = ExDao.getInstance();

		for (ExDocumento docPubl : documentosPublicar) {

			if (docPubl
					.getMobilGeral()
					.getMovimentacoesPorTipo(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI, false)
					.size() > 0)

				throw new AplicacaoException(
						"O documento "
								+ docPubl.getCodigo()
								+ " já foi publicado em outro boletim. Retire esse documento da lista de documentos a publicar e entre em contato com a equipe de suporte do siga-doc.");

			boletim = dao.consultarBoletimEmQueODocumentoEstaIncluso(docPubl);
			boletim.setBoletim(docBIE);
			dao.gravar(boletim);
		}

		for (ExDocumento docPubl : documentosNaoPublicar) {
			boletim = dao.consultarBoletimEmQueODocumentoEstaIncluso(docPubl);
			if (boletim.getBoletim() != null
					&& boletim.getBoletim().getIdDoc() == docBIE.getIdDoc())
				boletim.setBoletim(null);
			dao.gravar(boletim);
		}
	}

	public void excluirBIE(ExDocumento docBIE) throws Exception {
		ExDao dao = ExDao.getInstance();

		final List<ExBoletimDoc> documentosDoBoletim = dao
				.consultarBoletim(docBIE);
		for (ExBoletimDoc docBol : documentosDoBoletim) {
			docBol.setBoletim(null);
			dao.gravar(docBol);
		}
	}

	public void finalizarBIE(ExDocumento docBIE) throws Exception {
		final List<ExDocumento> documentosPublicar = new ManipuladorEntrevista(docBIE).obterDocsMarcados();
		ExDao dao = ExDao.getInstance();

		for (ExDocumento exDoc : documentosPublicar) {
			ExBoletimDoc boletim = dao.consultarBoletimEmQueODocumentoEstaIncluso(exDoc);
			if (boletim == null) {
				throw new AplicacaoException(
						"Foi cancelada a solicitação de pedido de publicação do documento "
								+ exDoc.getCodigo()
								+ ". Por favor queira retorna é tela de edição de documento para uma nova conferência.");
			} else if (boletim.getBoletim() == null) {
				throw new AplicacaoException(
						"O documento "
								+ exDoc.getCodigo()
								+ " foi retirado da lista de documentos deste boletim"
								+ ". Por favor queira retorna é tela de edição de documento para uma nova conferência.");
			} else if (boletim.getBoletim() != docBIE) {
				throw new AplicacaoException(
						"O documento "
								+ exDoc.getCodigo()
								+ " já consta da lista de documentos do boletim "
								+ boletim.getBoletim().getCodigo()
								+ ". Por favor queira retorna é tela de edição de documento para uma nova conferência.");

			}
		}
	}
	
	public void deixarDocDisponivelParaInclusaoEmBoletim(ExDocumento doc){
		ExBoletimDoc boletim = new ExBoletimDoc();
		boletim.setExDocumento(doc);
		ExDao.getInstance().gravar(boletim);
	}
	
	public void deixarDocIndisponivelParaInclusaoEmBoletim(ExDocumento doc) throws Exception {
		ExBoletimDoc boletim;
		ExDao dao = ExDao.getInstance();
		boletim = dao.consultarBoletimEmQueODocumentoEstaIncluso(doc);
		dao.excluir(boletim);
	}
	
}
