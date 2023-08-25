package server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;

@SpringBootApplication(exclude = ValidationAutoConfiguration.class,scanBasePackages = "server")

public class DemoProjectHibernateApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoProjectHibernateApplication.class, args);
    }





}
