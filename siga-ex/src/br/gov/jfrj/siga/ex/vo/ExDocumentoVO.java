/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.ProcessadorModeloFreemarker;

public class ExDocumentoVO extends ExVO {
	ExDocumento doc;
	String classe;
	List<ExMobilVO> mobs = new ArrayList<ExMobilVO>();
	String nomeCompleto;
	String dtDocDDMMYY;
	String subscritorString;
	String classificacaoDescricaoCompleta;
	List<String> tags;
	String destinatarioString;
	String descrDocumento;
	String nmNivelAcesso;
	String paiSigla;
	String tipoDocumento;
	String dtFechamento;
	String nmArqMod;
	String conteudoBlobHtmlString;
	String sigla;
	String fisicoOuEletronico;
	boolean fDigital;
	String dadosComplementares;
	String forma;
	String modelo;

	public ExDocumentoVO(ExDocumento doc, ExMobil mob, DpPessoa titular,
			DpLotacao lotaTitular, boolean completo) throws Exception {
		this.doc = doc;
		this.sigla = doc.getSigla();
		this.descrDocumento = doc.getDescrDocumento();

		if (!completo)
			return;

		this.nomeCompleto = doc.getNomeCompleto();
		this.dtDocDDMMYY = doc.getDtDocDDMMYY();
		this.subscritorString = doc.getSubscritorString();
		if (doc.getExClassificacao() != null)
			this.classificacaoDescricaoCompleta = doc.getExClassificacao()
					.getDescricaoCompleta();
		this.destinatarioString = doc.getDestinatarioString();
		if (doc.getExNivelAcesso() != null)
			this.nmNivelAcesso = doc.getExNivelAcesso().getNmNivelAcesso();
		if (doc.getExMobilPai() != null)
			this.paiSigla = doc.getExMobilPai().getSigla();
		if (doc.getExTipoDocumento() != null)
			switch (doc.getExTipoDocumento().getId().intValue()) {
			case 1:
				this.tipoDocumento = "interno";
				break;
			case 2:
				this.tipoDocumento = "interno_importado";
				break;
			case 3:
				this.tipoDocumento = "externo";
				break;
			}
		this.dtFechamento = doc.getDtFechamentoDDMMYY();
		if (doc.getExModelo() != null)
			this.nmArqMod = doc.getExModelo().getNmArqMod();

		this.conteudoBlobHtmlString = doc
				.getConteudoBlobHtmlStringComReferencias();

		if (doc.isEletronico()) {
			this.classe = "header_eletronico";
			this.fisicoOuEletronico = "Documento Eletrônico";
			this.fDigital = true;
		} else {
			this.classe = "header";
			this.fisicoOuEletronico = "Documento Físico";
			this.fDigital = false;
		}

		this.forma = doc.getExFormaDocumento() != null ? doc
				.getExFormaDocumento().getDescricao() : "";
		this.modelo = doc.getExModelo() != null ? doc.getExModelo().getNmMod()
				: "";

		if (mob != null) {
			SortedSet<ExMobil> mobsDoc;
			if (doc.isProcesso())
				mobsDoc = doc.getExMobilSetInvertido();
			else
				mobsDoc = doc.getExMobilSet();

			for (ExMobil m : mobsDoc) {
				if (mob.isGeral() || m.isGeral()
						|| mob.getId().equals(m.getId()))
					mobs.add(new ExMobilVO(m, titular, lotaTitular, completo));
			}

			addAcoes(doc, titular, lotaTitular);
		}

		addDadosComplementares();

		tags = new ArrayList<String>();
		if (doc.getExClassificacao() != null) {
			String classificacao = doc.getExClassificacao().getDescricao();
			if (classificacao != null && classificacao.length() != 0) {
				String a[] = classificacao.split(": ");
				for (String s : a) {
					String ss = "@" + Texto.slugify(s, true, true);
					if (!tags.contains(ss)) {
						tags.add(ss);
					}
				}
			}
		}
		if (doc.getExModelo() != null) {
			String ss = "@"
					+ Texto.slugify(doc.getExModelo().getNmMod(), true, true);
			if (!tags.contains(ss)) {
				tags.add(ss);
			}
		}
		// if (doc.getExClassificacao() != null)
		// tags.add("@doc-classe:" + doc.getExClassificacao().getSigla());
		// if (doc.getExFormaDocumento() != null)
		// tags.add("@doc-tipo:" +
		// Texto.slugify(doc.getExFormaDocumento().getSigla(), true, true));
		// if (doc.getExModelo() != null)
		// tags.add("@doc-modelo:" + Texto.slugify(doc.getExModelo().getNmMod(),
		// true, true));
	}

