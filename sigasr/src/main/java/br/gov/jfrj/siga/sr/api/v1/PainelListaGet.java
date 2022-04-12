package br.gov.jfrj.siga.sr.api.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.api.v1.ISrApiV1.IPainelListaGet;
import br.gov.jfrj.siga.sr.api.v1.ISrApiV1.PainelListaItem;
import br.gov.jfrj.siga.sr.model.SrMarca;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class PainelListaGet implements IPainelListaGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, SrApiV1Context ctx) throws Exception {
		if (req.idMarcas == null || req.idMarcas.trim().isEmpty())
			return;
		String[] aMarcas = req.idMarcas.split(",");
		List<Long> lMarcas = new ArrayList<>();
		for (String s : aMarcas)
			lMarcas.add(Long.parseLong(s));
		List<Object[]> l = consultarPainelLista(lMarcas);

		for (Object[] o : l) {
			SrSolicitacao sol = (SrSolicitacao) o[0];
			SrMarca marca = (SrMarca) o[1];
			PainelListaItem r = new PainelListaItem();

			r.marcaId = marca.getIdMarca().toString();
			r.dataIni = marca.getDtIniMarca();
			r.dataFim = marca.getDtFimMarca();
			r.moduloId = marca.getCpTipoMarca().getIdTpMarca().toString();

			r.tipo = "Solicitação";
			r.codigo = sol.getSiglaCompacta();
			r.sigla = sol.getSigla();
			if (sol.getLotaSolicitante() != null)
				r.origem = sol.getLotaSolicitante().getSigla();
			r.descricao = sol.getDescricaoMax40();
			r.ultimaAnotacao = null;

			resp.list.add(r);
		}
	}

	public List<Object[]> consultarPainelLista(final List<Long> l) {
		if (l == null || l.size() == 0)
			return null;

		List<Object[]> l2 = new ArrayList<Object[]>();

		Query query = ContextoPersistencia.em().createQuery("select sol, label from SrMarca label"
				+ " inner join label.solicitacao sol" + " where label.idMarca in (:listIdMarca)");
		query.setParameter("listIdMarca", l);
		l2 = query.getResultList();
		Collections.sort(l2, Comparator.comparing(item -> l.indexOf(Long.valueOf(((SrMarca) (item[1])).getIdMarca()))));
		return l2;
	}

	@Override
	public String getContext() {
		return "obter lista de documentos por marcador";
	}

}
