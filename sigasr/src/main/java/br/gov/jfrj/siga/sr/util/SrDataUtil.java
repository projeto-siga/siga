package br.gov.jfrj.siga.sr.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SrDataUtil {

	private static List<CpFeriado> listaFeriados;

	public static List<CpFeriado> getListaFeriados() {
		if (listaFeriados == null) {
			try{
				CpDao dao = CpDao.getInstance();
				listaFeriados = dao.listarFeriados();
				for (CpFeriado f : listaFeriados)
					f.getCpOcorrenciaFeriadoSet().size();
			} catch(Exception e){
				listaFeriados = new ArrayList<CpFeriado>();
			}
		}
		return listaFeriados;
	}

	public static void setListaFeriados(List<CpFeriado> listaFeriados) {
		SrDataUtil.listaFeriados = listaFeriados;
	}
 
	public static boolean isFinalDeSemana(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
	}
	
	public static boolean isFeriado(Date data) {
		for (CpFeriado f : getListaFeriados())
			if (f.abrange(data))
				return true;
		return false; 
	}
	
	public static Date addDia(Date data, int i) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(data); 
		c.add(Calendar.DATE, i);
		return c.getTime();
	}
	
	public static int getHora(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinuto(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.get(Calendar.MINUTE);
	}
	
	public static Date getDataComHorario(Date d, int h, int min, int s, int m) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, s);
		c.set(Calendar.MILLISECOND, m);
		return c.getTime();
	}
}
