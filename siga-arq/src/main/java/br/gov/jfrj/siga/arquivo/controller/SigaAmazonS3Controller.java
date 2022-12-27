package br.gov.jfrj.siga.arquivo.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.gov.jfrj.siga.arquivo.ArqApiV1Servlet;
import br.gov.jfrj.siga.arquivo.SigaAmazonS3;

@RestController
@RequestMapping("api/v1")
public class SigaAmazonS3Controller {
	private ArqApiV1Servlet servlet;
	SigaAmazonS3Controller() throws Exception {
		servlet = new ArqApiV1Servlet();
	}
	
	@PostMapping("/upload")
    public ResponseEntity<String> upload(HttpServletRequest request, @RequestHeader String parms) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
		try {
			String respParms = sigaS3.upload(request.getInputStream(), parms);
	        return ResponseEntity.ok(respParms);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
	
	@GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam String tokenArquivo) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
		return sigaS3.download(tokenArquivo);
    }
	
	@DeleteMapping("/remover")
    public ResponseEntity<String> remover(@RequestHeader String tokenArquivo) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
		sigaS3.remover(tokenArquivo);
		return ResponseEntity.ok("OK");
    }
	
	@GetMapping("/test")
    public ResponseEntity<String> test(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		servlet.doGet(req, res);
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
		sigaS3.conectar();
		return ResponseEntity.ok("{" + System.lineSeparator()
				+ "  \"service\": \"sigaarq\"," + System.lineSeparator()
				+ "  \"url\": \"/sigaarq/api/v1/test\"" + System.lineSeparator()
				+ "}");
    }
	
}