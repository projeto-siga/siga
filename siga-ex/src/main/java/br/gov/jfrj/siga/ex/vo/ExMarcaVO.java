package br.gov.jfrj.siga.ex.vo;

import java.util.Date;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMarcaVO {
	Long id;
	Date dtIni;
	Date dtFim;

	Long idMarcador;
	Long idLotacaoIniMarcador;
	String descr;
	CpMarcadorGrupoEnum grupo;
	Integer ordem;
	String descrDetalhada;
	CpMarcadorCorEnum cor;
	CpMarcadorIconeEnum icone;
	CpMarcadorFinalidadeEnum finalidade;

	Long idTipoMarca;
	String descrTipoMarca;

	Long idPessoaIni;
	String siglaPessoa;
	String nomePessoa;
	Long idLotacaoIni;
	String siglaLotacao;
	String nomeLotacao;
	Long idMob;
	String siglaMob;
	String identificadorMob;
	Long idMov;
	Boolean podeCancelarMov;
	String descrMov;
	String descricaoComDatas;

	ExMarcaVO(CpMarca m, DpPessoa titular, DpLotacao lotaTitular) {
		id = m.getIdMarca();
		dtIni = m.getDtIniMarca();
		dtFim = m.getDtFimMarca();
		if (m.getCpMarcador() != null) {
			CpMarcador marcador = m.getCpMarcador();
			idMarcador = marcador.getId();
			if (marcador.getDpLotacaoIni() != null)
				idLotacaoIniMarcador = marcador.getDpLotacaoIni().getId();
			descr = marcador.getDescrMarcador();
			CpMarcadorGrupoEnum grupo = marcador.getIdGrupo();
			Integer ordem = marcador.getOrdem();
			String descrDetalhada = marcador.getDescrDetalhada();
			CpMarcadorCorEnum cor = marcador.getIdCor();
			CpMarcadorIconeEnum icone = marcador.getIdIcone();
			CpMarcadorFinalidadeEnum finalidade = marcador.getIdFinalidade();
		}
		if (m.getCpTipoMarca() != null) {
			idTipoMarca = m.getCpTipoMarca().getIdTpMarca();
			descrTipoMarca = m.getCpTipoMarca().getDescrTipoMarca();
		}
		if (m.getDpPessoaIni() != null) {
			idPessoaIni = m.getDpPessoaIni().getId();
			DpPessoa pes = m.getDpPessoaIni().getPessoaAtual();
			siglaPessoa = pes.getSigla();
			nomePessoa = pes.getNomePessoa();
		}
		if (m.getDpLotacaoIni() != null) {
			idLotacaoIni = m.getDpLotacaoIni().getId();
			DpLotacao pes = m.getDpLotacaoIni().getLotacaoAtual();
			siglaLotacao = pes.getSigla();
			nomeLotacao = pes.getNomeLotacao();
		}
		if (m instanceof ExMarca) {
			ExMarca ex = ((ExMarca) m);
			ExMobil mob = ex.getExMobil();
			if (mob != null) {
				idMob = mob.getId();
				siglaMob = mob.getSigla();
				identificadorMob = mob.isGeral() ? "Geral" : mob.getTerminacaoSigla();
			}
			ExMovimentacao mov = ex.getExMovimentacao();
			if (mov != null) {
				podeCancelarMov = mov.podeCancelar(titular, lotaTitular);
				idMov = mov.getIdMov();
				descrMov = mov.getDescrMov();
			}
			descricaoComDatas = ex.getDescricaoComDatas();
		}

	}
}
