package microservices.signin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Array;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private User user;
    @Data
    public class User {
        private String email;
        private String firstName;
        private String lastName;
    }
}
