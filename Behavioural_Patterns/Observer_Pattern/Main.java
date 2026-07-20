package Behavioural_Patterns.Observer_Pattern;

import java.util.*;

// Observer Interface
interface Subscriber {
    void update(String videoTitle);
}

// Concrete Observer - Email Subscriber
class EmailSubscriber implements Subscriber {
    private String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void update(String videoTitle) {
        System.out.println("Email sent to " + email +
                ": New video uploaded - " + videoTitle);
    }
}

// Concrete Observer - Mobile App Subscriber
class MobileAppSubscriber implements Subscriber {
    private String username;

    public MobileAppSubscriber(String username) {
        this.username = username;
    }

    @Override
    public void update(String videoTitle) {
        System.out.println("Mobile notification to " + username +
                ": New video uploaded - " + videoTitle);
    }
}

// Subject Interface
interface Channel {
    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void notifySubscribers(String videoTitle);
}

// Concrete Subject
class YouTubeChannel implements Channel {
    private List<Subscriber> subscribers;
    private String channelName;

    public YouTubeChannel(String channelName) {
        this.channelName = channelName;
        subscribers = new ArrayList<>();
    }

    public void uploadVideo(String title) {
        System.out.println("\n" + channelName +
                " uploaded a new video: " + title);
        notifySubscribers(title);
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(String videoTitle) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(videoTitle);
        }
    }
}

// Driver
public class Main {
    public static void main(String[] args) {

        YouTubeChannel channel = new YouTubeChannel("CodeWithHarsha");

        Subscriber s1 = new EmailSubscriber("harsha@gmail.com");
        Subscriber s2 = new MobileAppSubscriber("SreeHarsha");
        Subscriber s3 = new EmailSubscriber("abc@gmail.com");

        channel.subscribe(s1);
        channel.subscribe(s2);
        channel.subscribe(s3);

        channel.uploadVideo("Observer Design Pattern");

        channel.unsubscribe(s3);

        channel.uploadVideo("Iterator Design Pattern");
    }
}
