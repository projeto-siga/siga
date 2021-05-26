package br.gov.jfrj.siga.armazenamento.zip;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.prop.ext.ModeloPropriedade;

public class ZipPropriedades extends ModeloPropriedade {

	private static final Log log = LogFactory.getLog(ZipPropriedades.class);
	private static final ZipPropriedades INSTANCE = new ZipPropriedades();
	private static final String ERRO_CAPTURA_CAMINHO_BASE = "Não foi possível obter caminho base para armazenamento a partir da propriedade ";

	private static final String MODULO = "pbdoc";
	private static final String ARMAZENAMENTO_PROPRIEDADE = "documento.armazenamento";

	private ZipPropriedades() {}

	public static final ZipPropriedades getInstance() {
		return INSTANCE;
	}

	public Path obterCaminhoBase() {
		try {
			String base = this.obterPropriedade(ARMAZENAMENTO_PROPRIEDADE);
			log.debug("Caminho base para armazenamento de arquivos: " + base);
			return Paths.get(base);
		} catch (Exception e) {
			throw new AplicacaoException(ERRO_CAPTURA_CAMINHO_BASE + getPrefixoModulo() + ARMAZENAMENTO_PROPRIEDADE, 0, e);
		}
	}

	@Override
	public String getPrefixoModulo() {
		return MODULO;
	}

}
