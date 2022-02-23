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
	private String flagEletronico;
	private String finalidade;
	private String processo;
	private String codUnico;
	private String codSemPapel;
	private String desdobramento;

	public SiafDoc(String[] dados) {
		try {
			int i = -1;
			this.codUnico = dados[++i].trim().replace("-", "");
			this.unidadeGestora = "00000" + (dados[++i].trim().split(" ")[0]);
			this.unidadeGestora = this.unidadeGestora.substring(this.unidadeGestora.length() - 6, this.unidadeGestora.length());
			this.gestao = dados[++i].trim();
			this.objetoProcesso = URLDecoder.decode(dados[++i].trim(), "UTF-8");
			this.tipoLicitacao = dados[++i].split("-")[0].trim().matches("\\d+") ? (Integer.valueOf(dados[i].split("-")[0].trim()) + "") : "";
			this.id = dados[++i].trim();
			this.ata = dados[++i].trim().matches("[1Ss]") ? "S" : "N";
			this.convenio = dados[++i].trim().matches("[1Ss]") ? "S" : "N";
			this.finalidade = URLDecoder.decode(dados[++i].trim(), "UTF-8");
			this.processo = dados[++i].trim();
			this.desdobramento = dados[++i].trim();
			this.flagEletronico = "X";

//			Quando for Legado o usuário deve preencher SOMENTE os campos
//			- UG/Gestão
//			- número do processo legado (10 posições alfanumérico)
//			- desdobramento (1 posição)
			if (this.processo != null && !this.processo.isEmpty()) {
				this.objetoProcesso = this.tipoLicitacao = this.id = this.ata = this.convenio = this.finalidade = this.flagEletronico = "";
			}

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
		String[] parametros = new String[] { unidadeGestora, gestao, processo, desdobramento, codUnico, codSemPapel, objetoProcesso, tipoLicitacao, id, ata, convenio, flagEletronico, finalidade };

		String xml = SIAFDOC;

		for (int i = 0; i < parametros.length; i++)
			xml = inserirValor(xml, i, parametros[i]);

		return xml.toString();
	}

	private String inserirValor(String xml, int i, String valor) {
		String tagVazia = "<" + TAGS[i] + "/>";
		String tagValor = "<" + TAGS[i] + ">" + valor + "</" + TAGS[i] + ">";

		if (valor != null && !valor.isEmpty())
			xml = xml.replace(tagVazia, tagValor);

		return xml;
	}

	private String[] TAGS = { "UG", "Gestao", "Processo", "Desdobramento", "CodUnico", "CodSemPapel", "Objeto", "TipoLicitacao", "ID", "ATA", "Convenio", "FlagEletronico", "Finalidade" };

	private String SIAFDOC = "<SIAFDOC>\n" +
			"   <cdMsg>SIAFPROCESSO001</cdMsg>\n" +
			"   <SiafemDocProcesso>\n" +
			"      <documento>\n" +
			"         <UG/>\n" +
			"         <Gestao/>\n" +
			"         <Processo/>\n" +
			"         <Desdobramento/>\n" +
			"         <CodUnico/>\n" +
			"         <CodSemPapel/>\n" +
			"         <Objeto/>\n" +
			"         <TipoLicitacao/>\n" +
			"         <ID/>\n" +
			"         <DigitoID/>\n" +
			"         <ATA/>\n" +
			"         <Convenio/>\n" +
			"         <FlagPresencial/>\n" +
			"         <FlagEletronico/>\n" +
			"         <ObjetoConvenio/>\n" +
			"         <CNPJ/>\n" +
			"         <CodMunicipio/>\n" +
			"         <SignatarioCedente/>\n" +
			"         <SignatarioConvenente/>\n" +
			"         <NaturezaDespesa/>\n" +
			"         <DataVigenciaInicial/>\n" +
			"         <DataVigenciaFinal/>\n" +
			"         <DataCelebracao/>\n" +
			"         <DataPublicacao/>\n" +
			"         <ValorContrapartida/>\n" +
			"         <ValorTotal/>\n" +
			"         <Situacao/>\n" +
			"         <ObjetoResumido1/>\n" +
			"         <ObjetoResumido2/>\n" +
			"         <ObjetoResumido3/>\n" +
			"      </documento>\n" +
			"      <finalidade>\n" +
			"         <Repeticao>\n" +
			"            <desc ID='1'>\n" +
			"               <Finalidade/>\n" +
			"            </desc>\n" +
			"         </Repeticao>\n" +
			"      </finalidade>\n" +
			"   </SiafemDocProcesso>\n" +
			"</SIAFDOC>";
}
