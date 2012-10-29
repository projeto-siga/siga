package controllers;

import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpCompetenciaBL;
import br.gov.jfrj.siga.cp.bl.CpPropriedadeBL;

public class Sr extends
		Cp<SrConfiguracaoBL, SrCompetenciaBL, SrBL, CpPropriedadeBL> {

	/**
	 * Retorna uma instância do sistema de workflow. Através dessa instância é
	 * possível acessar a lógica de negócio, competências e configurações do
	 * sistema de workflow.
	 * 
	 * @return Instância de workflow
	 */
	public static Sr getInstance() {
		if (!isInstantiated()) {
			synchronized (Cp.class) {
				if (!isInstantiated()) {
					Sr instance = new Sr();
					setInstance(instance);
					instance.setConf(new SrConfiguracaoBL());
					instance.getConf().setComparator(
							new SrConfiguracaoComparator());
					instance.setComp(new SrCompetenciaBL());
					instance.getComp().setConfiguracaoBL(instance.getConf());
					instance.setBL(new SrBL());
					instance.getBL().setComp(instance.getComp());
					instance.setProp(new CpPropriedadeBL());
				}
			}
		}
		return (Sr) Cp.getInstance();
	}
}
