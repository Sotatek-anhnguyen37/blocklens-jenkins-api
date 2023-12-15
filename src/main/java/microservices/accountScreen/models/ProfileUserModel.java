package microservices.accountScreen.models;

import lombok.Data;

@Data
public class ProfileUserModel {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String address;
    private String name;
    private Object payment;
    private long balance;
    private SettingModel setting;
    private boolean isPaymentMethodIntegrated;
    private Object stripe;
    private String[] authProviders;

    @Data
    public static class SettingModel{
        private String billingEmail;
        private boolean notificationEnabled;
    }
}
