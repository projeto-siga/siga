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
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExParte;
import br.gov.jfrj.siga.ex.util.CronologiaComparator;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Table(name = "siga.ex_mobil")
public class ExMobil extends AbstractExMobil implements Serializable, Selecionavel, Comparable {

	private static final Logger log = Logger.getLogger(ExMobil.class);
	
	@Transient
	private static boolean isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso = false;

	/**
	 * Retorna A penúltima movimentação não cancelada de um Mobil.
	 * 
	 * @return Penúltima movimentação não cancelada de um Mobil.
	 * 
	 */
	public ExMovimentacao getPenultimaMovimentacaoNaoCancelada() {
		final Set<ExMovimentacao> movs = getExMovimentacaoSet();

		ExMovimentacao mov = null;
		ExMovimentacao penMov = null;
		for (final Object element : movs) {
			final ExMovimentacao movIterate = (ExMovimentacao) element;

			if (movIterate.getExTipoMovimentacao()
					.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
					&& movIterate.getExMovimentacaoCanceladora() == null) {
				if (mov == null && penMov == null) {
					mov = movIterate;
				} else {
					penMov = mov;
					mov = movIterate;
				}
			}
		}
		return penMov;
	}

	/**
	 * Retorna as movimentações de um Mobil de acordo com um tipo específico de
	 * movimentação.
	 * 
	 * @param tpMov
	 * 
	 * @return Lista de movimentações de um Mobil de acordo com um tipo
	 *         específico de movimentação.
	 * 
	 */
	public List<ExMovimentacao> getMovimentacoesPorTipo(long tpMov, boolean somenteAtivas) {

		final Set<ExMovimentacao> movs = getExMovimentacaoSet();
		List<ExMovimentacao> movsTp = new ArrayList<ExMovimentacao>();

		if (movs != null)
			for (final ExMovimentacao m : movs) {
				if (somenteAtivas && m.isCancelada())
					continue;
				if (m.getExTipoMovimentacao().getIdTpMov().equals(tpMov))
					movsTp.add(m);
			}
		return movsTp;
	}

