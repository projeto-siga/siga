package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class NivelDeAcessoUtil {
	public static List<ExNivelAcesso> getListaNivelAcesso(ExTipoDocumento exTpDoc, ExFormaDocumento forma,
			ExModelo exMod, ExClassificacao classif, DpPessoa titular, DpLotacao lotaTitular) {
		List<ExNivelAcesso> listaNiveis = ExDao.getInstance().listarOrdemNivel();
		ArrayList<ExNivelAcesso> niveisFinal = new ArrayList<ExNivelAcesso>();
		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		ExConfiguracao config = new ExConfiguracao();
		config.setDpPessoa(titular);
		config.setLotacao(lotaTitular);
		config.setExTipoDocumento(exTpDoc);
		config.setExFormaDocumento(forma);
		config.setExModelo(exMod);
		config.setExClassificacao(classif);

		ExConfiguracaoCache exConfiguracaoMin;
		config.setCpTipoConfiguracao(ExTipoDeConfiguracao.NIVEL_ACESSO_MINIMO);
		try {
			exConfiguracaoMin = (ExConfiguracaoCache) Ex.getInstance().getConf().buscaConfiguracao(config,
					new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt);
		} catch (Exception e) {
			exConfiguracaoMin = null;
		}

		ExConfiguracaoCache exConfiguracaoMax;
		config.setCpTipoConfiguracao(ExTipoDeConfiguracao.NIVEL_ACESSO_MAXIMO);
		try {
			exConfiguracaoMax = (ExConfiguracaoCache) Ex.getInstance().getConf().buscaConfiguracao(config,
					new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt);
		} catch (Exception e) {
			exConfiguracaoMax = null;
		}

		if (exConfiguracaoMin != null && exConfiguracaoMax != null && exConfiguracaoMin.exNivelAcesso != 0
				&& exConfiguracaoMax.exNivelAcesso != 0) {
			int nivelMinimo = 0;
			int nivelMaximo = 0;
			for (ExNivelAcesso nivelAcesso : listaNiveis) {
				if (nivelAcesso.getIdNivelAcesso().equals(exConfiguracaoMin.exNivelAcesso))
					nivelMinimo = nivelAcesso.getGrauNivelAcesso();
				if (nivelAcesso.getIdNivelAcesso().equals(exConfiguracaoMax.exNivelAcesso))
					nivelMaximo = nivelAcesso.getGrauNivelAcesso();
			}

			for (ExNivelAcesso nivelAcesso : listaNiveis) {
				if (nivelAcesso.getGrauNivelAcesso() >= nivelMinimo
						&& nivelAcesso.getGrauNivelAcesso() <= nivelMaximo) {
					niveisFinal.add(nivelAcesso);
				}
			}
		} else {
			niveisFinal.addAll(listaNiveis);
		}

		return niveisFinal;
	}
}
