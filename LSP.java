//A child class should be able to replace its parent class without causing problems.
//Wherever you use a parent object, you should also be able to use any child object.
class Notification{
    public void sendNotification(){
        System.out.println("Sending notification");
    }
}

class EmailNotification extends Notification{
    public void sendNotification(){
        System.out.println("Sending email notification");
    }
}

class SMSNotification extends Notification{
    public void sendNotification(){
        System.out.println("Sending SMS notification");
    }
}


public class LSP {
    public static void main(String[] args) {
        Notification notification1=new EmailNotification();
        notification1.sendNotification();

        Notification notification2=new SMSNotification();
        notification2.sendNotification();
    }
}