	/**
	 * Verifica se um Mobil é do tipo Geral.
	 * 
	 * @return Verdadeiro se um Mobil for do tipo Geral e Falso caso contrário.
	 * 
	 */
	public boolean isGeral() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_GERAL;
		return getExTipoMobil() != null && getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_GERAL;
	}

	/**
	 * Verifica se um Mobil é do tipo Via.
	 * 
	 * @return Verdadeiro se um Mobil for do tipo Via e Falso caso contrário.
	 * 
	 */
	public boolean isVia() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_VIA;
		return getExTipoMobil() != null && getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VIA;
	}

	/**
	 * Verifica se um Mobil é do tipo Volume.
	 * 
	 * @return Verdadeiro se um Mobil for do tipo Volume e Falso caso contrário.
	 * 
	 */
	public boolean isVolume() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_VOLUME;
		return getExTipoMobil() != null && getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VOLUME;
	}

	public boolean isUltimoVolume() {
		if (!isVolume())
			return false;
		return getNumSequencia().equals(doc().getNumUltimoVolume());
	}

	/**
	 * Verifica se um Mobil é do tipo Geral de um doc do tipo Processo.
	 * 
	 * @return Verdadeiro ou Falso.
	 * 
	 */
	public boolean isGeralDeProcesso() {
		return isGeral() && doc().isProcesso();
	}

	/**
	 * Verifica se um Mobil é do tipo Geral de um doc do tipo Expediente.
	 * 
	 * @return Verdadeiro ou Falso.
	 * 
	 */
	public boolean isGeralDeExpediente() {
		return isGeral() && doc().isExpediente();
	}

	/**
	 * Verifica se um Mobil está Cancelado.
	 * 
	 * @return Verdadeiro se um Mobil está Cancelado e Falso caso contrário.
	 * 
	 */
	public boolean isCancelada() {
		/*
		 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Verifica se
		 * getExTipoMobil() é diferente de nulo antes de chamar o método
		 * getIdTipoMobil() do objeto
		 */
		// return (getExTipoMobil().getIdTipoMobil() ==
		// ExTipoMobil.TIPO_MOBIL_VIA && getUltimaMovimentacaoNaoCancelada() ==
		// null);
		return getExTipoMobil() != null
				&& (getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VIA
						|| getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VOLUME)
				&& getUltimaMovimentacaoNaoCancelada() == null;
	}

	/**
	 * Verifica se um usuario está ciente para este Mobil
	 * 
	 * @return Verdadeiro se está ciente e Falso caso contrário.
	 * 
	 */
	public boolean isCiente(DpPessoa titular) {
		Set<ExMovimentacao> setMovCiente = getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA); 
		if (setMovCiente == null || setMovCiente.size() == 0)
			return false;

		for (ExMovimentacao mov : setMovCiente) {
			if (mov.getCadastrante() != null &&  mov.getCadastrante().equivale(titular)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Retorna a descrição do documento relacionado ao Mobil como um link em
	 * html.
	 * 
	 * @return A descrição do documento relacionado ao Mobil como um link em
	 *         html.
	 * 
	 */
	public String getDescricao() {
		final Integer popW = 700;
		final Integer popH = 500;
		final String winleft = "(screen.width - " + popW + ") / 2";
		final String winUp = "(screen.height - " + popH + ") / 2";
		final String winProp = "'width=" + popW + ",height=" + popH + ",left='+" + winleft + "+',top='+" + winUp
				+ "+',scrollbars=yes,resizable'";
		String s = "<a href=\"javascript:void(0)\" onclick=\"window.open('/sigaex/app/expediente/doc/exibir?popup=true&idmob="
				+ getIdMobil();

		String descricaoCurta = null;
		ExDocumento exDocumento = getExDocumento();

		if (exDocumento != null) {
			try {
				descricaoCurta = exDocumento.getDescrCurta();
			} catch (Exception e) {
				log.warn(
						"[getDescricao] - Não foi possível recuperar a descrição curta do Documento. Retornando descrição em branco.",
						e);
				descricaoCurta = "";
			}
		} else {
			log.warn("[getDescricao] - O Documento informado é nulo.");
		}

		if (descricaoCurta == null) {
			descricaoCurta = "";
		}

		s = s + "', 'documento', " + winProp + ")\">" + descricaoCurta + "</a>";
		return descricaoCurta;
	}

	/**
	 * Retorna a sigla mais a descrição do documento relacionado ao Mobil.
	 * 
	 * @return A sigla mais a descrição do documento relacionado ao Mobil.
	 * 
	 */
	public String getSiglaEDescricaoCompleta() {
		return getSigla() + " - " + getDescricaoCompleta();

	}

	/**
	 * Retorna a descrição do tipo de documento mais o código do documento e a
	 * descrição do documento.
	 * 
	 * @return A descrição do tipo de documento mais o código do documento e a
	 *         descrição do documento.
	 * 
	 */
	public String getNomeCompleto() {
		String s = getExDocumento().getExFormaDocumento().getExTipoFormaDoc().getDescricao() + ": "
				+ getExDocumento().getCodigoString() + ": "; // getNomeCompleto();
		s += getDescricaoCompleta();
		return s;
	}

	/**
	 * Retorna o número de sequência, a descrição de tipo de mobil mais a
	 * descrição do tipo de destinação.
	 * 
	 * @return O número de sequência, a descrição de tipo de mobil mais a
	 *         descrição do tipo de destinação.
	 */
	public String getDescricaoCompleta() {
		return getDescricaoCompleta(true);
	}

	/**
	 * Retorna o número de sequência e a descrição de tipo de mobil
	 * 
	 * @return O número de sequência, a descrição de tipo de mobil
	 */
	public String getDescricaoCompletaSemDestinacao() {
		return getDescricaoCompleta(false);
	}

	private String getDescricaoCompleta(boolean incluindoTpDestinacao) {
		String descTipoMobil = getExTipoMobil().getDescTipoMobil();

		if (isGeral())
			return descTipoMobil;

		if (isVia()) {
			ExVia via = getDoc().via(getNumSequencia().shortValue());
			if (via != null && via.getExTipoDestinacao() != null
					&& via.getExTipoDestinacao().getFacilitadorDest() != null) {
				descTipoMobil = via.getExTipoDestinacao().getFacilitadorDest();
			}
		}

		String s = getNumSequencia() + (isVia() ? "&ordf; " : "&ordm; ") + descTipoMobil;

		if (incluindoTpDestinacao && isVia()) {
			ExVia via = getDoc().via(getNumSequencia().shortValue());
			if (via != null && via.getExTipoDestinacao() != null) {
				s += " (" + via.getExTipoDestinacao().getDescrTipoDestinacao() + ")";
			}
		}

		return s;
	}

	/**
	 * Informa a sigla de um mobil.
	 * 
	 * @param sigla
	 */
	public void setSigla(String sigla) {
		sigla = sigla.trim().toUpperCase();
		if (sigla != null && sigla.contains(":"))
			sigla = sigla.split(":")[0];

		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : ExDao.getInstance().listarOrgaosUsuarios()) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
			mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
		}
		String acronimos = "";
		for (String s : mapAcronimo.keySet()) {
			acronimos += "|" + s;
		}

		final Pattern p2 = Pattern.compile("^TMP-?([0-9]{1,10})");

		// Edson: testes unitários para esta regex:
		// https://regex101.com/r/NJidBr/2
		// Ao acessar, clique em "Switch to unit tests"
		final Pattern p1 = Pattern.compile("^(?<orgao>" + acronimos
				+ ")?-?(?<especie>[A-Za-z]{3})?-?(?:(?<sonumero>[0-9]{1,8})|(?:(?<ano>\\d{4}?)/?)(?<numero>[0-9]{1,8})(?<subnumero>\\.?[0-9]{1,3})??)(?:(?<via>(?:-?[a-zA-Z]{1})|(?:-[0-9]{1,2}))|(?:-?V(?<volume>[0-9]{1,2})))?$");
		final Matcher m2 = p2.matcher(sigla);
		final Matcher m1 = p1.matcher(sigla);

		if (getExDocumento() == null) {
			final ExDocumento doc = new ExDocumento();
			setExDocumento(doc);
		}

		if (m2.find()) {
			if (m2.group(1) != null)
				getExDocumento().setIdDoc(new Long(m2.group(1)));
			return;
		}

		if (m1.find()) {
			String orgao = m1.group("orgao");
			String especie = m1.group("especie");
			String ano = m1.group("ano");
			String numero = m1.group("numero");
			String subnumero = m1.group("subnumero");
			String sonumero = m1.group("sonumero");
			String via = m1.group("via");
			String volume = m1.group("volume");

			if (orgao != null && orgao.length() > 0) {
				try {
					if (mapAcronimo.containsKey(orgao)) {
						getExDocumento().setOrgaoUsuario(mapAcronimo.get(orgao));
					} else {
						CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
						orgaoUsuario.setSiglaOrgaoUsu(orgao);

						orgaoUsuario = ExDao.getInstance().consultarPorSigla(orgaoUsuario);

						getExDocumento().setOrgaoUsuario(orgaoUsuario);
					}
				} catch (final Exception ce) {

				}
			}

			if (especie != null) {
				try {
					ExFormaDocumento formaDoc = new ExFormaDocumento();
					formaDoc.setSiglaFormaDoc(especie);
					formaDoc = ExDao.getInstance().consultarPorSigla(formaDoc);
					if (formaDoc != null)
						getExDocumento().setExFormaDocumento(formaDoc);
				} catch (final Exception ce) {
					throw new Error(ce);
				}
			}

			if (ano != null)
				getExDocumento().setAnoEmissao(Long.parseLong(ano));
			// else {
			// Date dt = new Date();
			// getExDocumento().setAnoEmissao((long) dt.getYear());
			// }
			if (numero != null)
				getExDocumento().setNumExpediente(Long.parseLong(numero));
			if (sonumero != null) {
				getExDocumento().setNumExpediente(Long.parseLong(sonumero));
				getExDocumento().setAnoEmissao((long) new Date().getYear() + 1900);

			}

			// Numero de sequencia do documento filho
			//
			if (subnumero != null) {
				String vsNumSubdocumento = subnumero.toUpperCase();
				if (vsNumSubdocumento.contains("."))
					vsNumSubdocumento = vsNumSubdocumento.substring(vsNumSubdocumento.indexOf(".") + 1);
				Integer vshNumSubdocumento = new Integer(vsNumSubdocumento);
				if (vshNumSubdocumento != 0) {
					try {
						String siglaPai = (orgao == null ? (getExDocumento().getOrgaoUsuario() != null
								? getExDocumento().getOrgaoUsuario().getAcronimoOrgaoUsu() : "") : orgao)
								+ (especie == null ? "" : especie) + (ano == null ? "" : ano)
								+ ((ano != null && numero != null) ? "/" : "") + (numero == null ? "" : numero);
						ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
						flt.setSigla(siglaPai);
						ExMobil mobPai = null;
						if (flt.getIdOrgaoUsu() == null) {
							flt.setIdOrgaoUsu(getExDocumento().getOrgaoUsuario().getId());
						}
						mobPai = ExDao.getInstance().consultarPorSigla(flt);
						ExDocumento docFilho = mobPai.doc().getMobilGeral().getSubdocumento(vshNumSubdocumento);
						setExDocumento(docFilho);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}
			}

			// Numero da via
			//
			if (via != null) {
				String vsNumVia = via.toUpperCase();
				if (vsNumVia.contains("-"))
					vsNumVia = vsNumVia.substring(vsNumVia.indexOf("-") + 1);
				Integer vshNumVia;
				final String alfabeto = "ABCDEFGHIJLMNOPQRSTUZ";
				final int vi = (alfabeto.indexOf(vsNumVia)) + 1;
				if (vi <= 0)
					vshNumVia = new Integer(vsNumVia);
				else {
					vshNumVia = (new Integer(vi).intValue());
					setExTipoMobil(ExDao.getInstance().consultar(ExTipoMobil.TIPO_MOBIL_VIA, ExTipoMobil.class, false));
				}
				setNumSequencia(vshNumVia);
			} else {
				if (volume != null) {
					String vsNumVolume = volume.toUpperCase();
					Integer vshNumVolume = new Integer(vsNumVolume);
					setExTipoMobil(
							ExDao.getInstance().consultar(ExTipoMobil.TIPO_MOBIL_VOLUME, ExTipoMobil.class, false));
					setNumSequencia(vshNumVolume);
				} else {
					setExTipoMobil(
							ExDao.getInstance().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
				}
			}
		}
	}

	/**
	 * Retorna o código do documento mais o número da via ou do volume.
	 * 
	 * @return O código do documento mais o número da via ou do volume.
	 */
	public String getSigla() {
		if (getExDocumento() == null)
			return null;
		if (getExTipoMobil() == null)
			return null;
		if ((isVia() || isVolume()) && (getNumSequencia() == null || getNumSequencia() == 0))
			throw new Error("Via e Volume devem possuir número válido de sequencia.");
		String terminacao = getTerminacaoSigla();
		return getExDocumento().getSigla() + (terminacao.equals("") ? "" : "-") + getTerminacaoSigla();
	}

	/**
	 * Retorna o código do documento resumido mais o número da via ou do volume.
	 * 
	 * @return O código do documento resumido mais o número da via ou do volume.
	 */
	public String getSiglaResumida(CpOrgaoUsuario orgao, ExDocumento docRef) {
		if (getExDocumento() == null)
			return null;
		if (getExTipoMobil() == null)
			return null;
		if ((isVia() || isVolume()) && (getNumSequencia() == null || getNumSequencia() == 0))
			throw new Error("Via e Volume devem possuir número válido de sequencia.");
		String codigoDoc = getExDocumento().getCodigoResumido(orgao, docRef);
		String terminacao = getTerminacaoSigla();
		return codigoDoc + (terminacao.equals("") || codigoDoc.equals("") ? "" : "-") + getTerminacaoSigla();
	}

	/**
	 * Retorna o código do documento mais o número da via ou do volume.
	 * 
	 * @return O código do documento mais o número da via ou do volume.
	 */
	public static String getSigla(String codigoDocumento, Integer numSequencia, Long idTipoMobil) {
		if (codigoDocumento == null)
			return null;
		if (idTipoMobil == null)
			return null;

		if ((idTipoMobil == ExTipoMobil.TIPO_MOBIL_VIA || idTipoMobil == ExTipoMobil.TIPO_MOBIL_VOLUME)
				&& (numSequencia == null || numSequencia == 0))
			throw new Error("Via e Volume devem possuir número válido de sequencia.");
		if (idTipoMobil == ExTipoMobil.TIPO_MOBIL_VIA) {
			final String alfabeto = "ABCDEFGHIJLMNOPQRSTUZ";

			// as vias vão até a letra 'U', se passar disso, assume letra 'Z'
			if (numSequencia <= 20) {
				final String vsNumVia = alfabeto.substring(numSequencia - 1, numSequencia);
				return codigoDocumento + "-" + vsNumVia;
			} else {
				final String vsNumVia = "Z";
				return codigoDocumento + "-" + vsNumVia;
			}
		} else if (idTipoMobil == ExTipoMobil.TIPO_MOBIL_VOLUME) {
			final String vsNumVolume = "V" + (numSequencia < 10 ? "0" + numSequencia : numSequencia);
			return codigoDocumento + "-" + vsNumVolume;
		} else {
			return codigoDocumento;
		}
	}

	/*
	 * public Long getId() { if (getExDocumento() == null) return null;
	 * ExMovimentacao mov = getExDocumento()
	 * .getUltimaMovimentacaoNaoCancelada(getNumVia()); if (mov == null) return
	 * null; return mov.getIdMov(); }
	 */

	/**
	 * Se esse documento estiver juntado, retorna o pai Senão, retorna ele mesmo
	 * 
	 */
	public ExMobil getMobilPrincipal() {

		if (getExMobilPai() == null)
			return this;

		return getExMobilPai().getMobilPrincipal();
	}

	/**
	 * Retorna a sigla sem os sinais "-" e "/".
	 * 
	 * @return A sigla sem os sinais "-" e "/".
	 * 
	 */
	public String getCodigoCompacto() {
		String s = getSigla();
		if (s == null)
			return null;
		return s.replace("-", "").replace("/", "");
	}

	/**
	 * Retorna a sigla.
	 * 
	 * @return A sigla.
	 * 
	 */
	public Object getCodigo() {
		return getSigla();
	}

	/**
	 * Retorna o documento relacionado ao Mobil.
	 * 
	 * @return O documento relacionado ao Mobil.
	 * 
	 */
	public ExDocumento doc() {
		return getExDocumento();
	}

	/**
	 * 
	 * 
	 * 
	 * Retorna se o móbil recebeu alguma movimentação do tipo informado que não
	 * tenha sido cancelada.
	 * 
	 * @param tpMov
	 * @return
	 */
	public boolean sofreuMov(long tpMov) {

		return sofreuMov(tpMov, 0);
	}

	/**
	 * Retorna se o móbil recebeu alguma movimentação do tipo informado que não
	 * tenha sido cancelada e também não tenha sido revertida pela movimentação
	 * de reversão do tipo informado.
	 * 
	 * @param tpMov
	 * @param tpMovReversao
	 * @return
	 */
	public boolean sofreuMov(long tpMov, long tpMovReversao) {
		return sofreuMov(tpMov, tpMovReversao, this);
	}

	/**
	 * 
	 * Retorna se um móbil mob recebeu alguma movimentação do tipo informado que
	 * não tenha sido cancelada e também não tenha sido revertida pela
	 * movimentação de reversão do tipo informado.
	 * 
	 * @param tpMov
	 * @param tpMovReversao
	 * @return
	 */

	public boolean sofreuMov(long tpMov, long tpMovReversao, ExMobil mob) {
		return sofreuMov(new long[] { tpMov }, new long[] { tpMovReversao }, mob);
	}

	/**
	 * Retorna se o móbil recebeu alguma movimentação de um dos tipos informados
	 * que não tenha sido cancelada e também não tenha sido revertida pela
	 * movimentação de reversão do tipo informado.
	 * 
	 * @param tpMovs
	 * @param tpMovReversao
	 * @return
	 */
	public boolean sofreuMov(long[] tpMovs, long tpMovReversao) {
		return sofreuMov(tpMovs, new long[] { tpMovReversao }, this);
	}

	/**
	 * Retorna se um móbil mob recebeu alguma movimentação de um dos tipos
	 * informados que não tenha sido cancelada e também não tenha sido revertida
	 * pela movimentação de reversão do tipo informado.
	 * 
	 * @param tpMovs
	 * @param tpMovReversao
	 * @param mob
	 * @return
	 */
	public boolean sofreuMov(long[] tpMovs, long[] tpMovReversao, ExMobil mob) {
		return getUltimaMovimentacao(tpMovs, tpMovReversao, mob, false, null) != null;
	}

	/**
	 * Retorna a última movimentação não cancelada que o móbil recebeu.
	 * 
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacaoNaoCancelada() {
		return getUltimaMovimentacaoNaoCancelada(0L);
	}
	
	/**
	 * Retorna a última movimentação não cancelada de um tipo específico que o
	 * móbil recebeu.
	 * 
	 * @param tpMov
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacaoNaoCancelada(long tpMov) {
		return getUltimaMovimentacaoNaoCancelada(tpMov, 0L);
	}

	/**
	 * Retorna a última movimentação não cancelada de um tipo específico que o
	 * móbil recebeu e que não tenha sido revertida pela movimentação de
	 * reversão do tipo especificado.
	 * 
	 * @param tpMov
	 * @param tpMovReversao
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacaoNaoCancelada(long tpMov, long tpMovReversao) {
		return getUltimaMovimentacao(new long[] { tpMov }, new long[] { tpMovReversao }, this, false, null);
	}

	/**
	 * Retorna a última movimentação não cancelada que o móbil recebeu, com base
	 * nas informações constantes na movimentação informada como parâmetro.
	 * 
	 * @param movParam
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacaoNaoCancelada(ExMovimentacao movParam) {
		return getUltimaMovimentacao(new long[] { movParam.getExTipoMovimentacao().getIdTpMov() }, new long[] { 0L },
				this, false, movParam.getDtMov());
	}	

	/**
	 * Retorna A última movimentação de um Mobil.
	 * 
	 * @return Última movimentação de um Mobil.
	 * 
	 */
	public ExMovimentacao getUltimaMovimentacao() {
		return getUltimaMovimentacao(0L);
	}

	/**
	 * Retorna a última movimentação de um tipo específico que o móbil recebeu.
	 * 
	 * @param tpMov
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacao(long tpMov) {
		return getUltimaMovimentacao(new long[] { tpMov }, new long[] { 0L }, this, true, null);
	}

	/**
	 * Retorna a última movimentação (cancelada ou não, conforme o parâmetro
	 * permitirCancelada) que o móbil mob recebeu e que seja de um dos tpMovs
	 * informados, que não tenha sido revertida por uma movimentação do
	 * tpMovReversao e que tenha ocorrido na data dt
	 * 
	 * @param tpMovs
	 * @param tpMovReversao
	 * @param mob
	 * @param permitirCancelada
	 * @param dt
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacao(long[] tpMovs, long[] tpMovsReversao, ExMobil mob,
			boolean permitirCancelada, Date dt) {

		if (mob == null)
			return null;

		Set<ExMovimentacao> movSet = mob.getExMovimentacaoSet();
		if (movSet == null || movSet.size() == 0)
			return null;

		ExMovimentacao movReturn = null;
		for (ExMovimentacao mov : movSet) {
			if (!permitirCancelada && (mov.isCancelada() || mov.isCanceladora()))
				continue;
		
			if (tpMovs.length == 0 || tpMovs[0] == 0L)
				movReturn = mov;
			else
				for (long t : tpMovs)
					if (mov.getExTipoMovimentacao().getIdTpMov() == t)

						if (dt == null || (dt != null && mov.getDtMov().equals(dt))) {
							movReturn = mov;
							break;
						}

			for (long t : tpMovsReversao)
				if (mov.getExTipoMovimentacao().getIdTpMov() == t) {
					movReturn = null;
					break;
				}
		}
		return movReturn;
	}

	/**
	 * Retorna a última movimentação não cancelada que o móbil mob recebeu antes
	 * de determinada data dt passada por parâmetro
	 * 
	 * @param mob
	 * @param dt
	 * @return
	 */
	public ExMovimentacao getUltimaMovimentacaoAntesDaData(Date dt) {

		ExMovimentacao ultMovAntesDaData = null;

		for (ExMovimentacao mov : getExMovimentacaoSet()) {

			if (mov.isCancelada() || mov.isCanceladora())
				continue;
			if (mov.getDtMov().after(dt))
				break;

			ultMovAntesDaData = mov;

		}
		return ultMovAntesDaData;
	}

	/**
	 * Verifica se o mobil está arquivado corrente
	 */
	public boolean isArquivadoCorrente() {
		if (isGeral() && doc().isFinalizado() && doc().getExMobilSet() != null) {
			for (ExMobil m : doc().getExMobilSet()) {
				if ((m.isVia() || m.isUltimoVolume()) && !m.isArquivado())
					return false;
			}
			return true;
		}
		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE, getMobilParaMovimentarDestinacao());
	}

	/**
	 * Verifica se o mobil está arquivado permanente
	 */
	public boolean isArquivadoPermanente() {
		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE, getMobilParaMovimentarDestinacao());
	}

	/**
	 * Verifica se o mobil está arquivado intermediário
	 */
	public boolean isArquivadoIntermediario() {
		return sofreuMov(new long[] { ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO },
				new long[] { ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO },
				getMobilParaMovimentarDestinacao());
	}

	/**
	 * Verifica se o mobil está em arquivo, seja corrente, intermediário ou
	 * permanente
	 */
	public boolean isArquivado() {
		return isArquivadoCorrente() || isArquivadoIntermediario() || isArquivadoPermanente();
	}
	
	public boolean isAguardandoAndamento(DpPessoa titular, DpLotacao lotaTitular) {
		return doc().isFinalizado()
			&& (isVia() || isVolume())
			&& !isArquivado()
			&& !isApensadoAVolumeDoMesmoProcesso()
			&& !isSobrestado()
			&& !isJuntado()
			&& !isEmTransito(titular, lotaTitular)
			&& !doc().isSemEfeito();
	}

	/**
	 * Verifica se o mobil está sobrestado
	 */
	public boolean isSobrestado() {
		if (isApensadoAVolumeDoMesmoProcesso())
			return false;
		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESOBRESTAR);
	}

	/**
	 * Verifica se um Mobil está em trânsito. Um Mobil está em trânsito quando
	 * ele possui movimentações não canceladas dos tipos: TRANSFERENCIA,
	 * DESPACHO_TRANSFERENCIA, DESPACHO_TRANSFERENCIA_EXTERNA ou
	 * TRANSFERENCIA_EXTERNA e não possuem movimentação de recebimento.
	 * 
	 * Nato: alterei para sinalizar apenas se existe recebimento pendente para a pessoa em questão. Pois agora temos o trâmite paralelo.
	 * 
	 * @return Verdadeiro se o Mobil está em trânsito e Falso caso contrário.
	 * 
	 */
	public boolean isEmTransito(DpPessoa titular, DpLotacao lotaTitular) {
		Pendencias p = calcularTramitesPendentes();

		if (isApensadoAVolumeDoMesmoProcesso() || p.tramitesPendentes.size() == 0)
			return false;
		return Ex.getInstance().getComp().podeReceber(titular, lotaTitular, this);

	}
	
	/**
	 * Verifica se um Mobil recebeu movimentação de inclusão em edital de
	 * eliminação, não revertida pela de retirada de edital de eliminação.
	 * 
	 * @return Verdadeiro ou Falso
	 */
	public boolean isEmEditalEliminacao() {
		return !isEliminado() && sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO,
				getMobilParaMovimentarDestinacao());
	}

	/**
	 * Retorna se o móbil sofreu movimentação de eliminação.
	 * 
	 * @return
	 */
	public boolean isEliminado() {

		if (isGeral() && doc().isExpediente())
			return doc().isEliminado();

		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO, 0, getMobilParaMovimentarDestinacao());

	}

	/**
	 * Verifica se um Mobil recebeu movimentação de indicação para guarda
	 * permanente. Se o móbil for um volume, considera o geral do processo.
	 * 
	 * @return Verdadeiro ou Falso
	 */
	public boolean isindicadoGuardaPermanente() {
		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INDICACAO_GUARDA_PERMANENTE,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_REVERSAO_INDICACAO_GUARDA_PERMANENTE,
				getMobilParaMovimentarDestinacao());
	}

	/**
	 * Verifica se um Mobil está em trânsito interno. Um Mobil está em trânsito
	 * interno quando ele possui movimentações não canceladas dos tipos:
	 * TRANSFERENCIA ou DESPACHO_TRANSFERENCIA e não possuem movimentação de
	 * recebimento.
	 * 
	 * @return Verdadeiro se o Mobil está em trânsito interno e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isEmTransitoInterno() {

		return sofreuMov(new long[] { ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA },

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO);

	}

	/**
	 * Verifica se um Mobil está em trânsito externo. Um Mobil está em trânsito
	 * externo quando ele possui movimentações não canceladas dos tipos:
	 * DESPACHO_TRANSFERENCIA_EXTERNA ou TRANSFERENCIA_EXTERNA e não possuem
	 * movimentação de recebimento.
	 * 
	 * @return Verdadeiro se o Mobil está em trânsito externo e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isEmTransitoExterno() {

		return sofreuMov(new long[] { ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA,

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA },

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO);

	}

	/**
	 * Verifica se um Mobil está juntado a outro. Um Mobil está em juntado a
	 * outro quando ele possui movimentações não canceladas dos tipos: JUNTADA
	 * ou JUNTADA_EXTERNO e não possuem movimentação de cancelamento de juntada.
	 * 
	 * @return Verdadeiro se o Mobil está juntado a outro e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isJuntado() {

		return sofreuMov(new long[] { ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO },

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA);

	}

	public boolean isPendenteDeAnexacao() {
		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO);
	}

	public boolean isPendenteDeColaboracao() {
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao()
					.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO)
				continue;
			if (ExParte.create(mov.getDescrMov()).isPendente())
				return true;
		}
		return false;
	}

	/**
	 * Verifica se um Mobil está juntado a outro mobil do tipo interno. Um Mobil
	 * está em juntado a outro mobil do tipo interno quando ele possui
	 * movimentaçao não cancelada do tipo: JUNTADA e não possue movimentação de
	 * cancelamento de juntada.
	 * 
	 * @return Verdadeiro se o Mobil está juntado a outro mobil do tipo interno
	 *         e Falso caso contrário.
	 * 
	 */
	public boolean isJuntadoInterno() {

		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA,

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA);

	}

	/**
	 * Verifica se um Mobil está juntado a outro mobil do tipo externo. Um Mobil
	 * está em juntado a outro mobil do tipo externo quando ele possui
	 * movimentaçao não cancelada do tipo: JUNTADA_EXTERNO e não possue
	 * movimentação de cancelamento de juntada.
	 * 
	 * @return Verdadeiro se o Mobil está juntado a outro mobil do tipo externo
	 *         e Falso caso contrário.
	 * 
	 */
	public boolean isJuntadoExterno() {

		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO,

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA);

	}

	/**
	 * Verifica se um Mobil está apensado a outro mobil. Um Mobil está apensado
	 * a outro mobil quando ele possui movimentaçao do tipo: APENSACAO e não
	 * possue movimentação de desapensação
	 * 
	 * @return Verdadeiro se o Mobil está apensado a outro mobil e Falso caso
	 *         contrário.
	 * 
	 */
	public boolean isApensado() {

		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO,

				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO);

	}

	/**
	 * Retorna o Mobil pai do Mobil atual.
	 * 
	 * @return Mobil pai do Mobil atual.
	 * 
	 */
	public ExMobil getExMobilPai() {
		ExMobil m = null;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				m = mov.getExMobilRef();
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
				m = null;
		}
		return m;
	}

	public ExMovimentacao anexoPendente(final String descrMov, final boolean fIncluirCancelados) {
		if (getExMovimentacaoSet() == null)
			return null;

		ExMobil m = null;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if ((!fIncluirCancelados) && mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao()
					.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO) {
				if (descrMov == null) {
					return mov;
				}
				if (descrMov.equals(mov.getDescrMov()))
					return mov;
			}
		}
		return null;
	}

	public Long getId() {
		return getIdMobil();
	}

	/**
	 * Retorna a descrição da última movimentação não cancelada.
	 * 
	 * @return Descrição da última movimentação não cancelada.
	 * 
	 */
	public String getDescricaoUltimaMovimentacaoNaoCancelada() {
		ExMovimentacao mov = getUltimaMovimentacaoNaoCancelada();
		if (mov == null)
			return null;
		return mov.getExTipoMovimentacao().getDescricao();
	}

	/**
	 * Retorna o documento relacionado ao Mobil atual.
	 * 
	 * @return Documento relacionado ao Mobil atual.
	 * 
	 */
	public ExDocumento getDoc() {
		return doc();
	}

	public java.util.Set<ExMovimentacao> getCronologiaSet() {
		final TreeSet<ExMovimentacao> set = new TreeSet<ExMovimentacao>(new CronologiaComparator());
		set.addAll(getExMovimentacaoSet());
		set.addAll(getExMovimentacaoReferenciaSet());
		return set;
	}

	public int compareTo(Object o) {
		if (this == o)
			return 0;
		ExMobil other = (ExMobil) o;
		int i;
		i = other.getExDocumento().getIdDoc().compareTo(getExDocumento().getIdDoc());
		if (i != 0)
			return -i;
		i = other.getExTipoMobil().getIdTipoMobil().compareTo(getExTipoMobil().getIdTipoMobil());
		if (i != 0)
			return i;
		return getNumSequencia().compareTo(other.getNumSequencia());
	}

	public List<ExArquivoNumerado> getArquivosNumerados() {
		return getExDocumento().getArquivosNumerados(this);
	}

	/**
	 * Retorna a descrição dos marcadores relacionado ao Mobil atual.
	 * 
	 * @return Descrição dos marcadores relacionado ao Mobil atual.
	 * 
	 */
	public String getMarcadores() {
		StringBuilder sb = new StringBuilder();
		for (ExMarca mar : getExMarcaSetAtivas()) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(mar.getCpMarcador().getDescrMarcador());
		}
		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	/**
	 * Retorna a descrição completa (descrição, lotação, pessoa e datas de
	 * início e fim) dos marcadores relacionados ao Mobil atual, incluindo os
	 * não ativos no momento.
	 * 
	 * @return Descrição dos marcadores relacionado ao Mobil atual.
	 * 
	 */
	public String getMarcadoresDescrCompleta(boolean apenasTemporalidade) {
		StringBuilder marcas = new StringBuilder();

		Set<ExMarca> set = apenasTemporalidade ? getExMarcaSetTemporalidade() : getExMarcaSet();
		for (CpMarca mar : set) {
			marcas.append(mar.getCpMarcador().getDescrMarcador());
			marcas.append(" [");
			marcas.append(mar.getDpPessoaIni() != null ? mar.getDpPessoaIni()

					.getSigla() : "");
			marcas.append(", ");

			marcas.append(mar.getDpLotacaoIni() != null ? mar.getDpLotacaoIni()

					.getSiglaLotacao() : "");
			marcas.append(", ");
			marcas.append(mar.getDtIniMarcaDDMMYYYY());
			marcas.append(", ");

			marcas.append(mar.getDtFimMarcaDDMMYYYY());

			marcas.append("] ");

		}
		return marcas.toString();

	}

	/**
	 * Retorna o Mobil Mestre relacionado ao Mobil atual.
	 * 
	 * @return Mobil Mestre relacionado ao Mobil atual.
	 * 
	 */
	public ExMobil getMestre() {
		ExMobil m = null;
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO)
				m = mov.getExMobilRef();
			if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO)
				m = null;
		}
		return m;
	}

	/**
	 * Se o mobil em questão for o grande mestre de uma cadeia de apensação, ele
	 * deve retornar "this".
	 * 
	 * @return Retorna o mestre de último nivel, ou seja, o mestre de todos os
	 *         mestres dessa apensacao.
	 */
	public ExMobil getGrandeMestre() {
		ExMobil m = getMestre();
		if (m == null) {
			if (getApensos() == null || getApensos().size() != 0)
				return this;
		}
		while (m != null) {
			ExMobil m2 = m.getMestre();
			if (m2 == null)
				return m;
			else
				m = m2;
		}
		return null;
	}

	public SortedSet<ExMobil> getMobilEApensosExcetoVolumeApensadoAoProximo() {
		TreeSet<ExMobil> setFinal = new TreeSet<ExMobil>();
		ExMobil grandeMestre = getGrandeMestre();
		if (grandeMestre != null) {
			setFinal.add(grandeMestre);
			setFinal.addAll(grandeMestre.getApensosExcetoVolumeApensadoAoProximo());
		} else
			setFinal.add(this);
		return setFinal;

	}

	public SortedSet<ExMobil> getApensosExcetoVolumeApensadoAoProximo() {
		return getApensos(true, false);
	}

	/**
	 * 
	 * @return Retorna todos os apensos desse mobil para baixo. Não inclui o
	 *         próprio mobil que está sendo chamado.
	 */
	public SortedSet<ExMobil> getApensos() {
		return getApensos(false, true);
	}

	public SortedSet<ExMobil> getApensos(boolean incluirApensosIndiretos, boolean incluirVolumesApensadosAosProximos) {
		SortedSet<ExMobil> set = new TreeSet<ExMobil>();
		return getApensos(set, incluirApensosIndiretos, incluirVolumesApensadosAosProximos);
	}

	public SortedSet<ExMobil> getApensos(SortedSet<ExMobil> set, boolean incluirApensosIndiretos,
			boolean incluirVolumesApensadosAosProximos) {

		if (getExMovimentacaoReferenciaSet() == null)
			return set;

		varrendoMovRefsDesteMobil: for (ExMovimentacao mov : getExMovimentacaoReferenciaSet()) {

			if (mov.getExTipoMovimentacao().getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO)
				continue varrendoMovRefsDesteMobil;

			if (mov.isCancelada())
				continue varrendoMovRefsDesteMobil;

			if (mov.getExMovimentacaoReferenciadoraSet() != null)
				for (ExMovimentacao ref : mov.getExMovimentacaoReferenciadoraSet())
					if (ref.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO)
						continue varrendoMovRefsDesteMobil;

			if (!set.contains(mov.getExMobil())) {
				if (incluirVolumesApensadosAosProximos || !mov.getExMobil().isApensadoAVolumeDoMesmoProcesso()) {
					set.add(mov.getExMobil());
					// Edson: passando a deixar o if abaixo dentro do anterior
					// pois, se um nó não vai
					// ser adicionado, não há necessidade de verificar os nós
					// abaixo
					if (incluirApensosIndiretos)
						mov.getExMobil().getApensos(set, incluirApensosIndiretos, incluirVolumesApensadosAosProximos);
				}
			}

		}
		return set;
	}

	/**
	 * Retorna, num Set, os móbiles que tenham sido juntados a este móbil, de
	 * modo recursivo ou não, conforme parâmetro.
	 * 
	 * @param recursivo
	 * @return
	 */
	public Set<ExMobil> getJuntados(boolean recursivo) {
		Set<ExMobil> set = new LinkedHashSet<ExMobil>();
		for (ExMovimentacao mov : getExMovimentacaoReferenciaSet())
			if (!mov.isCancelada()) {
				if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
					set.add(mov.getExMobil());
				if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
					set.remove(mov.getExMobil());
			}
		if (!recursivo)
			return set;
		else {
			Set<ExMobil> setRecursivo = new LinkedHashSet<ExMobil>();
			for (ExMobil mob : set) {
				setRecursivo.add(mob);
				setRecursivo.addAll(mob.getJuntadosRecursivo());
			}
			return setRecursivo;
		}

	}

	/**
	 * Retorna, num Set, os móbiles que tenham sido referenciados a este móbil
	 * ou vice-versa.
	 * 
	 * @param recursivo
	 * @return
	 */
	public Set<ExMobil> getVinculados() {
		Set<ExMobil> set = new LinkedHashSet<ExMobil>();
		for (ExMovimentacao mov : getCronologiaSet())
			if (!mov.isCancelada()) {
				if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA) {
					set.add(mov.getExMobilRef());
					set.add(mov.getExMobil());
				}
			}
		set.remove(this);
		if (!isGeral())
			set.addAll(doc().getMobilGeral().getVinculados());
		return set;
	}

	/**
	 * Retorna os móbiles que tenham sido juntados a este móbil, sem
	 * recursividade.
	 * 
	 * @return
	 */
	public Set<ExMobil> getJuntados() {
		return getJuntados(false);
	}

	/**
	 * Retorna todos os móbiles, de modo recursivo, que tenham sido juntados a
	 * este móbil.
	 * 
	 * @return
	 */
	public Set<ExMobil> getJuntadosRecursivo() {
		return getJuntados(true);
	}

	/**
	 * Retorna um Set contendo este móbil e todos os que foram juntados a ele,
	 * de modo recursivo.
	 * 
	 * @return
	 */
	public SortedSet<ExMobil> getMobilETodosOsJuntados() {
		TreeSet<ExMobil> setFinal = new TreeSet<ExMobil>();
		setFinal.add(this);
		setFinal.addAll(getJuntadosRecursivo());
		return setFinal;

	}

	@Override
	public String toString() {
		return getSigla();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getIdMobil() == null)
			return super.equals(obj);
		ExMobil other = (ExMobil) obj;
		if (other == null || other.getIdMobil() == null)
			return false;
		return this.getIdMobil().equals(other.getIdMobil());
	}

	/**
	 * Verifica se um volume está encerrado. Um volume está encerrado se ele
	 * possui movimentação não cancelada do tipo ENCERRAMENTO DE VOLUME.
	 * 
	 * @return Verdadeiro se o volume está encerrado e Falso caso contrário.
	 * 
	 */
	public boolean isVolumeEncerrado() {
		return sofreuMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME);
	}

	/**
	 * Retorna a lista de movimentações de anexação não assinadas
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public List<ExMovimentacao> getAnexosNaoAssinados() {

		List<ExMovimentacao> naoAssinados = new ArrayList<ExMovimentacao>();
		Date dataDeInicioDeObrigacaoDeAssinatura = null;
		dataDeInicioDeObrigacaoDeAssinatura = Prop.getData("data.obrigacao.assinar.anexo.despacho");

		for (ExMovimentacao mov : this.getExMovimentacaoSet()) {
			if (!mov.isCancelada()) {
				if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO)
					if (mov.isAssinada())
						continue;
					else {
						if (dataDeInicioDeObrigacaoDeAssinatura != null
								&& mov.getDtMov().before(dataDeInicioDeObrigacaoDeAssinatura))
							continue;
						naoAssinados.add(mov);
					}
			}
		}
		return naoAssinados;
	}

	/**
	 * Retorna a lista de movimentações de pendência anexação
	 * 
	 * @return Verdadeiro se o Mobil possui pendência de anexos e False caso
	 *         contrário.
	 * 
	 */
	public List<ExMovimentacao> getPendenciasDeAnexacao() {

		List<ExMovimentacao> pendenciasDeAnexacao = new ArrayList<ExMovimentacao>();

		for (ExMovimentacao mov : this.getExMovimentacaoSet()) {
			if (!mov.isCancelada() && mov.getExTipoMovimentacao()
					.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO)
				pendenciasDeAnexacao.add(mov);
		}

		return pendenciasDeAnexacao;
	}

	/**
	 * Retorna a lista de movimentações replicadas devido a lentidão no Siga
	 * 
	 */
	public List<ExMovimentacao> getMovimentacoesReplicadas(Set<ExMovimentacao> movs) {
		List<ExMovimentacao> movsReplicadas = new ArrayList<ExMovimentacao>();
		ExMovimentacao movPosterior = null;

		Iterator it = movs.iterator();
		Iterator itPosterior = movs.iterator();

		if (itPosterior.hasNext())
			movPosterior = (ExMovimentacao) itPosterior.next();

		while (it.hasNext()) {
			ExMovimentacao mov = (ExMovimentacao) it.next();

			while (itPosterior.hasNext()) {
				movPosterior = (ExMovimentacao) itPosterior.next();

				if (mov.getCadastrante().equivale(movPosterior.getCadastrante())
						&& mov.getLotaCadastrante().equivale(movPosterior.getLotaCadastrante())

						&& mov.getSubscritor().equivale(movPosterior.getSubscritor())
						&& mov.getLotaSubscritor().equivale(movPosterior.getLotaSubscritor())
						&& mov.getLotaResp().equivale(movPosterior.getLotaResp())
						&& mov.getLotaTitular().equivale(movPosterior.getLotaTitular())
						&& mov.getIdTpMov().equals(movPosterior.getIdTpMov())
						&& (mov.getDtMov().getTime() - movPosterior.getDtMov().getTime()) < 3600000
						&& (mov.getDtIniMov().getTime() - movPosterior.getDtIniMov().getTime()) < 3600000) {
					movsReplicadas.add(mov);
				}
				break;
			}
			continue;
		}
		return movsReplicadas;
	}

	/**
	 * Retorna a lista de movimentações de pendência de colaboração
	 * 
	 */
	public List<ExMovimentacao> getPendenciasDeColaboracao() {

		List<ExMovimentacao> pendencias = new ArrayList<ExMovimentacao>();

		for (ExMovimentacao mov : this.getExMovimentacaoSet()) {
			if (!mov.isCancelada() && mov.getExTipoMovimentacao()
					.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO) {
				ExParte parte = ExParte.create(mov.getDescrMov());
				if (parte.isAtivo() && !parte.isPreenchido()) {
					pendencias.add(mov);
				}
			}
		}

		return pendencias;
	}

	/**
	 * Verifica se um Mobil possui Pendências de Anexação
	 * 
	 * @return Verdadeiro se o Mobil possui pendências de anexos e False caso
	 *         contrário.
	 * 
	 */
	public boolean temPendenciasDeAnexacao() {
		return getPendenciasDeAnexacao().size() > 0;
	}

	/**
	 * Verifica se um Mobil possui Anexos Pendentes de Assinatura
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public boolean temAnexosNaoAssinados() {
		return getAnexosNaoAssinados().size() > 0;
	}

	/**
	 * Retorna a lista de despachos não assinados
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public List<ExMovimentacao> getDespachosNaoAssinados() {
		List<ExMovimentacao> naoAssinados = new ArrayList<ExMovimentacao>();
		Date dataDeInicioDeObrigacaoDeAssinatura = null;

		dataDeInicioDeObrigacaoDeAssinatura = Prop.getData("data.obrigacao.assinar.anexo.despacho");
		for (ExMovimentacao mov : this.getExMovimentacaoSet()) {
			if (!mov.isCancelada()) {
				if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
						|| mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO
						|| mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA
						|| mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
						|| mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA)
					if (mov.isAssinada())
						continue;
					else {
						if (dataDeInicioDeObrigacaoDeAssinatura != null
								&& mov.getDtMov().before(dataDeInicioDeObrigacaoDeAssinatura))
							continue;
						naoAssinados.add(mov);
					}
			}
		}
		return naoAssinados;
	}

	/**
	 * Verifica se um Mobil possui Anexos Pendentes de Assinatura
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public boolean temDespachosNaoAssinados() {
		return getDespachosNaoAssinados().size() > 0;
	}

	/**
	 * Retorna a lista de expedientes filhos não juntados
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public List<ExDocumento> getDocsFilhosNaoJuntados() {
		Set<ExMobil> todosOsMeusJuntados = getJuntados();
		List<ExDocumento> meusFilhosNaoJuntados = new ArrayList<ExDocumento>();
		for (ExDocumento docFilho : getExDocumentoFilhoSet()) {
			if (!docFilho.isExpediente() || docFilho.isCancelado() || docFilho.isArquivado() || docFilho.isSemEfeito())
				continue;
			boolean juntado = false, juntadoAOutro = false;
			for (ExMobil mobFilho : docFilho.getExMobilSet()) {
				if (todosOsMeusJuntados.contains(mobFilho))
					juntado = true;
				else if (mobFilho.isJuntado())
					juntadoAOutro = true;

			}
			if (!juntado && !juntadoAOutro)
				meusFilhosNaoJuntados.add(docFilho);
		}
		return meusFilhosNaoJuntados;
	}

	/**
	 * Verifica se um Mobil possui documentos filhos não juntados
	 * 
	 * @return Verdadeiro se o Mobil possui anexos não assinados e False caso
	 *         contrário.
	 * 
	 */
	public boolean temDocsFilhosNaoJuntados() {
		return getDocsFilhosNaoJuntados().size() > 0;
	}

	/**
	 * Verifica se um Mobil possui arquivos anexados
	 * 
	 * @return Verdadeiro se o Mobil possui arquivos anexados e False caso
	 *         contrário.
	 * 
	 */
	public boolean temAnexos() {
		boolean b = false;
		for (ExMovimentacao movAss : this.getExMovimentacaoSet()) {
			if (movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO) {
				b = true;
				break;
			}
		}
		return b;

	}

	public boolean temDocumentosJuntados() {
		boolean b = false;
		for (ExMovimentacao movRef : getExMovimentacaoReferenciaSet()) {
			if (!movRef.isCancelada())
				if (movRef.getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
					b = true;
				else if (movRef.getExTipoMovimentacao()
						.getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
					b = false;
		}
		return b;
	}

	/**
	 * Retorna se o móbil possui algum apensado a si.
	 * 
	 * @return
	 */
	public boolean temApensos() {
		return getApensos() != null && getApensos().size() > 0;
	}

	/**
	 * Verifica se um Mobil do tipo Volume está Apensado a outro Mobil do mesmo
	 * Processo.
	 * 
	 * @return Verdadeiro se o Mobil estiver apensado a outro volume do mesmo
	 *         processo e Falso caso contrário.
	 * 
	 */
	public boolean isApensadoAVolumeDoMesmoProcesso() {
		if (getMestre() == null)
			return false;
		if (!isVolume())
			return false;
		if (!getMestre().isVolume())
			return false;
		if (!getMestre().doc().getIdDoc().equals(doc().getIdDoc()))
			return false;
		return true;
	}

	/**
	 * Retorna o último documento que é filho do Mobil atual.
	 * 
	 * @return Último documento que é filho do Mobil atual.
	 * 
	 */
	public int getNumUltimoSubdocumento() {
		int maxNumSubdocumento = 0;
		for (ExDocumento d : getExDocumentoFilhoSet()) {
			if (d.getNumSequencia() == null)
				continue;
			if (d.getNumSequencia() > maxNumSubdocumento) {
				maxNumSubdocumento = d.getNumSequencia();
			}
		}
		return maxNumSubdocumento;
	}

	/**
	 * Retorna um documento filho do Mobil atual de acordo com o número de
	 * sequência informado.
	 * 
	 * @param numSubdocumento
	 * 
	 * @return Documento filho do Mobil atual de acordo com o número de
	 *         sequência informado.
	 * 
	 */
	public ExDocumento getSubdocumento(int numSubdocumento) {
		for (final ExDocumento d : getExDocumentoFilhoSet()) {
			if (d.getNumSequencia() == null)
				continue;
			if (d.getNumSequencia() == numSubdocumento) {
				return d;
			}
		}
		return null;
	}

	public boolean isNumeracaoUnicaAutomatica() {
		return doc().isNumeracaoUnicaAutomatica();
	}

	public List<ExArquivoNumerado> filtrarArquivosNumerados(ExMovimentacao mov, boolean bCompleto) {
		ExMobil mobPrincipal = getMobilPrincipal();

		List<ExArquivoNumerado> arquivosNumerados = null;

		arquivosNumerados = mobPrincipal.getExDocumento().getArquivosNumerados(mobPrincipal);		
		
		boolean teveReordenacao = (mobPrincipal.getDoc() != null && 
				mobPrincipal.getDoc().podeReordenar() &&
				mobPrincipal.getDoc().podeExibirReordenacao() &&
				mobPrincipal.getDoc().temOrdenacao());

		List<ExArquivoNumerado> ans = new ArrayList<ExArquivoNumerado>();
		int i = 0;
		if (mov != null) {
			// Se for uma movimentacao, remover todos os arquivos alem da
			// movimentacao
			for (ExArquivoNumerado an : arquivosNumerados) {
				if (an.getArquivo() instanceof ExMovimentacao) {
					if (((ExMovimentacao) an.getArquivo()).getIdMov().equals(mov.getIdMov())) {
						ans.add(an);
						break;
					}
				}
			}
		} else if (mobPrincipal != this) {
			// Se for um documento juntado, remover todos os documentos que alem
			// dele e de seus anexos
			ExArquivoNumerado an;
			for (i = 0; i < arquivosNumerados.size(); i++) {
				an = arquivosNumerados.get(i);
				if (an.getArquivo() instanceof ExDocumento) {
					if (((ExDocumento) an.getArquivo()).getIdDoc().equals(getExDocumento().getIdDoc())
							&& an.getMobil().equals(this)) {
						ans.add(an);
						break;
					}
				}
			}
		} else {
			if (mobPrincipal == this && teveReordenacao) {
				for (ExArquivoNumerado arquivo : arquivosNumerados) {
					if (mobPrincipal.getDoc().getIdDoc().equals(arquivo.getArquivo().getIdDoc())) {						
						ans.add(arquivo);
						break;
					}
				}
			} else {
				ans.add(arquivosNumerados.get(0));
			}			
		}
		
		if (bCompleto && teveReordenacao) {
			ans.clear();
			for (ExArquivoNumerado arquivo : arquivosNumerados) {				
				ans.add(arquivo);
			}
		} else if (bCompleto && i != -1) {
			for (int j = i + 1; j < arquivosNumerados.size(); j++) {
				ExArquivoNumerado anSub = arquivosNumerados.get(j);
				if (anSub.getNivel() <= arquivosNumerados.get(i).getNivel())
					break;
				ans.add(anSub);
			}
		}
		return ans;
	}

	public Long getByteCount() {
		if (getExMobilPai() != null)
			return null;
		long l = 0;

		List<ExArquivoNumerado> arquivosNumerados = getExDocumento().getArquivosNumerados(this);

		for (int i = 0; i < arquivosNumerados.size(); i++)
			l += arquivosNumerados.get(i).getArquivo().getByteCount();

		return l;
	}

	/**
	 * Retorna as movimentações de um Mobil que estão canceladas.
	 * 
	 * @return Lista de movimentações de um Mobil que estão canceladas.
	 * 
	 */
	public List<ExMovimentacao> getMovimentacoesCanceladas() {

		final Set<ExMovimentacao> movs = getExMovimentacaoSet();
		List<ExMovimentacao> movsCanceladas = new ArrayList<ExMovimentacao>();

		if (movs != null)
			for (final ExMovimentacao mov : movs) {
				if (mov.isCancelada())
					movsCanceladas.add(mov);
			}
		return movsCanceladas;
	}

	public int getTotalDePaginas() {
		int totalDePaginas = 0;

		for (ExArquivoNumerado arquivo : getArquivosNumerados()) {

			totalDePaginas += arquivo.getNumeroDePaginasParaInsercaoEmDossie();
		}

		return totalDePaginas;
	}

	public int getTotalDePaginasSemAnexosDoMobilGeral() {
		int totalDePaginasDoGeral = 0;

		if (getDoc().getMobilGeral().temAnexos()) {
			totalDePaginasDoGeral = getDoc().getMobilGeral().getTotalDePaginas();
		}

		return getTotalDePaginas() - totalDePaginasDoGeral;
	}

	public SortedSet<ExMarca> getExMarcaSetAtivas() {
		SortedSet<ExMarca> finalSet = new TreeSet<ExMarca>();
		Date dt = new Date();
		for (ExMarca m : getExMarcaSet())
			if ((m.getDtIniMarca() == null || m.getDtIniMarca().before(dt))
					&& (m.getDtFimMarca() == null || m.getDtFimMarca().after(dt)))
				finalSet.add(m);
		return finalSet;
	}

	public boolean temMarcaNaoAtiva() {
		Date dt = new Date();
		for (ExMarca m : getExMarcaSet())
			if ((m.getDtIniMarca() != null && m.getDtIniMarca().after(dt))
					|| (m.getDtFimMarca() != null && m.getDtFimMarca().before(dt)))
				return true;
		return false;
	}

	public SortedSet<ExMarca> getExMarcaSetTemporalidade() {
		SortedSet<ExMarca> setFinal = new TreeSet<ExMarca>();
		for (ExMarca m : getExMarcaSet()) {
			long idM = m.getCpMarcador().getIdMarcador();
			if (idM == CpMarcadorEnum.ARQUIVADO_CORRENTE.getId() || idM == CpMarcadorEnum.TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO.getId()
					|| idM == CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId() || idM == CpMarcadorEnum.RECOLHER_PARA_ARQUIVO_PERMANENTE.getId()
					|| idM == CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId() || idM == CpMarcadorEnum.A_ELIMINAR.getId()
					|| idM == CpMarcadorEnum.EM_EDITAL_DE_ELIMINACAO.getId() || idM == CpMarcadorEnum.ELIMINADO.getId())
				setFinal.add(m);
		}
		return setFinal;
	}

	/**
	 * Retorna o tempo de permanência deste móbil no arquivo corrente conforme o
	 * PCTT.
	 * 
	 * @return
	 */
	public ExTemporalidade getTemporalidadeCorrente() {
		ExVia viaPCTT = getViaPCTT();
		return viaPCTT != null ? viaPCTT.getTemporalidadeCorrente() : null;
	}

	/**
	 * Retorna o tempo de permanência deste móbil no arquivo corrente conforme o
	 * PCTT, considerando as temporalidades de todos os outros móbiles da árvore
	 * de juntados, predominando a maior delas.
	 * 
	 * @return
	 */
	public ExTemporalidade getTemporalidadeCorrenteEfetiva() {
		ExTemporalidade tmpCorrentePredominante = null;
		for (ExMobil mob : getArvoreMobilesParaAnaliseDestinacao()) {
			final ExTemporalidade tmpCorrente = mob.getTemporalidadeCorrente();
			if (tmpCorrentePredominante == null
					|| (tmpCorrente != null && tmpCorrente.compareTo(tmpCorrentePredominante) > 0))
				tmpCorrentePredominante = tmpCorrente;
		}
		return tmpCorrentePredominante;
	}

	/**
	 * Retorna se o móbil tem atrelado um tempo de permanência no arquivo
	 * corrente, mesmo que ele não seja fixo e mensurável. Essa avaliação
	 * considera todos os móbiles juntados.
	 * 
	 * @return
	 */
	public boolean temTemporalidadeCorrente() {
		return getTemporalidadeCorrenteEfetiva() != null;
	}

	/**
	 * Retorna o tempo de permanência deste móbil no arquivo intermediário
	 * conforme o PCTT.
	 * 
	 * @return
	 */
	public ExTemporalidade getTemporalidadeIntermediario() {
		ExVia viaPCTT = getViaPCTT();
		return viaPCTT != null ? viaPCTT.getTemporalidadeIntermediario() : null;
	}

	/**
	 * Retorna o tempo de permanência deste móbil no arquivo intermediário
	 * conforme o PCTT, considerando as temporalidades de todos os outros
	 * móbiles da árvore de juntados, predominando a maior delas.
	 * 
	 * @return
	 */
	public ExTemporalidade getTemporalidadeIntermediarioEfetiva() {
		ExTemporalidade tmpIntermedPredominante = null;
		for (ExMobil mob : getArvoreMobilesParaAnaliseDestinacao()) {
			final ExTemporalidade tmpIntermed = mob.getTemporalidadeIntermediario();
			if (tmpIntermedPredominante == null
					|| (tmpIntermed != null && tmpIntermed.compareTo(tmpIntermedPredominante) > 0))
				tmpIntermedPredominante = tmpIntermed;
		}
		return tmpIntermedPredominante;
	}

	/**
	 * Retorna se o móbil tem atrelado um tempo de permanência no arquivo
	 * intermediário, mesmo que ele não seja fixo e mensurável. Essa avaliação
	 * considera todos os móbiles juntados.
	 * 
	 * @return
	 */
	public boolean temTemporalidadeIntermediario() {
		return getTemporalidadeIntermediarioEfetiva() != null;
	}

	public String getReferencia() {
		return getCodigoCompacto();
	}

	/**
	 * Retorna a referência do objeto mais o extensão ".html".
	 * 
	 */
	public String getReferenciaHtml() {
		return getReferencia() + ".html";
	}

	/**
	 * Retorna a referência do objeto mais o extensão ".pdf".
	 * 
	 */
	public String getReferenciaPDF() {
		return getReferencia() + ".pdf";
	};

	/**
	 * Retorna a referência do objeto mais o extensão ".rtf".
	 * 
	 */
	public String getReferenciaRTF() {
		return getReferencia() + ".rtf";
	};

	/**
	 * Verifica se o mobil está na mesma lotação de outro
	 * 
	 */
	public boolean estaNaMesmaLotacao(ExMobil outroMobil) {
		if (getUltimaMovimentacao() != null && outroMobil.getUltimaMovimentacao() != null)
			return getUltimaMovimentacao().getLotaResp().equivale(outroMobil.getUltimaMovimentacao().getLotaResp());

		return false;
	}

	/**
	 * Retorna a destinação final deste móbil conforme o PCTT.
	 * 
	 * @return
	 */
	public ExTipoDestinacao getExDestinacaoFinal() {
		ExVia viaPCTT = getViaPCTT();
		return viaPCTT != null ? viaPCTT.getExDestinacaoFinal() : null;
	}

	/**
	 * Retorna a destinação final deste móbil conforme o PCTT, considerando a
	 * destinação de todos os outros móbiles da árvore de juntados, predominando
	 * a guarda permanente sobre a eliminação.
	 * 
	 * @return
	 */
	public ExTipoDestinacao getExDestinacaoFinalEfetiva() {
		ExTipoDestinacao destinacaoPredominante = null;
		for (ExMobil mob : getArvoreMobilesParaAnaliseDestinacao())
			if (mob.getExDestinacaoFinal() != null && mob.getExDestinacaoFinal().getIdTpDestinacao()
					.equals(ExTipoDestinacao.TIPO_DESTINACAO_GUARDA_PERMANENTE))
				return mob.getExDestinacaoFinal();
			else if (mob.isindicadoGuardaPermanente())
				return ExTipoDestinacao.guardaPermanente();
			else
				destinacaoPredominante = mob.getExDestinacaoFinal();
		return destinacaoPredominante;
	}

	/**
	 * Retorna se a destinação final do móbil é guarda permanente. Essa
	 * avaliação considera todos os móbiles juntados e a existência de indicação
	 * para guarda permanente.
	 * 
	 * @return
	 */
	public boolean isDestinacaoGuardaPermanente() {
		ExTipoDestinacao dest = getExDestinacaoFinalEfetiva();
		return dest != null && dest.getIdTpDestinacao().equals(ExTipoDestinacao.TIPO_DESTINACAO_GUARDA_PERMANENTE);
	}

	/**
	 * Retorna se a destinação final do móbil é eliminação. Essa avaliação
	 * considera todos os móbiles juntados e a existência de indicação para
	 * guarda permanente.
	 * 
	 * @return
	 */
	public boolean isDestinacaoEliminacao() {
		ExTipoDestinacao dest = getExDestinacaoFinalEfetiva();
		return dest != null && dest.getIdTpDestinacao().equals(ExTipoDestinacao.TIPO_DESTINACAO_ELIMINACAO);
	}

	/**
	 * Retorna a via da tabela de classificaçã correspondente a este móbil. Caso
	 * seja móbil de processo, a via será sempre a de número 1.
	 * 
	 * @return
	 */
	public ExVia getViaPCTT() {
		Short numVia = doc().isProcesso() || isGeral() ? 1 : getNumSequencia().shortValue();

		ExVia via = getExDocumento().via(numVia);

		if (via != null)
			return via;

		return getExDocumento().via((short) 1);
	}

	/**
	 * Retorna o móbil que representa este para fins de movimentação de
	 * destinação, ou seja, o móbil que, representando este, recebe as
	 * movimentações de destinação (arquivamento, inclusão em edital, etc). Se
	 * este móbil for uma via, ela mesma é retornada. Se for volume ou geral de
	 * processo, o geral é retornado. Geral de expediente retorna nulo, pois não
	 * sofre movimentação de destinação nem é representado por outro móbil.
	 * 
	 * @return
	 */
	private ExMobil getMobilParaMovimentarDestinacao() {
		if (isVia())
			return this;
		else if (doc().isProcesso())
			return doc().getMobilGeral();
		return null;
	}

	/**
	 * Retorna os móbiles <b>do documento deste móbil</b> que deverão ser
	 * percorridos para se concluir a temporalidade e a destinação deste móbil.
	 * Se este móbil for geral (seja de expediente ou de processo) ou volume,
	 * todos os móbiles do documento serão retornados. Se for uma via de
	 * expediente, apenas ela será retornada.
	 * 
	 * @return
	 */
	private Set<ExMobil> getMobilesDoDocParaAnaliseDestinacao() {
		SortedSet<ExMobil> set = new TreeSet<ExMobil>();
		if (doc().isProcesso() || isGeral())
			set.addAll(doc().getExMobilSet());
		else
			set.add(this);
		return set;
	}

	/**
	 * Retorna, numa lista, o conjunto de móbiles que formam uma unidade para
	 * fins de destinação. Essa lista faz varreduras horizontais (móbiles do
	 * documento) e verticais (relações de juntada entre móbiles).
	 * 
	 * @return
	 */
	public Set<ExMobil> getArvoreMobilesParaAnaliseDestinacao() {

		Set<ExMobil> set = new LinkedHashSet<ExMobil>();
		// Edson: por enquanto, comentando este trecho, pois está causando
		// terríveis
		// impactos na performance de processos grandes. Melhorar esta
		// verificação.
		/*
		 * for (ExMobil mob : getMobilesDoDocParaAnaliseDestinacao()) for
		 * (ExMobil mob2 : mob.getMobilPrincipal()
		 * .getMobilesDoDocParaAnaliseDestinacao()) for (ExMobil mob3 :
		 * mob2.getMobilETodosOsJuntados()) if (!mob3.isVolume()) set.add(mob3);
		 */
		set.add(this);
		return set;
	}

	public String getTerminacaoSigla() {
		if (isVia()) {
			final String alfabeto = "ABCDEFGHIJLMNOPQRSTUZ";

			// as vias vão até a letra 'U', se passar disso, assume letra 'Z'
			if (getNumSequencia() <= 20) {
				return alfabeto.substring(getNumSequencia() - 1, getNumSequencia());
			} else {
				return "Z";
			}
		} else if (isVolume()) {
			return "V" + (getNumSequencia() < 10 ? "0" + getNumSequencia() : getNumSequencia());
		} else {
			return "";
		}
		// return getSigla().substring(getSigla().lastIndexOf("-")+1);
	}

	public Set<ExMovimentacao> getTransferenciasPendentesDeDevolucao(ExMobil mob) {
		List<ExMovimentacao> transferencias = mob.getMovimentacoesPorTipo(3, false);
		transferencias.addAll(mob.getMovimentacoesPorTipo(6, false));
		transferencias.removeAll(mob.getMovimentacoesCanceladas());
		Set<ExMovimentacao> transferenciasComData = new TreeSet<ExMovimentacao>();

		Iterator it = transferencias.iterator();
		while (it.hasNext()) {
			ExMovimentacao mov = (ExMovimentacao) it.next();
			if (mov.getDtFimMov() != null) {
				transferenciasComData.add(mov);
			}
		}
		return transferenciasComData;
	}

	/**
	 * Retorna as {@link ExMovimentacao Movimentações} de determinado tipo e que
	 * <i>não {@link ExMovimentacao#getExMovimentacaoCanceladora() foram
	 * canceladas}</i>.
	 * 
	 * @param idTpMov Tipo de Movimentação solicitada
	 * @return As Movimentações do tipo Solicitado.
	 * @see #getMovsNaoCanceladas(long, boolean)
	 */
	public Set<ExMovimentacao> getMovsNaoCanceladas(long idTpMov) {
		return getMovsNaoCanceladas(idTpMov, false);
	}

	/**
	 * Retorna as {@link ExMovimentacao Movimentações} de determinado tipo e que
	 * <i>não {@link ExMovimentacao#getExMovimentacaoCanceladora() foram
	 * canceladas}</i>.
	 * 
	 * @param idTpMov                  Tipo de Movimentação solicitada
	 * @param apenasNaoReferenciadoras <code>true</code> se as Movimentações
	 *                                 solicitadas <b>não</b> devem fazer
	 *                                 {@link ExMovimentacao#getExMovimentacaoRef()
	 *                                 referências a outras Movimentações}. Serve
	 *                                 para, por exemplo, não retornar movimentações
	 *                                 de autenticação de anexos, mas apenas de
	 *                                 documento
	 * @return As Movimentações do tipo Solicitado.
	 * @see #getExMovimentacaoSet()
	 */
	public Set<ExMovimentacao> getMovsNaoCanceladas(long idTpMov, boolean apenasNaoReferenciadoras) {
		// Edson: o apenasNaoReferenciadoras serve para, por exemplo, não
		// retornar movimentações
		// de autenticação de anexos, mas apenas de documento
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getExMovimentacaoSet() == null)
			return set;

		for (ExMovimentacao m : getExMovimentacaoSet()) {
			if (m.getExMovimentacaoCanceladora() != null)
				continue;
			if (idTpMov > 0 && m.getExTipoMovimentacao().getIdTpMov() != idTpMov)
				continue;
			if (apenasNaoReferenciadoras && m.getExMovimentacaoRef() != null)
				continue;
			set.add(m);
		}
		return set;
	}
	
	public Set<ExMovimentacao> getMovsNaoCanceladas(long[] idTpMovs) {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getExMovimentacaoSet() == null)
			return set;
				
		for (ExMovimentacao m : getExMovimentacaoSet()) {
			for (long idTpMov : idTpMovs) {
				if (m.getExMovimentacaoCanceladora() != null)
					continue;
				if (m.getExTipoMovimentacao().getIdTpMov() != idTpMov)
					continue;
			
				set.add(m);
			}
		}			
		
		return set;
	}
	
	public static void adicionarIndicativoDeMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso() {
		isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso = true;
	}
	
	public static void removerIndicativoDeMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso() {
		isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso = false;
	}
	
	public static boolean isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso() {
		return isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso;
	}
	
	public void indicarSeDeveExibirDocumentoCompletoReordenado(boolean exibirReordenacao) {
		this.getDoc().setPodeExibirReordenacao(exibirReordenacao);
	}
	
	public boolean isModeloIncluso(Long idModelo) {
		ExModelo mod = ExDao.getInstance().consultar(idModelo, ExModelo.class, false);
		
		for (ExMovimentacao m : getExMovimentacaoReferenciaSet()) {
			if (m.getExMovimentacaoCanceladora() != null)
				continue;
			if (m.getExTipoMovimentacao().getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				continue;
			if (m.getExMobilRef() == this && m.getExMobil() != null 
					&& m.getExMobil().doc().getExModelo().getIdInicial().equals(mod.getIdInicial()))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se exibe o conteudo do documento no histórico do acompanhamento do protocolo
	 * @return
	 */
	public boolean isExibirNoAcompanhamento() {
		return podeExibirNoAcompanhamento(null, null);
	}
	
	public boolean podeExibirNoAcompanhamento(DpPessoa pessoa, DpLotacao lotacao) {
		Set<ExMovimentacao> movs = getMovsNaoCanceladas(ExTipoMovimentacao
				.TIPO_MOVIMENTACAO_EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO);
		if (!movs.isEmpty())
			return Ex.getInstance().getComp()
					.podeDisponibilizarNoAcompanhamentoDoProtocolo(pessoa, lotacao, this.getDoc());			
		return false;
	}
	
	public Set<PessoaLotacaoParser> getAtendente() {
		Pendencias p = calcularTramitesPendentes();
		
		Set<ExMovimentacao> setMov = new HashSet<>();
		setMov.addAll(p.tramitesPendentes);
		setMov.addAll(p.recebimentosPendentes);
		setMov.removeAll(p.tramitesDeNotificacoesPendentes);
		setMov.removeAll(p.recebimentosDeNotificacoesPendentes);
		
		return calcularAtendentes(setMov, p.fIncluirCadastrante); 
	}

	public Set<PessoaLotacaoParser> getNotificados() {
		Pendencias p = calcularTramitesPendentes();
		
		Set<ExMovimentacao> setMov = new HashSet<>();
		setMov.addAll(p.tramitesDeNotificacoesPendentes);
		setMov.addAll(p.recebimentosDeNotificacoesPendentes);
		
		return calcularAtendentes(setMov, false); 
	}

	public Set<PessoaLotacaoParser> calcularAtendentes(Set<ExMovimentacao> setMov, boolean fIncluirCadastrante) {
		Set<PessoaLotacaoParser> set = new HashSet<>();
		for (ExMovimentacao mov : setMov) {
			DpPessoa pes = mov.getResp();
			DpLotacao lot = mov.getLotaResp();
			if (pes != null || lot != null) {
				set.add(new PessoaLotacaoParser(pes, lot));
			}
		}
		// Cadastrante é o atendente quando o móbil ainda não foi movimentado
		if (fIncluirCadastrante) {
			set.add(new PessoaLotacaoParser(doc().getCadastrante(), doc().getLotaCadastrante()));
		}
		return set;
	}
	
	public boolean isEmTramiteParalelo() {
		return getAtendente().size() > 1;
	}
	
	public boolean isAtendente(DpPessoa pessoa, DpLotacao lotacao) {
		Set<PessoaLotacaoParser> set = getAtendente();
		return equivalePessoaOuLotacao(pessoa, lotacao, set);
	}

	public boolean isNotificado(DpPessoa pessoa, DpLotacao lotacao) {
		Set<PessoaLotacaoParser> set = getNotificados();
		return equivalePessoaOuLotacao(pessoa, lotacao, set);
	}
	
	private boolean equivalePessoaOuLotacao(DpPessoa pessoa, DpLotacao lotacao, Set<PessoaLotacaoParser> set) {
		for (PessoaLotacaoParser pl : set) {
			if (Utils.equivale(pl.getPessoa(), pessoa))
				return true;
			if (Utils.equivale(pl.getLotacao(), lotacao))
				return true;
		}
		return false;
	}

	public static class Pendencias {
		// Trâmite serial, paralelo e notificações
		public Set<ExMovimentacao> tramitesPendentes = new TreeSet<ExMovimentacao>();
		// Trâmite serial, paralelo e notificações recebidos e ainda não concluídos
		public Set<ExMovimentacao> recebimentosPendentes = new TreeSet<ExMovimentacao>();
		// Somente notificações
		public Set<ExMovimentacao> tramitesDeNotificacoesPendentes = new TreeSet<ExMovimentacao>();
		// Somente notificações recebidas e ainda não concluídos
		public Set<ExMovimentacao> recebimentosDeNotificacoesPendentes = new TreeSet<ExMovimentacao>();
		// Indica se o cadastrante do documento deve ser incluído na lista de atendentes 
		public boolean fIncluirCadastrante = true;
	}
	
	public Pendencias calcularTramitesPendentes() {
		Pendencias p = new Pendencias();
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			long t = mov.getIdTpMov();
			if ((t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA 
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRAMITE_PARALELO 
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO)) {
				p.tramitesPendentes.add(mov);
			}
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO) {
				// Recebimento sem movRef limpa todos os pendentes até agora
				if (mov.getExMovimentacaoRef() == null)
					p.tramitesPendentes.clear();
				else { 
					if (mov.getExMovimentacaoRef() != null)
						p.tramitesPendentes.remove(mov.getExMovimentacaoRef());
					p.recebimentosPendentes.add(mov);
				}
			}
			if (mov.getExMovimentacaoRef() != null) {
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONCLUSAO) {
					// Existe a conclusão direta, que cancela um trâmite pendente, ou a conclusão
					// normal que cancela um recebimento pendente
					p.tramitesPendentes.remove(mov.getExMovimentacaoRef());
					p.recebimentosPendentes.remove(mov.getExMovimentacaoRef());
				} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA) {
					// Também existe a possibilidade de cancelar um recebimento pendente tramitando
					// serialmente para outro lugar
					p.recebimentosPendentes.remove(mov.getExMovimentacaoRef());
				}
			} else {
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONCLUSAO) 
					p.fIncluirCadastrante = false;
			}
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA 
					&& (Utils.equivale(mov.getCadastrante(), doc().getCadastrante()) 
							|| Utils.equivale(mov.getLotaCadastrante(), doc().getLotaCadastrante()))) 
				p.fIncluirCadastrante = false;
		}
		
		for (ExMovimentacao mov : p.tramitesPendentes) {
			if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO)
				p.tramitesDeNotificacoesPendentes.add(mov);
		}
		for (ExMovimentacao mov : p.recebimentosPendentes) {
			if (mov.getExMovimentacaoRef() == null)
				continue;
			if (mov.getExMovimentacaoRef().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO)
				p.tramitesDeNotificacoesPendentes.add(mov);
		}
		return p;
	}
	
}