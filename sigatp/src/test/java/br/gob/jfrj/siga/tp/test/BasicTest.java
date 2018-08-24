package br.gob.jfrj.siga.tp.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import br.gov.jfrj.siga.tp.validation.RenavamConstraintValidator;
import br.gov.jfrj.siga.validation.EmailConstraintValidator;

public class BasicTest {
	@Test
	public void retornarEmailsValidos() {
	    EmailConstraintValidator validador = new EmailConstraintValidator();

		String[] emailsValidos = { "mkyong@yahoo.com", "mkyong-100@yahoo.com", "mkyong.100@yahoo.com", "mkyong111@mkyong.com", "mkyong-100@mkyong.net", "mkyong.100@mkyong.com.au", "mkyong@1.com",
				"mkyong@gmail.com.com", "mkyong+100@gmail.com", "mkyong-100@yahoo-test.com" };

		String[] emailsInvalidos = { "mkyong", "mkyong@.com.my", "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com", ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
				"mkyong..2002@gmail.com", "mkyong.@gmail.com", "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a" };

		for (String email : emailsValidos) {
			boolean valid = validador.validarEmail(email, false);
			System.out.println("Email is valid : " + email + " , " + valid);
			Assert.assertEquals(valid, true);
		}

		for (String email : emailsInvalidos) {
			boolean valid = validador.validarEmail(email, false);
			System.out.println("Email is valid : " + email + " , " + valid);
			Assert.assertEquals(valid, false);
		}

		boolean valid = validador.validarEmail("", false);
		System.out.println("Email is valid : " + valid);
		Assert.assertEquals(valid, false);
	}

	@Test
	public void testaGeraRenavam() throws IOException {
		RenavamConstraintValidator validador = new RenavamConstraintValidator();

		Boolean ok = false;
		Boolean renavamOk;
		String nomeArquivo = "src//test//java//lista_renavam.txt";
		String linha = "";
		StringBuilder mostra = new StringBuilder();
		int totrenavamOk = 0;
		int totrenavamNotOk = 0;

		File arqLeitura = new File(nomeArquivo);

		if (arqLeitura.exists()) {
			FileReader fr = new FileReader(nomeArquivo);
			BufferedReader br = new BufferedReader(fr);

			try {
				while (true) {
					linha = br.readLine();

					if (linha == null) 
						break;

					mostra.append(linha).append(" ");
				}

				String[] renavam = mostra.toString().split(" ");
				String texto = "";

				File arquivo;
				arquivo = new File("src//test//java//resultado.txt");
				FileOutputStream fos = new FileOutputStream(arquivo);

				for (String valor : renavam) {
					renavamOk = validador.validarRenavam(valor);
					texto = valor + (renavamOk ? " ok" : " not ok") + "\n";
					fos.write(texto.getBytes());
					totrenavamOk = renavamOk ? totrenavamOk + 1 : totrenavamOk;
					totrenavamNotOk = !renavamOk ? totrenavamNotOk + 1 : totrenavamNotOk;
				}

				texto = "Total de renavam : " + renavam.length + "\n";
				texto += "Total de renavam ok : " + totrenavamOk + "\n";
				texto += "Total de renavam not ok : " + totrenavamNotOk;
				fos.write(texto.getBytes());
				fos.close();

				ok = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				br.close();
			}

			Assert.assertTrue(ok);
		}
	}

	/*
	 * @Test public void testaGeraRenavam11Digitos() { RenavamCheck validador = new RenavamCheck(); Assert.assertTrue(validador.validarRenavam("83251476778"));
	 * 
	 * Boolean ok = false; Boolean cnhOk; String nomeArquivo = "..//sigatp//test//listaCnh.txt"; String linha = ""; String mostra = ""; int totcnhsOk = 0; int totcnhsNotOk = 0;
	 * 
	 * }
	 * 
	 * @Test public void testaGeraRenavam9Digitos() { RenavamCheck validador = new RenavamCheck(); Assert.assertTrue(validador.validarRenavam("00481563857")); }
	 */
	/*
	 * @Test public void testaValidadorCnh() { CnhCheck validador = new CnhCheck();
	 * 
	 * Boolean ok = false; Boolean cnhOk; String nomeArquivo = "..//sigatp//test//listaCnh.txt"; String linha = ""; String mostra = ""; int totcnhsOk = 0; int totcnhsNotOk = 0;
	 * 
	 * File arqLeitura = new File(nomeArquivo);
	 * 
	 * if (arqLeitura.exists()) { try { FileReader fr = new FileReader(nomeArquivo); BufferedReader br = new BufferedReader(fr);
	 * 
	 * while(true) { linha = br.readLine();
	 * 
	 * if (linha==null) { break; }
	 * 
	 * mostra += linha; }
	 * 
	 * String[] cnhs = mostra.split(" "); String texto="";
	 * 
	 * File arquivo; arquivo = new File("..//sigatp//test//resultado.txt"); FileOutputStream fos = new FileOutputStream(arquivo);
	 * 
	 * for (String cnh : cnhs) { cnhOk = validador.validarCnh(cnh); texto = cnh + (cnhOk ? " ok" : " not ok") + "\n"; fos.write(texto.getBytes()); totcnhsOk = cnhOk ? totcnhsOk + 1 : totcnhsOk;
	 * totcnhsNotOk = !cnhOk ? totcnhsNotOk + 1 : totcnhsNotOk; }
	 * 
	 * texto = "Total de cnhs : " + cnhs.length + "\n"; texto += "Total de cnhs ok : " + totcnhsOk + "\n"; texto += "Total de cnhs not ok : " + totcnhsNotOk; fos.write(texto.getBytes()); fos.close();
	 * 
	 * ok = true; } catch (Exception e) { System.out.println(e.getMessage()); } }
	 * 
	 * Assert.assertTrue(ok); }
	 */
}