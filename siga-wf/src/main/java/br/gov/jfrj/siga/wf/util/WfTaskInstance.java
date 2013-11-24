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
package br.gov.jfrj.siga.wf.util;

import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfTaskInstance extends TaskInstance {
	private static final long serialVersionUID = 4328916761067641066L;

	public Long getDays() throws Exception {
		if (getCreate() == null)
			return null;

		SigaCalendar lIni = new SigaCalendar(getCreate().getTime());
		SigaCalendar lFim = new SigaCalendar(WfDao.getInstance().dt().getTime());
		long l = lFim.getUnixDay() - lIni.getUnixDay();

		return l;
	}
}
