package br.gov.jfrj.siga.sr.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.io.IOUtils;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.base.util.Catalogs;

@Entity
@Table(name = "SR_ARQUIVO", schema = Catalogs.SIGASR)
public class SrArquivo extends GenericModel {

	@Id
	@SequenceGenerator(sequenceName = Catalogs.SIGASR +".SR_ARQUIVO_SEQ", name = "srArquivoSeq")
	@GeneratedValue(generator = "srArquivoSeq")
	@Column(name = "ID_ARQUIVO")
	public Long idArquivo;

	@Lob
	public byte[] blob;

	@Column(name = "MIME")
	public String mime;

	@Column(name = "NOME_ARQUIVO")
	public String nomeArquivo;

	@Column(name = "DESCRICAO")
	public String descricao;

	private SrArquivo() {

	}

	private SrArquivo(File file) {
		try {

			nomeArquivo = file.getName();
			blob = IOUtils.toByteArray(new FileInputStream(file));
			mime = new javax.activation.MimetypesFileTypeMap()
					.getContentType(file);
		} catch (IOException ioe) {
			// Ver o que fazer aqui
		}
	}

	// Edson: Necessário porque é preciso garantir
	// que o SrArquivo não seja instanciado a não ser que realmente tenha sido
	// selecionado um arquivo no form (para que não surja um registro SrArquivo
	// sem conteúdo no banco)
	public static SrArquivo newInstance(File file) {
		if (file != null)
			return new SrArquivo(file);
		else
			return null;
	}

}
