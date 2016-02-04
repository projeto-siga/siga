/*
 * The Nheengatu Project : a free Java library for HTML  abstraction.
 *
 * Project Info:  http://www.aryjr.com/nheengatu/
 * Project Lead:  Ary Rodrigues Ferreira Junior
 *
 * (C) Copyright 2005, 2006 by Ary Junior
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.aryjr.nheengatu.pdf;

import java.util.ArrayList;

/**
 * 
 * A cell widths collection.
 * 
 * @version $Id: CellWidths.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class CellWidths extends ArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3442175852961231731L;

	public float[] toFloatArray() {
		final float[] cws = new float[size()];
		for (int inc = 0; inc < size(); inc++) {
			cws[inc] = ((Float) get(inc)).floatValue();
		}
		return cws;
	}

}
/**
 * 
 * $Log: CellWidths.java,v $
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:47  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:46  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:42 tah Utilizando o
 * nheengatu
 * 
 * Revision 1.2 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.1 2005/12/22 15:15:19 aryjr Fixed many bugs.
 * 
 * 
 */
