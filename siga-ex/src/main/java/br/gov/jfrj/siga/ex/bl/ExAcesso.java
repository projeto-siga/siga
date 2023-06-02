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
import java.util.stream.Collectors;

import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExAcesso {

	// Armazena os acessos específicos de um documento. Para calcular o acesso
	// ainda é necessário acrescentar os acessos do documento ao qual este está
	// juntado, e por assim em diante.
	//
	private Map<ExDocumento, Set<Object>> cache = new HashMap<ExDocumento, Set<Object>>();

	private Set<Object> acessos = null;

	private void add(Object o) {
	    if (o == null)
	        return;
	    
		if (acessos.contains(XjusUtils.ACESSO_PUBLICO)) {
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
				if (mov.getLotaResp() != null)
					add(mov.getLotaResp());
				else if (mov.getResp() != null)
					add(mov.getResp().getLotacao());					
			}
			if (dtDeRedefinicaoDoNivelDeAcesso != null){
				ExMovimentacao movUlt = m.getUltimaMovimentacaoAntesDaData(dtDeRedefinicaoDoNivelDeAcesso);
				if (movUlt != null){
					if (movUlt.getLotaResp() != null)
						add(movUlt.getLotaResp());
					else if (movUlt.getResp() != null)
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
				if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
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
				if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONTROLE_DE_COLABORACAO
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
						&& mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.VINCULACAO_PAPEL) {
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
		
		//Caso o Acesso esteja restrito, valerá a lista de pessoas na qual foi limitada
		if (!doc.getMobilGeral().isAcessoRestrito()) {
		
			// Aberto
			if (doc.isPendenteDeAssinatura()) {
				switch (doc.getExNivelAcessoAtual().getGrauNivelAcesso()) {
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
						add(XjusUtils.ACESSO_PUBLICO);
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
			boolean adicionaAcesso = false;
			for (ExMovimentacao exMovimentacao : doc.getMobilGeral().getMovsNaoCanceladas (ExTipoDeMovimentacao.RESTRINGIR_ACESSO)) {
				//Mantém último cadastrante de Restrição de Acesso na lista de Acesso para que não perca acesso imediato após ação
				//Não usado a Cancelada, para na exclusão o cadastrante da última restrição não perder o acesso
				//Então no mínimo, se houver a exclusão de todas as restrições, o operador não perde o acesso
				add(doc.getMobilGeral().getUltimaMovimentacao(ExTipoDeMovimentacao.RESTRINGIR_ACESSO).getCadastrante());
				
				//Adiciona as pessoas objetos da restrição na lista de acesso caso não esteja
				adicionaAcesso = !acessos
									.stream()
									.anyMatch(acesso -> exMovimentacao.getSubscritor().equivale(acesso));
				
				if (adicionaAcesso)
					add(exMovimentacao.getSubscritor());
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
				if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO
						|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_INTERNO
						|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA || mov
						.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA)
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
		return XjusUtils.getAcessosString(acessos, dt, incluirAcesso, excluirAcesso);
	}

}
