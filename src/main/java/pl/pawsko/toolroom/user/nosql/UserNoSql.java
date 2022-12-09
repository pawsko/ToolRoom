package pl.pawsko.toolroom.user.nosql;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Data
@Document(collection = "users")
public class UserNoSql {

        @Id
        private Long id;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;
        private int rating;
        @CreatedDate
        private Date created;
}
