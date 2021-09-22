package br.gov.jfrj.siga.tp.model;

import java.io.File;
import java.util.Calendar;

import br.gov.jfrj.siga.tp.util.SigaProperties;


//@Entity
//@Audited
//@Table(schema = "SIGATP")
public class Imagem /*extends TpModel implements Comparable<Condutor>*/ {

//	@Id
//	@GeneratedValue
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence_generator") @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName="sigatp.hibernate_sequence") 
	public Long id;
	
//	@NotNull
	public Calendar dataHora;
	
//	@NotNull
	public byte[] blob;
	
//	@NotNull
	public String nomeArquivo;

//	@NotNull
	public String mime;
	
	public Imagem() {
		this.id = new Long(0);
	}
	
	public Imagem(File file, Calendar dataHora) {
//		try {
//			//this.id = id;
//			this.dataHora = dataHora;
//			this.blob = IOUtils.toByteArray(new FileInputStream(file));
//			this.nomeArquivo = file.getName();
//			this.mime = new javax.activation.MimetypesFileTypeMap().getContentType(file);
//		} catch (IOException ioe) {
//		}
	}
	
	/* Garante que a Imagem nao seja instanciada sem que tenha sido
	   selecionado um arquivo no form, para evitar um registro sem conteudo no banco */
	public static Imagem newInstance(File file) {
		if (file != null) {
			return new Imagem(file, Calendar.getInstance());
		}
		else {
			return null;
		}
	}
	
	public static boolean tamanhoImagemAceito(int tamanho) {
		int valorMaxMBConfigurado = Integer.parseInt(SigaProperties.getValue("imagem.filesize"));
		final int valor1MB = 1048576;  
		int valorMaximo = valorMaxMBConfigurado * valor1MB;
		
		if (tamanho <= valorMaximo) {
			return true;
		}
		
		return false;
	}

}
