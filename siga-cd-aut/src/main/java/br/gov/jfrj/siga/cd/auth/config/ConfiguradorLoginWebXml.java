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
package br.gov.jfrj.siga.cd.auth.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

// import org.jboss.deployers.spi.management.deploy.DeploymentManager // interface

/**
 * Classe responsável por atualizar o arquivo web.xml de um arquivo war de forma
 * a proporcionar a autenticação via certificado ou via formulário.
 * 
 * @author aym
 * 
 */
public class ConfiguradorLoginWebXml {
	private static String CAMINHO_WEBXML_WAR = "WEB-INF/web.xml";
	private static String CAMINHO_JBOSSWEBXML_WAR = "WEB-INF/jboss-web.xml";

	/**
	 * Altera os arquivos necessários dentro do (.war) para o modo informado
	 * OBS: refatorar par aotimizar o código - faz toda operação para cada
	 * caminho dentro do war
	 * 
	 * @param modo
	 *            se configura para formulario ou certificado
	 * @param caminhoWar
	 *            caminho de origem do arquivo (.war)
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void atualizarArquivosXml(final ModoConfigurarLoginWebXml modo,
			final String caminhoWar) throws IOException, InterruptedException {
		atualizarWebXml(modo, caminhoWar, CAMINHO_WEBXML_WAR);
		atualizarWebXml(modo, caminhoWar, CAMINHO_JBOSSWEBXML_WAR);
	}

	/**
	 * Atualiza o arquivo web.xml contido no war para usar certificado ou
	 * formulário.
	 * 
	 * Essa atualização é feita: 1)Renomeando o arquivo de origem (parâmetro
	 * caminhoWar) para .old. 2)Lê as linhas do arquivo web.xml no arquivo
	 * (.old). 3)Troca as linhas necessárias de acordo com o modo (parâmetro) .
	 * 4)Processa o arquivo (.old) em um temporário. 3)Fecha o arquivo (.old).
	 * 4)Renomeia o temporário para o nome original do .war (caminhoWar)
	 * 5)Exclui o arquivo original (.old)
	 * 
	 * @param modo
	 *            se configura para formulario ou certificado
	 * @param caminhoWar
	 *            caminho de origem do arquivo (.war)
	 * @param caminhoNoWar
	 *            o caminho do arquivo no war
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void atualizarWebXml(final ModoConfigurarLoginWebXml modo,
			final String caminhoWar, final String caminhoNoWar)
			throws IOException, InterruptedException {
		moveRename(caminhoWar, caminhoWar + ".old");
		ZipFile war = new ZipFile(caminhoWar + ".old");
		ZipEntry webXml = war.getEntry(caminhoNoWar);
		ArrayList<String> linhas = obterLinhasWebXmlAntes(war, webXml);
		trocar(linhas, modo);
		String temporario = processarWarEmTemporario(caminhoWar + ".old",
				webXml, linhas);
		war.close();
		moveRename(temporario, caminhoWar);
		excluir(caminhoWar + ".old");
	}

	/*
	 * private void atualizarArquivoWar(String caminhoWar, ZipEntry webXml,
	 * ArrayList<String> linhas) throws IOException, InterruptedException {
	 * 
	 * 
	 * String temporario = processarWarEmTemporario(caminhoWar + ".old", webXml,
	 * linhas); String pastaWar = pastaSuperior(caminhoWar);
	 * moveRename(temporario, pastaWar); //moveRename(caminhoWar + + ".old",
	 * caminhoWar.replaceAll("\\.", "-") + ".old"); moveRename(temporario,
	 * caminhoWar); excluir(caminhoWar + ".old"); }
	 */
	/**
	 * Processa o war em arquivo temporário substituindo as linhas do web.xml
	 * pelas linhas (parâmetro) e retornando o nome do arquivo temporário
	 * gerado.
	 * 
	 * @param caminhoWar
	 *            caminho completo do arquivo war de origem
	 * @param webXml
	 *            A entrada referente ao arquivo web.xml
	 * @param linhas
	 * @return o nome do arquivo temporário gerado no processamento
	 * @throws InterruptedException
	 */
	private String processarWarEmTemporario(String caminhoWar, ZipEntry webXml,
			ArrayList<String> linhas) throws InterruptedException {
		File fileWar = new File(caminhoWar);
		if (!fileWar.exists())
			throw new Error("Arquivo '" + caminhoWar + "' não encontrado.");
		String nomePuro = fileWar.getName();
		int posPonto = nomePuro.lastIndexOf('.');
		String prefixo = nomePuro.substring(0, posPonto);
		String sufixo = nomePuro.substring(posPonto + 1, nomePuro.length());
		// Cria o arquivo temporário para copiar as informações do war
		File fileWarNew;
		try {
			fileWarNew = File.createTempFile(prefixo + "-", "." + sufixo);
		} catch (IOException e1) {
			throw new Error("Erro ao criar arquivo temporário '" + prefixo
					+ "-NNNNNN." + sufixo + "':" + e1.getMessage());
		}
		String caminhoWarNew = fileWarNew.getAbsolutePath();
		//
		byte[] buf = new byte[1024];
		// abre o war (zip)
		FileInputStream fin;
		try {
			fin = new FileInputStream(caminhoWar);
		} catch (Exception e) {
			throw new Error("Erro ao criar o FileInputStream para o arquivo '"
					+ caminhoWar + "':" + e.getMessage());
		}
		// abre o output stream para o war (zip) temporário
		ZipInputStream zin = new ZipInputStream(fin);
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(caminhoWarNew);
		} catch (Exception e) {
			throw new Error("Erro ao criar o FileOutputStream para o arquivo '"
					+ caminhoWarNew + "':" + e.getMessage());

		}
		ZipOutputStream zout = new ZipOutputStream(fout);
		// processa a transferência
		ZipEntry entry;
		try {
			while ((entry = zin.getNextEntry()) != null) {
				String name = entry.getName();
				// System.out.println(name);
				String webXmlName = webXml.getName();
				if (webXmlName.equals(name)) {
					zout.putNextEntry(new ZipEntry(webXml.getName()));
					for (String linha : linhas) {
						for (byte b : linha.getBytes()) {
							zout.write(b);
						}
						zout.write('\n');
					}
				} else {
					zout.putNextEntry(new ZipEntry(name));
					int len;
					while ((len = zin.read(buf)) > 0) {
						zout.write(buf, 0, len);
					}
				}
			}
		} catch (Exception e) {
			throw new Error("Erro ao processar o arquivo war '" + caminhoWar
					+ "' para o temporário '" + caminhoWarNew + "':"
					+ e.getMessage());
		} finally {
			try {
				zout.close();
				zin.close();
				fout.close();
				fin.close();
			} catch (Exception e) {
				throw new Error(
						"Erro ao fechar arquivos no processamento do arquivo war '"
								+ caminhoWar + "' para o temporário '"
								+ caminhoWarNew + "':" + e.getMessage());
			}
		}
		fileWarNew.setWritable(true);
		return caminhoWarNew;
	}

	/**
	 * Renomeia (ou move) o arquivo de origem (ori) para o arquivo de destino
	 * (dest)
	 * 
	 * @param ori
	 *            caminho completo do arquivo de origem
	 * @param dest
	 *            caminho completo do arquivo de destino
	 * @throws InterruptedException
	 */
	private void moveRename(String ori, String dest) {
		File flOri = new File(ori);
		File flDes = new File(dest);
		if (flDes.isDirectory()) {
			flDes = new File(flDes.getAbsolutePath() + File.separator
					+ flOri.getName());
		}
		if (!flOri.renameTo(flDes)) {
			throw new Error("Não foi possível renomear o arquivo '" + ori
					+ "' para '" + dest + "'.");
		}
	}

	/**
	 * Excluir um arquivo
	 * 
	 * @param nome
	 * @throws InterruptedException
	 */
	private void excluir(String caminho) throws InterruptedException {
		File fil = new File(caminho);
		if (!fil.delete()) {
			throw new Error("Não foi possível excluir o arquivo '" + caminho
					+ "'.");
		}
	}

	/**
	 * troca as linhas de forma a que o arquivo passe a refletir o modo
	 * solicitado
	 * 
	 * @param linhas
	 * @param modo
	 */
	private void trocar(ArrayList<String> linhas,
			final ModoConfigurarLoginWebXml modo) {
		int tamanho = linhas.size();
		for (int index = 0; index < tamanho; index++) {
			String linha = linhas.get(index);
			String novaLinha = modo.procesarLinha(linha);
			if (!linha.equals(novaLinha))
				linhas.set(index, novaLinha);
		}
	}

	/**
	 * Lê o arquivo web.xml do zip e armazena em um array de linhas
	 * 
	 * @param war
	 * @return
	 * @throws IOException
	 */
	private ArrayList<String> obterLinhasWebXmlAntes(final ZipFile war,
			final ZipEntry webXml) throws IOException {

		InputStream is = war.getInputStream(webXml);
		ArrayList<String> linhas = new ArrayList<String>();
		StringBuffer stb = new StringBuffer();
		int intChr;
		while ((intChr = is.read()) != -1) {
			char chr = (char) intChr;
			if (chr != '\r') {
				if (chr == '\n') {
					linhas.add(stb.toString());
					stb = new StringBuffer();
				} else {
					stb.append(chr);
				}
			}
		}
		if (stb.length() > 0) {
			linhas.add(stb.toString());
		}
		is.close();
		return linhas;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if (args.length < 2 ) {
			throw new Error("-> uso: java - jar siga-cd-aut.jar [modo] [arquivo] ");
		}
		boolean achou = false;
		ModoConfigurarLoginWebXml modoEncontrado = null;
		for (ModoConfigurarLoginWebXml modoLista : ModoConfigurarLoginWebXml.values()) {
			if (modoLista.toString().equalsIgnoreCase(args[0])) {
				achou = true;
				modoEncontrado = modoLista;
				break;
			}
		}
		if (!achou) {
			throw new Error ("primeiro parâmetro tem de ser um modo válido : (" +  ModoConfigurarLoginWebXml.valuesAsString() + ")!");
		}
		ConfiguradorLoginWebXml conf = new ConfiguradorLoginWebXml();
				conf.atualizarArquivosXml(modoEncontrado, args[1] /* "C:\\Users\\aym\\Documents\\siga.war" */);
		System.exit(0);
	}
}
