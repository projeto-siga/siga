package br.gov.jfrj.siga.gc.util;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.vraptor.AppController;
import br.gov.jfrj.siga.gc.vraptor.GcInterceptor;
import br.gov.jfrj.siga.gc.vraptor.SigaLogicResult;

public class GcCloud extends Cloud {

	private double maxWeight;
	private double minWeight;

	public GcCloud(double maxWeight, double minWeight) {
		this.maxWeight = maxWeight;
		this.minWeight = minWeight;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public double getMinWeight() {
		return minWeight;
	}

	public void criarCloud(Object[] tag, Long idLotacao) throws Exception {
		GcInformacaoFiltro filtro = new GcInformacaoFiltro();
		filtro.setTag(new GcTag());
		filtro.tag.setSigla((String) (tag[0]));
		filtro.situacao = CpMarcador.AR.findById(CpMarcadorEnum.ATIVO.getId());
		if (idLotacao != null)
			filtro.lotacao = DpLotacao.AR.findById(idLotacao);
		filtro.pesquisa = true;

		StringBuilder sb = new StringBuilder();
 	 	SigaLogicResult router = GcInterceptor.result().use(
 	 			SigaLogicResult.class);
 		router.getRedirectURL(sb, AppController.class).listar(filtro, 0,false, null, null);
		sb.append("/sigagc/app/listar");
		sb.append("?filtro.pesquisa=true");
		sb.append("&filtro.tag.id=" + filtro.getTag().getId());
		sb.append("&filtro.situacao.id=" + filtro.getSituacao().getIdMarcador());
		if (idLotacao != null)
			sb.append("&filtro.lotacao.id=" + filtro.getLotacao().getId());

		String link = sb.toString();
		Tag tagCloud = new Tag(tag[0].toString(), link,
				Double.parseDouble(tag[1].toString())); // tag[1] - contador
		this.addTag(tagCloud);
	}
}
