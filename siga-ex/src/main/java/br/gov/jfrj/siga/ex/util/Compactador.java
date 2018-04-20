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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Compactador {
	/**
	 * Deleta o diretório informado e todos os arquivos e diretórios abaixo
	 * deste.
	 * 
	 * @param filename
	 *            String - nome do diretório a ser apagado
	 * @return true se obteve sucesso
	 */
	public static boolean deleteDir(final String filename) {
		final File f = new File(filename);
		return Compactador.deleteDir(f);
	}

	/**
	 * Deleta o diretório informado e todos os arquivos e diretórios abaixo
	 * deste.
	 * 
	 * @param dir
	 *            File - objeto File que representa o diretório a ser apagado
	 * @return true se obteve sucesso
	 */
	public static boolean deleteDir(final File dir) {
		if (dir.isDirectory()) {
			final String[] children = dir.list();
			for (String element : children) {
				boolean success = Compactador.deleteDir(new File(dir, element));
				if (!success)
					return false;
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Adiciona um arquivo novo a um arquivo compactado já existente
	 * 
	 * @param arqEntrada -
	 *            String com o path do arquivo a ser adicionado. O path só pode
	 *            ser do diretório atual ou de subdiretórios do diretório atual.
	 * @param zipSaida -
	 *            path do arquivo compactado já existente
	 */
	public void adicionar(final String arqEntrada, final String zipSaida) {
		final ArrayList<String> arqEntradas = new ArrayList<String>();
		arqEntradas.add(arqEntrada);
		adicionar(arqEntradas, zipSaida);
	}

	/**
	 * Adiciona arquivos novos a um arquivo compactado já existente
	 * 
	 * @param arqEntrada -
	 *            ArrayList com o path dos arquivos a ser adicionados. Os paths
	 *            só podem ser do diretório atual ou de subdiretórios do
	 *            diretório atual.
	 * @param zipSaida -
	 *            path do arquivo compactado já existente
	 */
	public void adicionar(final ArrayList<String> arqEntrada, final String zipSaida) {
		final ArrayList<String> arqExist = new ArrayList<String>();
		final String dirtemp = "ziptemp\\";
		for (int i = 0; i < arqEntrada.size(); i++) {
			arqEntrada.set(i, arqEntrada.get(i).replace("/", "\\"));
		}
		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new FileInputStream(zipSaida));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			(new File(dirtemp)).mkdirs();

			while (entry != null) {

				if (entry.isDirectory()) {
					// Create a directory; all non-existent ancestor directories
					// are
					// automatically created
					(new File(dirtemp + entry.getName())).mkdirs();
				} else {
					if (entry.getName().contains("\\")) {
						(new File(dirtemp + entry.getName())).getParentFile().mkdirs();
					}
					// Open the output file
					final String outFilename = dirtemp + entry.getName();
					try (OutputStream out = new FileOutputStream(outFilename)) {
					if (!arqEntrada.contains(entry.getName())) {
						arqExist.add(outFilename);
					}
					// Transfer bytes from the ZIP file to the output file
					final byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					}				}
				// Get the next entry
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();
		} catch (final IOException e) {
		}
		// Create a buffer for reading the files
		final byte[] buf = new byte[1024];

		try {
			// Create the ZIP file
			final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipSaida));

			// Compress the files
			for (int i = 0; i < arqEntrada.size(); i++) {
				arqEntrada.set(i, arqEntrada.get(i).replace("/", "\\"));
				final File f = new File(arqEntrada.get(i));
				if (!f.exists()) {
					continue;
				}
				final FileInputStream in = new FileInputStream(f);

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(f.getPath()));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}
			for (int i = 0; i < arqExist.size(); i++) {
				final File f = new File(arqExist.get(i));
				final FileInputStream in = new FileInputStream(f);

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(f.getPath().substring(dirtemp.length())));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
		} catch (final IOException e) {
		}
		Compactador.deleteDir(dirtemp);
	}

	/**
	 * Remove um arquivo de um arquivo compactado já existente
	 * 
	 * @param arqRemover -
	 *            String com o path do arquivo a ser removido. O path só podem
	 *            ser do diretório atual ou de subdiretórios do diretório atual.
	 * @param zipSaida -
	 *            path do arquivo compactado já existente
	 */
	public void remover(final String arqRemover, final String zipSaida) {
		final ArrayList<String> arqRemover2 = new ArrayList<String>();
		arqRemover2.add(arqRemover);
		remover(arqRemover2, zipSaida);
	}

	/**
	 * Remove arquivos de um arquivo compactado já existente
	 * 
	 * @param arqRemover -
	 *            ArrayList com o path dos arquivos a ser removidos. Os paths só
	 *            podem ser do diretório atual ou de subdiretórios do diretório
	 *            atual.
	 * @param zipSaida -
	 *            path do arquivo compactado já existente
	 */
	public void remover(final ArrayList<String> arqRemover, final String zipSaida) {

		final String dirtemp = "ziptemp\\";
		final ArrayList<String> arqExist = new ArrayList<String>();
		for (int i = 0; i < arqRemover.size(); i++) {
			arqRemover.set(i, arqRemover.get(i).replace("/", "\\"));
		}
		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new FileInputStream(zipSaida));

			(new File(dirtemp)).mkdirs();

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {

				if (entry.isDirectory()) {
					// Create a directory; all non-existent ancestor directories
					// are
					// automatically created
					(new File(dirtemp + entry.getName())).mkdirs();
				} else {
					if (entry.getName().contains("\\")) {
						(new File(dirtemp + entry.getName())).getParentFile().mkdirs();
					}
					// Open the output file
					final String outFilename = dirtemp + entry.getName();
					final OutputStream out = new FileOutputStream(outFilename);
					if (!arqRemover.contains(entry.getName())) {
						arqExist.add(outFilename);
					}

					// Transfer bytes from the ZIP file to the output file
					final byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.close();
				}
				// Get the next entry
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();
		} catch (final IOException e) {
		}
		// Create a buffer for reading the files
		final byte[] buf = new byte[1024];

		try {
			// Create the ZIP file
			final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipSaida));

			for (int i = 0; i < arqExist.size(); i++) {
				final File f = new File(arqExist.get(i));
				final FileInputStream in = new FileInputStream(f);

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(f.getPath().substring(dirtemp.length())));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
		} catch (final IOException e) {
		}
		Compactador.deleteDir(dirtemp);
	}

	/**
	 * Compacta um arquivo em um arquivo do tipo zip
	 * 
	 * @param arqEntrada -
	 *            String com o path do arquivo a ser compactado. O path só podem
	 *            ser do diretório atual ou de subdiretórios do diretório atual.
	 * @param zipSaida -
	 *            path do arquivo compactado a ser criado
	 */
	public void compactar(final String arqEntrada, final String zipSaida) {
		final ArrayList<String> arqEntradas = new ArrayList<String>();
		arqEntradas.add(arqEntrada);
		compactar(arqEntradas, zipSaida);
	}

	/**
	 * Compacta arquivos em um arquivo do tipo zip
	 * 
	 * @param arqEntrada -
	 *            ArrayList com o path dos arquivos a ser compactados. Os paths
	 *            só podem ser do diretório atual ou de subdiretórios do
	 *            diretório atual.
	 * @param zipSaida -
	 *            path do arquivo compactado a ser criado
	 */
	public void compactar(final ArrayList<String> arqEntrada, final String zipSaida) {

		// Create a buffer for reading the files
		final byte[] buf = new byte[1024];
		for (int i = 0; i < arqEntrada.size(); i++) {
			arqEntrada.set(i, arqEntrada.get(i).replace("/", "\\"));
		}
		try {
			// Create the ZIP file
			final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipSaida));

			// Compress the files
			for (int i = 0; i < arqEntrada.size(); i++) {
				arqEntrada.set(i, arqEntrada.get(i).replace("/", "\\"));
				final File f = new File(arqEntrada.get(i));
				if (!f.exists()) {
					continue;
				}
				final FileInputStream in = new FileInputStream(f);

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(f.getPath()));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
		} catch (final IOException e) {
		}
	}

	/**
	 * Descompacta todos os arquivos
	 * 
	 * @param zipSaida -
	 *            path do arquivo compactado já existente
	 */
	public void descompactarTudo(final String zipEntrada) {
		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new FileInputStream(zipEntrada));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {

				if (entry.isDirectory()) {
					// Create a directory; all non-existent ancestor directories
					// are
					// automatically created
					(new File(entry.getName())).mkdirs();
				} else {
					// Open the output file
					final String outFilename = entry.getName();
					final OutputStream out = new FileOutputStream(outFilename);

					// Transfer bytes from the ZIP file to the output file
					final byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.close();
				}
				// Get the next entry
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();
		} catch (final IOException e) {
		}
	}

	/**
	 * Lista todos os arquivos dentro do zip
	 * 
	 * @param zipSaida -
	 *            path do arquivo compactado já existente
	 * @return ArrayList com os nomes dos arquivos dentro do zip
	 */
	public ArrayList<String> Listar(final String zipEntrada) {
		try {
			final ArrayList<String> lista = new ArrayList<String>();
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new FileInputStream(zipEntrada));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {

				lista.add(entry.getName());
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();
			if (lista.size() > 0)
				return lista;
			else
				return null;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Descompacta um arquivo de dentro do arquivo compactado já existente
	 * 
	 * @param arquivo -
	 *            path do arquivo a ser descompactado.
	 * @param zipEntrada -
	 *            path do arquivo compactado já existente
	 */
	public void descompactar(final String zipEntrada, String arquivo) {

		arquivo = arquivo.replace("/", "\\");

		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new FileInputStream(zipEntrada));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {
				if (entry.getName().toUpperCase().contains(arquivo.toUpperCase())) {
					break;
				}
				entry = in.getNextEntry();
			}
			if (entry != null) {
				if (entry.isDirectory()) {
					// Create a directory; all non-existent ancestor directories
					// are
					// automatically created
					(new File(entry.getName())).mkdirs();
				} else {
					if (entry.getName().contains("\\")) {
						(new File(entry.getName())).getParentFile().mkdirs();
					}
					// Open the output file
					final String outFilename = entry.getName();
					final OutputStream out = new FileOutputStream(outFilename);

					// Transfer bytes from the ZIP file to the output file
					final byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.close();
				}

				// Close the streams
				in.close();
			}
		} catch (final IOException e) {
		}
	}

	/**
	 * Adiciona um array de bytes a outro array de bytes compactado já existente
	 * 
	 * @param arqNome -
	 *            String com o nome interno para referência dentro do array
	 *            compactado.
	 * @param arqEntrada -
	 *            byte[] - array de bytes a ser adicionado.
	 * @param zipEntrada -
	 *            Array de bytes compactado já existente.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] adicionarStream(final String arqNome, final byte[] arqEntrada, final byte[] zipEntrada) {
		final ArrayList<String> arqNomes = new ArrayList<String>();
		final ArrayList<byte[]> arqEntradas = new ArrayList<byte[]>();
		arqNomes.add(arqNome);
		arqEntradas.add(arqEntrada);
		return adicionarStream(arqNomes, arqEntradas, zipEntrada);
	}

	/**
	 * Adiciona array de bytes a outro array de bytes compactado já existente
	 * 
	 * @param arqNome -
	 *            ArrayList com os nomes internos para referência dentro do
	 *            array compactado.
	 * @param arqEntrada -
	 *            ArrayList com os arrays de bytes a ser adicionados.
	 * @param zipEntrada -
	 *            Array de bytes compactado já existente.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] adicionarStream(final ArrayList<String> arqNome, final ArrayList<byte[]> arqEntrada, final byte[] zipEntrada) {
		final ArrayList<String> arqNomeExist = new ArrayList<String>();
		final ArrayList<byte[]> arqEntradaExist = new ArrayList<byte[]>();

		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipEntrada));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {
				// Open the output file
				if (arqNome.contains(entry.getName())) {
					entry = in.getNextEntry();
					continue;
				}
				final ByteArrayOutputStream out = new ByteArrayOutputStream();

				// Transfer bytes from the ZIP file to the output file
				final byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();

				arqNomeExist.add(entry.getName());
				arqEntradaExist.add(out.toByteArray());

				// Get the next entry
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();

		} catch (final IOException e) {

		}

		// Create a buffer for reading the files
		final byte[] buf = new byte[1024];
		// ByteBuffer byteBuf = ByteBuffer.;

		try {
			// Create the ZIP file
			final ByteArrayOutputStream outByte = new ByteArrayOutputStream();
			final ZipOutputStream out = new ZipOutputStream(outByte);

			// Compress the files
			for (int i = 0; i < arqEntrada.size(); i++) {
				// File f = new File(arqEntrada[i]);
				// FileInputStream in = new FileInputStream(f);
				final ByteArrayInputStream in = new ByteArrayInputStream(arqEntrada.get(i));
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(arqNome.get(i)));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Compress the files
			for (int i = 0; i < arqEntradaExist.size(); i++) {
				// File f = new File(arqEntrada[i]);
				// FileInputStream in = new FileInputStream(f);
				final ByteArrayInputStream in = new ByteArrayInputStream(arqEntradaExist.get(i));
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(arqNomeExist.get(i)));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
			return outByte.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	/**
	 * Remove um array de bytes de dentro do array de bytes compactado
	 * 
	 * @param arqNome -
	 *            String com o nomes de referência dos array de bytes a ser
	 *            removidos.
	 * @param zipEntrada -
	 *            Array de bytes compactado que contém o array a ser removidos.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] removerStream(final String arqNome, final byte[] zipEntrada) {
		final ArrayList<String> arqNomes = new ArrayList<String>();
		arqNomes.add(arqNome);
		return removerStream(arqNomes, zipEntrada);
	}

	/**
	 * Remove arrays de bytes de dentro do array de bytes compactado
	 * 
	 * @param arqNome -
	 *            ArrayList com os nomes de referência dos arrays de bytes a ser
	 *            removidos.
	 * @param zipEntrada -
	 *            Array de bytes compactado que contém os arrays a ser
	 *            removidos.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] removerStream(final ArrayList<String> arqNome, final byte[] zipEntrada) {
		final ArrayList<String> arqNomeExist = new ArrayList<String>();
		final ArrayList<byte[]> arqEntradaExist = new ArrayList<byte[]>();

		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipEntrada));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {
				// Skip the file to be deleted
				if (arqNome.contains(entry.getName())) {
					entry = in.getNextEntry();
					continue;
				}
				final ByteArrayOutputStream out = new ByteArrayOutputStream();

				// Transfer bytes from the ZIP file to the output file
				final byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();

				arqNomeExist.add(entry.getName());
				arqEntradaExist.add(out.toByteArray());

				// Get the next entry
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();

		} catch (final IOException e) {

		}

		// Create a buffer for reading the files
		final byte[] buf = new byte[1024];
		// ByteBuffer byteBuf = ByteBuffer.;

		try {
			// Create the ZIP file
			final ByteArrayOutputStream outByte = new ByteArrayOutputStream();
			final ZipOutputStream out = new ZipOutputStream(outByte);

			// Compress the files
			for (int i = 0; i < arqEntradaExist.size(); i++) {
				// File f = new File(arqEntrada[i]);
				// FileInputStream in = new FileInputStream(f);
				final ByteArrayInputStream in = new ByteArrayInputStream(arqEntradaExist.get(i));
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(arqNomeExist.get(i)));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
			return outByte.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	/**
	 * Compacta um array de bytes em um outro array de bytes
	 * 
	 * @param arqNome -
	 *            String com o nome interno para referência dentro do array
	 *            compactado.
	 * @param arqEntrada -
	 *            byte[] - array de bytes a ser compactado.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] compactarStream(final String arqNome, final byte[] arqEntrada) {
		final ArrayList<String> arqNomes = new ArrayList<String>();
		final ArrayList<byte[]> arqEntradas = new ArrayList<byte[]>();
		arqNomes.add(arqNome);
		arqEntradas.add(arqEntrada);
		return compactarStream(arqNomes, arqEntradas);
	}

	/**
	 * Compacta arrays de bytes em um outro array de bytes
	 * 
	 * @param arqNome -
	 *            ArrayList com os nomes internos para referência dentro do
	 *            array compactado.
	 * @param arqEntrada -
	 *            ArrayList com os arrays de bytes a ser adicionados.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] compactarStream(final ArrayList<String> arqNome, final ArrayList<byte[]> arqEntrada) {

		// Create a buffer for reading the files
		final byte[] buf = new byte[1024];
		// ByteBuffer byteBuf = ByteBuffer.;

		try {
			// Create the ZIP file
			final ByteArrayOutputStream outByte = new ByteArrayOutputStream();
			final ZipOutputStream out = new ZipOutputStream(outByte);

			// Compress the files
			for (int i = 0; i < arqEntrada.size(); i++) {
				// File f = new File(arqEntrada[i]);
				// FileInputStream in = new FileInputStream(f);
				final ByteArrayInputStream in = new ByteArrayInputStream(arqEntrada.get(i));
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(arqNome.get(i)));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
			return outByte.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	/**
	 * Descompacta todos os arrays de bytes que estão dentro do array de bytes
	 * compactado
	 * 
	 * @param zipEntrada -
	 *            array de bytes compactado
	 * @param nomes -
	 *            ArrayList com os nomes internos de referência.
	 * @aparam aList - ArrayList com os arrays de bytes descompactados.
	 * 
	 */
	public void descompactarTudoStream(final byte[] zipEntrada, ArrayList<String> nomes, ArrayList<byte[]> aList) {
		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipEntrada));
			aList = new ArrayList<byte[]>();
			nomes = new ArrayList<String>();
			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {
				// Open the output file
				// String outFilename = entry.getName();
				// OutputStream out = new FileOutputStream(outFilename);
				final ByteArrayOutputStream out = new ByteArrayOutputStream();

				// Transfer bytes from the ZIP file to the output file
				final byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();

				nomes.add(entry.getName());
				aList.add(out.toByteArray());

				// Get the next entry
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();

		} catch (final IOException e) {

		}
	}

	/**
	 * Lista todos os arrays de bytes que estão dentro do array de bytes
	 * compactado
	 * 
	 * @param zipEntrada -
	 *            array de bytes compactado
	 * @return ArrayList com os nomes internos de referência.
	 * 
	 */
	public ArrayList<String> listarStream(final byte[] zipEntrada) {
		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipEntrada));
			final ArrayList<String> nomes = new ArrayList<String>();
			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {

				nomes.add(entry.getName());
				entry = in.getNextEntry();
			}
			// Close the streams
			in.close();
			if (nomes.size() > 0)
				return nomes;
			else
				return null;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Descompacta um array de bytes que está dentro do array de bytes
	 * compactado
	 * 
	 * @param zipEntrada -
	 *            array de bytes compactado
	 * @param arquivo -
	 *            Nome interno de referência do array de bytes a ser
	 *            descompactado.
	 * @param arqSaida -
	 *            Array de bytes descompactado.
	 * @return byte[] - array de bytes compactado resultante
	 */
	public byte[] descompactarStream(final byte[] zipEntrada, final String arquivo) {
		try {
			// Open the ZIP file
			final ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipEntrada));

			// Get the first entry
			ZipEntry entry = in.getNextEntry();

			while (entry != null) {
				if (entry.getName().toUpperCase().contains(arquivo.toUpperCase())) {
					break;
				}
				entry = in.getNextEntry();
			}
			if (entry != null) {

				// Open the output file
				// String outFilename = entry.getName();
				// OutputStream out = new FileOutputStream(outFilename);
				final ByteArrayOutputStream out = new ByteArrayOutputStream();

				// Transfer bytes from the ZIP file to the output file
				final byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				// Close the streams
				in.close();
				return out.toByteArray();
			}
			return null;
		} catch (final IOException e) {
			return null;
		}
	}

}
