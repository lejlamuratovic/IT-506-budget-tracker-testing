package ba.edu.ibu.budgettracker.rest.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "IBU AI Software Engineering",
                version = "1.0.0",
                description = "AI Software Engineering Backend Application",
                contact = @Contact(name = "Lejla Muratovic", email = "lejla.muratovic@stu.ibu.edu.ba")
        ),
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)

public class SwaggerConfiguration {

}
