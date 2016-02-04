package br.gov.jfrj.siga.base.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoveUTF8BOM {
	public static void main(String[] args) throws IOException {
		listFiles(Paths.get("/Users/nato/Repositories/projeto-siga/siga"));
	}

	static void listFiles(Path path) throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					listFiles(entry);
				}
				process(entry);
			}
		}
	}

	private static void process(Path path) throws IOException {
		String fileName = path.getFileName().toString();
		String filePathName = path.toString();

		if (filePathName.contains("siga-play-module"))
			return;
		if (filePathName.contains("/codigo-fonte/"))
			return;
		if (filePathName.contains("/siga-documentacao/"))
			return;

		if (!(fileName.endsWith(".java") || fileName.endsWith(".jsp") || fileName
				.endsWith(".tag")))
			return;

		// System.out.println(" - " + directory);
		byte[] readAllBytes = Files.readAllBytes(path);

		if (readAllBytes.length > 3 && readAllBytes[0] == -17
				&& readAllBytes[1] == -69 && readAllBytes[2] == -65) {
			System.out.println("UTF-8 BOM - " + filePathName);
			byte[] bytes = new byte[readAllBytes.length - 3];
			System.arraycopy(readAllBytes, 3, bytes, 0, readAllBytes.length - 3);
			Files.write(path, bytes);
		} else {
			// System.out.println(filePathName);
		}

	}

}
