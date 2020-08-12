package br.gov.jfrj.siga.cp.arquivo;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpArquivo;

public class ArmazenamentoBDTabela implements ArmazenamentoBCInterface {

	private final static Logger log = Logger.getLogger(ArmazenamentoBDTabela.class);
	
	private static final String ERRO_RECUPERAR_ARQUIVO = "Erro ao recuperar o arquivo";
	private static final String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar o arquivo";
	private static final String ERRO_EXCLUIR_ARQUIVO = "Erro ao excluir o arquivo";
	
	@Override
	public void salvar(CpArquivo cpArquivo, byte[] conteudo) {
		try {
			cpArquivo.setTamanho(conteudo.length);
			cpArquivo.setConteudoBlobArq(conteudo);
		} catch (Exception e) {
			log.error(ERRO_GRAVAR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_EXCLUIR_ARQUIVO);
		}
	}

	@Override
	public byte[] recuperar(CpArquivo cpArquivo) {
		try {
			return cpArquivo.getConteudoBlobArq();
		} catch (Exception e) {
			log.error(ERRO_RECUPERAR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_RECUPERAR_ARQUIVO);
		}
	}

	@Override
	public void apagar(CpArquivo cpArquivo) {
		try {
			cpArquivo.setConteudoBlobArq(null);
		} catch (Exception e) {
			log.error(ERRO_EXCLUIR_ARQUIVO, cpArquivo.getIdArq(), e);
			throw new AplicacaoException(ERRO_EXCLUIR_ARQUIVO);
		}
	}

}
