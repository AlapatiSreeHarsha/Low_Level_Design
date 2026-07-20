package Behavioural_Patterns.Template_Pattern;

// Template Class
abstract class NotificationSender {

    // Template Method
    public final void send(String recipient) {

        rateLimitCheck(recipient);

        validateRecipient(recipient);

        String message = composeMessage(recipient);

        formatMessage(message);

        preSendAuditLog(recipient, message);

        sendMessage(recipient, message);

        postSendAnalysis(recipient);
    }

    public void rateLimitCheck(String recipient) {
        System.out.println("Rate limit check for " + recipient);
    }

    public void validateRecipient(String recipient) {
        System.out.println("Validating recipient: " + recipient);
    }

    public void formatMessage(String message) {
        System.out.println("Formatting message: " + message);
    }

    public void preSendAuditLog(String recipient, String message) {
        System.out.println("Audit Log -> Recipient: " + recipient +
                ", Message: " + message);
    }

    // Steps implemented by subclasses
    protected abstract String composeMessage(String recipient);

    protected abstract void sendMessage(String recipient, String message);

    public void postSendAnalysis(String recipient) {
        System.out.println("Post-send analysis completed for " + recipient);
    }
}

// Concrete Class - Email
class EmailNotification extends NotificationSender {

    @Override
    protected String composeMessage(String recipient) {
        return "Hello " + recipient + ", this is your Email Notification.";
    }

    @Override
    protected void sendMessage(String recipient, String message) {
        System.out.println("Sending EMAIL to " + recipient);
        System.out.println(message);
    }
}

// Concrete Class - SMS
class SMSNotification extends NotificationSender {

    @Override
    protected String composeMessage(String recipient) {
        return "Hi " + recipient + ", this is your SMS Notification.";
    }

    @Override
    protected void sendMessage(String recipient, String message) {
        System.out.println("Sending SMS to " + recipient);
        System.out.println(message);
    }
}

// Driver
public class Main {

    public static void main(String[] args) {

        NotificationSender email = new EmailNotification();

        System.out.println("=== Email Notification ===");
        email.send("harsha@gmail.com");

        System.out.println();

        NotificationSender sms = new SMSNotification();

        System.out.println("=== SMS Notification ===");
        sms.send("9876543210");
    }
}