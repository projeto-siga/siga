package br.gov.jfrj.siga.sr.reports;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import edu.emory.mathcs.backport.java.util.Arrays;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.sr.model.SrEtapaSolicitacao;
import br.gov.jfrj.siga.sr.model.SrEtapaSolicitacaoComparator;
import br.gov.jfrj.siga.sr.model.SrParametro;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class SrRelAtendimento extends RelatorioTemplate {

	public SrRelAtendimento(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("lotaAtendente") == null && parametros.get("siglaLotacao") == null
				&& parametros.get("listaLotacoes") == null ) {
			throw new DJBuilderException("Parâmetro Lotação não informado!");
		}
		if (parametros.get("dtIni") == null || parametros.get("dtIni").equals("")) {
			throw new DJBuilderException("Parâmetro data inicial não informado!");
		}
		if (parametros.get("dtFim") == null || parametros.get("dtFim").equals("")) {
			throw new DJBuilderException("Parâmetro data final não informado!");
		}
}
    
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, ColumnBuilderException {
		
		this.setTitle("Relatório de Atendimentos");
		this.addColuna("Solicitação", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data de Abertura", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Descrição", 90, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Equipe Atendente", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Pessoa Atendente", 18, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data de Início Atendimento", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Data de Fim Atendimento", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Tipo de Atendimento", 28, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Prioridade", 20, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Item de Configuração", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Ação", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Solicitação Fechada?", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Tempo de Atendimento", 26, RelatorioRapido.DIREITA, false);
		this.addColuna("Faixa", 18, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Totalizador (%)", 14, RelatorioRapido.DIREITA, false, Number.class);
		
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection processarDados() throws Exception {
		List<Object> registros = new LinkedList<Object>();
		Set<SrEtapaSolicitacao> etapas = new TreeSet<SrEtapaSolicitacao>();
		Set<SrEtapaSolicitacao> etapasOrdenadas = new TreeSet<SrEtapaSolicitacao>(new SrEtapaSolicitacaoComparator());
		List<Long> idsIniciais = new ArrayList<Long>();
		int contadorDeRegistro = 1; int totalDeRegistros = 0;
		DpLotacao  lotaAtendente = (DpLotacao) parametros.get("lotaAtendente");
		String tipo = (String) parametros.get("tipo");
		
		try {
			if (tipo.equals("lotacao")) 
				idsIniciais.add(lotaAtendente.getIdLotacaoIni());
			else if (tipo.equals("lista_lotacao")) 
				idsIniciais = listarLotacoesPorSigla();
			else if (tipo.equals("expressao"))
				idsIniciais = listarLotacoesPorExpressao();

			List<SrSolicitacao> lista = consultarAtendimentoPorLotacao(idsIniciais);
			//juntando todas as etapas de todas as solicitações
			for (SrSolicitacao sol : lista) 
				etapas.addAll(sol.getEtapas());
			//filtrando as etapas 	
			for (SrEtapaSolicitacao e : etapas) {
				if (e.getParametro().equals(SrParametro.ATENDIMENTO) &&
						idsIniciais.contains(e.getLotaResponsavel().getIdInicial())) 
					etapasOrdenadas.add(e);
			}
			//adicionando os registros do relatório
			totalDeRegistros = etapasOrdenadas.size();
			for (SrEtapaSolicitacao etapa : etapasOrdenadas) {
				registros.add(etapa.getSolicitacao().getCodigo());
				registros.add(etapa.getSolicitacao().getHisDtIniDDMMYYYYHHMM());
				registros.add(etapa.getSolicitacao().getDescricaoSemQuebraDeLinha());
				registros.add(etapa.getLotaResponsavel().getLotacaoAtual().getSiglaCompleta()); 
				registros.add(etapa.getPessoaResponsavel() != null ?  etapa.getPessoaResponsavel().getPessoaAtual().getPrimeiroNomeEIniciais() : "");
				registros.add(etapa.getInicioString());
				registros.add(etapa.getFimString());
				registros.add(etapa.getTipoMov() != null ? etapa.getTipoMov().getNome() : "");
				registros.add(etapa.getPrioridade().getDescPrioridade());
				registros.add(etapa.getItem() != null ? etapa.getItem().toString() : "");
				registros.add(etapa.getAcao() != null ? etapa.getAcao().toString() : "");
				registros.add(etapa.getSolicitacao().isFechado() ? "Sim" : "Nao");
				registros.add(etapa.getValorRealizado().toString());
				registros.add(etapa.getFaixa().getDescricao());
				registros.add(Float.parseFloat(String.format(Locale.US,"%.2f", 
						(contadorDeRegistro * 100f)/totalDeRegistros)));
				contadorDeRegistro++;
			}
			/*for (SrSolicitacao sol : lista) {
				if (sol.isPai())
					listaAtendimento = sol.getAtendimentosSetSolicitacaoPai();
				else 
					listaAtendimento = sol.getAtendimentosSet();	
				for (SrAtendimento a : listaAtendimento) {
					if (idsIniciais.contains(a.getLotacaoAtendente().getIdInicial()) &&
						a.getTempoAtendimento() != null) {
						listaAtendimentoTotal.add(a);
					}
				}
			}
			tamanhoLista = listaAtendimentoTotal.size();
			for (SrAtendimento atendimento : listaAtendimentoTotal) {
				listaFinal.add(atendimento.getSolicitacao().getCodigo());
				listaFinal.add(atendimento.getSolicitacao().getHisDtIniDDMMYYYYHHMM());
				listaFinal.add(atendimento.getLotacaoAtendente().getSiglaCompleta());
				listaFinal.add(atendimento.getPessoaAtendente() != null ? 
						atendimento.getPessoaAtendente().getPrimeiroNomeEIniciais() : "");
				listaFinal.add(atendimento.getDataInicioDDMMYYYYHHMMSS());
				listaFinal.add(atendimento.getDataFinalDDMMYYYYHHMMSS());
				listaFinal.add(atendimento.getTipoAtendimento());
				listaFinal.add(atendimento.getLotacaoAtendenteDestino() != null ? 
						atendimento.getLotacaoAtendenteDestino().getSiglaCompleta() : "");
				listaFinal.add(atendimento.getItemConfiguracao());
				listaFinal.add(atendimento.getAcao());
				listaFinal.add(atendimento.getSolicitacao().isFechado() ? "Sim" : "Nao" );
				listaFinal.add(atendimento.getTempoAtendimento().toString());
				listaFinal.add(atendimento.getFaixa().descricao);
				listaFinal.add(Float.parseFloat(String.format(Locale.US,"%.2f", 
						(quantRegistros * 100f)/tamanhoLista)));
				quantRegistros++;
			}*/
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
			//throw new AplicacaoException("Erro ao gerar o relatorio de atendimentos"); 
		}
		return registros;
	}

	@SuppressWarnings("unchecked")
	private List<SrSolicitacao> consultarAtendimentoPorLotacao(List<Long> ids) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
		//movimentacao de inicio de atendimento (tipo = 1), fechamento (tipo = 7), escalonamento (tipo = 24)  			
		Query query = SrSolicitacao.AR.em().createQuery("select s from SrSolicitacao s where s.idSolicitacao in ("
				+ "select distinct (sol.idSolicitacao) from SrSolicitacao sol "
				+ "inner join sol.meuMovimentacaoSet mov inner join sol.meuMarcaSet marca "
				+ "where marca.cpMarcador.idMarcador <> 45 and sol.hisDtIni between :dataIni and :dataFim "
					+ "and (mov.tipoMov = 1 or mov.tipoMov = 7 or mov.tipoMov = 24) "
					+ "and mov.lotaAtendente.idLotacaoIni in (:idsLotaAtendenteIni))");

		Date dtIni = formatter.parse((String) parametros.get("dtIni") + " 00:00:00");
		query.setParameter("dataIni", dtIni, TemporalType.TIMESTAMP);
		Date dtFim = formatter.parse((String) parametros.get("dtFim") + " 23:59:59");
		query.setParameter("dataFim", dtFim, TemporalType.TIMESTAMP);
		query.setParameter("idsLotaAtendenteIni", ids);
			
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> listarLotacoesPorSigla() {
		String listaLotacoes = (String) parametros.get("listaLotacoes");
		String[] siglas = listaLotacoes.trim().toUpperCase().split(";");
		
		Query query = DpLotacao.AR.em().createQuery("select idLotacaoIni from DpLotacao where "
				+ "dataFimLotacao = null and siglaLotacao in :siglasLotacao and orgaoUsuario.idOrgaoUsu = :idOrgao");
		query.setParameter("siglasLotacao", Arrays.asList(siglas));
		query.setParameter("idOrgao", (Long) parametros.get("idOrgao"));
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> listarLotacoesPorExpressao() {
		String siglaModificada = null;
		String sigla = (String) parametros.get("siglaLotacao");
		if(sigla.indexOf('*') >= 0)
			siglaModificada = sigla.trim().toUpperCase().replace('*', '%');
		
		Query query = DpLotacao.AR.em().createQuery("select idLotacaoIni from DpLotacao where "
				+ "dataFimLotacao = null and siglaLotacao like :sigla and orgaoUsuario.idOrgaoUsu = :idOrgao");
		query.setParameter("sigla", siglaModificada);
		query.setParameter("idOrgao", (Long) parametros.get("idOrgao"));
		return query.getResultList();
	}
		
}