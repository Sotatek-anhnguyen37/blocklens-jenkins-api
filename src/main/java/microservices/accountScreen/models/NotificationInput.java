package microservices.accountScreen.models;

import lombok.Data;

@Data
public class NotificationInput {
    private boolean notificationEnabled;

    public NotificationInput(boolean notificationEnabled){
        this.notificationEnabled = notificationEnabled;
    }
}
