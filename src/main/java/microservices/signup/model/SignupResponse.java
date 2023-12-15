package microservices.signup.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
}
