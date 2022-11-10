package pl.pawsko.toolroom;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRoomConfiguration {
    @Bean
    public OpenAPI springToolRoomOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("ToolRoomAPI")
                        .description("API for the ToolRoom Application")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("ToolRoom project page")
                        .url("https://github.com/pawsko/ToolRoom"));
    }
}
