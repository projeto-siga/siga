package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.io.IOUtils;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_ARQUIVO")
public class SrArquivo extends GenericModel {

	@Id
	@GeneratedValue
	@Column(name = "ID_ARQUIVO")
	public long idArquivo;

	@Lob
	public byte[] blob;

	@Column(name = "MIME")
	public String mime;

	@Column(name = "NOME_ARQUIVO")
	public String nomeArquivo;

	@Column(name = "DESCRICAO")
	public String descricao;

	// Necessário porque não há binder para arquivo
	public void setFile(File file) {
		if (file != null)

			try {

				nomeArquivo = file.getName();
				blob = IOUtils.toByteArray(new FileInputStream(file));
				mime = new javax.activation.MimetypesFileTypeMap()
						.getContentType(file);
			} catch (IOException ioe) {
				//Ver o que fazer aqui
			}
	}

}
