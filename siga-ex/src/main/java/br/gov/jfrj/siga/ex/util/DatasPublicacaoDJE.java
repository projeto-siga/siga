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
package br.gov.jfrj.siga.ex.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.dp.CpFeriado;

public class DatasPublicacaoDJE {

	private Date dataDisponibilizacao;
	private Date dataPublicacao;
	private Date hojeMeiaNoite;
	private Long difHojeDisponib;
	private CpFeriado feriadoPublicacao;
	private CpFeriado feriadoDisponibilizacao;

	// Necessário para verificação do dia da semana
	private Calendar calPublicacao;

	// Necessário para verificação do dia da semana
	private Calendar calDisponibilizacao;

	// Necessário para verificação do horário limite
	private Calendar calHoje;

	public DatasPublicacaoDJE(Date dataDisponib) throws AplicacaoException {
		if (dataDisponib == null)
			throw new AplicacaoException(
					"Não foi informada uma data de disponibilização para cálculos");
		setDataDisponibilizacao(dataDisponib);
	}

	public String validarDataDeDisponibilizacao(boolean apenasSolicitacao)
			throws AplicacaoException {
		if (getDataDisponibilizacao() == null)
			throw new AplicacaoException(
					"Não foi informada uma data de disponibilização para cálculos");

		Date proximaDataDisponivel = null;
		
		if(!(foraDoHorarioDe11as17horas() && apenasSolicitacao))
			proximaDataDisponivel = consultarProximaDataDisponivel();
		
		if(proximaDataDisponivel != null && getDataDisponibilizacao().before(proximaDataDisponivel)) {
			SimpleDateFormat formatBra = new SimpleDateFormat("dd/MM/yyyy");  
			return "Data de disponibilização inferior a próxima data disponível. Próxima data para disponibilização ￩ " + formatBra.format(proximaDataDisponivel) + ".";
		}
		
		if(proximaDataDisponivel == null || getDataDisponibilizacao().after(proximaDataDisponivel)) {
		
			if (isDisponibilizacaoDMais30())
				return "Data de disponibilização está além do limite: mais de 31 dias a partir de hoje";
			else if (foraDoHorarioDe11as17horas() && apenasSolicitacao)
				return "Data de disponibilização não permitida: Excedido Horário de Solicitação (17 Horas). Defina a disponibilização com mais de 2 dias a partir de hoje";
			else if (isDisponibilizacaoFeriado())
				return "Data de disponibilização ￩ feriado: "
						+ getFeriadoDisponibilizacao().getDscFeriado();
			else if (isDisponibilizacaoAntesDeDMais2())
				return "Data de disponibilização não permitida: menos de 2 dias a partir de hoje";
			else if (isDisponibilizacaoDomingo())
				return "Data de disponibilização é domingo";
			else if (isDisponibilizacaoSabado())
				return "Data de disponibilização é sábado";
			else if (isDisponibilizacaoFeriado())
				return "Data de disponibilização ￩ feriado: "
						+ getFeriadoDisponibilizacao().getDscFeriado();
		}

		while (true) {
			if (isPublicacaoDomingo() || isPublicacaoSabado()
					|| isPublicacaoFeriado())
				setDataPublicacao(adicionaUmDia(getDataPublicacao()));
			else
				break;
		}

		return null;
	}
	
