package Structural_Patterns.Adapter_Pattern;
interface PaymentGateway {
    void pay(int orderId, double amount);
}

class PayUGateway implements PaymentGateway {

    @Override
    public void pay(int orderId, double amount) {
        System.out.println("Paying " + amount + " for order " + orderId + " using PayU Gateway");
    }
}

class RazorpayAPI {

    public void makePayment(int orderId, double amount) {
        System.out.println("Paying " + amount + " for order " + orderId + " using Razorpay API");
    }
}

class RazorpayAdapter implements PaymentGateway {

    private RazorpayAPI razorpayAPI;

    public RazorpayAdapter() {
        razorpayAPI = new RazorpayAPI();
    }

    @Override
    public void pay(int orderId, double amount) {
        razorpayAPI.makePayment(orderId, amount);
    }
}

class Checkout {

    private PaymentGateway paymentGateway;

    public Checkout(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void processPayment(int orderId, double amount) {
        paymentGateway.pay(orderId, amount);
    }
}

public class Main {

    public static void main(String[] args) {

        Checkout checkout1 = new Checkout(new PayUGateway());
        checkout1.processPayment(101, 999.0);

        Checkout checkout2 = new Checkout(new RazorpayAdapter());
        checkout2.processPayment(102, 1499.0);
    }
}