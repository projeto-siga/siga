package br.gov.jfrj.siga.ex;

import static java.util.Optional.ofNullable;

import java.nio.file.Path;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import br.gov.jfrj.siga.armazenamento.zip.ZipItem;
import br.gov.jfrj.siga.armazenamento.zip.ZipServico;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;

public interface ExArquivoFilesystem {

	static final String YEAR_PATTERN = "yyyy";
	static final String EXTENSAO_ZIP = ".zip";
	public static final String ZIP_MIME_TYPE = "application/zip";
	static final String ERRO_CAMINHO_ARQUIVO = "Erro ao montar caminho para o arquivo \"%s\" de ID=%d: campo \"%s\" não pôde ser convertido em caminho";

	Long getId();

	Date getData();

	DpLotacao getLotaTitular();

	Path getPathConteudo(Path base);

	default Path getPathConteudo(AbstractExDocumento documento, String tipoNome, Path base) {

		final String acronimoOrgao = ofNullable(this.getLotaTitular())
				.map(DpLotacao::getOrgaoUsuario)
				.map(CpOrgaoUsuario::getAcronimoOrgaoUsu)
				.map(StringUtils::stripToNull)
				.orElseThrow(() -> new IllegalArgumentException(String.format(ERRO_CAMINHO_ARQUIVO, tipoNome, this.getId(), "ÓRGAO")));

		final String siglaForma = ofNullable(documento)
				.map(AbstractExDocumento::getExFormaDocumento)
				.map(ExFormaDocumento::getSiglaFormaDoc)
				.orElseThrow(() -> new IllegalArgumentException(String.format(ERRO_CAMINHO_ARQUIVO, tipoNome, this.getId(), "FORMA DOCUMENTO")));

		final String ano = ofNullable(this.getData())
				.map(dataHora -> DateFormatUtils.format(dataHora, YEAR_PATTERN))
				.orElseThrow(() -> new IllegalArgumentException(String.format(ERRO_CAMINHO_ARQUIVO, tipoNome, this.getId(), "DATA/HORA")));

		final Long id = ofNullable(this.getId())
				.orElseThrow(() -> new IllegalArgumentException(String.format(ERRO_CAMINHO_ARQUIVO, tipoNome, this.getId(), "ID")));

		return base.resolve(tipoNome)
				.resolve(acronimoOrgao)
				.resolve(siglaForma)
				.resolve(ano)
				.resolve(id + EXTENSAO_ZIP);
	}

	default Map<ZipItem, byte[]> atualizarCache(byte[] zip) {
		final Map<ZipItem, byte[]> cache = new LinkedHashMap<>();
		final List<ZipItem> itens = ZipServico.listarItens(zip);
		for (ZipItem item : itens) {
			byte[] itemBytes = ZipServico.lerItem(zip, item);
			if (itemBytes != null) {
				cache.put(item, itemBytes);
			}
		}

		// Liberando cache antiga
		if (this.getCacheConteudo() != null) {
			this.getCacheConteudo().clear();
		}
		this.setCacheConteudo(cache);
		return this.getCacheConteudo();
	}

	default void inicializarCacheSeNecessario() {
		if (this.getCacheConteudo() == null) {
			this.setCacheConteudo(new LinkedHashMap<>());
		}
	}

	default byte[] getConteudoBlobInicializarOuAtualizarCache() {
		this.inicializarCacheSeNecessario();
		if (this.getId() == null) {
			return null;
		}

		byte[] zip = ZipServico.ler(this);
		if (zip == null) {
			return null;
		}

		this.atualizarCache(zip);
		return zip;
	}

	default byte[] getConteudoBlob(final ZipItem zipItem) {
		if (this.getCacheConteudo() == null) {
			this.getConteudoBlobInicializarOuAtualizarCache();
		}
		// Retornando item a partir da cache
		return this.getCacheConteudo().get(zipItem);
	}

	default void setConteudoBlob(final ZipItem zipItem, final byte[] conteudo) {
		if (zipItem != null && conteudo != null) {
			this.setMimeType(ZIP_MIME_TYPE);
			if (this.getCacheConteudo() == null) {
				this.getConteudoBlobInicializarOuAtualizarCache();
			}
			this.getCacheConteudo().put(zipItem, conteudo);
		}
	}

	default void clonarConteudo(ExArquivo origem) {
		origem.getConteudoBlobInicializarOuAtualizarCache();
		this.inicializarCacheSeNecessario();
		this.getCacheConteudo().putAll(origem.getCacheConteudo());
	}

	void setMimeType(String mimeType);

	Map<ZipItem, byte[]> getCacheConteudo();

	void setCacheConteudo(Map<ZipItem, byte[]> cacheConteudo);

}
