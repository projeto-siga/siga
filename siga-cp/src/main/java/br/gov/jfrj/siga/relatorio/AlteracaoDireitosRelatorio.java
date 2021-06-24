/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.relatorio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

public class AlteracaoDireitosRelatorio extends RelatorioTemplate {
	private Date dtInicio;
	private Date dtFim;
	private CpOrgaoUsuario cpOrgaoUsuario;

	public AlteracaoDireitosRelatorio(Map<String, String> parametros)
			throws DJBuilderException {
		super(parametros);
		if (parametros.get("dataInicio") == null) {
			throw new DJBuilderException(
					"Parâmetro data de início não informada!");
		}
		if (parametros.get("dataFim") == null) {
			throw new DJBuilderException("Parâmetro data de fim não informada!");
		}
		String t_strDataHoraIni = (String) parametros.get("dataInicio")
				+ " 00:00:00";
		Date t_dtaDataHoraIni;
		try {
			t_dtaDataHoraIni = converteParaData(t_strDataHoraIni);
		} catch (Exception e) {
			throw new DJBuilderException(
					"Data de início tem de estar no formato 'DD/MM/AAAA'!");
		}
		String t_strDataHoraFim = (String) parametros.get("dataFim")
				+ " 23:59:59";
		Date t_dtaDataHoraFim;
		try {
			t_dtaDataHoraFim = converteParaData(t_strDataHoraFim);
		} catch (Exception e) {
			throw new DJBuilderException(
					"Data de fim tem de estar no formato 'DD/MM/AAAA'!");
		}

		try {
			Long idOrg = Long.parseLong((String) parametros
					.get("idOrgaoUsuario"));
			setCpOrgaoUsuario(dao().consultar(idOrg, CpOrgaoUsuario.class,
					false));
		} catch (Exception e) {
			throw new DJBuilderException("Orgao Usuario inválido ! erro:"
					+ e.getMessage());
		}
		setDtInicio(t_dtaDataHoraIni);
		setDtFim(t_dtaDataHoraFim);
		parametros.put("titulo","SIGA");
		parametros.put("subtitulo","Sistema de Gestão Administrativa");
		parametros.put("secaoUsuario", "");
		if ( Prop.get("/siga.relat.brasao")  == null ) {
			parametros.put("brasao","brasao.png");
		} else {
			parametros.put("brasao", Prop.get("/siga.relat.brasao") );
		}
		//System.out.println("Brasao: " + parametros.get("brasao"));
		// this.setPageSizeAndOrientation(Page.Page_A4_Landscape());
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {// jar:file:/fullpath/main.jar!/a.resource
		//this.setTemplateFile(null);
		this.setTitle("Alteracao de Direitos - de "
				+ parametros.get("dataInicio") + " até "
				+ parametros.get("dataFim"));
		this.addColuna("Pessoa", 40, RelatorioRapido.ESQUERDA, false, false);
		this.addColuna("Serviço", 40, RelatorioRapido.ESQUERDA, false, false);
		this.addColuna("Situação Inicial", 10, RelatorioRapido.ESQUERDA, false,
				false);
		this.addColuna("Situação Final", 10, RelatorioRapido.ESQUERDA, false,
				false);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection processarDados() {
		ArrayList<String> dados = new ArrayList<String>();
		try {
			List<Item> list = compararDasPessoasDoOrgaoNoPeriodo(
					getCpOrgaoUsuario(), getDtInicio(), getDtFim());
			for (Item itm : list) {
				processarItem(itm, dados);
			}
		} catch (Exception e) {
			throw new Error(e);
		}
		// descomentar linha abaixo para testar no JUNIT
		// gerarArquivoDados(dados, "c:\\ArquivoDados.txt");
		return dados;
	}

	@SuppressWarnings("unused")
	private void gerarArquivoDados(ArrayList<String> dados, String nomeArq) {
		final int QUANTOS_CAMPOS = 4;
		File file = new File(nomeArq);
		try {
			Writer output = new BufferedWriter(new FileWriter(file));
			int contaCampos = 0;
			for (String campo : dados) {
				contaCampos++;
				output.write(campo);
				if (contaCampos < QUANTOS_CAMPOS) {
					output.write("\t");

				} else {
					output.write("\r\n");
					contaCampos = 0;
				}
			}
			output.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Preenche os dados com as informações da configuração já formatados
	 * 
	 * @param cfga
	 *            - Configuração acesso
	 * @param dados
	 *            - coleção de linhas do relatório
	 */
	private void processarItem(Item itm, List<String> dados) {

		AlteracaoDireitosItem antigo = (AlteracaoDireitosItem) itm.getAntigo();
		AlteracaoDireitosItem novo = (AlteracaoDireitosItem) itm.getNovo();
		try {
			dados.add((antigo != null) ? antigo.getPessoa().getDescricao()
					: novo.getPessoa().getDescricao());
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add((antigo != null) ? antigo.getServico().getDescricao()
					: novo.getServico().getDescricao());
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add((antigo != null) ? antigo.getSituacao()
					.getDescr() : "-");
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add((novo != null) ? novo.getSituacao()
					.getDescr() : " - ");
		} catch (Exception e) {
			dados.add("");
		}

	}

	@SuppressWarnings("unused")
	private String printDate(Date dte) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(dte);
	}

	private Date converteParaData(String p_strData) throws Exception {
		SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"dd/MM/yyyy hh:mm:ss");
		Date t_dteData = null;
		t_dteData = formatter.parse(p_strData);
		return t_dteData;
	}

	private CpDao dao() {
		return CpDao.getInstance();
	}

	public static void main(String args[]) throws Exception {
	}

	/**
	 * @return the dtInicio
	 */
	public Date getDtInicio() {
		return dtInicio;
	}

	/**
	 * @param dtInicio
	 *            the dtInicio to set
	 */
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	/**
	 * @return the dtFim
	 */
	public Date getDtFim() {
		return dtFim;
	}

	/**
	 * @param dtFim
	 *            the dtFim to set
	 */
	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}

	/**
	 * @return the cpOrgaoUsuario
	 */
	public CpOrgaoUsuario getCpOrgaoUsuario() {
		return cpOrgaoUsuario;
	}

	/**
	 * @param cpOrgaoUsuario
	 *            the cpOrgaoUsuario to set
	 */
	public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.cpOrgaoUsuario = cpOrgaoUsuario;
	}

