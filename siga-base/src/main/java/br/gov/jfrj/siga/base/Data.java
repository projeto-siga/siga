package br.gov.jfrj.siga.base;

import java.util.Date;

public class Data {
	
	// Verifica se a data está entre o ano 2000 e o ano 2100
		@SuppressWarnings("deprecation")
		public static Boolean dataDentroSeculo21(Date data) {
			
			if ((data.before(new Date(100, 0, 1)) || data.after(new Date(200, 0, 1)))) {
				return false;
			} else {
				return true;
			}
			
		}
		

}
