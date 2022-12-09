package pl.pawsko.toolroom.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class DbConfig {
    @Value("${pl.pawsko.toolroom.database}")
    private String dataBase;

}