	public static Date consultarProximaDataDisponivel() {
		try {
			
			StringBuilder corpo = new StringBuilder();
			corpo.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">");
			corpo.append("<soapenv:Header/>");
			corpo.append("<soapenv:Body>");
			corpo.append("<tem:ProximaDataDisponivel/>");
			corpo.append("</soapenv:Body>");
			corpo.append("</soapenv:Envelope>");
			
			String retorno = FuncoesEL.webservice(Prop.get("dje.servidor.data.disponivel"), corpo.toString(), 6000);
			String tagDataInicial = "<ProximaDataDisponivelResult>";
			String tagDataFinal = "</ProximaDataDisponivelResult>";
			String proximaDataDisponibilizacaoString = null; 
			Date proximaDataDisponibilizacao;
			
			
			if(retorno != null && 
					retorno.contains(tagDataInicial)  && 
					retorno.contains(tagDataFinal)) {
				
				proximaDataDisponibilizacaoString = retorno.substring(retorno.indexOf(tagDataInicial) + tagDataInicial.length(), retorno.indexOf(tagDataFinal));
				
				if(proximaDataDisponibilizacaoString != null) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					proximaDataDisponibilizacao = formatter.parse(proximaDataDisponibilizacaoString);
					return proximaDataDisponibilizacao;
				}
			}
		} catch (Exception e) {
			return null;
		}
		
		return null;
	}
	
	public static String consultarProximaDataDisponivelString() {
		try {
			Date proximaDataDisponivel = consultarProximaDataDisponivel();
				
			if(proximaDataDisponivel != null) {
				SimpleDateFormat formatBra = new SimpleDateFormat("dd/MM/yyyy");  
				return formatBra.format(proximaDataDisponivel);
			}
		} catch (Exception e) {
		   return "";
		}
	   
	   return "";
	}

	private Date adicionaUmDia(Date data) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	public boolean isDisponibilizacaoDMais30() {
		return getDifHojeDisponib() > 30;
	}

	public boolean isDisponibilizacaoAntesDeDMais2() {
		return getDifHojeDisponib() < 2;
	}

	public boolean isDisponibilizacaoDMais2() {
		return getDifHojeDisponib() == 2;
	}

	public boolean isDisponibilizacaoMaiorQueDMais1() {
		return getDifHojeDisponib() > 1;
	}

	public boolean isPublicacaoDomingo() {
		return (getCalPublicacao().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

	public boolean isPublicacaoSabado() {
		return (getCalPublicacao().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
	}

	public boolean isPublicacaoFeriado() {
		return getFeriadoPublicacao() != null;
	}

	public boolean isDisponibilizacaoDomingo() {
		return (getCalDisponibilizacao().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

	public boolean isDisponibilizacaoSabado() {
		return (getCalDisponibilizacao().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
	}

	public boolean isDisponibilizacaoFeriado() {
		return getFeriadoDisponibilizacao() != null;
	}

	public boolean sao17Horas() {
		int hora = getCalHoje().get(Calendar.HOUR_OF_DAY);
		return (hora >= 17 && getDifHojeDisponib() <= 2);
	}
	
	public boolean foraDoHorarioDe11as17horas() {
		int hora = getCalHoje().get(Calendar.HOUR_OF_DAY);
		return ((hora < 11 || hora >= 17) && getDifHojeDisponib() <= 2);
	}
	
	public Date getDataDisponibilizacao() {
		return dataDisponibilizacao;
	}

	public void setDataDisponibilizacao(Date dataDisponibilizacao) {
		this.dataDisponibilizacao = dataDisponibilizacao;
		setDataPublicacao(null);
		setCalDisponibilizacao(null);
		setFeriadoDisponibilizacao(null);
		setDifHojeDisponib(null);
	}

	private Date getHojeMeiaNoite() {
		if (hojeMeiaNoite == null) {
			Calendar calHojeMeiaNoite = new GregorianCalendar();
			calHojeMeiaNoite.setTime(new Date());
			calHojeMeiaNoite.set(Calendar.HOUR_OF_DAY, 0);
			calHojeMeiaNoite.set(Calendar.MINUTE, 0);
			calHojeMeiaNoite.set(Calendar.SECOND, 0);
			calHojeMeiaNoite.set(Calendar.MILLISECOND, 0);
			setHojeMeiaNoite(calHojeMeiaNoite.getTime());
		}
		return hojeMeiaNoite;
	}

	private long getDifHojeDisponib() {
		Long diasUteis, diasInuteis, diferencaEmMilisegundos;
		if (difHojeDisponib == null) {
			
			diferencaEmMilisegundos = getDataDisponibilizacao().getTime() - getHojeMeiaNoite().getTime();
			
			//Início de Horário de Verão.
			if(diferencaEmMilisegundos == 342000000L)
				diferencaEmMilisegundos += 3600000L; 
			
			diasUteis = (diferencaEmMilisegundos) / 86400000;
			diasInuteis = getNumeroDiasInuteis(getHojeMeiaNoite(),
					getDataDisponibilizacao());

			setDifHojeDisponib(diasUteis - diasInuteis);
		}
		return difHojeDisponib;
	}

	public Date getDataPublicacao() {
		if (dataPublicacao == null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(getDataDisponibilizacao());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			setDataPublicacao(cal.getTime());
		}
		return dataPublicacao;
	}

	private void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
		setCalPublicacao(null);
		setFeriadoPublicacao(null);
	}

	private void setHojeMeiaNoite(Date hojeMeiaNoite) {
		this.hojeMeiaNoite = hojeMeiaNoite;
	}

	private void setDifHojeDisponib(Long difHojeDisponib) {
		this.difHojeDisponib = difHojeDisponib;
	}

	private Calendar getCalPublicacao() {
		if (calPublicacao == null) {
			GregorianCalendar provCal = new GregorianCalendar();
			provCal.setTime(getDataPublicacao());
			setCalPublicacao(provCal);
		}
		return calPublicacao;
	}

	private void setCalPublicacao(Calendar calPublicacao) {
		this.calPublicacao = calPublicacao;
	}

	public CpFeriado getFeriadoPublicacao() {
		if (feriadoPublicacao == null) {
			setFeriadoPublicacao(Feriados.verificarPorData(getDataPublicacao()));
		}
		return feriadoPublicacao;
	}

	private void setFeriadoPublicacao(CpFeriado feriadoPublicacao) {
		this.feriadoPublicacao = feriadoPublicacao;
	}

	public Calendar getCalHoje() {
		if (calHoje == null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			setCalHoje(cal);
		}
		return calHoje;
	}

	private void setCalHoje(Calendar calHoje) {
		this.calHoje = calHoje;
	}

	private Calendar getCalDisponibilizacao() {
		if (calDisponibilizacao == null) {
			GregorianCalendar provCal = new GregorianCalendar();
			provCal.setTime(getDataDisponibilizacao());
			// Elimina o risco de erro na contagem por causa do início do
			// horário de verão:
			provCal.add(Calendar.HOUR_OF_DAY, 3);
			setCalDisponibilizacao(provCal);
		}
		return calDisponibilizacao;
	}

	private void setCalDisponibilizacao(Calendar calDisponibilizacao) {
		this.calDisponibilizacao = calDisponibilizacao;
	}

	public CpFeriado getFeriadoDisponibilizacao() {
		if (feriadoDisponibilizacao == null) {
			setFeriadoDisponibilizacao(Feriados
					.verificarPorData(getDataDisponibilizacao()));
		}
		return feriadoDisponibilizacao;
	}

	public void setFeriadoDisponibilizacao(CpFeriado feriadoDisponibilizacao) {
		this.feriadoDisponibilizacao = feriadoDisponibilizacao;
	}

	private Long getNumeroDiasInuteis(Date pDataInicio, Date pDataFim) {
		SigaCalendar dataInicio = new SigaCalendar();
		SigaCalendar dataFim = new SigaCalendar();

		dataInicio.setTime(pDataInicio);
		dataFim.setTime(pDataFim);

		int diasInuteis = 0;
		Calendar ini = new SigaCalendar(dataInicio.getTimeInMillis());
		while (ini.compareTo(dataFim) <= 0) {
			if (Feriados.verificarPorData(ini.getTime()) != null
					|| ini.get(SigaCalendar.DAY_OF_WEEK) == SigaCalendar.SATURDAY
					|| ini.get(SigaCalendar.DAY_OF_WEEK) == SigaCalendar.SUNDAY) {
				diasInuteis++;
				// System.out.println(inicio.getTime());
			}
			ini.add(SigaCalendar.DAY_OF_MONTH, 1);
		}

		return Long.valueOf(diasInuteis);
	}

}
