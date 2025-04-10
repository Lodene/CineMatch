package fr.cpe.cinematch_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinematchBackendApplication {

	public static void main(String[] args) {
		System.out.println("🚀 Lancement de l'application Spring Boot...");
		SpringApplication.run(CinematchBackendApplication.class, args);
	}

}
