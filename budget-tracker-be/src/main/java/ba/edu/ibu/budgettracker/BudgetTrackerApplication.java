package ba.edu.ibu.budgettracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgetTrackerApplication {

	public static void main(String[] args) {
		try {
			// Attempt to load the .env file
			Dotenv dotenv = Dotenv.configure().load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		} catch (Exception e) {
			// Fallback if .env is not found
			System.out.println("Could not load .env file. Falling back to system environment variables.");
		}

		SpringApplication.run(BudgetTrackerApplication.class, args);
	}
}
