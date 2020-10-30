package br.gov.jfrj.siga.ex.bl;

import static br.gov.jfrj.siga.ex.ExMobil.isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExAcesso {
	public static final String ACESSO_PUBLICO = "PUBLICO";

	// Armazena os acessos específicos de um documento. Para calcular o acesso
	// ainda é necessário acrescentar os acessos do documento ao qual este está
	// juntado, e por assim em diante.
	//
	private Map<ExDocumento, Set<Object>> cache = new HashMap<ExDocumento, Set<Object>>();

	private Set<Object> acessos = null;

	private void add(Object o) {
		if (acessos.contains(ACESSO_PUBLICO)) {
			return;
		}

		if (o instanceof DpLotacao) {
			DpLotacao l = (DpLotacao) o;
			if (acessos.contains(l.getOrgaoUsuario()))
				return;
		}

		if (o instanceof DpPessoa) {
			DpPessoa p = (DpPessoa) o;
			if (acessos.contains(p.getOrgaoUsuario()))
				return;
		}

		acessos.add(o);
	}

	private void incluirPessoas(ExDocumento doc, Date dtDeRedefinicaoDoNivelDeAcesso) {
		for (ExMobil m : getMobilesAVarrer(doc)) {
			if (m.isGeral())
				continue;
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if (dtDeRedefinicaoDoNivelDeAcesso != null && mov.getDtMov().before(dtDeRedefinicaoDoNivelDeAcesso))
					continue;
				if (mov.getResp() == null) {
					add(mov.getLotaResp());
				} else {
					add(mov.getResp());
				}				
			 }	
			if (dtDeRedefinicaoDoNivelDeAcesso != null) {
				ExMovimentacao movUlt = m.getUltimaMovimentacaoAntesDaData(dtDeRedefinicaoDoNivelDeAcesso);
				if (movUlt != null){
					if (movUlt.getResp() == null) {
						add(movUlt.getLotaResp());
					} else {
						add(movUlt.getResp());
					}				
				}				
			}			
		}
	}

	private Set<ExMobil> getMobilesAVarrer(ExDocumento doc) {
		Set<ExMobil> mobiles = new HashSet<ExMobil>(); 
		if (doc.isProcesso()){
			mobiles.add(doc.getMobilGeral());
			mobiles.add(doc.getUltimoVolume());
			mobiles.add(doc.getVolume(doc.getNumUltimoVolume()-1)) ;
		} else {
			mobiles.addAll(doc.getExMobilSet());
		}
		mobiles.remove(null);
		return mobiles;
	}

	private void incluirLotacoes(ExDocumento doc, Date dtDeRedefinicaoDoNivelDeAcesso) {
		for (ExMobil m : getMobilesAVarrer(doc)) {
			if (m.isGeral())
				continue;
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if (dtDeRedefinicaoDoNivelDeAcesso != null && mov.getDtMov().before(dtDeRedefinicaoDoNivelDeAcesso))
					continue;
				add(mov.getLotaResp());
				if (mov.getResp() != null)
					add(mov.getResp().getLotacao());				
			}
			if (dtDeRedefinicaoDoNivelDeAcesso != null){
				ExMovimentacao movUlt = m.getUltimaMovimentacaoAntesDaData(dtDeRedefinicaoDoNivelDeAcesso);
				if (movUlt != null){
				    add(movUlt.getLotaResp());
				    if (movUlt.getResp() != null)
				        add(movUlt.getResp().getLotacao());
				}				
			}			
		}		
	}

	private void incluirCossignatarios(ExDocumento doc) {
		if (doc.getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao mov : doc.getMobilGeral()
					.getExMovimentacaoSet()) {
				if (mov.isCancelada())
					continue;
				if (mov.getExTipoMovimentacao()
						.getIdTpMov()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO)
						&& mov.getExMovimentacaoCanceladora() == null)
					add(mov.getSubscritor());
			}
		}
	}

	private void incluirColaboradores(ExDocumento doc) {
		if (doc.getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao mov : doc.getMobilGeral()
					.getExMovimentacaoSet()) {
				if (mov.isCancelada())
					continue;
				if (mov.getExTipoMovimentacao()
						.getIdTpMov()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO)
						&& mov.getExMovimentacaoCanceladora() == null) {
					ExParte parte = ExParte.create(mov.getDescrMov());
					String as[] = parte.getResponsavel().split(";");
					for (String s : as) {
						s = s.trim();
						DpPessoa pes = ExDao.getInstance()
								.getPessoaFromSigla(s);
						if (pes != null) {
							add(pes);
							continue;
						}
						DpLotacao lot = ExDao.getInstance()
								.getLotacaoFromSigla(s);
						if (lot != null) {
							add(lot);
							continue;
						}
					}
				}
			}
		}
	}

	private void incluirPerfis(ExDocumento doc) {
		if (doc.getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao mov : doc.getMobilGeral()
					.getExMovimentacaoSet()) {
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
	}

	private void incluirOrgaos(ExDocumento doc, Date dtDeRedefinicaoDoNivelDeAcesso) {
		for (ExMobil m : getMobilesAVarrer(doc)) {
			if (m.isGeral())
				continue;
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if (dtDeRedefinicaoDoNivelDeAcesso != null && mov.getDtMov().before(dtDeRedefinicaoDoNivelDeAcesso))
					continue;
				if (mov.getLotaResp() != null)
					add(mov.getLotaResp().getOrgaoUsuario());
				if (mov.getResp() != null)
					add(mov.getResp().getOrgaoUsuario());
			}
			if (dtDeRedefinicaoDoNivelDeAcesso != null){
                ExMovimentacao movUlt = m.getUltimaMovimentacaoAntesDaData(dtDeRedefinicaoDoNivelDeAcesso);
				if (movUlt != null) {
				  if (movUlt.getLotaResp() != null)
					  add(movUlt.getLotaResp().getOrgaoUsuario());
			      if (movUlt.getResp() != null)
					 add(movUlt.getResp().getOrgaoUsuario());
				}
			}					
		}
	}

	private void incluirSubsecretaria(DpLotacao lot) {
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

	private Set<Object> calcularAcessos(ExDocumento doc, Date dt) {
		acessos = new HashSet<Object>();

		if (doc == null)
			return acessos;

		List<ExMovimentacao> listaMov = new ArrayList<ExMovimentacao>();
		
		if (isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso()) 									
			listaMov = doc.getListaMovimentacaoPorRestricaoAcesso();
		else
			listaMov.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO));
		
		if(listaMov.isEmpty()) {

		
			// Aberto
			if (doc.isPendenteDeAssinatura()) {
				switch (doc.getExNivelAcesso().getGrauNivelAcesso()) {
				case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOAL:
				case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOA_SUB:
					add(doc.getCadastrante());
					break;
				default:
					add(doc.getLotaCadastrante());
				}
				add(doc.getSubscritor());
				add(doc.getTitular());
				// podeMovimentar(titular, lotaTitular, mob)
				incluirPerfis(doc);
				incluirCossignatarios(doc);
				incluirColaboradores(doc);
			}
	
			// TODO: devemos buscar a data de cancelamento
	
			// Cancelado
			else if (doc.isCancelado()) {
				add(doc.getLotaCadastrante());
				add((doc.getSubscritor() != null)?doc.getSubscritor():doc.getCadastrante());
				add(doc.getTitular());
			}
	
			// TODO: devemos buscar a data que ficou sem efeito
	
			// Sem Efeito
			else if (doc.isSemEfeito()) {			
				add((doc.getSubscritor() != null)?doc.getSubscritor():doc.getCadastrante());			
			}
	
			// Por nivel de acesso
			else {
				Set<ExDocumento> documentoESeusPais = doc.getDocumentoETodosOsPaisDasVias();
	
				// Calcula os acessos de cada documento individualmente e armazena
				// no cache
				for (ExDocumento d : documentoESeusPais) {
					if (cache.containsKey(d))
						continue;
	
					acessos = new HashSet<Object>();
	
					add(d.getSubscritor());
					add(d.getTitular());
					incluirPerfis(d);
					incluirCossignatarios(d);
	
					// Verifica se o titular é subscritor de algum despacho do
					// dumento
					addSubscritorDespacho(d);
	
					// TODO: buscar a data que foi feita a última movimentação de
					// mudança de nivel de acesso
					
					Date dtDeRedefinicaoDoNivelDeAcesso = d.getDataDeRedefinicaoDoNivelDeAcesso();
	
					switch (d.getExNivelAcessoAtual().getGrauNivelAcesso().intValue()) {
					case (int) ExNivelAcesso.NIVEL_ACESSO_PUBLICO:
						add(ACESSO_PUBLICO);
						break;
					case (int) ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS:
						add(d.getLotaCadastrante().getOrgaoUsuario());
						add(d.getSubscritor());
						add(d.getTitular());
						add(d.getDestinatario());
						add(d.getLotaDestinatario());
						incluirOrgaos(d, dtDeRedefinicaoDoNivelDeAcesso);
						break;
					case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOA_SUB:
						add(d.getSubscritor());
						add(d.getTitular());
						add(d.getDestinatario());
						if (d.getDestinatario() == null)
							add(d.getLotaDestinatario());
						incluirSubsecretaria(d.getLotaDestinatario());
						incluirPessoas(d, dtDeRedefinicaoDoNivelDeAcesso);
						break;
					case (int) ExNivelAcesso.NIVEL_ACESSO_SUB_PESSOA:
						add(d.getSubscritor());
						add(d.getTitular());
						add(d.getDestinatario());
						if (d.getDestinatario() == null)
							add(d.getLotaDestinatario());
						incluirSubsecretaria(d.getLotaCadastrante());
						incluirPessoas(d, dtDeRedefinicaoDoNivelDeAcesso);
						break;
					case (int) ExNivelAcesso.NIVEL_ACESSO_ENTRE_LOTACOES:
						add(d.getLotaCadastrante());
						add(d.getSubscritor());
						add(d.getTitular());
						add(d.getLotaDestinatario());
						incluirLotacoes(d, dtDeRedefinicaoDoNivelDeAcesso);
						break;
					case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOAL:
						add(d.getCadastrante());
						add(d.getSubscritor());
						add(d.getTitular());
						add(d.getDestinatario());
						incluirPessoas(d, dtDeRedefinicaoDoNivelDeAcesso);
						break;
					}
					cache.put(d, acessos);
				}
	
				// Combina os acessos de todos os documentos para gerar o resultado
				acessos = new HashSet<Object>();
				for (ExDocumento d : documentoESeusPais) {
	
					for (Object o : cache.get(d))
						add(o);
				}
			}
		} else {
			List<ExMovimentacao> listaAcomp =  new ArrayList<ExMovimentacao>();
			listaAcomp.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL));
			
			for(ExMobil exMobil : doc.getExMobilSet()) {
				listaAcomp.addAll(exMobil.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA));
			}
			
			if(doc.getPai() != null) {
				for (ExMobil exMobil : doc.getPai().getExMobilSet()) {
					listaAcomp.addAll(exMobil.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA));
				}				
			}
			
			for (ExMovimentacao exMovimentacao : listaMov) {
				add(exMovimentacao.getCadastrante());
								
				if (isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso()) {
					add(exMovimentacao.getSubscritor());					
				} else {					
					if(exMovimentacao.getSubscritor().equals(doc.getCadastrante())) {
						add(doc.getCadastrante());
					}
					if(exMovimentacao.getSubscritor().equals(doc.getDestinatario())) {
						add(doc.getDestinatario());
					}
					if(exMovimentacao.getSubscritor().equals(doc.getSubscritor())) {
						add(doc.getSubscritor());
					}
					if(exMovimentacao.getSubscritor().equals(doc.getTitular())) {
						add(doc.getTitular());
					}
					
					for (ExMovimentacao exMovimentacaoAcomp : listaAcomp) {
						if(exMovimentacao.getSubscritor().equals(exMovimentacaoAcomp.getSubscritor())) {
							add(exMovimentacao.getSubscritor());
						}
						if(exMovimentacao.getSubscritor().equals(exMovimentacaoAcomp.getResp())) {
							add(exMovimentacao.getSubscritor());
						}
					}
				}				
			}
		}
		return acessos;
	}

	/**
	 * Retorna uma lista com os subscritores de todos os despachos não
	 * cancelados do documento.
	 */
	private void addSubscritorDespacho(ExDocumento doc) {
		List<DpPessoa> subscritoresDesp = new ArrayList<DpPessoa>();

		for (ExMobil mob : getMobilesAVarrer(doc)) {
			for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
				if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
						|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO
						|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA || mov
						.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA)
						&& !mov.isCancelada())
					add(mov.getSubscritor());
			}
		}
	}
	public String getAcessosString(ExDocumento doc, Date dt) {
		return getAcessosString(doc, dt, null, null);
	}
	public String getAcessosString(ExDocumento doc, Date dt, Object incluirAcesso, Object excluirAcesso) {
		calcularAcessos(doc, dt);

		if (acessos.contains(ACESSO_PUBLICO)) {
			return ACESSO_PUBLICO;
		}

		acessos.remove(null);

		// Otimizar a lista removendo todas as pessoas e lotações de um órgão,
		// quando este órgão todo pode acessar o documento
		Set<Object> toRemove = new HashSet<Object>();
		for (Object o : acessos) {
			if (o instanceof CpOrgaoUsuario) {
				CpOrgaoUsuario ou = (CpOrgaoUsuario) o;
				for (Object oo : acessos) {
					if (oo instanceof DpLotacao) {
						if (((DpLotacao) oo).getOrgaoUsuario().getId()
								.equals(ou.getId()))
							toRemove.add(oo);
					} else if (oo instanceof DpPessoa) {
						if (((DpPessoa) oo).getOrgaoUsuario().getId()
								.equals(ou.getId()))
							toRemove.add(oo);
					}
				}
			}
		}
		if (incluirAcesso != null)
			acessos.add(incluirAcesso);
		if (excluirAcesso != null)
			toRemove.add(excluirAcesso);
		
		acessos.removeAll(toRemove);

		SortedSet<String> result = new TreeSet<String>();
		for (Object o : acessos) {
			if (o instanceof String)
				result.add((String) o);
			else if (o instanceof CpOrgaoUsuario)
				result.add("O" + ((CpOrgaoUsuario) o).getId());
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

}
