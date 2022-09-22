package br.gov.jfrj.siga.arquivo.controllers;
import java.util.Optional;

import org.apache.http.HttpHeaders;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.gov.jfrj.siga.arquivo.SigaAmazonS3;

@RestController
@RequestMapping("sigaAmazonS3/api/v1")
public class SigaAmazonS3Controller {

	@PostMapping("/uploadIniciar")
    public ResponseEntity<String> uploadIniciar(@RequestParam MultipartFile arquivo, @RequestParam String parms) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
        String respParms = sigaS3.uploadMultipartIniciar(arquivo, parms);
        return ResponseEntity.ok(respParms);
    }
	
	@PostMapping("/uploadEnviarParte")
    public ResponseEntity<String> uploadEnviarParte(@RequestParam MultipartFile arquivo, @RequestParam String parms) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
        String respParms = sigaS3.uploadMultipartEnviarParte(arquivo, parms);
        return ResponseEntity.ok(respParms);
    }

	@PostMapping("/uploadFinalizar")
    public ResponseEntity<String> uploadFinalizar(@RequestParam String parms) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
        String respParms = sigaS3.uploadMultipartFinalizar(parms);
        return ResponseEntity.ok(respParms);
    }

	@PostMapping("/uploadAbortar")
    public ResponseEntity<String> uploadAbortar(@RequestParam String parms) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
        String respParms = sigaS3.uploadMultipartAbortar(parms);
        return ResponseEntity.ok(respParms);
    }

	@GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam String tokenArquivo, @RequestHeader(HttpHeaders.RANGE) Optional<String> range) throws Exception {
		SigaAmazonS3 sigaS3 = new SigaAmazonS3();
		return sigaS3.download(tokenArquivo, range);
    }
	
}
