package microservices.signup.model;

import lombok.Data;

@Data
public class SignupInput {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public SignupInput(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public SignupInput(String password, String firstName, String lastName) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public SignupInput() {
    }
}

