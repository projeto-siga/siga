package br.gov.jfrj.siga.cp;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CP_UNIDADE_MEDIDA", schema = "CORPORATIVO")
public class CpUnidadeMedida extends AbstractCpUnidadeMedida {

	final static public int ANO = 1;

	final static public int MES = 2;

	final static public int DIA = 3;

}
