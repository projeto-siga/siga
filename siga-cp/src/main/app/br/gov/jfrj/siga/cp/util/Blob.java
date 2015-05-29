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
package br.gov.jfrj.siga.cp.util;

import java.sql.SQLException;

public class Blob {

	public static byte[] toByteArray(java.sql.Blob fromBlob) {
		if (fromBlob == null)
			return null;
		try {
			int blobLength = (int) fromBlob.length();
			byte[] blobAsBytes = fromBlob.getBytes(1, blobLength);

			// Foi necessario comentar o .free() pois estava gerando uma
			// mensagem de erro quando os documentos eram finalizados.
			//
			// release the blob and free up memory. (since JDBC 4.0)
			// fromBlob.free();
			return blobAsBytes;
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível ler um BLOB.", e);
		}

		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// InputStream is = null;
		//
		// if (fromBlob == null)
		// return null;
		// else {
		// try {
		// byte[] buf = new byte[4000];
		// is = fromBlob.getBinaryStream();
		// for (;;) {
		// int dataSize = is.read(buf);
		// if (dataSize == -1)
		// break;
		// baos.write(buf, 0, dataSize);
		// }
		// } catch (SQLException e) {
		// throw new RuntimeException(e);
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// } finally {
		// if (baos != null) {
		// try {
		// baos.close();
		// } catch (IOException ex) {
		// }
		// }
		// if (is != null) {
		// try {
		// is.close();
		// } catch (IOException ex) {
		// int a = 0;
		// }
		// }
		// }
		// return baos.toByteArray();
		// }
	}
}
