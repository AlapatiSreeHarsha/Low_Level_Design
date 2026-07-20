package Behavioural_Patterns.Chain_of_Responsibility;

// Handler
abstract class SupportHandler {

    protected SupportHandler nextHandler;

    public void setNextHandler(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(String requestType);
}

// Concrete Handler - Technical Support
class TechnicalSupport extends SupportHandler {

    @Override
    public void handleRequest(String requestType) {

        if (requestType.equalsIgnoreCase("Technical")) {
            System.out.println("Technical Support handled the request.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        } else {
            System.out.println("No handler available.");
        }
    }
}

// Concrete Handler - Billing Support
class BillingSupport extends SupportHandler {

    @Override
    public void handleRequest(String requestType) {

        if (requestType.equalsIgnoreCase("Billing")) {
            System.out.println("Billing Support handled the request.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        } else {
            System.out.println("No handler available.");
        }
    }
}

// Concrete Handler - General Support
class GeneralSupport extends SupportHandler {

    @Override
    public void handleRequest(String requestType) {

        if (requestType.equalsIgnoreCase("General")) {
            System.out.println("General Support handled the request.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        } else {
            System.out.println("No handler available.");
        }
    }
}

// Concrete Handler - Delivery Support
class DeliverySupport extends SupportHandler {

    @Override
    public void handleRequest(String requestType) {

        if (requestType.equalsIgnoreCase("Delivery")) {
            System.out.println("Delivery Support handled the request.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        } else {
            System.out.println("No handler available.");
        }
    }
}

// Client
public class Main {

    public static void main(String[] args) {

        SupportHandler technical = new TechnicalSupport();
        SupportHandler billing = new BillingSupport();
        SupportHandler general = new GeneralSupport();
        SupportHandler delivery = new DeliverySupport();

        technical.setNextHandler(billing);
        billing.setNextHandler(general);
        general.setNextHandler(delivery);

        technical.handleRequest("Technical");
        technical.handleRequest("Billing");
        technical.handleRequest("General");
        technical.handleRequest("Delivery");
        technical.handleRequest("Refund");
    }
}
