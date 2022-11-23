package pl.pawsko.toolroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.pawsko.toolroom.user.nosql.UserRepositoryNosql;

@EnableMongoRepositories(basePackageClasses = UserRepositoryNosql.class)
@EnableMongoAuditing
@Configuration
public class MongoDbConfig {
}
