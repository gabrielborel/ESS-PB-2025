package al.infnet.edu.br.books_management;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
public class BooksManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksManagementApiApplication.class, args);
	}

}
