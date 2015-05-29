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
package br.gov.jfrj.siga.sinc.lib;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Objeto;

public abstract class SincronizavelSuporte extends Objeto implements
		Sincronizavel, Serializable {
	private Long id;
	private Long idInicial;
	private String idExterna;
	private Date dataInicio;
	private Date dataFim;
	private String loteDeImportacao;

	public String getDescricaoExterna() {
		return this.getClass().getName() + ": " + this.getIdExterna();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdInicial() {
		return idInicial;
	}

	public void setIdInicial(Long idInicial) {
		this.idInicial = idInicial;
	}

	public String getIdExterna() {
		return idExterna;
	}

	public void setIdExterna(String idExterna) {
		this.idExterna = idExterna;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getLoteDeImportacao() {
		return loteDeImportacao;
	}

	public void setLoteDeImportacao(String loteDeImportacao) {
		this.loteDeImportacao = loteDeImportacao;
	}

	public int getNivelDeDependencia() {
		return getNivelDeDependencia(this);
	}

	public static int getNivelDeDependencia(Sincronizavel s) {
		int nivel = 0;
		Class cls = s.getClass();

		do {
			try {
				Field fieldlist[] = cls.getDeclaredFields();
				for (int i = 0; i < fieldlist.length; i++) {
					Field fld = fieldlist[i];
					if (((fld.getModifiers() & Modifier.STATIC) != 0)
							|| (fld.getAnnotation(Desconsiderar.class) != null))
						continue;

					fld.setAccessible(true);
					Object o1 = fld.get(s);
					if (o1 != null) {
						if (o1 instanceof Sincronizavel) {
							int n = ((Sincronizavel) o1)
									.getNivelDeDependencia();
							if (nivel <= n)
								nivel = n + 1;
						} else if (o1 instanceof Collection<?>) {
							for (Object o2 : (Collection<?>) o1) {
								if (o2 instanceof Sincronizavel) {
									int n = ((Sincronizavel) o2)
											.getNivelDeDependencia();
									if (nivel <= n)
										nivel = n + 1;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			cls = cls.getSuperclass();
		} while (!cls.equals(Object.class));

		return nivel;
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return semelhante(this, obj, nivel);
	}

	public static boolean semelhante(Assemelhavel s, Assemelhavel obj, int nivel) {
		s = (Assemelhavel) Objeto.getImplementation(s);
		obj = (Assemelhavel) Objeto.getImplementation(obj);

		Class cls = s.getClass();

		if (obj == null)
			return false;

		// if (s.getClass().getName().contains("DpCargo")) {
		// if (((Sincronizavel) s).getIdExterna().equals("2101"))
		// System.out.println("teste");
		// }

		do {
			try {
				Field fieldlist[] = cls.getDeclaredFields();
				for (int i = 0; i < fieldlist.length; i++) {
					Field fld = fieldlist[i];
					fld.setAccessible(true);
					// if (fld.getName().equals("ideCargo")) {
					// Object o = fld.get(s);
					// if (o.equals("2101"))
					// System.out.println("teste");
					// }

					if (((fld.getModifiers() & Modifier.STATIC) != 0)
							|| fld.isAnnotationPresent(Desconsiderar.class)
							|| (fld.isAnnotationPresent(DesconsiderarParaSemelhanca.class) && condicaoValida(
									fld, DesconsiderarParaSemelhanca.class,s))
							|| (fld.isAnnotationPresent(NaoPropagar.class) && (nivel > 0)))
						continue;

					Object o1 = fld.get(s);
					Object o2 = fld.get(obj);

					if (o1 == null) {
						if (o2 != null)
							return false;
					} else {
						if (o2 == null)
							return false;
						if (o1 instanceof Sincronizavel) {
							if (((Sincronizavel) o1).getIdExterna() == null)
								return false;
							if (!((Sincronizavel) o1).getIdExterna().equals(
									((Sincronizavel) o2).getIdExterna()))
								return false;
							if (fld.isAnnotationPresent(NaoRecursivo.class))
								continue;
							if (!((Assemelhavel) o1).semelhante(
									(Assemelhavel) o2, nivel + 1))
								return false;
						} else if (o1 instanceof Assemelhavel) {
							if (fld.isAnnotationPresent(NaoRecursivo.class))
								continue;
							if (!((Assemelhavel) o1).semelhante(
									(Assemelhavel) o2, nivel + 1))
								return false;
						} else if (o1 instanceof Date) {
							if (!o1.equals(o2)) {
								// Nato: Esse "if" corrige um problema que
								// ocorre na meia noite de um dia de mudança
								// para o horário de verão.
								if (((Date) o2).getTimezoneOffset()
										- ((Date) o1).getTimezoneOffset() != 60
										|| (((Date) o1).getTime() - ((Date) o2)
												.getTime()) != 3600000)
									return false;
							}
						} else if (o1 instanceof String) {
							// Nato: Esse "if" corrige um problema que
							// ocorre quando existe um campo no banco de dados
							// que é do tipo CHAR, em vez de VARCHAR2.
							if (!((String) o1).trim().equals(
									((String) o2).trim())) {
								return false;
							}
						} else if (o1 instanceof Collection) {
							Collection c1 = (Collection) o1;
							Collection c2 = (Collection) o2;

							if (!semelhante(c1, c2, nivel + 1))
								return false;
						} else {
							if (!Objeto.getImplementation(o1).equals(
									Objeto.getImplementation(o2)))
								// if (!o1.equals(o2))
								return false;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			cls = cls.getSuperclass();
		} while (!cls.equals(Object.class));
		return true;
	}

	private static boolean condicaoValida(Field fld,
			Class<DesconsiderarParaSemelhanca> clazz, Assemelhavel obj) {
		String[] condicao = fld.getAnnotation(clazz).condicao();
		if(condicao[0].length()==0){
			return true;
		}
		try {
			String campoCondicao = condicao[0];
			Boolean valorCondicao = Boolean.valueOf(condicao[1]);

			Field f =  fld.getDeclaringClass().getDeclaredField(campoCondicao);
			f.setAccessible(true);
			return f.getBoolean(obj) == valorCondicao;

		} catch (Exception e) {
			return false;
		}

	}

	private static boolean semelhante(Collection c1, Collection c2, int nivel) {
		Iterator<Sincronizavel> i1 = c1.iterator();
		Iterator<Sincronizavel> i2 = c2.iterator();

		Sincronizavel o1 = null;
		Sincronizavel o2 = null;

		if (i2.hasNext())
			o2 = i2.next();
		if (i1.hasNext())
			o1 = i1.next();
		while (o2 != null || o1 != null) {
			if ((o2 == null)
					|| (o1 != null && ((Comparable) o2).compareTo(o1) > 0)) {
				// O novo não existe entre os antigos, portanto deve ser
				// incluido
				return false;
			} else if (o1 == null
					|| (o2 != null && ((Comparable) o1).compareTo(o2) > 0)) {
				// O corp não existe no xml
				return false;
			} else {
				if (o2 == null) {
					int i = 0;
				}
				// O registro existe no corp e no xml
				if (!o1.semelhante(o2, nivel + 1))
					return false;
				if (i1.hasNext())
					o1 = i1.next();
				else
					o1 = null;
				if (i2.hasNext())
					o2 = i2.next();
				else
					o2 = null;
			}
		}
		i1 = null;
		i2 = null;
		return true;
	}

	/**
	 * retorna se ativo na data
	 * 
	 * @param dt
	 *            data quando ser saber se estava ativo
	 * @return true or false
	 */
	public boolean ativoNaData(Date dt) {
		if (dt == null)
			return this.getDataFim() == null;
		if (dt.before(this.getDataInicio()))
			return false;
		// dt >= DtIni
		if (this.getDataFim() == null)
			return true;
		if (dt.before(this.getDataFim()))
			return true;
		return false;
	}
}
