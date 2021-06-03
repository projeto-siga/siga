package br.gov.jfrj.siga.armazenamento.zip;

import static org.apache.commons.io.FileUtils.getUserDirectoryPath;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.prop.ext.ModeloPropriedade;

public class ZipPropriedades extends ModeloPropriedade {

	private static final Log log = LogFactory.getLog(ZipPropriedades.class);
	private static final ZipPropriedades INSTANCE = new ZipPropriedades();
	private static final String ERRO_CAPTURA_CAMINHO_BASE = "Não foi possível obter caminho base para armazenamento a partir da propriedade ";
	private static final String MODULO = "sigaex";
	private static final String USER_HOME_MODULE_SUBDIR = "." + MODULO;
	private static final String ARMAZENAMENTO_PROPRIEDADE = "diretorio.armazenamento.arquivos";

	private Path base;

	private ZipPropriedades() {}

	public static final ZipPropriedades getInstance() {
		return INSTANCE;
	}

	public Path obterCaminhoBase() {
		if (this.base != null) {
			return this.base;
		}

		try {
			final String propertyDiretorioBaseArmazenamento = this.obterPropriedade(ARMAZENAMENTO_PROPRIEDADE);
			this.base = isEmpty(propertyDiretorioBaseArmazenamento) ? this.obterCaminhoBasePadrao() : Paths.get(propertyDiretorioBaseArmazenamento);;
			FileUtils.forceMkdir(this.base.toFile());

			log.debug("Caminho base para armazenamento de arquivos: " + propertyDiretorioBaseArmazenamento);
			return this.base;
		} catch (Exception e) {
			throw new AplicacaoException(ERRO_CAPTURA_CAMINHO_BASE + getPrefixoModulo() + ARMAZENAMENTO_PROPRIEDADE, 0, e);
		}
	}

	@Override
	public String getPrefixoModulo() {
		return MODULO;
	}

	private Path obterCaminhoBasePadrao() {
		return Paths.get(getUserDirectoryPath(), USER_HOME_MODULE_SUBDIR);
	}

}
