package microservices.resetpassword.model;

import lombok.Data;

@Data
public class ResetPasswordInput {
    private String newPassword;
    private String resetToken;


    public ResetPasswordInput(String newPassword, String resetToken) {
        this.newPassword = newPassword;
        this.resetToken = resetToken;
    }
}
