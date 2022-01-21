package br.gov.jfrj.siga.integracao.ws.siafem;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import br.gov.jfrj.siga.base.AplicacaoException;

public class SiafDoc {
	private String unidadeGestora;
	private String gestao;
	private String objetoProcesso;
	private String tipoLicitacao;
	private String id;
	private String ata;
	private String convenio;
	private String finalidade;
	private String processo;
	private String codUnico;
	private String codSemPapel;
	private String desdobramento;
	
	public SiafDoc(String[] dados) {
		try {
			int i = 0;
			this.codUnico = dados[i++].trim().replace("-", "");
			this.unidadeGestora = "00000" + (dados[i++].trim().split(" ")[0]);
			this.unidadeGestora = this.unidadeGestora.substring(this.unidadeGestora.length() - 6, this.unidadeGestora.length());
			this.gestao = dados[i++].trim();
			this.objetoProcesso = URLDecoder.decode(dados[i++].trim(), "UTF-8");
			this.tipoLicitacao = Integer.valueOf(dados[i++].split("-")[0].trim()) + "";
			this.id = dados[i++].trim();
			this.ata = dados[i++].trim().matches("[1Ss]") ? "S" : "N";
			this.convenio = dados[i++].trim().matches("[1Ss]") ? "S" : "N";
			this.finalidade = URLDecoder.decode(dados[i++].trim(), "UTF-8");
			this.processo = dados[i++].trim();
			this.desdobramento = dados[i++].trim();
		} catch (UnsupportedEncodingException e) {
			throw new AplicacaoException("Falha ao realizar a leitura da entrevista: " + e.getMessage());
		}
	}

	public void setCodUnico(String codUnico) {
		this.codUnico = codUnico;
	}
	
	public void setCodSemPapel(String codSemPapel) {
		this.codSemPapel = codSemPapel;
	}

	public String getSiafDoc() {
		String[] parametros = new String[] { unidadeGestora, gestao, processo, desdobramento, codUnico, codSemPapel, objetoProcesso, tipoLicitacao, id, ata, convenio, finalidade };

		String xml = SIAFDOC;
		if (id != null && !id.isEmpty())
			xml = xml.replace("<ID/>", "<ID>?9</ID>");
		if (processo != null && !processo.isEmpty())
			xml = xml.replace("<Processo/>", "<Processo>?3</Processo>");
		if (desdobramento != null && !desdobramento.isEmpty())
			xml = xml.replace("<Desdobramento/>", "<Desdobramento>?4</Desdobramento>");

		for (int i = 0; i < parametros.length; i++) {
			if(parametros[i] != null)
				xml = xml.replaceFirst("\\?" + (i+1), parametros[i]);
		}

		return xml.toString();
	}

	private String SIAFDOC = "<SIAFDOC>\n" +
			"	<cdMsg>SIAFPROCESSO001</cdMsg>\n" +
			"	<SiafemDocProcesso>\n" +
			"	  <documento>\n" +
			"		 <UG>?1</UG>\n" +
			"		 <Gestao>?2</Gestao>\n" +
			"		 <Processo/>\n" +
			"		 <Desdobramento/>\n" +
			"		 <CodUnico>?5</CodUnico>\n" +
			"		 <CodSemPapel>?6</CodSemPapel>\n" +
			"		 <Objeto>?7</Objeto>\n" +
			"		 <TipoLicitacao>?8</TipoLicitacao>\n" +
			"		 <ID/>\n" +
			"		 <DigitoID/>\n" +
			"		 <ATA>?10</ATA>\n" +
			"		 <Convenio>?11</Convenio>\n" +
			"		 <FlagPresencial/>\n" +
			"		 <FlagEletronico>X</FlagEletronico>\n" +
			"		 <ObjetoConvenio/>\n" +
			"		 <CNPJ/>\n" +
			"		 <CodMunicipio/>\n" +
			"		 <SignatarioCedente/>\n" +
			"		 <SignatarioConvenente/>\n" +
			"		 <NaturezaDespesa/>\n" +
			"		 <DataVigenciaInicial/>\n" +
			"		 <DataVigenciaFinal/>\n" +
			"		 <DataCelebracao/>\n" +
			"		 <DataPublicacao/>\n" +
			"		 <ValorContrapartida/>\n" +
			"		 <ValorTotal/>\n" +
			"		 <Situacao/>\n" +
			"		 <ObjetoResumido1/>\n" +
			"		 <ObjetoResumido2/>\n" +
			"		 <ObjetoResumido3/>\n" +
			"	  </documento>\n" +
			"	  <finalidade>\n" +
			"		 <Repeticao>\n" +
			"			<desc ID=\"1\">\n" +
			"			   <Finalidade>?12</Finalidade>\n" +
			"			</desc>\n" +
			"		 </Repeticao>\n" +
			"	  </finalidade>\n" +
			"	</SiafemDocProcesso>\n" +
			"</SIAFDOC>";
}
