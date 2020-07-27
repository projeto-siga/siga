package br.gov.jfrj.siga.ex.bl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

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
		
		if (doc.getSubscritor() != null && doc.getSubscritor().equals(titular)) return true;
		
		if (doc.getCadastrante().equals(titular)) return true;
		
		if (doc.getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao mov : doc.getMobilGeral()
					.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if ((mov.getExTipoMovimentacao()
								.getIdTpMov()
								.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL))
				  || (mov.getExTipoMovimentacao()
						  		.getIdTpMov()
						  		.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO))) {
					if (mov.getSubscritor() != null &&  titular != null && mov.getSubscritor() == titular ) {
						return true;
					} 
					if (mov.getLotaSubscritor() != null &&  lotaTitular != null && mov.getLotaSubscritor() == lotaTitular ) {
						return true;
					} 
				}
			}
		}
		
		for (ExMobil m : getMobilesAVarrer(doc)) {
			if (m.isGeral())
				continue;
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.isCanceladora())
					continue;
				if ((mov.getExTipoMovimentacao()
						.getIdTpMov()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO))
		  || (mov.getExTipoMovimentacao()
				  		.getIdTpMov()
				  		.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA))) {
			if (mov.getResp() != null &&  titular != null && mov.getResp() == titular ) {
				return true;
			} 
			if (mov.getLotaResp() != null &&  lotaTitular != null && mov.getLotaResp() == lotaTitular ) {
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
