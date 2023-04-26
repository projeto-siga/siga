package br.gov.jfrj.siga.gc.api.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.gc.api.v1.IGcApiV1.IPainelListaGet;
import br.gov.jfrj.siga.gc.api.v1.IGcApiV1.PainelListaItem;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcMarca;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class PainelListaGet implements IPainelListaGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, GcApiV1Context ctx) throws Exception {
		if (req.idMarcas == null || req.idMarcas.trim().isEmpty())
			return;
		String[] aMarcas = req.idMarcas.split(",");
		List<Long> lMarcas = new ArrayList<>();
		for (String s : aMarcas)
			lMarcas.add(Long.parseLong(s));
		List<Object[]> l = consultarPainelLista(lMarcas);

		for (Object[] o : l) {
			GcInformacao inf = (GcInformacao) o[0];
			GcMarca marca = (GcMarca) o[1];
			PainelListaItem r = new PainelListaItem();

			r.marcaId = marca.getIdMarca().toString();
			r.dataIni = marca.getDtIniMarca();
			r.dataFim = marca.getDtFimMarca();
			r.moduloId = marca.getCpTipoMarca().getIdTpMarca().toString();

			r.tipo = inf.getTipo().getNome();
			r.codigo = inf.getSiglaCompacta();
			r.sigla = inf.getSigla();
			if (inf.getLotacao() != null)
				r.origem = inf.getLotacao().getSigla();
			r.descricao = inf.getDescrCurta();
			r.ultimaAnotacao = null;

			resp.list.add(r);
		}
	}

	public List<Object[]> consultarPainelLista(final List<Long> l) {
		if (l == null || l.size() == 0)
			return null;

		List<Object[]> l2 = new ArrayList<Object[]>();

		Query query = ContextoPersistencia.em().createQuery("select inf, label from GcMarca label"
				+ " inner join label.inf inf" + " where label.idMarca in (:listIdMarca)");
		query.setParameter("listIdMarca", l);
		l2 = query.getResultList();
		Collections.sort(l2, Comparator.comparing(item -> l.indexOf(Long.valueOf(((GcMarca) (item[1])).getIdMarca()))));
		return l2;
	}

	@Override
	public String getContext() {
		return "obter lista de documentos por marcador";
	}

}
