package moe.pine.est;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String... args) {
        AppInitializer.run();
        SpringApplication.run(App.class, args);
    }
}
