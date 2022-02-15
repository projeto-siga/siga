package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPainelQuadroGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PainelQuadroItem;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PainelQuadroQtdItem;
import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class PainelQuadroGet implements IPainelQuadroGet {

	private static class QuadroItemQtdVO {
		public CpTipoMarca tipo;
		public String filtro;
		public long qtd;

		public QuadroItemQtdVO(CpTipoMarca tipo, String filtro, long qtd) {
			this.tipo = tipo;
			this.filtro = filtro;
			this.qtd = qtd;
		}
	}

	private static class QuadroItemVO {
		public CpMarcadorFinalidadeEnum finalidadeId;
		public CpTipoMarcadorEnum tipoId;
		public CpMarcadorGrupoEnum grupoId;
		public CpMarcadorEnum marcadorEnum;
		public Long marcadorId;
		public String marcadorNome;
		public CpMarcadorIconeEnum marcadorIcone;
		public CpMarcadorCorEnum marcadorCor;
		public List<QuadroItemQtdVO> qtds = new ArrayList<>();
		public Long qtdLotaAtendente;

		public QuadroItemVO(CpMarcadorFinalidadeEnum finalidadeId, CpTipoMarcadorEnum tipoId,
				CpMarcadorGrupoEnum grupoId, CpMarcadorEnum marcadorEnum, Long marcadorId, String marcadorNome,
				CpMarcadorIconeEnum marcadorIcone, CpMarcadorCorEnum marcadorCor) {
			super();
			this.finalidadeId = finalidadeId;
			this.tipoId = tipoId;
			this.grupoId = grupoId;
			this.marcadorEnum = marcadorEnum;
			this.marcadorId = marcadorId;
			this.marcadorNome = marcadorNome;
			this.marcadorIcone = marcadorIcone;
			this.marcadorCor = marcadorCor;
		}

		public void addQtd(CpTipoMarca tipo, String filtro, long qtd) {
			this.qtds.add(new QuadroItemQtdVO(tipo, filtro, qtd));
		}

		public static QuadroItemVO fromObjectArray(Object[] i, QuadroItemVO previous) {
			CpMarcadorFinalidadeEnum finalidade = (CpMarcadorFinalidadeEnum) i[2];
			Long marcadorId = (Long) i[0];
			CpMarcadorEnum marcadorEnum = CpMarcadorEnum.getById(marcadorId != null ? marcadorId.intValue() : null);

			QuadroItemVO item = previous;
			if (item == null || !marcadorId.equals(previous.marcadorId))
				item = new QuadroItemVO(finalidade, finalidade.getIdTpMarcador(),
						marcadorEnum != null ? marcadorEnum.getGrupo() : null, marcadorEnum, marcadorId,
						marcadorEnum != null && !marcadorEnum.getNome().startsWith("???") ? marcadorEnum.getNome()
								: (String) i[1],
						(CpMarcadorIconeEnum) i[5], (CpMarcadorCorEnum) i[4]);
			CpTipoMarca tipoMarca = (CpTipoMarca) i[6];

			Long qtdTotal = (Long) i[7];
			Long qtdAtendente = (Long) i[8];
			Long qtdLotaAtendente = (Long) i[9];
			Long qtdNaoAtribuido = (Long) i[10];

			if (qtdTotal != null && qtdTotal != 0L)
				item.qtds.add(new QuadroItemQtdVO(tipoMarca, "TOTAL", qtdTotal));
			if (qtdAtendente != null && qtdAtendente != 0L)
				item.qtds.add(new QuadroItemQtdVO(tipoMarca, "PESSOA", qtdAtendente));
			if (qtdLotaAtendente != null && qtdLotaAtendente != 0L)
				item.qtds.add(new QuadroItemQtdVO(tipoMarca, "LOTACAO", qtdLotaAtendente));
			if (qtdNaoAtribuido != null && qtdNaoAtribuido != 0L)
				item.qtds.add(new QuadroItemQtdVO(tipoMarca, "NAO_ATRIBUIDO", qtdNaoAtribuido));
			return item;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		CpTipoMarca cpTipoMarca = CpTipoMarca.findByName(req.tipoMarca);

		List<Object[]> listEstados = CpDao.getInstance().consultarPainelQuadro(ctx.getTitular(), ctx.getLotaTitular(),
				cpTipoMarca);

		List<QuadroItemVO> items = new ArrayList<>();
		QuadroItemVO previous = null;
		for (Object[] i : listEstados) {
			QuadroItemVO item = QuadroItemVO.fromObjectArray(i, previous);
			if (item != previous)
				items.add(item);
			previous = item;
		}

		if (req.estilo != null && "AGRUPADOS".equals(req.estilo.toUpperCase())) {
			items.sort(new Comparator<QuadroItemVO>() {

				@Override
				public int compare(QuadroItemVO o1, QuadroItemVO o2) {
					int i = 0;
					i = br.gov.jfrj.siga.base.util.Utils.comparar(o2.finalidadeId.getGrupo(),
							o1.finalidadeId.getGrupo());
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
			PainelQuadroItem r = new PainelQuadroItem();
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
			for (QuadroItemQtdVO q : i.qtds) {
				if (q.qtd != 0L) {
					PainelQuadroQtdItem qtd = new PainelQuadroQtdItem();
					qtd.tipo = q.tipo.getDescrTipoMarca().replace("-", "_");
					qtd.filtro = q.filtro;
					qtd.qtd = Long.toString(q.qtd);
					r.qtds.add(qtd);
				}
			}
			resp.list.add(r);
		}
	}

	@Override
	public String getContext() {
		return "obter quadro do painel";
	}

}
