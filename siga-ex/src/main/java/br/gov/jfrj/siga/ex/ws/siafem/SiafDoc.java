package br.gov.jfrj.siga.ex.ws.siafem;

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
	
	// processo = código único
	public SiafDoc(String unidadeGestora, String gestao, String objetoProcesso, String tipoLicitacao, String id,
			String ata, String convenio, String finalidade) {
		this.unidadeGestora = unidadeGestora;
		this.gestao = gestao;
		this.objetoProcesso = objetoProcesso;
		this.tipoLicitacao = tipoLicitacao;
		this.id = id;
		this.ata = ata;
		this.convenio = convenio;
		this.finalidade = finalidade;
	}

	public SiafDoc(String[] dados) {
		try {
			int i = 0;
			this.processo = dados[i++].trim();
			this.unidadeGestora = dados[i++].trim();
			this.gestao = dados[i++].trim();
			this.objetoProcesso = URLDecoder.decode(dados[i++].trim(), "UTF-8");
			this.tipoLicitacao = Integer.valueOf(dados[i++].split("-")[0].trim()) + "";
			this.id = dados[i++].trim();
			this.ata = dados[i++].trim().matches("[1Ss]") ? "S" : "N";
			this.convenio = dados[i++].trim().matches("[1Ss]") ? "S" : "N";
			this.finalidade = URLDecoder.decode(dados[i++].trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AplicacaoException("Falha ao realizar a leitura da entrevista: " + e.getMessage());
		}
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public String getSiafDoc() {
		String[] parametros = new String[] { unidadeGestora, gestao, processo, objetoProcesso, tipoLicitacao, id, ata, convenio, finalidade };

		StringBuilder xml = new StringBuilder();
		if (id == null || id.isEmpty())
			xml.append(SIAFDOC.replace("<ID/>", "<ID/>"));
		else
			xml.append(SIAFDOC.replace("<ID/>", "<ID>?6</ID>"));

		for (int i = 0; i < parametros.length; i++) {
			incluirParametro(xml, "?" + (i + 1), parametros[i]);
		}

		return xml.toString();
	}

	private void incluirParametro(StringBuilder xml, String posicao, String parametro) {
		int index = xml.indexOf(posicao);

		if (index >= 0)
			xml.replace(index, index + posicao.length(), parametro);
	}

	private String SIAFDOC = "<SIAFDOC>\n" +
			"	<cdMsg>SIAFPROCESSO001</cdMsg>\n" +
			"	<SiafemDocProcesso>\n" +
			"	  <documento>\n" +
			"		 <UG>?1</UG>\n" +
			"		 <Gestao>?2</Gestao>\n" +
			"		 <Processo>?3</Processo>\n" +
			"		 <Desdobramento/>\n" +
			"		 <Objeto>?4</Objeto>\n" +
			"		 <TipoLicitacao>?5</TipoLicitacao>\n" +
			"		 <ID/>\n" +
			"		 <DigitoID/>\n" +
			"		 <ATA>?7</ATA>\n" +
			"		 <Convenio>?8</Convenio>\n" +
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
			"			   <Finalidade>?9</Finalidade>\n" +
			"			</desc>\n" +
			"		 </Repeticao>\n" +
			"	  </finalidade>\n" +
			"	</SiafemDocProcesso>\n" +
			"</SIAFDOC>";

	public String getProcesso() {
		return processo;
	}
}
