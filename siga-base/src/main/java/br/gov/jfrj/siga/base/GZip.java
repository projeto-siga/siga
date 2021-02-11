package br.gov.jfrj.siga.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZip {
	public static byte[] compress(byte[] str) throws IOException {
		if (str == null || str.length == 0) {
			return null;
		}
//		System.out.println("String length : " + str.length);
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(obj);
		gzip.write(str);
		gzip.close();
		return obj.toByteArray();
	}

	public static byte[] decompress(byte[] str) throws IOException {
		int BUFFER = 8192;
		byte data[] = new byte[BUFFER];
		int count;
		byte out[];

		if (str == null || str.length == 0) {
			return null;
		}
//		System.out.println("Input byte[] length : " + str.length);
		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));

		// write the files to the disk
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		while ((count = gis.read(data, 0, BUFFER)) != -1) {
			fos.write(data, 0, count);
		}
		fos.flush();
		fos.close();
		out = fos.toByteArray();

//		System.out.println("Output String lenght : " + out.length);
		return out;
	}
}
