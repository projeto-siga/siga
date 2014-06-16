package br.gov.jfrj.siga.ex.bl;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.tools.ant.util.StringUtils;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExAcesso {

	private Set<Object> acessos;

	public void add(Object o) {
		getAcessos().add(o);
	}

	public void incluirPessoas(ExDocumento doc) {
		for (ExMobil m : doc.getExMobilSet()) {
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.getResp() == null) {
					add(mov.getLotaResp());
				} else {
					add(mov.getResp());
				}

				if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO) {
					if (mov.getCadastrante() == null) {
						add(mov.getLotaCadastrante());
					} else {
						add(mov.getCadastrante());
					}
				}
			}
		}
	}

	public void incluirLotacoes(ExDocumento doc) {
		for (ExMobil m : doc.getExMobilSet()) {
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				add(mov.getLotaResp());
				if (mov.getResp() != null)
					add(mov.getResp().getOrgaoUsuario());
			}
		}
	}

	public void incluirCossignatarios(ExDocumento doc) {
		for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (m.getExTipoMovimentacao()
					.getIdTpMov()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO)
					&& m.getExMovimentacaoCanceladora() == null)
				add(m.getSubscritor());
		}
	}

	public void incluirPerfis(ExDocumento doc) {
		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao()
							.getIdTpMov()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {
				if (mov.getSubscritor() != null) {
					add(mov.getSubscritor());
				} else {
					add(mov.getLotaSubscritor());
				}
			}
		}
	}

	public void incluirOrgaos(ExDocumento doc) {
		for (ExMobil m : doc.getExMobilSet()) {
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.getLotaResp() != null)
					add(mov.getLotaResp().getOrgaoUsuario());
				if (mov.getResp() != null)
					add(mov.getResp().getOrgaoUsuario());
			}
		}
	}

	public void incluirSubsecretaria(DpLotacao lot) {
		DpLotacao subLotaDoc = Ex.getInstance().getComp().getSubsecretaria(lot);
		if (subLotaDoc == null) {
			add(lot);
			return;
		}
		Set<DpLotacao> lista = new HashSet<DpLotacao>();
		lista.add(subLotaDoc);
		lista = getSetoresSubordinados(lista);
		for (DpLotacao l : lista)
			add(l);
	}

	private Set<DpLotacao> getSetoresSubordinados(Set<DpLotacao> lista) {
		Set<DpLotacao> todosSubordinados = new HashSet<DpLotacao>();

		for (DpLotacao pai : lista) {
			if (pai.getDpLotacaoSubordinadosSet().size() <= 0) {
				todosSubordinados.add(pai);
				continue;
			} else {
				todosSubordinados.add(pai);
				todosSubordinados.addAll(getSetoresSubordinados(pai
						.getDpLotacaoSubordinadosSet()));
			}
		}

		return todosSubordinados;
	}

	private Set<ExDocumento> getDocumentoESeusPais(ExDocumento doc,
			Set<ExDocumento> set) {
		if (set == null) {
			set = new HashSet<ExDocumento>();
			set.add(doc);
		}

		for (ExMobil mob : doc.getExMobilSet()) {
			ExMobil pai = mob.getExMobilPai();
			if (pai != null) {
				ExDocumento docPai = pai.doc();
				if (!set.contains(docPai)) {
					getDocumentoESeusPais(docPai, set);
				}
			}
		}
		return set;
	}

	public Set<Object> getAcessos(ExDocumento doc) {

		if (doc == null)
			return getAcessos();

		// Aberto
		if (!doc.isAssinado()) {
			add(doc.getLotaCadastrante());
			add(doc.getSubscritor());
			add(doc.getTitular());
			// podeMovimentar(titular, lotaTitular, mob)
			incluirPerfis(doc);
			incluirCossignatarios(doc);
		}

		// Cancelado
		else if (doc.isCancelado()) {
			add(doc.getLotaCadastrante());
			add(doc.getSubscritor());
			add(doc.getTitular());
		}

		// Sem Efeito
		else if (doc.isSemEfeito()) {
			add(doc.getLotaCadastrante());
			add(doc.getSubscritor());
			add(doc.getTitular());
		}

		// Por nivel de acesso
		else {
			for (ExDocumento d : getDocumentoESeusPais(doc, null)) {
				incluirPerfis(d);

				// Verifica se o titular é subscritor de algum despacho do
				// dumento
				for (DpPessoa p : d.getSubscritorDespacho())
					add(p);

				// Verifica se o titular é subscritor ou cosignatário do dumento
				for (DpPessoa p : d.getSubscritorECosignatarios())
					add(p);

				switch (d.getExNivelAcesso().getGrauNivelAcesso().intValue()) {
				case (int) ExNivelAcesso.NIVEL_ACESSO_PUBLICO:
					add("PUBLICO");
					break;
				case (int) ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS:
					add(d.getLotaCadastrante().getOrgaoUsuario());
					add(d.getSubscritor());
					add(d.getTitular());
					add(d.getDestinatario());
					add(d.getLotaDestinatario());
					incluirOrgaos(d);
					break;
				case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOA_SUB:
					add(d.getSubscritor());
					add(d.getTitular());
					add(d.getDestinatario());
					if (d.getDestinatario() == null)
						add(d.getLotaDestinatario());
					incluirSubsecretaria(d.getLotaDestinatario());
					incluirPessoas(d);
					break;
				case (int) ExNivelAcesso.NIVEL_ACESSO_SUB_PESSOA:
					add(d.getSubscritor());
					add(d.getTitular());
					add(d.getDestinatario());
					if (d.getDestinatario() == null)
						add(d.getLotaDestinatario());
					incluirSubsecretaria(d.getLotaCadastrante());
					incluirPessoas(d);
					break;
				case (int) ExNivelAcesso.NIVEL_ACESSO_ENTRE_LOTACOES:
					add(d.getLotaCadastrante());
					add(d.getSubscritor());
					add(d.getTitular());
					add(d.getLotaDestinatario());
					incluirLotacoes(d);
					break;
				case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOAL:
					add(d.getCadastrante());
					add(d.getSubscritor());
					add(d.getTitular());
					add(d.getDestinatario());
					incluirPessoas(d);
					break;
				}
			}
		}
		return getAcessos();
	}

	public String getAcessosString(ExDocumento doc) {
		getAcessos(doc);

		if (getAcessos().contains("PUBLIC")) {
			return "PUBLIC";
		}

		// Otimizar a lista removendo todas as pessoas e lotações de um órgão,
		// quando este órgão todo pode acessar o documento
		Set<Object> toRemove = new HashSet<Object>();
		for (Object o : getAcessos()) {
			if (o instanceof CpOrgaoUsuario) {
				for (Object oo : getAcessos()) {
					if (oo instanceof DpLotacao) {
						if (((DpLotacao) oo).getOrgaoUsuario().equals(o))
							toRemove.add(oo);
					} else if (oo instanceof DpPessoa) {
						if (((DpPessoa) oo).getOrgaoUsuario().equals(o))
							toRemove.add(oo);
					}
				}
			}
		}
		getAcessos().removeAll(toRemove);

		SortedSet<String> result = new TreeSet<String>();
		for (Object o : getAcessos()) {
			if (o instanceof String)
				result.add((String) o);
			else if (o instanceof DpLotacao)
				result.add("L" + ((DpLotacao) o).getIdInicial());
			else if (o instanceof DpPessoa)
				result.add("P" + ((DpPessoa) o).getIdInicial());
		}

		StringBuilder sb = new StringBuilder();

		for (String each : result) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(each);
		}

		return sb.toString();
	}

	public Set<Object> getAcessos() {
		if (acessos == null)
			acessos = new HashSet<Object>();
		return acessos;
	}
}