	/**
	 * Obtém uma lista ordenada com as configuraçõesAcesso na data
	 * 
	 * @param ou
	 *            Orgão Usuário
	 * @param dtEvn
	 *            Data em questão
	 * @return lista ordenada
	 */
	@SuppressWarnings("unchecked")
	public static SortedSet obterDasPessoasDoOrgaoNaData(
			CpTipoDeConfiguracao tipo, List<DpPessoa> pessoas,
			List<CpServico> servicos, Date dtEvn) throws Exception {
		TreeSet lista = new TreeSet<AlteracaoDireitosItem>();
		for (DpPessoa pes : pessoas) {
			if (pes.ativaNaData(dtEvn)) {
				for (CpServico srv : servicos) {
					AlteracaoDireitosItem item = gerar(tipo, null, pes, null,
							null, srv, dtEvn);
					lista.add(item);
				}
			}
		}
		return lista;
	}

	/**
	 * Utilizado para teste com um volume pequeno de pessoas
	 * 
	 * @return AArrayList<DpPessoa> com pesosas para teste
	 */
	static public ArrayList<DpPessoa> testeObterPessoas() {
		ArrayList<DpPessoa> pasas = new ArrayList<DpPessoa>();
		pasas
				.add(CpDao.getInstance().consultar(160631L, DpPessoa.class,
						false));
		pasas
				.add(CpDao.getInstance().consultar(160632L, DpPessoa.class,
						false));
		pasas
				.add(CpDao.getInstance().consultar(130642L, DpPessoa.class,
						false));
		pasas
				.add(CpDao.getInstance().consultar(160634L, DpPessoa.class,
						false));
		pasas
				.add(CpDao.getInstance().consultar(160623L, DpPessoa.class,
						false));
		pasas
				.add(CpDao.getInstance().consultar(132181L, DpPessoa.class,
						false));
		return pasas;
	}

	static public ArrayList<DpPessoa> testeObterPessoas2() {
		ArrayList<DpPessoa> pasas = new ArrayList<DpPessoa>();
		pasas.addAll(CpDao.getInstance()
				.consultarPorIdInicialInclusiveFechadas(26822L));
		return pasas;
	}

	private static AlteracaoDireitosItem gerar(CpTipoDeConfiguracao tipo,
			CpPerfil perfil, DpPessoa pessoa, DpLotacao lotacao,
			CpOrgaoUsuario orgao, CpServico servico, Date dtEvn)
			throws Exception {
		CpConfiguracao cfgFiltro = new CpConfiguracao();
		cfgFiltro.setCpGrupo(perfil);
		cfgFiltro.setDpPessoa(pessoa);
		cfgFiltro.setLotacao(lotacao);
		cfgFiltro.setOrgaoUsuario(orgao);
		cfgFiltro.setCpServico(servico);
		cfgFiltro.setCpTipoConfiguracao(tipo);
		CpConfiguracaoCache cache = Cp.getInstance().getConf().buscaConfiguracao(
				cfgFiltro, new int[0], dtEvn);
		CpConfiguracao cfg = CpDao.getInstance().consultar(cache.idConfiguracao, CpConfiguracao.class, false);
		AlteracaoDireitosItem itm = new AlteracaoDireitosItem();
		itm.setServico(servico);
		itm.setPessoa(pessoa);
		if (cfg != null) {
			itm.setCfg(cfg);
			itm.setSituacao(cfg.getCpSituacaoConfiguracao());
		} else {
			itm.setSituacao(servico.getCpTipoServico().getSituacaoDefault());
		}
		return itm;
	}

	/**
	 * Compara a configuração
	 * 
	 * @param ou
	 *            órgão do usuário
	 * @param dtAntes
	 *            Primeira data da comparação
	 * @param dtDepois
	 *            Segunda data da comparação
	 * @return ArrayList<ConfiguracaoAcesso[2]> com a sincronização das
	 *         configurações nas datas
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	static public List<Item> compararDasPessoasDoOrgaoNoPeriodo(
			CpOrgaoUsuario ou, Date dtAntes, Date dtDepois) throws Exception {
		List<DpPessoa> pessoas = (List<DpPessoa>) CpDao.getInstance()
				.consultarPorOrgaoUsuDpPessoaInclusiveFechadas(ou.getId());
		// ArrayList<DpPessoa> pesas = testeObterPessoas2();
		List<CpServico> servicos = CpDao.getInstance().listarServicos();
		CpTipoDeConfiguracao tipo = CpDao.getInstance().consultar(
				CpTipoDeConfiguracao.UTILIZAR_SERVICO,
				CpTipoDeConfiguracao.class, false);
		SortedSet<Sincronizavel> setAntes = obterDasPessoasDoOrgaoNaData(tipo,
				pessoas, servicos, dtAntes);
		SortedSet<Sincronizavel> setDepois = obterDasPessoasDoOrgaoNaData(tipo,
				pessoas, servicos, dtDepois);

		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setDepois);
		sinc.setSetAntigo(setAntes);

		List<Item> list = sinc.getEncaixe();
		return list;
	}
}
