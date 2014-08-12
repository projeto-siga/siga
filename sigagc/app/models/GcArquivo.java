package models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.io.IOUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import play.db.jpa.GenericModel;
import utils.ProcessadorHtml;

@Entity
@Table(name = "GC_ARQUIVO", schema = "SIGAGC")
public class GcArquivo extends GenericModel implements Serializable{
	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.hibernate_sequence", name = "gcArquivoSeq")
	@GeneratedValue(generator = "gcArquivoSeq")
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

	public void setConteudoTXT(String html) {
		if (html != null && html.startsWith("<")) {
			mimeType = "text/html";
		} else {
			mimeType = "text/plain";
		}
		conteudo = html.getBytes(Charset.forName("utf-8"));
	}

	public String getConteudoTXT() throws IOException {
		return new String(conteudo, Charset.forName("utf-8"));
	}

	public void setConteudoBinario(byte[] conteudo, String mimeType) {
		this.conteudo = conteudo;
		this.mimeType = mimeType;
	}

	public boolean isImage() {
		return mimeType != null && mimeType.startsWith("image/");
	}

	public String getIcon() {
		if (mimeType != null) {
			if (mimeType.startsWith("image/"))
				return "image";
			if (mimeType.startsWith("application/pdf"))
				return "page_white_acrobat";
			if (mimeType.startsWith("application/msword") || mimeType.endsWith(".wordprocessingml.document"))
				return "page_white_word";	
			if (mimeType.startsWith("application/vnd.ms-excel") || mimeType.endsWith(".spreadsheetml.sheet"))
				return "page_white_excel";	
			if (mimeType.startsWith("application/vnd.ms-powerpoint") || mimeType.endsWith(".presentationml.presentation"))
				return "page_white_powerpoint";	
		}
		return "page_white_text";
	}
	
	/**
	 * Duplica o conteúdo de um conhecimento através de serialização.
	 * Uma das formas de se fazer deep copying do conteúdo, assim quando alterar
	 * a cópia não modifica o original 
	 */
	public GcArquivo duplicarConteudoInfo() {
		try {
			ByteArrayOutputStream saida = new ByteArrayOutputStream();
			ObjectOutputStream objSaida = new ObjectOutputStream(saida);
			objSaida.writeObject(this);
			objSaida.close();
			
			ByteArrayInputStream entrada = new ByteArrayInputStream(saida.toByteArray());
			ObjectInputStream objEntrada = new ObjectInputStream(entrada);
			
			GcArquivo cloneConteudoInfo = (GcArquivo) objEntrada.readObject();
			return cloneConteudoInfo;
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível duplicar esse conhecimento.");
		}
	}	
}
