package microservices.resendemail.model;

import lombok.Data;

@Data
public class ResendInput {
    private String email;

    public ResendInput(String email) {
        this.email = email;
    }
    public ResendInput() {
    }
}
