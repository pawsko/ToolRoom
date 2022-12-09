package pl.pawsko.toolroom.user.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepositoryNosql extends MongoRepository<UserNoSql, Long> {
}
