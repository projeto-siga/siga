package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Comparator;

public enum Ordenacao {
	
	LOCALIDADE{
		@Override
		public Comparator<Materia> getComparator() {
			if (instance == null)
				instance = new Comparator<Materia>(){
				public int compare(Materia o1, Materia o2) {
					return o1.getLocalidade().compareTo(o2.getLocalidade());
				}
			};
			return instance;
		}
	},
	DATA{
		@Override
		public Comparator<Materia> getComparator() {
			if (instance == null)
				instance = new Comparator<Materia>(){
				public int compare(Materia o1, Materia o2) {
					if (o1.getDt().after(o2.getDt()))
						return 1;
					else if (o1.getDt().before(o2.getDt()))
						return -1;
					else
						return 0;
				}
			};
			return instance;
		}
	},
	TIPO_E_DATA{
		@Override
		public Comparator<Materia> getComparator() {
			if (instance == null) 
				instance = new Comparator<Materia>(){
				public int compare(Materia o1, Materia o2) {
					return o1.getTipoMateria().compareTo(o2.getTipoMateria());
				}
			};
			return instance;
		}
	};

	protected Comparator<Materia> instance;
	public abstract Comparator<Materia> getComparator();
}