	public ExDocumentoVO(ExDocumento doc) throws Exception {
		this.doc = doc;
		this.sigla = doc.getSigla();
		this.nomeCompleto = doc.getNomeCompleto();
		this.dtDocDDMMYY = doc.getDtDocDDMMYY();
		this.descrDocumento = doc.getDescrDocumento();
		if (doc.isEletronico()) {
			this.classe = "header_eletronico";
			this.fisicoOuEletronico = "Documento Eletrônico";
			this.fDigital = true;
		} else {
			this.classe = "header";
			this.fisicoOuEletronico = "Documento Físico";
			this.fDigital = false;
		}

	}

	/**
	 * @param doc
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExDocumento doc, DpPessoa titular,
			DpLotacao lotaTitular) throws Exception {
		ExVO vo = this;
		for (ExMobilVO mobvo : mobs) {
			if (mobvo.getMob().isGeral())
				vo = mobvo;
		}

		ExMobil mob = doc.getMobilGeral();

		vo.addAcao("folder_magnify", "Visualizar Dossiê", "/expediente/doc",
				"exibirProcesso", Ex.getInstance().getComp()
						.podeVisualizarImpressao(titular, lotaTitular, mob));

		vo.addAcao(
				"printer",
				"Visualizar Impressão",
				"/expediente/doc",
				"pdf",
				Ex.getInstance().getComp()
						.podeVisualizarImpressao(titular, lotaTitular, mob),
				null, "&popup=true", null, null, null);

		vo.addAcao(
				"lock",
				"Finalizar",
				"/expediente/doc",
				"fechar",
				Ex.getInstance().getComp()
						.podeFinalizar(titular, lotaTitular, mob),
				"Confirma a finalização do documento?", null, null, null, "once");

		// addAcao("Finalizar e Assinar", "/expediente/mov", "fechar_assinar",
		// podeFinalizarAssinar(titular, lotaTitular, mob),
		// "Confirma a finalização do documento?", null, null, null);

		vo.addAcao("pencil", "Editar", "/expediente/doc", "editar", Ex
				.getInstance().getComp().podeEditar(titular, lotaTitular, mob));

		vo.addAcao(
				"delete",
				"Excluir",
				"/expediente/doc",
				"excluir",
				Ex.getInstance().getComp()
						.podeExcluir(titular, lotaTitular, mob),
				"Confirma a exclusão do documento?", null, null, null, "once");

		vo.addAcao("user_add", "Incluir Cossignatário", "/expediente/mov",
				"incluir_cosignatario", Ex.getInstance().getComp()
						.podeIncluirCosignatario(titular, lotaTitular, mob));

		vo.addAcao(
				"attach",
				"Anexar Arquivo",
				"/expediente/mov",
				"anexar",
				Ex.getInstance().getComp()
						.podeAnexarArquivo(titular, lotaTitular, mob));
		vo.addAcao(
				"tag_yellow",
				"Fazer Anotação",
				"/expediente/mov",
				"anotar",
				Ex.getInstance().getComp()
						.podeFazerAnotacao(titular, lotaTitular, mob));

		vo.addAcao("folder_user", "Definir Perfil", "/expediente/mov",
				"vincularPapel", Ex.getInstance().getComp()
						.podeFazerVinculacaoPapel(titular, lotaTitular, mob));

		vo.addAcao(
				"cd",
				"Download do Conteúdo",
				"/expediente/doc",
				"anexo",
				Ex.getInstance().getComp()
						.podeDownloadConteudo(titular, lotaTitular, mob));

		vo.addAcao("sitemap_color", "Exibir Todas as Vias", "/expediente/doc",
				"exibir", doc.isExpediente() && doc.getDtFechamento() != null,
				null, "&exibirCompleto=false", null, null, null);

		vo.addAcao("sitemap_color", "Exibir Todos os Volumes",
				"/expediente/doc", "exibir",
				doc.isProcesso() && doc.getDtFechamento() != null, null,
				"&exibirCompleto=false", null, null, "once");

		vo.addAcao("add", "Criar Via", "/expediente/doc", "criarVia", Ex
				.getInstance().getComp()
				.podeCriarVia(titular, lotaTitular, mob), null, null, null, null, "once");

		vo.addAcao(
				"add",
				"Abrir Novo Volume",
				"/expediente/doc",
				"criarVolume",
				Ex.getInstance().getComp()
						.podeCriarVolume(titular, lotaTitular, mob),
				"Confirma a abertura de um novo volume?", null, null, null, "once");

		vo.addAcao(
				"link_add",
				"Criar Subprocesso",
				"/expediente/doc",
				"editar",
				Ex.getInstance().getComp()
						.podeCriarSubprocesso(titular, lotaTitular, mob), null,
				"mobilPaiSel.sigla=" + getSigla() + "&idForma="
						+ mob.doc().getExFormaDocumento().getIdFormaDoc(),
				null, null, null);

		vo.addAcao(
				"script_edit",
				"Registrar Assinatura Manual",
				"/expediente/mov",
				"registrar_assinatura",
				Ex.getInstance().getComp()
						.podeRegistrarAssinatura(titular, lotaTitular, mob));

		vo.addAcao(
				"script_key",
				"Assinar Digitalmente",
				"/expediente/mov",
				"assinar",
				Ex.getInstance().getComp()
						.podeAssinar(titular, lotaTitular, mob));

		if (doc.getDtFechamento() != null && doc.getNumExpediente() != null) {
			// documentos finalizados
			if (mob.temAnexos())
				vo.addAcao("script_key", "Assinar Anexos", "/expediente/mov",
						"assinar_anexos_geral", true);

			vo.addAcao(
					"link_add",
					"Criar Anexo",
					"/expediente/doc",
					"editar",
					Ex.getInstance()
							.getComp()
							.podeAnexarArquivoAlternativo(titular, lotaTitular,
									mob), null,
					"criandoAnexo=true&mobilPaiSel.sigla=" + getSigla(), null,
					null, null);
		}

		vo.addAcao("shield", "Redefinir Nível de Acesso", "/expediente/mov",
				"redefinir_nivel_acesso", Ex.getInstance().getComp()
						.podeRedefinirNivelAcesso(titular, lotaTitular, mob));

		vo.addAcao(
				"book_add",
				"Solicitar Publicação no Boletim",
				"/expediente/mov",
				"boletim_agendar",
				Ex.getInstance()
						.getComp()
						.podeBotaoAgendarPublicacaoBoletim(titular,
								lotaTitular, mob));

		vo.addAcao(
				"book_link",
				"Registrar Publicação do BIE",
				"/expediente/mov",
				"boletim_publicar",
				Ex.getInstance()
						.getComp()
						.podeBotaoAgendarPublicacaoBoletim(titular,
								lotaTitular, mob), null, null, null, null, "once");

		vo.addAcao(
				"error_go",
				"Refazer",
				"/expediente/doc",
				"reabrir",
				Ex.getInstance().getComp()
						.podeReabrir(titular, lotaTitular, mob),
				"Esse documento será cancelado e seus dados serão copiados para um novo expediente em elaboração. Prosseguir?",
				null, null, null, "once");

		vo.addAcao(
				"arrow_divide",
				"Duplicar",
				"/expediente/doc",
				"duplicar",
				Ex.getInstance().getComp()
						.podeDuplicar(titular, lotaTitular, mob),
				"Esta operação criará um expediente com os mesmos dados do atual. Prosseguir?",
				null, null, null, "once");

		// test="${exibirCompleto != true}" />
		vo.addAcao(
				"eye",
				"Exibir Informações Completas",
				"/expediente/doc",
				"exibir",
				Ex.getInstance().getComp()
						.podeDuplicar(titular, lotaTitular, mob), null,
				"&exibirCompleto=true", null, null, null);

		vo.addAcao(
				"report_link",
				"Agendar Publicação no DJE",
				"/expediente/mov",
				"agendar_publicacao",
				Ex.getInstance().getComp()
						.podeAgendarPublicacao(titular, lotaTitular, mob));

		vo.addAcao(
				"report_add",
				"Solicitar Publicação no DJE",
				"/expediente/mov",
				"pedir_publicacao",
				Ex.getInstance().getComp()
						.podePedirPublicacao(titular, lotaTitular, mob));

		// <ww:param name="idFormaDoc">60</ww:param>
		vo.addAcao(
				"arrow_undo",
				"Desfazer Cancelamento",
				"/expediente/doc",
				"desfazerCancelamentoDocumento",
				Ex.getInstance()
						.getComp()
						.podeDesfazerCancelamentoDocumento(titular,
								lotaTitular, mob),
				"Esta operação anulará o cancelamento do documento e tornará o documento novamente editável. Prosseguir?",
				null, null, null, "once");

		vo.addAcao(
				"delete",
				"Cancelar Documento",
				"/expediente/doc",
				"tornarDocumentoSemEfeito",
				Ex.getInstance()
						.getComp()
						.podeTornarDocumentoSemEfeito(titular, lotaTitular, mob),
				"Esta operação tornará esse documento sem efeito. Prosseguir?",
				null, null, null, "once");
	}

	public void addDadosComplementares() throws Exception {
		ProcessadorModeloFreemarker p = new ProcessadorModeloFreemarker();
		Map attrs = new HashMap();
		attrs.put("nmMod", "macro dadosComplementares");
		attrs.put("template", "[@dadosComplementares/]");
		attrs.put("doc", this.getDoc());
		dadosComplementares = p.processarModelo(doc.getOrgaoUsuario(), attrs,
				null);
	}

	public String getClasse() {
		return classe;
	}

	public String getClassificacaoDescricaoCompleta() {
		return classificacaoDescricaoCompleta;
	}

	public String getConteudoBlobHtmlString() {
		return conteudoBlobHtmlString;
	}

	public String getDescrDocumento() {
		return descrDocumento;
	}

	public String getDestinatarioString() {
		return destinatarioString;
	}

	public ExDocumento getDoc() {
		return doc;
	}

	public String getDtDocDDMMYY() {
		return dtDocDDMMYY;
	}

	public String getDtFechamento() {
		return dtFechamento;
	}

	public List<ExMobilVO> getMobs() {
		return mobs;
	}

	public String getNmArqMod() {
		return nmArqMod;
	}

	public String getNmNivelAcesso() {
		return nmNivelAcesso;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public String getPaiSigla() {
		return paiSigla;
	}

	public String getSigla() {
		return sigla;
	}
	
	public String getSiglaCurtaSubProcesso() {
		if(doc.isProcesso() && doc.getExMobilPai() != null) {
			try {
				return sigla.substring(sigla.length() - 3, sigla.length());
			} catch (Exception e) {
				return sigla;
			}
		}
		
		return "";
	}	

	public String getSubscritorString() {
		return subscritorString;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getFisicoOuEletronico() {
		return fisicoOuEletronico;
	}

	public boolean isDigital() {
		return fDigital;
	}

	@Override
	public String toString() {
		String s = getSigla() + "[" + getAcoes() + "]";
		for (ExMobilVO m : getMobs()) {
			s += "\n" + m.toString();
		}
		return s;
	}

	public String getDadosComplementares() {
		return dadosComplementares;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
