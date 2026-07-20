import java.util.HashMap;

interface EmailTemplate {
    void setContent(String content);
    void sendEmail();
    EmailTemplate clone();
}

class WelcomeEmailTemplate implements EmailTemplate {
    private String userName;

    @Override
    public void setContent(String userName) {
        this.userName = userName;
    }

    @Override
    public void sendEmail() {
        String content = "Hi " + userName + ", Welcome!";
        System.out.println("Sending Welcome Email with content: " + content);
    }

    @Override
    public EmailTemplate clone() {
        WelcomeEmailTemplate clone = new WelcomeEmailTemplate();
        clone.setContent(this.userName);
        return clone;
    }
}

class EmailRegistry {
    private final HashMap<String,EmailTemplate> emailTemplates = new HashMap<>();

    public void registerTemplate(String templateName, EmailTemplate template) {
        emailTemplates.put(templateName, template);
    }

    public EmailTemplate getTemplate(String templateName) {
        EmailTemplate template = emailTemplates.get(templateName);
        return template != null ? template.clone() : null;
    }
}

public class EmailTemplateRegistry {
    public static void main(String[] args) {
        EmailRegistry registry = new EmailRegistry();

        WelcomeEmailTemplate welcomeTemplate = new WelcomeEmailTemplate();
        registry.registerTemplate("welcome", welcomeTemplate);

        EmailTemplate email1 = registry.getTemplate("welcome");
        email1.setContent("Harsha");
        email1.sendEmail();

        EmailTemplate email2 = registry.getTemplate("welcome");
        email2.setContent("Sam");
        email2.sendEmail();
    }
}