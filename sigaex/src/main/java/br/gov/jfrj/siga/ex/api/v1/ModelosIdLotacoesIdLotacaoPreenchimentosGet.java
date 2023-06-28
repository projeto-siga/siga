package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosIdLotacoesIdLotacaoPreenchimentosGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PreenchimentoItem;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ModelosIdLotacoesIdLotacaoPreenchimentosGet implements IModelosIdLotacoesIdLotacaoPreenchimentosGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		//novo código
		//lista todos os preenchimentos, independente da lotação selecionada
		resp.list = listarPreenchimentosSemFiltroLotacao(Long.parseLong(req.id));
		
		//código original
		//lista os preenchimentos filtrados por Lotação
		//resp.list = listarPreenchimentos(Long.parseLong(req.id), Long.parseLong(req.idLotacao));
		
		
	}
	
	// Validei que esse é o código executado quando o usuário executa a seguinte operação
		/*
		 * USUÁRIO SELECIONA FILTRO POR LOTAÇÃO
		 * 
		 */
	
	//Lista todos os preenchimentos de 1 lotação específica.
	
	//URL: https://sigatms.jfrj.jus.br/sigaex/api/v1/modelos/82210/lotacoes/86761/preenchimentos
	//GET
	
	//public static List<PreenchimentoItem> listarPreenchimentos(Long idModelo, Long idLotacao) {
	public static List<PreenchimentoItem> listarPreenchimentos(Long idModelo, Long idLotacao) {
		//se tirar o idLotacao do filtro, ele deve listar todos
		// faz select pelo modelo e lotação
		//após alteração deve exibir o resultado sem o filtro de lotação
		//o resultado com o filtro de lotação deve ser exibido 1º
		//
		//na sequencia, exibir todas as outras opções em ordem alfabetica
		//qual o filtro que mostra todos?
		ExDao dao = ExDao.getInstance();
		ExModelo mod = dao.consultar(idModelo, ExModelo.class, false);
		mod = dao.consultar(mod.getIdInicial(), ExModelo.class, false);
		DpLotacao lota = dao.consultar(idLotacao, DpLotacao.class, false);
		lota = dao.consultar(lota.getIdInicial(), DpLotacao.class, false);

		ExPreenchimento filtro = new ExPreenchimento();
		filtro.setExModelo(mod);
		
		//alterando a linha abaixo tira o filtro de lotação
		filtro.setDpLotacao(lota);
		List<ExPreenchimento> l = dao.consultar(filtro);
		List<PreenchimentoItem> list = new ArrayList<>();
		for (ExPreenchimento i : l) {
			PreenchimentoItem item = new PreenchimentoItem();
			item.idPreenchimento = Long.toString(i.getIdPreenchimento());
			item.nome = i.getNomePreenchimento();
			list.add(item);
		}
		
		
		/*
		Ajuste proposto:

			Na seleção do texto padrão, exibir todos os textos existentes, independentemente da lotação selecionada, conforme já ocorre ao selecionar como responsável a "Lotação do titular".(AO [TIPO_RESPONSAVEL] = TITULAR)

			Retornar a lista em ordem alfabética, filtrando o resultado a cada letra digitada (como já ocorre na seleção do modelo), porém, trazendo os textos padrão da lotação responsável pela tarefa na frente e os das demais lotações na sequencia, conforme exemplo abaixo:
		*/
		//System.out.println("########################################");
		//System.out.println("Lista todos os preenchimentos de 1 lotação específica.");
		//System.out.println("https://sigatms.jfrj.jus.br/sigaex/api/v1/modelos/82210/lotacoes/86761/preenchimentos");
		//System.out.println("########################################");
		return list;
	}
	
	// Validei que esse é o código executado quando o usuário executa a seguinte operação
		/*
		 * Na seleção do texto padrão, exibir todos os textos existentes, 
		 * independentemente da lotação selecionada, 
		 * conforme já ocorre ao selecionar como responsável a "Lotação do titular".
		 * 
		 */
		
		
		//Lista os preenchimentos sem filtrar por lotações
		//filtra apenas por modelos
		public static List<PreenchimentoItem> listarPreenchimentosSemFiltroLotacao(Long idModelo) {
			ExDao dao = ExDao.getInstance();
			ExModelo mod = dao.consultar(idModelo, ExModelo.class, false);
			mod = dao.consultar(mod.getIdInicial(), ExModelo.class, false);

			ExPreenchimento filtro = new ExPreenchimento();
			filtro.setExModelo(mod);
			List<ExPreenchimento> l = dao.consultar(filtro);
			List<PreenchimentoItem> list = new ArrayList<>();
			for (ExPreenchimento i : l) {
				PreenchimentoItem item = new PreenchimentoItem();
				item.idPreenchimento = Long.toString(i.getIdPreenchimento());
				item.nome = i.getNomePreenchimento();
				list.add(item);
			}
			
			//System.out.println("########################################");
			//System.out.println("Lista todos os preenchimentos de todas as lotações:");
			//System.out.println("https://sigatms.jfrj.jus.br/sigaex/api/v1/modelos/82210/preenchimentos");
			//System.out.println("########################################");
			return list;
		}

	@Override
	public String getContext() {
		return "obter preenchimentos";
	}
}
