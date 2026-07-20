package Behavioural_Patterns.Mediator_Pattern;

import java.util.*;

// Mediator Interface
interface DocumentSessionMediator {

    void broadcastChange(String change, User sender);

    void join(User user);
}

// Concrete Mediator
class CollaborativeDocument implements DocumentSessionMediator {

    private List<User> users;

    public CollaborativeDocument() {
        users = new ArrayList<>();
    }

    @Override
    public void join(User user) {
        users.add(user);
        System.out.println(user.getName() + " joined the document.");
    }

    @Override
    public void broadcastChange(String change, User sender) {

        for (User user : users) {
            if (user != sender) {
                user.receiveChange(change, sender);
            }
        }
    }
}

// Colleague
class User {

    private String name;
    private DocumentSessionMediator mediator;

    public User(String name, DocumentSessionMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public String getName() {
        return name;
    }

    public void makeChange(String change) {
        System.out.println(name + " made change: " + change);
        mediator.broadcastChange(change, this);
    }

    public void receiveChange(String change, User sender) {
        System.out.println(name + " received change from "
                + sender.getName() + ": " + change);
    }
}

// Client
public class Main {

    public static void main(String[] args) {

        DocumentSessionMediator mediator = new CollaborativeDocument();

        User u1 = new User("Harsha", mediator);
        User u2 = new User("Rahul", mediator);
        User u3 = new User("Anjali", mediator);

        mediator.join(u1);
        mediator.join(u2);
        mediator.join(u3);

        System.out.println();

        u1.makeChange("Added Introduction");
        System.out.println();

        u2.makeChange("Fixed Grammar");
        System.out.println();

        u3.makeChange("Added Conclusion");
    }
}