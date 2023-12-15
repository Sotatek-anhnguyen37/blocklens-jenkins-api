package microservices.signin.model;

import lombok.Data;

@Data
public class SignInInput {
    private String email;
    private String password;

    public SignInInput(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public SignInInput(String email) {
        this.email = email;
    }
    public SignInInput() {
    }

}
