package Creational_Patterns.Abstract_Factory_Pattern;
interface PaymentGateway { // Product interface for processing payments
    void processPayment(double amount);
}

class PayPalPaymentGateway implements PaymentGateway { // Concrete product: PayPal payment processor
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through PayPal.");
    }
}

class StripePaymentGateway implements PaymentGateway { // Concrete product: Stripe payment processor
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through Stripe.");
    }
}

class RazorpayPaymentGateway implements PaymentGateway { // Concrete product: Razorpay payment processor
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through Razorpay.");
    }
}

interface InvoiceGenerator { // Product interface for generating invoices
    void generateInvoice(double amount);
}

class USInvoice implements InvoiceGenerator { // Concrete product: US-style invoice
    public void generateInvoice(double amount) {
        System.out.println("Generating invoice for $" + amount + " in the US.");
    }
}

class GSTInvoice implements InvoiceGenerator { // Concrete product: India GST invoice
    public void generateInvoice(double amount) {
        System.out.println("Generating GST invoice for $" + amount + " in India.");
    }
}

interface RegionalPaymentGatewayFactory { // Abstract factory declaring creation methods for related products
    PaymentGateway createPaymentGateway();
    InvoiceGenerator createInvoiceGenerator();
}

class USPaymentGateway implements RegionalPaymentGatewayFactory { // Concrete factory producing US payment gateway + invoice
    private PaymentGateway paymentGateway;
    private InvoiceGenerator invoiceGenerator;

    public USPaymentGateway(String mode) {
        if (mode.equalsIgnoreCase("paypal")) {
            this.paymentGateway = new PayPalPaymentGateway();
        } else if (mode.equalsIgnoreCase("stripe")) {
            this.paymentGateway = new StripePaymentGateway();
        }
        this.invoiceGenerator = new USInvoice();
    }

    public PaymentGateway createPaymentGateway() {
        return paymentGateway;
    }

    public InvoiceGenerator createInvoiceGenerator() {
        return invoiceGenerator;
    }
}

class IndiaPaymentGateway implements RegionalPaymentGatewayFactory { // Concrete factory producing India payment gateway + invoice
    private PaymentGateway paymentGateway;
    private InvoiceGenerator invoiceGenerator;

    public IndiaPaymentGateway(String mode) {
        if (mode.equalsIgnoreCase("paypal")) {
            this.paymentGateway = new PayPalPaymentGateway();
        } else if (mode.equalsIgnoreCase("razorpay")) {
            this.paymentGateway = new RazorpayPaymentGateway();
        }
        this.invoiceGenerator = new GSTInvoice();
    }

    public PaymentGateway createPaymentGateway() {
        return paymentGateway;
    }

    public InvoiceGenerator createInvoiceGenerator() {
        return invoiceGenerator;
    }
}

class Checkout_Service { // Client that uses a factory without knowing concrete product classes
    private PaymentGateway paymentGateway;
    private InvoiceGenerator invoiceGenerator;

    public Checkout_Service(RegionalPaymentGatewayFactory factory) {
        this.paymentGateway = factory.createPaymentGateway();
        this.invoiceGenerator = factory.createInvoiceGenerator();
    }

    public void checkout(double amount) {
        paymentGateway.processPayment(amount);
        invoiceGenerator.generateInvoice(amount);
    }

    public static void main(String[] args) {
        Checkout_Service usCheckout = new Checkout_Service(new USPaymentGateway("stripe"));
        usCheckout.checkout(100.0);

        Checkout_Service indiaCheckout = new Checkout_Service(new IndiaPaymentGateway("razorpay"));
        indiaCheckout.checkout(100.0);
    }
}