package br.gov.jfrj.siga.armazenamento.zip;

import static org.apache.commons.io.FileUtils.getUserDirectoryPath;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.base.AplicacaoException;

public class ZipPropriedades {

	private static final Log log = LogFactory.getLog(ZipPropriedades.class);
	private static final ZipPropriedades INSTANCE = new ZipPropriedades();
	private static final String USER_HOME_MODULE_SUBDIR = ".sigaex";
	private static final String ARMAZENAMENTO_PROPRIEDADE = "sigaex.diretorio.armazenamento.arquivos";

	private Path diretorioBase;

	private ZipPropriedades() {}

	public static final ZipPropriedades getInstance() {
		return INSTANCE;
	}

	public Path obterCaminhoBase() {
		if (this.diretorioBase != null) {
			return this.diretorioBase;
		}

		try {
			final String propertyDiretorioBaseArmazenamento = System.getProperty(ARMAZENAMENTO_PROPRIEDADE);
			this.diretorioBase = isEmpty(propertyDiretorioBaseArmazenamento)
					? this.obterCaminhoBasePadrao()
					: Paths.get(propertyDiretorioBaseArmazenamento);

			FileUtils.forceMkdir(this.diretorioBase.toFile());
			log.debug("Caminho base para armazenamento de arquivos: " + propertyDiretorioBaseArmazenamento);
			return this.diretorioBase;
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível obter caminho base para armazenamento a partir da propriedade " + ARMAZENAMENTO_PROPRIEDADE, 0, e);
		}
	}

	private Path obterCaminhoBasePadrao() {
		return Paths.get(getUserDirectoryPath(), USER_HOME_MODULE_SUBDIR);
	}

}
