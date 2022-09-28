package br.gov.jfrj.siga.arquivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SigaArqApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigaArqApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/sigaAmazonS3/api/**")
					.allowedOrigins(System.getProperty("siga.allowed.origin.urls"))
                	.allowCredentials(true)
	                .allowedMethods("OPTIONS","POST","PUT","GET","DELETE","HEAD")
	                .allowedHeaders("Cookie","Content-Disposition","content-type","parms","tokenArquivo")
	                .maxAge(3600);
			}
		};
	}	

}
