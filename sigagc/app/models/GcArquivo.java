package models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.io.IOUtils;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "GC_ARQUIVO") 
public class GcArquivo extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_CONTEUDO")
	public long id;

	@Column(name = "TITULO")
	public String titulo;

	@Column(name = "CLASSIFICACAO")
	public String classificacao;

	@Lob
	@Column(name = "CONTEUDO")
	public byte[] conteudo;

	@Column(name = "CONTEUDO_TIPO")
	public String mimeType;

	public void setConteudoHTML(String html) {
		conteudo = html.getBytes(Charset.forName("utf-8"));
		mimeType = "text/html";
	}

	public String getConteudoHTML() throws IOException {
		return new String(conteudo, Charset.forName("utf-8"));
	}
}
