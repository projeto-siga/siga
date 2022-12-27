package br.gov.jfrj.siga.arquivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.gov.jfrj.siga.base.Prop;

@SpringBootApplication
public class SigaArqApplication extends SpringBootServletInitializer  {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		return builder.sources(SigaArqApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SigaArqApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				for (String url: Prop.get("/sigaarq.allowed.origin.urls").split(","))
					System.out.println("CORS: " + url);
				registry.addMapping("/api/**")
					.allowedOrigins(Prop.get("/sigaarq.allowed.origin.urls").split(","))
                	.allowCredentials(true)
	                .allowedMethods("OPTIONS","POST","PUT","GET","DELETE","HEAD")
	                .allowedHeaders("Cookie","Content-Disposition","content-type","parms","tokenArquivo")
	                .maxAge(3600);
			}
		};
	}	

}
