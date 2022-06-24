package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IQuadroGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.QuadroItem;
import br.gov.jfrj.siga.hibernate.ExDao;

public class QuadroGet implements IQuadroGet {

	private static class QuadroItemVO {
		public CpMarcadorFinalidadeEnum finalidadeId;
		public CpTipoMarcadorEnum tipoId;
		public CpMarcadorGrupoEnum grupoId;
		public CpMarcadorEnum marcadorEnum;
		public Long marcadorId;
		public String marcadorNome;
		public CpMarcadorIconeEnum marcadorIcone;
		public CpMarcadorCorEnum marcadorCor;
		public Long qtdAtendente;
		public Long qtdLotaAtendente;

		public QuadroItemVO(CpMarcadorFinalidadeEnum finalidadeId, CpTipoMarcadorEnum tipoId,
				CpMarcadorGrupoEnum grupoId, CpMarcadorEnum marcadorEnum, Long marcadorId, String marcadorNome,
				CpMarcadorIconeEnum marcadorIcone, CpMarcadorCorEnum marcadorCor, Long qtdAtendente,
				Long qtdLotaAtendente) {
			super();
			this.finalidadeId = finalidadeId;
			this.tipoId = tipoId;
			this.grupoId = grupoId;
			this.marcadorEnum = marcadorEnum;
			this.marcadorId = marcadorId;
			this.marcadorNome = marcadorNome;
			this.marcadorIcone = marcadorIcone;
			this.marcadorCor = marcadorCor;
			this.qtdAtendente = qtdAtendente;
			this.qtdLotaAtendente = qtdLotaAtendente;
		}

		public static QuadroItemVO fromObjectArray(Object[] i) {
			CpMarcadorFinalidadeEnum finalidade = (CpMarcadorFinalidadeEnum) i[4];
			Long marcadorId = (Long) i[0];
			CpMarcadorEnum marcadorEnum = CpMarcadorEnum.getById(marcadorId != null ? marcadorId.intValue() : null);
			return new QuadroItemVO(finalidade, finalidade.getIdTpMarcador(),
					marcadorEnum != null ? marcadorEnum.getGrupo() : null, marcadorEnum, marcadorId, (String) i[1],
					(CpMarcadorIconeEnum) i[7], (CpMarcadorCorEnum) i[6], (Long) i[2], (Long) i[3]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		Integer idTpFormaDoc = 0;
		if (req.filtroExpedienteProcesso != null) {
			switch (req.filtroExpedienteProcesso) {
			case "Expediente":
				idTpFormaDoc = 1;
				break;
			case "Processo":
				idTpFormaDoc = 2;
				break;
			}
		}

		List<Object[]> listEstados = ExDao.getInstance().consultarPaginaInicial(ctx.getTitular(), ctx.getLotaTitular(),
				idTpFormaDoc);

		List<QuadroItemVO> items = new ArrayList<>();
		for (Object[] i : listEstados) {
			items.add(QuadroItemVO.fromObjectArray(i));
		}

		if (req.estilo != null && "AGRUPADOS".equals(req.estilo.toUpperCase())) {
			items.sort(new Comparator<QuadroItemVO>() {

				@Override
				public int compare(QuadroItemVO o1, QuadroItemVO o2) {
					int i = 0;
					i = br.gov.jfrj.siga.base.util.Utils.comparar(o2.finalidadeId.getGrupo(), o1.finalidadeId.getGrupo());
					if (i != 0)
						return i;
					i = br.gov.jfrj.siga.base.util.Utils.comparar(o1.grupoId, o2.grupoId);
					if (i != 0)
						return i;
					return i;
				}

			});

		}

		for (QuadroItemVO i : items) {
			QuadroItem r = new QuadroItem();
			r.marcadorId = i.marcadorId.toString();
			r.marcadorNome = i.marcadorNome;
			r.marcadorEnum = i.marcadorEnum != null ? i.marcadorEnum.name() : null;
			CpMarcadorCorEnum cor = i.marcadorCor;
			if (cor != null)
				r.marcadorCor = cor.name().substring(4);
			CpMarcadorIconeEnum icone = i.marcadorIcone;
			if (icone != null)
				r.marcadorIcone = icone.getCodigoFontAwesome();

			CpMarcadorFinalidadeEnum finalidade = i.finalidadeId;
			if (finalidade != null) {
				r.finalidadeId = finalidade.name();
				r.tipoId = finalidade.getGrupo().name();
				r.tipoNome = finalidade.getGrupo().getNome();
			}
			if (i.marcadorEnum != null) {
				r.grupoId = i.marcadorEnum.getGrupo().name();
				r.grupoNome = i.marcadorEnum.getGrupo().getNome();
			}
			r.qtdAtendente = i.qtdAtendente.toString();
			r.qtdLotaAtendente = i.qtdLotaAtendente.toString();

			resp.list.add(r);
		}
	}

	@Override
	public String getContext() {
		return "obter quadro quantitativo";
	}

}
