package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.logic.ExLotacaoEstaVinculadoPorPerfil;
import br.gov.jfrj.siga.ex.logic.ExPessoaEstaVinculadaPorPerfil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class AcessoConsulta {
	Pattern pattern;

	public AcessoConsulta(long idPessoaIni, long idLotacaoIni,
			long idOrgaoUsuPessoa, long idOrgaoUsuLotacao) {
		super();

		StringBuilder sb = new StringBuilder();
		sb.append("((^|,)PUBLICO($|,))");
		if (idPessoaIni != 0L)
			sb.append("|((^|,)P" + idPessoaIni + "($|,))");
		if (idLotacaoIni != 0L)
			sb.append("|((^|,)L" + idLotacaoIni + "($|,))");
		if (idOrgaoUsuPessoa != 0L)
			sb.append("|((^|,)O" + idOrgaoUsuPessoa + "($|,))");
		if (idOrgaoUsuLotacao != idOrgaoUsuPessoa && idOrgaoUsuLotacao != 0L)
			sb.append("|((^|,)O" + idOrgaoUsuLotacao + "($|,))");

		this.pattern = Pattern.compile(sb.toString());
	}

	public boolean podeAcessar(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		if (titular != null && ehPublicoExterno(titular)) {
			return podeAcessarPublicoExterno(doc, titular, lotaTitular);
		} else {
			return this.pattern.matcher(doc.getDnmAcesso()).find();
		}
	}
	
	public boolean podeAcessarPublicoExterno(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		
        if (Utils.equivaleENaoENulo(titular, doc.getSubscritor())) return true;
        if (Utils.equivaleENaoENulo(titular, doc.getCadastrante())) return true;
		
		if (doc.getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao mov : doc.getMobilGeral()
					.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.VINCULACAO_PAPEL)
				  || (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO)) {
					if (mov.getSubscritor() != null &&  titular != null && mov.getSubscritor().equivale(titular) ) {
						return true;
					} 
					if (mov.getLotaSubscritor() != null &&  lotaTitular != null && mov.getLotaSubscritor().equivale(lotaTitular) ) {
						return true;
					} 
				}
			}
		}
		
		List<ExDocumento> listaTodosOsPais = new ArrayList<ExDocumento>();
		listaTodosOsPais.addAll(doc.getTodosOsPaisDasVias());
		
		for (ExDocumento exDocumento : listaTodosOsPais) {
			if((new ExPessoaEstaVinculadaPorPerfil(exDocumento, titular).eval()
					|| new ExLotacaoEstaVinculadoPorPerfil(exDocumento, lotaTitular).eval()) 
					&& this.pattern.matcher(exDocumento.getDnmAcesso()).find()) {
				return true;
			}
		}
		
		for (ExMobil m : getMobilesAVarrer(doc)) {
			if (m.isGeral())
				continue;
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.RECEBIMENTO)
						|| (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.TRANSFERENCIA)) {
					if (mov.getResp() != null &&  titular != null && mov.getResp().equivale(titular) ) {
						return true;
					} 
					if (mov.getLotaResp() != null &&  lotaTitular != null && mov.getLotaResp().equivale(lotaTitular) ) {
						return true;
					} 
				}				
			}
		}
		
		return false;
		
	}
	
	private Set<ExMobil> getMobilesAVarrer(ExDocumento doc) {;
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
	
	public static boolean ehPublicoExterno(DpPessoa titular) {
		return (
				(titular.getOrgaoUsuario().getIsExternoOrgaoUsu() != null && titular.getOrgaoUsuario().getIsExternoOrgaoUsu() == 1)
				|| (titular.getLotacao() != null && titular.getLotacao().getIsExternaLotacao() != null && titular.getLotacao().getIsExternaLotacao() == 1)
				);
	}

}
