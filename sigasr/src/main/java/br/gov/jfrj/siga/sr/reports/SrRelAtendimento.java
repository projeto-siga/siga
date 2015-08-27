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
import br.gov.jfrj.siga.sr.model.SrAtendimento;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class SrRelAtendimento extends RelatorioTemplate {

	public SrRelAtendimento(Map parametros) throws DJBuilderException {
		super(parametros);
		if ((Long) parametros.get("idlotaAtendenteIni") == 0L && parametros.get("siglaLotacao") == null
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
		this.addColuna("Equipe Atendente", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Pessoa Atendente", 18, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data de Início Atendimento", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Data de Fim Atendimento", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Tipo de Atendimento", 28, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Próximo Atendente", 15, RelatorioRapido.ESQUERDA, false);
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
		List<Object> listaFinal = new LinkedList<Object>();
		Set<SrAtendimento> listaAtendimento = null;
		Set<SrAtendimento> listaAtendimentoTotal = new TreeSet<SrAtendimento>();
		List<SrSolicitacao> lista = null;
		List<Long> idsIniciais = new ArrayList<Long>();
		int quantRegistros = 1; int tamanhoLista = 0;
		Long  idlotaAtendenteIni = (Long) parametros.get("idlotaAtendenteIni");
		String tipo = (String) parametros.get("tipo");
		
		try {
			if (tipo.equals("lotacao")) 
				idsIniciais.add(idlotaAtendenteIni);
			else if (tipo.equals("lista_lotacao")) 
				idsIniciais = listarLotacoesPorSigla();
			else if (tipo.equals("expressao"))
				idsIniciais = listarLotacoesPorExpressao();

			lista = consultarAtendimentoPorLotacao(idsIniciais);
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
		return listaFinal;
	}

	@SuppressWarnings("unchecked")
	private List<SrSolicitacao> consultarAtendimentoPorLotacao(List<Long> ids) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
		//movimentacao de inicio de atendimento (tipo = 1), fechamento (tipo = 7), escalonamento (tipo = 24)  			
		Query query = SrSolicitacao.AR.em().createQuery("select sol from SrSolicitacao sol inner join sol.meuMarcaSet marca "
				+ "where marca.cpMarcador.idMarcador <> 45 and sol.idSolicitacao in ("
					+ "select s.idSolicitacao from SrSolicitacao s inner join s.meuMovimentacaoSet mov "
					+ "where s.hisDtIni between :dataIni and :dataFim and (mov.tipoMov = 1 or mov.tipoMov = 7 or mov.tipoMov = 24) "
					+ "and mov.lotaAtendente.idLotacaoIni in :idsLotaAtendenteIni group by s.idSolicitacao) "
				+ "order by sol.hisDtIni");

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