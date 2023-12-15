package microservices.resetpassword.model;

import lombok.Data;

@Data
public class ForgotPasswordInput {
    private String email;


    public ForgotPasswordInput(String email) {
        this.email = email;
    }
}
