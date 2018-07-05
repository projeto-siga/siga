package br.gov.jfrj.siga.tp.vraptor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BundleConverter {

	private static final String FILE_PATH = "C:\\devel\\projetos\\siga-transportes\\sigatp\\src\\main\\resources\\conf\\messages.properties";

	public static void main(String[] args) throws IOException {
		FileReader f = new FileReader(FILE_PATH);
		BufferedReader bufferedReader = new BufferedReader(f);
		String linha = null;

		StringBuilder resultado = new StringBuilder();

		while ((linha = bufferedReader.readLine()) != null) {
			int i = 0;
			while (linha.contains("%s")) {
				linha = linha.replaceFirst("%s", "{" + i + "}");
				i++;
			}
			resultado.append(linha);
			resultado.append("\n");
		}
		bufferedReader.close();

		BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
		bw.write(resultado.toString());
		bw.flush();
		bw.close();
	}
}
